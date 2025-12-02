// Issue: #172
package server

import (
	"context"
	"errors"

	"github.com/gc-lover/necpgame/services/chat-service-go/pkg/api"
)

type Service interface {
	SendMessage(ctx context.Context, req *api.SendMessageRequest) (*api.MessageResponse, error)
	GetChannelMessages(ctx context.Context, channelID string, params api.GetChannelMessagesParams) (*api.MessagesHistoryResponse, error)
	GetChannels(ctx context.Context) (*api.ChannelsListResponse, error)
	CreateChannel(ctx context.Context, req *api.CreateChannelRequest) (*api.ChannelResponse, error)
	GetChannel(ctx context.Context, channelID string) (*api.ChannelResponse, error)
	UpdateChannel(ctx context.Context, channelID string, req *api.UpdateChannelRequest) (*api.ChannelResponse, error)
	DeleteChannel(ctx context.Context, channelID string) error
	AddChannelMember(ctx context.Context, channelID string, req *api.AddMemberRequest) error
	RemoveChannelMember(ctx context.Context, channelID, playerID string) error
	BanPlayer(ctx context.Context, req *api.BanPlayerRequest) (*api.BanResponse, error)
	UnbanPlayer(ctx context.Context, banID string) error
	DeleteMessage(ctx context.Context, messageID string) error
}

type ChatService struct {
	repository Repository
}

func NewChatService(repository Repository) Service {
	return &ChatService{repository: repository}
}

func (s *ChatService) SendMessage(ctx context.Context, req *api.SendMessageRequest) (*api.MessageResponse, error) {
	return nil, errors.New("not implemented")
}

func (s *ChatService) GetChannelMessages(ctx context.Context, channelID string, params api.GetChannelMessagesParams) (*api.MessagesHistoryResponse, error) {
	msgs := []api.MessageResponse{}
	return &api.MessagesHistoryResponse{Messages: &msgs}, nil
}

func (s *ChatService) GetChannels(ctx context.Context) (*api.ChannelsListResponse, error) {
	channels := []api.ChannelResponse{}
	return &api.ChannelsListResponse{Channels: &channels}, nil
}

func (s *ChatService) CreateChannel(ctx context.Context, req *api.CreateChannelRequest) (*api.ChannelResponse, error) {
	return nil, errors.New("not implemented")
}

func (s *ChatService) GetChannel(ctx context.Context, channelID string) (*api.ChannelResponse, error) {
	return nil, errors.New("not implemented")
}

func (s *ChatService) UpdateChannel(ctx context.Context, channelID string, req *api.UpdateChannelRequest) (*api.ChannelResponse, error) {
	return nil, errors.New("not implemented")
}

func (s *ChatService) DeleteChannel(ctx context.Context, channelID string) error {
	return nil
}

func (s *ChatService) AddChannelMember(ctx context.Context, channelID string, req *api.AddMemberRequest) error {
	return nil
}

func (s *ChatService) RemoveChannelMember(ctx context.Context, channelID, playerID string) error {
	return nil
}

func (s *ChatService) BanPlayer(ctx context.Context, req *api.BanPlayerRequest) (*api.BanResponse, error) {
	return nil, errors.New("not implemented")
}

func (s *ChatService) UnbanPlayer(ctx context.Context, banID string) error {
	return nil
}

func (s *ChatService) DeleteMessage(ctx context.Context, messageID string) error {
	return nil
}

