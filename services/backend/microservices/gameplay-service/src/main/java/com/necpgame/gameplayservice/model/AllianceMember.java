package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
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
 * AllianceMember
 */


public class AllianceMember {

  private @Nullable String clanId;

  /**
   * Gets or Sets role
   */
  public enum RoleEnum {
    ALLY("ally"),
    
    MERCENARY("mercenary");

    private final String value;

    RoleEnum(String value) {
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
    public static RoleEnum fromValue(String value) {
      for (RoleEnum b : RoleEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RoleEnum role;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime acceptedAt;

  private @Nullable Integer sharedRewardsPercent;

  public AllianceMember clanId(@Nullable String clanId) {
    this.clanId = clanId;
    return this;
  }

  /**
   * Get clanId
   * @return clanId
   */
  
  @Schema(name = "clanId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("clanId")
  public @Nullable String getClanId() {
    return clanId;
  }

  public void setClanId(@Nullable String clanId) {
    this.clanId = clanId;
  }

  public AllianceMember role(@Nullable RoleEnum role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  
  @Schema(name = "role", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("role")
  public @Nullable RoleEnum getRole() {
    return role;
  }

  public void setRole(@Nullable RoleEnum role) {
    this.role = role;
  }

  public AllianceMember acceptedAt(@Nullable OffsetDateTime acceptedAt) {
    this.acceptedAt = acceptedAt;
    return this;
  }

  /**
   * Get acceptedAt
   * @return acceptedAt
   */
  @Valid 
  @Schema(name = "acceptedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("acceptedAt")
  public @Nullable OffsetDateTime getAcceptedAt() {
    return acceptedAt;
  }

  public void setAcceptedAt(@Nullable OffsetDateTime acceptedAt) {
    this.acceptedAt = acceptedAt;
  }

  public AllianceMember sharedRewardsPercent(@Nullable Integer sharedRewardsPercent) {
    this.sharedRewardsPercent = sharedRewardsPercent;
    return this;
  }

  /**
   * Get sharedRewardsPercent
   * minimum: 0
   * maximum: 100
   * @return sharedRewardsPercent
   */
  @Min(value = 0) @Max(value = 100) 
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
    AllianceMember allianceMember = (AllianceMember) o;
    return Objects.equals(this.clanId, allianceMember.clanId) &&
        Objects.equals(this.role, allianceMember.role) &&
        Objects.equals(this.acceptedAt, allianceMember.acceptedAt) &&
        Objects.equals(this.sharedRewardsPercent, allianceMember.sharedRewardsPercent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clanId, role, acceptedAt, sharedRewardsPercent);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AllianceMember {\n");
    sb.append("    clanId: ").append(toIndentedString(clanId)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    acceptedAt: ").append(toIndentedString(acceptedAt)).append("\n");
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

