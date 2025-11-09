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
 * CheckImplantCompatibilityRequest
 */

@JsonTypeName("checkImplantCompatibility_request")

public class CheckImplantCompatibilityRequest {

  private String characterId;

  @Valid
  private List<String> implantIds = new ArrayList<>();

  public CheckImplantCompatibilityRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CheckImplantCompatibilityRequest(String characterId, List<String> implantIds) {
    this.characterId = characterId;
    this.implantIds = implantIds;
  }

  public CheckImplantCompatibilityRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public CheckImplantCompatibilityRequest implantIds(List<String> implantIds) {
    this.implantIds = implantIds;
    return this;
  }

  public CheckImplantCompatibilityRequest addImplantIdsItem(String implantIdsItem) {
    if (this.implantIds == null) {
      this.implantIds = new ArrayList<>();
    }
    this.implantIds.add(implantIdsItem);
    return this;
  }

  /**
   * Список ID имплантов для проверки
   * @return implantIds
   */
  @NotNull 
  @Schema(name = "implant_ids", description = "Список ID имплантов для проверки", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("implant_ids")
  public List<String> getImplantIds() {
    return implantIds;
  }

  public void setImplantIds(List<String> implantIds) {
    this.implantIds = implantIds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CheckImplantCompatibilityRequest checkImplantCompatibilityRequest = (CheckImplantCompatibilityRequest) o;
    return Objects.equals(this.characterId, checkImplantCompatibilityRequest.characterId) &&
        Objects.equals(this.implantIds, checkImplantCompatibilityRequest.implantIds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, implantIds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CheckImplantCompatibilityRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    implantIds: ").append(toIndentedString(implantIds)).append("\n");
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

