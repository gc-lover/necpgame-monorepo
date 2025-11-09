package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.LootRoll;
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
 * ActiveRollsResponse
 */


public class ActiveRollsResponse {

  @Valid
  private List<@Valid LootRoll> rolls = new ArrayList<>();

  public ActiveRollsResponse rolls(List<@Valid LootRoll> rolls) {
    this.rolls = rolls;
    return this;
  }

  public ActiveRollsResponse addRollsItem(LootRoll rollsItem) {
    if (this.rolls == null) {
      this.rolls = new ArrayList<>();
    }
    this.rolls.add(rollsItem);
    return this;
  }

  /**
   * Get rolls
   * @return rolls
   */
  @Valid 
  @Schema(name = "rolls", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rolls")
  public List<@Valid LootRoll> getRolls() {
    return rolls;
  }

  public void setRolls(List<@Valid LootRoll> rolls) {
    this.rolls = rolls;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ActiveRollsResponse activeRollsResponse = (ActiveRollsResponse) o;
    return Objects.equals(this.rolls, activeRollsResponse.rolls);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rolls);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActiveRollsResponse {\n");
    sb.append("    rolls: ").append(toIndentedString(rolls)).append("\n");
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

