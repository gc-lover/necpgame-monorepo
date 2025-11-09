package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * PersonalDistributionRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class PersonalDistributionRequest {

  private UUID resultId;

  private UUID playerId;

  /**
   * Gets or Sets grantMode
   */
  public enum GrantModeEnum {
    INVENTORY("INVENTORY"),
    
    MAIL("MAIL"),
    
    BANK("BANK");

    private final String value;

    GrantModeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static GrantModeEnum fromValue(String value) {
      for (GrantModeEnum b : GrantModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable GrantModeEnum grantMode;

  public PersonalDistributionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PersonalDistributionRequest(UUID resultId, UUID playerId) {
    this.resultId = resultId;
    this.playerId = playerId;
  }

  public PersonalDistributionRequest resultId(UUID resultId) {
    this.resultId = resultId;
    return this;
  }

  /**
   * Get resultId
   * @return resultId
   */
  @NotNull @Valid 
  @Schema(name = "resultId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("resultId")
  public UUID getResultId() {
    return resultId;
  }

  public void setResultId(UUID resultId) {
    this.resultId = resultId;
  }

  public PersonalDistributionRequest playerId(UUID playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull @Valid 
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public UUID getPlayerId() {
    return playerId;
  }

  public void setPlayerId(UUID playerId) {
    this.playerId = playerId;
  }

  public PersonalDistributionRequest grantMode(@Nullable GrantModeEnum grantMode) {
    this.grantMode = grantMode;
    return this;
  }

  /**
   * Get grantMode
   * @return grantMode
   */
  
  @Schema(name = "grantMode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("grantMode")
  public @Nullable GrantModeEnum getGrantMode() {
    return grantMode;
  }

  public void setGrantMode(@Nullable GrantModeEnum grantMode) {
    this.grantMode = grantMode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PersonalDistributionRequest personalDistributionRequest = (PersonalDistributionRequest) o;
    return Objects.equals(this.resultId, personalDistributionRequest.resultId) &&
        Objects.equals(this.playerId, personalDistributionRequest.playerId) &&
        Objects.equals(this.grantMode, personalDistributionRequest.grantMode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resultId, playerId, grantMode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PersonalDistributionRequest {\n");
    sb.append("    resultId: ").append(toIndentedString(resultId)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    grantMode: ").append(toIndentedString(grantMode)).append("\n");
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

