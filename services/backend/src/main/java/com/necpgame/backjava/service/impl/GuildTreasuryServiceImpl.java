package com.necpgame.backjava.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.backjava.entity.TradingGuildBankTransactionEntity;
import com.necpgame.backjava.entity.TradingGuildEntity;
import com.necpgame.backjava.entity.TradingGuildMemberEntity;
import com.necpgame.backjava.entity.TradingGuildMemberId;
import com.necpgame.backjava.entity.TradingGuildTreasuryEntity;
import com.necpgame.backjava.entity.enums.DistributionType;
import com.necpgame.backjava.entity.enums.GuildTransactionType;
import com.necpgame.backjava.entity.enums.TradingGuildRole;
import com.necpgame.backjava.model.ContributeToTreasury200Response;
import com.necpgame.backjava.model.ContributionRequest;
import com.necpgame.backjava.model.DistributeProfits200Response;
import com.necpgame.backjava.model.DistributeProfits200ResponseDistributionsInner;
import com.necpgame.backjava.model.DistributeProfitsRequest;
import com.necpgame.backjava.model.GuildTreasury;
import com.necpgame.backjava.model.GuildTreasuryAssetsInner;
import com.necpgame.backjava.model.TreasuryTransaction;
import com.necpgame.backjava.repository.TradingGuildBankTransactionRepository;
import com.necpgame.backjava.repository.TradingGuildMemberRepository;
import com.necpgame.backjava.repository.TradingGuildRepository;
import com.necpgame.backjava.repository.TradingGuildTreasuryRepository;
import com.necpgame.backjava.service.GuildTreasuryService;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.math.RoundingMode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GuildTreasuryServiceImpl implements GuildTreasuryService {

    private static final TypeReference<Map<String, Integer>> CURRENCY_TYPE = new TypeReference<>() {
    };
    private static final TypeReference<List<GuildTreasuryAssetsInner>> ASSET_TYPE = new TypeReference<>() {
    };
    private static final TypeReference<List<DistributeProfits200ResponseDistributionsInner>> DISTRIBUTION_LIST_TYPE = new TypeReference<>() {
    };

    private final TradingGuildRepository tradingGuildRepository;
    private final TradingGuildTreasuryRepository tradingGuildTreasuryRepository;
    private final TradingGuildBankTransactionRepository tradingGuildBankTransactionRepository;
    private final TradingGuildMemberRepository tradingGuildMemberRepository;
    private final ObjectMapper objectMapper;

    @Override
    public GuildTreasury getGuildTreasury(UUID guildId) {
        TradingGuildEntity guild = findGuildOrThrow(guildId);
        TradingGuildTreasuryEntity treasury = tradingGuildTreasuryRepository.findById(guildId)
            .orElseGet(() -> TradingGuildTreasuryEntity.builder()
                .guildId(guildId)
                .balance(BigDecimal.ZERO)
                .currenciesJson(writeDefaultCurrencies())
                .assetsJson("[]")
                .build());

        Map<String, Integer> currencies = readCurrencies(treasury.getCurrenciesJson());
        List<GuildTreasuryAssetsInner> assets = readAssets(treasury.getAssetsJson());
        List<TreasuryTransaction> transactions = tradingGuildBankTransactionRepository
            .findTop10ByGuildIdOrderByCreatedAtDesc(guildId)
            .stream()
            .map(this::mapToTransaction)
            .toList();

        return new GuildTreasury()
            .guildId(guild.getId())
            .balance(treasury.getBalance().intValue())
            .currencies(currencies)
            .assets(assets)
            .recentTransactions(transactions);
    }

    @Override
    @Transactional
    public ContributeToTreasury200Response contributeToTreasury(UUID guildId, ContributionRequest contributionRequest) {
        findGuildOrThrow(guildId);
        if (contributionRequest == null || contributionRequest.getCharacterId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "character_id is required");
        }
        if (contributionRequest.getAmount() == null || contributionRequest.getAmount() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "amount must be positive");
        }

        TradingGuildTreasuryEntity treasury = tradingGuildTreasuryRepository.findById(guildId)
            .orElseGet(() -> TradingGuildTreasuryEntity.builder()
                .guildId(guildId)
                .balance(BigDecimal.ZERO)
                .currenciesJson(writeDefaultCurrencies())
                .assetsJson("[]")
                .build());

        BigDecimal newBalance = treasury.getBalance().add(BigDecimal.valueOf(contributionRequest.getAmount()));
        treasury.setBalance(newBalance);

        Map<String, Integer> currencies = readCurrencies(treasury.getCurrenciesJson());
        currencies.merge(contributionRequest.getCurrency(), contributionRequest.getAmount(), Integer::sum);
        treasury.setCurrenciesJson(writeCurrencies(currencies));
        tradingGuildTreasuryRepository.save(treasury);

        UUID transactionId = UUID.randomUUID();
        TradingGuildBankTransactionEntity transaction = TradingGuildBankTransactionEntity.builder()
            .id(transactionId)
            .guildId(guildId)
            .performedBy(contributionRequest.getCharacterId())
            .transactionType(GuildTransactionType.CONTRIBUTION)
            .amount(BigDecimal.valueOf(contributionRequest.getAmount()))
            .currency(contributionRequest.getCurrency())
            .description("Member contribution")
            .createdAt(Instant.now())
            .build();
        tradingGuildBankTransactionRepository.save(transaction);

        TradingGuildMemberEntity member = tradingGuildMemberRepository
            .findByIdGuildIdAndIdCharacterId(guildId, contributionRequest.getCharacterId())
            .orElseGet(() -> TradingGuildMemberEntity.builder()
                .id(new TradingGuildMemberId(guildId, contributionRequest.getCharacterId()))
                .role(TradingGuildRole.RECRUIT)
                .contributionTotal(BigDecimal.ZERO)
                .votingPower(BigDecimal.ONE)
                .tradesCompleted(0)
                .joinedAt(Instant.now())
                .build());
        member.setContributionTotal(member.getContributionTotal().add(BigDecimal.valueOf(contributionRequest.getAmount())));
        tradingGuildMemberRepository.save(member);

        return new ContributeToTreasury200Response()
            .contributionId(transactionId)
            .newBalance(newBalance.intValue());
    }

    @Override
    @Transactional
    public DistributeProfits200Response distributeProfits(UUID guildId, DistributeProfitsRequest distributeProfitsRequest) {
        TradingGuildEntity guild = findGuildOrThrow(guildId);
        TradingGuildTreasuryEntity treasury = tradingGuildTreasuryRepository.findById(guildId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Treasury is not initialized"));

        DistributionType distributionType = Optional.ofNullable(distributeProfitsRequest)
            .map(DistributeProfitsRequest::getDistributionType)
            .map(type -> DistributionType.valueOf(type.getValue()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "distribution_type is required"));

        List<TradingGuildMemberEntity> members = tradingGuildMemberRepository.findByIdGuildId(guildId);
        if (members.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Guild has no members");
        }

        int requestedAmount = Optional.ofNullable(distributeProfitsRequest.getTotalAmount()).orElse(0);

        List<DistributeProfits200ResponseDistributionsInner> distributions;
        if (distributionType == DistributionType.CUSTOM) {
            distributions = resolveCustomDistribution(distributeProfitsRequest, members);
        } else {
            if (requestedAmount <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "total_amount must be positive");
            }
            if (treasury.getBalance().compareTo(BigDecimal.valueOf(requestedAmount)) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance in treasury");
            }
            distributions = switch (distributionType) {
                case EQUAL -> resolveEqualDistribution(members, requestedAmount);
                case BY_CONTRIBUTION -> resolveContributionDistribution(members, requestedAmount);
                case BY_ROLE -> resolveRoleDistribution(members, requestedAmount);
                default -> Collections.emptyList();
            };
        }

        int distributedSum = distributions.stream()
            .map(DistributeProfits200ResponseDistributionsInner::getAmount)
            .filter(amount -> amount != null && amount > 0)
            .mapToInt(Integer::intValue)
            .sum();

        if (distributedSum <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Distribution result is empty");
        }

        if (treasury.getBalance().compareTo(BigDecimal.valueOf(distributedSum)) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance in treasury");
        }

        treasury.setBalance(treasury.getBalance().subtract(BigDecimal.valueOf(distributedSum)));
        tradingGuildTreasuryRepository.save(treasury);

        UUID distributionId = UUID.randomUUID();
        TradingGuildBankTransactionEntity transaction = TradingGuildBankTransactionEntity.builder()
            .id(UUID.randomUUID())
            .guildId(guildId)
            .performedBy(null)
            .transactionType(GuildTransactionType.DISTRIBUTION)
            .amount(BigDecimal.valueOf(distributedSum))
            .currency("eddies")
            .description("Profit distribution")
            .metadataJson(writeDistributionDetails(distributions))
            .createdAt(Instant.now())
            .build();
        tradingGuildBankTransactionRepository.save(transaction);

        return new DistributeProfits200Response()
            .distributionId(distributionId)
            .totalDistributed(distributedSum)
            .distributions(distributions);
    }

    private TradingGuildEntity findGuildOrThrow(UUID guildId) {
        return tradingGuildRepository.findById(guildId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Guild not found"));
    }

    private Map<String, Integer> readCurrencies(String json) {
        if (json == null || json.isBlank()) {
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(json, CURRENCY_TYPE);
        } catch (JsonProcessingException ex) {
            return new HashMap<>();
        }
    }

    private String writeCurrencies(Map<String, Integer> currencies) {
        try {
            return objectMapper.writeValueAsString(currencies);
        } catch (JsonProcessingException ex) {
            return writeDefaultCurrencies();
        }
    }

    private String writeDefaultCurrencies() {
        Map<String, Integer> defaults = new HashMap<>();
        defaults.put("eddies", 0);
        try {
            return objectMapper.writeValueAsString(defaults);
        } catch (JsonProcessingException ex) {
            return "{\"eddies\":0}";
        }
    }

    private List<GuildTreasuryAssetsInner> readAssets(String json) {
        if (json == null || json.isBlank()) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(json, ASSET_TYPE);
        } catch (JsonProcessingException ex) {
            return Collections.emptyList();
        }
    }

    private TreasuryTransaction mapToTransaction(TradingGuildBankTransactionEntity entity) {
        return new TreasuryTransaction()
            .transactionId(entity.getId())
            .type(TreasuryTransaction.TypeEnum.fromValue(entity.getTransactionType().name()))
            .amount(entity.getAmount().intValue())
            .characterId(entity.getPerformedBy())
            .description(entity.getDescription())
            .timestamp(toOffsetDateTime(entity.getCreatedAt()));
    }

    private OffsetDateTime toOffsetDateTime(Instant instant) {
        return instant != null ? OffsetDateTime.ofInstant(instant, ZoneOffset.UTC) : null;
    }

    private List<DistributeProfits200ResponseDistributionsInner> resolveCustomDistribution(
        DistributeProfitsRequest request,
        List<TradingGuildMemberEntity> members
    ) {
        if (request.getCustomDistributions() == null || !request.getCustomDistributions().isPresent()
            || request.getCustomDistributions().get().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "custom_distributions is required for CUSTOM type");
        }
        List<DistributeProfits200ResponseDistributionsInner> items = request.getCustomDistributions().get();
        List<UUID> memberIds = members.stream()
            .map(m -> m.getId().getCharacterId())
            .collect(Collectors.toList());
        for (DistributeProfits200ResponseDistributionsInner item : items) {
            if (item.getCharacterId() == null || !memberIds.contains(item.getCharacterId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "custom distribution contains unknown member");
            }
        }
        int sum = items.stream()
            .map(DistributeProfits200ResponseDistributionsInner::getAmount)
            .filter(amount -> amount != null && amount > 0)
            .mapToInt(Integer::intValue)
            .sum();
        if (sum <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "custom distributions must contain positive amounts");
        }
        return items;
    }

    private List<DistributeProfits200ResponseDistributionsInner> resolveEqualDistribution(List<TradingGuildMemberEntity> members, int totalAmount) {
        int perMember = totalAmount / members.size();
        int remainder = totalAmount % members.size();
        List<DistributeProfits200ResponseDistributionsInner> result = new ArrayList<>();
        for (int i = 0; i < members.size(); i++) {
            int amount = perMember + (i < remainder ? 1 : 0);
            if (amount <= 0) {
                continue;
            }
            result.add(new DistributeProfits200ResponseDistributionsInner()
                .characterId(members.get(i).getId().getCharacterId())
                .amount(amount));
        }
        return result;
    }

    private List<DistributeProfits200ResponseDistributionsInner> resolveContributionDistribution(
        List<TradingGuildMemberEntity> members,
        int totalAmount
    ) {
        BigDecimal totalContributions = members.stream()
            .map(TradingGuildMemberEntity::getContributionTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (totalContributions.compareTo(BigDecimal.ZERO) <= 0) {
            return resolveEqualDistribution(members, totalAmount);
        }
        List<DistributeProfits200ResponseDistributionsInner> result = new ArrayList<>();
        BigDecimal distributed = BigDecimal.ZERO;
        for (int i = 0; i < members.size(); i++) {
            TradingGuildMemberEntity member = members.get(i);
            BigDecimal ratio = member.getContributionTotal().divide(totalContributions, 6, RoundingMode.HALF_UP);
            BigDecimal share = ratio.multiply(BigDecimal.valueOf(totalAmount));
            int amount = share.setScale(0, RoundingMode.DOWN).intValue();
            distributed = distributed.add(BigDecimal.valueOf(amount));
            result.add(new DistributeProfits200ResponseDistributionsInner()
                .characterId(member.getId().getCharacterId())
                .amount(amount));
        }
        int remainder = totalAmount - distributed.intValue();
        for (int i = 0; remainder > 0 && i < result.size(); i++, remainder--) {
            DistributeProfits200ResponseDistributionsInner item = result.get(i);
            item.setAmount(Optional.ofNullable(item.getAmount()).orElse(0) + 1);
        }
        return result;
    }

    private List<DistributeProfits200ResponseDistributionsInner> resolveRoleDistribution(
        List<TradingGuildMemberEntity> members,
        int totalAmount
    ) {
        Map<TradingGuildRole, Integer> weights = Map.of(
            TradingGuildRole.GUILD_MASTER, 4,
            TradingGuildRole.TREASURER, 3,
            TradingGuildRole.MERCHANT, 2,
            TradingGuildRole.TRADER, 2,
            TradingGuildRole.RECRUIT, 1
        );

        int totalWeight = members.stream()
            .map(member -> weights.getOrDefault(member.getRole(), 1))
            .mapToInt(Integer::intValue)
            .sum();
        if (totalWeight <= 0) {
            return resolveEqualDistribution(members, totalAmount);
        }

        List<DistributeProfits200ResponseDistributionsInner> result = new ArrayList<>();
        int distributed = 0;
        for (TradingGuildMemberEntity member : members) {
            int weight = weights.getOrDefault(member.getRole(), 1);
            int amount = (int) ((long) totalAmount * weight / totalWeight);
            distributed += amount;
            result.add(new DistributeProfits200ResponseDistributionsInner()
                .characterId(member.getId().getCharacterId())
                .amount(amount));
        }
        int remainder = totalAmount - distributed;
        for (int i = 0; remainder > 0 && i < result.size(); i++, remainder--) {
            DistributeProfits200ResponseDistributionsInner item = result.get(i);
            item.setAmount(Optional.ofNullable(item.getAmount()).orElse(0) + 1);
        }
        return result;
    }

    private String writeDistributionDetails(List<DistributeProfits200ResponseDistributionsInner> distributions) {
        try {
            return objectMapper.writeValueAsString(distributions);
        } catch (JsonProcessingException ex) {
            return "[]";
        }
    }
}

