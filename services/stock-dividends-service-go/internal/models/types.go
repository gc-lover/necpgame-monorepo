package models

// AuthorType represents the type of user
type AuthorType string

const (
	AuthorTypeCustomer AuthorType = "customer"
	AuthorTypeAgent    AuthorType = "agent"
	AuthorTypeSystem   AuthorType = "system"
)