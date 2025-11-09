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
 * LoginScreenDataNewsInner
 */

@JsonTypeName("LoginScreenData_news_inner")

public class LoginScreenDataNewsInner {

  private @Nullable String title;

  private @Nullable String content;

  private @Nullable String imageUrl;

  public LoginScreenDataNewsInner title(@Nullable String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   */
  
  @Schema(name = "title", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  public @Nullable String getTitle() {
    return title;
  }

  public void setTitle(@Nullable String title) {
    this.title = title;
  }

  public LoginScreenDataNewsInner content(@Nullable String content) {
    this.content = content;
    return this;
  }

  /**
   * Get content
   * @return content
   */
  
  @Schema(name = "content", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("content")
  public @Nullable String getContent() {
    return content;
  }

  public void setContent(@Nullable String content) {
    this.content = content;
  }

  public LoginScreenDataNewsInner imageUrl(@Nullable String imageUrl) {
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
    LoginScreenDataNewsInner loginScreenDataNewsInner = (LoginScreenDataNewsInner) o;
    return Objects.equals(this.title, loginScreenDataNewsInner.title) &&
        Objects.equals(this.content, loginScreenDataNewsInner.content) &&
        Objects.equals(this.imageUrl, loginScreenDataNewsInner.imageUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, content, imageUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoginScreenDataNewsInner {\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
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

