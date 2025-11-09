package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * PlayerOrderValidationRequest
 */


public class PlayerOrderValidationRequest {

  private UUID orderId;

  private UUID ownerId;

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

  /**
   * Gets or Sets privacyMode
   */
  public enum PrivacyModeEnum {
    PUBLIC("public"),
    
    INVITE("invite"),
    
    PRIVATE("private");

    private final String value;

    PrivacyModeEnum(String value) {
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
    public static PrivacyModeEnum fromValue(String value) {
      for (PrivacyModeEnum b : PrivacyModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable PrivacyModeEnum privacyMode;

  /**
   * Gets or Sets riskLevel
   */
  public enum RiskLevelEnum {
    LOW("low"),
    
    MEDIUM("medium"),
    
    HIGH("high"),
    
    SEVERE("severe"),
    
    EXTREME("extreme");

    private final String value;

    RiskLevelEnum(String value) {
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
    public static RiskLevelEnum fromValue(String value) {
      for (RiskLevelEnum b : RiskLevelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RiskLevelEnum riskLevel;

  private Float budget;

  @Valid
  private List<String> objectives = new ArrayList<>();

  @Valid
  private List<String> invitees = new ArrayList<>();

  @Valid
  private List<String> documents = new ArrayList<>();

  private @Nullable String locale;

  public PlayerOrderValidationRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderValidationRequest(UUID orderId, UUID ownerId, TemplateCodeEnum templateCode, String zoneId, Float budget, List<String> objectives) {
    this.orderId = orderId;
    this.ownerId = ownerId;
    this.templateCode = templateCode;
    this.zoneId = zoneId;
    this.budget = budget;
    this.objectives = objectives;
  }

  public PlayerOrderValidationRequest orderId(UUID orderId) {
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

  public PlayerOrderValidationRequest ownerId(UUID ownerId) {
    this.ownerId = ownerId;
    return this;
  }

  /**
   * Get ownerId
   * @return ownerId
   */
  @NotNull @Valid 
  @Schema(name = "ownerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ownerId")
  public UUID getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(UUID ownerId) {
    this.ownerId = ownerId;
  }

  public PlayerOrderValidationRequest templateCode(TemplateCodeEnum templateCode) {
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

  public PlayerOrderValidationRequest zoneId(String zoneId) {
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

  public PlayerOrderValidationRequest privacyMode(@Nullable PrivacyModeEnum privacyMode) {
    this.privacyMode = privacyMode;
    return this;
  }

  /**
   * Get privacyMode
   * @return privacyMode
   */
  
  @Schema(name = "privacyMode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("privacyMode")
  public @Nullable PrivacyModeEnum getPrivacyMode() {
    return privacyMode;
  }

  public void setPrivacyMode(@Nullable PrivacyModeEnum privacyMode) {
    this.privacyMode = privacyMode;
  }

  public PlayerOrderValidationRequest riskLevel(@Nullable RiskLevelEnum riskLevel) {
    this.riskLevel = riskLevel;
    return this;
  }

  /**
   * Get riskLevel
   * @return riskLevel
   */
  
  @Schema(name = "riskLevel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("riskLevel")
  public @Nullable RiskLevelEnum getRiskLevel() {
    return riskLevel;
  }

  public void setRiskLevel(@Nullable RiskLevelEnum riskLevel) {
    this.riskLevel = riskLevel;
  }

  public PlayerOrderValidationRequest budget(Float budget) {
    this.budget = budget;
    return this;
  }

  /**
   * Get budget
   * minimum: 0
   * @return budget
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "budget", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("budget")
  public Float getBudget() {
    return budget;
  }

  public void setBudget(Float budget) {
    this.budget = budget;
  }

  public PlayerOrderValidationRequest objectives(List<String> objectives) {
    this.objectives = objectives;
    return this;
  }

  public PlayerOrderValidationRequest addObjectivesItem(String objectivesItem) {
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
  @NotNull @Size(min = 1) 
  @Schema(name = "objectives", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("objectives")
  public List<String> getObjectives() {
    return objectives;
  }

  public void setObjectives(List<String> objectives) {
    this.objectives = objectives;
  }

  public PlayerOrderValidationRequest invitees(List<String> invitees) {
    this.invitees = invitees;
    return this;
  }

  public PlayerOrderValidationRequest addInviteesItem(String inviteesItem) {
    if (this.invitees == null) {
      this.invitees = new ArrayList<>();
    }
    this.invitees.add(inviteesItem);
    return this;
  }

  /**
   * Get invitees
   * @return invitees
   */
  
  @Schema(name = "invitees", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("invitees")
  public List<String> getInvitees() {
    return invitees;
  }

  public void setInvitees(List<String> invitees) {
    this.invitees = invitees;
  }

  public PlayerOrderValidationRequest documents(List<String> documents) {
    this.documents = documents;
    return this;
  }

  public PlayerOrderValidationRequest addDocumentsItem(String documentsItem) {
    if (this.documents == null) {
      this.documents = new ArrayList<>();
    }
    this.documents.add(documentsItem);
    return this;
  }

  /**
   * Get documents
   * @return documents
   */
  
  @Schema(name = "documents", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("documents")
  public List<String> getDocuments() {
    return documents;
  }

  public void setDocuments(List<String> documents) {
    this.documents = documents;
  }

  public PlayerOrderValidationRequest locale(@Nullable String locale) {
    this.locale = locale;
    return this;
  }

  /**
   * Get locale
   * @return locale
   */
  
  @Schema(name = "locale", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("locale")
  public @Nullable String getLocale() {
    return locale;
  }

  public void setLocale(@Nullable String locale) {
    this.locale = locale;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderValidationRequest playerOrderValidationRequest = (PlayerOrderValidationRequest) o;
    return Objects.equals(this.orderId, playerOrderValidationRequest.orderId) &&
        Objects.equals(this.ownerId, playerOrderValidationRequest.ownerId) &&
        Objects.equals(this.templateCode, playerOrderValidationRequest.templateCode) &&
        Objects.equals(this.zoneId, playerOrderValidationRequest.zoneId) &&
        Objects.equals(this.privacyMode, playerOrderValidationRequest.privacyMode) &&
        Objects.equals(this.riskLevel, playerOrderValidationRequest.riskLevel) &&
        Objects.equals(this.budget, playerOrderValidationRequest.budget) &&
        Objects.equals(this.objectives, playerOrderValidationRequest.objectives) &&
        Objects.equals(this.invitees, playerOrderValidationRequest.invitees) &&
        Objects.equals(this.documents, playerOrderValidationRequest.documents) &&
        Objects.equals(this.locale, playerOrderValidationRequest.locale);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, ownerId, templateCode, zoneId, privacyMode, riskLevel, budget, objectives, invitees, documents, locale);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderValidationRequest {\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    ownerId: ").append(toIndentedString(ownerId)).append("\n");
    sb.append("    templateCode: ").append(toIndentedString(templateCode)).append("\n");
    sb.append("    zoneId: ").append(toIndentedString(zoneId)).append("\n");
    sb.append("    privacyMode: ").append(toIndentedString(privacyMode)).append("\n");
    sb.append("    riskLevel: ").append(toIndentedString(riskLevel)).append("\n");
    sb.append("    budget: ").append(toIndentedString(budget)).append("\n");
    sb.append("    objectives: ").append(toIndentedString(objectives)).append("\n");
    sb.append("    invitees: ").append(toIndentedString(invitees)).append("\n");
    sb.append("    documents: ").append(toIndentedString(documents)).append("\n");
    sb.append("    locale: ").append(toIndentedString(locale)).append("\n");
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

