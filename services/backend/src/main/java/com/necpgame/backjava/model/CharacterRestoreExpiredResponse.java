package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * CharacterRestoreExpiredResponse
 */


public class CharacterRestoreExpiredResponse {

  private UUID characterId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime lastAvailableDate;

  private @Nullable String supportTicketHint;

  public CharacterRestoreExpiredResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CharacterRestoreExpiredResponse(UUID characterId, OffsetDateTime lastAvailableDate) {
    this.characterId = characterId;
    this.lastAvailableDate = lastAvailableDate;
  }

  public CharacterRestoreExpiredResponse characterId(UUID characterId) {
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

  public CharacterRestoreExpiredResponse lastAvailableDate(OffsetDateTime lastAvailableDate) {
    this.lastAvailableDate = lastAvailableDate;
    return this;
  }

  /**
   * Get lastAvailableDate
   * @return lastAvailableDate
   */
  @NotNull @Valid 
  @Schema(name = "lastAvailableDate", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("lastAvailableDate")
  public OffsetDateTime getLastAvailableDate() {
    return lastAvailableDate;
  }

  public void setLastAvailableDate(OffsetDateTime lastAvailableDate) {
    this.lastAvailableDate = lastAvailableDate;
  }

  public CharacterRestoreExpiredResponse supportTicketHint(@Nullable String supportTicketHint) {
    this.supportTicketHint = supportTicketHint;
    return this;
  }

  /**
   * Guidance for escalation to support
   * @return supportTicketHint
   */
  
  @Schema(name = "supportTicketHint", description = "Guidance for escalation to support", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("supportTicketHint")
  public @Nullable String getSupportTicketHint() {
    return supportTicketHint;
  }

  public void setSupportTicketHint(@Nullable String supportTicketHint) {
    this.supportTicketHint = supportTicketHint;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterRestoreExpiredResponse characterRestoreExpiredResponse = (CharacterRestoreExpiredResponse) o;
    return Objects.equals(this.characterId, characterRestoreExpiredResponse.characterId) &&
        Objects.equals(this.lastAvailableDate, characterRestoreExpiredResponse.lastAvailableDate) &&
        Objects.equals(this.supportTicketHint, characterRestoreExpiredResponse.supportTicketHint);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, lastAvailableDate, supportTicketHint);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterRestoreExpiredResponse {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    lastAvailableDate: ").append(toIndentedString(lastAvailableDate)).append("\n");
    sb.append("    supportTicketHint: ").append(toIndentedString(supportTicketHint)).append("\n");
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

