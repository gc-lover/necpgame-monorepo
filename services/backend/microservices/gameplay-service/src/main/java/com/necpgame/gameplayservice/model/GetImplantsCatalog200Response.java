package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.Implant;
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
 * GetImplantsCatalog200Response
 */

@JsonTypeName("getImplantsCatalog_200_response")

public class GetImplantsCatalog200Response {

  @Valid
  private List<@Valid Implant> implants = new ArrayList<>();

  public GetImplantsCatalog200Response implants(List<@Valid Implant> implants) {
    this.implants = implants;
    return this;
  }

  public GetImplantsCatalog200Response addImplantsItem(Implant implantsItem) {
    if (this.implants == null) {
      this.implants = new ArrayList<>();
    }
    this.implants.add(implantsItem);
    return this;
  }

  /**
   * Get implants
   * @return implants
   */
  @Valid 
  @Schema(name = "implants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("implants")
  public List<@Valid Implant> getImplants() {
    return implants;
  }

  public void setImplants(List<@Valid Implant> implants) {
    this.implants = implants;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetImplantsCatalog200Response getImplantsCatalog200Response = (GetImplantsCatalog200Response) o;
    return Objects.equals(this.implants, getImplantsCatalog200Response.implants);
  }

  @Override
  public int hashCode() {
    return Objects.hash(implants);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetImplantsCatalog200Response {\n");
    sb.append("    implants: ").append(toIndentedString(implants)).append("\n");
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

