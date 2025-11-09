package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * MitigationAction
 */


public class MitigationAction {

  private UUID actionId;

  private String title;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    PENDING("pending"),
    
    SCHEDULED("scheduled"),
    
    EXECUTING("executing"),
    
    COMPLETED("completed"),
    
    FAILED("failed");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private StatusEnum status;

  private @Nullable String responsible;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime eta;

  private @Nullable Float impactReduction;

  public MitigationAction() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MitigationAction(UUID actionId, String title, StatusEnum status) {
    this.actionId = actionId;
    this.title = title;
    this.status = status;
  }

  public MitigationAction actionId(UUID actionId) {
    this.actionId = actionId;
    return this;
  }

  /**
   * Get actionId
   * @return actionId
   */
  @NotNull @Valid 
  @Schema(name = "actionId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("actionId")
  public UUID getActionId() {
    return actionId;
  }

  public void setActionId(UUID actionId) {
    this.actionId = actionId;
  }

  public MitigationAction title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   */
  @NotNull 
  @Schema(name = "title", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public MitigationAction status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public MitigationAction responsible(@Nullable String responsible) {
    this.responsible = responsible;
    return this;
  }

  /**
   * Get responsible
   * @return responsible
   */
  
  @Schema(name = "responsible", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("responsible")
  public @Nullable String getResponsible() {
    return responsible;
  }

  public void setResponsible(@Nullable String responsible) {
    this.responsible = responsible;
  }

  public MitigationAction eta(@Nullable OffsetDateTime eta) {
    this.eta = eta;
    return this;
  }

  /**
   * Get eta
   * @return eta
   */
  @Valid 
  @Schema(name = "eta", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("eta")
  public @Nullable OffsetDateTime getEta() {
    return eta;
  }

  public void setEta(@Nullable OffsetDateTime eta) {
    this.eta = eta;
  }

  public MitigationAction impactReduction(@Nullable Float impactReduction) {
    this.impactReduction = impactReduction;
    return this;
  }

  /**
   * Ожидаемое снижение индекса кризиса.
   * @return impactReduction
   */
  
  @Schema(name = "impactReduction", description = "Ожидаемое снижение индекса кризиса.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("impactReduction")
  public @Nullable Float getImpactReduction() {
    return impactReduction;
  }

  public void setImpactReduction(@Nullable Float impactReduction) {
    this.impactReduction = impactReduction;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MitigationAction mitigationAction = (MitigationAction) o;
    return Objects.equals(this.actionId, mitigationAction.actionId) &&
        Objects.equals(this.title, mitigationAction.title) &&
        Objects.equals(this.status, mitigationAction.status) &&
        Objects.equals(this.responsible, mitigationAction.responsible) &&
        Objects.equals(this.eta, mitigationAction.eta) &&
        Objects.equals(this.impactReduction, mitigationAction.impactReduction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(actionId, title, status, responsible, eta, impactReduction);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MitigationAction {\n");
    sb.append("    actionId: ").append(toIndentedString(actionId)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    responsible: ").append(toIndentedString(responsible)).append("\n");
    sb.append("    eta: ").append(toIndentedString(eta)).append("\n");
    sb.append("    impactReduction: ").append(toIndentedString(impactReduction)).append("\n");
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

