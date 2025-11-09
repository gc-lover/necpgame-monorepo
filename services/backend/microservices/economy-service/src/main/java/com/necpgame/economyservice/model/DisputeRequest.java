package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.economyservice.model.DisputeRequestEvidenceInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * DisputeRequest
 */


public class DisputeRequest {

  private UUID characterId;

  private String reason;

  @Valid
  private List<@Valid DisputeRequestEvidenceInner> evidence = new ArrayList<>();

  public DisputeRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DisputeRequest(UUID characterId, String reason) {
    this.characterId = characterId;
    this.reason = reason;
  }

  public DisputeRequest characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
    this.characterId = characterId;
  }

  public DisputeRequest reason(String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  @NotNull 
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reason")
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public DisputeRequest evidence(List<@Valid DisputeRequestEvidenceInner> evidence) {
    this.evidence = evidence;
    return this;
  }

  public DisputeRequest addEvidenceItem(DisputeRequestEvidenceInner evidenceItem) {
    if (this.evidence == null) {
      this.evidence = new ArrayList<>();
    }
    this.evidence.add(evidenceItem);
    return this;
  }

  /**
   * Get evidence
   * @return evidence
   */
  @Valid 
  @Schema(name = "evidence", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("evidence")
  public List<@Valid DisputeRequestEvidenceInner> getEvidence() {
    return evidence;
  }

  public void setEvidence(List<@Valid DisputeRequestEvidenceInner> evidence) {
    this.evidence = evidence;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DisputeRequest disputeRequest = (DisputeRequest) o;
    return Objects.equals(this.characterId, disputeRequest.characterId) &&
        Objects.equals(this.reason, disputeRequest.reason) &&
        Objects.equals(this.evidence, disputeRequest.evidence);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, reason, evidence);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DisputeRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    evidence: ").append(toIndentedString(evidence)).append("\n");
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

