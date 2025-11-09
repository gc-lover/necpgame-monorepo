package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CharacterStatsUpdatedEventPayload
 */

@JsonTypeName("CharacterStatsUpdatedEvent_payload")

public class CharacterStatsUpdatedEventPayload {

  private UUID characterId;

  private UUID accountId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime recalculatedAt;

  @Valid
  private Map<String, BigDecimal> delta = new HashMap<>();

  public CharacterStatsUpdatedEventPayload() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CharacterStatsUpdatedEventPayload(UUID characterId, UUID accountId, OffsetDateTime recalculatedAt, Map<String, BigDecimal> delta) {
    this.characterId = characterId;
    this.accountId = accountId;
    this.recalculatedAt = recalculatedAt;
    this.delta = delta;
  }

  public CharacterStatsUpdatedEventPayload characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "characterId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("characterId")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
    this.characterId = characterId;
  }

  public CharacterStatsUpdatedEventPayload accountId(UUID accountId) {
    this.accountId = accountId;
    return this;
  }

  /**
   * Get accountId
   * @return accountId
   */
  @NotNull @Valid 
  @Schema(name = "accountId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("accountId")
  public UUID getAccountId() {
    return accountId;
  }

  public void setAccountId(UUID accountId) {
    this.accountId = accountId;
  }

  public CharacterStatsUpdatedEventPayload recalculatedAt(OffsetDateTime recalculatedAt) {
    this.recalculatedAt = recalculatedAt;
    return this;
  }

  /**
   * Get recalculatedAt
   * @return recalculatedAt
   */
  @NotNull @Valid 
  @Schema(name = "recalculatedAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("recalculatedAt")
  public OffsetDateTime getRecalculatedAt() {
    return recalculatedAt;
  }

  public void setRecalculatedAt(OffsetDateTime recalculatedAt) {
    this.recalculatedAt = recalculatedAt;
  }

  public CharacterStatsUpdatedEventPayload delta(Map<String, BigDecimal> delta) {
    this.delta = delta;
    return this;
  }

  public CharacterStatsUpdatedEventPayload putDeltaItem(String key, BigDecimal deltaItem) {
    if (this.delta == null) {
      this.delta = new HashMap<>();
    }
    this.delta.put(key, deltaItem);
    return this;
  }

  /**
   * Get delta
   * @return delta
   */
  @NotNull @Valid 
  @Schema(name = "delta", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("delta")
  public Map<String, BigDecimal> getDelta() {
    return delta;
  }

  public void setDelta(Map<String, BigDecimal> delta) {
    this.delta = delta;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterStatsUpdatedEventPayload characterStatsUpdatedEventPayload = (CharacterStatsUpdatedEventPayload) o;
    return Objects.equals(this.characterId, characterStatsUpdatedEventPayload.characterId) &&
        Objects.equals(this.accountId, characterStatsUpdatedEventPayload.accountId) &&
        Objects.equals(this.recalculatedAt, characterStatsUpdatedEventPayload.recalculatedAt) &&
        Objects.equals(this.delta, characterStatsUpdatedEventPayload.delta);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, accountId, recalculatedAt, delta);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterStatsUpdatedEventPayload {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    recalculatedAt: ").append(toIndentedString(recalculatedAt)).append("\n");
    sb.append("    delta: ").append(toIndentedString(delta)).append("\n");
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

