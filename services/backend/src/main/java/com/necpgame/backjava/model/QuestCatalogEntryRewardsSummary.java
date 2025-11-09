package com.necpgame.backjava.model;

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
 * QuestCatalogEntryRewardsSummary
 */

@JsonTypeName("QuestCatalogEntry_rewards_summary")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class QuestCatalogEntryRewardsSummary {

  private @Nullable Integer experience;

  private @Nullable Integer eddies;

  private @Nullable Integer itemsCount;

  public QuestCatalogEntryRewardsSummary experience(@Nullable Integer experience) {
    this.experience = experience;
    return this;
  }

  /**
   * Get experience
   * @return experience
   */
  
  @Schema(name = "experience", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("experience")
  public @Nullable Integer getExperience() {
    return experience;
  }

  public void setExperience(@Nullable Integer experience) {
    this.experience = experience;
  }

  public QuestCatalogEntryRewardsSummary eddies(@Nullable Integer eddies) {
    this.eddies = eddies;
    return this;
  }

  /**
   * Get eddies
   * @return eddies
   */
  
  @Schema(name = "eddies", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("eddies")
  public @Nullable Integer getEddies() {
    return eddies;
  }

  public void setEddies(@Nullable Integer eddies) {
    this.eddies = eddies;
  }

  public QuestCatalogEntryRewardsSummary itemsCount(@Nullable Integer itemsCount) {
    this.itemsCount = itemsCount;
    return this;
  }

  /**
   * Get itemsCount
   * @return itemsCount
   */
  
  @Schema(name = "items_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("items_count")
  public @Nullable Integer getItemsCount() {
    return itemsCount;
  }

  public void setItemsCount(@Nullable Integer itemsCount) {
    this.itemsCount = itemsCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestCatalogEntryRewardsSummary questCatalogEntryRewardsSummary = (QuestCatalogEntryRewardsSummary) o;
    return Objects.equals(this.experience, questCatalogEntryRewardsSummary.experience) &&
        Objects.equals(this.eddies, questCatalogEntryRewardsSummary.eddies) &&
        Objects.equals(this.itemsCount, questCatalogEntryRewardsSummary.itemsCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(experience, eddies, itemsCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestCatalogEntryRewardsSummary {\n");
    sb.append("    experience: ").append(toIndentedString(experience)).append("\n");
    sb.append("    eddies: ").append(toIndentedString(eddies)).append("\n");
    sb.append("    itemsCount: ").append(toIndentedString(itemsCount)).append("\n");
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

