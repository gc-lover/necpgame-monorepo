package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.worldservice.model.EconomicModifier;
import com.necpgame.worldservice.model.OutcomeEffectReputationChangesInner;
import com.necpgame.worldservice.model.RewardDescriptor;
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
 * OutcomeEffect
 */


public class OutcomeEffect {

  @Valid
  private List<String> worldFlags = new ArrayList<>();

  @Valid
  private List<@Valid OutcomeEffectReputationChangesInner> reputationChanges = new ArrayList<>();

  @Valid
  private List<@Valid EconomicModifier> economicModifiers = new ArrayList<>();

  @Valid
  private List<@Valid RewardDescriptor> lootBundles = new ArrayList<>();

  public OutcomeEffect worldFlags(List<String> worldFlags) {
    this.worldFlags = worldFlags;
    return this;
  }

  public OutcomeEffect addWorldFlagsItem(String worldFlagsItem) {
    if (this.worldFlags == null) {
      this.worldFlags = new ArrayList<>();
    }
    this.worldFlags.add(worldFlagsItem);
    return this;
  }

  /**
   * Get worldFlags
   * @return worldFlags
   */
  
  @Schema(name = "worldFlags", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("worldFlags")
  public List<String> getWorldFlags() {
    return worldFlags;
  }

  public void setWorldFlags(List<String> worldFlags) {
    this.worldFlags = worldFlags;
  }

  public OutcomeEffect reputationChanges(List<@Valid OutcomeEffectReputationChangesInner> reputationChanges) {
    this.reputationChanges = reputationChanges;
    return this;
  }

  public OutcomeEffect addReputationChangesItem(OutcomeEffectReputationChangesInner reputationChangesItem) {
    if (this.reputationChanges == null) {
      this.reputationChanges = new ArrayList<>();
    }
    this.reputationChanges.add(reputationChangesItem);
    return this;
  }

  /**
   * Get reputationChanges
   * @return reputationChanges
   */
  @Valid 
  @Schema(name = "reputationChanges", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputationChanges")
  public List<@Valid OutcomeEffectReputationChangesInner> getReputationChanges() {
    return reputationChanges;
  }

  public void setReputationChanges(List<@Valid OutcomeEffectReputationChangesInner> reputationChanges) {
    this.reputationChanges = reputationChanges;
  }

  public OutcomeEffect economicModifiers(List<@Valid EconomicModifier> economicModifiers) {
    this.economicModifiers = economicModifiers;
    return this;
  }

  public OutcomeEffect addEconomicModifiersItem(EconomicModifier economicModifiersItem) {
    if (this.economicModifiers == null) {
      this.economicModifiers = new ArrayList<>();
    }
    this.economicModifiers.add(economicModifiersItem);
    return this;
  }

  /**
   * Get economicModifiers
   * @return economicModifiers
   */
  @Valid 
  @Schema(name = "economicModifiers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("economicModifiers")
  public List<@Valid EconomicModifier> getEconomicModifiers() {
    return economicModifiers;
  }

  public void setEconomicModifiers(List<@Valid EconomicModifier> economicModifiers) {
    this.economicModifiers = economicModifiers;
  }

  public OutcomeEffect lootBundles(List<@Valid RewardDescriptor> lootBundles) {
    this.lootBundles = lootBundles;
    return this;
  }

  public OutcomeEffect addLootBundlesItem(RewardDescriptor lootBundlesItem) {
    if (this.lootBundles == null) {
      this.lootBundles = new ArrayList<>();
    }
    this.lootBundles.add(lootBundlesItem);
    return this;
  }

  /**
   * Get lootBundles
   * @return lootBundles
   */
  @Valid 
  @Schema(name = "lootBundles", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lootBundles")
  public List<@Valid RewardDescriptor> getLootBundles() {
    return lootBundles;
  }

  public void setLootBundles(List<@Valid RewardDescriptor> lootBundles) {
    this.lootBundles = lootBundles;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OutcomeEffect outcomeEffect = (OutcomeEffect) o;
    return Objects.equals(this.worldFlags, outcomeEffect.worldFlags) &&
        Objects.equals(this.reputationChanges, outcomeEffect.reputationChanges) &&
        Objects.equals(this.economicModifiers, outcomeEffect.economicModifiers) &&
        Objects.equals(this.lootBundles, outcomeEffect.lootBundles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(worldFlags, reputationChanges, economicModifiers, lootBundles);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OutcomeEffect {\n");
    sb.append("    worldFlags: ").append(toIndentedString(worldFlags)).append("\n");
    sb.append("    reputationChanges: ").append(toIndentedString(reputationChanges)).append("\n");
    sb.append("    economicModifiers: ").append(toIndentedString(economicModifiers)).append("\n");
    sb.append("    lootBundles: ").append(toIndentedString(lootBundles)).append("\n");
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

