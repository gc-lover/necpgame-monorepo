package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.HireableNPC;
import com.necpgame.backjava.model.HiredNPCContract;
import java.util.Arrays;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * HiredNPC
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class HiredNPC {

  private @Nullable UUID hireId;

  private @Nullable UUID characterId;

  private @Nullable HireableNPC npc;

  private @Nullable HiredNPCContract contract;

  private JsonNullable<String> currentAssignment = JsonNullable.<String>undefined();

  private @Nullable Integer loyalty;

  private @Nullable Float performanceRating;

  public HiredNPC hireId(@Nullable UUID hireId) {
    this.hireId = hireId;
    return this;
  }

  /**
   * Get hireId
   * @return hireId
   */
  @Valid 
  @Schema(name = "hire_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hire_id")
  public @Nullable UUID getHireId() {
    return hireId;
  }

  public void setHireId(@Nullable UUID hireId) {
    this.hireId = hireId;
  }

  public HiredNPC characterId(@Nullable UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @Valid 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable UUID characterId) {
    this.characterId = characterId;
  }

  public HiredNPC npc(@Nullable HireableNPC npc) {
    this.npc = npc;
    return this;
  }

  /**
   * Get npc
   * @return npc
   */
  @Valid 
  @Schema(name = "npc", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("npc")
  public @Nullable HireableNPC getNpc() {
    return npc;
  }

  public void setNpc(@Nullable HireableNPC npc) {
    this.npc = npc;
  }

  public HiredNPC contract(@Nullable HiredNPCContract contract) {
    this.contract = contract;
    return this;
  }

  /**
   * Get contract
   * @return contract
   */
  @Valid 
  @Schema(name = "contract", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("contract")
  public @Nullable HiredNPCContract getContract() {
    return contract;
  }

  public void setContract(@Nullable HiredNPCContract contract) {
    this.contract = contract;
  }

  public HiredNPC currentAssignment(String currentAssignment) {
    this.currentAssignment = JsonNullable.of(currentAssignment);
    return this;
  }

  /**
   * Get currentAssignment
   * @return currentAssignment
   */
  
  @Schema(name = "current_assignment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_assignment")
  public JsonNullable<String> getCurrentAssignment() {
    return currentAssignment;
  }

  public void setCurrentAssignment(JsonNullable<String> currentAssignment) {
    this.currentAssignment = currentAssignment;
  }

  public HiredNPC loyalty(@Nullable Integer loyalty) {
    this.loyalty = loyalty;
    return this;
  }

  /**
   * Get loyalty
   * @return loyalty
   */
  
  @Schema(name = "loyalty", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loyalty")
  public @Nullable Integer getLoyalty() {
    return loyalty;
  }

  public void setLoyalty(@Nullable Integer loyalty) {
    this.loyalty = loyalty;
  }

  public HiredNPC performanceRating(@Nullable Float performanceRating) {
    this.performanceRating = performanceRating;
    return this;
  }

  /**
   * Get performanceRating
   * @return performanceRating
   */
  
  @Schema(name = "performance_rating", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("performance_rating")
  public @Nullable Float getPerformanceRating() {
    return performanceRating;
  }

  public void setPerformanceRating(@Nullable Float performanceRating) {
    this.performanceRating = performanceRating;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HiredNPC hiredNPC = (HiredNPC) o;
    return Objects.equals(this.hireId, hiredNPC.hireId) &&
        Objects.equals(this.characterId, hiredNPC.characterId) &&
        Objects.equals(this.npc, hiredNPC.npc) &&
        Objects.equals(this.contract, hiredNPC.contract) &&
        equalsNullable(this.currentAssignment, hiredNPC.currentAssignment) &&
        Objects.equals(this.loyalty, hiredNPC.loyalty) &&
        Objects.equals(this.performanceRating, hiredNPC.performanceRating);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(hireId, characterId, npc, contract, hashCodeNullable(currentAssignment), loyalty, performanceRating);
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HiredNPC {\n");
    sb.append("    hireId: ").append(toIndentedString(hireId)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    npc: ").append(toIndentedString(npc)).append("\n");
    sb.append("    contract: ").append(toIndentedString(contract)).append("\n");
    sb.append("    currentAssignment: ").append(toIndentedString(currentAssignment)).append("\n");
    sb.append("    loyalty: ").append(toIndentedString(loyalty)).append("\n");
    sb.append("    performanceRating: ").append(toIndentedString(performanceRating)).append("\n");
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

