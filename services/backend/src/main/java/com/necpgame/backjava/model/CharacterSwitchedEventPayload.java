package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.OffsetDateTime;
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
 * CharacterSwitchedEventPayload
 */

@JsonTypeName("CharacterSwitchedEvent_payload")

public class CharacterSwitchedEventPayload {

  private UUID accountId;

  private UUID newCharacterId;

  private UUID previousCharacterId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime switchedAt;

  public CharacterSwitchedEventPayload() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CharacterSwitchedEventPayload(UUID accountId, UUID newCharacterId, UUID previousCharacterId, OffsetDateTime switchedAt) {
    this.accountId = accountId;
    this.newCharacterId = newCharacterId;
    this.previousCharacterId = previousCharacterId;
    this.switchedAt = switchedAt;
  }

  public CharacterSwitchedEventPayload accountId(UUID accountId) {
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

  public CharacterSwitchedEventPayload newCharacterId(UUID newCharacterId) {
    this.newCharacterId = newCharacterId;
    return this;
  }

  /**
   * Get newCharacterId
   * @return newCharacterId
   */
  @NotNull @Valid 
  @Schema(name = "newCharacterId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("newCharacterId")
  public UUID getNewCharacterId() {
    return newCharacterId;
  }

  public void setNewCharacterId(UUID newCharacterId) {
    this.newCharacterId = newCharacterId;
  }

  public CharacterSwitchedEventPayload previousCharacterId(UUID previousCharacterId) {
    this.previousCharacterId = previousCharacterId;
    return this;
  }

  /**
   * Get previousCharacterId
   * @return previousCharacterId
   */
  @NotNull @Valid 
  @Schema(name = "previousCharacterId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("previousCharacterId")
  public UUID getPreviousCharacterId() {
    return previousCharacterId;
  }

  public void setPreviousCharacterId(UUID previousCharacterId) {
    this.previousCharacterId = previousCharacterId;
  }

  public CharacterSwitchedEventPayload switchedAt(OffsetDateTime switchedAt) {
    this.switchedAt = switchedAt;
    return this;
  }

  /**
   * Get switchedAt
   * @return switchedAt
   */
  @NotNull @Valid 
  @Schema(name = "switchedAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("switchedAt")
  public OffsetDateTime getSwitchedAt() {
    return switchedAt;
  }

  public void setSwitchedAt(OffsetDateTime switchedAt) {
    this.switchedAt = switchedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterSwitchedEventPayload characterSwitchedEventPayload = (CharacterSwitchedEventPayload) o;
    return Objects.equals(this.accountId, characterSwitchedEventPayload.accountId) &&
        Objects.equals(this.newCharacterId, characterSwitchedEventPayload.newCharacterId) &&
        Objects.equals(this.previousCharacterId, characterSwitchedEventPayload.previousCharacterId) &&
        Objects.equals(this.switchedAt, characterSwitchedEventPayload.switchedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, newCharacterId, previousCharacterId, switchedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterSwitchedEventPayload {\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    newCharacterId: ").append(toIndentedString(newCharacterId)).append("\n");
    sb.append("    previousCharacterId: ").append(toIndentedString(previousCharacterId)).append("\n");
    sb.append("    switchedAt: ").append(toIndentedString(switchedAt)).append("\n");
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

