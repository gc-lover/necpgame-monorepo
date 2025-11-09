package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.ChannelErrorAllOfError;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ChannelError
 */


public class ChannelError {

  private ChannelErrorAllOfError error;

  public ChannelError() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ChannelError(ChannelErrorAllOfError error) {
    this.error = error;
  }

  public ChannelError error(ChannelErrorAllOfError error) {
    this.error = error;
    return this;
  }

  /**
   * Get error
   * @return error
   */
  @NotNull @Valid 
  @Schema(name = "error", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("error")
  public ChannelErrorAllOfError getError() {
    return error;
  }

  public void setError(ChannelErrorAllOfError error) {
    this.error = error;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChannelError channelError = (ChannelError) o;
    return Objects.equals(this.error, channelError.error);
  }

  @Override
  public int hashCode() {
    return Objects.hash(error);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChannelError {\n");
    sb.append("    error: ").append(toIndentedString(error)).append("\n");
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

