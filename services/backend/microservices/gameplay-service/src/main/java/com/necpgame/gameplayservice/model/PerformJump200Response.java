package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PerformJump200Response
 */

@JsonTypeName("performJump_200_response")

public class PerformJump200Response {

  private @Nullable Boolean success;

  private @Nullable String characterId;

  private @Nullable BigDecimal staminaCost;

  private @Nullable BigDecimal distance;

  private @Nullable BigDecimal airTime;

  /**
   * Gets or Sets landingQuality
   */
  public enum LandingQualityEnum {
    PERFECT("perfect"),
    
    GOOD("good"),
    
    ROUGH("rough"),
    
    FAILED("failed");

    private final String value;

    LandingQualityEnum(String value) {
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
    public static LandingQualityEnum fromValue(String value) {
      for (LandingQualityEnum b : LandingQualityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable LandingQualityEnum landingQuality;

  private @Nullable BigDecimal damageTaken;

  public PerformJump200Response success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public PerformJump200Response characterId(@Nullable String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable String characterId) {
    this.characterId = characterId;
  }

  public PerformJump200Response staminaCost(@Nullable BigDecimal staminaCost) {
    this.staminaCost = staminaCost;
    return this;
  }

  /**
   * Get staminaCost
   * @return staminaCost
   */
  @Valid 
  @Schema(name = "stamina_cost", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stamina_cost")
  public @Nullable BigDecimal getStaminaCost() {
    return staminaCost;
  }

  public void setStaminaCost(@Nullable BigDecimal staminaCost) {
    this.staminaCost = staminaCost;
  }

  public PerformJump200Response distance(@Nullable BigDecimal distance) {
    this.distance = distance;
    return this;
  }

  /**
   * Get distance
   * @return distance
   */
  @Valid 
  @Schema(name = "distance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("distance")
  public @Nullable BigDecimal getDistance() {
    return distance;
  }

  public void setDistance(@Nullable BigDecimal distance) {
    this.distance = distance;
  }

  public PerformJump200Response airTime(@Nullable BigDecimal airTime) {
    this.airTime = airTime;
    return this;
  }

  /**
   * Время в воздухе (секунды)
   * @return airTime
   */
  @Valid 
  @Schema(name = "air_time", description = "Время в воздухе (секунды)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("air_time")
  public @Nullable BigDecimal getAirTime() {
    return airTime;
  }

  public void setAirTime(@Nullable BigDecimal airTime) {
    this.airTime = airTime;
  }

  public PerformJump200Response landingQuality(@Nullable LandingQualityEnum landingQuality) {
    this.landingQuality = landingQuality;
    return this;
  }

  /**
   * Get landingQuality
   * @return landingQuality
   */
  
  @Schema(name = "landing_quality", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("landing_quality")
  public @Nullable LandingQualityEnum getLandingQuality() {
    return landingQuality;
  }

  public void setLandingQuality(@Nullable LandingQualityEnum landingQuality) {
    this.landingQuality = landingQuality;
  }

  public PerformJump200Response damageTaken(@Nullable BigDecimal damageTaken) {
    this.damageTaken = damageTaken;
    return this;
  }

  /**
   * Урон от падения (если есть)
   * @return damageTaken
   */
  @Valid 
  @Schema(name = "damage_taken", description = "Урон от падения (если есть)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("damage_taken")
  public @Nullable BigDecimal getDamageTaken() {
    return damageTaken;
  }

  public void setDamageTaken(@Nullable BigDecimal damageTaken) {
    this.damageTaken = damageTaken;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PerformJump200Response performJump200Response = (PerformJump200Response) o;
    return Objects.equals(this.success, performJump200Response.success) &&
        Objects.equals(this.characterId, performJump200Response.characterId) &&
        Objects.equals(this.staminaCost, performJump200Response.staminaCost) &&
        Objects.equals(this.distance, performJump200Response.distance) &&
        Objects.equals(this.airTime, performJump200Response.airTime) &&
        Objects.equals(this.landingQuality, performJump200Response.landingQuality) &&
        Objects.equals(this.damageTaken, performJump200Response.damageTaken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, characterId, staminaCost, distance, airTime, landingQuality, damageTaken);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PerformJump200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    staminaCost: ").append(toIndentedString(staminaCost)).append("\n");
    sb.append("    distance: ").append(toIndentedString(distance)).append("\n");
    sb.append("    airTime: ").append(toIndentedString(airTime)).append("\n");
    sb.append("    landingQuality: ").append(toIndentedString(landingQuality)).append("\n");
    sb.append("    damageTaken: ").append(toIndentedString(damageTaken)).append("\n");
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

