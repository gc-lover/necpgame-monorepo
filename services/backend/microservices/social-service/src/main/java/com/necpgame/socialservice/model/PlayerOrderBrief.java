package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.PlayerOrderAttachment;
import com.necpgame.socialservice.model.PlayerOrderCheckpoint;
import com.necpgame.socialservice.model.PlayerOrderObjective;
import com.necpgame.socialservice.model.PlayerOrderPrivacy;
import com.necpgame.socialservice.model.PlayerOrderRiskProfile;
import com.necpgame.socialservice.model.PlayerOrderTeamRequirement;
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
 * PlayerOrderBrief
 */


public class PlayerOrderBrief {

  /**
   * Gets or Sets template
   */
  public enum TemplateEnum {
    COMBAT("combat"),
    
    HACKER("hacker"),
    
    ECONOMY("economy"),
    
    SOCIAL("social"),
    
    EXPLORATION("exploration");

    private final String value;

    TemplateEnum(String value) {
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
    public static TemplateEnum fromValue(String value) {
      for (TemplateEnum b : TemplateEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TemplateEnum template;

  private String goal;

  @Valid
  private List<@Valid PlayerOrderObjective> objectives = new ArrayList<>();

  @Valid
  private List<@Valid PlayerOrderCheckpoint> checkpoints = new ArrayList<>();

  @Valid
  private List<@Valid PlayerOrderAttachment> attachments = new ArrayList<>();

  private PlayerOrderRiskProfile riskProfile;

  private PlayerOrderTeamRequirement team;

  private PlayerOrderPrivacy privacy;

  private @Nullable String notes;

  public PlayerOrderBrief() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderBrief(TemplateEnum template, String goal, List<@Valid PlayerOrderObjective> objectives, PlayerOrderRiskProfile riskProfile, PlayerOrderTeamRequirement team, PlayerOrderPrivacy privacy) {
    this.template = template;
    this.goal = goal;
    this.objectives = objectives;
    this.riskProfile = riskProfile;
    this.team = team;
    this.privacy = privacy;
  }

  public PlayerOrderBrief template(TemplateEnum template) {
    this.template = template;
    return this;
  }

  /**
   * Get template
   * @return template
   */
  @NotNull 
  @Schema(name = "template", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("template")
  public TemplateEnum getTemplate() {
    return template;
  }

  public void setTemplate(TemplateEnum template) {
    this.template = template;
  }

  public PlayerOrderBrief goal(String goal) {
    this.goal = goal;
    return this;
  }

  /**
   * Главная цель заказа.
   * @return goal
   */
  @NotNull 
  @Schema(name = "goal", description = "Главная цель заказа.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("goal")
  public String getGoal() {
    return goal;
  }

  public void setGoal(String goal) {
    this.goal = goal;
  }

  public PlayerOrderBrief objectives(List<@Valid PlayerOrderObjective> objectives) {
    this.objectives = objectives;
    return this;
  }

  public PlayerOrderBrief addObjectivesItem(PlayerOrderObjective objectivesItem) {
    if (this.objectives == null) {
      this.objectives = new ArrayList<>();
    }
    this.objectives.add(objectivesItem);
    return this;
  }

  /**
   * Get objectives
   * @return objectives
   */
  @NotNull @Valid 
  @Schema(name = "objectives", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("objectives")
  public List<@Valid PlayerOrderObjective> getObjectives() {
    return objectives;
  }

  public void setObjectives(List<@Valid PlayerOrderObjective> objectives) {
    this.objectives = objectives;
  }

  public PlayerOrderBrief checkpoints(List<@Valid PlayerOrderCheckpoint> checkpoints) {
    this.checkpoints = checkpoints;
    return this;
  }

  public PlayerOrderBrief addCheckpointsItem(PlayerOrderCheckpoint checkpointsItem) {
    if (this.checkpoints == null) {
      this.checkpoints = new ArrayList<>();
    }
    this.checkpoints.add(checkpointsItem);
    return this;
  }

  /**
   * Get checkpoints
   * @return checkpoints
   */
  @Valid 
  @Schema(name = "checkpoints", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("checkpoints")
  public List<@Valid PlayerOrderCheckpoint> getCheckpoints() {
    return checkpoints;
  }

  public void setCheckpoints(List<@Valid PlayerOrderCheckpoint> checkpoints) {
    this.checkpoints = checkpoints;
  }

  public PlayerOrderBrief attachments(List<@Valid PlayerOrderAttachment> attachments) {
    this.attachments = attachments;
    return this;
  }

  public PlayerOrderBrief addAttachmentsItem(PlayerOrderAttachment attachmentsItem) {
    if (this.attachments == null) {
      this.attachments = new ArrayList<>();
    }
    this.attachments.add(attachmentsItem);
    return this;
  }

  /**
   * Вложения, сохранённые в content-service.
   * @return attachments
   */
  @Valid 
  @Schema(name = "attachments", description = "Вложения, сохранённые в content-service.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attachments")
  public List<@Valid PlayerOrderAttachment> getAttachments() {
    return attachments;
  }

  public void setAttachments(List<@Valid PlayerOrderAttachment> attachments) {
    this.attachments = attachments;
  }

  public PlayerOrderBrief riskProfile(PlayerOrderRiskProfile riskProfile) {
    this.riskProfile = riskProfile;
    return this;
  }

  /**
   * Get riskProfile
   * @return riskProfile
   */
  @NotNull @Valid 
  @Schema(name = "riskProfile", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("riskProfile")
  public PlayerOrderRiskProfile getRiskProfile() {
    return riskProfile;
  }

  public void setRiskProfile(PlayerOrderRiskProfile riskProfile) {
    this.riskProfile = riskProfile;
  }

  public PlayerOrderBrief team(PlayerOrderTeamRequirement team) {
    this.team = team;
    return this;
  }

  /**
   * Get team
   * @return team
   */
  @NotNull @Valid 
  @Schema(name = "team", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("team")
  public PlayerOrderTeamRequirement getTeam() {
    return team;
  }

  public void setTeam(PlayerOrderTeamRequirement team) {
    this.team = team;
  }

  public PlayerOrderBrief privacy(PlayerOrderPrivacy privacy) {
    this.privacy = privacy;
    return this;
  }

  /**
   * Get privacy
   * @return privacy
   */
  @NotNull @Valid 
  @Schema(name = "privacy", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("privacy")
  public PlayerOrderPrivacy getPrivacy() {
    return privacy;
  }

  public void setPrivacy(PlayerOrderPrivacy privacy) {
    this.privacy = privacy;
  }

  public PlayerOrderBrief notes(@Nullable String notes) {
    this.notes = notes;
    return this;
  }

  /**
   * Get notes
   * @return notes
   */
  
  @Schema(name = "notes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notes")
  public @Nullable String getNotes() {
    return notes;
  }

  public void setNotes(@Nullable String notes) {
    this.notes = notes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderBrief playerOrderBrief = (PlayerOrderBrief) o;
    return Objects.equals(this.template, playerOrderBrief.template) &&
        Objects.equals(this.goal, playerOrderBrief.goal) &&
        Objects.equals(this.objectives, playerOrderBrief.objectives) &&
        Objects.equals(this.checkpoints, playerOrderBrief.checkpoints) &&
        Objects.equals(this.attachments, playerOrderBrief.attachments) &&
        Objects.equals(this.riskProfile, playerOrderBrief.riskProfile) &&
        Objects.equals(this.team, playerOrderBrief.team) &&
        Objects.equals(this.privacy, playerOrderBrief.privacy) &&
        Objects.equals(this.notes, playerOrderBrief.notes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(template, goal, objectives, checkpoints, attachments, riskProfile, team, privacy, notes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderBrief {\n");
    sb.append("    template: ").append(toIndentedString(template)).append("\n");
    sb.append("    goal: ").append(toIndentedString(goal)).append("\n");
    sb.append("    objectives: ").append(toIndentedString(objectives)).append("\n");
    sb.append("    checkpoints: ").append(toIndentedString(checkpoints)).append("\n");
    sb.append("    attachments: ").append(toIndentedString(attachments)).append("\n");
    sb.append("    riskProfile: ").append(toIndentedString(riskProfile)).append("\n");
    sb.append("    team: ").append(toIndentedString(team)).append("\n");
    sb.append("    privacy: ").append(toIndentedString(privacy)).append("\n");
    sb.append("    notes: ").append(toIndentedString(notes)).append("\n");
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

