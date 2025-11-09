package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.narrativeservice.model.PlayerChoice;
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
 * GetPlayerChoices200Response
 */

@JsonTypeName("getPlayerChoices_200_response")

public class GetPlayerChoices200Response {

  private @Nullable String characterId;

  private @Nullable Integer totalChoices;

  @Valid
  private List<@Valid PlayerChoice> choices = new ArrayList<>();

  public GetPlayerChoices200Response characterId(@Nullable String characterId) {
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

  public GetPlayerChoices200Response totalChoices(@Nullable Integer totalChoices) {
    this.totalChoices = totalChoices;
    return this;
  }

  /**
   * Get totalChoices
   * @return totalChoices
   */
  
  @Schema(name = "total_choices", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_choices")
  public @Nullable Integer getTotalChoices() {
    return totalChoices;
  }

  public void setTotalChoices(@Nullable Integer totalChoices) {
    this.totalChoices = totalChoices;
  }

  public GetPlayerChoices200Response choices(List<@Valid PlayerChoice> choices) {
    this.choices = choices;
    return this;
  }

  public GetPlayerChoices200Response addChoicesItem(PlayerChoice choicesItem) {
    if (this.choices == null) {
      this.choices = new ArrayList<>();
    }
    this.choices.add(choicesItem);
    return this;
  }

  /**
   * Get choices
   * @return choices
   */
  @Valid 
  @Schema(name = "choices", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("choices")
  public List<@Valid PlayerChoice> getChoices() {
    return choices;
  }

  public void setChoices(List<@Valid PlayerChoice> choices) {
    this.choices = choices;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetPlayerChoices200Response getPlayerChoices200Response = (GetPlayerChoices200Response) o;
    return Objects.equals(this.characterId, getPlayerChoices200Response.characterId) &&
        Objects.equals(this.totalChoices, getPlayerChoices200Response.totalChoices) &&
        Objects.equals(this.choices, getPlayerChoices200Response.choices);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, totalChoices, choices);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetPlayerChoices200Response {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    totalChoices: ").append(toIndentedString(totalChoices)).append("\n");
    sb.append("    choices: ").append(toIndentedString(choices)).append("\n");
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

