package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.socialservice.model.TrustHistoryEntry;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ListResonanceHistory200Response
 */

@JsonTypeName("listResonanceHistory_200_response")

public class ListResonanceHistory200Response {

  @Valid
  private List<@Valid TrustHistoryEntry> data = new ArrayList<>();

  private @Nullable Boolean crisisAlertsTriggered;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<OffsetDateTime> lastCrisisAt = JsonNullable.<OffsetDateTime>undefined();

  public ListResonanceHistory200Response() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ListResonanceHistory200Response(List<@Valid TrustHistoryEntry> data) {
    this.data = data;
  }

  public ListResonanceHistory200Response data(List<@Valid TrustHistoryEntry> data) {
    this.data = data;
    return this;
  }

  public ListResonanceHistory200Response addDataItem(TrustHistoryEntry dataItem) {
    if (this.data == null) {
      this.data = new ArrayList<>();
    }
    this.data.add(dataItem);
    return this;
  }

  /**
   * Get data
   * @return data
   */
  @NotNull @Valid 
  @Schema(name = "data", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("data")
  public List<@Valid TrustHistoryEntry> getData() {
    return data;
  }

  public void setData(List<@Valid TrustHistoryEntry> data) {
    this.data = data;
  }

  public ListResonanceHistory200Response crisisAlertsTriggered(@Nullable Boolean crisisAlertsTriggered) {
    this.crisisAlertsTriggered = crisisAlertsTriggered;
    return this;
  }

  /**
   * Флаг, указывающий, был ли активирован Crisis Alert в заданном диапазоне
   * @return crisisAlertsTriggered
   */
  
  @Schema(name = "crisisAlertsTriggered", example = "true", description = "Флаг, указывающий, был ли активирован Crisis Alert в заданном диапазоне", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("crisisAlertsTriggered")
  public @Nullable Boolean getCrisisAlertsTriggered() {
    return crisisAlertsTriggered;
  }

  public void setCrisisAlertsTriggered(@Nullable Boolean crisisAlertsTriggered) {
    this.crisisAlertsTriggered = crisisAlertsTriggered;
  }

  public ListResonanceHistory200Response lastCrisisAt(OffsetDateTime lastCrisisAt) {
    this.lastCrisisAt = JsonNullable.of(lastCrisisAt);
    return this;
  }

  /**
   * Время последнего уведомления Crisis системы
   * @return lastCrisisAt
   */
  @Valid 
  @Schema(name = "lastCrisisAt", description = "Время последнего уведомления Crisis системы", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastCrisisAt")
  public JsonNullable<OffsetDateTime> getLastCrisisAt() {
    return lastCrisisAt;
  }

  public void setLastCrisisAt(JsonNullable<OffsetDateTime> lastCrisisAt) {
    this.lastCrisisAt = lastCrisisAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ListResonanceHistory200Response listResonanceHistory200Response = (ListResonanceHistory200Response) o;
    return Objects.equals(this.data, listResonanceHistory200Response.data) &&
        Objects.equals(this.crisisAlertsTriggered, listResonanceHistory200Response.crisisAlertsTriggered) &&
        equalsNullable(this.lastCrisisAt, listResonanceHistory200Response.lastCrisisAt);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, crisisAlertsTriggered, hashCodeNullable(lastCrisisAt));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ListResonanceHistory200Response {\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    crisisAlertsTriggered: ").append(toIndentedString(crisisAlertsTriggered)).append("\n");
    sb.append("    lastCrisisAt: ").append(toIndentedString(lastCrisisAt)).append("\n");
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

