package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.EnemyType;
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
 * GetEnemyTypes200Response
 */

@JsonTypeName("getEnemyTypes_200_response")

public class GetEnemyTypes200Response {

  @Valid
  private List<@Valid EnemyType> enemyTypes = new ArrayList<>();

  public GetEnemyTypes200Response enemyTypes(List<@Valid EnemyType> enemyTypes) {
    this.enemyTypes = enemyTypes;
    return this;
  }

  public GetEnemyTypes200Response addEnemyTypesItem(EnemyType enemyTypesItem) {
    if (this.enemyTypes == null) {
      this.enemyTypes = new ArrayList<>();
    }
    this.enemyTypes.add(enemyTypesItem);
    return this;
  }

  /**
   * Get enemyTypes
   * @return enemyTypes
   */
  @Valid 
  @Schema(name = "enemy_types", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("enemy_types")
  public List<@Valid EnemyType> getEnemyTypes() {
    return enemyTypes;
  }

  public void setEnemyTypes(List<@Valid EnemyType> enemyTypes) {
    this.enemyTypes = enemyTypes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetEnemyTypes200Response getEnemyTypes200Response = (GetEnemyTypes200Response) o;
    return Objects.equals(this.enemyTypes, getEnemyTypes200Response.enemyTypes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(enemyTypes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetEnemyTypes200Response {\n");
    sb.append("    enemyTypes: ").append(toIndentedString(enemyTypes)).append("\n");
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

