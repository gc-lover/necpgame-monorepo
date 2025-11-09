package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CyberpsychosisStatusSocialImpact
 */

@JsonTypeName("CyberpsychosisStatus_social_impact")

public class CyberpsychosisStatusSocialImpact {

  private @Nullable BigDecimal reputationPenalty;

  private @Nullable Boolean npcAccessRestricted;

  public CyberpsychosisStatusSocialImpact reputationPenalty(@Nullable BigDecimal reputationPenalty) {
    this.reputationPenalty = reputationPenalty;
    return this;
  }

  /**
   * Get reputationPenalty
   * @return reputationPenalty
   */
  @Valid 
  @Schema(name = "reputation_penalty", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputation_penalty")
  public @Nullable BigDecimal getReputationPenalty() {
    return reputationPenalty;
  }

  public void setReputationPenalty(@Nullable BigDecimal reputationPenalty) {
    this.reputationPenalty = reputationPenalty;
  }

  public CyberpsychosisStatusSocialImpact npcAccessRestricted(@Nullable Boolean npcAccessRestricted) {
    this.npcAccessRestricted = npcAccessRestricted;
    return this;
  }

  /**
   * Get npcAccessRestricted
   * @return npcAccessRestricted
   */
  
  @Schema(name = "npc_access_restricted", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("npc_access_restricted")
  public @Nullable Boolean getNpcAccessRestricted() {
    return npcAccessRestricted;
  }

  public void setNpcAccessRestricted(@Nullable Boolean npcAccessRestricted) {
    this.npcAccessRestricted = npcAccessRestricted;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CyberpsychosisStatusSocialImpact cyberpsychosisStatusSocialImpact = (CyberpsychosisStatusSocialImpact) o;
    return Objects.equals(this.reputationPenalty, cyberpsychosisStatusSocialImpact.reputationPenalty) &&
        Objects.equals(this.npcAccessRestricted, cyberpsychosisStatusSocialImpact.npcAccessRestricted);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reputationPenalty, npcAccessRestricted);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CyberpsychosisStatusSocialImpact {\n");
    sb.append("    reputationPenalty: ").append(toIndentedString(reputationPenalty)).append("\n");
    sb.append("    npcAccessRestricted: ").append(toIndentedString(npcAccessRestricted)).append("\n");
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

