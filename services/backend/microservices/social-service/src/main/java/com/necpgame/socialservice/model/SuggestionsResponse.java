package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.Suggestion;
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
 * SuggestionsResponse
 */


public class SuggestionsResponse {

  @Valid
  private List<@Valid Suggestion> suggestions = new ArrayList<>();

  public SuggestionsResponse suggestions(List<@Valid Suggestion> suggestions) {
    this.suggestions = suggestions;
    return this;
  }

  public SuggestionsResponse addSuggestionsItem(Suggestion suggestionsItem) {
    if (this.suggestions == null) {
      this.suggestions = new ArrayList<>();
    }
    this.suggestions.add(suggestionsItem);
    return this;
  }

  /**
   * Get suggestions
   * @return suggestions
   */
  @Valid 
  @Schema(name = "suggestions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("suggestions")
  public List<@Valid Suggestion> getSuggestions() {
    return suggestions;
  }

  public void setSuggestions(List<@Valid Suggestion> suggestions) {
    this.suggestions = suggestions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SuggestionsResponse suggestionsResponse = (SuggestionsResponse) o;
    return Objects.equals(this.suggestions, suggestionsResponse.suggestions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(suggestions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SuggestionsResponse {\n");
    sb.append("    suggestions: ").append(toIndentedString(suggestions)).append("\n");
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

