package server

import (
	"bytes"
	"context"
	"encoding/json"
	"fmt"
	"net/http"
	"time"

	"github.com/necpgame/feedback-service-go/models"
	"github.com/sirupsen/logrus"
)

type GitHubClient struct {
	token  string
	client *http.Client
	logger *logrus.Logger
}

type GitHubIssueRequest struct {
	Title  string   `json:"title"`
	Body   string   `json:"body"`
	Labels []string `json:"labels"`
}

type GitHubIssueResponse struct {
	Number int    `json:"number"`
	URL    string `json:"html_url"`
}

func NewGitHubClient(token string, logger *logrus.Logger) *GitHubClient {
	return &GitHubClient{
		token:  token,
		client: &http.Client{Timeout: 10 * time.Second},
		logger: logger,
	}
}

func (c *GitHubClient) CreateIssue(ctx context.Context, feedback *models.Feedback) (int, string, error) {
	if c.token == "" {
		return 0, "", fmt.Errorf("GitHub token not configured")
	}

	labels := []string{"player-feedback"}
	labels = append(labels, string(feedback.Type))
	labels = append(labels, string(feedback.Category))

	if feedback.Priority != nil {
		labels = append(labels, "priority-"+string(*feedback.Priority))
	}

	body := fmt.Sprintf(`## Описание

%s

## Тип обращения
%s

## Категория
%s

## Контекст игры
%s

## Скриншоты
%s

---
*Создано через систему обратной связи игроков*
*Feedback ID: %s*`,
		feedback.Description,
		feedback.Type,
		feedback.Category,
		c.formatGameContext(feedback.GameContext),
		c.formatScreenshots(feedback.Screenshots),
		feedback.ID.String(),
	)

	reqBody := GitHubIssueRequest{
		Title:  feedback.Title,
		Body:   body,
		Labels: labels,
	}

	jsonData, err := json.Marshal(reqBody)
	if err != nil {
		return 0, "", err
	}

	url := "https://api.github.com/repos/gc-lover/necpgame-monorepo/issues"
	req, err := http.NewRequestWithContext(ctx, "POST", url, bytes.NewBuffer(jsonData))
	if err != nil {
		return 0, "", err
	}

	req.Header.Set("Authorization", "token "+c.token)
	req.Header.Set("Content-Type", "application/json")
	req.Header.Set("Accept", "application/vnd.github.v3+json")

	resp, err := c.client.Do(req)
	if err != nil {
		return 0, "", err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusCreated {
		var errorBody map[string]interface{}
		json.NewDecoder(resp.Body).Decode(&errorBody)
		return 0, "", fmt.Errorf("GitHub API error: %d - %v", resp.StatusCode, errorBody)
	}

	var issueResp GitHubIssueResponse
	if err := json.NewDecoder(resp.Body).Decode(&issueResp); err != nil {
		return 0, "", err
	}

	return issueResp.Number, issueResp.URL, nil
}

func (c *GitHubClient) formatGameContext(ctx *models.GameContext) string {
	if ctx == nil {
		return "Не указан"
	}

	result := fmt.Sprintf("- Версия: %s\n", ctx.Version)
	if ctx.Location != "" {
		result += fmt.Sprintf("- Локация: %s\n", ctx.Location)
	}
	if ctx.CharacterLevel != nil {
		result += fmt.Sprintf("- Уровень персонажа: %d\n", *ctx.CharacterLevel)
	}
	if len(ctx.ActiveQuests) > 0 {
		result += fmt.Sprintf("- Активные квесты: %v\n", ctx.ActiveQuests)
	}
	if ctx.PlaytimeHours != nil {
		result += fmt.Sprintf("- Время в игре: %.1f часов\n", *ctx.PlaytimeHours)
	}

	return result
}

func (c *GitHubClient) formatScreenshots(screenshots []string) string {
	if len(screenshots) == 0 {
		return "Нет"
	}

	result := ""
	for i, url := range screenshots {
		result += fmt.Sprintf("%d. %s\n", i+1, url)
	}
	return result
}


















