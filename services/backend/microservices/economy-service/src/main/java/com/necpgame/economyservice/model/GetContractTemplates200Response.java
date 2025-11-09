package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.ContractTemplate;
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
 * GetContractTemplates200Response
 */

@JsonTypeName("getContractTemplates_200_response")

public class GetContractTemplates200Response {

  @Valid
  private List<@Valid ContractTemplate> templates = new ArrayList<>();

  public GetContractTemplates200Response templates(List<@Valid ContractTemplate> templates) {
    this.templates = templates;
    return this;
  }

  public GetContractTemplates200Response addTemplatesItem(ContractTemplate templatesItem) {
    if (this.templates == null) {
      this.templates = new ArrayList<>();
    }
    this.templates.add(templatesItem);
    return this;
  }

  /**
   * Get templates
   * @return templates
   */
  @Valid 
  @Schema(name = "templates", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("templates")
  public List<@Valid ContractTemplate> getTemplates() {
    return templates;
  }

  public void setTemplates(List<@Valid ContractTemplate> templates) {
    this.templates = templates;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetContractTemplates200Response getContractTemplates200Response = (GetContractTemplates200Response) o;
    return Objects.equals(this.templates, getContractTemplates200Response.templates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(templates);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetContractTemplates200Response {\n");
    sb.append("    templates: ").append(toIndentedString(templates)).append("\n");
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

