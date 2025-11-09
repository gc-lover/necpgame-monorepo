package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * PlayerOrderGuaranteeSelection
 */


public class PlayerOrderGuaranteeSelection {

  /**
   * Gets or Sets escrowPolicy
   */
  public enum EscrowPolicyEnum {
    STANDARD("standard"),
    
    EXTENDED("extended"),
    
    PREMIUM("premium");

    private final String value;

    EscrowPolicyEnum(String value) {
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
    public static EscrowPolicyEnum fromValue(String value) {
      for (EscrowPolicyEnum b : EscrowPolicyEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private EscrowPolicyEnum escrowPolicy;

  /**
   * Gets or Sets insuranceTier
   */
  public enum InsuranceTierEnum {
    NONE("none"),
    
    BASIC("basic"),
    
    ENHANCED("enhanced"),
    
    ULTIMATE("ultimate");

    private final String value;

    InsuranceTierEnum(String value) {
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
    public static InsuranceTierEnum fromValue(String value) {
      for (InsuranceTierEnum b : InsuranceTierEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable InsuranceTierEnum insuranceTier;

  private @Nullable Boolean reputationBond;

  private @Nullable BigDecimal performanceBonus;

  public PlayerOrderGuaranteeSelection() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderGuaranteeSelection(EscrowPolicyEnum escrowPolicy) {
    this.escrowPolicy = escrowPolicy;
  }

  public PlayerOrderGuaranteeSelection escrowPolicy(EscrowPolicyEnum escrowPolicy) {
    this.escrowPolicy = escrowPolicy;
    return this;
  }

  /**
   * Get escrowPolicy
   * @return escrowPolicy
   */
  @NotNull 
  @Schema(name = "escrowPolicy", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("escrowPolicy")
  public EscrowPolicyEnum getEscrowPolicy() {
    return escrowPolicy;
  }

  public void setEscrowPolicy(EscrowPolicyEnum escrowPolicy) {
    this.escrowPolicy = escrowPolicy;
  }

  public PlayerOrderGuaranteeSelection insuranceTier(@Nullable InsuranceTierEnum insuranceTier) {
    this.insuranceTier = insuranceTier;
    return this;
  }

  /**
   * Get insuranceTier
   * @return insuranceTier
   */
  
  @Schema(name = "insuranceTier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("insuranceTier")
  public @Nullable InsuranceTierEnum getInsuranceTier() {
    return insuranceTier;
  }

  public void setInsuranceTier(@Nullable InsuranceTierEnum insuranceTier) {
    this.insuranceTier = insuranceTier;
  }

  public PlayerOrderGuaranteeSelection reputationBond(@Nullable Boolean reputationBond) {
    this.reputationBond = reputationBond;
    return this;
  }

  /**
   * Требуется ли залог репутации исполнителей.
   * @return reputationBond
   */
  
  @Schema(name = "reputationBond", description = "Требуется ли залог репутации исполнителей.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputationBond")
  public @Nullable Boolean getReputationBond() {
    return reputationBond;
  }

  public void setReputationBond(@Nullable Boolean reputationBond) {
    this.reputationBond = reputationBond;
  }

  public PlayerOrderGuaranteeSelection performanceBonus(@Nullable BigDecimal performanceBonus) {
    this.performanceBonus = performanceBonus;
    return this;
  }

  /**
   * Дополнительный бонус за выполнение в срок.
   * @return performanceBonus
   */
  @Valid 
  @Schema(name = "performanceBonus", description = "Дополнительный бонус за выполнение в срок.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("performanceBonus")
  public @Nullable BigDecimal getPerformanceBonus() {
    return performanceBonus;
  }

  public void setPerformanceBonus(@Nullable BigDecimal performanceBonus) {
    this.performanceBonus = performanceBonus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderGuaranteeSelection playerOrderGuaranteeSelection = (PlayerOrderGuaranteeSelection) o;
    return Objects.equals(this.escrowPolicy, playerOrderGuaranteeSelection.escrowPolicy) &&
        Objects.equals(this.insuranceTier, playerOrderGuaranteeSelection.insuranceTier) &&
        Objects.equals(this.reputationBond, playerOrderGuaranteeSelection.reputationBond) &&
        Objects.equals(this.performanceBonus, playerOrderGuaranteeSelection.performanceBonus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(escrowPolicy, insuranceTier, reputationBond, performanceBonus);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderGuaranteeSelection {\n");
    sb.append("    escrowPolicy: ").append(toIndentedString(escrowPolicy)).append("\n");
    sb.append("    insuranceTier: ").append(toIndentedString(insuranceTier)).append("\n");
    sb.append("    reputationBond: ").append(toIndentedString(reputationBond)).append("\n");
    sb.append("    performanceBonus: ").append(toIndentedString(performanceBonus)).append("\n");
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

