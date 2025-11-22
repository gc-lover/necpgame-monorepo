package server

import (
	"html"
	"regexp"
	"strings"
)

func FormatMessage(content string) string {
	formatted := html.EscapeString(content)

	formatted = formatBold(formatted)
	formatted = formatItalic(formatted)
	formatted = formatLinks(formatted)
	formatted = formatMentions(formatted)
	formatted = formatEmoji(formatted)

	return formatted
}

func formatBold(text string) string {
	re := regexp.MustCompile(`\*\*(.+?)\*\*`)
	return re.ReplaceAllStringFunc(text, func(match string) string {
		content := strings.Trim(match, "*")
		return "<strong>" + content + "</strong>"
	})
}

func formatItalic(text string) string {
	re := regexp.MustCompile(`\*(.+?)\*`)
	return re.ReplaceAllStringFunc(text, func(match string) string {
		if strings.HasPrefix(match, "**") {
			return match
		}
		content := strings.Trim(match, "*")
		return "<em>" + content + "</em>"
	})
}

func formatLinks(text string) string {
	urlPattern := regexp.MustCompile(`https?://[^\s]+`)
	whitelistDomains := []string{"necp.game", "github.com", "discord.gg"}

	return urlPattern.ReplaceAllStringFunc(text, func(url string) string {
		for _, domain := range whitelistDomains {
			if strings.Contains(url, domain) {
				return `<a href="` + html.EscapeString(url) + `" target="_blank" rel="noopener noreferrer">` + url + `</a>`
			}
		}
		return "[link blocked]"
	})
}

func formatMentions(text string) string {
	re := regexp.MustCompile(`@(\w+)`)
	return re.ReplaceAllStringFunc(text, func(match string) string {
		username := strings.TrimPrefix(match, "@")
		return `<span class="mention">@` + html.EscapeString(username) + `</span>`
	})
}

func formatEmoji(text string) string {
	emojiMap := map[string]string{
		":)":  "😊",
		":(":  "😢",
		":D":  "😃",
		":P":  "😛",
		":o":  "😮",
		":*":  "😘",
		"<3":  "❤️",
		":thumbsup:": "👍",
		":thumbsdown:": "👎",
	}

	for code, emoji := range emojiMap {
		text = strings.ReplaceAll(text, code, emoji)
	}

	return text
}

