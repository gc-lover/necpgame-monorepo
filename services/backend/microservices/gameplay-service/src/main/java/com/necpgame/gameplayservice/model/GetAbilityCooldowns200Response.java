package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.AbilityCooldown;
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
 * GetAbilityCooldowns200Response
 */

@JsonTypeName("getAbilityCooldowns_200_response")

public class GetAbilityCooldowns200Response {

  @Valid
  private List<@Valid AbilityCooldown> cooldowns = new ArrayList<>();

  public GetAbilityCooldowns200Response cooldowns(List<@Valid AbilityCooldown> cooldowns) {
    this.cooldowns = cooldowns;
    return this;
  }

  public GetAbilityCooldowns200Response addCooldownsItem(AbilityCooldown cooldownsItem) {
    if (this.cooldowns == null) {
      this.cooldowns = new ArrayList<>();
    }
    this.cooldowns.add(cooldownsItem);
    return this;
  }

  /**
   * Get cooldowns
   * @return cooldowns
   */
  @Valid 
  @Schema(name = "cooldowns", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cooldowns")
  public List<@Valid AbilityCooldown> getCooldowns() {
    return cooldowns;
  }

  public void setCooldowns(List<@Valid AbilityCooldown> cooldowns) {
    this.cooldowns = cooldowns;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetAbilityCooldowns200Response getAbilityCooldowns200Response = (GetAbilityCooldowns200Response) o;
    return Objects.equals(this.cooldowns, getAbilityCooldowns200Response.cooldowns);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cooldowns);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetAbilityCooldowns200Response {\n");
    sb.append("    cooldowns: ").append(toIndentedString(cooldowns)).append("\n");
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

