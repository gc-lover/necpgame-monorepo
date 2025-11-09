package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.LayoutPreset;
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
 * LayoutPresetResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class LayoutPresetResponse {

  @Valid
  private List<LayoutPreset> layouts = new ArrayList<>();

  public LayoutPresetResponse layouts(List<LayoutPreset> layouts) {
    this.layouts = layouts;
    return this;
  }

  public LayoutPresetResponse addLayoutsItem(LayoutPreset layoutsItem) {
    if (this.layouts == null) {
      this.layouts = new ArrayList<>();
    }
    this.layouts.add(layoutsItem);
    return this;
  }

  /**
   * Get layouts
   * @return layouts
   */
  @Valid 
  @Schema(name = "layouts", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("layouts")
  public List<LayoutPreset> getLayouts() {
    return layouts;
  }

  public void setLayouts(List<LayoutPreset> layouts) {
    this.layouts = layouts;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LayoutPresetResponse layoutPresetResponse = (LayoutPresetResponse) o;
    return Objects.equals(this.layouts, layoutPresetResponse.layouts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(layouts);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LayoutPresetResponse {\n");
    sb.append("    layouts: ").append(toIndentedString(layouts)).append("\n");
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

