package com.necpgame.backjava.model;

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
 * LootGenerationContextLuckModifiers
 */

@JsonTypeName("LootGenerationContext_luckModifiers")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class LootGenerationContextLuckModifiers {

  private @Nullable Float personal;

  private @Nullable Float party;

  private @Nullable Float global;

  public LootGenerationContextLuckModifiers personal(@Nullable Float personal) {
    this.personal = personal;
    return this;
  }

  /**
   * Get personal
   * @return personal
   */
  
  @Schema(name = "personal", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("personal")
  public @Nullable Float getPersonal() {
    return personal;
  }

  public void setPersonal(@Nullable Float personal) {
    this.personal = personal;
  }

  public LootGenerationContextLuckModifiers party(@Nullable Float party) {
    this.party = party;
    return this;
  }

  /**
   * Get party
   * @return party
   */
  
  @Schema(name = "party", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("party")
  public @Nullable Float getParty() {
    return party;
  }

  public void setParty(@Nullable Float party) {
    this.party = party;
  }

  public LootGenerationContextLuckModifiers global(@Nullable Float global) {
    this.global = global;
    return this;
  }

  /**
   * Get global
   * @return global
   */
  
  @Schema(name = "global", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("global")
  public @Nullable Float getGlobal() {
    return global;
  }

  public void setGlobal(@Nullable Float global) {
    this.global = global;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootGenerationContextLuckModifiers lootGenerationContextLuckModifiers = (LootGenerationContextLuckModifiers) o;
    return Objects.equals(this.personal, lootGenerationContextLuckModifiers.personal) &&
        Objects.equals(this.party, lootGenerationContextLuckModifiers.party) &&
        Objects.equals(this.global, lootGenerationContextLuckModifiers.global);
  }

  @Override
  public int hashCode() {
    return Objects.hash(personal, party, global);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootGenerationContextLuckModifiers {\n");
    sb.append("    personal: ").append(toIndentedString(personal)).append("\n");
    sb.append("    party: ").append(toIndentedString(party)).append("\n");
    sb.append("    global: ").append(toIndentedString(global)).append("\n");
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

