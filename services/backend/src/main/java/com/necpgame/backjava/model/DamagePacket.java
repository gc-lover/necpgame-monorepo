package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * DamagePacket
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class DamagePacket {

  private @Nullable String sourceId;

  private @Nullable String targetId;

  private @Nullable BigDecimal baseDamage;

  private @Nullable BigDecimal critMultiplier;

  private @Nullable BigDecimal mitigation;

  private @Nullable BigDecimal shieldAbsorbed;

  private @Nullable BigDecimal finalDamage;

  @Valid
  private List<String> tags = new ArrayList<>();

  public DamagePacket sourceId(@Nullable String sourceId) {
    this.sourceId = sourceId;
    return this;
  }

  /**
   * Get sourceId
   * @return sourceId
   */
  
  @Schema(name = "sourceId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sourceId")
  public @Nullable String getSourceId() {
    return sourceId;
  }

  public void setSourceId(@Nullable String sourceId) {
    this.sourceId = sourceId;
  }

  public DamagePacket targetId(@Nullable String targetId) {
    this.targetId = targetId;
    return this;
  }

  /**
   * Get targetId
   * @return targetId
   */
  
  @Schema(name = "targetId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("targetId")
  public @Nullable String getTargetId() {
    return targetId;
  }

  public void setTargetId(@Nullable String targetId) {
    this.targetId = targetId;
  }

  public DamagePacket baseDamage(@Nullable BigDecimal baseDamage) {
    this.baseDamage = baseDamage;
    return this;
  }

  /**
   * Get baseDamage
   * @return baseDamage
   */
  @Valid 
  @Schema(name = "baseDamage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("baseDamage")
  public @Nullable BigDecimal getBaseDamage() {
    return baseDamage;
  }

  public void setBaseDamage(@Nullable BigDecimal baseDamage) {
    this.baseDamage = baseDamage;
  }

  public DamagePacket critMultiplier(@Nullable BigDecimal critMultiplier) {
    this.critMultiplier = critMultiplier;
    return this;
  }

  /**
   * Get critMultiplier
   * @return critMultiplier
   */
  @Valid 
  @Schema(name = "critMultiplier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("critMultiplier")
  public @Nullable BigDecimal getCritMultiplier() {
    return critMultiplier;
  }

  public void setCritMultiplier(@Nullable BigDecimal critMultiplier) {
    this.critMultiplier = critMultiplier;
  }

  public DamagePacket mitigation(@Nullable BigDecimal mitigation) {
    this.mitigation = mitigation;
    return this;
  }

  /**
   * Get mitigation
   * @return mitigation
   */
  @Valid 
  @Schema(name = "mitigation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mitigation")
  public @Nullable BigDecimal getMitigation() {
    return mitigation;
  }

  public void setMitigation(@Nullable BigDecimal mitigation) {
    this.mitigation = mitigation;
  }

  public DamagePacket shieldAbsorbed(@Nullable BigDecimal shieldAbsorbed) {
    this.shieldAbsorbed = shieldAbsorbed;
    return this;
  }

  /**
   * Get shieldAbsorbed
   * @return shieldAbsorbed
   */
  @Valid 
  @Schema(name = "shieldAbsorbed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("shieldAbsorbed")
  public @Nullable BigDecimal getShieldAbsorbed() {
    return shieldAbsorbed;
  }

  public void setShieldAbsorbed(@Nullable BigDecimal shieldAbsorbed) {
    this.shieldAbsorbed = shieldAbsorbed;
  }

  public DamagePacket finalDamage(@Nullable BigDecimal finalDamage) {
    this.finalDamage = finalDamage;
    return this;
  }

  /**
   * Get finalDamage
   * @return finalDamage
   */
  @Valid 
  @Schema(name = "finalDamage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("finalDamage")
  public @Nullable BigDecimal getFinalDamage() {
    return finalDamage;
  }

  public void setFinalDamage(@Nullable BigDecimal finalDamage) {
    this.finalDamage = finalDamage;
  }

  public DamagePacket tags(List<String> tags) {
    this.tags = tags;
    return this;
  }

  public DamagePacket addTagsItem(String tagsItem) {
    if (this.tags == null) {
      this.tags = new ArrayList<>();
    }
    this.tags.add(tagsItem);
    return this;
  }

  /**
   * Get tags
   * @return tags
   */
  
  @Schema(name = "tags", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tags")
  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DamagePacket damagePacket = (DamagePacket) o;
    return Objects.equals(this.sourceId, damagePacket.sourceId) &&
        Objects.equals(this.targetId, damagePacket.targetId) &&
        Objects.equals(this.baseDamage, damagePacket.baseDamage) &&
        Objects.equals(this.critMultiplier, damagePacket.critMultiplier) &&
        Objects.equals(this.mitigation, damagePacket.mitigation) &&
        Objects.equals(this.shieldAbsorbed, damagePacket.shieldAbsorbed) &&
        Objects.equals(this.finalDamage, damagePacket.finalDamage) &&
        Objects.equals(this.tags, damagePacket.tags);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sourceId, targetId, baseDamage, critMultiplier, mitigation, shieldAbsorbed, finalDamage, tags);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DamagePacket {\n");
    sb.append("    sourceId: ").append(toIndentedString(sourceId)).append("\n");
    sb.append("    targetId: ").append(toIndentedString(targetId)).append("\n");
    sb.append("    baseDamage: ").append(toIndentedString(baseDamage)).append("\n");
    sb.append("    critMultiplier: ").append(toIndentedString(critMultiplier)).append("\n");
    sb.append("    mitigation: ").append(toIndentedString(mitigation)).append("\n");
    sb.append("    shieldAbsorbed: ").append(toIndentedString(shieldAbsorbed)).append("\n");
    sb.append("    finalDamage: ").append(toIndentedString(finalDamage)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
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

