package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.worldservice.model.GlobalEventDetailsImpact;
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
 * GlobalEventDetails
 */


public class GlobalEventDetails {

  private @Nullable String eventId;

  private @Nullable String name;

  private @Nullable String type;

  private @Nullable String era;

  private @Nullable String description;

  private @Nullable String lore;

  private @Nullable GlobalEventDetailsImpact impact;

  @Valid
  private List<String> relatedQuests = new ArrayList<>();

  private @Nullable Object triggers;

  public GlobalEventDetails eventId(@Nullable String eventId) {
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

  public GlobalEventDetails name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public GlobalEventDetails type(@Nullable String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable String getType() {
    return type;
  }

  public void setType(@Nullable String type) {
    this.type = type;
  }

  public GlobalEventDetails era(@Nullable String era) {
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

  public GlobalEventDetails description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Полное описание события
   * @return description
   */
  
  @Schema(name = "description", description = "Полное описание события", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public GlobalEventDetails lore(@Nullable String lore) {
    this.lore = lore;
    return this;
  }

  /**
   * Лор из Cyberpunk
   * @return lore
   */
  
  @Schema(name = "lore", description = "Лор из Cyberpunk", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lore")
  public @Nullable String getLore() {
    return lore;
  }

  public void setLore(@Nullable String lore) {
    this.lore = lore;
  }

  public GlobalEventDetails impact(@Nullable GlobalEventDetailsImpact impact) {
    this.impact = impact;
    return this;
  }

  /**
   * Get impact
   * @return impact
   */
  @Valid 
  @Schema(name = "impact", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("impact")
  public @Nullable GlobalEventDetailsImpact getImpact() {
    return impact;
  }

  public void setImpact(@Nullable GlobalEventDetailsImpact impact) {
    this.impact = impact;
  }

  public GlobalEventDetails relatedQuests(List<String> relatedQuests) {
    this.relatedQuests = relatedQuests;
    return this;
  }

  public GlobalEventDetails addRelatedQuestsItem(String relatedQuestsItem) {
    if (this.relatedQuests == null) {
      this.relatedQuests = new ArrayList<>();
    }
    this.relatedQuests.add(relatedQuestsItem);
    return this;
  }

  /**
   * Квесты, связанные с событием
   * @return relatedQuests
   */
  
  @Schema(name = "related_quests", description = "Квесты, связанные с событием", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("related_quests")
  public List<String> getRelatedQuests() {
    return relatedQuests;
  }

  public void setRelatedQuests(List<String> relatedQuests) {
    this.relatedQuests = relatedQuests;
  }

  public GlobalEventDetails triggers(@Nullable Object triggers) {
    this.triggers = triggers;
    return this;
  }

  /**
   * Триггеры запуска события
   * @return triggers
   */
  
  @Schema(name = "triggers", description = "Триггеры запуска события", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("triggers")
  public @Nullable Object getTriggers() {
    return triggers;
  }

  public void setTriggers(@Nullable Object triggers) {
    this.triggers = triggers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GlobalEventDetails globalEventDetails = (GlobalEventDetails) o;
    return Objects.equals(this.eventId, globalEventDetails.eventId) &&
        Objects.equals(this.name, globalEventDetails.name) &&
        Objects.equals(this.type, globalEventDetails.type) &&
        Objects.equals(this.era, globalEventDetails.era) &&
        Objects.equals(this.description, globalEventDetails.description) &&
        Objects.equals(this.lore, globalEventDetails.lore) &&
        Objects.equals(this.impact, globalEventDetails.impact) &&
        Objects.equals(this.relatedQuests, globalEventDetails.relatedQuests) &&
        Objects.equals(this.triggers, globalEventDetails.triggers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, name, type, era, description, lore, impact, relatedQuests, triggers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GlobalEventDetails {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    era: ").append(toIndentedString(era)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    lore: ").append(toIndentedString(lore)).append("\n");
    sb.append("    impact: ").append(toIndentedString(impact)).append("\n");
    sb.append("    relatedQuests: ").append(toIndentedString(relatedQuests)).append("\n");
    sb.append("    triggers: ").append(toIndentedString(triggers)).append("\n");
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

