package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.ResetHistoryEntry;
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
 * GetResetHistory200Response
 */

@JsonTypeName("getResetHistory_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetResetHistory200Response {

  @Valid
  private List<@Valid ResetHistoryEntry> history = new ArrayList<>();

  public GetResetHistory200Response history(List<@Valid ResetHistoryEntry> history) {
    this.history = history;
    return this;
  }

  public GetResetHistory200Response addHistoryItem(ResetHistoryEntry historyItem) {
    if (this.history == null) {
      this.history = new ArrayList<>();
    }
    this.history.add(historyItem);
    return this;
  }

  /**
   * Get history
   * @return history
   */
  @Valid 
  @Schema(name = "history", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("history")
  public List<@Valid ResetHistoryEntry> getHistory() {
    return history;
  }

  public void setHistory(List<@Valid ResetHistoryEntry> history) {
    this.history = history;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetResetHistory200Response getResetHistory200Response = (GetResetHistory200Response) o;
    return Objects.equals(this.history, getResetHistory200Response.history);
  }

  @Override
  public int hashCode() {
    return Objects.hash(history);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetResetHistory200Response {\n");
    sb.append("    history: ").append(toIndentedString(history)).append("\n");
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

