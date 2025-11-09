package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * MakeStoryChoice200Response
 */

@JsonTypeName("makeStoryChoice_200_response")

public class MakeStoryChoice200Response {

  private @Nullable Boolean success;

  private @Nullable String choiceId;

  private @Nullable String decision;

  @Valid
  private List<String> consequences = new ArrayList<>();

  private @Nullable BigDecimal humanityChange;

  @Valid
  private List<Object> relationshipsAffected = new ArrayList<>();

  public MakeStoryChoice200Response success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public MakeStoryChoice200Response choiceId(@Nullable String choiceId) {
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

  public MakeStoryChoice200Response decision(@Nullable String decision) {
    this.decision = decision;
    return this;
  }

  /**
   * Get decision
   * @return decision
   */
  
  @Schema(name = "decision", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("decision")
  public @Nullable String getDecision() {
    return decision;
  }

  public void setDecision(@Nullable String decision) {
    this.decision = decision;
  }

  public MakeStoryChoice200Response consequences(List<String> consequences) {
    this.consequences = consequences;
    return this;
  }

  public MakeStoryChoice200Response addConsequencesItem(String consequencesItem) {
    if (this.consequences == null) {
      this.consequences = new ArrayList<>();
    }
    this.consequences.add(consequencesItem);
    return this;
  }

  /**
   * Get consequences
   * @return consequences
   */
  
  @Schema(name = "consequences", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("consequences")
  public List<String> getConsequences() {
    return consequences;
  }

  public void setConsequences(List<String> consequences) {
    this.consequences = consequences;
  }

  public MakeStoryChoice200Response humanityChange(@Nullable BigDecimal humanityChange) {
    this.humanityChange = humanityChange;
    return this;
  }

  /**
   * Get humanityChange
   * @return humanityChange
   */
  @Valid 
  @Schema(name = "humanity_change", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("humanity_change")
  public @Nullable BigDecimal getHumanityChange() {
    return humanityChange;
  }

  public void setHumanityChange(@Nullable BigDecimal humanityChange) {
    this.humanityChange = humanityChange;
  }

  public MakeStoryChoice200Response relationshipsAffected(List<Object> relationshipsAffected) {
    this.relationshipsAffected = relationshipsAffected;
    return this;
  }

  public MakeStoryChoice200Response addRelationshipsAffectedItem(Object relationshipsAffectedItem) {
    if (this.relationshipsAffected == null) {
      this.relationshipsAffected = new ArrayList<>();
    }
    this.relationshipsAffected.add(relationshipsAffectedItem);
    return this;
  }

  /**
   * Get relationshipsAffected
   * @return relationshipsAffected
   */
  
  @Schema(name = "relationships_affected", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relationships_affected")
  public List<Object> getRelationshipsAffected() {
    return relationshipsAffected;
  }

  public void setRelationshipsAffected(List<Object> relationshipsAffected) {
    this.relationshipsAffected = relationshipsAffected;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MakeStoryChoice200Response makeStoryChoice200Response = (MakeStoryChoice200Response) o;
    return Objects.equals(this.success, makeStoryChoice200Response.success) &&
        Objects.equals(this.choiceId, makeStoryChoice200Response.choiceId) &&
        Objects.equals(this.decision, makeStoryChoice200Response.decision) &&
        Objects.equals(this.consequences, makeStoryChoice200Response.consequences) &&
        Objects.equals(this.humanityChange, makeStoryChoice200Response.humanityChange) &&
        Objects.equals(this.relationshipsAffected, makeStoryChoice200Response.relationshipsAffected);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, choiceId, decision, consequences, humanityChange, relationshipsAffected);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MakeStoryChoice200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    choiceId: ").append(toIndentedString(choiceId)).append("\n");
    sb.append("    decision: ").append(toIndentedString(decision)).append("\n");
    sb.append("    consequences: ").append(toIndentedString(consequences)).append("\n");
    sb.append("    humanityChange: ").append(toIndentedString(humanityChange)).append("\n");
    sb.append("    relationshipsAffected: ").append(toIndentedString(relationshipsAffected)).append("\n");
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

