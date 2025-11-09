package com.necpgame.socialservice.model;

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
 * CreatePersonalNPC200Response
 */

@JsonTypeName("createPersonalNPC_200_response")

public class CreatePersonalNPC200Response {

  private @Nullable String npcId;

  private @Nullable BigDecimal costDaily;

  public CreatePersonalNPC200Response npcId(@Nullable String npcId) {
    this.npcId = npcId;
    return this;
  }

  /**
   * Get npcId
   * @return npcId
   */
  
  @Schema(name = "npc_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("npc_id")
  public @Nullable String getNpcId() {
    return npcId;
  }

  public void setNpcId(@Nullable String npcId) {
    this.npcId = npcId;
  }

  public CreatePersonalNPC200Response costDaily(@Nullable BigDecimal costDaily) {
    this.costDaily = costDaily;
    return this;
  }

  /**
   * Get costDaily
   * @return costDaily
   */
  @Valid 
  @Schema(name = "cost_daily", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cost_daily")
  public @Nullable BigDecimal getCostDaily() {
    return costDaily;
  }

  public void setCostDaily(@Nullable BigDecimal costDaily) {
    this.costDaily = costDaily;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreatePersonalNPC200Response createPersonalNPC200Response = (CreatePersonalNPC200Response) o;
    return Objects.equals(this.npcId, createPersonalNPC200Response.npcId) &&
        Objects.equals(this.costDaily, createPersonalNPC200Response.costDaily);
  }

  @Override
  public int hashCode() {
    return Objects.hash(npcId, costDaily);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreatePersonalNPC200Response {\n");
    sb.append("    npcId: ").append(toIndentedString(npcId)).append("\n");
    sb.append("    costDaily: ").append(toIndentedString(costDaily)).append("\n");
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

