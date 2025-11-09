package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.GetAIDifficulty200ResponseDifficultyLevelsInner;
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
 * GetAIDifficulty200Response
 */

@JsonTypeName("getAIDifficulty_200_response")

public class GetAIDifficulty200Response {

  @Valid
  private List<@Valid GetAIDifficulty200ResponseDifficultyLevelsInner> difficultyLevels = new ArrayList<>();

  public GetAIDifficulty200Response difficultyLevels(List<@Valid GetAIDifficulty200ResponseDifficultyLevelsInner> difficultyLevels) {
    this.difficultyLevels = difficultyLevels;
    return this;
  }

  public GetAIDifficulty200Response addDifficultyLevelsItem(GetAIDifficulty200ResponseDifficultyLevelsInner difficultyLevelsItem) {
    if (this.difficultyLevels == null) {
      this.difficultyLevels = new ArrayList<>();
    }
    this.difficultyLevels.add(difficultyLevelsItem);
    return this;
  }

  /**
   * Get difficultyLevels
   * @return difficultyLevels
   */
  @Valid 
  @Schema(name = "difficulty_levels", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("difficulty_levels")
  public List<@Valid GetAIDifficulty200ResponseDifficultyLevelsInner> getDifficultyLevels() {
    return difficultyLevels;
  }

  public void setDifficultyLevels(List<@Valid GetAIDifficulty200ResponseDifficultyLevelsInner> difficultyLevels) {
    this.difficultyLevels = difficultyLevels;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetAIDifficulty200Response getAIDifficulty200Response = (GetAIDifficulty200Response) o;
    return Objects.equals(this.difficultyLevels, getAIDifficulty200Response.difficultyLevels);
  }

  @Override
  public int hashCode() {
    return Objects.hash(difficultyLevels);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetAIDifficulty200Response {\n");
    sb.append("    difficultyLevels: ").append(toIndentedString(difficultyLevels)).append("\n");
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

