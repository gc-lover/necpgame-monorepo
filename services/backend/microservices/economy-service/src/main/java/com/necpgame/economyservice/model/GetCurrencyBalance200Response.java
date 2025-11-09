package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.GetCurrencyBalance200ResponseBalancesInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetCurrencyBalance200Response
 */

@JsonTypeName("getCurrencyBalance_200_response")

public class GetCurrencyBalance200Response {

  private @Nullable String characterId;

  @Valid
  private List<@Valid GetCurrencyBalance200ResponseBalancesInner> balances = new ArrayList<>();

  public GetCurrencyBalance200Response characterId(@Nullable String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable String characterId) {
    this.characterId = characterId;
  }

  public GetCurrencyBalance200Response balances(List<@Valid GetCurrencyBalance200ResponseBalancesInner> balances) {
    this.balances = balances;
    return this;
  }

  public GetCurrencyBalance200Response addBalancesItem(GetCurrencyBalance200ResponseBalancesInner balancesItem) {
    if (this.balances == null) {
      this.balances = new ArrayList<>();
    }
    this.balances.add(balancesItem);
    return this;
  }

  /**
   * Get balances
   * @return balances
   */
  @Valid 
  @Schema(name = "balances", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("balances")
  public List<@Valid GetCurrencyBalance200ResponseBalancesInner> getBalances() {
    return balances;
  }

  public void setBalances(List<@Valid GetCurrencyBalance200ResponseBalancesInner> balances) {
    this.balances = balances;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetCurrencyBalance200Response getCurrencyBalance200Response = (GetCurrencyBalance200Response) o;
    return Objects.equals(this.characterId, getCurrencyBalance200Response.characterId) &&
        Objects.equals(this.balances, getCurrencyBalance200Response.balances);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, balances);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetCurrencyBalance200Response {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    balances: ").append(toIndentedString(balances)).append("\n");
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

