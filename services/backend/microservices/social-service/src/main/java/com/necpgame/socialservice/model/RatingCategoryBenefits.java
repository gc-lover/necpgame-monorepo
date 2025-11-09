package com.necpgame.socialservice.model;

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
 * RatingCategoryBenefits
 */


public class RatingCategoryBenefits {

  private @Nullable Float commissionDiscount;

  private @Nullable Float escrowBonus;

  private @Nullable Integer inviteQuota;

  private @Nullable Boolean exclusiveContracts;

  private @Nullable Boolean matchmakingBoost;

  public RatingCategoryBenefits commissionDiscount(@Nullable Float commissionDiscount) {
    this.commissionDiscount = commissionDiscount;
    return this;
  }

  /**
   * Get commissionDiscount
   * @return commissionDiscount
   */
  
  @Schema(name = "commissionDiscount", example = "0.05", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("commissionDiscount")
  public @Nullable Float getCommissionDiscount() {
    return commissionDiscount;
  }

  public void setCommissionDiscount(@Nullable Float commissionDiscount) {
    this.commissionDiscount = commissionDiscount;
  }

  public RatingCategoryBenefits escrowBonus(@Nullable Float escrowBonus) {
    this.escrowBonus = escrowBonus;
    return this;
  }

  /**
   * Get escrowBonus
   * @return escrowBonus
   */
  
  @Schema(name = "escrowBonus", example = "0.1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("escrowBonus")
  public @Nullable Float getEscrowBonus() {
    return escrowBonus;
  }

  public void setEscrowBonus(@Nullable Float escrowBonus) {
    this.escrowBonus = escrowBonus;
  }

  public RatingCategoryBenefits inviteQuota(@Nullable Integer inviteQuota) {
    this.inviteQuota = inviteQuota;
    return this;
  }

  /**
   * Get inviteQuota
   * @return inviteQuota
   */
  
  @Schema(name = "inviteQuota", example = "10", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("inviteQuota")
  public @Nullable Integer getInviteQuota() {
    return inviteQuota;
  }

  public void setInviteQuota(@Nullable Integer inviteQuota) {
    this.inviteQuota = inviteQuota;
  }

  public RatingCategoryBenefits exclusiveContracts(@Nullable Boolean exclusiveContracts) {
    this.exclusiveContracts = exclusiveContracts;
    return this;
  }

  /**
   * Get exclusiveContracts
   * @return exclusiveContracts
   */
  
  @Schema(name = "exclusiveContracts", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("exclusiveContracts")
  public @Nullable Boolean getExclusiveContracts() {
    return exclusiveContracts;
  }

  public void setExclusiveContracts(@Nullable Boolean exclusiveContracts) {
    this.exclusiveContracts = exclusiveContracts;
  }

  public RatingCategoryBenefits matchmakingBoost(@Nullable Boolean matchmakingBoost) {
    this.matchmakingBoost = matchmakingBoost;
    return this;
  }

  /**
   * Get matchmakingBoost
   * @return matchmakingBoost
   */
  
  @Schema(name = "matchmakingBoost", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("matchmakingBoost")
  public @Nullable Boolean getMatchmakingBoost() {
    return matchmakingBoost;
  }

  public void setMatchmakingBoost(@Nullable Boolean matchmakingBoost) {
    this.matchmakingBoost = matchmakingBoost;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RatingCategoryBenefits ratingCategoryBenefits = (RatingCategoryBenefits) o;
    return Objects.equals(this.commissionDiscount, ratingCategoryBenefits.commissionDiscount) &&
        Objects.equals(this.escrowBonus, ratingCategoryBenefits.escrowBonus) &&
        Objects.equals(this.inviteQuota, ratingCategoryBenefits.inviteQuota) &&
        Objects.equals(this.exclusiveContracts, ratingCategoryBenefits.exclusiveContracts) &&
        Objects.equals(this.matchmakingBoost, ratingCategoryBenefits.matchmakingBoost);
  }

  @Override
  public int hashCode() {
    return Objects.hash(commissionDiscount, escrowBonus, inviteQuota, exclusiveContracts, matchmakingBoost);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RatingCategoryBenefits {\n");
    sb.append("    commissionDiscount: ").append(toIndentedString(commissionDiscount)).append("\n");
    sb.append("    escrowBonus: ").append(toIndentedString(escrowBonus)).append("\n");
    sb.append("    inviteQuota: ").append(toIndentedString(inviteQuota)).append("\n");
    sb.append("    exclusiveContracts: ").append(toIndentedString(exclusiveContracts)).append("\n");
    sb.append("    matchmakingBoost: ").append(toIndentedString(matchmakingBoost)).append("\n");
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

