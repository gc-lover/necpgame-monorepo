package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.QuestEnding;
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
 * GetQuestEndings200Response
 */

@JsonTypeName("getQuestEndings_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetQuestEndings200Response {

  @Valid
  private List<@Valid QuestEnding> endings = new ArrayList<>();

  @Valid
  private List<String> achievedEndings = new ArrayList<>();

  public GetQuestEndings200Response endings(List<@Valid QuestEnding> endings) {
    this.endings = endings;
    return this;
  }

  public GetQuestEndings200Response addEndingsItem(QuestEnding endingsItem) {
    if (this.endings == null) {
      this.endings = new ArrayList<>();
    }
    this.endings.add(endingsItem);
    return this;
  }

  /**
   * Get endings
   * @return endings
   */
  @Valid 
  @Schema(name = "endings", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("endings")
  public List<@Valid QuestEnding> getEndings() {
    return endings;
  }

  public void setEndings(List<@Valid QuestEnding> endings) {
    this.endings = endings;
  }

  public GetQuestEndings200Response achievedEndings(List<String> achievedEndings) {
    this.achievedEndings = achievedEndings;
    return this;
  }

  public GetQuestEndings200Response addAchievedEndingsItem(String achievedEndingsItem) {
    if (this.achievedEndings == null) {
      this.achievedEndings = new ArrayList<>();
    }
    this.achievedEndings.add(achievedEndingsItem);
    return this;
  }

  /**
   * Концовки, которые игрок уже видел
   * @return achievedEndings
   */
  
  @Schema(name = "achieved_endings", description = "Концовки, которые игрок уже видел", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("achieved_endings")
  public List<String> getAchievedEndings() {
    return achievedEndings;
  }

  public void setAchievedEndings(List<String> achievedEndings) {
    this.achievedEndings = achievedEndings;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetQuestEndings200Response getQuestEndings200Response = (GetQuestEndings200Response) o;
    return Objects.equals(this.endings, getQuestEndings200Response.endings) &&
        Objects.equals(this.achievedEndings, getQuestEndings200Response.achievedEndings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(endings, achievedEndings);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetQuestEndings200Response {\n");
    sb.append("    endings: ").append(toIndentedString(endings)).append("\n");
    sb.append("    achievedEndings: ").append(toIndentedString(achievedEndings)).append("\n");
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

