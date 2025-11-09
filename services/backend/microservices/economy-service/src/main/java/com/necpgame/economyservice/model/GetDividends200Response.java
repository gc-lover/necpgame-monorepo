package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.GetDividends200ResponseUpcomingDividendsInner;
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
 * GetDividends200Response
 */

@JsonTypeName("getDividends_200_response")

public class GetDividends200Response {

  private @Nullable String characterId;

  private @Nullable BigDecimal totalDividendsReceived;

  @Valid
  private List<@Valid GetDividends200ResponseUpcomingDividendsInner> upcomingDividends = new ArrayList<>();

  @Valid
  private List<Object> history = new ArrayList<>();

  public GetDividends200Response characterId(@Nullable String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable String characterId) {
    this.characterId = characterId;
  }

  public GetDividends200Response totalDividendsReceived(@Nullable BigDecimal totalDividendsReceived) {
    this.totalDividendsReceived = totalDividendsReceived;
    return this;
  }

  /**
   * Get totalDividendsReceived
   * @return totalDividendsReceived
   */
  @Valid 
  @Schema(name = "total_dividends_received", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_dividends_received")
  public @Nullable BigDecimal getTotalDividendsReceived() {
    return totalDividendsReceived;
  }

  public void setTotalDividendsReceived(@Nullable BigDecimal totalDividendsReceived) {
    this.totalDividendsReceived = totalDividendsReceived;
  }

  public GetDividends200Response upcomingDividends(List<@Valid GetDividends200ResponseUpcomingDividendsInner> upcomingDividends) {
    this.upcomingDividends = upcomingDividends;
    return this;
  }

  public GetDividends200Response addUpcomingDividendsItem(GetDividends200ResponseUpcomingDividendsInner upcomingDividendsItem) {
    if (this.upcomingDividends == null) {
      this.upcomingDividends = new ArrayList<>();
    }
    this.upcomingDividends.add(upcomingDividendsItem);
    return this;
  }

  /**
   * Get upcomingDividends
   * @return upcomingDividends
   */
  @Valid 
  @Schema(name = "upcoming_dividends", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("upcoming_dividends")
  public List<@Valid GetDividends200ResponseUpcomingDividendsInner> getUpcomingDividends() {
    return upcomingDividends;
  }

  public void setUpcomingDividends(List<@Valid GetDividends200ResponseUpcomingDividendsInner> upcomingDividends) {
    this.upcomingDividends = upcomingDividends;
  }

  public GetDividends200Response history(List<Object> history) {
    this.history = history;
    return this;
  }

  public GetDividends200Response addHistoryItem(Object historyItem) {
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
  
  @Schema(name = "history", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("history")
  public List<Object> getHistory() {
    return history;
  }

  public void setHistory(List<Object> history) {
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
    GetDividends200Response getDividends200Response = (GetDividends200Response) o;
    return Objects.equals(this.characterId, getDividends200Response.characterId) &&
        Objects.equals(this.totalDividendsReceived, getDividends200Response.totalDividendsReceived) &&
        Objects.equals(this.upcomingDividends, getDividends200Response.upcomingDividends) &&
        Objects.equals(this.history, getDividends200Response.history);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, totalDividendsReceived, upcomingDividends, history);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetDividends200Response {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    totalDividendsReceived: ").append(toIndentedString(totalDividendsReceived)).append("\n");
    sb.append("    upcomingDividends: ").append(toIndentedString(upcomingDividends)).append("\n");
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

