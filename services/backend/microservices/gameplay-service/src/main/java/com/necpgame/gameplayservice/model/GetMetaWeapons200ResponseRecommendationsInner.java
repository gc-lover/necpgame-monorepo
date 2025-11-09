package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * GetMetaWeapons200ResponseRecommendationsInner
 */

@JsonTypeName("getMetaWeapons_200_response_recommendations_inner")

public class GetMetaWeapons200ResponseRecommendationsInner {

  private @Nullable String weaponId;

  /**
   * Gets or Sets rank
   */
  public enum RankEnum {
    S("S"),
    
    A("A"),
    
    B("B"),
    
    C("C"),
    
    D("D");

    private final String value;

    RankEnum(String value) {
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
    public static RankEnum fromValue(String value) {
      for (RankEnum b : RankEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RankEnum rank;

  private @Nullable String reason;

  public GetMetaWeapons200ResponseRecommendationsInner weaponId(@Nullable String weaponId) {
    this.weaponId = weaponId;
    return this;
  }

  /**
   * Get weaponId
   * @return weaponId
   */
  
  @Schema(name = "weapon_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("weapon_id")
  public @Nullable String getWeaponId() {
    return weaponId;
  }

  public void setWeaponId(@Nullable String weaponId) {
    this.weaponId = weaponId;
  }

  public GetMetaWeapons200ResponseRecommendationsInner rank(@Nullable RankEnum rank) {
    this.rank = rank;
    return this;
  }

  /**
   * Get rank
   * @return rank
   */
  
  @Schema(name = "rank", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rank")
  public @Nullable RankEnum getRank() {
    return rank;
  }

  public void setRank(@Nullable RankEnum rank) {
    this.rank = rank;
  }

  public GetMetaWeapons200ResponseRecommendationsInner reason(@Nullable String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reason")
  public @Nullable String getReason() {
    return reason;
  }

  public void setReason(@Nullable String reason) {
    this.reason = reason;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetMetaWeapons200ResponseRecommendationsInner getMetaWeapons200ResponseRecommendationsInner = (GetMetaWeapons200ResponseRecommendationsInner) o;
    return Objects.equals(this.weaponId, getMetaWeapons200ResponseRecommendationsInner.weaponId) &&
        Objects.equals(this.rank, getMetaWeapons200ResponseRecommendationsInner.rank) &&
        Objects.equals(this.reason, getMetaWeapons200ResponseRecommendationsInner.reason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(weaponId, rank, reason);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetMetaWeapons200ResponseRecommendationsInner {\n");
    sb.append("    weaponId: ").append(toIndentedString(weaponId)).append("\n");
    sb.append("    rank: ").append(toIndentedString(rank)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
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

