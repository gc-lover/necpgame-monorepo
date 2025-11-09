package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SocialCampaignCost
 */

@JsonTypeName("SocialCampaign_cost")

public class SocialCampaignCost {

  private Integer eddies;

  private @Nullable Integer influenceTokens;

  public SocialCampaignCost() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SocialCampaignCost(Integer eddies) {
    this.eddies = eddies;
  }

  public SocialCampaignCost eddies(Integer eddies) {
    this.eddies = eddies;
    return this;
  }

  /**
   * Get eddies
   * minimum: 0
   * @return eddies
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "eddies", example = "450000", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("eddies")
  public Integer getEddies() {
    return eddies;
  }

  public void setEddies(Integer eddies) {
    this.eddies = eddies;
  }

  public SocialCampaignCost influenceTokens(@Nullable Integer influenceTokens) {
    this.influenceTokens = influenceTokens;
    return this;
  }

  /**
   * Get influenceTokens
   * minimum: 0
   * @return influenceTokens
   */
  @Min(value = 0) 
  @Schema(name = "influenceTokens", example = "12", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("influenceTokens")
  public @Nullable Integer getInfluenceTokens() {
    return influenceTokens;
  }

  public void setInfluenceTokens(@Nullable Integer influenceTokens) {
    this.influenceTokens = influenceTokens;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SocialCampaignCost socialCampaignCost = (SocialCampaignCost) o;
    return Objects.equals(this.eddies, socialCampaignCost.eddies) &&
        Objects.equals(this.influenceTokens, socialCampaignCost.influenceTokens);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eddies, influenceTokens);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SocialCampaignCost {\n");
    sb.append("    eddies: ").append(toIndentedString(eddies)).append("\n");
    sb.append("    influenceTokens: ").append(toIndentedString(influenceTokens)).append("\n");
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

