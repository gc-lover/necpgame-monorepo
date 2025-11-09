package com.necpgame.backjava.model;

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
 * MainStoryQuestMajorChoicesInner
 */

@JsonTypeName("MainStoryQuest_major_choices_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class MainStoryQuestMajorChoicesInner {

  private @Nullable String choiceId;

  private @Nullable String description;

  private @Nullable String consequences;

  public MainStoryQuestMajorChoicesInner choiceId(@Nullable String choiceId) {
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

  public MainStoryQuestMajorChoicesInner description(@Nullable String description) {
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

  public MainStoryQuestMajorChoicesInner consequences(@Nullable String consequences) {
    this.consequences = consequences;
    return this;
  }

  /**
   * Get consequences
   * @return consequences
   */
  
  @Schema(name = "consequences", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("consequences")
  public @Nullable String getConsequences() {
    return consequences;
  }

  public void setConsequences(@Nullable String consequences) {
    this.consequences = consequences;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MainStoryQuestMajorChoicesInner mainStoryQuestMajorChoicesInner = (MainStoryQuestMajorChoicesInner) o;
    return Objects.equals(this.choiceId, mainStoryQuestMajorChoicesInner.choiceId) &&
        Objects.equals(this.description, mainStoryQuestMajorChoicesInner.description) &&
        Objects.equals(this.consequences, mainStoryQuestMajorChoicesInner.consequences);
  }

  @Override
  public int hashCode() {
    return Objects.hash(choiceId, description, consequences);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MainStoryQuestMajorChoicesInner {\n");
    sb.append("    choiceId: ").append(toIndentedString(choiceId)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    consequences: ").append(toIndentedString(consequences)).append("\n");
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

