package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.Combo;
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
 * GetCombos200Response
 */

@JsonTypeName("getCombos_200_response")

public class GetCombos200Response {

  @Valid
  private List<@Valid Combo> combos = new ArrayList<>();

  public GetCombos200Response combos(List<@Valid Combo> combos) {
    this.combos = combos;
    return this;
  }

  public GetCombos200Response addCombosItem(Combo combosItem) {
    if (this.combos == null) {
      this.combos = new ArrayList<>();
    }
    this.combos.add(combosItem);
    return this;
  }

  /**
   * Get combos
   * @return combos
   */
  @Valid 
  @Schema(name = "combos", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("combos")
  public List<@Valid Combo> getCombos() {
    return combos;
  }

  public void setCombos(List<@Valid Combo> combos) {
    this.combos = combos;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetCombos200Response getCombos200Response = (GetCombos200Response) o;
    return Objects.equals(this.combos, getCombos200Response.combos);
  }

  @Override
  public int hashCode() {
    return Objects.hash(combos);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetCombos200Response {\n");
    sb.append("    combos: ").append(toIndentedString(combos)).append("\n");
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

