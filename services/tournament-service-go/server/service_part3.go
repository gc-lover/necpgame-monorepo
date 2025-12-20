w.Header().Set("Content-Type", "application/json")
json.NewEncoder(w).Encode(resp)

s.logger.WithField("match_id", match.MatchID).Info("match result updated successfully")
}

// Ranking Handlers
func (s *TournamentService) GetRankings(w http.ResponseWriter, r *http.Request) {
leaderboardType := r.URL.Query().Get("leaderboard_type")
gameModeFilter := r.URL.Query().Get("game_mode")
limit := 50

var rankings []*PlayerRanking
s.rankings.Range(func(key, value interface{}) bool {
	ranking := value.(*PlayerRanking)

	if gameModeFilter != "" {
		if _, ok := ranking.GameModeStats[gameModeFilter]; !ok {
			return true
		}
	}

	rankings = append(rankings, ranking)
	return true
})

// Sort rankings by rating descending
sort.Slice(rankings, func(i, j int) bool {
	return rankings[i].Rating > rankings[j].Rating
})

// Apply limit
if len(rankings) > limit {
	rankings = rankings[:limit]
}

resp := s.rankingResponsePool.Get().(*GetRankingsResponse)
defer s.rankingResponsePool.Put(resp)

resp.LeaderboardType = leaderboardType
resp.Rankings = rankings
resp.TotalCount = len(rankings)
resp.GeneratedAt = time.Now().Unix()

w.Header().Set("Content-Type", "application/json")
json.NewEncoder(w).Encode(resp)
}

func (s *TournamentService) GetPlayerRanking(w http.ResponseWriter, r *http.Request) {
playerID := chi.URLParam(r, "playerId")

rankingValue, exists := s.rankings.Load(playerID)
if !exists {
	http.Error(w, "Player ranking not found", http.StatusNotFound)
	return
}

ranking := rankingValue.(*PlayerRanking)

resp := s.rankingResponsePool.Get().(*GetPlayerRankingResponse)
defer s.rankingResponsePool.Put(resp)

resp.PlayerID = playerID
resp.Rankings = map[string]*PlayerRanking{
	"overall": ranking,
}
resp.OverallStats = &ranking.OverallStats

w.Header().Set("Content-Type", "application/json")
json.NewEncoder(w).Encode(resp)
}

// League Handlers
func (s *TournamentService) ListLeagues(w http.ResponseWriter, r *http.Request) {
statusFilter := r.URL.Query().Get("status")

var leagues []*LeagueSummary
s.leagues.Range(func(key, value interface{}) bool {
	league := value.(*League)

	if statusFilter != "" && league.Status != statusFilter {
		return true
	}

	summary := &LeagueSummary{
		LeagueID:      league.LeagueID,
		Name:          league.Name,
		GameMode:      league.GameMode,
		Status:        league.Status,
		CurrentSeason: league.CurrentSeason,
		TeamCount:     league.CurrentTeams,
		MaxTeams:      league.MaxTeams,
		SeasonEndTime: league.SeasonEndTime.Unix(),
		Region:        league.Region,
	}
	leagues = append(leagues, summary)
	return true
})

resp := s.leagueResponsePool.Get().(*ListLeaguesResponse)
defer s.leagueResponsePool.Put(resp)

resp.Leagues = leagues
resp.TotalCount = len(leagues)

w.Header().Set("Content-Type", "application/json")
json.NewEncoder(w).Encode(resp)
}

func (s *TournamentService) CreateLeague(w http.ResponseWriter, r *http.Request) {
var req CreateLeagueRequest
if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
	s.logger.WithError(err).Error("failed to decode create league request")
	http.Error(w, "Invalid request body", http.StatusBadRequest)
	return
}

league := &League{
	LeagueID:        uuid.New().String(),
	Name:            req.Name,
	Description:     req.Description,
	GameMode:        req.GameMode,
	Status:          "active",
	CurrentSeason:   1,
	MaxTeams:        req.MaxTeams,
	CurrentTeams:    0,
	SeasonStartTime: time.Unix(req.SeasonStartTime, 0),
	SeasonEndTime:   time.Unix(req.SeasonEndTime, 0),
	Rules:           req.Rules,
	Prizes:          req.Prizes,
	Region:          req.Region,
	CreatedAt:       time.Now(),
	UpdatedAt:       time.Now(),
}

s.leagues.Store(league.LeagueID, league)
s.metrics.ActiveLeagues.Inc()

resp := s.leagueResponsePool.Get().(*CreateLeagueResponse)
defer s.leagueResponsePool.Put(resp)

resp.LeagueID = league.LeagueID
resp.Name = league.Name
resp.Status = league.Status
resp.CurrentSeason = league.CurrentSeason
resp.CreatedAt = league.CreatedAt.Unix()

w.Header().Set("Content-Type", "application/json")
w.WriteHeader(http.StatusCreated)
json.NewEncoder(w).Encode(resp)

s.logger.WithField("league_id", league.LeagueID).Info("league created successfully")
}

func (s *TournamentService) JoinLeague(w http.ResponseWriter, r *http.Request) {
leagueID := chi.URLParam(r, "leagueId")

var req JoinLeagueRequest
if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
	s.logger.WithError(err).Error("failed to decode join league request")
	http.Error(w, "Invalid request body", http.StatusBadRequest)
	return
}

leagueValue, exists := s.leagues.Load(leagueID)
if !exists {
	http.Error(w, "League not found", http.StatusNotFound)
	return
}
league := leagueValue.(*League)

if league.Status != "active" {
	http.Error(w, "League is not active for joining", http.StatusConflict)
	return
}
if league.CurrentTeams >= league.MaxTeams {
	http.Error(w, "League is full", http.StatusConflict)
	return
}

teamID := uuid.New().String()
leagueTeam := &LeagueTeam{
	TeamID:    teamID,
	Name:      req.TeamName,
	MemberIDs: []string{req.PlayerID},
	CaptainID: req.PlayerID,
	Rating:    1000, // Default ELO
	CreatedAt: time.Now(),
}
league.Teams.Store(teamID, leagueTeam)
league.CurrentTeams++
s.leagues.Store(league.LeagueID, league)

resp := s.leagueResponsePool.Get().(*JoinLeagueResponse)
defer s.leagueResponsePool.Put(resp)

resp.LeagueID = leagueID
resp.PlayerID = req.PlayerID
resp.TeamID = teamID
resp.JoinedAt = time.Now().Unix()
resp.CurrentRank = league.CurrentTeams

w.Header().Set("Content-Type", "application/json")
json.NewEncoder(w).Encode(resp)

s.logger.WithFields(logrus.Fields{
	"league_id": leagueID,
	"player_id": req.PlayerID,
}).Info("player joined league")
}

// Reward Handlers
func (s *TournamentService) GetTournamentRewards(w http.ResponseWriter, r *http.Request) {
tournamentID := chi.URLParam(r, "tournamentId")
playerID := r.URL.Query().Get("player_id")

tournamentValue, exists := s.tournaments.Load(tournamentID)
if !exists {
	http.Error(w, "Tournament not found", http.StatusNotFound)
	return
}
tournament := tournamentValue.(*Tournament)

var availableRewards []Reward
var claimedRewards []string

// Logic to determine player's position and corresponding rewards
if tournament.Status == "completed" {
	for i, prize := range tournament.Prizes {
		reward := Reward{
			RewardID:      fmt.Sprintf("%s-prize-%d", tournamentID, i),
			Position:      prize.Position,
			RewardType:    prize.RewardType,
			RewardValue:   prize.RewardValue,
			Description:   prize.Description,
			Claimed:       false,
			ClaimDeadline: time.Now().Add(7 * 24 * time.Hour).Unix(),
		}
		availableRewards = append(availableRewards, reward)
	}
}

resp := s.rewardResponsePool.Get().(*GetTournamentRewardsResponse)
defer s.rewardResponsePool.Put(resp)

resp.TournamentID = tournamentID
resp.PlayerID = playerID
resp.AvailableRewards = availableRewards
resp.ClaimedRewards = claimedRewards

w.Header().Set("Content-Type", "application/json")
json.NewEncoder(w).Encode(resp)
}

func (s *TournamentService) ClaimRewards(w http.ResponseWriter, r *http.Request) {
var req ClaimRewardsRequest
if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
	s.logger.WithError(err).Error("failed to decode claim rewards request")
	http.Error(w, "Invalid request body", http.StatusBadRequest)
	return
}

// Logic to process reward claims
s.metrics.RewardClaims.Inc()

resp := s.rewardResponsePool.Get().(*ClaimRewardsResponse)
defer s.rewardResponsePool.Put(resp)

resp.TournamentID = req.TournamentID
resp.PlayerID = req.PlayerID
resp.ClaimedRewards = req.RewardIDs
resp.TotalValue = 1000 // Placeholder
resp.ClaimedAt = time.Now().Unix()

w.Header().Set("Content-Type", "application/json")
json.NewEncoder(w).Encode(resp)

s.logger.WithFields(logrus.Fields{
	"tournament_id": req.TournamentID,
	"player_id":     req.PlayerID,
	"rewards":       req.RewardIDs,
}).Info("rewards claimed successfully")
}

// Statistics Handler
func (s *TournamentService) GetGlobalStatistics(w http.ResponseWriter, r *http.Request) {
timeRange := r.URL.Query().Get("time_range")
if timeRange == "" {
	timeRange = "month"
}

resp := s.statsResponsePool.Get().(*GetGlobalStatisticsResponse)
defer s.statsResponsePool.Put(resp)

resp.TimeRange = timeRange
resp.TotalTournaments = int(s.metrics.CompletedTournaments)
resp.ActiveTournaments = int(s.metrics.ActiveTournaments)
resp.TotalParticipants = int(s.metrics.TotalParticipants)
resp.TotalMatchesPlayed = int(s.metrics.ActiveMatches)
resp.AverageTournamentSize = 50.5 // Placeholder
resp.PopularGameModes = []GameModeStats{
	{GameMode: "Deathmatch", TournamentsCount: 500, ParticipantsCount: 25000},
	{GameMode: "CaptureTheFlag", TournamentsCount: 300, ParticipantsCount: 15000},
}
resp.RegionActivity = map[string]int{"North America": 20000, "Europe": 15000, "Asia": 10000}
resp.GeneratedAt = time.Now().Unix()

w.Header().Set("Content-Type", "application/json")
json.NewEncoder(w).Encode(resp)
}

// Background processes
func (s *TournamentService) tournamentScheduler() {
ticker := time.NewTicker(s.config.TournamentUpdateInterval)
defer ticker.Stop()

for range ticker.C {
	s.logger.Debug("running tournament scheduler")
	s.tournaments.Range(func(key, value interface{}) bool {
		tournament := value.(*Tournament)
		now := time.Now()

		// Transition from draft to registration_open
		if tournament.Status == "draft" && now.After(tournament.RegistrationStart) {
			tournament.Status = "registration_open"
			s.logger.WithField("tournament_id", tournament.TournamentID).Info("tournament registration is now open")
			s.saveTournamentToRedis(context.Background(), tournament)
		}

		// Transition from registration_open to in_progress and generate bracket
		if tournament.Status == "registration_open" && now.After(tournament.RegistrationEnd) {
			if tournament.CurrentParticipants >= tournament.MinParticipants {
				tournament.Status = "in_progress"
				tournament.Bracket = s.generateBracket(tournament)
				tournament.CurrentRound = 1
				tournament.TotalRounds = s.calculateTotalRounds(tournament.CurrentParticipants)
				s.logger.WithField("tournament_id", tournament.TournamentID).Info("tournament started and bracket generated")
			} else {
				tournament.Status = "cancelled"
				s.logger.WithField("tournament_id", tournament.TournamentID).Warn("tournament cancelled due to insufficient participants")
			}
			s.saveTournamentToRedis(context.Background(), tournament)
		}

		// Transition from in_progress to completed
		if tournament.Status == "in_progress" && s.isTournamentCompleted(tournament) {
			tournament.Status = "completed"
			s.logger.WithField("tournament_id", tournament.TournamentID).Info("tournament completed")
			s.saveTournamentToRedis(context.Background(), tournament)
			s.distributePrizes(context.Background(), tournament)
		}

		return true
	})
}
}

func (s *TournamentService) matchMonitor() {
ticker := time.NewTicker(s.config.MatchUpdateInterval)
defer ticker.Stop()

for range ticker.C {
	s.logger.Debug("running match monitor")
	s.matches.Range(func(key, value interface{}) bool {
		match := value.(*Match)
		now := time.Now()

		// Auto-start scheduled matches
		if match.Status == "scheduled" && now.After(match.ScheduledTime) {
			match.Status = "in_progress"
			match.StartedTime = now
			s.logger.WithField("match_id", match.MatchID).Info("match started automatically")
			s.saveMatchToRedis(context.Background(), match)
		}

		// Handle match timeouts
		if match.Status == "in_progress" {
			tournamentValue, exists := s.tournaments.Load(match.TournamentID)
			if exists {
				tournament := tournamentValue.(*Tournament)
				if tournament.MatchTimeout > 0 && now.After(match.StartedTime.Add(tournament.MatchTimeout)) {
					match.Status = "completed"
					match.CompletedTime = now
					match.WinnerID = "system_timeout"
					s.logger.WithField("match_id", match.MatchID).Warn("match timed out")
					s.saveMatchToRedis(context.Background(), match)
					s.updateTournamentProgress(context.Background(), tournament.TournamentID)
				}
			}
		}
		return true
	})
}
}

func (s *TournamentService) rankingUpdater() {
ticker := time.NewTicker(s.config.RankingUpdateInterval)
defer ticker.Stop()
