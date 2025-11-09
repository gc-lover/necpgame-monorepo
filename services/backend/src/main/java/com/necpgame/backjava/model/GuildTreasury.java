package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.GuildTreasuryAssetsInner;
import com.necpgame.backjava.model.TreasuryTransaction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GuildTreasury
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GuildTreasury {

  private @Nullable UUID guildId;

  private @Nullable Integer balance;

  @Valid
  private Map<String, Integer> currencies = new HashMap<>();

  @Valid
  private List<@Valid GuildTreasuryAssetsInner> assets = new ArrayList<>();

  @Valid
  private List<@Valid TreasuryTransaction> recentTransactions = new ArrayList<>();

  public GuildTreasury guildId(@Nullable UUID guildId) {
    this.guildId = guildId;
    return this;
  }

  /**
   * Get guildId
   * @return guildId
   */
  @Valid 
  @Schema(name = "guild_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guild_id")
  public @Nullable UUID getGuildId() {
    return guildId;
  }

  public void setGuildId(@Nullable UUID guildId) {
    this.guildId = guildId;
  }

  public GuildTreasury balance(@Nullable Integer balance) {
    this.balance = balance;
    return this;
  }

  /**
   * Get balance
   * @return balance
   */
  
  @Schema(name = "balance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("balance")
  public @Nullable Integer getBalance() {
    return balance;
  }

  public void setBalance(@Nullable Integer balance) {
    this.balance = balance;
  }

  public GuildTreasury currencies(Map<String, Integer> currencies) {
    this.currencies = currencies;
    return this;
  }

  public GuildTreasury putCurrenciesItem(String key, Integer currenciesItem) {
    if (this.currencies == null) {
      this.currencies = new HashMap<>();
    }
    this.currencies.put(key, currenciesItem);
    return this;
  }

  /**
   * Get currencies
   * @return currencies
   */
  
  @Schema(name = "currencies", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currencies")
  public Map<String, Integer> getCurrencies() {
    return currencies;
  }

  public void setCurrencies(Map<String, Integer> currencies) {
    this.currencies = currencies;
  }

  public GuildTreasury assets(List<@Valid GuildTreasuryAssetsInner> assets) {
    this.assets = assets;
    return this;
  }

  public GuildTreasury addAssetsItem(GuildTreasuryAssetsInner assetsItem) {
    if (this.assets == null) {
      this.assets = new ArrayList<>();
    }
    this.assets.add(assetsItem);
    return this;
  }

  /**
   * Товары и ресурсы в казне
   * @return assets
   */
  @Valid 
  @Schema(name = "assets", description = "Товары и ресурсы в казне", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("assets")
  public List<@Valid GuildTreasuryAssetsInner> getAssets() {
    return assets;
  }

  public void setAssets(List<@Valid GuildTreasuryAssetsInner> assets) {
    this.assets = assets;
  }

  public GuildTreasury recentTransactions(List<@Valid TreasuryTransaction> recentTransactions) {
    this.recentTransactions = recentTransactions;
    return this;
  }

  public GuildTreasury addRecentTransactionsItem(TreasuryTransaction recentTransactionsItem) {
    if (this.recentTransactions == null) {
      this.recentTransactions = new ArrayList<>();
    }
    this.recentTransactions.add(recentTransactionsItem);
    return this;
  }

  /**
   * Get recentTransactions
   * @return recentTransactions
   */
  @Valid 
  @Schema(name = "recent_transactions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recent_transactions")
  public List<@Valid TreasuryTransaction> getRecentTransactions() {
    return recentTransactions;
  }

  public void setRecentTransactions(List<@Valid TreasuryTransaction> recentTransactions) {
    this.recentTransactions = recentTransactions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuildTreasury guildTreasury = (GuildTreasury) o;
    return Objects.equals(this.guildId, guildTreasury.guildId) &&
        Objects.equals(this.balance, guildTreasury.balance) &&
        Objects.equals(this.currencies, guildTreasury.currencies) &&
        Objects.equals(this.assets, guildTreasury.assets) &&
        Objects.equals(this.recentTransactions, guildTreasury.recentTransactions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(guildId, balance, currencies, assets, recentTransactions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildTreasury {\n");
    sb.append("    guildId: ").append(toIndentedString(guildId)).append("\n");
    sb.append("    balance: ").append(toIndentedString(balance)).append("\n");
    sb.append("    currencies: ").append(toIndentedString(currencies)).append("\n");
    sb.append("    assets: ").append(toIndentedString(assets)).append("\n");
    sb.append("    recentTransactions: ").append(toIndentedString(recentTransactions)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

