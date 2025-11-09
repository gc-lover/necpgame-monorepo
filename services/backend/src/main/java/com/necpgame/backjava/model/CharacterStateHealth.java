package com.necpgame.backjava.model;

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
 * CharacterStateHealth
 */

@JsonTypeName("CharacterState_health")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class CharacterStateHealth {

  private @Nullable Integer current;

  private @Nullable Integer max;

  public CharacterStateHealth current(@Nullable Integer current) {
    this.current = current;
    return this;
  }

  /**
   * Get current
   * @return current
   */
  
  @Schema(name = "current", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current")
  public @Nullable Integer getCurrent() {
    return current;
  }

  public void setCurrent(@Nullable Integer current) {
    this.current = current;
  }

  public CharacterStateHealth max(@Nullable Integer max) {
    this.max = max;
    return this;
  }

  /**
   * Get max
   * @return max
   */
  
  @Schema(name = "max", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max")
  public @Nullable Integer getMax() {
    return max;
  }

  public void setMax(@Nullable Integer max) {
    this.max = max;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterStateHealth characterStateHealth = (CharacterStateHealth) o;
    return Objects.equals(this.current, characterStateHealth.current) &&
        Objects.equals(this.max, characterStateHealth.max);
  }

  @Override
  public int hashCode() {
    return Objects.hash(current, max);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterStateHealth {\n");
    sb.append("    current: ").append(toIndentedString(current)).append("\n");
    sb.append("    max: ").append(toIndentedString(max)).append("\n");
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

