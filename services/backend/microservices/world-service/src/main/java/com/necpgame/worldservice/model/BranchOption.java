package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.BranchType;
import com.necpgame.worldservice.model.ReputationChange;
import com.necpgame.worldservice.model.RewardPayload;
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
 * BranchOption
 */


public class BranchOption {

  private String branchId;

  private String title;

  private BranchType type;

  private @Nullable String description;

  @Valid
  private List<String> recommendedRoles = new ArrayList<>();

  @Valid
  private List<@Valid ReputationChange> reputationPreview = new ArrayList<>();

  private @Nullable RewardPayload rewardPreview;

  private @Nullable Integer lockoutOnSelect;

  public BranchOption() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BranchOption(String branchId, String title, BranchType type) {
    this.branchId = branchId;
    this.title = title;
    this.type = type;
  }

  public BranchOption branchId(String branchId) {
    this.branchId = branchId;
    return this;
  }

  /**
   * Get branchId
   * @return branchId
   */
  @NotNull 
  @Schema(name = "branchId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("branchId")
  public String getBranchId() {
    return branchId;
  }

  public void setBranchId(String branchId) {
    this.branchId = branchId;
  }

  public BranchOption title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   */
  @NotNull 
  @Schema(name = "title", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public BranchOption type(BranchType type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @NotNull @Valid 
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public BranchType getType() {
    return type;
  }

  public void setType(BranchType type) {
    this.type = type;
  }

  public BranchOption description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public BranchOption recommendedRoles(List<String> recommendedRoles) {
    this.recommendedRoles = recommendedRoles;
    return this;
  }

  public BranchOption addRecommendedRolesItem(String recommendedRolesItem) {
    if (this.recommendedRoles == null) {
      this.recommendedRoles = new ArrayList<>();
    }
    this.recommendedRoles.add(recommendedRolesItem);
    return this;
  }

  /**
   * Get recommendedRoles
   * @return recommendedRoles
   */
  
  @Schema(name = "recommendedRoles", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recommendedRoles")
  public List<String> getRecommendedRoles() {
    return recommendedRoles;
  }

  public void setRecommendedRoles(List<String> recommendedRoles) {
    this.recommendedRoles = recommendedRoles;
  }

  public BranchOption reputationPreview(List<@Valid ReputationChange> reputationPreview) {
    this.reputationPreview = reputationPreview;
    return this;
  }

  public BranchOption addReputationPreviewItem(ReputationChange reputationPreviewItem) {
    if (this.reputationPreview == null) {
      this.reputationPreview = new ArrayList<>();
    }
    this.reputationPreview.add(reputationPreviewItem);
    return this;
  }

  /**
   * Get reputationPreview
   * @return reputationPreview
   */
  @Valid 
  @Schema(name = "reputationPreview", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputationPreview")
  public List<@Valid ReputationChange> getReputationPreview() {
    return reputationPreview;
  }

  public void setReputationPreview(List<@Valid ReputationChange> reputationPreview) {
    this.reputationPreview = reputationPreview;
  }

  public BranchOption rewardPreview(@Nullable RewardPayload rewardPreview) {
    this.rewardPreview = rewardPreview;
    return this;
  }

  /**
   * Get rewardPreview
   * @return rewardPreview
   */
  @Valid 
  @Schema(name = "rewardPreview", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewardPreview")
  public @Nullable RewardPayload getRewardPreview() {
    return rewardPreview;
  }

  public void setRewardPreview(@Nullable RewardPayload rewardPreview) {
    this.rewardPreview = rewardPreview;
  }

  public BranchOption lockoutOnSelect(@Nullable Integer lockoutOnSelect) {
    this.lockoutOnSelect = lockoutOnSelect;
    return this;
  }

  /**
   * Длительность блокировки в минутах
   * minimum: 0
   * @return lockoutOnSelect
   */
  @Min(value = 0) 
  @Schema(name = "lockoutOnSelect", description = "Длительность блокировки в минутах", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lockoutOnSelect")
  public @Nullable Integer getLockoutOnSelect() {
    return lockoutOnSelect;
  }

  public void setLockoutOnSelect(@Nullable Integer lockoutOnSelect) {
    this.lockoutOnSelect = lockoutOnSelect;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BranchOption branchOption = (BranchOption) o;
    return Objects.equals(this.branchId, branchOption.branchId) &&
        Objects.equals(this.title, branchOption.title) &&
        Objects.equals(this.type, branchOption.type) &&
        Objects.equals(this.description, branchOption.description) &&
        Objects.equals(this.recommendedRoles, branchOption.recommendedRoles) &&
        Objects.equals(this.reputationPreview, branchOption.reputationPreview) &&
        Objects.equals(this.rewardPreview, branchOption.rewardPreview) &&
        Objects.equals(this.lockoutOnSelect, branchOption.lockoutOnSelect);
  }

  @Override
  public int hashCode() {
    return Objects.hash(branchId, title, type, description, recommendedRoles, reputationPreview, rewardPreview, lockoutOnSelect);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BranchOption {\n");
    sb.append("    branchId: ").append(toIndentedString(branchId)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    recommendedRoles: ").append(toIndentedString(recommendedRoles)).append("\n");
    sb.append("    reputationPreview: ").append(toIndentedString(reputationPreview)).append("\n");
    sb.append("    rewardPreview: ").append(toIndentedString(rewardPreview)).append("\n");
    sb.append("    lockoutOnSelect: ").append(toIndentedString(lockoutOnSelect)).append("\n");
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

