package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.necpgame.backjava.model.QuestProgress;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetActiveQuests200Response
 */

@JsonTypeName("getActiveQuests_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T20:50:05.709666800+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class GetActiveQuests200Response {

  @Valid
  private List<QuestProgress> quests = new ArrayList<>();

  public GetActiveQuests200Response quests(List<QuestProgress> quests) {
    this.quests = quests;
    return this;
  }

  public GetActiveQuests200Response addQuestsItem(QuestProgress questsItem) {
    if (this.quests == null) {
      this.quests = new ArrayList<>();
    }
    this.quests.add(questsItem);
    return this;
  }

  /**
   * Get quests
   * @return quests
   */
  @Valid 
  @Schema(name = "quests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quests")
  public List<QuestProgress> getQuests() {
    return quests;
  }

  public void setQuests(List<QuestProgress> quests) {
    this.quests = quests;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetActiveQuests200Response getActiveQuests200Response = (GetActiveQuests200Response) o;
    return Objects.equals(this.quests, getActiveQuests200Response.quests);
  }

  @Override
  public int hashCode() {
    return Objects.hash(quests);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetActiveQuests200Response {\n");
    sb.append("    quests: ").append(toIndentedString(quests)).append("\n");
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



