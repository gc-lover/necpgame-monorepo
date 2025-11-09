package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * PlayerOrderTeamRequirement
 */


public class PlayerOrderTeamRequirement {

  private Integer slots;

  @Valid
  private List<String> requiredSkills = new ArrayList<>();

  /**
   * Gets or Sets minimumRating
   */
  public enum MinimumRatingEnum {
    BRONZE("bronze"),
    
    SILVER("silver"),
    
    GOLD("gold"),
    
    PLATINUM("platinum"),
    
    LEGENDARY("legendary");

    private final String value;

    MinimumRatingEnum(String value) {
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
    public static MinimumRatingEnum fromValue(String value) {
      for (MinimumRatingEnum b : MinimumRatingEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable MinimumRatingEnum minimumRating;

  private @Nullable Boolean allowNpcBrokers;

  public PlayerOrderTeamRequirement() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderTeamRequirement(Integer slots) {
    this.slots = slots;
  }

  public PlayerOrderTeamRequirement slots(Integer slots) {
    this.slots = slots;
    return this;
  }

  /**
   * Количество требуемых исполнителей.
   * minimum: 1
   * @return slots
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "slots", description = "Количество требуемых исполнителей.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("slots")
  public Integer getSlots() {
    return slots;
  }

  public void setSlots(Integer slots) {
    this.slots = slots;
  }

  public PlayerOrderTeamRequirement requiredSkills(List<String> requiredSkills) {
    this.requiredSkills = requiredSkills;
    return this;
  }

  public PlayerOrderTeamRequirement addRequiredSkillsItem(String requiredSkillsItem) {
    if (this.requiredSkills == null) {
      this.requiredSkills = new ArrayList<>();
    }
    this.requiredSkills.add(requiredSkillsItem);
    return this;
  }

  /**
   * Навыки или специализации исполнителей.
   * @return requiredSkills
   */
  
  @Schema(name = "requiredSkills", description = "Навыки или специализации исполнителей.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requiredSkills")
  public List<String> getRequiredSkills() {
    return requiredSkills;
  }

  public void setRequiredSkills(List<String> requiredSkills) {
    this.requiredSkills = requiredSkills;
  }

  public PlayerOrderTeamRequirement minimumRating(@Nullable MinimumRatingEnum minimumRating) {
    this.minimumRating = minimumRating;
    return this;
  }

  /**
   * Get minimumRating
   * @return minimumRating
   */
  
  @Schema(name = "minimumRating", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("minimumRating")
  public @Nullable MinimumRatingEnum getMinimumRating() {
    return minimumRating;
  }

  public void setMinimumRating(@Nullable MinimumRatingEnum minimumRating) {
    this.minimumRating = minimumRating;
  }

  public PlayerOrderTeamRequirement allowNpcBrokers(@Nullable Boolean allowNpcBrokers) {
    this.allowNpcBrokers = allowNpcBrokers;
    return this;
  }

  /**
   * Разрешено ли использовать NPC брокеров.
   * @return allowNpcBrokers
   */
  
  @Schema(name = "allowNpcBrokers", description = "Разрешено ли использовать NPC брокеров.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("allowNpcBrokers")
  public @Nullable Boolean getAllowNpcBrokers() {
    return allowNpcBrokers;
  }

  public void setAllowNpcBrokers(@Nullable Boolean allowNpcBrokers) {
    this.allowNpcBrokers = allowNpcBrokers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderTeamRequirement playerOrderTeamRequirement = (PlayerOrderTeamRequirement) o;
    return Objects.equals(this.slots, playerOrderTeamRequirement.slots) &&
        Objects.equals(this.requiredSkills, playerOrderTeamRequirement.requiredSkills) &&
        Objects.equals(this.minimumRating, playerOrderTeamRequirement.minimumRating) &&
        Objects.equals(this.allowNpcBrokers, playerOrderTeamRequirement.allowNpcBrokers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(slots, requiredSkills, minimumRating, allowNpcBrokers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderTeamRequirement {\n");
    sb.append("    slots: ").append(toIndentedString(slots)).append("\n");
    sb.append("    requiredSkills: ").append(toIndentedString(requiredSkills)).append("\n");
    sb.append("    minimumRating: ").append(toIndentedString(minimumRating)).append("\n");
    sb.append("    allowNpcBrokers: ").append(toIndentedString(allowNpcBrokers)).append("\n");
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

