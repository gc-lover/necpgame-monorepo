package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AllianceRequest
 */


public class AllianceRequest {

  /**
   * Gets or Sets action
   */
  public enum ActionEnum {
    INVITE("invite"),
    
    ACCEPT("accept"),
    
    DECLINE("decline"),
    
    REVOKE("revoke");

    private final String value;

    ActionEnum(String value) {
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
    public static ActionEnum fromValue(String value) {
      for (ActionEnum b : ActionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ActionEnum action;

  private String clanId;

  private @Nullable Integer sharedRewardsPercent;

  public AllianceRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AllianceRequest(ActionEnum action, String clanId) {
    this.action = action;
    this.clanId = clanId;
  }

  public AllianceRequest action(ActionEnum action) {
    this.action = action;
    return this;
  }

  /**
   * Get action
   * @return action
   */
  @NotNull 
  @Schema(name = "action", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("action")
  public ActionEnum getAction() {
    return action;
  }

  public void setAction(ActionEnum action) {
    this.action = action;
  }

  public AllianceRequest clanId(String clanId) {
    this.clanId = clanId;
    return this;
  }

  /**
   * Get clanId
   * @return clanId
   */
  @NotNull 
  @Schema(name = "clanId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("clanId")
  public String getClanId() {
    return clanId;
  }

  public void setClanId(String clanId) {
    this.clanId = clanId;
  }

  public AllianceRequest sharedRewardsPercent(@Nullable Integer sharedRewardsPercent) {
    this.sharedRewardsPercent = sharedRewardsPercent;
    return this;
  }

  /**
   * Get sharedRewardsPercent
   * minimum: 0
   * maximum: 50
   * @return sharedRewardsPercent
   */
  @Min(value = 0) @Max(value = 50) 
  @Schema(name = "sharedRewardsPercent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sharedRewardsPercent")
  public @Nullable Integer getSharedRewardsPercent() {
    return sharedRewardsPercent;
  }

  public void setSharedRewardsPercent(@Nullable Integer sharedRewardsPercent) {
    this.sharedRewardsPercent = sharedRewardsPercent;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AllianceRequest allianceRequest = (AllianceRequest) o;
    return Objects.equals(this.action, allianceRequest.action) &&
        Objects.equals(this.clanId, allianceRequest.clanId) &&
        Objects.equals(this.sharedRewardsPercent, allianceRequest.sharedRewardsPercent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(action, clanId, sharedRewardsPercent);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AllianceRequest {\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    clanId: ").append(toIndentedString(clanId)).append("\n");
    sb.append("    sharedRewardsPercent: ").append(toIndentedString(sharedRewardsPercent)).append("\n");
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

