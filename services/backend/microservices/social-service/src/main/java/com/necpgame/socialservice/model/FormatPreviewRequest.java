package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * FormatPreviewRequest
 */


public class FormatPreviewRequest {

  private String rawText;

  private @Nullable String channelType;

  private Boolean allowLinks = false;

  public FormatPreviewRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public FormatPreviewRequest(String rawText) {
    this.rawText = rawText;
  }

  public FormatPreviewRequest rawText(String rawText) {
    this.rawText = rawText;
    return this;
  }

  /**
   * Get rawText
   * @return rawText
   */
  @NotNull @Size(max = 2000) 
  @Schema(name = "rawText", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("rawText")
  public String getRawText() {
    return rawText;
  }

  public void setRawText(String rawText) {
    this.rawText = rawText;
  }

  public FormatPreviewRequest channelType(@Nullable String channelType) {
    this.channelType = channelType;
    return this;
  }

  /**
   * Get channelType
   * @return channelType
   */
  
  @Schema(name = "channelType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("channelType")
  public @Nullable String getChannelType() {
    return channelType;
  }

  public void setChannelType(@Nullable String channelType) {
    this.channelType = channelType;
  }

  public FormatPreviewRequest allowLinks(Boolean allowLinks) {
    this.allowLinks = allowLinks;
    return this;
  }

  /**
   * Get allowLinks
   * @return allowLinks
   */
  
  @Schema(name = "allowLinks", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("allowLinks")
  public Boolean getAllowLinks() {
    return allowLinks;
  }

  public void setAllowLinks(Boolean allowLinks) {
    this.allowLinks = allowLinks;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FormatPreviewRequest formatPreviewRequest = (FormatPreviewRequest) o;
    return Objects.equals(this.rawText, formatPreviewRequest.rawText) &&
        Objects.equals(this.channelType, formatPreviewRequest.channelType) &&
        Objects.equals(this.allowLinks, formatPreviewRequest.allowLinks);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rawText, channelType, allowLinks);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FormatPreviewRequest {\n");
    sb.append("    rawText: ").append(toIndentedString(rawText)).append("\n");
    sb.append("    channelType: ").append(toIndentedString(channelType)).append("\n");
    sb.append("    allowLinks: ").append(toIndentedString(allowLinks)).append("\n");
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

