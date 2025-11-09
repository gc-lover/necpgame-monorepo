package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.FurniturePlacement;
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
 * FurniturePlacementRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class FurniturePlacementRequest {

  /**
   * Gets or Sets action
   */
  public enum ActionEnum {
    PLACE("place"),
    
    MOVE("move"),
    
    REMOVE("remove");

    private final String value;

    ActionEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ActionEnum fromValue(String value) {
      for (ActionEnum b : ActionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ActionEnum action;

  @Valid
  private List<@Valid FurniturePlacement> placements = new ArrayList<>();

  public FurniturePlacementRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public FurniturePlacementRequest(ActionEnum action, List<@Valid FurniturePlacement> placements) {
    this.action = action;
    this.placements = placements;
  }

  public FurniturePlacementRequest action(ActionEnum action) {
    this.action = action;
    return this;
  }

  /**
   * Get action
   * @return action
   */
  @NotNull 
  @Schema(name = "action", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("action")
  public ActionEnum getAction() {
    return action;
  }

  public void setAction(ActionEnum action) {
    this.action = action;
  }

  public FurniturePlacementRequest placements(List<@Valid FurniturePlacement> placements) {
    this.placements = placements;
    return this;
  }

  public FurniturePlacementRequest addPlacementsItem(FurniturePlacement placementsItem) {
    if (this.placements == null) {
      this.placements = new ArrayList<>();
    }
    this.placements.add(placementsItem);
    return this;
  }

  /**
   * Get placements
   * @return placements
   */
  @NotNull @Valid 
  @Schema(name = "placements", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("placements")
  public List<@Valid FurniturePlacement> getPlacements() {
    return placements;
  }

  public void setPlacements(List<@Valid FurniturePlacement> placements) {
    this.placements = placements;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FurniturePlacementRequest furniturePlacementRequest = (FurniturePlacementRequest) o;
    return Objects.equals(this.action, furniturePlacementRequest.action) &&
        Objects.equals(this.placements, furniturePlacementRequest.placements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(action, placements);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FurniturePlacementRequest {\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    placements: ").append(toIndentedString(placements)).append("\n");
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

