package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.narrativeservice.model.TimelineEvent;
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
 * GetTimeline200Response
 */

@JsonTypeName("getTimeline_200_response")

public class GetTimeline200Response {

  @Valid
  private List<@Valid TimelineEvent> timeline = new ArrayList<>();

  public GetTimeline200Response timeline(List<@Valid TimelineEvent> timeline) {
    this.timeline = timeline;
    return this;
  }

  public GetTimeline200Response addTimelineItem(TimelineEvent timelineItem) {
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
  public List<@Valid TimelineEvent> getTimeline() {
    return timeline;
  }

  public void setTimeline(List<@Valid TimelineEvent> timeline) {
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
    GetTimeline200Response getTimeline200Response = (GetTimeline200Response) o;
    return Objects.equals(this.timeline, getTimeline200Response.timeline);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timeline);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetTimeline200Response {\n");
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

