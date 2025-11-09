package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.narrativeservice.model.Quest;
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
 * GetQuests200Response
 */

@JsonTypeName("getQuests_200_response")

public class GetQuests200Response {

  @Valid
  private List<@Valid Quest> quests = new ArrayList<>();

  private @Nullable Integer total;

  public GetQuests200Response quests(List<@Valid Quest> quests) {
    this.quests = quests;
    return this;
  }

  public GetQuests200Response addQuestsItem(Quest questsItem) {
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
  public List<@Valid Quest> getQuests() {
    return quests;
  }

  public void setQuests(List<@Valid Quest> quests) {
    this.quests = quests;
  }

  public GetQuests200Response total(@Nullable Integer total) {
    this.total = total;
    return this;
  }

  /**
   * Get total
   * @return total
   */
  
  @Schema(name = "total", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total")
  public @Nullable Integer getTotal() {
    return total;
  }

  public void setTotal(@Nullable Integer total) {
    this.total = total;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetQuests200Response getQuests200Response = (GetQuests200Response) o;
    return Objects.equals(this.quests, getQuests200Response.quests) &&
        Objects.equals(this.total, getQuests200Response.total);
  }

  @Override
  public int hashCode() {
    return Objects.hash(quests, total);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetQuests200Response {\n");
    sb.append("    quests: ").append(toIndentedString(quests)).append("\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
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

