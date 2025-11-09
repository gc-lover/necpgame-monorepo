package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CheckStamina200ResponseAvailableActions
 */

@JsonTypeName("checkStamina_200_response_available_actions")

public class CheckStamina200ResponseAvailableActions {

  private @Nullable Boolean jump;

  private @Nullable Boolean climb;

  private @Nullable Boolean slide;

  private @Nullable Boolean grapple;

  public CheckStamina200ResponseAvailableActions jump(@Nullable Boolean jump) {
    this.jump = jump;
    return this;
  }

  /**
   * Get jump
   * @return jump
   */
  
  @Schema(name = "jump", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("jump")
  public @Nullable Boolean getJump() {
    return jump;
  }

  public void setJump(@Nullable Boolean jump) {
    this.jump = jump;
  }

  public CheckStamina200ResponseAvailableActions climb(@Nullable Boolean climb) {
    this.climb = climb;
    return this;
  }

  /**
   * Get climb
   * @return climb
   */
  
  @Schema(name = "climb", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("climb")
  public @Nullable Boolean getClimb() {
    return climb;
  }

  public void setClimb(@Nullable Boolean climb) {
    this.climb = climb;
  }

  public CheckStamina200ResponseAvailableActions slide(@Nullable Boolean slide) {
    this.slide = slide;
    return this;
  }

  /**
   * Get slide
   * @return slide
   */
  
  @Schema(name = "slide", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("slide")
  public @Nullable Boolean getSlide() {
    return slide;
  }

  public void setSlide(@Nullable Boolean slide) {
    this.slide = slide;
  }

  public CheckStamina200ResponseAvailableActions grapple(@Nullable Boolean grapple) {
    this.grapple = grapple;
    return this;
  }

  /**
   * Get grapple
   * @return grapple
   */
  
  @Schema(name = "grapple", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("grapple")
  public @Nullable Boolean getGrapple() {
    return grapple;
  }

  public void setGrapple(@Nullable Boolean grapple) {
    this.grapple = grapple;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CheckStamina200ResponseAvailableActions checkStamina200ResponseAvailableActions = (CheckStamina200ResponseAvailableActions) o;
    return Objects.equals(this.jump, checkStamina200ResponseAvailableActions.jump) &&
        Objects.equals(this.climb, checkStamina200ResponseAvailableActions.climb) &&
        Objects.equals(this.slide, checkStamina200ResponseAvailableActions.slide) &&
        Objects.equals(this.grapple, checkStamina200ResponseAvailableActions.grapple);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jump, climb, slide, grapple);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CheckStamina200ResponseAvailableActions {\n");
    sb.append("    jump: ").append(toIndentedString(jump)).append("\n");
    sb.append("    climb: ").append(toIndentedString(climb)).append("\n");
    sb.append("    slide: ").append(toIndentedString(slide)).append("\n");
    sb.append("    grapple: ").append(toIndentedString(grapple)).append("\n");
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

