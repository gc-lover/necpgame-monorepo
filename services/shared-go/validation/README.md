# Input Validation and Sanitization Library

## Overview

Enterprise-grade input validation and sanitization library for SQL injection, XSS, and CSRF protection. Designed for MMOFPS games requiring secure input handling across all services.

## Issue: #2154

## Features

### 1. SQL Injection Protection
- Pattern-based detection of SQL injection attempts
- Automatic rejection of dangerous SQL patterns
- Support for common SQL injection vectors

### 2. XSS Protection
- HTML tag detection and sanitization
- JavaScript event handler detection
- Script tag removal
- Iframe and SVG attack prevention

### 3. CSRF Protection
- Token-based validation
- Customizable token validator
- Request validation support

### 4. Input Sanitization
- HTML escaping
- Null byte removal
- Whitespace trimming
- URL validation

### 5. Validation Rules
- Length validation (min/max)
- Pattern matching (regex)
- Required field validation
- Alphanumeric validation
- Email validation
- UUID validation

## Usage

### Basic Usage

```go
import "necpgame/services/shared-go/validation"

// Create validator
v := validation.NewValidator()

// Validate and sanitize input
result := v.ValidateString(ctx, "player_name", playerName,
    &validation.RequiredRule{},
    &validation.LengthRule{Min: 3, Max: 20},
    &validation.AlphanumericRule{},
)

if !result.Valid {
    return result.Errors[0]
}

// Use sanitized value
sanitizedName := result.Sanitized
```

### Common Validation Functions

```go
// Validate player name
if err := validation.ValidatePlayerName(name); err != nil {
    return err
}

// Validate email
if err := validation.ValidateEmail(email); err != nil {
    return err
}

// Validate UUID
if err := validation.ValidateUUID(uuid); err != nil {
    return err
}
```

### Custom Rules

```go
// Create custom pattern rule
patternRule := &validation.PatternRule{
    Pattern: regexp.MustCompile(`^[a-zA-Z0-9_]+$`),
    Message: "Value must contain only alphanumeric characters and underscores",
}

result := v.ValidateString(ctx, "username", username,
    &validation.RequiredRule{},
    &validation.LengthRule{Min: 3, Max: 30},
    patternRule,
)
```

### CSRF Protection

```go
// Create CSRF protection
csrfProtection := validation.NewCSRFProtection(func(ctx context.Context, token string) bool {
    // Validate token against session or database
    return validateTokenFromSession(ctx, token)
})

// Validate CSRF token
if err := csrfProtection.ValidateToken(ctx, csrfToken); err != nil {
    return err
}
```

### URL Sanitization

```go
sanitizedURL, err := v.SanitizeURL(rawURL)
if err != nil {
    return err
}
```

## Integration

This library can be used in all Go services:

```go
// In handler.go
func (h *Handler) CreatePlayer(ctx context.Context, req CreatePlayerRequest) error {
    // Validate player name
    if err := validation.ValidatePlayerName(req.Name); err != nil {
        return err
    }

    // Validate email
    if err := validation.ValidateEmail(req.Email); err != nil {
        return err
    }

    // Validate and sanitize description
    v := validation.NewValidator()
    description, err := v.ValidateAndSanitize(ctx, "description", req.Description,
        &validation.LengthRule{Min: 0, Max: 500},
    )
    if err != nil {
        return err
    }

    // Use sanitized values
    // ...
}
```

## Security Best Practices

1. **Always validate on server**: Client-side validation is not enough
2. **Sanitize before storage**: Sanitize all user input before storing in database
3. **Use prepared statements**: This library complements but doesn't replace prepared statements
4. **Validate CSRF tokens**: Always validate CSRF tokens for state-changing operations
5. **Log validation failures**: Log failed validation attempts for security monitoring

## Performance

- **Pattern matching**: Pre-compiled regex patterns for fast detection
- **Early exit**: Stops validation on first error
- **Minimal allocations**: Efficient string operations
- **Thread-safe**: Safe for concurrent use

## Migration

To migrate existing validation code:

1. Replace custom validation with `validation.ValidateString()`
2. Use common validation functions (`ValidatePlayerName`, `ValidateEmail`, etc.)
3. Remove duplicate SQL injection/XSS checks
4. Integrate CSRF protection where needed
