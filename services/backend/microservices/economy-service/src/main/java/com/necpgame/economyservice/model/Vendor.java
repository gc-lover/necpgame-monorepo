package com.necpgame.economyservice.model;

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
 * Vendor
 */


public class Vendor {

  private String id;

  private String name;

  private String locationId;

  /**
   * Gets or Sets specialization
   */
  public enum SpecializationEnum {
    WEAPONS("weapons"),
    
    ARMOR("armor"),
    
    GENERAL("general"),
    
    MEDICAL("medical"),
    
    TECH("tech");

    private final String value;

    SpecializationEnum(String value) {
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
    public static SpecializationEnum fromValue(String value) {
      for (SpecializationEnum b : SpecializationEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable SpecializationEnum specialization;

  public Vendor() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Vendor(String id, String name, String locationId) {
    this.id = id;
    this.name = name;
    this.locationId = locationId;
  }

  public Vendor id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  @NotNull 
  @Schema(name = "id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Vendor name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Vendor locationId(String locationId) {
    this.locationId = locationId;
    return this;
  }

  /**
   * Get locationId
   * @return locationId
   */
  @NotNull 
  @Schema(name = "locationId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("locationId")
  public String getLocationId() {
    return locationId;
  }

  public void setLocationId(String locationId) {
    this.locationId = locationId;
  }

  public Vendor specialization(@Nullable SpecializationEnum specialization) {
    this.specialization = specialization;
    return this;
  }

  /**
   * Get specialization
   * @return specialization
   */
  
  @Schema(name = "specialization", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("specialization")
  public @Nullable SpecializationEnum getSpecialization() {
    return specialization;
  }

  public void setSpecialization(@Nullable SpecializationEnum specialization) {
    this.specialization = specialization;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Vendor vendor = (Vendor) o;
    return Objects.equals(this.id, vendor.id) &&
        Objects.equals(this.name, vendor.name) &&
        Objects.equals(this.locationId, vendor.locationId) &&
        Objects.equals(this.specialization, vendor.specialization);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, locationId, specialization);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Vendor {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    locationId: ").append(toIndentedString(locationId)).append("\n");
    sb.append("    specialization: ").append(toIndentedString(specialization)).append("\n");
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

