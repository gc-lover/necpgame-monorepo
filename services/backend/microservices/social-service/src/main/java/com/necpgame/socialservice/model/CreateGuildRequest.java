package com.necpgame.socialservice.model;

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
 * CreateGuildRequest
 */

@JsonTypeName("createGuild_request")

public class CreateGuildRequest {

  private String founderCharacterId;

  private String name;

  private String tag;

  public CreateGuildRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateGuildRequest(String founderCharacterId, String name, String tag) {
    this.founderCharacterId = founderCharacterId;
    this.name = name;
    this.tag = tag;
  }

  public CreateGuildRequest founderCharacterId(String founderCharacterId) {
    this.founderCharacterId = founderCharacterId;
    return this;
  }

  /**
   * Get founderCharacterId
   * @return founderCharacterId
   */
  @NotNull 
  @Schema(name = "founder_character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("founder_character_id")
  public String getFounderCharacterId() {
    return founderCharacterId;
  }

  public void setFounderCharacterId(String founderCharacterId) {
    this.founderCharacterId = founderCharacterId;
  }

  public CreateGuildRequest name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull @Size(min = 3, max = 50) 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CreateGuildRequest tag(String tag) {
    this.tag = tag;
    return this;
  }

  /**
   * Тег гильдии (уникальный)
   * @return tag
   */
  @NotNull @Size(min = 2, max = 4) 
  @Schema(name = "tag", description = "Тег гильдии (уникальный)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("tag")
  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateGuildRequest createGuildRequest = (CreateGuildRequest) o;
    return Objects.equals(this.founderCharacterId, createGuildRequest.founderCharacterId) &&
        Objects.equals(this.name, createGuildRequest.name) &&
        Objects.equals(this.tag, createGuildRequest.tag);
  }

  @Override
  public int hashCode() {
    return Objects.hash(founderCharacterId, name, tag);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateGuildRequest {\n");
    sb.append("    founderCharacterId: ").append(toIndentedString(founderCharacterId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    tag: ").append(toIndentedString(tag)).append("\n");
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

