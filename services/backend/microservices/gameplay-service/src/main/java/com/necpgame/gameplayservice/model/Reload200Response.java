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
 * Reload200Response
 */

@JsonTypeName("reload_200_response")

public class Reload200Response {

  private @Nullable Boolean success;

  private @Nullable BigDecimal reloadTime;

  private @Nullable Integer ammoLoaded;

  public Reload200Response success(@Nullable Boolean success) {
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

  public Reload200Response reloadTime(@Nullable BigDecimal reloadTime) {
    this.reloadTime = reloadTime;
    return this;
  }

  /**
   * Время перезарядки в секундах
   * @return reloadTime
   */
  @Valid 
  @Schema(name = "reload_time", description = "Время перезарядки в секундах", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reload_time")
  public @Nullable BigDecimal getReloadTime() {
    return reloadTime;
  }

  public void setReloadTime(@Nullable BigDecimal reloadTime) {
    this.reloadTime = reloadTime;
  }

  public Reload200Response ammoLoaded(@Nullable Integer ammoLoaded) {
    this.ammoLoaded = ammoLoaded;
    return this;
  }

  /**
   * Количество патронов в магазине
   * @return ammoLoaded
   */
  
  @Schema(name = "ammo_loaded", description = "Количество патронов в магазине", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ammo_loaded")
  public @Nullable Integer getAmmoLoaded() {
    return ammoLoaded;
  }

  public void setAmmoLoaded(@Nullable Integer ammoLoaded) {
    this.ammoLoaded = ammoLoaded;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Reload200Response reload200Response = (Reload200Response) o;
    return Objects.equals(this.success, reload200Response.success) &&
        Objects.equals(this.reloadTime, reload200Response.reloadTime) &&
        Objects.equals(this.ammoLoaded, reload200Response.ammoLoaded);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, reloadTime, ammoLoaded);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Reload200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    reloadTime: ").append(toIndentedString(reloadTime)).append("\n");
    sb.append("    ammoLoaded: ").append(toIndentedString(ammoLoaded)).append("\n");
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

