package server

import (
	"context"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/models"
)

func (s *SocialService) GetGuildBank(ctx context.Context, guildID uuid.UUID) (*models.GuildBank, error) {
	return s.guildRepo.GetBank(ctx, guildID)
}

func (s *SocialService) DepositToGuildBank(ctx context.Context, guildID, accountID uuid.UUID, req *models.GuildBankDepositRequest) (*models.GuildBankTransaction, error) {
	guild, err := s.guildRepo.GetByID(ctx, guildID)
	if err != nil {
		return nil, err
	}
	if guild == nil {
		return nil, nil
	}

	member, err := s.guildRepo.GetMember(ctx, guildID, accountID)
	if err != nil {
		return nil, err
	}
	if member == nil {
		return nil, nil
	}

	bank, err := s.guildRepo.GetBank(ctx, guildID)
	if err != nil {
		return nil, err
	}
	if bank == nil {
		bank = &models.GuildBank{
			ID:        uuid.New(),
			GuildID:   guildID,
			Currency:  make(map[string]int),
			Items:     []map[string]interface{}{},
			UpdatedAt: time.Now(),
		}
		err = s.guildRepo.CreateBank(ctx, bank)
		if err != nil {
			return nil, err
		}
	}

	if req.Currency > 0 {
		if bank.Currency == nil {
			bank.Currency = make(map[string]int)
		}
		bank.Currency["credits"] = bank.Currency["credits"] + req.Currency
	}

	if len(req.Items) > 0 {
		bank.Items = append(bank.Items, req.Items...)
	}

	err = s.guildRepo.UpdateBank(ctx, bank)
	if err != nil {
		return nil, err
	}

	transaction := &models.GuildBankTransaction{
		ID:        uuid.New(),
		GuildID:   guildID,
		AccountID: accountID,
		Type:      "deposit",
		Currency:  req.Currency,
		Items:     req.Items,
		CreatedAt: time.Now(),
	}

	err = s.guildRepo.CreateBankTransaction(ctx, transaction)
	if err != nil {
		return nil, err
	}

	s.invalidateGuildCache(ctx, guildID)
	return transaction, nil
}

func (s *SocialService) WithdrawFromGuildBank(ctx context.Context, guildID, accountID uuid.UUID, req *models.GuildBankWithdrawRequest) (*models.GuildBankTransaction, error) {
	guild, err := s.guildRepo.GetByID(ctx, guildID)
	if err != nil {
		return nil, err
	}
	if guild == nil {
		return nil, nil
	}

	member, err := s.guildRepo.GetMember(ctx, guildID, accountID)
	if err != nil {
		return nil, err
	}
	if member == nil || (member.Rank != models.GuildRankLeader && member.Rank != models.GuildRankOfficer) {
		return nil, nil
	}

	bank, err := s.guildRepo.GetBank(ctx, guildID)
	if err != nil {
		return nil, err
	}
	if bank == nil {
		return nil, nil
	}

	if req.Currency > 0 {
		if bank.Currency == nil {
			bank.Currency = make(map[string]int)
		}
		if bank.Currency["credits"] < req.Currency {
			return nil, nil
		}
		bank.Currency["credits"] = bank.Currency["credits"] - req.Currency
	}

	if len(req.Items) > 0 {
		for _, withdrawItem := range req.Items {
			for i, bankItem := range bank.Items {
				if itemID, ok := withdrawItem["item_id"].(string); ok {
					if bankItemID, ok := bankItem["item_id"].(string); ok && itemID == bankItemID {
						bank.Items = append(bank.Items[:i], bank.Items[i+1:]...)
						break
					}
				}
			}
		}
	}

	err = s.guildRepo.UpdateBank(ctx, bank)
	if err != nil {
		return nil, err
	}

	transaction := &models.GuildBankTransaction{
		ID:        uuid.New(),
		GuildID:   guildID,
		AccountID: accountID,
		Type:      "withdraw",
		Currency:  req.Currency,
		Items:     req.Items,
		CreatedAt: time.Now(),
	}

	err = s.guildRepo.CreateBankTransaction(ctx, transaction)
	if err != nil {
		return nil, err
	}

	s.invalidateGuildCache(ctx, guildID)
	return transaction, nil
}

func (s *SocialService) GetGuildBankTransactions(ctx context.Context, guildID uuid.UUID, limit, offset int) (*models.GuildBankTransactionsResponse, error) {
	transactions, err := s.guildRepo.GetBankTransactions(ctx, guildID, limit, offset)
	if err != nil {
		return nil, err
	}

	total, err := s.guildRepo.CountBankTransactions(ctx, guildID)
	if err != nil {
		return nil, err
	}

	return &models.GuildBankTransactionsResponse{
		Transactions: transactions,
		Total:        total,
	}, nil
}

