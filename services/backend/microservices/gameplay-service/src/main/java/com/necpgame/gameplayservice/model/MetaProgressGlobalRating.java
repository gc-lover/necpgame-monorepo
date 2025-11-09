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
 * Глобальный рейтинг (сохраняется между лигами)
 */

@Schema(name = "MetaProgress_global_rating", description = "Глобальный рейтинг (сохраняется между лигами)")
@JsonTypeName("MetaProgress_global_rating")

public class MetaProgressGlobalRating {

  private @Nullable BigDecimal mmr;

  private @Nullable BigDecimal elo;

  private @Nullable String rank;

  public MetaProgressGlobalRating mmr(@Nullable BigDecimal mmr) {
    this.mmr = mmr;
    return this;
  }

  /**
   * Get mmr
   * @return mmr
   */
  @Valid 
  @Schema(name = "mmr", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mmr")
  public @Nullable BigDecimal getMmr() {
    return mmr;
  }

  public void setMmr(@Nullable BigDecimal mmr) {
    this.mmr = mmr;
  }

  public MetaProgressGlobalRating elo(@Nullable BigDecimal elo) {
    this.elo = elo;
    return this;
  }

  /**
   * Get elo
   * @return elo
   */
  @Valid 
  @Schema(name = "elo", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("elo")
  public @Nullable BigDecimal getElo() {
    return elo;
  }

  public void setElo(@Nullable BigDecimal elo) {
    this.elo = elo;
  }

  public MetaProgressGlobalRating rank(@Nullable String rank) {
    this.rank = rank;
    return this;
  }

  /**
   * Get rank
   * @return rank
   */
  
  @Schema(name = "rank", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rank")
  public @Nullable String getRank() {
    return rank;
  }

  public void setRank(@Nullable String rank) {
    this.rank = rank;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MetaProgressGlobalRating metaProgressGlobalRating = (MetaProgressGlobalRating) o;
    return Objects.equals(this.mmr, metaProgressGlobalRating.mmr) &&
        Objects.equals(this.elo, metaProgressGlobalRating.elo) &&
        Objects.equals(this.rank, metaProgressGlobalRating.rank);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mmr, elo, rank);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MetaProgressGlobalRating {\n");
    sb.append("    mmr: ").append(toIndentedString(mmr)).append("\n");
    sb.append("    elo: ").append(toIndentedString(elo)).append("\n");
    sb.append("    rank: ").append(toIndentedString(rank)).append("\n");
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

