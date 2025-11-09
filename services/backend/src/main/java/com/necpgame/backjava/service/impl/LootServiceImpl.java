package com.necpgame.backjava.service.impl;

import com.necpgame.backjava.model.DistributionResult;
import com.necpgame.backjava.model.GuaranteedDistributionRequest;
import com.necpgame.backjava.model.LootConfigResponse;
import com.necpgame.backjava.model.LootEntryBatchRequest;
import com.necpgame.backjava.model.LootEventNotificationRequest;
import com.necpgame.backjava.model.LootGenerationRequest;
import com.necpgame.backjava.model.LootGenerationResult;
import com.necpgame.backjava.model.LootHistoryResponse;
import com.necpgame.backjava.model.LootReleaseRequest;
import com.necpgame.backjava.model.LootReservationResponse;
import com.necpgame.backjava.model.LootReserveRequest;
import com.necpgame.backjava.model.LootRoll;
import com.necpgame.backjava.model.LootStatsResponse;
import com.necpgame.backjava.model.LootTableListResponse;
import com.necpgame.backjava.model.LootTableSummary;
import com.necpgame.backjava.model.LootTableUpsertRequest;
import com.necpgame.backjava.model.PersonalDistributionRequest;
import com.necpgame.backjava.model.PityTimerState;
import com.necpgame.backjava.model.PityTimerUpdateRequest;
import com.necpgame.backjava.model.RaidDistributionRequest;
import com.necpgame.backjava.model.RollStartRequest;
import com.necpgame.backjava.model.SharedDistributionRequest;
import com.necpgame.backjava.model.SimulationRequest;
import com.necpgame.backjava.model.SimulationResponse;
import com.necpgame.backjava.service.LootService;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class LootServiceImpl implements LootService {

    private UnsupportedOperationException error() {
        return new UnsupportedOperationException("Loot service is not implemented yet");
    }

    @Override
    public LootConfigResponse lootConfigGet() {
        throw error();
    }

    @Override
    public Void lootDistributionGuaranteedPost(GuaranteedDistributionRequest guaranteedDistributionRequest) {
        throw error();
    }

    @Override
    public DistributionResult lootDistributionPersonalPost(PersonalDistributionRequest personalDistributionRequest) {
        throw error();
    }

    @Override
    public DistributionResult lootDistributionRaidPost(RaidDistributionRequest raidDistributionRequest) {
        throw error();
    }

    @Override
    public DistributionResult lootDistributionSharedPost(SharedDistributionRequest sharedDistributionRequest) {
        throw error();
    }

    @Override
    public Void lootEventsNotifyPost(LootEventNotificationRequest lootEventNotificationRequest) {
        throw error();
    }

    @Override
    public LootHistoryResponse lootHistoryGet(String playerId, String sourceId, String distributionMode, String rarity, OffsetDateTime from, OffsetDateTime to, Integer page, Integer pageSize) {
        throw error();
    }

    @Override
    public PityTimerState lootPityPost(PityTimerUpdateRequest pityTimerUpdateRequest) {
        throw error();
    }

    @Override
    public Void lootReleasePost(LootReleaseRequest lootReleaseRequest) {
        throw error();
    }

    @Override
    public LootReservationResponse lootReservePost(LootReserveRequest lootReserveRequest) {
        throw error();
    }

    @Override
    public Void lootRollsPost(RollStartRequest rollStartRequest) {
        throw error();
    }

    @Override
    public LootRoll lootRollsSessionIdGet(UUID sessionId) {
        throw error();
    }

    @Override
    public LootGenerationResult lootSourcesSourceIdGeneratePost(String sourceId, LootGenerationRequest lootGenerationRequest) {
        throw error();
    }

    @Override
    public SimulationResponse lootSourcesSourceIdSimulatePost(String sourceId, SimulationRequest simulationRequest) {
        throw error();
    }

    @Override
    public LootStatsResponse lootStatsGet(String tableId, String timeRange) {
        throw error();
    }

    @Override
    public LootTableListResponse lootTablesGet(String sourceType, Boolean active, String rarityCurve, Integer page, Integer pageSize) {
        throw error();
    }

    @Override
    public LootTableSummary lootTablesPost(LootTableUpsertRequest lootTableUpsertRequest) {
        throw error();
    }

    @Override
    public Void lootTablesTableIdEntriesPost(String tableId, LootEntryBatchRequest lootEntryBatchRequest) {
        throw error();
    }

    @Override
    public LootTableUpsertRequest lootTablesTableIdGet(String tableId) {
        throw error();
    }
}


