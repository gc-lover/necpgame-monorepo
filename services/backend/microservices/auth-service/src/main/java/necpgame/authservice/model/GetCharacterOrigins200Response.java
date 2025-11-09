package com.necpgame.authservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.authservice.model.GameCharacterOrigin;
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
 * GetCharacterOrigins200Response
 */

@JsonTypeName("getCharacterOrigins_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetCharacterOrigins200Response {

  @Valid
  private List<@Valid GameCharacterOrigin> origins = new ArrayList<>();

  public GetCharacterOrigins200Response() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GetCharacterOrigins200Response(List<@Valid GameCharacterOrigin> origins) {
    this.origins = origins;
  }

  public GetCharacterOrigins200Response origins(List<@Valid GameCharacterOrigin> origins) {
    this.origins = origins;
    return this;
  }

  public GetCharacterOrigins200Response addOriginsItem(GameCharacterOrigin originsItem) {
    if (this.origins == null) {
      this.origins = new ArrayList<>();
    }
    this.origins.add(originsItem);
    return this;
  }

  /**
   * Список доступных происхождений
   * @return origins
   */
  @NotNull @Valid 
  @Schema(name = "origins", description = "Список доступных происхождений", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("origins")
  public List<@Valid GameCharacterOrigin> getOrigins() {
    return origins;
  }

  public void setOrigins(List<@Valid GameCharacterOrigin> origins) {
    this.origins = origins;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetCharacterOrigins200Response getCharacterOrigins200Response = (GetCharacterOrigins200Response) o;
    return Objects.equals(this.origins, getCharacterOrigins200Response.origins);
  }

  @Override
  public int hashCode() {
    return Objects.hash(origins);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetCharacterOrigins200Response {\n");
    sb.append("    origins: ").append(toIndentedString(origins)).append("\n");
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

