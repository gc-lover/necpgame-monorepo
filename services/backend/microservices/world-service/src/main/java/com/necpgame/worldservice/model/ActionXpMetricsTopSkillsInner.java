package com.necpgame.worldservice.model;

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
 * ActionXpMetricsTopSkillsInner
 */

@JsonTypeName("ActionXpMetrics_topSkills_inner")

public class ActionXpMetricsTopSkillsInner {

  private @Nullable String skillId;

  private @Nullable BigDecimal xp;

  private @Nullable BigDecimal fatigueOverflow;

  public ActionXpMetricsTopSkillsInner skillId(@Nullable String skillId) {
    this.skillId = skillId;
    return this;
  }

  /**
   * Get skillId
   * @return skillId
   */
  
  @Schema(name = "skillId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skillId")
  public @Nullable String getSkillId() {
    return skillId;
  }

  public void setSkillId(@Nullable String skillId) {
    this.skillId = skillId;
  }

  public ActionXpMetricsTopSkillsInner xp(@Nullable BigDecimal xp) {
    this.xp = xp;
    return this;
  }

  /**
   * Get xp
   * @return xp
   */
  @Valid 
  @Schema(name = "xp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("xp")
  public @Nullable BigDecimal getXp() {
    return xp;
  }

  public void setXp(@Nullable BigDecimal xp) {
    this.xp = xp;
  }

  public ActionXpMetricsTopSkillsInner fatigueOverflow(@Nullable BigDecimal fatigueOverflow) {
    this.fatigueOverflow = fatigueOverflow;
    return this;
  }

  /**
   * Get fatigueOverflow
   * @return fatigueOverflow
   */
  @Valid 
  @Schema(name = "fatigueOverflow", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fatigueOverflow")
  public @Nullable BigDecimal getFatigueOverflow() {
    return fatigueOverflow;
  }

  public void setFatigueOverflow(@Nullable BigDecimal fatigueOverflow) {
    this.fatigueOverflow = fatigueOverflow;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ActionXpMetricsTopSkillsInner actionXpMetricsTopSkillsInner = (ActionXpMetricsTopSkillsInner) o;
    return Objects.equals(this.skillId, actionXpMetricsTopSkillsInner.skillId) &&
        Objects.equals(this.xp, actionXpMetricsTopSkillsInner.xp) &&
        Objects.equals(this.fatigueOverflow, actionXpMetricsTopSkillsInner.fatigueOverflow);
  }

  @Override
  public int hashCode() {
    return Objects.hash(skillId, xp, fatigueOverflow);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActionXpMetricsTopSkillsInner {\n");
    sb.append("    skillId: ").append(toIndentedString(skillId)).append("\n");
    sb.append("    xp: ").append(toIndentedString(xp)).append("\n");
    sb.append("    fatigueOverflow: ").append(toIndentedString(fatigueOverflow)).append("\n");
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

