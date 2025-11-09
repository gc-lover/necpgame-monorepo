package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * CraftingRecipeDetailedAllOfResultItemQualityRange
 */

@JsonTypeName("CraftingRecipeDetailed_allOf_result_item_quality_range")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class CraftingRecipeDetailedAllOfResultItemQualityRange {

  /**
   * Gets or Sets min
   */
  public enum MinEnum {
    POOR("POOR"),
    
    COMMON("COMMON"),
    
    UNCOMMON("UNCOMMON"),
    
    RARE("RARE"),
    
    EPIC("EPIC"),
    
    LEGENDARY("LEGENDARY");

    private final String value;

    MinEnum(String value) {
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
    public static MinEnum fromValue(String value) {
      for (MinEnum b : MinEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable MinEnum min;

  private @Nullable String max;

  public CraftingRecipeDetailedAllOfResultItemQualityRange min(@Nullable MinEnum min) {
    this.min = min;
    return this;
  }

  /**
   * Get min
   * @return min
   */
  
  @Schema(name = "min", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("min")
  public @Nullable MinEnum getMin() {
    return min;
  }

  public void setMin(@Nullable MinEnum min) {
    this.min = min;
  }

  public CraftingRecipeDetailedAllOfResultItemQualityRange max(@Nullable String max) {
    this.max = max;
    return this;
  }

  /**
   * Get max
   * @return max
   */
  
  @Schema(name = "max", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max")
  public @Nullable String getMax() {
    return max;
  }

  public void setMax(@Nullable String max) {
    this.max = max;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CraftingRecipeDetailedAllOfResultItemQualityRange craftingRecipeDetailedAllOfResultItemQualityRange = (CraftingRecipeDetailedAllOfResultItemQualityRange) o;
    return Objects.equals(this.min, craftingRecipeDetailedAllOfResultItemQualityRange.min) &&
        Objects.equals(this.max, craftingRecipeDetailedAllOfResultItemQualityRange.max);
  }

  @Override
  public int hashCode() {
    return Objects.hash(min, max);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CraftingRecipeDetailedAllOfResultItemQualityRange {\n");
    sb.append("    min: ").append(toIndentedString(min)).append("\n");
    sb.append("    max: ").append(toIndentedString(max)).append("\n");
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

