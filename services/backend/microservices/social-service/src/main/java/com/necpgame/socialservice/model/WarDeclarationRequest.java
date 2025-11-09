package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.WarDeclarationRequestPayment;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * WarDeclarationRequest
 */


public class WarDeclarationRequest {

  private String attackerClanId;

  private String defenderClanId;

  @Valid
  private List<String> targetTerritories = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime proposedStart;

  @Valid
  private List<String> allies = new ArrayList<>();

  private @Nullable WarDeclarationRequestPayment payment;

  private @Nullable String justification;

  public WarDeclarationRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public WarDeclarationRequest(String attackerClanId, String defenderClanId, List<String> targetTerritories, OffsetDateTime proposedStart) {
    this.attackerClanId = attackerClanId;
    this.defenderClanId = defenderClanId;
    this.targetTerritories = targetTerritories;
    this.proposedStart = proposedStart;
  }

  public WarDeclarationRequest attackerClanId(String attackerClanId) {
    this.attackerClanId = attackerClanId;
    return this;
  }

  /**
   * Get attackerClanId
   * @return attackerClanId
   */
  @NotNull 
  @Schema(name = "attackerClanId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("attackerClanId")
  public String getAttackerClanId() {
    return attackerClanId;
  }

  public void setAttackerClanId(String attackerClanId) {
    this.attackerClanId = attackerClanId;
  }

  public WarDeclarationRequest defenderClanId(String defenderClanId) {
    this.defenderClanId = defenderClanId;
    return this;
  }

  /**
   * Get defenderClanId
   * @return defenderClanId
   */
  @NotNull 
  @Schema(name = "defenderClanId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("defenderClanId")
  public String getDefenderClanId() {
    return defenderClanId;
  }

  public void setDefenderClanId(String defenderClanId) {
    this.defenderClanId = defenderClanId;
  }

  public WarDeclarationRequest targetTerritories(List<String> targetTerritories) {
    this.targetTerritories = targetTerritories;
    return this;
  }

  public WarDeclarationRequest addTargetTerritoriesItem(String targetTerritoriesItem) {
    if (this.targetTerritories == null) {
      this.targetTerritories = new ArrayList<>();
    }
    this.targetTerritories.add(targetTerritoriesItem);
    return this;
  }

  /**
   * Get targetTerritories
   * @return targetTerritories
   */
  @NotNull @Size(min = 1) 
  @Schema(name = "targetTerritories", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("targetTerritories")
  public List<String> getTargetTerritories() {
    return targetTerritories;
  }

  public void setTargetTerritories(List<String> targetTerritories) {
    this.targetTerritories = targetTerritories;
  }

  public WarDeclarationRequest proposedStart(OffsetDateTime proposedStart) {
    this.proposedStart = proposedStart;
    return this;
  }

  /**
   * Get proposedStart
   * @return proposedStart
   */
  @NotNull @Valid 
  @Schema(name = "proposedStart", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("proposedStart")
  public OffsetDateTime getProposedStart() {
    return proposedStart;
  }

  public void setProposedStart(OffsetDateTime proposedStart) {
    this.proposedStart = proposedStart;
  }

  public WarDeclarationRequest allies(List<String> allies) {
    this.allies = allies;
    return this;
  }

  public WarDeclarationRequest addAlliesItem(String alliesItem) {
    if (this.allies == null) {
      this.allies = new ArrayList<>();
    }
    this.allies.add(alliesItem);
    return this;
  }

  /**
   * Get allies
   * @return allies
   */
  
  @Schema(name = "allies", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("allies")
  public List<String> getAllies() {
    return allies;
  }

  public void setAllies(List<String> allies) {
    this.allies = allies;
  }

  public WarDeclarationRequest payment(@Nullable WarDeclarationRequestPayment payment) {
    this.payment = payment;
    return this;
  }

  /**
   * Get payment
   * @return payment
   */
  @Valid 
  @Schema(name = "payment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("payment")
  public @Nullable WarDeclarationRequestPayment getPayment() {
    return payment;
  }

  public void setPayment(@Nullable WarDeclarationRequestPayment payment) {
    this.payment = payment;
  }

  public WarDeclarationRequest justification(@Nullable String justification) {
    this.justification = justification;
    return this;
  }

  /**
   * Get justification
   * @return justification
   */
  @Size(max = 500) 
  @Schema(name = "justification", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("justification")
  public @Nullable String getJustification() {
    return justification;
  }

  public void setJustification(@Nullable String justification) {
    this.justification = justification;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WarDeclarationRequest warDeclarationRequest = (WarDeclarationRequest) o;
    return Objects.equals(this.attackerClanId, warDeclarationRequest.attackerClanId) &&
        Objects.equals(this.defenderClanId, warDeclarationRequest.defenderClanId) &&
        Objects.equals(this.targetTerritories, warDeclarationRequest.targetTerritories) &&
        Objects.equals(this.proposedStart, warDeclarationRequest.proposedStart) &&
        Objects.equals(this.allies, warDeclarationRequest.allies) &&
        Objects.equals(this.payment, warDeclarationRequest.payment) &&
        Objects.equals(this.justification, warDeclarationRequest.justification);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attackerClanId, defenderClanId, targetTerritories, proposedStart, allies, payment, justification);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WarDeclarationRequest {\n");
    sb.append("    attackerClanId: ").append(toIndentedString(attackerClanId)).append("\n");
    sb.append("    defenderClanId: ").append(toIndentedString(defenderClanId)).append("\n");
    sb.append("    targetTerritories: ").append(toIndentedString(targetTerritories)).append("\n");
    sb.append("    proposedStart: ").append(toIndentedString(proposedStart)).append("\n");
    sb.append("    allies: ").append(toIndentedString(allies)).append("\n");
    sb.append("    payment: ").append(toIndentedString(payment)).append("\n");
    sb.append("    justification: ").append(toIndentedString(justification)).append("\n");
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

