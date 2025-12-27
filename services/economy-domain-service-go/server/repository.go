package server

import (
    "context"
    "database/sql"
    "time"

    _ "github.com/lib/pq"
    "go.uber.org/zap"
)

type Repository struct {
    db    *sql.DB
    logger *zap.Logger
}

func NewRepository() *Repository {
    logger, _ := zap.NewProduction()
    return &Repository{
        logger: logger,
    }
}

func (r *Repository) InitDB(dsn string) error {
    var err error
    r.db, err = sql.Open("postgres", dsn)
    if err != nil {
        return err
    }

    r.db.SetMaxOpenConns(25)
    r.db.SetMaxIdleConns(25 / 2)
    r.db.SetConnMaxLifetime(time.Hour)

    return r.db.Ping()
}

func (r *Repository) HealthCheck(ctx context.Context) error {
    if r.db == nil {
        return sql.ErrNoRows
    }
    return r.db.PingContext(ctx)
}
