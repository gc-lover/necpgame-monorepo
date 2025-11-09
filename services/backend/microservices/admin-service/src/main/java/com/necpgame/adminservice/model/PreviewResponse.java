package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.PreviewResponseRendersInner;
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
 * PreviewResponse
 */


public class PreviewResponse {

  @Valid
  private List<@Valid PreviewResponseRendersInner> renders = new ArrayList<>();

  public PreviewResponse renders(List<@Valid PreviewResponseRendersInner> renders) {
    this.renders = renders;
    return this;
  }

  public PreviewResponse addRendersItem(PreviewResponseRendersInner rendersItem) {
    if (this.renders == null) {
      this.renders = new ArrayList<>();
    }
    this.renders.add(rendersItem);
    return this;
  }

  /**
   * Get renders
   * @return renders
   */
  @Valid 
  @Schema(name = "renders", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("renders")
  public List<@Valid PreviewResponseRendersInner> getRenders() {
    return renders;
  }

  public void setRenders(List<@Valid PreviewResponseRendersInner> renders) {
    this.renders = renders;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PreviewResponse previewResponse = (PreviewResponse) o;
    return Objects.equals(this.renders, previewResponse.renders);
  }

  @Override
  public int hashCode() {
    return Objects.hash(renders);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PreviewResponse {\n");
    sb.append("    renders: ").append(toIndentedString(renders)).append("\n");
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

