package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.EventEffect;
import com.necpgame.backjava.model.WorldEventDetailedAllOfFactionInvolvement;
import com.necpgame.backjava.model.WorldEventDetailedAllOfQuestHooks;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * WorldEventDetailed
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class WorldEventDetailed {

  private @Nullable String eventId;

  private @Nullable String name;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    GLOBAL("GLOBAL"),
    
    REGIONAL("REGIONAL"),
    
    LOCAL("LOCAL"),
    
    FACTION("FACTION"),
    
    ECONOMIC("ECONOMIC"),
    
    TECHNOLOGICAL("TECHNOLOGICAL");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TypeEnum type;

  private @Nullable String era;

  /**
   * Gets or Sets severity
   */
  public enum SeverityEnum {
    MINOR("MINOR"),
    
    MODERATE("MODERATE"),
    
    MAJOR("MAJOR"),
    
    CATASTROPHIC("CATASTROPHIC");

    private final String value;

    SeverityEnum(String value) {
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
    public static SeverityEnum fromValue(String value) {
      for (SeverityEnum b : SeverityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable SeverityEnum severity;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<OffsetDateTime> endDate = JsonNullable.<OffsetDateTime>undefined();

  private @Nullable Boolean isActive;

  @Valid
  private List<String> affectedRegions = new ArrayList<>();

  private @Nullable String description;

  private @Nullable String loreBackground;

  @Valid
  private List<@Valid EventEffect> effects = new ArrayList<>();

  @Valid
  private List<@Valid WorldEventDetailedAllOfQuestHooks> questHooks = new ArrayList<>();

  @Valid
  private List<@Valid WorldEventDetailedAllOfFactionInvolvement> factionInvolvement = new ArrayList<>();

  public WorldEventDetailed eventId(@Nullable String eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * Get eventId
   * @return eventId
   */
  
  @Schema(name = "event_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_id")
  public @Nullable String getEventId() {
    return eventId;
  }

  public void setEventId(@Nullable String eventId) {
    this.eventId = eventId;
  }

  public WorldEventDetailed name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", example = "The DataKrash (2020)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public WorldEventDetailed type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public WorldEventDetailed era(@Nullable String era) {
    this.era = era;
    return this;
  }

  /**
   * Get era
   * @return era
   */
  
  @Schema(name = "era", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("era")
  public @Nullable String getEra() {
    return era;
  }

  public void setEra(@Nullable String era) {
    this.era = era;
  }

  public WorldEventDetailed severity(@Nullable SeverityEnum severity) {
    this.severity = severity;
    return this;
  }

  /**
   * Get severity
   * @return severity
   */
  
  @Schema(name = "severity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("severity")
  public @Nullable SeverityEnum getSeverity() {
    return severity;
  }

  public void setSeverity(@Nullable SeverityEnum severity) {
    this.severity = severity;
  }

  public WorldEventDetailed startDate(@Nullable OffsetDateTime startDate) {
    this.startDate = startDate;
    return this;
  }

  /**
   * Get startDate
   * @return startDate
   */
  @Valid 
  @Schema(name = "start_date", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("start_date")
  public @Nullable OffsetDateTime getStartDate() {
    return startDate;
  }

  public void setStartDate(@Nullable OffsetDateTime startDate) {
    this.startDate = startDate;
  }

  public WorldEventDetailed endDate(OffsetDateTime endDate) {
    this.endDate = JsonNullable.of(endDate);
    return this;
  }

  /**
   * Get endDate
   * @return endDate
   */
  @Valid 
  @Schema(name = "end_date", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("end_date")
  public JsonNullable<OffsetDateTime> getEndDate() {
    return endDate;
  }

  public void setEndDate(JsonNullable<OffsetDateTime> endDate) {
    this.endDate = endDate;
  }

  public WorldEventDetailed isActive(@Nullable Boolean isActive) {
    this.isActive = isActive;
    return this;
  }

  /**
   * Get isActive
   * @return isActive
   */
  
  @Schema(name = "is_active", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("is_active")
  public @Nullable Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(@Nullable Boolean isActive) {
    this.isActive = isActive;
  }

  public WorldEventDetailed affectedRegions(List<String> affectedRegions) {
    this.affectedRegions = affectedRegions;
    return this;
  }

  public WorldEventDetailed addAffectedRegionsItem(String affectedRegionsItem) {
    if (this.affectedRegions == null) {
      this.affectedRegions = new ArrayList<>();
    }
    this.affectedRegions.add(affectedRegionsItem);
    return this;
  }

  /**
   * Get affectedRegions
   * @return affectedRegions
   */
  
  @Schema(name = "affected_regions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affected_regions")
  public List<String> getAffectedRegions() {
    return affectedRegions;
  }

  public void setAffectedRegions(List<String> affectedRegions) {
    this.affectedRegions = affectedRegions;
  }

  public WorldEventDetailed description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public WorldEventDetailed loreBackground(@Nullable String loreBackground) {
    this.loreBackground = loreBackground;
    return this;
  }

  /**
   * Get loreBackground
   * @return loreBackground
   */
  
  @Schema(name = "lore_background", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lore_background")
  public @Nullable String getLoreBackground() {
    return loreBackground;
  }

  public void setLoreBackground(@Nullable String loreBackground) {
    this.loreBackground = loreBackground;
  }

  public WorldEventDetailed effects(List<@Valid EventEffect> effects) {
    this.effects = effects;
    return this;
  }

  public WorldEventDetailed addEffectsItem(EventEffect effectsItem) {
    if (this.effects == null) {
      this.effects = new ArrayList<>();
    }
    this.effects.add(effectsItem);
    return this;
  }

  /**
   * Get effects
   * @return effects
   */
  @Valid 
  @Schema(name = "effects", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("effects")
  public List<@Valid EventEffect> getEffects() {
    return effects;
  }

  public void setEffects(List<@Valid EventEffect> effects) {
    this.effects = effects;
  }

  public WorldEventDetailed questHooks(List<@Valid WorldEventDetailedAllOfQuestHooks> questHooks) {
    this.questHooks = questHooks;
    return this;
  }

  public WorldEventDetailed addQuestHooksItem(WorldEventDetailedAllOfQuestHooks questHooksItem) {
    if (this.questHooks == null) {
      this.questHooks = new ArrayList<>();
    }
    this.questHooks.add(questHooksItem);
    return this;
  }

  /**
   * Квесты, связанные с событием
   * @return questHooks
   */
  @Valid 
  @Schema(name = "quest_hooks", description = "Квесты, связанные с событием", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quest_hooks")
  public List<@Valid WorldEventDetailedAllOfQuestHooks> getQuestHooks() {
    return questHooks;
  }

  public void setQuestHooks(List<@Valid WorldEventDetailedAllOfQuestHooks> questHooks) {
    this.questHooks = questHooks;
  }

  public WorldEventDetailed factionInvolvement(List<@Valid WorldEventDetailedAllOfFactionInvolvement> factionInvolvement) {
    this.factionInvolvement = factionInvolvement;
    return this;
  }

  public WorldEventDetailed addFactionInvolvementItem(WorldEventDetailedAllOfFactionInvolvement factionInvolvementItem) {
    if (this.factionInvolvement == null) {
      this.factionInvolvement = new ArrayList<>();
    }
    this.factionInvolvement.add(factionInvolvementItem);
    return this;
  }

  /**
   * Get factionInvolvement
   * @return factionInvolvement
   */
  @Valid 
  @Schema(name = "faction_involvement", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction_involvement")
  public List<@Valid WorldEventDetailedAllOfFactionInvolvement> getFactionInvolvement() {
    return factionInvolvement;
  }

  public void setFactionInvolvement(List<@Valid WorldEventDetailedAllOfFactionInvolvement> factionInvolvement) {
    this.factionInvolvement = factionInvolvement;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WorldEventDetailed worldEventDetailed = (WorldEventDetailed) o;
    return Objects.equals(this.eventId, worldEventDetailed.eventId) &&
        Objects.equals(this.name, worldEventDetailed.name) &&
        Objects.equals(this.type, worldEventDetailed.type) &&
        Objects.equals(this.era, worldEventDetailed.era) &&
        Objects.equals(this.severity, worldEventDetailed.severity) &&
        Objects.equals(this.startDate, worldEventDetailed.startDate) &&
        equalsNullable(this.endDate, worldEventDetailed.endDate) &&
        Objects.equals(this.isActive, worldEventDetailed.isActive) &&
        Objects.equals(this.affectedRegions, worldEventDetailed.affectedRegions) &&
        Objects.equals(this.description, worldEventDetailed.description) &&
        Objects.equals(this.loreBackground, worldEventDetailed.loreBackground) &&
        Objects.equals(this.effects, worldEventDetailed.effects) &&
        Objects.equals(this.questHooks, worldEventDetailed.questHooks) &&
        Objects.equals(this.factionInvolvement, worldEventDetailed.factionInvolvement);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, name, type, era, severity, startDate, hashCodeNullable(endDate), isActive, affectedRegions, description, loreBackground, effects, questHooks, factionInvolvement);
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WorldEventDetailed {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    era: ").append(toIndentedString(era)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
    sb.append("    isActive: ").append(toIndentedString(isActive)).append("\n");
    sb.append("    affectedRegions: ").append(toIndentedString(affectedRegions)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    loreBackground: ").append(toIndentedString(loreBackground)).append("\n");
    sb.append("    effects: ").append(toIndentedString(effects)).append("\n");
    sb.append("    questHooks: ").append(toIndentedString(questHooks)).append("\n");
    sb.append("    factionInvolvement: ").append(toIndentedString(factionInvolvement)).append("\n");
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

