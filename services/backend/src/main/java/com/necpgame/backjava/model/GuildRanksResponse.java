package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.GuildRank;
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
 * GuildRanksResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GuildRanksResponse {

  @Valid
  private List<@Valid GuildRank> ranks = new ArrayList<>();

  public GuildRanksResponse ranks(List<@Valid GuildRank> ranks) {
    this.ranks = ranks;
    return this;
  }

  public GuildRanksResponse addRanksItem(GuildRank ranksItem) {
    if (this.ranks == null) {
      this.ranks = new ArrayList<>();
    }
    this.ranks.add(ranksItem);
    return this;
  }

  /**
   * Get ranks
   * @return ranks
   */
  @Valid 
  @Schema(name = "ranks", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ranks")
  public List<@Valid GuildRank> getRanks() {
    return ranks;
  }

  public void setRanks(List<@Valid GuildRank> ranks) {
    this.ranks = ranks;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuildRanksResponse guildRanksResponse = (GuildRanksResponse) o;
    return Objects.equals(this.ranks, guildRanksResponse.ranks);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ranks);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildRanksResponse {\n");
    sb.append("    ranks: ").append(toIndentedString(ranks)).append("\n");
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

