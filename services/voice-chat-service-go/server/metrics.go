package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	_ = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "voice_chat_service_requests_total",
			Help: "Total number of HTTP requests",
		},
		[]string{"method", "path", "status"},
	)

	_ = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "voice_chat_service_request_duration_seconds",
			Help:    "HTTP request duration in seconds",
			Buckets: prometheus.DefBuckets,
		},
		[]string{"method", "path"},
	)

	VoiceChannelsTotal = promauto.NewGaugeVec(
		prometheus.GaugeOpts{
			Name: "voice_chat_service_channels_total",
			Help: "Total number of active voice channels",
		},
		[]string{"type"},
	)

	VoiceParticipantsTotal = promauto.NewGaugeVec(
		prometheus.GaugeOpts{
			Name: "voice_chat_service_participants_total",
			Help: "Total number of active voice participants",
		},
		[]string{"channel_type"},
	)
)

func SetChannelsTotal(channelType string, count float64) {
	VoiceChannelsTotal.WithLabelValues(channelType).Set(count)
}

func SetParticipantsTotal(channelType string, count float64) {
	VoiceParticipantsTotal.WithLabelValues(channelType).Set(count)
}
