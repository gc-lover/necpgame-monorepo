package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GuildCreateRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GuildCreateRequest {

  private String name;

  private String tag;

  private @Nullable String description;

  private @Nullable String language;

  private String shard;

  /**
   * Gets or Sets policy
   */
  public enum PolicyEnum {
    OPEN("open"),
    
    INVITE_ONLY("invite_only"),
    
    APPLICATION("application");

    private final String value;

    PolicyEnum(String value) {
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
    public static PolicyEnum fromValue(String value) {
      for (PolicyEnum b : PolicyEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable PolicyEnum policy;

  private @Nullable String emblemId;

  private @Nullable String playstyle;

  public GuildCreateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GuildCreateRequest(String name, String tag, String shard) {
    this.name = name;
    this.tag = tag;
    this.shard = shard;
  }

  public GuildCreateRequest name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull @Size(min = 3, max = 32) 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public GuildCreateRequest tag(String tag) {
    this.tag = tag;
    return this;
  }

  /**
   * Get tag
   * @return tag
   */
  @NotNull @Size(min = 2, max = 5) 
  @Schema(name = "tag", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("tag")
  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public GuildCreateRequest description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  @Size(max = 512) 
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public GuildCreateRequest language(@Nullable String language) {
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

  public GuildCreateRequest shard(String shard) {
    this.shard = shard;
    return this;
  }

  /**
   * Get shard
   * @return shard
   */
  @NotNull 
  @Schema(name = "shard", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("shard")
  public String getShard() {
    return shard;
  }

  public void setShard(String shard) {
    this.shard = shard;
  }

  public GuildCreateRequest policy(@Nullable PolicyEnum policy) {
    this.policy = policy;
    return this;
  }

  /**
   * Get policy
   * @return policy
   */
  
  @Schema(name = "policy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("policy")
  public @Nullable PolicyEnum getPolicy() {
    return policy;
  }

  public void setPolicy(@Nullable PolicyEnum policy) {
    this.policy = policy;
  }

  public GuildCreateRequest emblemId(@Nullable String emblemId) {
    this.emblemId = emblemId;
    return this;
  }

  /**
   * Get emblemId
   * @return emblemId
   */
  
  @Schema(name = "emblemId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("emblemId")
  public @Nullable String getEmblemId() {
    return emblemId;
  }

  public void setEmblemId(@Nullable String emblemId) {
    this.emblemId = emblemId;
  }

  public GuildCreateRequest playstyle(@Nullable String playstyle) {
    this.playstyle = playstyle;
    return this;
  }

  /**
   * Get playstyle
   * @return playstyle
   */
  
  @Schema(name = "playstyle", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("playstyle")
  public @Nullable String getPlaystyle() {
    return playstyle;
  }

  public void setPlaystyle(@Nullable String playstyle) {
    this.playstyle = playstyle;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuildCreateRequest guildCreateRequest = (GuildCreateRequest) o;
    return Objects.equals(this.name, guildCreateRequest.name) &&
        Objects.equals(this.tag, guildCreateRequest.tag) &&
        Objects.equals(this.description, guildCreateRequest.description) &&
        Objects.equals(this.language, guildCreateRequest.language) &&
        Objects.equals(this.shard, guildCreateRequest.shard) &&
        Objects.equals(this.policy, guildCreateRequest.policy) &&
        Objects.equals(this.emblemId, guildCreateRequest.emblemId) &&
        Objects.equals(this.playstyle, guildCreateRequest.playstyle);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, tag, description, language, shard, policy, emblemId, playstyle);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildCreateRequest {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    tag: ").append(toIndentedString(tag)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    language: ").append(toIndentedString(language)).append("\n");
    sb.append("    shard: ").append(toIndentedString(shard)).append("\n");
    sb.append("    policy: ").append(toIndentedString(policy)).append("\n");
    sb.append("    emblemId: ").append(toIndentedString(emblemId)).append("\n");
    sb.append("    playstyle: ").append(toIndentedString(playstyle)).append("\n");
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

