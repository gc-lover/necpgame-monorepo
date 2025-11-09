package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.TradingQuota;
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
 * GetGuildQuotas200Response
 */

@JsonTypeName("getGuildQuotas_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetGuildQuotas200Response {

  @Valid
  private List<@Valid TradingQuota> quotas = new ArrayList<>();

  public GetGuildQuotas200Response quotas(List<@Valid TradingQuota> quotas) {
    this.quotas = quotas;
    return this;
  }

  public GetGuildQuotas200Response addQuotasItem(TradingQuota quotasItem) {
    if (this.quotas == null) {
      this.quotas = new ArrayList<>();
    }
    this.quotas.add(quotasItem);
    return this;
  }

  /**
   * Get quotas
   * @return quotas
   */
  @Valid 
  @Schema(name = "quotas", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quotas")
  public List<@Valid TradingQuota> getQuotas() {
    return quotas;
  }

  public void setQuotas(List<@Valid TradingQuota> quotas) {
    this.quotas = quotas;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetGuildQuotas200Response getGuildQuotas200Response = (GetGuildQuotas200Response) o;
    return Objects.equals(this.quotas, getGuildQuotas200Response.quotas);
  }

  @Override
  public int hashCode() {
    return Objects.hash(quotas);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetGuildQuotas200Response {\n");
    sb.append("    quotas: ").append(toIndentedString(quotas)).append("\n");
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

