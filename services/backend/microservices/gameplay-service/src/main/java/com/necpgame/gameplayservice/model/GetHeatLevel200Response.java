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
 * GetHeatLevel200Response
 */

@JsonTypeName("getHeatLevel_200_response")

public class GetHeatLevel200Response {

  private @Nullable String characterId;

  private @Nullable BigDecimal currentHeat;

  private @Nullable BigDecimal maxHeat;

  private @Nullable BigDecimal coolingRate;

  private @Nullable BigDecimal overheatThreshold;

  public GetHeatLevel200Response characterId(@Nullable String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable String characterId) {
    this.characterId = characterId;
  }

  public GetHeatLevel200Response currentHeat(@Nullable BigDecimal currentHeat) {
    this.currentHeat = currentHeat;
    return this;
  }

  /**
   * Текущий перегрев (%)
   * minimum: 0
   * maximum: 100
   * @return currentHeat
   */
  @Valid @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "current_heat", description = "Текущий перегрев (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_heat")
  public @Nullable BigDecimal getCurrentHeat() {
    return currentHeat;
  }

  public void setCurrentHeat(@Nullable BigDecimal currentHeat) {
    this.currentHeat = currentHeat;
  }

  public GetHeatLevel200Response maxHeat(@Nullable BigDecimal maxHeat) {
    this.maxHeat = maxHeat;
    return this;
  }

  /**
   * Макс. перегрев до отключения
   * @return maxHeat
   */
  @Valid 
  @Schema(name = "max_heat", description = "Макс. перегрев до отключения", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max_heat")
  public @Nullable BigDecimal getMaxHeat() {
    return maxHeat;
  }

  public void setMaxHeat(@Nullable BigDecimal maxHeat) {
    this.maxHeat = maxHeat;
  }

  public GetHeatLevel200Response coolingRate(@Nullable BigDecimal coolingRate) {
    this.coolingRate = coolingRate;
    return this;
  }

  /**
   * Скорость охлаждения (% в секунду)
   * @return coolingRate
   */
  @Valid 
  @Schema(name = "cooling_rate", description = "Скорость охлаждения (% в секунду)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cooling_rate")
  public @Nullable BigDecimal getCoolingRate() {
    return coolingRate;
  }

  public void setCoolingRate(@Nullable BigDecimal coolingRate) {
    this.coolingRate = coolingRate;
  }

  public GetHeatLevel200Response overheatThreshold(@Nullable BigDecimal overheatThreshold) {
    this.overheatThreshold = overheatThreshold;
    return this;
  }

  /**
   * Порог для автоотключения
   * @return overheatThreshold
   */
  @Valid 
  @Schema(name = "overheat_threshold", description = "Порог для автоотключения", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("overheat_threshold")
  public @Nullable BigDecimal getOverheatThreshold() {
    return overheatThreshold;
  }

  public void setOverheatThreshold(@Nullable BigDecimal overheatThreshold) {
    this.overheatThreshold = overheatThreshold;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetHeatLevel200Response getHeatLevel200Response = (GetHeatLevel200Response) o;
    return Objects.equals(this.characterId, getHeatLevel200Response.characterId) &&
        Objects.equals(this.currentHeat, getHeatLevel200Response.currentHeat) &&
        Objects.equals(this.maxHeat, getHeatLevel200Response.maxHeat) &&
        Objects.equals(this.coolingRate, getHeatLevel200Response.coolingRate) &&
        Objects.equals(this.overheatThreshold, getHeatLevel200Response.overheatThreshold);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, currentHeat, maxHeat, coolingRate, overheatThreshold);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetHeatLevel200Response {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    currentHeat: ").append(toIndentedString(currentHeat)).append("\n");
    sb.append("    maxHeat: ").append(toIndentedString(maxHeat)).append("\n");
    sb.append("    coolingRate: ").append(toIndentedString(coolingRate)).append("\n");
    sb.append("    overheatThreshold: ").append(toIndentedString(overheatThreshold)).append("\n");
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

