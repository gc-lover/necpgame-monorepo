package com.necpgame.gameplayservice.model;

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
 * ContentOverviewQuestsByType
 */

@JsonTypeName("ContentOverview_quests_by_type")

public class ContentOverviewQuestsByType {

  private @Nullable Integer main;

  private @Nullable Integer side;

  private @Nullable Integer faction;

  public ContentOverviewQuestsByType main(@Nullable Integer main) {
    this.main = main;
    return this;
  }

  /**
   * Get main
   * @return main
   */
  
  @Schema(name = "main", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("main")
  public @Nullable Integer getMain() {
    return main;
  }

  public void setMain(@Nullable Integer main) {
    this.main = main;
  }

  public ContentOverviewQuestsByType side(@Nullable Integer side) {
    this.side = side;
    return this;
  }

  /**
   * Get side
   * @return side
   */
  
  @Schema(name = "side", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("side")
  public @Nullable Integer getSide() {
    return side;
  }

  public void setSide(@Nullable Integer side) {
    this.side = side;
  }

  public ContentOverviewQuestsByType faction(@Nullable Integer faction) {
    this.faction = faction;
    return this;
  }

  /**
   * Get faction
   * @return faction
   */
  
  @Schema(name = "faction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction")
  public @Nullable Integer getFaction() {
    return faction;
  }

  public void setFaction(@Nullable Integer faction) {
    this.faction = faction;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContentOverviewQuestsByType contentOverviewQuestsByType = (ContentOverviewQuestsByType) o;
    return Objects.equals(this.main, contentOverviewQuestsByType.main) &&
        Objects.equals(this.side, contentOverviewQuestsByType.side) &&
        Objects.equals(this.faction, contentOverviewQuestsByType.faction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(main, side, faction);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContentOverviewQuestsByType {\n");
    sb.append("    main: ").append(toIndentedString(main)).append("\n");
    sb.append("    side: ").append(toIndentedString(side)).append("\n");
    sb.append("    faction: ").append(toIndentedString(faction)).append("\n");
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

