// Input Validation Tests
// Issue: #2154

package validation

import (
	"context"
	"testing"
)

func TestValidator_SQLInjection(t *testing.T) {
	v := NewValidator()
	
	sqlInjectionAttempts := []string{
		"'; DROP TABLE users; --",
		"' OR '1'='1",
		"' UNION SELECT * FROM users --",
		"'; EXEC xp_cmdshell 'dir' --",
		"admin'--",
		"admin'/*",
	}

	for _, attempt := range sqlInjectionAttempts {
		result := v.ValidateString(context.Background(), "test", attempt)
		if result.Valid {
			t.Errorf("SQL injection attempt should be rejected: %s", attempt)
		}
		if len(result.Errors) == 0 || result.Errors[0].Code != "SQL_INJECTION" {
			t.Errorf("Expected SQL_INJECTION error for: %s", attempt)
		}
	}
}

func TestValidator_XSS(t *testing.T) {
	v := NewValidator()
	
	xssAttempts := []string{
		"<script>alert('XSS')</script>",
		"<iframe src='javascript:alert(1)'></iframe>",
		"<img src='javascript:alert(1)'>",
		"<svg onload=alert(1)>",
		"javascript:alert(1)",
		"onclick=alert(1)",
	}

	for _, attempt := range xssAttempts {
		result := v.ValidateString(context.Background(), "test", attempt)
		if result.Valid {
			t.Errorf("XSS attempt should be rejected: %s", attempt)
		}
		if len(result.Errors) == 0 || result.Errors[0].Code != "XSS" {
			t.Errorf("Expected XSS error for: %s", attempt)
		}
	}
}

func TestValidator_SanitizeString(t *testing.T) {
	v := NewValidator()
	
	tests := []struct {
		input    string
		expected string
	}{
		{"<script>alert(1)</script>", "&lt;script&gt;alert(1)&lt;/script&gt;"},
		{"Hello & World", "Hello &amp; World"},
		{"  test  ", "test"},
		{"test\x00null", "testnull"},
	}

	for _, test := range tests {
		result := v.SanitizeString(test.input)
		if result != test.expected {
			t.Errorf("SanitizeString(%q) = %q, expected %q", test.input, result, test.expected)
		}
	}
}

func TestValidator_LengthRule(t *testing.T) {
	v := NewValidator()
	
	// Too short
	result := v.ValidateString(context.Background(), "test", "ab", &LengthRule{Min: 3, Max: 10})
	if result.Valid {
		t.Error("Should reject string that is too short")
	}

	// Too long
	result = v.ValidateString(context.Background(), "test", "abcdefghijklmnop", &LengthRule{Min: 3, Max: 10})
	if result.Valid {
		t.Error("Should reject string that is too long")
	}

	// Valid
	result = v.ValidateString(context.Background(), "test", "abcde", &LengthRule{Min: 3, Max: 10})
	if !result.Valid {
		t.Error("Should accept string within length limits")
	}
}

func TestValidator_EmailRule(t *testing.T) {
	v := NewValidator()
	
	validEmails := []string{
		"test@example.com",
		"user.name@domain.co.uk",
		"user+tag@example.com",
	}

	invalidEmails := []string{
		"invalid",
		"@example.com",
		"test@",
		"test@.com",
		"test @example.com",
	}

	for _, email := range validEmails {
		result := v.ValidateString(context.Background(), "email", email, &EmailRule{})
		if !result.Valid {
			t.Errorf("Should accept valid email: %s", email)
		}
	}

	for _, email := range invalidEmails {
		result := v.ValidateString(context.Background(), "email", email, &EmailRule{})
		if result.Valid {
			t.Errorf("Should reject invalid email: %s", email)
		}
	}
}

func TestValidator_UUIDRule(t *testing.T) {
	v := NewValidator()
	
	validUUIDs := []string{
		"550e8400-e29b-41d4-a716-446655440000",
		"550E8400-E29B-41D4-A716-446655440000",
	}

	invalidUUIDs := []string{
		"invalid",
		"550e8400-e29b-41d4-a716",
		"550e8400-e29b-41d4-a716-446655440000-extra",
	}

	for _, uuid := range validUUIDs {
		result := v.ValidateString(context.Background(), "uuid", uuid, &UUIDRule{})
		if !result.Valid {
			t.Errorf("Should accept valid UUID: %s", uuid)
		}
	}

	for _, uuid := range invalidUUIDs {
		result := v.ValidateString(context.Background(), "uuid", uuid, &UUIDRule{})
		if result.Valid {
			t.Errorf("Should reject invalid UUID: %s", uuid)
		}
	}
}

func TestValidatePlayerName(t *testing.T) {
	validNames := []string{
		"Player123",
		"TestUser",
		"ABC",
	}

	invalidNames := []string{
		"ab",           // Too short
		"a",            // Too short
		"",             // Empty
		"Player Name", // Contains space
		"Player-Name", // Contains dash
		"Player_Name", // Contains underscore
		"<script>",    // XSS
		"'; DROP",     // SQL injection
	}

	for _, name := range validNames {
		if err := ValidatePlayerName(name); err != nil {
			t.Errorf("Should accept valid player name: %s, error: %v", name, err)
		}
	}

	for _, name := range invalidNames {
		if err := ValidatePlayerName(name); err == nil {
			t.Errorf("Should reject invalid player name: %s", name)
		}
	}
}

func TestValidateEmail(t *testing.T) {
	validEmails := []string{
		"test@example.com",
		"user@domain.co.uk",
	}

	invalidEmails := []string{
		"invalid",
		"@example.com",
		"test@",
		"<script>@example.com",
	}

	for _, email := range validEmails {
		if err := ValidateEmail(email); err != nil {
			t.Errorf("Should accept valid email: %s, error: %v", email, err)
		}
	}

	for _, email := range invalidEmails {
		if err := ValidateEmail(email); err == nil {
			t.Errorf("Should reject invalid email: %s", email)
		}
	}
}
