package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.GuildBankTransaction;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GuildBankEvent
 */


public class GuildBankEvent {

  private @Nullable String guildId;

  private @Nullable GuildBankTransaction transaction;

  public GuildBankEvent guildId(@Nullable String guildId) {
    this.guildId = guildId;
    return this;
  }

  /**
   * Get guildId
   * @return guildId
   */
  
  @Schema(name = "guildId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guildId")
  public @Nullable String getGuildId() {
    return guildId;
  }

  public void setGuildId(@Nullable String guildId) {
    this.guildId = guildId;
  }

  public GuildBankEvent transaction(@Nullable GuildBankTransaction transaction) {
    this.transaction = transaction;
    return this;
  }

  /**
   * Get transaction
   * @return transaction
   */
  @Valid 
  @Schema(name = "transaction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("transaction")
  public @Nullable GuildBankTransaction getTransaction() {
    return transaction;
  }

  public void setTransaction(@Nullable GuildBankTransaction transaction) {
    this.transaction = transaction;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuildBankEvent guildBankEvent = (GuildBankEvent) o;
    return Objects.equals(this.guildId, guildBankEvent.guildId) &&
        Objects.equals(this.transaction, guildBankEvent.transaction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(guildId, transaction);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildBankEvent {\n");
    sb.append("    guildId: ").append(toIndentedString(guildId)).append("\n");
    sb.append("    transaction: ").append(toIndentedString(transaction)).append("\n");
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

