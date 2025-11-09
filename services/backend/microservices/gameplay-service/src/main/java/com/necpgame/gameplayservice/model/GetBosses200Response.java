package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.Boss;
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
 * GetBosses200Response
 */

@JsonTypeName("getBosses_200_response")

public class GetBosses200Response {

  @Valid
  private List<@Valid Boss> bosses = new ArrayList<>();

  public GetBosses200Response bosses(List<@Valid Boss> bosses) {
    this.bosses = bosses;
    return this;
  }

  public GetBosses200Response addBossesItem(Boss bossesItem) {
    if (this.bosses == null) {
      this.bosses = new ArrayList<>();
    }
    this.bosses.add(bossesItem);
    return this;
  }

  /**
   * Get bosses
   * @return bosses
   */
  @Valid 
  @Schema(name = "bosses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bosses")
  public List<@Valid Boss> getBosses() {
    return bosses;
  }

  public void setBosses(List<@Valid Boss> bosses) {
    this.bosses = bosses;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetBosses200Response getBosses200Response = (GetBosses200Response) o;
    return Objects.equals(this.bosses, getBosses200Response.bosses);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bosses);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetBosses200Response {\n");
    sb.append("    bosses: ").append(toIndentedString(bosses)).append("\n");
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

