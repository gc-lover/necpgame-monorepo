package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CompleteContractRequest
 */

@JsonTypeName("completeContract_request")

public class CompleteContractRequest {

  private @Nullable UUID characterId;

  @Valid
  private Map<String, Object> completionProof = new HashMap<>();

  public CompleteContractRequest characterId(@Nullable UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @Valid 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable UUID characterId) {
    this.characterId = characterId;
  }

  public CompleteContractRequest completionProof(Map<String, Object> completionProof) {
    this.completionProof = completionProof;
    return this;
  }

  public CompleteContractRequest putCompletionProofItem(String key, Object completionProofItem) {
    if (this.completionProof == null) {
      this.completionProof = new HashMap<>();
    }
    this.completionProof.put(key, completionProofItem);
    return this;
  }

  /**
   * Доказательство выполнения
   * @return completionProof
   */
  
  @Schema(name = "completion_proof", description = "Доказательство выполнения", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("completion_proof")
  public Map<String, Object> getCompletionProof() {
    return completionProof;
  }

  public void setCompletionProof(Map<String, Object> completionProof) {
    this.completionProof = completionProof;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompleteContractRequest completeContractRequest = (CompleteContractRequest) o;
    return Objects.equals(this.characterId, completeContractRequest.characterId) &&
        Objects.equals(this.completionProof, completeContractRequest.completionProof);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, completionProof);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompleteContractRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    completionProof: ").append(toIndentedString(completionProof)).append("\n");
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

