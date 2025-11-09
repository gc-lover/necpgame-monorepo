package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AppearanceOptionsCustomizationSlidersInner
 */

@JsonTypeName("AppearanceOptions_customization_sliders_inner")

public class AppearanceOptionsCustomizationSlidersInner {

  private @Nullable String sliderId;

  private @Nullable String name;

  private @Nullable BigDecimal min;

  private @Nullable BigDecimal max;

  private @Nullable BigDecimal _default;

  public AppearanceOptionsCustomizationSlidersInner sliderId(@Nullable String sliderId) {
    this.sliderId = sliderId;
    return this;
  }

  /**
   * Get sliderId
   * @return sliderId
   */
  
  @Schema(name = "slider_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("slider_id")
  public @Nullable String getSliderId() {
    return sliderId;
  }

  public void setSliderId(@Nullable String sliderId) {
    this.sliderId = sliderId;
  }

  public AppearanceOptionsCustomizationSlidersInner name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public AppearanceOptionsCustomizationSlidersInner min(@Nullable BigDecimal min) {
    this.min = min;
    return this;
  }

  /**
   * Get min
   * @return min
   */
  @Valid 
  @Schema(name = "min", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("min")
  public @Nullable BigDecimal getMin() {
    return min;
  }

  public void setMin(@Nullable BigDecimal min) {
    this.min = min;
  }

  public AppearanceOptionsCustomizationSlidersInner max(@Nullable BigDecimal max) {
    this.max = max;
    return this;
  }

  /**
   * Get max
   * @return max
   */
  @Valid 
  @Schema(name = "max", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max")
  public @Nullable BigDecimal getMax() {
    return max;
  }

  public void setMax(@Nullable BigDecimal max) {
    this.max = max;
  }

  public AppearanceOptionsCustomizationSlidersInner _default(@Nullable BigDecimal _default) {
    this._default = _default;
    return this;
  }

  /**
   * Get _default
   * @return _default
   */
  @Valid 
  @Schema(name = "default", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("default")
  public @Nullable BigDecimal getDefault() {
    return _default;
  }

  public void setDefault(@Nullable BigDecimal _default) {
    this._default = _default;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AppearanceOptionsCustomizationSlidersInner appearanceOptionsCustomizationSlidersInner = (AppearanceOptionsCustomizationSlidersInner) o;
    return Objects.equals(this.sliderId, appearanceOptionsCustomizationSlidersInner.sliderId) &&
        Objects.equals(this.name, appearanceOptionsCustomizationSlidersInner.name) &&
        Objects.equals(this.min, appearanceOptionsCustomizationSlidersInner.min) &&
        Objects.equals(this.max, appearanceOptionsCustomizationSlidersInner.max) &&
        Objects.equals(this._default, appearanceOptionsCustomizationSlidersInner._default);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sliderId, name, min, max, _default);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AppearanceOptionsCustomizationSlidersInner {\n");
    sb.append("    sliderId: ").append(toIndentedString(sliderId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    min: ").append(toIndentedString(min)).append("\n");
    sb.append("    max: ").append(toIndentedString(max)).append("\n");
    sb.append("    _default: ").append(toIndentedString(_default)).append("\n");
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

