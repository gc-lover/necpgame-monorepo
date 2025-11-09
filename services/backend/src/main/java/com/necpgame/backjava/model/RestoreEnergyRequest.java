package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * Р—Р°РїСЂРѕСЃ РЅР° РІРѕСЃСЃС‚Р°РЅРѕРІР»РµРЅРёРµ СЌРЅРµСЂРіРёРё
 */

@Schema(name = "RestoreEnergyRequest", description = "Р—Р°РїСЂРѕСЃ РЅР° РІРѕСЃСЃС‚Р°РЅРѕРІР»РµРЅРёРµ СЌРЅРµСЂРіРёРё")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:51:47.912860600+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class RestoreEnergyRequest {

  private Float amount;

  /**
   * РњРµС‚РѕРґ РІРѕСЃСЃС‚Р°РЅРѕРІР»РµРЅРёСЏ СЌРЅРµСЂРіРёРё
   */
  public enum MethodEnum {
    NATURAL("natural"),
    
    CONSUMABLE("consumable"),
    
    ABILITY("ability"),
    
    OTHER("other");

    private final String value;

    MethodEnum(String value) {
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
    public static MethodEnum fromValue(String value) {
      for (MethodEnum b : MethodEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private MethodEnum method;

  public RestoreEnergyRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RestoreEnergyRequest(Float amount, MethodEnum method) {
    this.amount = amount;
    this.method = method;
  }

  public RestoreEnergyRequest amount(Float amount) {
    this.amount = amount;
    return this;
  }

  /**
   * РљРѕР»РёС‡РµСЃС‚РІРѕ СЌРЅРµСЂРіРёРё РґР»СЏ РІРѕСЃСЃС‚Р°РЅРѕРІР»РµРЅРёСЏ
   * minimum: 0
   * @return amount
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "amount", description = "РљРѕР»РёС‡РµСЃС‚РІРѕ СЌРЅРµСЂРіРёРё РґР»СЏ РІРѕСЃСЃС‚Р°РЅРѕРІР»РµРЅРёСЏ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("amount")
  public Float getAmount() {
    return amount;
  }

  public void setAmount(Float amount) {
    this.amount = amount;
  }

  public RestoreEnergyRequest method(MethodEnum method) {
    this.method = method;
    return this;
  }

  /**
   * РњРµС‚РѕРґ РІРѕСЃСЃС‚Р°РЅРѕРІР»РµРЅРёСЏ СЌРЅРµСЂРіРёРё
   * @return method
   */
  @NotNull 
  @Schema(name = "method", description = "РњРµС‚РѕРґ РІРѕСЃСЃС‚Р°РЅРѕРІР»РµРЅРёСЏ СЌРЅРµСЂРіРёРё", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("method")
  public MethodEnum getMethod() {
    return method;
  }

  public void setMethod(MethodEnum method) {
    this.method = method;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RestoreEnergyRequest restoreEnergyRequest = (RestoreEnergyRequest) o;
    return Objects.equals(this.amount, restoreEnergyRequest.amount) &&
        Objects.equals(this.method, restoreEnergyRequest.method);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount, method);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RestoreEnergyRequest {\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    method: ").append(toIndentedString(method)).append("\n");
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

