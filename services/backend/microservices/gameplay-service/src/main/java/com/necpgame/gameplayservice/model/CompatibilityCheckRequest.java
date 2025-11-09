package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * Запрос на проверку совместимости
 */

@Schema(name = "CompatibilityCheckRequest", description = "Запрос на проверку совместимости")

public class CompatibilityCheckRequest {

  private UUID implantId;

  private UUID targetSlot;

  public CompatibilityCheckRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CompatibilityCheckRequest(UUID implantId, UUID targetSlot) {
    this.implantId = implantId;
    this.targetSlot = targetSlot;
  }

  public CompatibilityCheckRequest implantId(UUID implantId) {
    this.implantId = implantId;
    return this;
  }

  /**
   * Идентификатор импланта для проверки
   * @return implantId
   */
  @NotNull @Valid 
  @Schema(name = "implant_id", description = "Идентификатор импланта для проверки", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("implant_id")
  public UUID getImplantId() {
    return implantId;
  }

  public void setImplantId(UUID implantId) {
    this.implantId = implantId;
  }

  public CompatibilityCheckRequest targetSlot(UUID targetSlot) {
    this.targetSlot = targetSlot;
    return this;
  }

  /**
   * Целевой слот для установки
   * @return targetSlot
   */
  @NotNull @Valid 
  @Schema(name = "target_slot", description = "Целевой слот для установки", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("target_slot")
  public UUID getTargetSlot() {
    return targetSlot;
  }

  public void setTargetSlot(UUID targetSlot) {
    this.targetSlot = targetSlot;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompatibilityCheckRequest compatibilityCheckRequest = (CompatibilityCheckRequest) o;
    return Objects.equals(this.implantId, compatibilityCheckRequest.implantId) &&
        Objects.equals(this.targetSlot, compatibilityCheckRequest.targetSlot);
  }

  @Override
  public int hashCode() {
    return Objects.hash(implantId, targetSlot);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompatibilityCheckRequest {\n");
    sb.append("    implantId: ").append(toIndentedString(implantId)).append("\n");
    sb.append("    targetSlot: ").append(toIndentedString(targetSlot)).append("\n");
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

