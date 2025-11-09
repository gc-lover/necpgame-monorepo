package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * HireNPCRequest
 */

@JsonTypeName("hireNPC_request")

public class HireNPCRequest {

  private String characterId;

  private String npcId;

  private Integer contractDuration;

  /**
   * Gets or Sets contractType
   */
  public enum ContractTypeEnum {
    TEMPORARY("temporary"),
    
    PERMANENT("permanent");

    private final String value;

    ContractTypeEnum(String value) {
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
    public static ContractTypeEnum fromValue(String value) {
      for (ContractTypeEnum b : ContractTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ContractTypeEnum contractType = ContractTypeEnum.TEMPORARY;

  public HireNPCRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public HireNPCRequest(String characterId, String npcId, Integer contractDuration) {
    this.characterId = characterId;
    this.npcId = npcId;
    this.contractDuration = contractDuration;
  }

  public HireNPCRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public HireNPCRequest npcId(String npcId) {
    this.npcId = npcId;
    return this;
  }

  /**
   * Get npcId
   * @return npcId
   */
  @NotNull 
  @Schema(name = "npc_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("npc_id")
  public String getNpcId() {
    return npcId;
  }

  public void setNpcId(String npcId) {
    this.npcId = npcId;
  }

  public HireNPCRequest contractDuration(Integer contractDuration) {
    this.contractDuration = contractDuration;
    return this;
  }

  /**
   * Длительность контракта (дни)
   * minimum: 1
   * @return contractDuration
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "contract_duration", description = "Длительность контракта (дни)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("contract_duration")
  public Integer getContractDuration() {
    return contractDuration;
  }

  public void setContractDuration(Integer contractDuration) {
    this.contractDuration = contractDuration;
  }

  public HireNPCRequest contractType(ContractTypeEnum contractType) {
    this.contractType = contractType;
    return this;
  }

  /**
   * Get contractType
   * @return contractType
   */
  
  @Schema(name = "contract_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("contract_type")
  public ContractTypeEnum getContractType() {
    return contractType;
  }

  public void setContractType(ContractTypeEnum contractType) {
    this.contractType = contractType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HireNPCRequest hireNPCRequest = (HireNPCRequest) o;
    return Objects.equals(this.characterId, hireNPCRequest.characterId) &&
        Objects.equals(this.npcId, hireNPCRequest.npcId) &&
        Objects.equals(this.contractDuration, hireNPCRequest.contractDuration) &&
        Objects.equals(this.contractType, hireNPCRequest.contractType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, npcId, contractDuration, contractType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HireNPCRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    npcId: ").append(toIndentedString(npcId)).append("\n");
    sb.append("    contractDuration: ").append(toIndentedString(contractDuration)).append("\n");
    sb.append("    contractType: ").append(toIndentedString(contractType)).append("\n");
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

