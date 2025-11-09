package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * EconomicModifier
 */


public class EconomicModifier {

  private String modifierId;

  /**
   * Gets or Sets scope
   */
  public enum ScopeEnum {
    GLOBAL("GLOBAL"),
    
    REGION("REGION"),
    
    CITY("CITY"),
    
    GUILD("GUILD");

    private final String value;

    ScopeEnum(String value) {
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
    public static ScopeEnum fromValue(String value) {
      for (ScopeEnum b : ScopeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ScopeEnum scope;

  private Float value;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    DISCOUNT("DISCOUNT"),
    
    SURCHARGE("SURCHARGE"),
    
    TAX("TAX"),
    
    BONUS_CREDIT("BONUS_CREDIT");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TypeEnum type;

  private @Nullable Integer durationMinutes;

  @Valid
  private List<String> affectedCommodities = new ArrayList<>();

  @Valid
  private Map<String, Object> metadata = new HashMap<>();

  public EconomicModifier() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EconomicModifier(String modifierId, ScopeEnum scope, Float value) {
    this.modifierId = modifierId;
    this.scope = scope;
    this.value = value;
  }

  public EconomicModifier modifierId(String modifierId) {
    this.modifierId = modifierId;
    return this;
  }

  /**
   * Get modifierId
   * @return modifierId
   */
  @NotNull 
  @Schema(name = "modifierId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("modifierId")
  public String getModifierId() {
    return modifierId;
  }

  public void setModifierId(String modifierId) {
    this.modifierId = modifierId;
  }

  public EconomicModifier scope(ScopeEnum scope) {
    this.scope = scope;
    return this;
  }

  /**
   * Get scope
   * @return scope
   */
  @NotNull 
  @Schema(name = "scope", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("scope")
  public ScopeEnum getScope() {
    return scope;
  }

  public void setScope(ScopeEnum scope) {
    this.scope = scope;
  }

  public EconomicModifier value(Float value) {
    this.value = value;
    return this;
  }

  /**
   * Get value
   * @return value
   */
  @NotNull 
  @Schema(name = "value", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("value")
  public Float getValue() {
    return value;
  }

  public void setValue(Float value) {
    this.value = value;
  }

  public EconomicModifier type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public EconomicModifier durationMinutes(@Nullable Integer durationMinutes) {
    this.durationMinutes = durationMinutes;
    return this;
  }

  /**
   * Get durationMinutes
   * minimum: 5
   * @return durationMinutes
   */
  @Min(value = 5) 
  @Schema(name = "durationMinutes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("durationMinutes")
  public @Nullable Integer getDurationMinutes() {
    return durationMinutes;
  }

  public void setDurationMinutes(@Nullable Integer durationMinutes) {
    this.durationMinutes = durationMinutes;
  }

  public EconomicModifier affectedCommodities(List<String> affectedCommodities) {
    this.affectedCommodities = affectedCommodities;
    return this;
  }

  public EconomicModifier addAffectedCommoditiesItem(String affectedCommoditiesItem) {
    if (this.affectedCommodities == null) {
      this.affectedCommodities = new ArrayList<>();
    }
    this.affectedCommodities.add(affectedCommoditiesItem);
    return this;
  }

  /**
   * Get affectedCommodities
   * @return affectedCommodities
   */
  
  @Schema(name = "affectedCommodities", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affectedCommodities")
  public List<String> getAffectedCommodities() {
    return affectedCommodities;
  }

  public void setAffectedCommodities(List<String> affectedCommodities) {
    this.affectedCommodities = affectedCommodities;
  }

  public EconomicModifier metadata(Map<String, Object> metadata) {
    this.metadata = metadata;
    return this;
  }

  public EconomicModifier putMetadataItem(String key, Object metadataItem) {
    if (this.metadata == null) {
      this.metadata = new HashMap<>();
    }
    this.metadata.put(key, metadataItem);
    return this;
  }

  /**
   * Get metadata
   * @return metadata
   */
  
  @Schema(name = "metadata", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metadata")
  public Map<String, Object> getMetadata() {
    return metadata;
  }

  public void setMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EconomicModifier economicModifier = (EconomicModifier) o;
    return Objects.equals(this.modifierId, economicModifier.modifierId) &&
        Objects.equals(this.scope, economicModifier.scope) &&
        Objects.equals(this.value, economicModifier.value) &&
        Objects.equals(this.type, economicModifier.type) &&
        Objects.equals(this.durationMinutes, economicModifier.durationMinutes) &&
        Objects.equals(this.affectedCommodities, economicModifier.affectedCommodities) &&
        Objects.equals(this.metadata, economicModifier.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(modifierId, scope, value, type, durationMinutes, affectedCommodities, metadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EconomicModifier {\n");
    sb.append("    modifierId: ").append(toIndentedString(modifierId)).append("\n");
    sb.append("    scope: ").append(toIndentedString(scope)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    durationMinutes: ").append(toIndentedString(durationMinutes)).append("\n");
    sb.append("    affectedCommodities: ").append(toIndentedString(affectedCommodities)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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

