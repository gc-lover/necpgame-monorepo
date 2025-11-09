package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.PerformMicroCheckRequestModifiersInner;
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
 * PerformMicroCheckRequest
 */

@JsonTypeName("performMicroCheck_request")

public class PerformMicroCheckRequest {

  private String characterId;

  /**
   * Gets or Sets checkType
   */
  public enum CheckTypeEnum {
    PARKOUR_UNDER_FIRE("parkour_under_fire"),
    
    SNAPSHOT_SHOT("snapshot_shot"),
    
    DOOR_BREACH("door_breach"),
    
    STEALTH_KILL("stealth_kill"),
    
    QUICK_HACK("quick_hack");

    private final String value;

    CheckTypeEnum(String value) {
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
    public static CheckTypeEnum fromValue(String value) {
      for (CheckTypeEnum b : CheckTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private CheckTypeEnum checkType;

  private Integer dc;

  @Valid
  private List<@Valid PerformMicroCheckRequestModifiersInner> modifiers = new ArrayList<>();

  public PerformMicroCheckRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PerformMicroCheckRequest(String characterId, CheckTypeEnum checkType, Integer dc) {
    this.characterId = characterId;
    this.checkType = checkType;
    this.dc = dc;
  }

  public PerformMicroCheckRequest characterId(String characterId) {
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

  public PerformMicroCheckRequest checkType(CheckTypeEnum checkType) {
    this.checkType = checkType;
    return this;
  }

  /**
   * Get checkType
   * @return checkType
   */
  @NotNull 
  @Schema(name = "check_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("check_type")
  public CheckTypeEnum getCheckType() {
    return checkType;
  }

  public void setCheckType(CheckTypeEnum checkType) {
    this.checkType = checkType;
  }

  public PerformMicroCheckRequest dc(Integer dc) {
    this.dc = dc;
    return this;
  }

  /**
   * Get dc
   * @return dc
   */
  @NotNull 
  @Schema(name = "dc", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("dc")
  public Integer getDc() {
    return dc;
  }

  public void setDc(Integer dc) {
    this.dc = dc;
  }

  public PerformMicroCheckRequest modifiers(List<@Valid PerformMicroCheckRequestModifiersInner> modifiers) {
    this.modifiers = modifiers;
    return this;
  }

  public PerformMicroCheckRequest addModifiersItem(PerformMicroCheckRequestModifiersInner modifiersItem) {
    if (this.modifiers == null) {
      this.modifiers = new ArrayList<>();
    }
    this.modifiers.add(modifiersItem);
    return this;
  }

  /**
   * Get modifiers
   * @return modifiers
   */
  @Valid 
  @Schema(name = "modifiers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modifiers")
  public List<@Valid PerformMicroCheckRequestModifiersInner> getModifiers() {
    return modifiers;
  }

  public void setModifiers(List<@Valid PerformMicroCheckRequestModifiersInner> modifiers) {
    this.modifiers = modifiers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PerformMicroCheckRequest performMicroCheckRequest = (PerformMicroCheckRequest) o;
    return Objects.equals(this.characterId, performMicroCheckRequest.characterId) &&
        Objects.equals(this.checkType, performMicroCheckRequest.checkType) &&
        Objects.equals(this.dc, performMicroCheckRequest.dc) &&
        Objects.equals(this.modifiers, performMicroCheckRequest.modifiers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, checkType, dc, modifiers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PerformMicroCheckRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    checkType: ").append(toIndentedString(checkType)).append("\n");
    sb.append("    dc: ").append(toIndentedString(dc)).append("\n");
    sb.append("    modifiers: ").append(toIndentedString(modifiers)).append("\n");
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

