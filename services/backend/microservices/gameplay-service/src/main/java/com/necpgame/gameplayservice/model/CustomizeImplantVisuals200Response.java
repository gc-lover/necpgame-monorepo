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
 * CustomizeImplantVisuals200Response
 */

@JsonTypeName("customizeImplantVisuals_200_response")

public class CustomizeImplantVisuals200Response {

  private @Nullable Boolean success;

  private @Nullable String implantId;

  private @Nullable Object visualConfig;

  public CustomizeImplantVisuals200Response success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public CustomizeImplantVisuals200Response implantId(@Nullable String implantId) {
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

  public CustomizeImplantVisuals200Response visualConfig(@Nullable Object visualConfig) {
    this.visualConfig = visualConfig;
    return this;
  }

  /**
   * Get visualConfig
   * @return visualConfig
   */
  
  @Schema(name = "visual_config", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("visual_config")
  public @Nullable Object getVisualConfig() {
    return visualConfig;
  }

  public void setVisualConfig(@Nullable Object visualConfig) {
    this.visualConfig = visualConfig;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CustomizeImplantVisuals200Response customizeImplantVisuals200Response = (CustomizeImplantVisuals200Response) o;
    return Objects.equals(this.success, customizeImplantVisuals200Response.success) &&
        Objects.equals(this.implantId, customizeImplantVisuals200Response.implantId) &&
        Objects.equals(this.visualConfig, customizeImplantVisuals200Response.visualConfig);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, implantId, visualConfig);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CustomizeImplantVisuals200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    implantId: ").append(toIndentedString(implantId)).append("\n");
    sb.append("    visualConfig: ").append(toIndentedString(visualConfig)).append("\n");
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

