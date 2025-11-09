package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.GuildBankTransaction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GuildBankResponse
 */


public class GuildBankResponse {

  private @Nullable Integer balance;

  private @Nullable String currency;

  private @Nullable Integer softCap;

  private @Nullable Integer hardCap;

  private @Nullable String withdrawalPolicy;

  @Valid
  private List<@Valid GuildBankTransaction> transactions = new ArrayList<>();

  public GuildBankResponse balance(@Nullable Integer balance) {
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

  public GuildBankResponse currency(@Nullable String currency) {
    this.currency = currency;
    return this;
  }

  /**
   * Get currency
   * @return currency
   */
  
  @Schema(name = "currency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currency")
  public @Nullable String getCurrency() {
    return currency;
  }

  public void setCurrency(@Nullable String currency) {
    this.currency = currency;
  }

  public GuildBankResponse softCap(@Nullable Integer softCap) {
    this.softCap = softCap;
    return this;
  }

  /**
   * Get softCap
   * @return softCap
   */
  
  @Schema(name = "softCap", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("softCap")
  public @Nullable Integer getSoftCap() {
    return softCap;
  }

  public void setSoftCap(@Nullable Integer softCap) {
    this.softCap = softCap;
  }

  public GuildBankResponse hardCap(@Nullable Integer hardCap) {
    this.hardCap = hardCap;
    return this;
  }

  /**
   * Get hardCap
   * @return hardCap
   */
  
  @Schema(name = "hardCap", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hardCap")
  public @Nullable Integer getHardCap() {
    return hardCap;
  }

  public void setHardCap(@Nullable Integer hardCap) {
    this.hardCap = hardCap;
  }

  public GuildBankResponse withdrawalPolicy(@Nullable String withdrawalPolicy) {
    this.withdrawalPolicy = withdrawalPolicy;
    return this;
  }

  /**
   * Get withdrawalPolicy
   * @return withdrawalPolicy
   */
  
  @Schema(name = "withdrawalPolicy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("withdrawalPolicy")
  public @Nullable String getWithdrawalPolicy() {
    return withdrawalPolicy;
  }

  public void setWithdrawalPolicy(@Nullable String withdrawalPolicy) {
    this.withdrawalPolicy = withdrawalPolicy;
  }

  public GuildBankResponse transactions(List<@Valid GuildBankTransaction> transactions) {
    this.transactions = transactions;
    return this;
  }

  public GuildBankResponse addTransactionsItem(GuildBankTransaction transactionsItem) {
    if (this.transactions == null) {
      this.transactions = new ArrayList<>();
    }
    this.transactions.add(transactionsItem);
    return this;
  }

  /**
   * Get transactions
   * @return transactions
   */
  @Valid 
  @Schema(name = "transactions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("transactions")
  public List<@Valid GuildBankTransaction> getTransactions() {
    return transactions;
  }

  public void setTransactions(List<@Valid GuildBankTransaction> transactions) {
    this.transactions = transactions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuildBankResponse guildBankResponse = (GuildBankResponse) o;
    return Objects.equals(this.balance, guildBankResponse.balance) &&
        Objects.equals(this.currency, guildBankResponse.currency) &&
        Objects.equals(this.softCap, guildBankResponse.softCap) &&
        Objects.equals(this.hardCap, guildBankResponse.hardCap) &&
        Objects.equals(this.withdrawalPolicy, guildBankResponse.withdrawalPolicy) &&
        Objects.equals(this.transactions, guildBankResponse.transactions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(balance, currency, softCap, hardCap, withdrawalPolicy, transactions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildBankResponse {\n");
    sb.append("    balance: ").append(toIndentedString(balance)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    softCap: ").append(toIndentedString(softCap)).append("\n");
    sb.append("    hardCap: ").append(toIndentedString(hardCap)).append("\n");
    sb.append("    withdrawalPolicy: ").append(toIndentedString(withdrawalPolicy)).append("\n");
    sb.append("    transactions: ").append(toIndentedString(transactions)).append("\n");
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

