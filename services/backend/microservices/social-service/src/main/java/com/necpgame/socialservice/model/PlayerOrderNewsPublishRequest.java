package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.PlayerOrderNewsAttachment;
import com.necpgame.socialservice.model.PlayerOrderNewsTag;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PlayerOrderNewsPublishRequest
 */


public class PlayerOrderNewsPublishRequest {

  private String title;

  private String summary;

  private @Nullable String body;

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

  private SeverityEnum severity;

  @Valid
  private List<UUID> orderIds = new ArrayList<>();

  @Valid
  private List<UUID> cityIds = new ArrayList<>();

  @Valid
  private List<@Valid PlayerOrderNewsTag> tags = new ArrayList<>();

  @Valid
  private List<@Valid PlayerOrderNewsAttachment> attachments = new ArrayList<>();

  private @Nullable String language;

  /**
   * Gets or Sets visibility
   */
  public enum VisibilityEnum {
    PUBLIC("public"),
    
    FACTION("faction"),
    
    PRIVATE("private");

    private final String value;

    VisibilityEnum(String value) {
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
    public static VisibilityEnum fromValue(String value) {
      for (VisibilityEnum b : VisibilityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable VisibilityEnum visibility;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime scheduledAt;

  public PlayerOrderNewsPublishRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderNewsPublishRequest(String title, String summary, SeverityEnum severity, List<@Valid PlayerOrderNewsTag> tags) {
    this.title = title;
    this.summary = summary;
    this.severity = severity;
    this.tags = tags;
  }

  public PlayerOrderNewsPublishRequest title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   */
  @NotNull @Size(min = 8) 
  @Schema(name = "title", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public PlayerOrderNewsPublishRequest summary(String summary) {
    this.summary = summary;
    return this;
  }

  /**
   * Get summary
   * @return summary
   */
  @NotNull @Size(min = 16) 
  @Schema(name = "summary", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("summary")
  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public PlayerOrderNewsPublishRequest body(@Nullable String body) {
    this.body = body;
    return this;
  }

  /**
   * Get body
   * @return body
   */
  
  @Schema(name = "body", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("body")
  public @Nullable String getBody() {
    return body;
  }

  public void setBody(@Nullable String body) {
    this.body = body;
  }

  public PlayerOrderNewsPublishRequest severity(SeverityEnum severity) {
    this.severity = severity;
    return this;
  }

  /**
   * Get severity
   * @return severity
   */
  @NotNull 
  @Schema(name = "severity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("severity")
  public SeverityEnum getSeverity() {
    return severity;
  }

  public void setSeverity(SeverityEnum severity) {
    this.severity = severity;
  }

  public PlayerOrderNewsPublishRequest orderIds(List<UUID> orderIds) {
    this.orderIds = orderIds;
    return this;
  }

  public PlayerOrderNewsPublishRequest addOrderIdsItem(UUID orderIdsItem) {
    if (this.orderIds == null) {
      this.orderIds = new ArrayList<>();
    }
    this.orderIds.add(orderIdsItem);
    return this;
  }

  /**
   * Get orderIds
   * @return orderIds
   */
  @Valid 
  @Schema(name = "orderIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("orderIds")
  public List<UUID> getOrderIds() {
    return orderIds;
  }

  public void setOrderIds(List<UUID> orderIds) {
    this.orderIds = orderIds;
  }

  public PlayerOrderNewsPublishRequest cityIds(List<UUID> cityIds) {
    this.cityIds = cityIds;
    return this;
  }

  public PlayerOrderNewsPublishRequest addCityIdsItem(UUID cityIdsItem) {
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

  public PlayerOrderNewsPublishRequest tags(List<@Valid PlayerOrderNewsTag> tags) {
    this.tags = tags;
    return this;
  }

  public PlayerOrderNewsPublishRequest addTagsItem(PlayerOrderNewsTag tagsItem) {
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
  @NotNull @Valid @Size(min = 1) 
  @Schema(name = "tags", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("tags")
  public List<@Valid PlayerOrderNewsTag> getTags() {
    return tags;
  }

  public void setTags(List<@Valid PlayerOrderNewsTag> tags) {
    this.tags = tags;
  }

  public PlayerOrderNewsPublishRequest attachments(List<@Valid PlayerOrderNewsAttachment> attachments) {
    this.attachments = attachments;
    return this;
  }

  public PlayerOrderNewsPublishRequest addAttachmentsItem(PlayerOrderNewsAttachment attachmentsItem) {
    if (this.attachments == null) {
      this.attachments = new ArrayList<>();
    }
    this.attachments.add(attachmentsItem);
    return this;
  }

  /**
   * Get attachments
   * @return attachments
   */
  @Valid 
  @Schema(name = "attachments", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attachments")
  public List<@Valid PlayerOrderNewsAttachment> getAttachments() {
    return attachments;
  }

  public void setAttachments(List<@Valid PlayerOrderNewsAttachment> attachments) {
    this.attachments = attachments;
  }

  public PlayerOrderNewsPublishRequest language(@Nullable String language) {
    this.language = language;
    return this;
  }

  /**
   * Get language
   * @return language
   */
  @Pattern(regexp = "^[a-zA-Z]{2,3}(-[a-zA-Z]{2})?$") 
  @Schema(name = "language", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("language")
  public @Nullable String getLanguage() {
    return language;
  }

  public void setLanguage(@Nullable String language) {
    this.language = language;
  }

  public PlayerOrderNewsPublishRequest visibility(@Nullable VisibilityEnum visibility) {
    this.visibility = visibility;
    return this;
  }

  /**
   * Get visibility
   * @return visibility
   */
  
  @Schema(name = "visibility", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("visibility")
  public @Nullable VisibilityEnum getVisibility() {
    return visibility;
  }

  public void setVisibility(@Nullable VisibilityEnum visibility) {
    this.visibility = visibility;
  }

  public PlayerOrderNewsPublishRequest scheduledAt(@Nullable OffsetDateTime scheduledAt) {
    this.scheduledAt = scheduledAt;
    return this;
  }

  /**
   * Get scheduledAt
   * @return scheduledAt
   */
  @Valid 
  @Schema(name = "scheduledAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("scheduledAt")
  public @Nullable OffsetDateTime getScheduledAt() {
    return scheduledAt;
  }

  public void setScheduledAt(@Nullable OffsetDateTime scheduledAt) {
    this.scheduledAt = scheduledAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderNewsPublishRequest playerOrderNewsPublishRequest = (PlayerOrderNewsPublishRequest) o;
    return Objects.equals(this.title, playerOrderNewsPublishRequest.title) &&
        Objects.equals(this.summary, playerOrderNewsPublishRequest.summary) &&
        Objects.equals(this.body, playerOrderNewsPublishRequest.body) &&
        Objects.equals(this.severity, playerOrderNewsPublishRequest.severity) &&
        Objects.equals(this.orderIds, playerOrderNewsPublishRequest.orderIds) &&
        Objects.equals(this.cityIds, playerOrderNewsPublishRequest.cityIds) &&
        Objects.equals(this.tags, playerOrderNewsPublishRequest.tags) &&
        Objects.equals(this.attachments, playerOrderNewsPublishRequest.attachments) &&
        Objects.equals(this.language, playerOrderNewsPublishRequest.language) &&
        Objects.equals(this.visibility, playerOrderNewsPublishRequest.visibility) &&
        Objects.equals(this.scheduledAt, playerOrderNewsPublishRequest.scheduledAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, summary, body, severity, orderIds, cityIds, tags, attachments, language, visibility, scheduledAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderNewsPublishRequest {\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    summary: ").append(toIndentedString(summary)).append("\n");
    sb.append("    body: ").append(toIndentedString(body)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    orderIds: ").append(toIndentedString(orderIds)).append("\n");
    sb.append("    cityIds: ").append(toIndentedString(cityIds)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
    sb.append("    attachments: ").append(toIndentedString(attachments)).append("\n");
    sb.append("    language: ").append(toIndentedString(language)).append("\n");
    sb.append("    visibility: ").append(toIndentedString(visibility)).append("\n");
    sb.append("    scheduledAt: ").append(toIndentedString(scheduledAt)).append("\n");
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

