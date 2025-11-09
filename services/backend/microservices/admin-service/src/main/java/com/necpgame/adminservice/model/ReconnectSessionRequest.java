package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ReconnectSessionRequest
 */

@JsonTypeName("reconnectSession_request")

public class ReconnectSessionRequest {

  private String accountId;

  private String characterId;

  private @Nullable String previousSessionId;

  public ReconnectSessionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ReconnectSessionRequest(String accountId, String characterId) {
    this.accountId = accountId;
    this.characterId = characterId;
  }

  public ReconnectSessionRequest accountId(String accountId) {
    this.accountId = accountId;
    return this;
  }

  /**
   * Get accountId
   * @return accountId
   */
  @NotNull 
  @Schema(name = "account_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("account_id")
  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public ReconnectSessionRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public ReconnectSessionRequest previousSessionId(@Nullable String previousSessionId) {
    this.previousSessionId = previousSessionId;
    return this;
  }

  /**
   * Get previousSessionId
   * @return previousSessionId
   */
  
  @Schema(name = "previous_session_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("previous_session_id")
  public @Nullable String getPreviousSessionId() {
    return previousSessionId;
  }

  public void setPreviousSessionId(@Nullable String previousSessionId) {
    this.previousSessionId = previousSessionId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReconnectSessionRequest reconnectSessionRequest = (ReconnectSessionRequest) o;
    return Objects.equals(this.accountId, reconnectSessionRequest.accountId) &&
        Objects.equals(this.characterId, reconnectSessionRequest.characterId) &&
        Objects.equals(this.previousSessionId, reconnectSessionRequest.previousSessionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, characterId, previousSessionId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReconnectSessionRequest {\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    previousSessionId: ").append(toIndentedString(previousSessionId)).append("\n");
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

