package server

import (
	"github.com/gorilla/mux"
	"github.com/necpgame/achievement-service-go/pkg/api"
)

func HandlerFromMux(si api.ServerInterface, r *mux.Router) {
	wrapper := &api.ServerInterfaceWrapper{
		Handler: si,
	}

	r.HandleFunc("/achievements", wrapper.GetAchievements).Methods("GET")
	r.HandleFunc("/achievements/{achievementId}", wrapper.GetAchievement).Methods("GET")
}




