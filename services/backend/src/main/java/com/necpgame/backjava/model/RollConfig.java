package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * RollConfig
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class RollConfig {

  private Integer timeoutSeconds = 60;

  private Boolean autoPassAfk = true;

  @Valid
  private List<String> allowNeedRoles = new ArrayList<>();

  public RollConfig timeoutSeconds(Integer timeoutSeconds) {
    this.timeoutSeconds = timeoutSeconds;
    return this;
  }

  /**
   * Get timeoutSeconds
   * @return timeoutSeconds
   */
  
  @Schema(name = "timeoutSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timeoutSeconds")
  public Integer getTimeoutSeconds() {
    return timeoutSeconds;
  }

  public void setTimeoutSeconds(Integer timeoutSeconds) {
    this.timeoutSeconds = timeoutSeconds;
  }

  public RollConfig autoPassAfk(Boolean autoPassAfk) {
    this.autoPassAfk = autoPassAfk;
    return this;
  }

  /**
   * Get autoPassAfk
   * @return autoPassAfk
   */
  
  @Schema(name = "autoPassAfk", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("autoPassAfk")
  public Boolean getAutoPassAfk() {
    return autoPassAfk;
  }

  public void setAutoPassAfk(Boolean autoPassAfk) {
    this.autoPassAfk = autoPassAfk;
  }

  public RollConfig allowNeedRoles(List<String> allowNeedRoles) {
    this.allowNeedRoles = allowNeedRoles;
    return this;
  }

  public RollConfig addAllowNeedRolesItem(String allowNeedRolesItem) {
    if (this.allowNeedRoles == null) {
      this.allowNeedRoles = new ArrayList<>();
    }
    this.allowNeedRoles.add(allowNeedRolesItem);
    return this;
  }

  /**
   * Get allowNeedRoles
   * @return allowNeedRoles
   */
  
  @Schema(name = "allowNeedRoles", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("allowNeedRoles")
  public List<String> getAllowNeedRoles() {
    return allowNeedRoles;
  }

  public void setAllowNeedRoles(List<String> allowNeedRoles) {
    this.allowNeedRoles = allowNeedRoles;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RollConfig rollConfig = (RollConfig) o;
    return Objects.equals(this.timeoutSeconds, rollConfig.timeoutSeconds) &&
        Objects.equals(this.autoPassAfk, rollConfig.autoPassAfk) &&
        Objects.equals(this.allowNeedRoles, rollConfig.allowNeedRoles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timeoutSeconds, autoPassAfk, allowNeedRoles);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RollConfig {\n");
    sb.append("    timeoutSeconds: ").append(toIndentedString(timeoutSeconds)).append("\n");
    sb.append("    autoPassAfk: ").append(toIndentedString(autoPassAfk)).append("\n");
    sb.append("    allowNeedRoles: ").append(toIndentedString(allowNeedRoles)).append("\n");
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

