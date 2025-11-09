package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.Role;
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
 * MatchSearchRequestPartyContextPartiesInner
 */

@JsonTypeName("MatchSearchRequest_partyContext_parties_inner")

public class MatchSearchRequestPartyContextPartiesInner {

  private UUID partyId;

  private Integer size;

  private @Nullable UUID leaderId;

  @Valid
  private List<Role> preferredRoles = new ArrayList<>();

  public MatchSearchRequestPartyContextPartiesInner() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MatchSearchRequestPartyContextPartiesInner(UUID partyId, Integer size) {
    this.partyId = partyId;
    this.size = size;
  }

  public MatchSearchRequestPartyContextPartiesInner partyId(UUID partyId) {
    this.partyId = partyId;
    return this;
  }

  /**
   * Get partyId
   * @return partyId
   */
  @NotNull @Valid 
  @Schema(name = "partyId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("partyId")
  public UUID getPartyId() {
    return partyId;
  }

  public void setPartyId(UUID partyId) {
    this.partyId = partyId;
  }

  public MatchSearchRequestPartyContextPartiesInner size(Integer size) {
    this.size = size;
    return this;
  }

  /**
   * Get size
   * minimum: 1
   * maximum: 8
   * @return size
   */
  @NotNull @Min(value = 1) @Max(value = 8) 
  @Schema(name = "size", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("size")
  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public MatchSearchRequestPartyContextPartiesInner leaderId(@Nullable UUID leaderId) {
    this.leaderId = leaderId;
    return this;
  }

  /**
   * Get leaderId
   * @return leaderId
   */
  @Valid 
  @Schema(name = "leaderId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("leaderId")
  public @Nullable UUID getLeaderId() {
    return leaderId;
  }

  public void setLeaderId(@Nullable UUID leaderId) {
    this.leaderId = leaderId;
  }

  public MatchSearchRequestPartyContextPartiesInner preferredRoles(List<Role> preferredRoles) {
    this.preferredRoles = preferredRoles;
    return this;
  }

  public MatchSearchRequestPartyContextPartiesInner addPreferredRolesItem(Role preferredRolesItem) {
    if (this.preferredRoles == null) {
      this.preferredRoles = new ArrayList<>();
    }
    this.preferredRoles.add(preferredRolesItem);
    return this;
  }

  /**
   * Get preferredRoles
   * @return preferredRoles
   */
  @Valid 
  @Schema(name = "preferredRoles", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("preferredRoles")
  public List<Role> getPreferredRoles() {
    return preferredRoles;
  }

  public void setPreferredRoles(List<Role> preferredRoles) {
    this.preferredRoles = preferredRoles;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MatchSearchRequestPartyContextPartiesInner matchSearchRequestPartyContextPartiesInner = (MatchSearchRequestPartyContextPartiesInner) o;
    return Objects.equals(this.partyId, matchSearchRequestPartyContextPartiesInner.partyId) &&
        Objects.equals(this.size, matchSearchRequestPartyContextPartiesInner.size) &&
        Objects.equals(this.leaderId, matchSearchRequestPartyContextPartiesInner.leaderId) &&
        Objects.equals(this.preferredRoles, matchSearchRequestPartyContextPartiesInner.preferredRoles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(partyId, size, leaderId, preferredRoles);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MatchSearchRequestPartyContextPartiesInner {\n");
    sb.append("    partyId: ").append(toIndentedString(partyId)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
    sb.append("    leaderId: ").append(toIndentedString(leaderId)).append("\n");
    sb.append("    preferredRoles: ").append(toIndentedString(preferredRoles)).append("\n");
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

