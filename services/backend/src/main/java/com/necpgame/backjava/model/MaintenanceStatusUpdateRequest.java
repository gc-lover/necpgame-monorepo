package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * MaintenanceStatusUpdateRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class MaintenanceStatusUpdateRequest {

  private String status;

  private @Nullable String message;

  private @Nullable Boolean _public;

  public MaintenanceStatusUpdateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MaintenanceStatusUpdateRequest(String status) {
    this.status = status;
  }

  public MaintenanceStatusUpdateRequest status(String status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public MaintenanceStatusUpdateRequest message(@Nullable String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   */
  
  @Schema(name = "message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message")
  public @Nullable String getMessage() {
    return message;
  }

  public void setMessage(@Nullable String message) {
    this.message = message;
  }

  public MaintenanceStatusUpdateRequest _public(@Nullable Boolean _public) {
    this._public = _public;
    return this;
  }

  /**
   * Get _public
   * @return _public
   */
  
  @Schema(name = "public", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("public")
  public @Nullable Boolean getPublic() {
    return _public;
  }

  public void setPublic(@Nullable Boolean _public) {
    this._public = _public;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MaintenanceStatusUpdateRequest maintenanceStatusUpdateRequest = (MaintenanceStatusUpdateRequest) o;
    return Objects.equals(this.status, maintenanceStatusUpdateRequest.status) &&
        Objects.equals(this.message, maintenanceStatusUpdateRequest.message) &&
        Objects.equals(this._public, maintenanceStatusUpdateRequest._public);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, message, _public);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaintenanceStatusUpdateRequest {\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    _public: ").append(toIndentedString(_public)).append("\n");
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

