package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * UniverseLoreSimulationLore
 */

@JsonTypeName("UniverseLore_simulation_lore")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class UniverseLoreSimulationLore {

  private @Nullable Boolean isSimulation;

  @Valid
  private List<String> revelationStages = new ArrayList<>();

  public UniverseLoreSimulationLore isSimulation(@Nullable Boolean isSimulation) {
    this.isSimulation = isSimulation;
    return this;
  }

  /**
   * Get isSimulation
   * @return isSimulation
   */
  
  @Schema(name = "is_simulation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("is_simulation")
  public @Nullable Boolean getIsSimulation() {
    return isSimulation;
  }

  public void setIsSimulation(@Nullable Boolean isSimulation) {
    this.isSimulation = isSimulation;
  }

  public UniverseLoreSimulationLore revelationStages(List<String> revelationStages) {
    this.revelationStages = revelationStages;
    return this;
  }

  public UniverseLoreSimulationLore addRevelationStagesItem(String revelationStagesItem) {
    if (this.revelationStages == null) {
      this.revelationStages = new ArrayList<>();
    }
    this.revelationStages.add(revelationStagesItem);
    return this;
  }

  /**
   * Get revelationStages
   * @return revelationStages
   */
  
  @Schema(name = "revelation_stages", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("revelation_stages")
  public List<String> getRevelationStages() {
    return revelationStages;
  }

  public void setRevelationStages(List<String> revelationStages) {
    this.revelationStages = revelationStages;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UniverseLoreSimulationLore universeLoreSimulationLore = (UniverseLoreSimulationLore) o;
    return Objects.equals(this.isSimulation, universeLoreSimulationLore.isSimulation) &&
        Objects.equals(this.revelationStages, universeLoreSimulationLore.revelationStages);
  }

  @Override
  public int hashCode() {
    return Objects.hash(isSimulation, revelationStages);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UniverseLoreSimulationLore {\n");
    sb.append("    isSimulation: ").append(toIndentedString(isSimulation)).append("\n");
    sb.append("    revelationStages: ").append(toIndentedString(revelationStages)).append("\n");
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

