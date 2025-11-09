package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * PurchaseImplantFromRipperdoc200Response
 */

@JsonTypeName("purchaseImplantFromRipperdoc_200_response")

public class PurchaseImplantFromRipperdoc200Response {

  private @Nullable Boolean success;

  private @Nullable String implantId;

  private @Nullable BigDecimal price;

  private @Nullable Boolean installed;

  public PurchaseImplantFromRipperdoc200Response success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public PurchaseImplantFromRipperdoc200Response implantId(@Nullable String implantId) {
    this.implantId = implantId;
    return this;
  }

  /**
   * Get implantId
   * @return implantId
   */
  
  @Schema(name = "implant_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("implant_id")
  public @Nullable String getImplantId() {
    return implantId;
  }

  public void setImplantId(@Nullable String implantId) {
    this.implantId = implantId;
  }

  public PurchaseImplantFromRipperdoc200Response price(@Nullable BigDecimal price) {
    this.price = price;
    return this;
  }

  /**
   * Get price
   * @return price
   */
  @Valid 
  @Schema(name = "price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("price")
  public @Nullable BigDecimal getPrice() {
    return price;
  }

  public void setPrice(@Nullable BigDecimal price) {
    this.price = price;
  }

  public PurchaseImplantFromRipperdoc200Response installed(@Nullable Boolean installed) {
    this.installed = installed;
    return this;
  }

  /**
   * Get installed
   * @return installed
   */
  
  @Schema(name = "installed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("installed")
  public @Nullable Boolean getInstalled() {
    return installed;
  }

  public void setInstalled(@Nullable Boolean installed) {
    this.installed = installed;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PurchaseImplantFromRipperdoc200Response purchaseImplantFromRipperdoc200Response = (PurchaseImplantFromRipperdoc200Response) o;
    return Objects.equals(this.success, purchaseImplantFromRipperdoc200Response.success) &&
        Objects.equals(this.implantId, purchaseImplantFromRipperdoc200Response.implantId) &&
        Objects.equals(this.price, purchaseImplantFromRipperdoc200Response.price) &&
        Objects.equals(this.installed, purchaseImplantFromRipperdoc200Response.installed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, implantId, price, installed);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PurchaseImplantFromRipperdoc200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    implantId: ").append(toIndentedString(implantId)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
    sb.append("    installed: ").append(toIndentedString(installed)).append("\n");
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

