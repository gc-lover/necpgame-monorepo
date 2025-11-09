package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.worldservice.model.EventEffect;
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
 * GetCharacterAffectedEvents200Response
 */

@JsonTypeName("getCharacterAffectedEvents_200_response")

public class GetCharacterAffectedEvents200Response {

  @Valid
  private List<@Valid EventEffect> activeEffects = new ArrayList<>();

  public GetCharacterAffectedEvents200Response activeEffects(List<@Valid EventEffect> activeEffects) {
    this.activeEffects = activeEffects;
    return this;
  }

  public GetCharacterAffectedEvents200Response addActiveEffectsItem(EventEffect activeEffectsItem) {
    if (this.activeEffects == null) {
      this.activeEffects = new ArrayList<>();
    }
    this.activeEffects.add(activeEffectsItem);
    return this;
  }

  /**
   * Get activeEffects
   * @return activeEffects
   */
  @Valid 
  @Schema(name = "active_effects", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active_effects")
  public List<@Valid EventEffect> getActiveEffects() {
    return activeEffects;
  }

  public void setActiveEffects(List<@Valid EventEffect> activeEffects) {
    this.activeEffects = activeEffects;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetCharacterAffectedEvents200Response getCharacterAffectedEvents200Response = (GetCharacterAffectedEvents200Response) o;
    return Objects.equals(this.activeEffects, getCharacterAffectedEvents200Response.activeEffects);
  }

  @Override
  public int hashCode() {
    return Objects.hash(activeEffects);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetCharacterAffectedEvents200Response {\n");
    sb.append("    activeEffects: ").append(toIndentedString(activeEffects)).append("\n");
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

