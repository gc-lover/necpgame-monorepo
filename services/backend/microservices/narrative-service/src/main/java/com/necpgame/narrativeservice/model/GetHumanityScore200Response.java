package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetHumanityScore200Response
 */

@JsonTypeName("getHumanityScore_200_response")

public class GetHumanityScore200Response {

  private @Nullable String characterId;

  private @Nullable BigDecimal humanityScore;

  /**
   * Gets or Sets alignment
   */
  public enum AlignmentEnum {
    TRANSHUMANIST("transhumanist"),
    
    NEUTRAL("neutral"),
    
    HUMANIST("humanist");

    private final String value;

    AlignmentEnum(String value) {
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
    public static AlignmentEnum fromValue(String value) {
      for (AlignmentEnum b : AlignmentEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable AlignmentEnum alignment;

  @Valid
  private List<Object> keyMoments = new ArrayList<>();

  public GetHumanityScore200Response characterId(@Nullable String characterId) {
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

  public GetHumanityScore200Response humanityScore(@Nullable BigDecimal humanityScore) {
    this.humanityScore = humanityScore;
    return this;
  }

  /**
   * 0 = Трансгуманизм, 100 = Человечность
   * minimum: 0
   * maximum: 100
   * @return humanityScore
   */
  @Valid @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "humanity_score", description = "0 = Трансгуманизм, 100 = Человечность", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("humanity_score")
  public @Nullable BigDecimal getHumanityScore() {
    return humanityScore;
  }

  public void setHumanityScore(@Nullable BigDecimal humanityScore) {
    this.humanityScore = humanityScore;
  }

  public GetHumanityScore200Response alignment(@Nullable AlignmentEnum alignment) {
    this.alignment = alignment;
    return this;
  }

  /**
   * Get alignment
   * @return alignment
   */
  
  @Schema(name = "alignment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("alignment")
  public @Nullable AlignmentEnum getAlignment() {
    return alignment;
  }

  public void setAlignment(@Nullable AlignmentEnum alignment) {
    this.alignment = alignment;
  }

  public GetHumanityScore200Response keyMoments(List<Object> keyMoments) {
    this.keyMoments = keyMoments;
    return this;
  }

  public GetHumanityScore200Response addKeyMomentsItem(Object keyMomentsItem) {
    if (this.keyMoments == null) {
      this.keyMoments = new ArrayList<>();
    }
    this.keyMoments.add(keyMomentsItem);
    return this;
  }

  /**
   * Моменты, повлиявшие на шкалу
   * @return keyMoments
   */
  
  @Schema(name = "key_moments", description = "Моменты, повлиявшие на шкалу", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("key_moments")
  public List<Object> getKeyMoments() {
    return keyMoments;
  }

  public void setKeyMoments(List<Object> keyMoments) {
    this.keyMoments = keyMoments;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetHumanityScore200Response getHumanityScore200Response = (GetHumanityScore200Response) o;
    return Objects.equals(this.characterId, getHumanityScore200Response.characterId) &&
        Objects.equals(this.humanityScore, getHumanityScore200Response.humanityScore) &&
        Objects.equals(this.alignment, getHumanityScore200Response.alignment) &&
        Objects.equals(this.keyMoments, getHumanityScore200Response.keyMoments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, humanityScore, alignment, keyMoments);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetHumanityScore200Response {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    humanityScore: ").append(toIndentedString(humanityScore)).append("\n");
    sb.append("    alignment: ").append(toIndentedString(alignment)).append("\n");
    sb.append("    keyMoments: ").append(toIndentedString(keyMoments)).append("\n");
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

