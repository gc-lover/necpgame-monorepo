package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.CheckRequestSituationModifiersInner;
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
 * CheckRequest
 */


public class CheckRequest {

  private String characterId;

  /**
   * Gets or Sets diceType
   */
  public enum DiceTypeEnum {
    D20("d20"),
    
    D100("d100");

    private final String value;

    DiceTypeEnum(String value) {
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
    public static DiceTypeEnum fromValue(String value) {
      for (DiceTypeEnum b : DiceTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private DiceTypeEnum diceType = DiceTypeEnum.D20;

  /**
   * Gets or Sets checkType
   */
  public enum CheckTypeEnum {
    SKILL("skill"),
    
    COMBAT("combat"),
    
    SOCIAL("social"),
    
    CRAFTING("crafting"),
    
    HACKING("hacking");

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

  /**
   * Gets or Sets attribute
   */
  public enum AttributeEnum {
    BODY("body"),
    
    REFLEX("reflex"),
    
    TECH("tech"),
    
    INTELLIGENCE("intelligence"),
    
    COOL("cool");

    private final String value;

    AttributeEnum(String value) {
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
    public static AttributeEnum fromValue(String value) {
      for (AttributeEnum b : AttributeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable AttributeEnum attribute;

  private @Nullable String skill;

  private Integer dc;

  @Valid
  private List<@Valid CheckRequestSituationModifiersInner> situationModifiers = new ArrayList<>();

  private Boolean advantage = false;

  private Boolean disadvantage = false;

  public CheckRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CheckRequest(String characterId, DiceTypeEnum diceType, CheckTypeEnum checkType, Integer dc) {
    this.characterId = characterId;
    this.diceType = diceType;
    this.checkType = checkType;
    this.dc = dc;
  }

  public CheckRequest characterId(String characterId) {
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

  public CheckRequest diceType(DiceTypeEnum diceType) {
    this.diceType = diceType;
    return this;
  }

  /**
   * Get diceType
   * @return diceType
   */
  @NotNull 
  @Schema(name = "dice_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("dice_type")
  public DiceTypeEnum getDiceType() {
    return diceType;
  }

  public void setDiceType(DiceTypeEnum diceType) {
    this.diceType = diceType;
  }

  public CheckRequest checkType(CheckTypeEnum checkType) {
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

  public CheckRequest attribute(@Nullable AttributeEnum attribute) {
    this.attribute = attribute;
    return this;
  }

  /**
   * Get attribute
   * @return attribute
   */
  
  @Schema(name = "attribute", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attribute")
  public @Nullable AttributeEnum getAttribute() {
    return attribute;
  }

  public void setAttribute(@Nullable AttributeEnum attribute) {
    this.attribute = attribute;
  }

  public CheckRequest skill(@Nullable String skill) {
    this.skill = skill;
    return this;
  }

  /**
   * Навык для бонуса
   * @return skill
   */
  
  @Schema(name = "skill", description = "Навык для бонуса", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill")
  public @Nullable String getSkill() {
    return skill;
  }

  public void setSkill(@Nullable String skill) {
    this.skill = skill;
  }

  public CheckRequest dc(Integer dc) {
    this.dc = dc;
    return this;
  }

  /**
   * Difficulty Class
   * minimum: 1
   * @return dc
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "dc", description = "Difficulty Class", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("dc")
  public Integer getDc() {
    return dc;
  }

  public void setDc(Integer dc) {
    this.dc = dc;
  }

  public CheckRequest situationModifiers(List<@Valid CheckRequestSituationModifiersInner> situationModifiers) {
    this.situationModifiers = situationModifiers;
    return this;
  }

  public CheckRequest addSituationModifiersItem(CheckRequestSituationModifiersInner situationModifiersItem) {
    if (this.situationModifiers == null) {
      this.situationModifiers = new ArrayList<>();
    }
    this.situationModifiers.add(situationModifiersItem);
    return this;
  }

  /**
   * Get situationModifiers
   * @return situationModifiers
   */
  @Valid 
  @Schema(name = "situation_modifiers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("situation_modifiers")
  public List<@Valid CheckRequestSituationModifiersInner> getSituationModifiers() {
    return situationModifiers;
  }

  public void setSituationModifiers(List<@Valid CheckRequestSituationModifiersInner> situationModifiers) {
    this.situationModifiers = situationModifiers;
  }

  public CheckRequest advantage(Boolean advantage) {
    this.advantage = advantage;
    return this;
  }

  /**
   * Get advantage
   * @return advantage
   */
  
  @Schema(name = "advantage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("advantage")
  public Boolean getAdvantage() {
    return advantage;
  }

  public void setAdvantage(Boolean advantage) {
    this.advantage = advantage;
  }

  public CheckRequest disadvantage(Boolean disadvantage) {
    this.disadvantage = disadvantage;
    return this;
  }

  /**
   * Get disadvantage
   * @return disadvantage
   */
  
  @Schema(name = "disadvantage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("disadvantage")
  public Boolean getDisadvantage() {
    return disadvantage;
  }

  public void setDisadvantage(Boolean disadvantage) {
    this.disadvantage = disadvantage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CheckRequest checkRequest = (CheckRequest) o;
    return Objects.equals(this.characterId, checkRequest.characterId) &&
        Objects.equals(this.diceType, checkRequest.diceType) &&
        Objects.equals(this.checkType, checkRequest.checkType) &&
        Objects.equals(this.attribute, checkRequest.attribute) &&
        Objects.equals(this.skill, checkRequest.skill) &&
        Objects.equals(this.dc, checkRequest.dc) &&
        Objects.equals(this.situationModifiers, checkRequest.situationModifiers) &&
        Objects.equals(this.advantage, checkRequest.advantage) &&
        Objects.equals(this.disadvantage, checkRequest.disadvantage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, diceType, checkType, attribute, skill, dc, situationModifiers, advantage, disadvantage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CheckRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    diceType: ").append(toIndentedString(diceType)).append("\n");
    sb.append("    checkType: ").append(toIndentedString(checkType)).append("\n");
    sb.append("    attribute: ").append(toIndentedString(attribute)).append("\n");
    sb.append("    skill: ").append(toIndentedString(skill)).append("\n");
    sb.append("    dc: ").append(toIndentedString(dc)).append("\n");
    sb.append("    situationModifiers: ").append(toIndentedString(situationModifiers)).append("\n");
    sb.append("    advantage: ").append(toIndentedString(advantage)).append("\n");
    sb.append("    disadvantage: ").append(toIndentedString(disadvantage)).append("\n");
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

