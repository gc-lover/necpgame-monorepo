package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.Announcement;
import com.necpgame.adminservice.model.AnnouncementContent;
import com.necpgame.adminservice.model.HistoryResponse;
import com.necpgame.adminservice.model.ScheduleInfo;
import com.necpgame.adminservice.model.TranslationPayload;
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
 * AnnouncementDetail
 */


public class AnnouncementDetail {

  private @Nullable Announcement announcement;

  private @Nullable AnnouncementContent content;

  private @Nullable ScheduleInfo schedule;

  @Valid
  private List<@Valid TranslationPayload> locales = new ArrayList<>();

  private @Nullable HistoryResponse history;

  public AnnouncementDetail announcement(@Nullable Announcement announcement) {
    this.announcement = announcement;
    return this;
  }

  /**
   * Get announcement
   * @return announcement
   */
  @Valid 
  @Schema(name = "announcement", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("announcement")
  public @Nullable Announcement getAnnouncement() {
    return announcement;
  }

  public void setAnnouncement(@Nullable Announcement announcement) {
    this.announcement = announcement;
  }

  public AnnouncementDetail content(@Nullable AnnouncementContent content) {
    this.content = content;
    return this;
  }

  /**
   * Get content
   * @return content
   */
  @Valid 
  @Schema(name = "content", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("content")
  public @Nullable AnnouncementContent getContent() {
    return content;
  }

  public void setContent(@Nullable AnnouncementContent content) {
    this.content = content;
  }

  public AnnouncementDetail schedule(@Nullable ScheduleInfo schedule) {
    this.schedule = schedule;
    return this;
  }

  /**
   * Get schedule
   * @return schedule
   */
  @Valid 
  @Schema(name = "schedule", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("schedule")
  public @Nullable ScheduleInfo getSchedule() {
    return schedule;
  }

  public void setSchedule(@Nullable ScheduleInfo schedule) {
    this.schedule = schedule;
  }

  public AnnouncementDetail locales(List<@Valid TranslationPayload> locales) {
    this.locales = locales;
    return this;
  }

  public AnnouncementDetail addLocalesItem(TranslationPayload localesItem) {
    if (this.locales == null) {
      this.locales = new ArrayList<>();
    }
    this.locales.add(localesItem);
    return this;
  }

  /**
   * Get locales
   * @return locales
   */
  @Valid 
  @Schema(name = "locales", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("locales")
  public List<@Valid TranslationPayload> getLocales() {
    return locales;
  }

  public void setLocales(List<@Valid TranslationPayload> locales) {
    this.locales = locales;
  }

  public AnnouncementDetail history(@Nullable HistoryResponse history) {
    this.history = history;
    return this;
  }

  /**
   * Get history
   * @return history
   */
  @Valid 
  @Schema(name = "history", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("history")
  public @Nullable HistoryResponse getHistory() {
    return history;
  }

  public void setHistory(@Nullable HistoryResponse history) {
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
    AnnouncementDetail announcementDetail = (AnnouncementDetail) o;
    return Objects.equals(this.announcement, announcementDetail.announcement) &&
        Objects.equals(this.content, announcementDetail.content) &&
        Objects.equals(this.schedule, announcementDetail.schedule) &&
        Objects.equals(this.locales, announcementDetail.locales) &&
        Objects.equals(this.history, announcementDetail.history);
  }

  @Override
  public int hashCode() {
    return Objects.hash(announcement, content, schedule, locales, history);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnnouncementDetail {\n");
    sb.append("    announcement: ").append(toIndentedString(announcement)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    schedule: ").append(toIndentedString(schedule)).append("\n");
    sb.append("    locales: ").append(toIndentedString(locales)).append("\n");
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

