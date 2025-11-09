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
 * CreatePersonalNPCRequest
 */

@JsonTypeName("createPersonalNPC_request")

public class CreatePersonalNPCRequest {

  private String ownerId;

  /**
   * Gets or Sets ownerType
   */
  public enum OwnerTypeEnum {
    CHARACTER("character"),
    
    GUILD("guild"),
    
    ORGANIZATION("organization");

    private final String value;

    OwnerTypeEnum(String value) {
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
    public static OwnerTypeEnum fromValue(String value) {
      for (OwnerTypeEnum b : OwnerTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private OwnerTypeEnum ownerType;

  private String name;

  /**
   * Gets or Sets npcType
   */
  public enum NpcTypeEnum {
    HUMAN("human"),
    
    ROBOT("robot"),
    
    ANDROID("android");

    private final String value;

    NpcTypeEnum(String value) {
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
    public static NpcTypeEnum fromValue(String value) {
      for (NpcTypeEnum b : NpcTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private NpcTypeEnum npcType;

  /**
   * Gets or Sets role
   */
  public enum RoleEnum {
    GUARD("guard"),
    
    COURIER("courier"),
    
    MERCHANT("merchant"),
    
    INFORMANT("informant"),
    
    ENGINEER("engineer"),
    
    MEDIC("medic"),
    
    DIPLOMAT("diplomat"),
    
    FIXER("fixer"),
    
    QUEST_GIVER("quest_giver");

    private final String value;

    RoleEnum(String value) {
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
    public static RoleEnum fromValue(String value) {
      for (RoleEnum b : RoleEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private RoleEnum role;

  public CreatePersonalNPCRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreatePersonalNPCRequest(String ownerId, OwnerTypeEnum ownerType, String name, NpcTypeEnum npcType, RoleEnum role) {
    this.ownerId = ownerId;
    this.ownerType = ownerType;
    this.name = name;
    this.npcType = npcType;
    this.role = role;
  }

  public CreatePersonalNPCRequest ownerId(String ownerId) {
    this.ownerId = ownerId;
    return this;
  }

  /**
   * Get ownerId
   * @return ownerId
   */
  @NotNull 
  @Schema(name = "owner_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("owner_id")
  public String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  public CreatePersonalNPCRequest ownerType(OwnerTypeEnum ownerType) {
    this.ownerType = ownerType;
    return this;
  }

  /**
   * Get ownerType
   * @return ownerType
   */
  @NotNull 
  @Schema(name = "owner_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("owner_type")
  public OwnerTypeEnum getOwnerType() {
    return ownerType;
  }

  public void setOwnerType(OwnerTypeEnum ownerType) {
    this.ownerType = ownerType;
  }

  public CreatePersonalNPCRequest name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CreatePersonalNPCRequest npcType(NpcTypeEnum npcType) {
    this.npcType = npcType;
    return this;
  }

  /**
   * Get npcType
   * @return npcType
   */
  @NotNull 
  @Schema(name = "npc_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("npc_type")
  public NpcTypeEnum getNpcType() {
    return npcType;
  }

  public void setNpcType(NpcTypeEnum npcType) {
    this.npcType = npcType;
  }

  public CreatePersonalNPCRequest role(RoleEnum role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  @NotNull 
  @Schema(name = "role", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("role")
  public RoleEnum getRole() {
    return role;
  }

  public void setRole(RoleEnum role) {
    this.role = role;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreatePersonalNPCRequest createPersonalNPCRequest = (CreatePersonalNPCRequest) o;
    return Objects.equals(this.ownerId, createPersonalNPCRequest.ownerId) &&
        Objects.equals(this.ownerType, createPersonalNPCRequest.ownerType) &&
        Objects.equals(this.name, createPersonalNPCRequest.name) &&
        Objects.equals(this.npcType, createPersonalNPCRequest.npcType) &&
        Objects.equals(this.role, createPersonalNPCRequest.role);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ownerId, ownerType, name, npcType, role);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreatePersonalNPCRequest {\n");
    sb.append("    ownerId: ").append(toIndentedString(ownerId)).append("\n");
    sb.append("    ownerType: ").append(toIndentedString(ownerType)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    npcType: ").append(toIndentedString(npcType)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
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

