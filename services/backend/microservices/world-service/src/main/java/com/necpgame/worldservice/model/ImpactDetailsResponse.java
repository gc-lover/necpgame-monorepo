package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.worldservice.model.ImpactHistoryEntry;
import com.necpgame.worldservice.model.PlayerOrderCrisis;
import com.necpgame.worldservice.model.PlayerOrderImpact;
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
 * ImpactDetailsResponse
 */


public class ImpactDetailsResponse {

  private PlayerOrderImpact data;

  private @Nullable PlayerOrderCrisis crisis;

  @Valid
  private List<@Valid ImpactHistoryEntry> history = new ArrayList<>();

  public ImpactDetailsResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ImpactDetailsResponse(PlayerOrderImpact data) {
    this.data = data;
  }

  public ImpactDetailsResponse data(PlayerOrderImpact data) {
    this.data = data;
    return this;
  }

  /**
   * Get data
   * @return data
   */
  @NotNull @Valid 
  @Schema(name = "data", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("data")
  public PlayerOrderImpact getData() {
    return data;
  }

  public void setData(PlayerOrderImpact data) {
    this.data = data;
  }

  public ImpactDetailsResponse crisis(@Nullable PlayerOrderCrisis crisis) {
    this.crisis = crisis;
    return this;
  }

  /**
   * Get crisis
   * @return crisis
   */
  @Valid 
  @Schema(name = "crisis", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("crisis")
  public @Nullable PlayerOrderCrisis getCrisis() {
    return crisis;
  }

  public void setCrisis(@Nullable PlayerOrderCrisis crisis) {
    this.crisis = crisis;
  }

  public ImpactDetailsResponse history(List<@Valid ImpactHistoryEntry> history) {
    this.history = history;
    return this;
  }

  public ImpactDetailsResponse addHistoryItem(ImpactHistoryEntry historyItem) {
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
  public List<@Valid ImpactHistoryEntry> getHistory() {
    return history;
  }

  public void setHistory(List<@Valid ImpactHistoryEntry> history) {
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
    ImpactDetailsResponse impactDetailsResponse = (ImpactDetailsResponse) o;
    return Objects.equals(this.data, impactDetailsResponse.data) &&
        Objects.equals(this.crisis, impactDetailsResponse.crisis) &&
        Objects.equals(this.history, impactDetailsResponse.history);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, crisis, history);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImpactDetailsResponse {\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    crisis: ").append(toIndentedString(crisis)).append("\n");
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

