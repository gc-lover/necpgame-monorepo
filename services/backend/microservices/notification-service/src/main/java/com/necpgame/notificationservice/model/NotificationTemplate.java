package com.necpgame.notificationservice.model;

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
 * NotificationTemplate
 */


public class NotificationTemplate {

  private @Nullable String templateId;

  private @Nullable String name;

  private @Nullable String category;

  private @Nullable String version;

  @Valid
  private List<String> locales = new ArrayList<>();

  @Valid
  private List<String> variables = new ArrayList<>();

  public NotificationTemplate templateId(@Nullable String templateId) {
    this.templateId = templateId;
    return this;
  }

  /**
   * Get templateId
   * @return templateId
   */
  
  @Schema(name = "templateId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("templateId")
  public @Nullable String getTemplateId() {
    return templateId;
  }

  public void setTemplateId(@Nullable String templateId) {
    this.templateId = templateId;
  }

  public NotificationTemplate name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public NotificationTemplate category(@Nullable String category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  
  @Schema(name = "category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category")
  public @Nullable String getCategory() {
    return category;
  }

  public void setCategory(@Nullable String category) {
    this.category = category;
  }

  public NotificationTemplate version(@Nullable String version) {
    this.version = version;
    return this;
  }

  /**
   * Get version
   * @return version
   */
  
  @Schema(name = "version", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("version")
  public @Nullable String getVersion() {
    return version;
  }

  public void setVersion(@Nullable String version) {
    this.version = version;
  }

  public NotificationTemplate locales(List<String> locales) {
    this.locales = locales;
    return this;
  }

  public NotificationTemplate addLocalesItem(String localesItem) {
    if (this.locales == null) {
      this.locales = new ArrayList<>();
    }
    this.locales.add(localesItem);
    return this;
  }

  /**
   * Get locales
   * @return locales
   */
  
  @Schema(name = "locales", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("locales")
  public List<String> getLocales() {
    return locales;
  }

  public void setLocales(List<String> locales) {
    this.locales = locales;
  }

  public NotificationTemplate variables(List<String> variables) {
    this.variables = variables;
    return this;
  }

  public NotificationTemplate addVariablesItem(String variablesItem) {
    if (this.variables == null) {
      this.variables = new ArrayList<>();
    }
    this.variables.add(variablesItem);
    return this;
  }

  /**
   * Get variables
   * @return variables
   */
  
  @Schema(name = "variables", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("variables")
  public List<String> getVariables() {
    return variables;
  }

  public void setVariables(List<String> variables) {
    this.variables = variables;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationTemplate notificationTemplate = (NotificationTemplate) o;
    return Objects.equals(this.templateId, notificationTemplate.templateId) &&
        Objects.equals(this.name, notificationTemplate.name) &&
        Objects.equals(this.category, notificationTemplate.category) &&
        Objects.equals(this.version, notificationTemplate.version) &&
        Objects.equals(this.locales, notificationTemplate.locales) &&
        Objects.equals(this.variables, notificationTemplate.variables);
  }

  @Override
  public int hashCode() {
    return Objects.hash(templateId, name, category, version, locales, variables);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationTemplate {\n");
    sb.append("    templateId: ").append(toIndentedString(templateId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    locales: ").append(toIndentedString(locales)).append("\n");
    sb.append("    variables: ").append(toIndentedString(variables)).append("\n");
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

