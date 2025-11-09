package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * VisibleImplant
 */


public class VisibleImplant {

  private @Nullable String implantId;

  private @Nullable String implantName;

  /**
   * Gets or Sets implantType
   */
  public enum ImplantTypeEnum {
    EXTERNAL("external"),
    
    INTERNAL("internal");

    private final String value;

    ImplantTypeEnum(String value) {
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
    public static ImplantTypeEnum fromValue(String value) {
      for (ImplantTypeEnum b : ImplantTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ImplantTypeEnum implantType;

  private @Nullable String color;

  private @Nullable String style;

  private @Nullable Object lightEffects;

  private @Nullable String location;

  public VisibleImplant implantId(@Nullable String implantId) {
    this.implantId = implantId;
    return this;
  }

  /**
   * Get implantId
   * @return implantId
   */
  
  @Schema(name = "implant_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("implant_id")
  public @Nullable String getImplantId() {
    return implantId;
  }

  public void setImplantId(@Nullable String implantId) {
    this.implantId = implantId;
  }

  public VisibleImplant implantName(@Nullable String implantName) {
    this.implantName = implantName;
    return this;
  }

  /**
   * Get implantName
   * @return implantName
   */
  
  @Schema(name = "implant_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("implant_name")
  public @Nullable String getImplantName() {
    return implantName;
  }

  public void setImplantName(@Nullable String implantName) {
    this.implantName = implantName;
  }

  public VisibleImplant implantType(@Nullable ImplantTypeEnum implantType) {
    this.implantType = implantType;
    return this;
  }

  /**
   * Get implantType
   * @return implantType
   */
  
  @Schema(name = "implant_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("implant_type")
  public @Nullable ImplantTypeEnum getImplantType() {
    return implantType;
  }

  public void setImplantType(@Nullable ImplantTypeEnum implantType) {
    this.implantType = implantType;
  }

  public VisibleImplant color(@Nullable String color) {
    this.color = color;
    return this;
  }

  /**
   * Get color
   * @return color
   */
  
  @Schema(name = "color", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("color")
  public @Nullable String getColor() {
    return color;
  }

  public void setColor(@Nullable String color) {
    this.color = color;
  }

  public VisibleImplant style(@Nullable String style) {
    this.style = style;
    return this;
  }

  /**
   * Get style
   * @return style
   */
  
  @Schema(name = "style", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("style")
  public @Nullable String getStyle() {
    return style;
  }

  public void setStyle(@Nullable String style) {
    this.style = style;
  }

  public VisibleImplant lightEffects(@Nullable Object lightEffects) {
    this.lightEffects = lightEffects;
    return this;
  }

  /**
   * Get lightEffects
   * @return lightEffects
   */
  
  @Schema(name = "light_effects", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("light_effects")
  public @Nullable Object getLightEffects() {
    return lightEffects;
  }

  public void setLightEffects(@Nullable Object lightEffects) {
    this.lightEffects = lightEffects;
  }

  public VisibleImplant location(@Nullable String location) {
    this.location = location;
    return this;
  }

  /**
   * Расположение на теле
   * @return location
   */
  
  @Schema(name = "location", description = "Расположение на теле", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location")
  public @Nullable String getLocation() {
    return location;
  }

  public void setLocation(@Nullable String location) {
    this.location = location;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VisibleImplant visibleImplant = (VisibleImplant) o;
    return Objects.equals(this.implantId, visibleImplant.implantId) &&
        Objects.equals(this.implantName, visibleImplant.implantName) &&
        Objects.equals(this.implantType, visibleImplant.implantType) &&
        Objects.equals(this.color, visibleImplant.color) &&
        Objects.equals(this.style, visibleImplant.style) &&
        Objects.equals(this.lightEffects, visibleImplant.lightEffects) &&
        Objects.equals(this.location, visibleImplant.location);
  }

  @Override
  public int hashCode() {
    return Objects.hash(implantId, implantName, implantType, color, style, lightEffects, location);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VisibleImplant {\n");
    sb.append("    implantId: ").append(toIndentedString(implantId)).append("\n");
    sb.append("    implantName: ").append(toIndentedString(implantName)).append("\n");
    sb.append("    implantType: ").append(toIndentedString(implantType)).append("\n");
    sb.append("    color: ").append(toIndentedString(color)).append("\n");
    sb.append("    style: ").append(toIndentedString(style)).append("\n");
    sb.append("    lightEffects: ").append(toIndentedString(lightEffects)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
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

