package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.CheckStamina200ResponseAvailableActions;
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
 * CheckStamina200Response
 */

@JsonTypeName("checkStamina_200_response")

public class CheckStamina200Response {

  private @Nullable String characterId;

  private @Nullable BigDecimal currentStamina;

  private @Nullable BigDecimal maxStamina;

  private @Nullable BigDecimal staminaRegenRate;

  private @Nullable CheckStamina200ResponseAvailableActions availableActions;

  public CheckStamina200Response characterId(@Nullable String characterId) {
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

  public CheckStamina200Response currentStamina(@Nullable BigDecimal currentStamina) {
    this.currentStamina = currentStamina;
    return this;
  }

  /**
   * Get currentStamina
   * @return currentStamina
   */
  @Valid 
  @Schema(name = "current_stamina", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_stamina")
  public @Nullable BigDecimal getCurrentStamina() {
    return currentStamina;
  }

  public void setCurrentStamina(@Nullable BigDecimal currentStamina) {
    this.currentStamina = currentStamina;
  }

  public CheckStamina200Response maxStamina(@Nullable BigDecimal maxStamina) {
    this.maxStamina = maxStamina;
    return this;
  }

  /**
   * Get maxStamina
   * @return maxStamina
   */
  @Valid 
  @Schema(name = "max_stamina", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max_stamina")
  public @Nullable BigDecimal getMaxStamina() {
    return maxStamina;
  }

  public void setMaxStamina(@Nullable BigDecimal maxStamina) {
    this.maxStamina = maxStamina;
  }

  public CheckStamina200Response staminaRegenRate(@Nullable BigDecimal staminaRegenRate) {
    this.staminaRegenRate = staminaRegenRate;
    return this;
  }

  /**
   * Get staminaRegenRate
   * @return staminaRegenRate
   */
  @Valid 
  @Schema(name = "stamina_regen_rate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stamina_regen_rate")
  public @Nullable BigDecimal getStaminaRegenRate() {
    return staminaRegenRate;
  }

  public void setStaminaRegenRate(@Nullable BigDecimal staminaRegenRate) {
    this.staminaRegenRate = staminaRegenRate;
  }

  public CheckStamina200Response availableActions(@Nullable CheckStamina200ResponseAvailableActions availableActions) {
    this.availableActions = availableActions;
    return this;
  }

  /**
   * Get availableActions
   * @return availableActions
   */
  @Valid 
  @Schema(name = "available_actions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("available_actions")
  public @Nullable CheckStamina200ResponseAvailableActions getAvailableActions() {
    return availableActions;
  }

  public void setAvailableActions(@Nullable CheckStamina200ResponseAvailableActions availableActions) {
    this.availableActions = availableActions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CheckStamina200Response checkStamina200Response = (CheckStamina200Response) o;
    return Objects.equals(this.characterId, checkStamina200Response.characterId) &&
        Objects.equals(this.currentStamina, checkStamina200Response.currentStamina) &&
        Objects.equals(this.maxStamina, checkStamina200Response.maxStamina) &&
        Objects.equals(this.staminaRegenRate, checkStamina200Response.staminaRegenRate) &&
        Objects.equals(this.availableActions, checkStamina200Response.availableActions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, currentStamina, maxStamina, staminaRegenRate, availableActions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CheckStamina200Response {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    currentStamina: ").append(toIndentedString(currentStamina)).append("\n");
    sb.append("    maxStamina: ").append(toIndentedString(maxStamina)).append("\n");
    sb.append("    staminaRegenRate: ").append(toIndentedString(staminaRegenRate)).append("\n");
    sb.append("    availableActions: ").append(toIndentedString(availableActions)).append("\n");
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

