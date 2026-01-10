package clients

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"time"

	"go.uber.org/zap"
)

// HTTPClient provides a base HTTP client for service-to-service communication
type HTTPClient struct {
	baseURL    string
	httpClient *http.Client
	logger     *zap.Logger
}

// NewHTTPClient creates a new HTTP client instance
func NewHTTPClient(baseURL string, logger *zap.Logger) *HTTPClient {
	return &HTTPClient{
		baseURL: baseURL,
		httpClient: &http.Client{
			Timeout: 30 * time.Second,
		},
		logger: logger,
	}
}

// Get performs a GET request
func (c *HTTPClient) Get(path string, headers map[string]string) (*http.Response, error) {
	return c.request("GET", path, nil, headers)
}

// Post performs a POST request
func (c *HTTPClient) Post(path string, body interface{}, headers map[string]string) (*http.Response, error) {
	return c.request("POST", path, body, headers)
}

// Put performs a PUT request
func (c *HTTPClient) Put(path string, body interface{}, headers map[string]string) (*http.Response, error) {
	return c.request("PUT", path, body, headers)
}

// Delete performs a DELETE request
func (c *HTTPClient) Delete(path string, headers map[string]string) (*http.Response, error) {
	return c.request("DELETE", path, nil, headers)
}

// request performs an HTTP request
func (c *HTTPClient) request(method, path string, body interface{}, headers map[string]string) (*http.Response, error) {
	url := c.baseURL + path

	var bodyReader io.Reader
	if body != nil {
		jsonData, err := json.Marshal(body)
		if err != nil {
			return nil, fmt.Errorf("failed to marshal request body: %w", err)
		}
		bodyReader = bytes.NewBuffer(jsonData)
	}

	req, err := http.NewRequest(method, url, bodyReader)
	if err != nil {
		return nil, fmt.Errorf("failed to create request: %w", err)
	}

	// Set default headers
	req.Header.Set("Content-Type", "application/json")
	req.Header.Set("Accept", "application/json")

	// Add custom headers
	for key, value := range headers {
		req.Header.Set(key, value)
	}

	c.logger.Debug("Making HTTP request",
		zap.String("method", method),
		zap.String("url", url),
		zap.Any("headers", headers),
	)

	resp, err := c.httpClient.Do(req)
	if err != nil {
		c.logger.Error("HTTP request failed",
			zap.String("method", method),
			zap.String("url", url),
			zap.Error(err),
		)
		return nil, fmt.Errorf("HTTP request failed: %w", err)
	}

	c.logger.Debug("HTTP response received",
		zap.String("method", method),
		zap.String("url", url),
		zap.Int("status", resp.StatusCode),
	)

	return resp, nil
}

// ReadJSONResponse reads and parses a JSON response
func (c *HTTPClient) ReadJSONResponse(resp *http.Response, target interface{}) error {
	defer resp.Body.Close()

	body, err := io.ReadAll(resp.Body)
	if err != nil {
		return fmt.Errorf("failed to read response body: %w", err)
	}

	if resp.StatusCode >= 400 {
		return fmt.Errorf("HTTP error %d: %s", resp.StatusCode, string(body))
	}

	if err := json.Unmarshal(body, target); err != nil {
		return fmt.Errorf("failed to unmarshal response: %w", err)
	}

	return nil
}