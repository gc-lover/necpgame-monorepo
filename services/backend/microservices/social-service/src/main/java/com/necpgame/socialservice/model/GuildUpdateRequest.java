package com.necpgame.socialservice.model;

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
 * GuildUpdateRequest
 */


public class GuildUpdateRequest {

  private @Nullable String description;

  private @Nullable String language;

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

  private @Nullable String playstyle;

  private @Nullable String emblemId;

  private @Nullable String rules;

  public GuildUpdateRequest description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public GuildUpdateRequest language(@Nullable String language) {
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

  public GuildUpdateRequest policy(@Nullable PolicyEnum policy) {
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

  public GuildUpdateRequest playstyle(@Nullable String playstyle) {
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

  public GuildUpdateRequest emblemId(@Nullable String emblemId) {
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

  public GuildUpdateRequest rules(@Nullable String rules) {
    this.rules = rules;
    return this;
  }

  /**
   * Get rules
   * @return rules
   */
  
  @Schema(name = "rules", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rules")
  public @Nullable String getRules() {
    return rules;
  }

  public void setRules(@Nullable String rules) {
    this.rules = rules;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuildUpdateRequest guildUpdateRequest = (GuildUpdateRequest) o;
    return Objects.equals(this.description, guildUpdateRequest.description) &&
        Objects.equals(this.language, guildUpdateRequest.language) &&
        Objects.equals(this.policy, guildUpdateRequest.policy) &&
        Objects.equals(this.playstyle, guildUpdateRequest.playstyle) &&
        Objects.equals(this.emblemId, guildUpdateRequest.emblemId) &&
        Objects.equals(this.rules, guildUpdateRequest.rules);
  }

  @Override
  public int hashCode() {
    return Objects.hash(description, language, policy, playstyle, emblemId, rules);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildUpdateRequest {\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    language: ").append(toIndentedString(language)).append("\n");
    sb.append("    policy: ").append(toIndentedString(policy)).append("\n");
    sb.append("    playstyle: ").append(toIndentedString(playstyle)).append("\n");
    sb.append("    emblemId: ").append(toIndentedString(emblemId)).append("\n");
    sb.append("    rules: ").append(toIndentedString(rules)).append("\n");
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

