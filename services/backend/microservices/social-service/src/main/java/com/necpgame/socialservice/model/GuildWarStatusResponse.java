package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.AllianceInfo;
import com.necpgame.socialservice.model.GuildWarStatus;
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
 * GuildWarStatusResponse
 */


public class GuildWarStatusResponse {

  @Valid
  private List<@Valid GuildWarStatus> wars = new ArrayList<>();

  @Valid
  private List<@Valid AllianceInfo> alliances = new ArrayList<>();

  public GuildWarStatusResponse wars(List<@Valid GuildWarStatus> wars) {
    this.wars = wars;
    return this;
  }

  public GuildWarStatusResponse addWarsItem(GuildWarStatus warsItem) {
    if (this.wars == null) {
      this.wars = new ArrayList<>();
    }
    this.wars.add(warsItem);
    return this;
  }

  /**
   * Get wars
   * @return wars
   */
  @Valid 
  @Schema(name = "wars", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("wars")
  public List<@Valid GuildWarStatus> getWars() {
    return wars;
  }

  public void setWars(List<@Valid GuildWarStatus> wars) {
    this.wars = wars;
  }

  public GuildWarStatusResponse alliances(List<@Valid AllianceInfo> alliances) {
    this.alliances = alliances;
    return this;
  }

  public GuildWarStatusResponse addAlliancesItem(AllianceInfo alliancesItem) {
    if (this.alliances == null) {
      this.alliances = new ArrayList<>();
    }
    this.alliances.add(alliancesItem);
    return this;
  }

  /**
   * Get alliances
   * @return alliances
   */
  @Valid 
  @Schema(name = "alliances", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("alliances")
  public List<@Valid AllianceInfo> getAlliances() {
    return alliances;
  }

  public void setAlliances(List<@Valid AllianceInfo> alliances) {
    this.alliances = alliances;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuildWarStatusResponse guildWarStatusResponse = (GuildWarStatusResponse) o;
    return Objects.equals(this.wars, guildWarStatusResponse.wars) &&
        Objects.equals(this.alliances, guildWarStatusResponse.alliances);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wars, alliances);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildWarStatusResponse {\n");
    sb.append("    wars: ").append(toIndentedString(wars)).append("\n");
    sb.append("    alliances: ").append(toIndentedString(alliances)).append("\n");
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

