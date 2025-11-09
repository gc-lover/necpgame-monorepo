package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.VisibleImplant;
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
 * GetImplantVisualPreview200Response
 */

@JsonTypeName("getImplantVisualPreview_200_response")

public class GetImplantVisualPreview200Response {

  private @Nullable String characterId;

  @Valid
  private List<@Valid VisibleImplant> visibleImplants = new ArrayList<>();

  private @Nullable Integer hiddenImplantsCount;

  public GetImplantVisualPreview200Response characterId(@Nullable String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable String characterId) {
    this.characterId = characterId;
  }

  public GetImplantVisualPreview200Response visibleImplants(List<@Valid VisibleImplant> visibleImplants) {
    this.visibleImplants = visibleImplants;
    return this;
  }

  public GetImplantVisualPreview200Response addVisibleImplantsItem(VisibleImplant visibleImplantsItem) {
    if (this.visibleImplants == null) {
      this.visibleImplants = new ArrayList<>();
    }
    this.visibleImplants.add(visibleImplantsItem);
    return this;
  }

  /**
   * Get visibleImplants
   * @return visibleImplants
   */
  @Valid 
  @Schema(name = "visible_implants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("visible_implants")
  public List<@Valid VisibleImplant> getVisibleImplants() {
    return visibleImplants;
  }

  public void setVisibleImplants(List<@Valid VisibleImplant> visibleImplants) {
    this.visibleImplants = visibleImplants;
  }

  public GetImplantVisualPreview200Response hiddenImplantsCount(@Nullable Integer hiddenImplantsCount) {
    this.hiddenImplantsCount = hiddenImplantsCount;
    return this;
  }

  /**
   * Количество скрытых имплантов
   * @return hiddenImplantsCount
   */
  
  @Schema(name = "hidden_implants_count", description = "Количество скрытых имплантов", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hidden_implants_count")
  public @Nullable Integer getHiddenImplantsCount() {
    return hiddenImplantsCount;
  }

  public void setHiddenImplantsCount(@Nullable Integer hiddenImplantsCount) {
    this.hiddenImplantsCount = hiddenImplantsCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetImplantVisualPreview200Response getImplantVisualPreview200Response = (GetImplantVisualPreview200Response) o;
    return Objects.equals(this.characterId, getImplantVisualPreview200Response.characterId) &&
        Objects.equals(this.visibleImplants, getImplantVisualPreview200Response.visibleImplants) &&
        Objects.equals(this.hiddenImplantsCount, getImplantVisualPreview200Response.hiddenImplantsCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, visibleImplants, hiddenImplantsCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetImplantVisualPreview200Response {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    visibleImplants: ").append(toIndentedString(visibleImplants)).append("\n");
    sb.append("    hiddenImplantsCount: ").append(toIndentedString(hiddenImplantsCount)).append("\n");
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

