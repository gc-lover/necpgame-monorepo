package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.worldservice.model.EventChoiceSkillCheck;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
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
 * EventChoice
 */


public class EventChoice {

  private @Nullable String choiceId;

  private @Nullable String text;

  private JsonNullable<EventChoiceSkillCheck> skillCheck = JsonNullable.<EventChoiceSkillCheck>undefined();

  private JsonNullable<UUID> itemRequirement = JsonNullable.<UUID>undefined();

  @Valid
  private JsonNullable<Map<String, Integer>> reputationRequirement = JsonNullable.<Map<String, Integer>>undefined();

  private @Nullable String leadsToOutcome;

  public EventChoice choiceId(@Nullable String choiceId) {
    this.choiceId = choiceId;
    return this;
  }

  /**
   * Get choiceId
   * @return choiceId
   */
  
  @Schema(name = "choice_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("choice_id")
  public @Nullable String getChoiceId() {
    return choiceId;
  }

  public void setChoiceId(@Nullable String choiceId) {
    this.choiceId = choiceId;
  }

  public EventChoice text(@Nullable String text) {
    this.text = text;
    return this;
  }

  /**
   * Get text
   * @return text
   */
  
  @Schema(name = "text", example = "Help the stranded nomad", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("text")
  public @Nullable String getText() {
    return text;
  }

  public void setText(@Nullable String text) {
    this.text = text;
  }

  public EventChoice skillCheck(EventChoiceSkillCheck skillCheck) {
    this.skillCheck = JsonNullable.of(skillCheck);
    return this;
  }

  /**
   * Get skillCheck
   * @return skillCheck
   */
  @Valid 
  @Schema(name = "skill_check", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill_check")
  public JsonNullable<EventChoiceSkillCheck> getSkillCheck() {
    return skillCheck;
  }

  public void setSkillCheck(JsonNullable<EventChoiceSkillCheck> skillCheck) {
    this.skillCheck = skillCheck;
  }

  public EventChoice itemRequirement(UUID itemRequirement) {
    this.itemRequirement = JsonNullable.of(itemRequirement);
    return this;
  }

  /**
   * Get itemRequirement
   * @return itemRequirement
   */
  @Valid 
  @Schema(name = "item_requirement", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("item_requirement")
  public JsonNullable<UUID> getItemRequirement() {
    return itemRequirement;
  }

  public void setItemRequirement(JsonNullable<UUID> itemRequirement) {
    this.itemRequirement = itemRequirement;
  }

  public EventChoice reputationRequirement(Map<String, Integer> reputationRequirement) {
    this.reputationRequirement = JsonNullable.of(reputationRequirement);
    return this;
  }

  public EventChoice putReputationRequirementItem(String key, Integer reputationRequirementItem) {
    if (this.reputationRequirement == null || !this.reputationRequirement.isPresent()) {
      this.reputationRequirement = JsonNullable.of(new HashMap<>());
    }
    this.reputationRequirement.get().put(key, reputationRequirementItem);
    return this;
  }

  /**
   * Get reputationRequirement
   * @return reputationRequirement
   */
  
  @Schema(name = "reputation_requirement", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputation_requirement")
  public JsonNullable<Map<String, Integer>> getReputationRequirement() {
    return reputationRequirement;
  }

  public void setReputationRequirement(JsonNullable<Map<String, Integer>> reputationRequirement) {
    this.reputationRequirement = reputationRequirement;
  }

  public EventChoice leadsToOutcome(@Nullable String leadsToOutcome) {
    this.leadsToOutcome = leadsToOutcome;
    return this;
  }

  /**
   * Get leadsToOutcome
   * @return leadsToOutcome
   */
  
  @Schema(name = "leads_to_outcome", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("leads_to_outcome")
  public @Nullable String getLeadsToOutcome() {
    return leadsToOutcome;
  }

  public void setLeadsToOutcome(@Nullable String leadsToOutcome) {
    this.leadsToOutcome = leadsToOutcome;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventChoice eventChoice = (EventChoice) o;
    return Objects.equals(this.choiceId, eventChoice.choiceId) &&
        Objects.equals(this.text, eventChoice.text) &&
        equalsNullable(this.skillCheck, eventChoice.skillCheck) &&
        equalsNullable(this.itemRequirement, eventChoice.itemRequirement) &&
        equalsNullable(this.reputationRequirement, eventChoice.reputationRequirement) &&
        Objects.equals(this.leadsToOutcome, eventChoice.leadsToOutcome);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(choiceId, text, hashCodeNullable(skillCheck), hashCodeNullable(itemRequirement), hashCodeNullable(reputationRequirement), leadsToOutcome);
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
    sb.append("class EventChoice {\n");
    sb.append("    choiceId: ").append(toIndentedString(choiceId)).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    skillCheck: ").append(toIndentedString(skillCheck)).append("\n");
    sb.append("    itemRequirement: ").append(toIndentedString(itemRequirement)).append("\n");
    sb.append("    reputationRequirement: ").append(toIndentedString(reputationRequirement)).append("\n");
    sb.append("    leadsToOutcome: ").append(toIndentedString(leadsToOutcome)).append("\n");
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

