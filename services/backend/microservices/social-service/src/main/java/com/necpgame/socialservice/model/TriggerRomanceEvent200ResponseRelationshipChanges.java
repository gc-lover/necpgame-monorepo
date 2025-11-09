package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TriggerRomanceEvent200ResponseRelationshipChanges
 */

@JsonTypeName("triggerRomanceEvent_200_response_relationship_changes")

public class TriggerRomanceEvent200ResponseRelationshipChanges {

  private @Nullable Integer affectionChange;

  private @Nullable Integer trustChange;

  private @Nullable Integer romanceStageChange;

  public TriggerRomanceEvent200ResponseRelationshipChanges affectionChange(@Nullable Integer affectionChange) {
    this.affectionChange = affectionChange;
    return this;
  }

  /**
   * Get affectionChange
   * @return affectionChange
   */
  
  @Schema(name = "affection_change", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affection_change")
  public @Nullable Integer getAffectionChange() {
    return affectionChange;
  }

  public void setAffectionChange(@Nullable Integer affectionChange) {
    this.affectionChange = affectionChange;
  }

  public TriggerRomanceEvent200ResponseRelationshipChanges trustChange(@Nullable Integer trustChange) {
    this.trustChange = trustChange;
    return this;
  }

  /**
   * Get trustChange
   * @return trustChange
   */
  
  @Schema(name = "trust_change", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trust_change")
  public @Nullable Integer getTrustChange() {
    return trustChange;
  }

  public void setTrustChange(@Nullable Integer trustChange) {
    this.trustChange = trustChange;
  }

  public TriggerRomanceEvent200ResponseRelationshipChanges romanceStageChange(@Nullable Integer romanceStageChange) {
    this.romanceStageChange = romanceStageChange;
    return this;
  }

  /**
   * Get romanceStageChange
   * @return romanceStageChange
   */
  
  @Schema(name = "romance_stage_change", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("romance_stage_change")
  public @Nullable Integer getRomanceStageChange() {
    return romanceStageChange;
  }

  public void setRomanceStageChange(@Nullable Integer romanceStageChange) {
    this.romanceStageChange = romanceStageChange;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TriggerRomanceEvent200ResponseRelationshipChanges triggerRomanceEvent200ResponseRelationshipChanges = (TriggerRomanceEvent200ResponseRelationshipChanges) o;
    return Objects.equals(this.affectionChange, triggerRomanceEvent200ResponseRelationshipChanges.affectionChange) &&
        Objects.equals(this.trustChange, triggerRomanceEvent200ResponseRelationshipChanges.trustChange) &&
        Objects.equals(this.romanceStageChange, triggerRomanceEvent200ResponseRelationshipChanges.romanceStageChange);
  }

  @Override
  public int hashCode() {
    return Objects.hash(affectionChange, trustChange, romanceStageChange);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TriggerRomanceEvent200ResponseRelationshipChanges {\n");
    sb.append("    affectionChange: ").append(toIndentedString(affectionChange)).append("\n");
    sb.append("    trustChange: ").append(toIndentedString(trustChange)).append("\n");
    sb.append("    romanceStageChange: ").append(toIndentedString(romanceStageChange)).append("\n");
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

