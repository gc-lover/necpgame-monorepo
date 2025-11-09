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
 * PlayerOrderAttachmentUpload
 */


public class PlayerOrderAttachmentUpload {

  private org.springframework.core.io.Resource file;

  private String description;

  public PlayerOrderAttachmentUpload() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderAttachmentUpload(org.springframework.core.io.Resource file, String description) {
    this.file = file;
    this.description = description;
  }

  public PlayerOrderAttachmentUpload file(org.springframework.core.io.Resource file) {
    this.file = file;
    return this;
  }

  /**
   * Файл вложения.
   * @return file
   */
  @NotNull @Valid 
  @Schema(name = "file", description = "Файл вложения.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("file")
  public org.springframework.core.io.Resource getFile() {
    return file;
  }

  public void setFile(org.springframework.core.io.Resource file) {
    this.file = file;
  }

  public PlayerOrderAttachmentUpload description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Подпись или пояснение к вложению.
   * @return description
   */
  @NotNull 
  @Schema(name = "description", description = "Подпись или пояснение к вложению.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderAttachmentUpload playerOrderAttachmentUpload = (PlayerOrderAttachmentUpload) o;
    return Objects.equals(this.file, playerOrderAttachmentUpload.file) &&
        Objects.equals(this.description, playerOrderAttachmentUpload.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(file, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderAttachmentUpload {\n");
    sb.append("    file: ").append(toIndentedString(file)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

