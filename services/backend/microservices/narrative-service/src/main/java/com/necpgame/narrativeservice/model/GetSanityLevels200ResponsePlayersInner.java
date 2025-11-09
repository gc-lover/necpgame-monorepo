package com.necpgame.narrativeservice.model;

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
 * GetSanityLevels200ResponsePlayersInner
 */

@JsonTypeName("getSanityLevels_200_response_players_inner")

public class GetSanityLevels200ResponsePlayersInner {

  private @Nullable String characterId;

  private @Nullable BigDecimal sanityLevel;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    STABLE("stable"),
    
    UNSTABLE("unstable"),
    
    CRITICAL("critical"),
    
    INSANE("insane");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  public GetSanityLevels200ResponsePlayersInner characterId(@Nullable String characterId) {
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

  public GetSanityLevels200ResponsePlayersInner sanityLevel(@Nullable BigDecimal sanityLevel) {
    this.sanityLevel = sanityLevel;
    return this;
  }

  /**
   * Get sanityLevel
   * minimum: 0
   * maximum: 100
   * @return sanityLevel
   */
  @Valid @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "sanity_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sanity_level")
  public @Nullable BigDecimal getSanityLevel() {
    return sanityLevel;
  }

  public void setSanityLevel(@Nullable BigDecimal sanityLevel) {
    this.sanityLevel = sanityLevel;
  }

  public GetSanityLevels200ResponsePlayersInner status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetSanityLevels200ResponsePlayersInner getSanityLevels200ResponsePlayersInner = (GetSanityLevels200ResponsePlayersInner) o;
    return Objects.equals(this.characterId, getSanityLevels200ResponsePlayersInner.characterId) &&
        Objects.equals(this.sanityLevel, getSanityLevels200ResponsePlayersInner.sanityLevel) &&
        Objects.equals(this.status, getSanityLevels200ResponsePlayersInner.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, sanityLevel, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetSanityLevels200ResponsePlayersInner {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    sanityLevel: ").append(toIndentedString(sanityLevel)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

