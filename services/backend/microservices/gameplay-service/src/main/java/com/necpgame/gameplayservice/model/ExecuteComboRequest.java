package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * ExecuteComboRequest
 */

@JsonTypeName("executeCombo_request")

public class ExecuteComboRequest {

  private @Nullable String characterId;

  private @Nullable String comboId;

  @Valid
  private List<String> targets = new ArrayList<>();

  public ExecuteComboRequest characterId(@Nullable String characterId) {
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

  public ExecuteComboRequest comboId(@Nullable String comboId) {
    this.comboId = comboId;
    return this;
  }

  /**
   * Get comboId
   * @return comboId
   */
  
  @Schema(name = "combo_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("combo_id")
  public @Nullable String getComboId() {
    return comboId;
  }

  public void setComboId(@Nullable String comboId) {
    this.comboId = comboId;
  }

  public ExecuteComboRequest targets(List<String> targets) {
    this.targets = targets;
    return this;
  }

  public ExecuteComboRequest addTargetsItem(String targetsItem) {
    if (this.targets == null) {
      this.targets = new ArrayList<>();
    }
    this.targets.add(targetsItem);
    return this;
  }

  /**
   * Get targets
   * @return targets
   */
  
  @Schema(name = "targets", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("targets")
  public List<String> getTargets() {
    return targets;
  }

  public void setTargets(List<String> targets) {
    this.targets = targets;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExecuteComboRequest executeComboRequest = (ExecuteComboRequest) o;
    return Objects.equals(this.characterId, executeComboRequest.characterId) &&
        Objects.equals(this.comboId, executeComboRequest.comboId) &&
        Objects.equals(this.targets, executeComboRequest.targets);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, comboId, targets);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExecuteComboRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    comboId: ").append(toIndentedString(comboId)).append("\n");
    sb.append("    targets: ").append(toIndentedString(targets)).append("\n");
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

