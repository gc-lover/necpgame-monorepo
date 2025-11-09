package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PurchaseImplantFromRipperdocRequest
 */

@JsonTypeName("purchaseImplantFromRipperdoc_request")

public class PurchaseImplantFromRipperdocRequest {

  private String characterId;

  private String ripperdocId;

  private String implantId;

  private Boolean installation = false;

  public PurchaseImplantFromRipperdocRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PurchaseImplantFromRipperdocRequest(String characterId, String ripperdocId, String implantId) {
    this.characterId = characterId;
    this.ripperdocId = ripperdocId;
    this.implantId = implantId;
  }

  public PurchaseImplantFromRipperdocRequest characterId(String characterId) {
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

  public PurchaseImplantFromRipperdocRequest ripperdocId(String ripperdocId) {
    this.ripperdocId = ripperdocId;
    return this;
  }

  /**
   * Get ripperdocId
   * @return ripperdocId
   */
  @NotNull 
  @Schema(name = "ripperdoc_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ripperdoc_id")
  public String getRipperdocId() {
    return ripperdocId;
  }

  public void setRipperdocId(String ripperdocId) {
    this.ripperdocId = ripperdocId;
  }

  public PurchaseImplantFromRipperdocRequest implantId(String implantId) {
    this.implantId = implantId;
    return this;
  }

  /**
   * Get implantId
   * @return implantId
   */
  @NotNull 
  @Schema(name = "implant_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("implant_id")
  public String getImplantId() {
    return implantId;
  }

  public void setImplantId(String implantId) {
    this.implantId = implantId;
  }

  public PurchaseImplantFromRipperdocRequest installation(Boolean installation) {
    this.installation = installation;
    return this;
  }

  /**
   * Установить сразу после покупки
   * @return installation
   */
  
  @Schema(name = "installation", description = "Установить сразу после покупки", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("installation")
  public Boolean getInstallation() {
    return installation;
  }

  public void setInstallation(Boolean installation) {
    this.installation = installation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PurchaseImplantFromRipperdocRequest purchaseImplantFromRipperdocRequest = (PurchaseImplantFromRipperdocRequest) o;
    return Objects.equals(this.characterId, purchaseImplantFromRipperdocRequest.characterId) &&
        Objects.equals(this.ripperdocId, purchaseImplantFromRipperdocRequest.ripperdocId) &&
        Objects.equals(this.implantId, purchaseImplantFromRipperdocRequest.implantId) &&
        Objects.equals(this.installation, purchaseImplantFromRipperdocRequest.installation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, ripperdocId, implantId, installation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PurchaseImplantFromRipperdocRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    ripperdocId: ").append(toIndentedString(ripperdocId)).append("\n");
    sb.append("    implantId: ").append(toIndentedString(implantId)).append("\n");
    sb.append("    installation: ").append(toIndentedString(installation)).append("\n");
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

