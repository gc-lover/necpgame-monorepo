package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.worldservice.model.SkillFatigue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ActionXpBatchResponse
 */


public class ActionXpBatchResponse {

  private UUID characterId;

  private Integer entriesProcessed;

  @Valid
  private List<String> warnings = new ArrayList<>();

  @Valid
  private List<@Valid SkillFatigue> updatedSkills = new ArrayList<>();

  public ActionXpBatchResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ActionXpBatchResponse(UUID characterId, Integer entriesProcessed, List<@Valid SkillFatigue> updatedSkills) {
    this.characterId = characterId;
    this.entriesProcessed = entriesProcessed;
    this.updatedSkills = updatedSkills;
  }

  public ActionXpBatchResponse characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "characterId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("characterId")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
    this.characterId = characterId;
  }

  public ActionXpBatchResponse entriesProcessed(Integer entriesProcessed) {
    this.entriesProcessed = entriesProcessed;
    return this;
  }

  /**
   * Get entriesProcessed
   * @return entriesProcessed
   */
  @NotNull 
  @Schema(name = "entriesProcessed", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("entriesProcessed")
  public Integer getEntriesProcessed() {
    return entriesProcessed;
  }

  public void setEntriesProcessed(Integer entriesProcessed) {
    this.entriesProcessed = entriesProcessed;
  }

  public ActionXpBatchResponse warnings(List<String> warnings) {
    this.warnings = warnings;
    return this;
  }

  public ActionXpBatchResponse addWarningsItem(String warningsItem) {
    if (this.warnings == null) {
      this.warnings = new ArrayList<>();
    }
    this.warnings.add(warningsItem);
    return this;
  }

  /**
   * Get warnings
   * @return warnings
   */
  
  @Schema(name = "warnings", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("warnings")
  public List<String> getWarnings() {
    return warnings;
  }

  public void setWarnings(List<String> warnings) {
    this.warnings = warnings;
  }

  public ActionXpBatchResponse updatedSkills(List<@Valid SkillFatigue> updatedSkills) {
    this.updatedSkills = updatedSkills;
    return this;
  }

  public ActionXpBatchResponse addUpdatedSkillsItem(SkillFatigue updatedSkillsItem) {
    if (this.updatedSkills == null) {
      this.updatedSkills = new ArrayList<>();
    }
    this.updatedSkills.add(updatedSkillsItem);
    return this;
  }

  /**
   * Get updatedSkills
   * @return updatedSkills
   */
  @NotNull @Valid 
  @Schema(name = "updatedSkills", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("updatedSkills")
  public List<@Valid SkillFatigue> getUpdatedSkills() {
    return updatedSkills;
  }

  public void setUpdatedSkills(List<@Valid SkillFatigue> updatedSkills) {
    this.updatedSkills = updatedSkills;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ActionXpBatchResponse actionXpBatchResponse = (ActionXpBatchResponse) o;
    return Objects.equals(this.characterId, actionXpBatchResponse.characterId) &&
        Objects.equals(this.entriesProcessed, actionXpBatchResponse.entriesProcessed) &&
        Objects.equals(this.warnings, actionXpBatchResponse.warnings) &&
        Objects.equals(this.updatedSkills, actionXpBatchResponse.updatedSkills);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, entriesProcessed, warnings, updatedSkills);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActionXpBatchResponse {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    entriesProcessed: ").append(toIndentedString(entriesProcessed)).append("\n");
    sb.append("    warnings: ").append(toIndentedString(warnings)).append("\n");
    sb.append("    updatedSkills: ").append(toIndentedString(updatedSkills)).append("\n");
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

