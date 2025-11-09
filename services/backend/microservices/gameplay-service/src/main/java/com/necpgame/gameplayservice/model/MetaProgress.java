package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.MetaProgressAchievementsInner;
import com.necpgame.gameplayservice.model.MetaProgressGlobalRating;
import com.necpgame.gameplayservice.model.MetaProgressHallOfFameInner;
import com.necpgame.gameplayservice.model.MetaProgressLegacyItemsInner;
import com.necpgame.gameplayservice.model.MetaProgressTitlesInner;
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
 * MetaProgress
 */


public class MetaProgress {

  private @Nullable String accountId;

  private @Nullable MetaProgressGlobalRating globalRating;

  @Valid
  private List<@Valid MetaProgressAchievementsInner> achievements = new ArrayList<>();

  @Valid
  private List<@Valid MetaProgressTitlesInner> titles = new ArrayList<>();

  @Valid
  private List<String> cosmetics = new ArrayList<>();

  @Valid
  private List<@Valid MetaProgressLegacyItemsInner> legacyItems = new ArrayList<>();

  @Valid
  private List<@Valid MetaProgressHallOfFameInner> hallOfFame = new ArrayList<>();

  public MetaProgress accountId(@Nullable String accountId) {
    this.accountId = accountId;
    return this;
  }

  /**
   * Get accountId
   * @return accountId
   */
  
  @Schema(name = "account_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("account_id")
  public @Nullable String getAccountId() {
    return accountId;
  }

  public void setAccountId(@Nullable String accountId) {
    this.accountId = accountId;
  }

  public MetaProgress globalRating(@Nullable MetaProgressGlobalRating globalRating) {
    this.globalRating = globalRating;
    return this;
  }

  /**
   * Get globalRating
   * @return globalRating
   */
  @Valid 
  @Schema(name = "global_rating", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("global_rating")
  public @Nullable MetaProgressGlobalRating getGlobalRating() {
    return globalRating;
  }

  public void setGlobalRating(@Nullable MetaProgressGlobalRating globalRating) {
    this.globalRating = globalRating;
  }

  public MetaProgress achievements(List<@Valid MetaProgressAchievementsInner> achievements) {
    this.achievements = achievements;
    return this;
  }

  public MetaProgress addAchievementsItem(MetaProgressAchievementsInner achievementsItem) {
    if (this.achievements == null) {
      this.achievements = new ArrayList<>();
    }
    this.achievements.add(achievementsItem);
    return this;
  }

  /**
   * Достижения (сохраняются между лигами)
   * @return achievements
   */
  @Valid 
  @Schema(name = "achievements", description = "Достижения (сохраняются между лигами)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("achievements")
  public List<@Valid MetaProgressAchievementsInner> getAchievements() {
    return achievements;
  }

  public void setAchievements(List<@Valid MetaProgressAchievementsInner> achievements) {
    this.achievements = achievements;
  }

  public MetaProgress titles(List<@Valid MetaProgressTitlesInner> titles) {
    this.titles = titles;
    return this;
  }

  public MetaProgress addTitlesItem(MetaProgressTitlesInner titlesItem) {
    if (this.titles == null) {
      this.titles = new ArrayList<>();
    }
    this.titles.add(titlesItem);
    return this;
  }

  /**
   * Титулы (сохраняются между лигами)
   * @return titles
   */
  @Valid 
  @Schema(name = "titles", description = "Титулы (сохраняются между лигами)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("titles")
  public List<@Valid MetaProgressTitlesInner> getTitles() {
    return titles;
  }

  public void setTitles(List<@Valid MetaProgressTitlesInner> titles) {
    this.titles = titles;
  }

  public MetaProgress cosmetics(List<String> cosmetics) {
    this.cosmetics = cosmetics;
    return this;
  }

  public MetaProgress addCosmeticsItem(String cosmeticsItem) {
    if (this.cosmetics == null) {
      this.cosmetics = new ArrayList<>();
    }
    this.cosmetics.add(cosmeticsItem);
    return this;
  }

  /**
   * Косметика (сохраняется между лигами)
   * @return cosmetics
   */
  
  @Schema(name = "cosmetics", description = "Косметика (сохраняется между лигами)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cosmetics")
  public List<String> getCosmetics() {
    return cosmetics;
  }

  public void setCosmetics(List<String> cosmetics) {
    this.cosmetics = cosmetics;
  }

  public MetaProgress legacyItems(List<@Valid MetaProgressLegacyItemsInner> legacyItems) {
    this.legacyItems = legacyItems;
    return this;
  }

  public MetaProgress addLegacyItemsItem(MetaProgressLegacyItemsInner legacyItemsItem) {
    if (this.legacyItems == null) {
      this.legacyItems = new ArrayList<>();
    }
    this.legacyItems.add(legacyItemsItem);
    return this;
  }

  /**
   * Уникальные награды за прошлые лиги
   * @return legacyItems
   */
  @Valid 
  @Schema(name = "legacy_items", description = "Уникальные награды за прошлые лиги", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("legacy_items")
  public List<@Valid MetaProgressLegacyItemsInner> getLegacyItems() {
    return legacyItems;
  }

  public void setLegacyItems(List<@Valid MetaProgressLegacyItemsInner> legacyItems) {
    this.legacyItems = legacyItems;
  }

  public MetaProgress hallOfFame(List<@Valid MetaProgressHallOfFameInner> hallOfFame) {
    this.hallOfFame = hallOfFame;
    return this;
  }

  public MetaProgress addHallOfFameItem(MetaProgressHallOfFameInner hallOfFameItem) {
    if (this.hallOfFame == null) {
      this.hallOfFame = new ArrayList<>();
    }
    this.hallOfFame.add(hallOfFameItem);
    return this;
  }

  /**
   * Лучшие результаты в прошлых лигах
   * @return hallOfFame
   */
  @Valid 
  @Schema(name = "hall_of_fame", description = "Лучшие результаты в прошлых лигах", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hall_of_fame")
  public List<@Valid MetaProgressHallOfFameInner> getHallOfFame() {
    return hallOfFame;
  }

  public void setHallOfFame(List<@Valid MetaProgressHallOfFameInner> hallOfFame) {
    this.hallOfFame = hallOfFame;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MetaProgress metaProgress = (MetaProgress) o;
    return Objects.equals(this.accountId, metaProgress.accountId) &&
        Objects.equals(this.globalRating, metaProgress.globalRating) &&
        Objects.equals(this.achievements, metaProgress.achievements) &&
        Objects.equals(this.titles, metaProgress.titles) &&
        Objects.equals(this.cosmetics, metaProgress.cosmetics) &&
        Objects.equals(this.legacyItems, metaProgress.legacyItems) &&
        Objects.equals(this.hallOfFame, metaProgress.hallOfFame);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, globalRating, achievements, titles, cosmetics, legacyItems, hallOfFame);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MetaProgress {\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    globalRating: ").append(toIndentedString(globalRating)).append("\n");
    sb.append("    achievements: ").append(toIndentedString(achievements)).append("\n");
    sb.append("    titles: ").append(toIndentedString(titles)).append("\n");
    sb.append("    cosmetics: ").append(toIndentedString(cosmetics)).append("\n");
    sb.append("    legacyItems: ").append(toIndentedString(legacyItems)).append("\n");
    sb.append("    hallOfFame: ").append(toIndentedString(hallOfFame)).append("\n");
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

