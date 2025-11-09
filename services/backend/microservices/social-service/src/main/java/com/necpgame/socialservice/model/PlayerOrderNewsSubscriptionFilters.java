package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.PlayerOrderNewsTag;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PlayerOrderNewsSubscriptionFilters
 */

@JsonTypeName("PlayerOrderNewsSubscription_filters")

public class PlayerOrderNewsSubscriptionFilters {

  @Valid
  private List<UUID> cityIds = new ArrayList<>();

  @Valid
  private List<@Valid PlayerOrderNewsTag> tags = new ArrayList<>();

  /**
   * Gets or Sets severity
   */
  public enum SeverityEnum {
    INFO("info"),
    
    CAUTION("caution"),
    
    WARNING("warning"),
    
    CRITICAL("critical");

    private final String value;

    SeverityEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static SeverityEnum fromValue(String value) {
      for (SeverityEnum b : SeverityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @Valid
  private List<SeverityEnum> severity = new ArrayList<>();

  private @Nullable String language;

  public PlayerOrderNewsSubscriptionFilters cityIds(List<UUID> cityIds) {
    this.cityIds = cityIds;
    return this;
  }

  public PlayerOrderNewsSubscriptionFilters addCityIdsItem(UUID cityIdsItem) {
    if (this.cityIds == null) {
      this.cityIds = new ArrayList<>();
    }
    this.cityIds.add(cityIdsItem);
    return this;
  }

  /**
   * Get cityIds
   * @return cityIds
   */
  @Valid 
  @Schema(name = "cityIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cityIds")
  public List<UUID> getCityIds() {
    return cityIds;
  }

  public void setCityIds(List<UUID> cityIds) {
    this.cityIds = cityIds;
  }

  public PlayerOrderNewsSubscriptionFilters tags(List<@Valid PlayerOrderNewsTag> tags) {
    this.tags = tags;
    return this;
  }

  public PlayerOrderNewsSubscriptionFilters addTagsItem(PlayerOrderNewsTag tagsItem) {
    if (this.tags == null) {
      this.tags = new ArrayList<>();
    }
    this.tags.add(tagsItem);
    return this;
  }

  /**
   * Get tags
   * @return tags
   */
  @Valid 
  @Schema(name = "tags", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tags")
  public List<@Valid PlayerOrderNewsTag> getTags() {
    return tags;
  }

  public void setTags(List<@Valid PlayerOrderNewsTag> tags) {
    this.tags = tags;
  }

  public PlayerOrderNewsSubscriptionFilters severity(List<SeverityEnum> severity) {
    this.severity = severity;
    return this;
  }

  public PlayerOrderNewsSubscriptionFilters addSeverityItem(SeverityEnum severityItem) {
    if (this.severity == null) {
      this.severity = new ArrayList<>();
    }
    this.severity.add(severityItem);
    return this;
  }

  /**
   * Get severity
   * @return severity
   */
  
  @Schema(name = "severity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("severity")
  public List<SeverityEnum> getSeverity() {
    return severity;
  }

  public void setSeverity(List<SeverityEnum> severity) {
    this.severity = severity;
  }

  public PlayerOrderNewsSubscriptionFilters language(@Nullable String language) {
    this.language = language;
    return this;
  }

  /**
   * Get language
   * @return language
   */
  
  @Schema(name = "language", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("language")
  public @Nullable String getLanguage() {
    return language;
  }

  public void setLanguage(@Nullable String language) {
    this.language = language;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderNewsSubscriptionFilters playerOrderNewsSubscriptionFilters = (PlayerOrderNewsSubscriptionFilters) o;
    return Objects.equals(this.cityIds, playerOrderNewsSubscriptionFilters.cityIds) &&
        Objects.equals(this.tags, playerOrderNewsSubscriptionFilters.tags) &&
        Objects.equals(this.severity, playerOrderNewsSubscriptionFilters.severity) &&
        Objects.equals(this.language, playerOrderNewsSubscriptionFilters.language);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cityIds, tags, severity, language);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderNewsSubscriptionFilters {\n");
    sb.append("    cityIds: ").append(toIndentedString(cityIds)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    language: ").append(toIndentedString(language)).append("\n");
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

