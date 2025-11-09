package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * PreviewRequest
 */


public class PreviewRequest {

  @Valid
  private List<String> channels = new ArrayList<>();

  private @Nullable String locale;

  private @Nullable Boolean screenshotMode;

  public PreviewRequest channels(List<String> channels) {
    this.channels = channels;
    return this;
  }

  public PreviewRequest addChannelsItem(String channelsItem) {
    if (this.channels == null) {
      this.channels = new ArrayList<>();
    }
    this.channels.add(channelsItem);
    return this;
  }

  /**
   * Get channels
   * @return channels
   */
  
  @Schema(name = "channels", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("channels")
  public List<String> getChannels() {
    return channels;
  }

  public void setChannels(List<String> channels) {
    this.channels = channels;
  }

  public PreviewRequest locale(@Nullable String locale) {
    this.locale = locale;
    return this;
  }

  /**
   * Get locale
   * @return locale
   */
  
  @Schema(name = "locale", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("locale")
  public @Nullable String getLocale() {
    return locale;
  }

  public void setLocale(@Nullable String locale) {
    this.locale = locale;
  }

  public PreviewRequest screenshotMode(@Nullable Boolean screenshotMode) {
    this.screenshotMode = screenshotMode;
    return this;
  }

  /**
   * Get screenshotMode
   * @return screenshotMode
   */
  
  @Schema(name = "screenshotMode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("screenshotMode")
  public @Nullable Boolean getScreenshotMode() {
    return screenshotMode;
  }

  public void setScreenshotMode(@Nullable Boolean screenshotMode) {
    this.screenshotMode = screenshotMode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PreviewRequest previewRequest = (PreviewRequest) o;
    return Objects.equals(this.channels, previewRequest.channels) &&
        Objects.equals(this.locale, previewRequest.locale) &&
        Objects.equals(this.screenshotMode, previewRequest.screenshotMode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(channels, locale, screenshotMode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PreviewRequest {\n");
    sb.append("    channels: ").append(toIndentedString(channels)).append("\n");
    sb.append("    locale: ").append(toIndentedString(locale)).append("\n");
    sb.append("    screenshotMode: ").append(toIndentedString(screenshotMode)).append("\n");
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

