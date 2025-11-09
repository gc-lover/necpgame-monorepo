package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ValidatePlayerOrderDuplicatesRequest
 */

@JsonTypeName("validatePlayerOrderDuplicates_request")

public class ValidatePlayerOrderDuplicatesRequest {

  private UUID orderId;

  /**
   * Gets or Sets templateCode
   */
  public enum TemplateCodeEnum {
    COMBAT("combat"),
    
    HACKER("hacker"),
    
    ECONOMY("economy"),
    
    SOCIAL("social"),
    
    EXPLORATION("exploration");

    private final String value;

    TemplateCodeEnum(String value) {
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
    public static TemplateCodeEnum fromValue(String value) {
      for (TemplateCodeEnum b : TemplateCodeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TemplateCodeEnum templateCode;

  private String zoneId;

  @Valid
  private List<String> objectives = new ArrayList<>();

  public ValidatePlayerOrderDuplicatesRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ValidatePlayerOrderDuplicatesRequest(UUID orderId, TemplateCodeEnum templateCode, String zoneId) {
    this.orderId = orderId;
    this.templateCode = templateCode;
    this.zoneId = zoneId;
  }

  public ValidatePlayerOrderDuplicatesRequest orderId(UUID orderId) {
    this.orderId = orderId;
    return this;
  }

  /**
   * Get orderId
   * @return orderId
   */
  @NotNull @Valid 
  @Schema(name = "orderId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("orderId")
  public UUID getOrderId() {
    return orderId;
  }

  public void setOrderId(UUID orderId) {
    this.orderId = orderId;
  }

  public ValidatePlayerOrderDuplicatesRequest templateCode(TemplateCodeEnum templateCode) {
    this.templateCode = templateCode;
    return this;
  }

  /**
   * Get templateCode
   * @return templateCode
   */
  @NotNull 
  @Schema(name = "templateCode", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("templateCode")
  public TemplateCodeEnum getTemplateCode() {
    return templateCode;
  }

  public void setTemplateCode(TemplateCodeEnum templateCode) {
    this.templateCode = templateCode;
  }

  public ValidatePlayerOrderDuplicatesRequest zoneId(String zoneId) {
    this.zoneId = zoneId;
    return this;
  }

  /**
   * Get zoneId
   * @return zoneId
   */
  @NotNull 
  @Schema(name = "zoneId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("zoneId")
  public String getZoneId() {
    return zoneId;
  }

  public void setZoneId(String zoneId) {
    this.zoneId = zoneId;
  }

  public ValidatePlayerOrderDuplicatesRequest objectives(List<String> objectives) {
    this.objectives = objectives;
    return this;
  }

  public ValidatePlayerOrderDuplicatesRequest addObjectivesItem(String objectivesItem) {
    if (this.objectives == null) {
      this.objectives = new ArrayList<>();
    }
    this.objectives.add(objectivesItem);
    return this;
  }

  /**
   * Get objectives
   * @return objectives
   */
  
  @Schema(name = "objectives", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("objectives")
  public List<String> getObjectives() {
    return objectives;
  }

  public void setObjectives(List<String> objectives) {
    this.objectives = objectives;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ValidatePlayerOrderDuplicatesRequest validatePlayerOrderDuplicatesRequest = (ValidatePlayerOrderDuplicatesRequest) o;
    return Objects.equals(this.orderId, validatePlayerOrderDuplicatesRequest.orderId) &&
        Objects.equals(this.templateCode, validatePlayerOrderDuplicatesRequest.templateCode) &&
        Objects.equals(this.zoneId, validatePlayerOrderDuplicatesRequest.zoneId) &&
        Objects.equals(this.objectives, validatePlayerOrderDuplicatesRequest.objectives);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, templateCode, zoneId, objectives);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ValidatePlayerOrderDuplicatesRequest {\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    templateCode: ").append(toIndentedString(templateCode)).append("\n");
    sb.append("    zoneId: ").append(toIndentedString(zoneId)).append("\n");
    sb.append("    objectives: ").append(toIndentedString(objectives)).append("\n");
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

