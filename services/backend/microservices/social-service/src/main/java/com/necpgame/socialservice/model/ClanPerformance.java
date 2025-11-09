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
 * ClanPerformance
 */


public class ClanPerformance {

  private @Nullable String clanId;

  private @Nullable Integer warsWon;

  private @Nullable Integer warsLost;

  private @Nullable Integer prestigeGained;

  public ClanPerformance clanId(@Nullable String clanId) {
    this.clanId = clanId;
    return this;
  }

  /**
   * Get clanId
   * @return clanId
   */
  
  @Schema(name = "clanId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("clanId")
  public @Nullable String getClanId() {
    return clanId;
  }

  public void setClanId(@Nullable String clanId) {
    this.clanId = clanId;
  }

  public ClanPerformance warsWon(@Nullable Integer warsWon) {
    this.warsWon = warsWon;
    return this;
  }

  /**
   * Get warsWon
   * @return warsWon
   */
  
  @Schema(name = "warsWon", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("warsWon")
  public @Nullable Integer getWarsWon() {
    return warsWon;
  }

  public void setWarsWon(@Nullable Integer warsWon) {
    this.warsWon = warsWon;
  }

  public ClanPerformance warsLost(@Nullable Integer warsLost) {
    this.warsLost = warsLost;
    return this;
  }

  /**
   * Get warsLost
   * @return warsLost
   */
  
  @Schema(name = "warsLost", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("warsLost")
  public @Nullable Integer getWarsLost() {
    return warsLost;
  }

  public void setWarsLost(@Nullable Integer warsLost) {
    this.warsLost = warsLost;
  }

  public ClanPerformance prestigeGained(@Nullable Integer prestigeGained) {
    this.prestigeGained = prestigeGained;
    return this;
  }

  /**
   * Get prestigeGained
   * @return prestigeGained
   */
  
  @Schema(name = "prestigeGained", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("prestigeGained")
  public @Nullable Integer getPrestigeGained() {
    return prestigeGained;
  }

  public void setPrestigeGained(@Nullable Integer prestigeGained) {
    this.prestigeGained = prestigeGained;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClanPerformance clanPerformance = (ClanPerformance) o;
    return Objects.equals(this.clanId, clanPerformance.clanId) &&
        Objects.equals(this.warsWon, clanPerformance.warsWon) &&
        Objects.equals(this.warsLost, clanPerformance.warsLost) &&
        Objects.equals(this.prestigeGained, clanPerformance.prestigeGained);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clanId, warsWon, warsLost, prestigeGained);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ClanPerformance {\n");
    sb.append("    clanId: ").append(toIndentedString(clanId)).append("\n");
    sb.append("    warsWon: ").append(toIndentedString(warsWon)).append("\n");
    sb.append("    warsLost: ").append(toIndentedString(warsLost)).append("\n");
    sb.append("    prestigeGained: ").append(toIndentedString(prestigeGained)).append("\n");
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

