package server

import (
	"context"
	"net/http"
	"time"

	"github.com/go-faster/jx"
	"github.com/rs/zerolog/log"

	"github.com/gc-lover/necpgame-monorepo/services/battle-pass-core-service-go/pkg/api"
)

// HTTPServer ...
type HTTPServer struct {
	httpSrv *http.Server
}

func NewHTTPServer(addr string, handlers *Handlers, middlewares ...func(http.Handler) http.Handler) *HTTPServer {
	mux := http.NewServeMux()

	var handler http.Handler = nil

	openapiServer, err := api.NewServer(handlers, handlers,
		api.WithErrorHandler(func(ctx context.Context, w http.ResponseWriter, r *http.Request, err error) {
			resp := handlers.NewError(ctx, err)
			w.Header().Set("Content-Type", "application/json")
			w.WriteHeader(http.StatusInternalServerError)
			enc := jx.GetEncoder()
			defer jx.PutEncoder(enc)
			resp.Encode(enc)
			_, _ = enc.WriteTo(w)
		}),
		api.WithNotFound(http.NotFound),
	)
	if err != nil {
		log.Fatal().Err(err).Msg("Failed to create OpenAPI server")
	}

	handler = openapiServer

	// Применяем middleware (в том числе внешние)
	handler = chainMiddlewares(handler, middlewares...)
	mux.Handle("/api/v1/", handler)

	httpSrv := &http.Server{
		Addr:              addr,
		Handler:           mux,
		ReadHeaderTimeout: 5 * time.Second,
		WriteTimeout:      10 * time.Second,
		IdleTimeout:       120 * time.Second,
	}

	return &HTTPServer{httpSrv: httpSrv}
}

func chainMiddlewares(h http.Handler, mws ...func(http.Handler) http.Handler) http.Handler {
	for i := len(mws) - 1; i >= 0; i-- {
		h = mws[i](h)
	}
	return h
}

func (s *HTTPServer) Start() error {
	log.Info().Str("addr", s.httpSrv.Addr).Msg("Starting HTTP server")
	return s.httpSrv.ListenAndServe()
}

func (s *HTTPServer) Shutdown(ctx context.Context) error {
	log.Info().Msg("Shutting down HTTP server")
	return s.httpSrv.Shutdown(ctx)
}
