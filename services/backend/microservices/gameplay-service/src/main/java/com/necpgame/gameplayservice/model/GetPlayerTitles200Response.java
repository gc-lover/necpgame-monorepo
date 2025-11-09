package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.Title;
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
 * GetPlayerTitles200Response
 */

@JsonTypeName("getPlayerTitles_200_response")

public class GetPlayerTitles200Response {

  @Valid
  private List<@Valid Title> titles = new ArrayList<>();

  private @Nullable String activeTitle;

  public GetPlayerTitles200Response titles(List<@Valid Title> titles) {
    this.titles = titles;
    return this;
  }

  public GetPlayerTitles200Response addTitlesItem(Title titlesItem) {
    if (this.titles == null) {
      this.titles = new ArrayList<>();
    }
    this.titles.add(titlesItem);
    return this;
  }

  /**
   * Get titles
   * @return titles
   */
  @Valid 
  @Schema(name = "titles", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("titles")
  public List<@Valid Title> getTitles() {
    return titles;
  }

  public void setTitles(List<@Valid Title> titles) {
    this.titles = titles;
  }

  public GetPlayerTitles200Response activeTitle(@Nullable String activeTitle) {
    this.activeTitle = activeTitle;
    return this;
  }

  /**
   * ID активного титула
   * @return activeTitle
   */
  
  @Schema(name = "active_title", description = "ID активного титула", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active_title")
  public @Nullable String getActiveTitle() {
    return activeTitle;
  }

  public void setActiveTitle(@Nullable String activeTitle) {
    this.activeTitle = activeTitle;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetPlayerTitles200Response getPlayerTitles200Response = (GetPlayerTitles200Response) o;
    return Objects.equals(this.titles, getPlayerTitles200Response.titles) &&
        Objects.equals(this.activeTitle, getPlayerTitles200Response.activeTitle);
  }

  @Override
  public int hashCode() {
    return Objects.hash(titles, activeTitle);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetPlayerTitles200Response {\n");
    sb.append("    titles: ").append(toIndentedString(titles)).append("\n");
    sb.append("    activeTitle: ").append(toIndentedString(activeTitle)).append("\n");
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

