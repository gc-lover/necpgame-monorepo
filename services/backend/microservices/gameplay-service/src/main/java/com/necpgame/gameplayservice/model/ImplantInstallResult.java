package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.Implant;
import com.necpgame.gameplayservice.model.ImplantSynergy;
import java.math.BigDecimal;
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
 * ImplantInstallResult
 */


public class ImplantInstallResult {

  private @Nullable Boolean success;

  private @Nullable Implant installedImplant;

  private @Nullable BigDecimal humanityLost;

  private @Nullable BigDecimal cyberpsychosisRisk;

  private @Nullable Object statChanges;

  @Valid
  private List<String> abilitiesUnlocked = new ArrayList<>();

  @Valid
  private List<@Valid ImplantSynergy> synergiesActivated = new ArrayList<>();

  public ImplantInstallResult success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public ImplantInstallResult installedImplant(@Nullable Implant installedImplant) {
    this.installedImplant = installedImplant;
    return this;
  }

  /**
   * Get installedImplant
   * @return installedImplant
   */
  @Valid 
  @Schema(name = "installed_implant", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("installed_implant")
  public @Nullable Implant getInstalledImplant() {
    return installedImplant;
  }

  public void setInstalledImplant(@Nullable Implant installedImplant) {
    this.installedImplant = installedImplant;
  }

  public ImplantInstallResult humanityLost(@Nullable BigDecimal humanityLost) {
    this.humanityLost = humanityLost;
    return this;
  }

  /**
   * Потеря очков человечности
   * @return humanityLost
   */
  @Valid 
  @Schema(name = "humanity_lost", description = "Потеря очков человечности", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("humanity_lost")
  public @Nullable BigDecimal getHumanityLost() {
    return humanityLost;
  }

  public void setHumanityLost(@Nullable BigDecimal humanityLost) {
    this.humanityLost = humanityLost;
  }

  public ImplantInstallResult cyberpsychosisRisk(@Nullable BigDecimal cyberpsychosisRisk) {
    this.cyberpsychosisRisk = cyberpsychosisRisk;
    return this;
  }

  /**
   * Текущий риск киберпсихоза
   * @return cyberpsychosisRisk
   */
  @Valid 
  @Schema(name = "cyberpsychosis_risk", description = "Текущий риск киберпсихоза", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cyberpsychosis_risk")
  public @Nullable BigDecimal getCyberpsychosisRisk() {
    return cyberpsychosisRisk;
  }

  public void setCyberpsychosisRisk(@Nullable BigDecimal cyberpsychosisRisk) {
    this.cyberpsychosisRisk = cyberpsychosisRisk;
  }

  public ImplantInstallResult statChanges(@Nullable Object statChanges) {
    this.statChanges = statChanges;
    return this;
  }

  /**
   * Изменения характеристик персонажа
   * @return statChanges
   */
  
  @Schema(name = "stat_changes", description = "Изменения характеристик персонажа", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stat_changes")
  public @Nullable Object getStatChanges() {
    return statChanges;
  }

  public void setStatChanges(@Nullable Object statChanges) {
    this.statChanges = statChanges;
  }

  public ImplantInstallResult abilitiesUnlocked(List<String> abilitiesUnlocked) {
    this.abilitiesUnlocked = abilitiesUnlocked;
    return this;
  }

  public ImplantInstallResult addAbilitiesUnlockedItem(String abilitiesUnlockedItem) {
    if (this.abilitiesUnlocked == null) {
      this.abilitiesUnlocked = new ArrayList<>();
    }
    this.abilitiesUnlocked.add(abilitiesUnlockedItem);
    return this;
  }

  /**
   * Разблокированные способности
   * @return abilitiesUnlocked
   */
  
  @Schema(name = "abilities_unlocked", description = "Разблокированные способности", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("abilities_unlocked")
  public List<String> getAbilitiesUnlocked() {
    return abilitiesUnlocked;
  }

  public void setAbilitiesUnlocked(List<String> abilitiesUnlocked) {
    this.abilitiesUnlocked = abilitiesUnlocked;
  }

  public ImplantInstallResult synergiesActivated(List<@Valid ImplantSynergy> synergiesActivated) {
    this.synergiesActivated = synergiesActivated;
    return this;
  }

  public ImplantInstallResult addSynergiesActivatedItem(ImplantSynergy synergiesActivatedItem) {
    if (this.synergiesActivated == null) {
      this.synergiesActivated = new ArrayList<>();
    }
    this.synergiesActivated.add(synergiesActivatedItem);
    return this;
  }

  /**
   * Активированные синергии
   * @return synergiesActivated
   */
  @Valid 
  @Schema(name = "synergies_activated", description = "Активированные синергии", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("synergies_activated")
  public List<@Valid ImplantSynergy> getSynergiesActivated() {
    return synergiesActivated;
  }

  public void setSynergiesActivated(List<@Valid ImplantSynergy> synergiesActivated) {
    this.synergiesActivated = synergiesActivated;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImplantInstallResult implantInstallResult = (ImplantInstallResult) o;
    return Objects.equals(this.success, implantInstallResult.success) &&
        Objects.equals(this.installedImplant, implantInstallResult.installedImplant) &&
        Objects.equals(this.humanityLost, implantInstallResult.humanityLost) &&
        Objects.equals(this.cyberpsychosisRisk, implantInstallResult.cyberpsychosisRisk) &&
        Objects.equals(this.statChanges, implantInstallResult.statChanges) &&
        Objects.equals(this.abilitiesUnlocked, implantInstallResult.abilitiesUnlocked) &&
        Objects.equals(this.synergiesActivated, implantInstallResult.synergiesActivated);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, installedImplant, humanityLost, cyberpsychosisRisk, statChanges, abilitiesUnlocked, synergiesActivated);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImplantInstallResult {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    installedImplant: ").append(toIndentedString(installedImplant)).append("\n");
    sb.append("    humanityLost: ").append(toIndentedString(humanityLost)).append("\n");
    sb.append("    cyberpsychosisRisk: ").append(toIndentedString(cyberpsychosisRisk)).append("\n");
    sb.append("    statChanges: ").append(toIndentedString(statChanges)).append("\n");
    sb.append("    abilitiesUnlocked: ").append(toIndentedString(abilitiesUnlocked)).append("\n");
    sb.append("    synergiesActivated: ").append(toIndentedString(synergiesActivated)).append("\n");
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

