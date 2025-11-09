package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * GuildProgression
 */


public class GuildProgression {

  private @Nullable Integer level;

  private @Nullable Integer xp;

  private @Nullable Integer nextLevelXp;

  @Valid
  private List<String> perksUnlocked = new ArrayList<>();

  @Valid
  private List<String> research = new ArrayList<>();

  public GuildProgression level(@Nullable Integer level) {
    this.level = level;
    return this;
  }

  /**
   * Get level
   * @return level
   */
  
  @Schema(name = "level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level")
  public @Nullable Integer getLevel() {
    return level;
  }

  public void setLevel(@Nullable Integer level) {
    this.level = level;
  }

  public GuildProgression xp(@Nullable Integer xp) {
    this.xp = xp;
    return this;
  }

  /**
   * Get xp
   * @return xp
   */
  
  @Schema(name = "xp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("xp")
  public @Nullable Integer getXp() {
    return xp;
  }

  public void setXp(@Nullable Integer xp) {
    this.xp = xp;
  }

  public GuildProgression nextLevelXp(@Nullable Integer nextLevelXp) {
    this.nextLevelXp = nextLevelXp;
    return this;
  }

  /**
   * Get nextLevelXp
   * @return nextLevelXp
   */
  
  @Schema(name = "nextLevelXp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nextLevelXp")
  public @Nullable Integer getNextLevelXp() {
    return nextLevelXp;
  }

  public void setNextLevelXp(@Nullable Integer nextLevelXp) {
    this.nextLevelXp = nextLevelXp;
  }

  public GuildProgression perksUnlocked(List<String> perksUnlocked) {
    this.perksUnlocked = perksUnlocked;
    return this;
  }

  public GuildProgression addPerksUnlockedItem(String perksUnlockedItem) {
    if (this.perksUnlocked == null) {
      this.perksUnlocked = new ArrayList<>();
    }
    this.perksUnlocked.add(perksUnlockedItem);
    return this;
  }

  /**
   * Get perksUnlocked
   * @return perksUnlocked
   */
  
  @Schema(name = "perksUnlocked", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("perksUnlocked")
  public List<String> getPerksUnlocked() {
    return perksUnlocked;
  }

  public void setPerksUnlocked(List<String> perksUnlocked) {
    this.perksUnlocked = perksUnlocked;
  }

  public GuildProgression research(List<String> research) {
    this.research = research;
    return this;
  }

  public GuildProgression addResearchItem(String researchItem) {
    if (this.research == null) {
      this.research = new ArrayList<>();
    }
    this.research.add(researchItem);
    return this;
  }

  /**
   * Get research
   * @return research
   */
  
  @Schema(name = "research", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("research")
  public List<String> getResearch() {
    return research;
  }

  public void setResearch(List<String> research) {
    this.research = research;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuildProgression guildProgression = (GuildProgression) o;
    return Objects.equals(this.level, guildProgression.level) &&
        Objects.equals(this.xp, guildProgression.xp) &&
        Objects.equals(this.nextLevelXp, guildProgression.nextLevelXp) &&
        Objects.equals(this.perksUnlocked, guildProgression.perksUnlocked) &&
        Objects.equals(this.research, guildProgression.research);
  }

  @Override
  public int hashCode() {
    return Objects.hash(level, xp, nextLevelXp, perksUnlocked, research);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildProgression {\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    xp: ").append(toIndentedString(xp)).append("\n");
    sb.append("    nextLevelXp: ").append(toIndentedString(nextLevelXp)).append("\n");
    sb.append("    perksUnlocked: ").append(toIndentedString(perksUnlocked)).append("\n");
    sb.append("    research: ").append(toIndentedString(research)).append("\n");
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

