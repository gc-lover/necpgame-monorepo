package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.FurniturePlacementCustomizationOptions;
import com.necpgame.gameplayservice.model.FurniturePlacementPosition;
import com.necpgame.gameplayservice.model.FurniturePlacementRotation;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * FurniturePlacement
 */


public class FurniturePlacement {

  private String itemId;

  private String slotId;

  private @Nullable FurniturePlacementPosition position;

  private @Nullable FurniturePlacementRotation rotation;

  private @Nullable FurniturePlacementCustomizationOptions customizationOptions;

  public FurniturePlacement() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public FurniturePlacement(String itemId, String slotId) {
    this.itemId = itemId;
    this.slotId = slotId;
  }

  public FurniturePlacement itemId(String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  @NotNull 
  @Schema(name = "itemId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("itemId")
  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public FurniturePlacement slotId(String slotId) {
    this.slotId = slotId;
    return this;
  }

  /**
   * Get slotId
   * @return slotId
   */
  @NotNull 
  @Schema(name = "slotId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("slotId")
  public String getSlotId() {
    return slotId;
  }

  public void setSlotId(String slotId) {
    this.slotId = slotId;
  }

  public FurniturePlacement position(@Nullable FurniturePlacementPosition position) {
    this.position = position;
    return this;
  }

  /**
   * Get position
   * @return position
   */
  @Valid 
  @Schema(name = "position", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("position")
  public @Nullable FurniturePlacementPosition getPosition() {
    return position;
  }

  public void setPosition(@Nullable FurniturePlacementPosition position) {
    this.position = position;
  }

  public FurniturePlacement rotation(@Nullable FurniturePlacementRotation rotation) {
    this.rotation = rotation;
    return this;
  }

  /**
   * Get rotation
   * @return rotation
   */
  @Valid 
  @Schema(name = "rotation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rotation")
  public @Nullable FurniturePlacementRotation getRotation() {
    return rotation;
  }

  public void setRotation(@Nullable FurniturePlacementRotation rotation) {
    this.rotation = rotation;
  }

  public FurniturePlacement customizationOptions(@Nullable FurniturePlacementCustomizationOptions customizationOptions) {
    this.customizationOptions = customizationOptions;
    return this;
  }

  /**
   * Get customizationOptions
   * @return customizationOptions
   */
  @Valid 
  @Schema(name = "customizationOptions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("customizationOptions")
  public @Nullable FurniturePlacementCustomizationOptions getCustomizationOptions() {
    return customizationOptions;
  }

  public void setCustomizationOptions(@Nullable FurniturePlacementCustomizationOptions customizationOptions) {
    this.customizationOptions = customizationOptions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FurniturePlacement furniturePlacement = (FurniturePlacement) o;
    return Objects.equals(this.itemId, furniturePlacement.itemId) &&
        Objects.equals(this.slotId, furniturePlacement.slotId) &&
        Objects.equals(this.position, furniturePlacement.position) &&
        Objects.equals(this.rotation, furniturePlacement.rotation) &&
        Objects.equals(this.customizationOptions, furniturePlacement.customizationOptions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, slotId, position, rotation, customizationOptions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FurniturePlacement {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    slotId: ").append(toIndentedString(slotId)).append("\n");
    sb.append("    position: ").append(toIndentedString(position)).append("\n");
    sb.append("    rotation: ").append(toIndentedString(rotation)).append("\n");
    sb.append("    customizationOptions: ").append(toIndentedString(customizationOptions)).append("\n");
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

