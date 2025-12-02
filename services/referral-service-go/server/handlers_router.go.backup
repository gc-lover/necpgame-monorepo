package server

import (
	"github.com/gorilla/mux"
	"github.com/necpgame/referral-service-go/pkg/api"
)

func HandlerFromMux(si api.ServerInterface, r *mux.Router) {
	wrapper := &api.ServerInterfaceWrapper{
		Handler: si,
	}

	r.HandleFunc("/growth/referral/code", wrapper.GetReferralCode).Methods("GET")
	r.HandleFunc("/growth/referral/code/generate", wrapper.GenerateReferralCode).Methods("POST")
	r.HandleFunc("/growth/referral/code/{code}/validate", wrapper.ValidateReferralCode).Methods("GET")
	r.HandleFunc("/growth/referral/events/{player_id}", wrapper.GetReferralEvents).Methods("GET")
	r.HandleFunc("/growth/referral/leaderboard", wrapper.GetReferralLeaderboard).Methods("GET")
	r.HandleFunc("/growth/referral/leaderboard/{player_id}/position", wrapper.GetLeaderboardPosition).Methods("GET")
	r.HandleFunc("/growth/referral/milestones/{milestone_id}/claim", wrapper.ClaimMilestoneReward).Methods("POST")
	r.HandleFunc("/growth/referral/milestones/{player_id}", wrapper.GetReferralMilestones).Methods("GET")
	r.HandleFunc("/growth/referral/register", wrapper.RegisterWithCode).Methods("POST")
	r.HandleFunc("/growth/referral/rewards/distribute", wrapper.DistributeReferralRewards).Methods("POST")
	r.HandleFunc("/growth/referral/rewards/history/{player_id}", wrapper.GetRewardHistory).Methods("GET")
	r.HandleFunc("/growth/referral/stats/public/{code}", wrapper.GetPublicReferralStats).Methods("GET")
	r.HandleFunc("/growth/referral/stats/{player_id}", wrapper.GetReferralStats).Methods("GET")
	r.HandleFunc("/growth/referral/status/{player_id}", wrapper.GetReferralStatus).Methods("GET")
}














