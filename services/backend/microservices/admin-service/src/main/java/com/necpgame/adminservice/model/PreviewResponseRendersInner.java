package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.net.URI;
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
 * PreviewResponseRendersInner
 */

@JsonTypeName("PreviewResponse_renders_inner")

public class PreviewResponseRendersInner {

  private @Nullable String channel;

  private @Nullable String content;

  @Valid
  private List<URI> assets = new ArrayList<>();

  public PreviewResponseRendersInner channel(@Nullable String channel) {
    this.channel = channel;
    return this;
  }

  /**
   * Get channel
   * @return channel
   */
  
  @Schema(name = "channel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("channel")
  public @Nullable String getChannel() {
    return channel;
  }

  public void setChannel(@Nullable String channel) {
    this.channel = channel;
  }

  public PreviewResponseRendersInner content(@Nullable String content) {
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

  public PreviewResponseRendersInner assets(List<URI> assets) {
    this.assets = assets;
    return this;
  }

  public PreviewResponseRendersInner addAssetsItem(URI assetsItem) {
    if (this.assets == null) {
      this.assets = new ArrayList<>();
    }
    this.assets.add(assetsItem);
    return this;
  }

  /**
   * Get assets
   * @return assets
   */
  @Valid 
  @Schema(name = "assets", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("assets")
  public List<URI> getAssets() {
    return assets;
  }

  public void setAssets(List<URI> assets) {
    this.assets = assets;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PreviewResponseRendersInner previewResponseRendersInner = (PreviewResponseRendersInner) o;
    return Objects.equals(this.channel, previewResponseRendersInner.channel) &&
        Objects.equals(this.content, previewResponseRendersInner.content) &&
        Objects.equals(this.assets, previewResponseRendersInner.assets);
  }

  @Override
  public int hashCode() {
    return Objects.hash(channel, content, assets);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PreviewResponseRendersInner {\n");
    sb.append("    channel: ").append(toIndentedString(channel)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    assets: ").append(toIndentedString(assets)).append("\n");
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

