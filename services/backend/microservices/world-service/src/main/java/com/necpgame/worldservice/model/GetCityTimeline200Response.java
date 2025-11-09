package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.worldservice.model.CityTimelineEntry;
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
 * GetCityTimeline200Response
 */

@JsonTypeName("getCityTimeline_200_response")

public class GetCityTimeline200Response {

  private @Nullable String cityId;

  @Valid
  private List<@Valid CityTimelineEntry> timeline = new ArrayList<>();

  public GetCityTimeline200Response cityId(@Nullable String cityId) {
    this.cityId = cityId;
    return this;
  }

  /**
   * Get cityId
   * @return cityId
   */
  
  @Schema(name = "city_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("city_id")
  public @Nullable String getCityId() {
    return cityId;
  }

  public void setCityId(@Nullable String cityId) {
    this.cityId = cityId;
  }

  public GetCityTimeline200Response timeline(List<@Valid CityTimelineEntry> timeline) {
    this.timeline = timeline;
    return this;
  }

  public GetCityTimeline200Response addTimelineItem(CityTimelineEntry timelineItem) {
    if (this.timeline == null) {
      this.timeline = new ArrayList<>();
    }
    this.timeline.add(timelineItem);
    return this;
  }

  /**
   * Get timeline
   * @return timeline
   */
  @Valid 
  @Schema(name = "timeline", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timeline")
  public List<@Valid CityTimelineEntry> getTimeline() {
    return timeline;
  }

  public void setTimeline(List<@Valid CityTimelineEntry> timeline) {
    this.timeline = timeline;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetCityTimeline200Response getCityTimeline200Response = (GetCityTimeline200Response) o;
    return Objects.equals(this.cityId, getCityTimeline200Response.cityId) &&
        Objects.equals(this.timeline, getCityTimeline200Response.timeline);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cityId, timeline);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetCityTimeline200Response {\n");
    sb.append("    cityId: ").append(toIndentedString(cityId)).append("\n");
    sb.append("    timeline: ").append(toIndentedString(timeline)).append("\n");
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

