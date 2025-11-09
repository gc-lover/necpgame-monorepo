package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.PlayerOrderBudgetEstimate;
import com.necpgame.socialservice.model.PlayerOrderBudgetHistoryItem;
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
 * PlayerOrderBudgetSummary
 */


public class PlayerOrderBudgetSummary {

  private PlayerOrderBudgetEstimate current;

  @Valid
  private List<@Valid PlayerOrderBudgetHistoryItem> history = new ArrayList<>();

  public PlayerOrderBudgetSummary() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderBudgetSummary(PlayerOrderBudgetEstimate current, List<@Valid PlayerOrderBudgetHistoryItem> history) {
    this.current = current;
    this.history = history;
  }

  public PlayerOrderBudgetSummary current(PlayerOrderBudgetEstimate current) {
    this.current = current;
    return this;
  }

  /**
   * Get current
   * @return current
   */
  @NotNull @Valid 
  @Schema(name = "current", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("current")
  public PlayerOrderBudgetEstimate getCurrent() {
    return current;
  }

  public void setCurrent(PlayerOrderBudgetEstimate current) {
    this.current = current;
  }

  public PlayerOrderBudgetSummary history(List<@Valid PlayerOrderBudgetHistoryItem> history) {
    this.history = history;
    return this;
  }

  public PlayerOrderBudgetSummary addHistoryItem(PlayerOrderBudgetHistoryItem historyItem) {
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
  @NotNull @Valid 
  @Schema(name = "history", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("history")
  public List<@Valid PlayerOrderBudgetHistoryItem> getHistory() {
    return history;
  }

  public void setHistory(List<@Valid PlayerOrderBudgetHistoryItem> history) {
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
    PlayerOrderBudgetSummary playerOrderBudgetSummary = (PlayerOrderBudgetSummary) o;
    return Objects.equals(this.current, playerOrderBudgetSummary.current) &&
        Objects.equals(this.history, playerOrderBudgetSummary.history);
  }

  @Override
  public int hashCode() {
    return Objects.hash(current, history);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderBudgetSummary {\n");
    sb.append("    current: ").append(toIndentedString(current)).append("\n");
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

