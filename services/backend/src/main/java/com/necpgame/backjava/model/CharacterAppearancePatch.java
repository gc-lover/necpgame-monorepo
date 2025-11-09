package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.CharacterAppearance;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CharacterAppearancePatch
 */


public class CharacterAppearancePatch {

  private @Nullable CharacterAppearance appearance;

  private Boolean loadoutPreview = false;

  public CharacterAppearancePatch appearance(@Nullable CharacterAppearance appearance) {
    this.appearance = appearance;
    return this;
  }

  /**
   * Get appearance
   * @return appearance
   */
  @Valid 
  @Schema(name = "appearance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("appearance")
  public @Nullable CharacterAppearance getAppearance() {
    return appearance;
  }

  public void setAppearance(@Nullable CharacterAppearance appearance) {
    this.appearance = appearance;
  }

  public CharacterAppearancePatch loadoutPreview(Boolean loadoutPreview) {
    this.loadoutPreview = loadoutPreview;
    return this;
  }

  /**
   * Get loadoutPreview
   * @return loadoutPreview
   */
  
  @Schema(name = "loadoutPreview", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loadoutPreview")
  public Boolean getLoadoutPreview() {
    return loadoutPreview;
  }

  public void setLoadoutPreview(Boolean loadoutPreview) {
    this.loadoutPreview = loadoutPreview;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterAppearancePatch characterAppearancePatch = (CharacterAppearancePatch) o;
    return Objects.equals(this.appearance, characterAppearancePatch.appearance) &&
        Objects.equals(this.loadoutPreview, characterAppearancePatch.loadoutPreview);
  }

  @Override
  public int hashCode() {
    return Objects.hash(appearance, loadoutPreview);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterAppearancePatch {\n");
    sb.append("    appearance: ").append(toIndentedString(appearance)).append("\n");
    sb.append("    loadoutPreview: ").append(toIndentedString(loadoutPreview)).append("\n");
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

