package com.necpgame.worldservice.model;

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
 * SocialBroadcastMemePayload
 */

@JsonTypeName("SocialBroadcast_memePayload")

public class SocialBroadcastMemePayload {

  private @Nullable String memeId;

  private @Nullable String template;

  @Valid
  private List<String> tags = new ArrayList<>();

  public SocialBroadcastMemePayload memeId(@Nullable String memeId) {
    this.memeId = memeId;
    return this;
  }

  /**
   * Get memeId
   * @return memeId
   */
  
  @Schema(name = "memeId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("memeId")
  public @Nullable String getMemeId() {
    return memeId;
  }

  public void setMemeId(@Nullable String memeId) {
    this.memeId = memeId;
  }

  public SocialBroadcastMemePayload template(@Nullable String template) {
    this.template = template;
    return this;
  }

  /**
   * Get template
   * @return template
   */
  
  @Schema(name = "template", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("template")
  public @Nullable String getTemplate() {
    return template;
  }

  public void setTemplate(@Nullable String template) {
    this.template = template;
  }

  public SocialBroadcastMemePayload tags(List<String> tags) {
    this.tags = tags;
    return this;
  }

  public SocialBroadcastMemePayload addTagsItem(String tagsItem) {
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
  
  @Schema(name = "tags", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tags")
  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SocialBroadcastMemePayload socialBroadcastMemePayload = (SocialBroadcastMemePayload) o;
    return Objects.equals(this.memeId, socialBroadcastMemePayload.memeId) &&
        Objects.equals(this.template, socialBroadcastMemePayload.template) &&
        Objects.equals(this.tags, socialBroadcastMemePayload.tags);
  }

  @Override
  public int hashCode() {
    return Objects.hash(memeId, template, tags);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SocialBroadcastMemePayload {\n");
    sb.append("    memeId: ").append(toIndentedString(memeId)).append("\n");
    sb.append("    template: ").append(toIndentedString(template)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
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

