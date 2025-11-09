package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.ModelDefinition;
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
 * GetMVPModels200Response
 */

@JsonTypeName("getMVPModels_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetMVPModels200Response {

  @Valid
  private List<@Valid ModelDefinition> models = new ArrayList<>();

  public GetMVPModels200Response models(List<@Valid ModelDefinition> models) {
    this.models = models;
    return this;
  }

  public GetMVPModels200Response addModelsItem(ModelDefinition modelsItem) {
    if (this.models == null) {
      this.models = new ArrayList<>();
    }
    this.models.add(modelsItem);
    return this;
  }

  /**
   * Get models
   * @return models
   */
  @Valid 
  @Schema(name = "models", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("models")
  public List<@Valid ModelDefinition> getModels() {
    return models;
  }

  public void setModels(List<@Valid ModelDefinition> models) {
    this.models = models;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetMVPModels200Response getMVPModels200Response = (GetMVPModels200Response) o;
    return Objects.equals(this.models, getMVPModels200Response.models);
  }

  @Override
  public int hashCode() {
    return Objects.hash(models);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetMVPModels200Response {\n");
    sb.append("    models: ").append(toIndentedString(models)).append("\n");
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

