package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Штрафы к характеристикам от киберпсихоза
 */

@Schema(name = "StatPenalties", description = "Штрафы к характеристикам от киберпсихоза")

public class StatPenalties {

  private @Nullable Float accuracy;

  private @Nullable Float socialSkills;

  private @Nullable Float healthRegen;

  private @Nullable Float energyMax;

  @Valid
  private JsonNullable<Map<String, Float>> other = JsonNullable.<Map<String, Float>>undefined();

  public StatPenalties accuracy(@Nullable Float accuracy) {
    this.accuracy = accuracy;
    return this;
  }

  /**
   * Штраф к точности
   * maximum: 0
   * @return accuracy
   */
  @DecimalMax(value = "0") 
  @Schema(name = "accuracy", description = "Штраф к точности", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("accuracy")
  public @Nullable Float getAccuracy() {
    return accuracy;
  }

  public void setAccuracy(@Nullable Float accuracy) {
    this.accuracy = accuracy;
  }

  public StatPenalties socialSkills(@Nullable Float socialSkills) {
    this.socialSkills = socialSkills;
    return this;
  }

  /**
   * Штраф к социальным навыкам
   * maximum: 0
   * @return socialSkills
   */
  @DecimalMax(value = "0") 
  @Schema(name = "social_skills", description = "Штраф к социальным навыкам", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("social_skills")
  public @Nullable Float getSocialSkills() {
    return socialSkills;
  }

  public void setSocialSkills(@Nullable Float socialSkills) {
    this.socialSkills = socialSkills;
  }

  public StatPenalties healthRegen(@Nullable Float healthRegen) {
    this.healthRegen = healthRegen;
    return this;
  }

  /**
   * Штраф к восстановлению здоровья
   * maximum: 0
   * @return healthRegen
   */
  @DecimalMax(value = "0") 
  @Schema(name = "health_regen", description = "Штраф к восстановлению здоровья", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("health_regen")
  public @Nullable Float getHealthRegen() {
    return healthRegen;
  }

  public void setHealthRegen(@Nullable Float healthRegen) {
    this.healthRegen = healthRegen;
  }

  public StatPenalties energyMax(@Nullable Float energyMax) {
    this.energyMax = energyMax;
    return this;
  }

  /**
   * Штраф к максимальной энергии
   * maximum: 0
   * @return energyMax
   */
  @DecimalMax(value = "0") 
  @Schema(name = "energy_max", description = "Штраф к максимальной энергии", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("energy_max")
  public @Nullable Float getEnergyMax() {
    return energyMax;
  }

  public void setEnergyMax(@Nullable Float energyMax) {
    this.energyMax = energyMax;
  }

  public StatPenalties other(Map<String, Float> other) {
    this.other = JsonNullable.of(other);
    return this;
  }

  public StatPenalties putOtherItem(String key, Float otherItem) {
    if (this.other == null || !this.other.isPresent()) {
      this.other = JsonNullable.of(new HashMap<>());
    }
    this.other.get().put(key, otherItem);
    return this;
  }

  /**
   * Другие штрафы
   * @return other
   */
  
  @Schema(name = "other", description = "Другие штрафы", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("other")
  public JsonNullable<Map<String, Float>> getOther() {
    return other;
  }

  public void setOther(JsonNullable<Map<String, Float>> other) {
    this.other = other;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StatPenalties statPenalties = (StatPenalties) o;
    return Objects.equals(this.accuracy, statPenalties.accuracy) &&
        Objects.equals(this.socialSkills, statPenalties.socialSkills) &&
        Objects.equals(this.healthRegen, statPenalties.healthRegen) &&
        Objects.equals(this.energyMax, statPenalties.energyMax) &&
        equalsNullable(this.other, statPenalties.other);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(accuracy, socialSkills, healthRegen, energyMax, hashCodeNullable(other));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StatPenalties {\n");
    sb.append("    accuracy: ").append(toIndentedString(accuracy)).append("\n");
    sb.append("    socialSkills: ").append(toIndentedString(socialSkills)).append("\n");
    sb.append("    healthRegen: ").append(toIndentedString(healthRegen)).append("\n");
    sb.append("    energyMax: ").append(toIndentedString(energyMax)).append("\n");
    sb.append("    other: ").append(toIndentedString(other)).append("\n");
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

