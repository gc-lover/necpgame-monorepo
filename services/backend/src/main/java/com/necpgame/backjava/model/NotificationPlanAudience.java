package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * NotificationPlanAudience
 */

@JsonTypeName("NotificationPlan_audience")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class NotificationPlanAudience {

  @Valid
  private List<String> segments = new ArrayList<>();

  private @Nullable Boolean includeGuildLeaders;

  private @Nullable Boolean includeOnlinePlayers;

  public NotificationPlanAudience segments(List<String> segments) {
    this.segments = segments;
    return this;
  }

  public NotificationPlanAudience addSegmentsItem(String segmentsItem) {
    if (this.segments == null) {
      this.segments = new ArrayList<>();
    }
    this.segments.add(segmentsItem);
    return this;
  }

  /**
   * Get segments
   * @return segments
   */
  
  @Schema(name = "segments", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("segments")
  public List<String> getSegments() {
    return segments;
  }

  public void setSegments(List<String> segments) {
    this.segments = segments;
  }

  public NotificationPlanAudience includeGuildLeaders(@Nullable Boolean includeGuildLeaders) {
    this.includeGuildLeaders = includeGuildLeaders;
    return this;
  }

  /**
   * Get includeGuildLeaders
   * @return includeGuildLeaders
   */
  
  @Schema(name = "includeGuildLeaders", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("includeGuildLeaders")
  public @Nullable Boolean getIncludeGuildLeaders() {
    return includeGuildLeaders;
  }

  public void setIncludeGuildLeaders(@Nullable Boolean includeGuildLeaders) {
    this.includeGuildLeaders = includeGuildLeaders;
  }

  public NotificationPlanAudience includeOnlinePlayers(@Nullable Boolean includeOnlinePlayers) {
    this.includeOnlinePlayers = includeOnlinePlayers;
    return this;
  }

  /**
   * Get includeOnlinePlayers
   * @return includeOnlinePlayers
   */
  
  @Schema(name = "includeOnlinePlayers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("includeOnlinePlayers")
  public @Nullable Boolean getIncludeOnlinePlayers() {
    return includeOnlinePlayers;
  }

  public void setIncludeOnlinePlayers(@Nullable Boolean includeOnlinePlayers) {
    this.includeOnlinePlayers = includeOnlinePlayers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationPlanAudience notificationPlanAudience = (NotificationPlanAudience) o;
    return Objects.equals(this.segments, notificationPlanAudience.segments) &&
        Objects.equals(this.includeGuildLeaders, notificationPlanAudience.includeGuildLeaders) &&
        Objects.equals(this.includeOnlinePlayers, notificationPlanAudience.includeOnlinePlayers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(segments, includeGuildLeaders, includeOnlinePlayers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationPlanAudience {\n");
    sb.append("    segments: ").append(toIndentedString(segments)).append("\n");
    sb.append("    includeGuildLeaders: ").append(toIndentedString(includeGuildLeaders)).append("\n");
    sb.append("    includeOnlinePlayers: ").append(toIndentedString(includeOnlinePlayers)).append("\n");
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

