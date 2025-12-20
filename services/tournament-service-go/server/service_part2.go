}
if start > len(tournaments) {
start = len(tournaments)
}
paginatedTournaments := tournaments[start:end]

resp := s.tournamentResponsePool.Get().(*ListTournamentsResponse)
defer s.tournamentResponsePool.Put(resp)

resp.Tournaments = paginatedTournaments
resp.TotalCount = len(tournaments)

w.Header().Set("Content-Type", "application/json")
json.NewEncoder(w).Encode(resp)
}

func (s *TournamentService) GetTournament(w http.ResponseWriter, r *http.Request) {
tournamentID := chi.URLParam(r, "tournamentId")

tournamentValue, exists := s.tournaments.Load(tournamentID)
if !exists {
http.Error(w, "Tournament not found", http.StatusNotFound)
return
}

tournament := tournamentValue.(*Tournament)

resp := s.tournamentResponsePool.Get().(*GetTournamentResponse)
defer s.tournamentResponsePool.Put(resp)

resp.Tournament = &TournamentDetails{
TournamentID:        tournament.TournamentID,
Name:                tournament.Name,
Description:         tournament.Description,
GameMode:            tournament.GameMode,
TournamentFormat:    tournament.Format,
Status:              tournament.Status,
ParticipantCount:    tournament.CurrentParticipants,
MaxParticipants:     tournament.MaxParticipants,
MinParticipants:     tournament.MinParticipants,
RegistrationStartTime: tournament.RegistrationStart.Unix(),
RegistrationEndTime: tournament.RegistrationEnd.Unix(),
StartTime:           tournament.StartTime.Unix(),
EndTime:             tournament.EndTime.Unix(),
Rules:               tournament.Rules,
Prizes:              tournament.Prizes,
EntryFee:            &tournament.EntryFee,
Visibility:          tournament.Visibility,
RegionRestrictions:  tournament.RegionRestrictions,
SkillRequirements:   &tournament.SkillRequirements,
AutoProgression:     tournament.AutoProgression,
MatchTimeout:        int(tournament.MatchTimeout.Seconds()),
AllowSpectators:     tournament.AllowSpectators,
StreamingEnabled:    tournament.StreamingEnabled,
CurrentRound:        tournament.CurrentRound,
TotalRounds:         tournament.TotalRounds,
CreatedAt:           tournament.CreatedAt.Unix(),
UpdatedAt:           tournament.UpdatedAt.Unix(),
}

w.Header().Set("Content-Type", "application/json")
json.NewEncoder(w).Encode(resp)
}

func (s *TournamentService) UpdateTournament(w http.ResponseWriter, r *http.Request) {
tournamentID := chi.URLParam(r, "tournamentId")

tournamentValue, exists := s.tournaments.Load(tournamentID)
if !exists {
http.Error(w, "Tournament not found", http.StatusNotFound)
return
}

tournament := tournamentValue.(*Tournament)
var req UpdateTournamentRequest
if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
s.logger.WithError(err).Error("failed to decode update tournament request")
http.Error(w, "Invalid request body", http.StatusBadRequest)
return
}

updatedFields := []string{}
if req.Name != "" {
tournament.Name = req.Name
updatedFields = append(updatedFields, "name")
}
if req.Description != "" {
tournament.Description = req.Description
updatedFields = append(updatedFields, "description")
}
if req.Status != "" {
tournament.Status = req.Status
updatedFields = append(updatedFields, "status")
}
// Handle other updatable fields...

tournament.UpdatedAt = time.Now()
s.tournaments.Store(tournament.TournamentID, tournament)
s.saveTournamentToRedis(r.Context(), tournament)

resp := s.tournamentResponsePool.Get().(*UpdateTournamentResponse)
defer s.tournamentResponsePool.Put(resp)

resp.TournamentID = tournament.TournamentID
resp.UpdatedFields = updatedFields
resp.UpdatedAt = tournament.UpdatedAt.Unix()

w.Header().Set("Content-Type", "application/json")
json.NewEncoder(w).Encode(resp)

s.logger.WithField("tournament_id", tournament.TournamentID).Info("tournament updated successfully")
}

func (s *TournamentService) CancelTournament(w http.ResponseWriter, r *http.Request) {
tournamentID := chi.URLParam(r, "tournamentId")

tournamentValue, exists := s.tournaments.Load(tournamentID)
if !exists {
http.Error(w, "Tournament not found", http.StatusNotFound)
return
}

tournament := tournamentValue.(*Tournament)
tournament.Status = "cancelled"
tournament.UpdatedAt = time.Now()
s.tournaments.Store(tournament.TournamentID, tournament)
s.saveTournamentToRedis(r.Context(), tournament)

s.metrics.ActiveTournaments.Dec()
s.metrics.CompletedTournaments.Inc()

w.WriteHeader(http.StatusNoContent)
s.logger.WithField("tournament_id", tournament.TournamentID).Info("tournament cancelled successfully")
}

func (s *TournamentService) RegisterForTournament(w http.ResponseWriter, r *http.Request) {
tournamentID := chi.URLParam(r, "tournamentId")

var req RegisterTournamentRequest
if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
s.logger.WithError(err).Error("failed to decode register tournament request")
http.Error(w, "Invalid request body", http.StatusBadRequest)
return
}

tournamentValue, exists := s.tournaments.Load(tournamentID)
if !exists {
http.Error(w, "Tournament not found", http.StatusNotFound)
return
}
tournament := tournamentValue.(*Tournament)

if tournament.Status != "registration_open" {
http.Error(w, "Tournament registration is not open", http.StatusConflict)
return
}
if tournament.CurrentParticipants >= tournament.MaxParticipants {
http.Error(w, "Tournament is full", http.StatusConflict)
return
}

// Check if player already registered
var alreadyRegistered bool
s.registrations.Range(func (key, value interface{}) bool {
reg := value.(*Registration)
if reg.TournamentID == tournamentID && reg.PlayerID == req.PlayerID {
alreadyRegistered = true
return false
}
return true
})
if alreadyRegistered {
http.Error(w, "Player already registered for this tournament", http.StatusConflict)
return
}

registration := &Registration{
RegistrationID: uuid.New().String(),
TournamentID:   tournamentID,
PlayerID:       req.PlayerID,
TeamID:         req.TeamID,
RegisteredAt:   time.Now(),
Status:         "confirmed",
Seed:           0,
}

s.registrations.Store(registration.RegistrationID, registration)
tournament.CurrentParticipants++
s.tournaments.Store(tournament.TournamentID, tournament)
s.saveTournamentToRedis(r.Context(), tournament)

s.metrics.TotalParticipants.Inc()

resp := s.tournamentResponsePool.Get().(*RegisterTournamentResponse)
defer s.tournamentResponsePool.Put(resp)

resp.TournamentID = tournamentID
resp.PlayerID = req.PlayerID
resp.Status = registration.Status
resp.JoinedAt = registration.RegisteredAt.Unix()

w.Header().Set("Content-Type", "application/json")
json.NewEncoder(w).Encode(resp)

s.logger.WithFields(logrus.Fields{
"tournament_id": tournamentID,
"player_id":     req.PlayerID,
}).Info("player registered for tournament")
}

func (s *TournamentService) UnregisterFromTournament(w http.ResponseWriter, r *http.Request) {
tournamentID := chi.URLParam(r, "tournamentId")
playerID := r.URL.Query().Get("player_id")

if playerID == "" {
http.Error(w, "Player ID is required", http.StatusBadRequest)
return
}

tournamentValue, exists := s.tournaments.Load(tournamentID)
if !exists {
http.Error(w, "Tournament not found", http.StatusNotFound)
return
}
tournament := tournamentValue.(*Tournament)

var registrationIDToDelete string
s.registrations.Range(func (key, value interface{}) bool {
reg := value.(*Registration)
if reg.TournamentID == tournamentID && reg.PlayerID == playerID {
registrationIDToDelete = reg.RegistrationID
return false
}
return true
})

if registrationIDToDelete == "" {
http.Error(w, "Player not registered for this tournament", http.StatusNotFound)
return
}

s.registrations.Delete(registrationIDToDelete)
tournament.CurrentParticipants--
s.tournaments.Store(tournament.TournamentID, tournament)
s.saveTournamentToRedis(r.Context(), tournament)

s.metrics.TotalParticipants.Dec()

resp := s.tournamentResponsePool.Get().(*UnregisterTournamentResponse)
defer s.tournamentResponsePool.Put(resp)

resp.TournamentID = tournamentID
resp.PlayerID = playerID
resp.UnregisteredAt = time.Now().Unix()
resp.RefundProcessed = true

w.Header().Set("Content-Type", "application/json")
json.NewEncoder(w).Encode(resp)

s.logger.WithFields(logrus.Fields{
"tournament_id": tournamentID,
"player_id":     playerID,
}).Info("player unregistered from tournament")
}

func (s *TournamentService) GetTournamentBracket(w http.ResponseWriter, r *http.Request) {
tournamentID := chi.URLParam(r, "tournamentId")

tournamentValue, exists := s.tournaments.Load(tournamentID)
if !exists {
http.Error(w, "Tournament not found", http.StatusNotFound)
return
}
tournament := tournamentValue.(*Tournament)

resp := s.tournamentResponsePool.Get().(*GetTournamentBracketResponse)
defer s.tournamentResponsePool.Put(resp)

resp.TournamentID = tournamentID
resp.CurrentRound = tournament.CurrentRound
resp.TotalRounds = tournament.TotalRounds
resp.Matches = tournament.Bracket

w.Header().Set("Content-Type", "application/json")
json.NewEncoder(w).Encode(resp)
}

// Match Management Handlers
func (s *TournamentService) ListMatches(w http.ResponseWriter, r *http.Request) {
tournamentIDFilter := r.URL.Query().Get("tournament_id")
playerIDFilter := r.URL.Query().Get("player_id")
statusFilter := r.URL.Query().Get("status")

var matches []*Match
s.matches.Range(func (key, value interface{}) bool {
match := value.(*Match)

if tournamentIDFilter != "" && match.TournamentID != tournamentIDFilter {
return true
}
if playerIDFilter != "" && match.Player1ID != playerIDFilter && match.Player2ID != playerIDFilter {
return true
}
if statusFilter != "" && match.Status != statusFilter {
return true
}

matches = append(matches, match)
return true
})

resp := s.matchResponsePool.Get().(*ListMatchesResponse)
defer s.matchResponsePool.Put(resp)

resp.Matches = matches
resp.TotalCount = len(matches)

w.Header().Set("Content-Type", "application/json")
json.NewEncoder(w).Encode(resp)
}

func (s *TournamentService) GetMatch(w http.ResponseWriter, r *http.Request) {
matchID := chi.URLParam(r, "matchId")

matchValue, exists := s.matches.Load(matchID)
if !exists {
http.Error(w, "Match not found", http.StatusNotFound)
return
}

match := matchValue.(*Match)

resp := s.matchResponsePool.Get().(*GetMatchResponse)
defer s.matchResponsePool.Put(resp)

resp.Match = &MatchDetail{
MatchID:       match.MatchID,
TournamentID:  match.TournamentID,
Round:         match.Round,
Player1:       &PlayerInfo{PlayerID: match.Player1ID, DisplayName: "Player 1"},
Player2:       &PlayerInfo{PlayerID: match.Player2ID, DisplayName: "Player 2"},
Winner:        &PlayerInfo{PlayerID: match.WinnerID, DisplayName: "Winner"},
Status:        match.Status,
ScheduledTime: match.ScheduledTime.Unix(),
StartedTime:   match.StartedTime.Unix(),
CompletedTime: match.CompletedTime.Unix(),
Score:         &match.Score,
SpectatorsCount: len(match.Spectators),
StreamingURL:  match.StreamingURL,
Metadata:      match.Metadata,
}

w.Header().Set("Content-Type", "application/json")
json.NewEncoder(w).Encode(resp)
}

func (s *TournamentService) UpdateMatchResult(w http.ResponseWriter, r *http.Request) {
matchID := chi.URLParam(r, "matchId")

matchValue, exists := s.matches.Load(matchID)
if !exists {
http.Error(w, "Match not found", http.StatusNotFound)
return
}
match := matchValue.(*Match)

var req UpdateMatchResultRequest
if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
s.logger.WithError(err).Error("failed to decode update match result request")
http.Error(w, "Invalid request body", http.StatusBadRequest)
return
}

match.Score = req.Score
match.WinnerID = req.WinnerID
match.Status = "completed"
match.CompletedTime = time.Now()
s.matches.Store(match.MatchID, match)

s.metrics.MatchCreations.Inc()

// Update tournament progress and player rankings
s.updateTournamentProgress(r.Context(), match.TournamentID)
s.updatePlayerRankings(r.Context(), match.Player1ID, match.Player2ID, match.WinnerID)

resp := s.matchResponsePool.Get().(*UpdateMatchResultResponse)
defer s.matchResponsePool.Put(resp)

resp.MatchID = match.MatchID
resp.TournamentID = match.TournamentID
resp.WinnerID = match.WinnerID
resp.LoserID = ""
if match.Player1ID == match.WinnerID {
resp.LoserID = match.Player2ID
} else {
resp.LoserID = match.Player1ID
}
resp.UpdatedAt = time.Now().Unix()
resp.NextMatchScheduled = true
resp.TournamentProgressUpdated = true
