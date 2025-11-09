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
 * TextVersionStateNearbyNpcsInner
 */

@JsonTypeName("TextVersionState_nearby_npcs_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class TextVersionStateNearbyNpcsInner {

  private @Nullable String name;

  private @Nullable Boolean canInteract;

  public TextVersionStateNearbyNpcsInner name(@Nullable String name) {
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

  public TextVersionStateNearbyNpcsInner canInteract(@Nullable Boolean canInteract) {
    this.canInteract = canInteract;
    return this;
  }

  /**
   * Get canInteract
   * @return canInteract
   */
  
  @Schema(name = "can_interact", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("can_interact")
  public @Nullable Boolean getCanInteract() {
    return canInteract;
  }

  public void setCanInteract(@Nullable Boolean canInteract) {
    this.canInteract = canInteract;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TextVersionStateNearbyNpcsInner textVersionStateNearbyNpcsInner = (TextVersionStateNearbyNpcsInner) o;
    return Objects.equals(this.name, textVersionStateNearbyNpcsInner.name) &&
        Objects.equals(this.canInteract, textVersionStateNearbyNpcsInner.canInteract);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, canInteract);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TextVersionStateNearbyNpcsInner {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    canInteract: ").append(toIndentedString(canInteract)).append("\n");
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

