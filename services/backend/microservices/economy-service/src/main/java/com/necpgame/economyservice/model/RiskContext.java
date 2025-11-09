package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.economyservice.model.RiskContextPlayerRatings;
import com.necpgame.economyservice.model.RiskIncident;
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
 * RiskContext
 */


public class RiskContext {

  /**
   * Gets or Sets orderType
   */
  public enum OrderTypeEnum {
    COMBAT("combat"),
    
    HACKER("hacker"),
    
    LOGISTICS("logistics"),
    
    ECONOMY("economy"),
    
    SOCIAL("social"),
    
    EXPLORATION("exploration");

    private final String value;

    OrderTypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static OrderTypeEnum fromValue(String value) {
      for (OrderTypeEnum b : OrderTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private OrderTypeEnum orderType;

  private Float complexityScore;

  private String regionId;

  private Integer deadlineHours;

  @Valid
  private List<String> requiredSkills = new ArrayList<>();

  @Valid
  private List<String> factionTags = new ArrayList<>();

  private @Nullable RiskContextPlayerRatings playerRatings;

  @Valid
  private List<@Valid RiskIncident> lastIncidents = new ArrayList<>();

  public RiskContext() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RiskContext(OrderTypeEnum orderType, Float complexityScore, String regionId, Integer deadlineHours) {
    this.orderType = orderType;
    this.complexityScore = complexityScore;
    this.regionId = regionId;
    this.deadlineHours = deadlineHours;
  }

  public RiskContext orderType(OrderTypeEnum orderType) {
    this.orderType = orderType;
    return this;
  }

  /**
   * Get orderType
   * @return orderType
   */
  @NotNull 
  @Schema(name = "orderType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("orderType")
  public OrderTypeEnum getOrderType() {
    return orderType;
  }

  public void setOrderType(OrderTypeEnum orderType) {
    this.orderType = orderType;
  }

  public RiskContext complexityScore(Float complexityScore) {
    this.complexityScore = complexityScore;
    return this;
  }

  /**
   * Get complexityScore
   * minimum: 0
   * @return complexityScore
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "complexityScore", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("complexityScore")
  public Float getComplexityScore() {
    return complexityScore;
  }

  public void setComplexityScore(Float complexityScore) {
    this.complexityScore = complexityScore;
  }

  public RiskContext regionId(String regionId) {
    this.regionId = regionId;
    return this;
  }

  /**
   * Get regionId
   * @return regionId
   */
  @NotNull @Pattern(regexp = "^[A-Z0-9\\\\-]{3,24}$") 
  @Schema(name = "regionId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("regionId")
  public String getRegionId() {
    return regionId;
  }

  public void setRegionId(String regionId) {
    this.regionId = regionId;
  }

  public RiskContext deadlineHours(Integer deadlineHours) {
    this.deadlineHours = deadlineHours;
    return this;
  }

  /**
   * Get deadlineHours
   * minimum: 1
   * @return deadlineHours
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "deadlineHours", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("deadlineHours")
  public Integer getDeadlineHours() {
    return deadlineHours;
  }

  public void setDeadlineHours(Integer deadlineHours) {
    this.deadlineHours = deadlineHours;
  }

  public RiskContext requiredSkills(List<String> requiredSkills) {
    this.requiredSkills = requiredSkills;
    return this;
  }

  public RiskContext addRequiredSkillsItem(String requiredSkillsItem) {
    if (this.requiredSkills == null) {
      this.requiredSkills = new ArrayList<>();
    }
    this.requiredSkills.add(requiredSkillsItem);
    return this;
  }

  /**
   * Get requiredSkills
   * @return requiredSkills
   */
  
  @Schema(name = "requiredSkills", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requiredSkills")
  public List<String> getRequiredSkills() {
    return requiredSkills;
  }

  public void setRequiredSkills(List<String> requiredSkills) {
    this.requiredSkills = requiredSkills;
  }

  public RiskContext factionTags(List<String> factionTags) {
    this.factionTags = factionTags;
    return this;
  }

  public RiskContext addFactionTagsItem(String factionTagsItem) {
    if (this.factionTags == null) {
      this.factionTags = new ArrayList<>();
    }
    this.factionTags.add(factionTagsItem);
    return this;
  }

  /**
   * Get factionTags
   * @return factionTags
   */
  
  @Schema(name = "factionTags", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("factionTags")
  public List<String> getFactionTags() {
    return factionTags;
  }

  public void setFactionTags(List<String> factionTags) {
    this.factionTags = factionTags;
  }

  public RiskContext playerRatings(@Nullable RiskContextPlayerRatings playerRatings) {
    this.playerRatings = playerRatings;
    return this;
  }

  /**
   * Get playerRatings
   * @return playerRatings
   */
  @Valid 
  @Schema(name = "playerRatings", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("playerRatings")
  public @Nullable RiskContextPlayerRatings getPlayerRatings() {
    return playerRatings;
  }

  public void setPlayerRatings(@Nullable RiskContextPlayerRatings playerRatings) {
    this.playerRatings = playerRatings;
  }

  public RiskContext lastIncidents(List<@Valid RiskIncident> lastIncidents) {
    this.lastIncidents = lastIncidents;
    return this;
  }

  public RiskContext addLastIncidentsItem(RiskIncident lastIncidentsItem) {
    if (this.lastIncidents == null) {
      this.lastIncidents = new ArrayList<>();
    }
    this.lastIncidents.add(lastIncidentsItem);
    return this;
  }

  /**
   * Get lastIncidents
   * @return lastIncidents
   */
  @Valid 
  @Schema(name = "lastIncidents", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastIncidents")
  public List<@Valid RiskIncident> getLastIncidents() {
    return lastIncidents;
  }

  public void setLastIncidents(List<@Valid RiskIncident> lastIncidents) {
    this.lastIncidents = lastIncidents;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RiskContext riskContext = (RiskContext) o;
    return Objects.equals(this.orderType, riskContext.orderType) &&
        Objects.equals(this.complexityScore, riskContext.complexityScore) &&
        Objects.equals(this.regionId, riskContext.regionId) &&
        Objects.equals(this.deadlineHours, riskContext.deadlineHours) &&
        Objects.equals(this.requiredSkills, riskContext.requiredSkills) &&
        Objects.equals(this.factionTags, riskContext.factionTags) &&
        Objects.equals(this.playerRatings, riskContext.playerRatings) &&
        Objects.equals(this.lastIncidents, riskContext.lastIncidents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderType, complexityScore, regionId, deadlineHours, requiredSkills, factionTags, playerRatings, lastIncidents);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RiskContext {\n");
    sb.append("    orderType: ").append(toIndentedString(orderType)).append("\n");
    sb.append("    complexityScore: ").append(toIndentedString(complexityScore)).append("\n");
    sb.append("    regionId: ").append(toIndentedString(regionId)).append("\n");
    sb.append("    deadlineHours: ").append(toIndentedString(deadlineHours)).append("\n");
    sb.append("    requiredSkills: ").append(toIndentedString(requiredSkills)).append("\n");
    sb.append("    factionTags: ").append(toIndentedString(factionTags)).append("\n");
    sb.append("    playerRatings: ").append(toIndentedString(playerRatings)).append("\n");
    sb.append("    lastIncidents: ").append(toIndentedString(lastIncidents)).append("\n");
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

