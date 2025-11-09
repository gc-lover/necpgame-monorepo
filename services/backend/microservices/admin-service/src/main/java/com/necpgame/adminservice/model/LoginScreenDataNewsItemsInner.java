package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LoginScreenDataNewsItemsInner
 */

@JsonTypeName("LoginScreenData_news_items_inner")

public class LoginScreenDataNewsItemsInner {

  private @Nullable String headline;

  private @Nullable String summary;

  private @Nullable String imageUrl;

  public LoginScreenDataNewsItemsInner headline(@Nullable String headline) {
    this.headline = headline;
    return this;
  }

  /**
   * Get headline
   * @return headline
   */
  
  @Schema(name = "headline", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("headline")
  public @Nullable String getHeadline() {
    return headline;
  }

  public void setHeadline(@Nullable String headline) {
    this.headline = headline;
  }

  public LoginScreenDataNewsItemsInner summary(@Nullable String summary) {
    this.summary = summary;
    return this;
  }

  /**
   * Get summary
   * @return summary
   */
  
  @Schema(name = "summary", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("summary")
  public @Nullable String getSummary() {
    return summary;
  }

  public void setSummary(@Nullable String summary) {
    this.summary = summary;
  }

  public LoginScreenDataNewsItemsInner imageUrl(@Nullable String imageUrl) {
    this.imageUrl = imageUrl;
    return this;
  }

  /**
   * Get imageUrl
   * @return imageUrl
   */
  
  @Schema(name = "image_url", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("image_url")
  public @Nullable String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(@Nullable String imageUrl) {
    this.imageUrl = imageUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LoginScreenDataNewsItemsInner loginScreenDataNewsItemsInner = (LoginScreenDataNewsItemsInner) o;
    return Objects.equals(this.headline, loginScreenDataNewsItemsInner.headline) &&
        Objects.equals(this.summary, loginScreenDataNewsItemsInner.summary) &&
        Objects.equals(this.imageUrl, loginScreenDataNewsItemsInner.imageUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(headline, summary, imageUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoginScreenDataNewsItemsInner {\n");
    sb.append("    headline: ").append(toIndentedString(headline)).append("\n");
    sb.append("    summary: ").append(toIndentedString(summary)).append("\n");
    sb.append("    imageUrl: ").append(toIndentedString(imageUrl)).append("\n");
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

