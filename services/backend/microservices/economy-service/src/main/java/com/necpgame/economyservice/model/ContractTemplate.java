package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.economyservice.model.ContractTerms;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ContractTemplate
 */


public class ContractTemplate {

  private @Nullable String templateId;

  private @Nullable String name;

  private @Nullable String type;

  private @Nullable String description;

  private @Nullable ContractTerms defaultTerms;

  private @Nullable Boolean popular;

  public ContractTemplate templateId(@Nullable String templateId) {
    this.templateId = templateId;
    return this;
  }

  /**
   * Get templateId
   * @return templateId
   */
  
  @Schema(name = "template_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("template_id")
  public @Nullable String getTemplateId() {
    return templateId;
  }

  public void setTemplateId(@Nullable String templateId) {
    this.templateId = templateId;
  }

  public ContractTemplate name(@Nullable String name) {
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

  public ContractTemplate type(@Nullable String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable String getType() {
    return type;
  }

  public void setType(@Nullable String type) {
    this.type = type;
  }

  public ContractTemplate description(@Nullable String description) {
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

  public ContractTemplate defaultTerms(@Nullable ContractTerms defaultTerms) {
    this.defaultTerms = defaultTerms;
    return this;
  }

  /**
   * Get defaultTerms
   * @return defaultTerms
   */
  @Valid 
  @Schema(name = "default_terms", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("default_terms")
  public @Nullable ContractTerms getDefaultTerms() {
    return defaultTerms;
  }

  public void setDefaultTerms(@Nullable ContractTerms defaultTerms) {
    this.defaultTerms = defaultTerms;
  }

  public ContractTemplate popular(@Nullable Boolean popular) {
    this.popular = popular;
    return this;
  }

  /**
   * Get popular
   * @return popular
   */
  
  @Schema(name = "popular", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("popular")
  public @Nullable Boolean getPopular() {
    return popular;
  }

  public void setPopular(@Nullable Boolean popular) {
    this.popular = popular;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContractTemplate contractTemplate = (ContractTemplate) o;
    return Objects.equals(this.templateId, contractTemplate.templateId) &&
        Objects.equals(this.name, contractTemplate.name) &&
        Objects.equals(this.type, contractTemplate.type) &&
        Objects.equals(this.description, contractTemplate.description) &&
        Objects.equals(this.defaultTerms, contractTemplate.defaultTerms) &&
        Objects.equals(this.popular, contractTemplate.popular);
  }

  @Override
  public int hashCode() {
    return Objects.hash(templateId, name, type, description, defaultTerms, popular);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContractTemplate {\n");
    sb.append("    templateId: ").append(toIndentedString(templateId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    defaultTerms: ").append(toIndentedString(defaultTerms)).append("\n");
    sb.append("    popular: ").append(toIndentedString(popular)).append("\n");
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

