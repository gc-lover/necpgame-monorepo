// Issue: #1595
package server

// Repository handles data access
type Repository struct{}

// NewRepository creates new repository
func NewRepository() *Repository {
	return &Repository{}
}
