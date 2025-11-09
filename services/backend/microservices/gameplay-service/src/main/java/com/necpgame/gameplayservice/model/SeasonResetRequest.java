package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.SeasonResetRequestTiersMappingInner;
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
 * SeasonResetRequest
 */


public class SeasonResetRequest {

  private String leagueId;

  private Float carryOverPercent;

  private @Nullable Integer softCapRating;

  @Valid
  private List<@Valid SeasonResetRequestTiersMappingInner> tiersMapping = new ArrayList<>();

  private Boolean archiveSnapshot = true;

  private Boolean notifyPlayers = true;

  public SeasonResetRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SeasonResetRequest(String leagueId, Float carryOverPercent) {
    this.leagueId = leagueId;
    this.carryOverPercent = carryOverPercent;
  }

  public SeasonResetRequest leagueId(String leagueId) {
    this.leagueId = leagueId;
    return this;
  }

  /**
   * Get leagueId
   * @return leagueId
   */
  @NotNull 
  @Schema(name = "leagueId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("leagueId")
  public String getLeagueId() {
    return leagueId;
  }

  public void setLeagueId(String leagueId) {
    this.leagueId = leagueId;
  }

  public SeasonResetRequest carryOverPercent(Float carryOverPercent) {
    this.carryOverPercent = carryOverPercent;
    return this;
  }

  /**
   * Get carryOverPercent
   * minimum: 0
   * maximum: 1
   * @return carryOverPercent
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "1") 
  @Schema(name = "carryOverPercent", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("carryOverPercent")
  public Float getCarryOverPercent() {
    return carryOverPercent;
  }

  public void setCarryOverPercent(Float carryOverPercent) {
    this.carryOverPercent = carryOverPercent;
  }

  public SeasonResetRequest softCapRating(@Nullable Integer softCapRating) {
    this.softCapRating = softCapRating;
    return this;
  }

  /**
   * Get softCapRating
   * @return softCapRating
   */
  
  @Schema(name = "softCapRating", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("softCapRating")
  public @Nullable Integer getSoftCapRating() {
    return softCapRating;
  }

  public void setSoftCapRating(@Nullable Integer softCapRating) {
    this.softCapRating = softCapRating;
  }

  public SeasonResetRequest tiersMapping(List<@Valid SeasonResetRequestTiersMappingInner> tiersMapping) {
    this.tiersMapping = tiersMapping;
    return this;
  }

  public SeasonResetRequest addTiersMappingItem(SeasonResetRequestTiersMappingInner tiersMappingItem) {
    if (this.tiersMapping == null) {
      this.tiersMapping = new ArrayList<>();
    }
    this.tiersMapping.add(tiersMappingItem);
    return this;
  }

  /**
   * Get tiersMapping
   * @return tiersMapping
   */
  @Valid 
  @Schema(name = "tiersMapping", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tiersMapping")
  public List<@Valid SeasonResetRequestTiersMappingInner> getTiersMapping() {
    return tiersMapping;
  }

  public void setTiersMapping(List<@Valid SeasonResetRequestTiersMappingInner> tiersMapping) {
    this.tiersMapping = tiersMapping;
  }

  public SeasonResetRequest archiveSnapshot(Boolean archiveSnapshot) {
    this.archiveSnapshot = archiveSnapshot;
    return this;
  }

  /**
   * Get archiveSnapshot
   * @return archiveSnapshot
   */
  
  @Schema(name = "archiveSnapshot", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("archiveSnapshot")
  public Boolean getArchiveSnapshot() {
    return archiveSnapshot;
  }

  public void setArchiveSnapshot(Boolean archiveSnapshot) {
    this.archiveSnapshot = archiveSnapshot;
  }

  public SeasonResetRequest notifyPlayers(Boolean notifyPlayers) {
    this.notifyPlayers = notifyPlayers;
    return this;
  }

  /**
   * Get notifyPlayers
   * @return notifyPlayers
   */
  
  @Schema(name = "notifyPlayers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notifyPlayers")
  public Boolean getNotifyPlayers() {
    return notifyPlayers;
  }

  public void setNotifyPlayers(Boolean notifyPlayers) {
    this.notifyPlayers = notifyPlayers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SeasonResetRequest seasonResetRequest = (SeasonResetRequest) o;
    return Objects.equals(this.leagueId, seasonResetRequest.leagueId) &&
        Objects.equals(this.carryOverPercent, seasonResetRequest.carryOverPercent) &&
        Objects.equals(this.softCapRating, seasonResetRequest.softCapRating) &&
        Objects.equals(this.tiersMapping, seasonResetRequest.tiersMapping) &&
        Objects.equals(this.archiveSnapshot, seasonResetRequest.archiveSnapshot) &&
        Objects.equals(this.notifyPlayers, seasonResetRequest.notifyPlayers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(leagueId, carryOverPercent, softCapRating, tiersMapping, archiveSnapshot, notifyPlayers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SeasonResetRequest {\n");
    sb.append("    leagueId: ").append(toIndentedString(leagueId)).append("\n");
    sb.append("    carryOverPercent: ").append(toIndentedString(carryOverPercent)).append("\n");
    sb.append("    softCapRating: ").append(toIndentedString(softCapRating)).append("\n");
    sb.append("    tiersMapping: ").append(toIndentedString(tiersMapping)).append("\n");
    sb.append("    archiveSnapshot: ").append(toIndentedString(archiveSnapshot)).append("\n");
    sb.append("    notifyPlayers: ").append(toIndentedString(notifyPlayers)).append("\n");
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

