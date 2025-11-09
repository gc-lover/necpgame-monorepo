package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.BoostStatus;
import com.necpgame.backjava.model.BoostStatusResponseCooldownsInner;
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
 * BoostStatusResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class BoostStatusResponse {

  @Valid
  private List<@Valid BoostStatus> activeBoosts = new ArrayList<>();

  @Valid
  private List<@Valid BoostStatusResponseCooldownsInner> cooldowns = new ArrayList<>();

  public BoostStatusResponse activeBoosts(List<@Valid BoostStatus> activeBoosts) {
    this.activeBoosts = activeBoosts;
    return this;
  }

  public BoostStatusResponse addActiveBoostsItem(BoostStatus activeBoostsItem) {
    if (this.activeBoosts == null) {
      this.activeBoosts = new ArrayList<>();
    }
    this.activeBoosts.add(activeBoostsItem);
    return this;
  }

  /**
   * Get activeBoosts
   * @return activeBoosts
   */
  @Valid 
  @Schema(name = "activeBoosts", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activeBoosts")
  public List<@Valid BoostStatus> getActiveBoosts() {
    return activeBoosts;
  }

  public void setActiveBoosts(List<@Valid BoostStatus> activeBoosts) {
    this.activeBoosts = activeBoosts;
  }

  public BoostStatusResponse cooldowns(List<@Valid BoostStatusResponseCooldownsInner> cooldowns) {
    this.cooldowns = cooldowns;
    return this;
  }

  public BoostStatusResponse addCooldownsItem(BoostStatusResponseCooldownsInner cooldownsItem) {
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
  public List<@Valid BoostStatusResponseCooldownsInner> getCooldowns() {
    return cooldowns;
  }

  public void setCooldowns(List<@Valid BoostStatusResponseCooldownsInner> cooldowns) {
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
    BoostStatusResponse boostStatusResponse = (BoostStatusResponse) o;
    return Objects.equals(this.activeBoosts, boostStatusResponse.activeBoosts) &&
        Objects.equals(this.cooldowns, boostStatusResponse.cooldowns);
  }

  @Override
  public int hashCode() {
    return Objects.hash(activeBoosts, cooldowns);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BoostStatusResponse {\n");
    sb.append("    activeBoosts: ").append(toIndentedString(activeBoosts)).append("\n");
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

