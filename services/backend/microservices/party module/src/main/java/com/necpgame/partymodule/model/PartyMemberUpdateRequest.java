package com.necpgame.partymodule.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PartyMemberUpdateRequest
 */


public class PartyMemberUpdateRequest {

  private @Nullable String role;

  private @Nullable Boolean promoteToLeader;

  private @Nullable String focusTarget;

  public PartyMemberUpdateRequest role(@Nullable String role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  
  @Schema(name = "role", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("role")
  public @Nullable String getRole() {
    return role;
  }

  public void setRole(@Nullable String role) {
    this.role = role;
  }

  public PartyMemberUpdateRequest promoteToLeader(@Nullable Boolean promoteToLeader) {
    this.promoteToLeader = promoteToLeader;
    return this;
  }

  /**
   * Get promoteToLeader
   * @return promoteToLeader
   */
  
  @Schema(name = "promoteToLeader", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("promoteToLeader")
  public @Nullable Boolean getPromoteToLeader() {
    return promoteToLeader;
  }

  public void setPromoteToLeader(@Nullable Boolean promoteToLeader) {
    this.promoteToLeader = promoteToLeader;
  }

  public PartyMemberUpdateRequest focusTarget(@Nullable String focusTarget) {
    this.focusTarget = focusTarget;
    return this;
  }

  /**
   * Get focusTarget
   * @return focusTarget
   */
  
  @Schema(name = "focusTarget", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("focusTarget")
  public @Nullable String getFocusTarget() {
    return focusTarget;
  }

  public void setFocusTarget(@Nullable String focusTarget) {
    this.focusTarget = focusTarget;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PartyMemberUpdateRequest partyMemberUpdateRequest = (PartyMemberUpdateRequest) o;
    return Objects.equals(this.role, partyMemberUpdateRequest.role) &&
        Objects.equals(this.promoteToLeader, partyMemberUpdateRequest.promoteToLeader) &&
        Objects.equals(this.focusTarget, partyMemberUpdateRequest.focusTarget);
  }

  @Override
  public int hashCode() {
    return Objects.hash(role, promoteToLeader, focusTarget);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PartyMemberUpdateRequest {\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    promoteToLeader: ").append(toIndentedString(promoteToLeader)).append("\n");
    sb.append("    focusTarget: ").append(toIndentedString(focusTarget)).append("\n");
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

