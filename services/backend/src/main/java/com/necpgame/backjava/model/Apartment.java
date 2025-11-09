package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.ApartmentUpkeep;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Apartment
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class Apartment {

  private String apartmentId;

  private String playerId;

  private String locationId;

  private Integer tier;

  private @Nullable Integer prestige;

  private @Nullable Integer rooms;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACTIVE("ACTIVE"),
    
    RENOVATION("RENOVATION"),
    
    FOR_SALE("FOR_SALE");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private StatusEnum status;

  private @Nullable ApartmentUpkeep upkeep;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime updatedAt;

  public Apartment() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Apartment(String apartmentId, String playerId, String locationId, Integer tier, StatusEnum status, OffsetDateTime createdAt) {
    this.apartmentId = apartmentId;
    this.playerId = playerId;
    this.locationId = locationId;
    this.tier = tier;
    this.status = status;
    this.createdAt = createdAt;
  }

  public Apartment apartmentId(String apartmentId) {
    this.apartmentId = apartmentId;
    return this;
  }

  /**
   * Get apartmentId
   * @return apartmentId
   */
  @NotNull 
  @Schema(name = "apartmentId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("apartmentId")
  public String getApartmentId() {
    return apartmentId;
  }

  public void setApartmentId(String apartmentId) {
    this.apartmentId = apartmentId;
  }

  public Apartment playerId(String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull 
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

  public Apartment locationId(String locationId) {
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

  public Apartment tier(Integer tier) {
    this.tier = tier;
    return this;
  }

  /**
   * Get tier
   * @return tier
   */
  @NotNull 
  @Schema(name = "tier", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("tier")
  public Integer getTier() {
    return tier;
  }

  public void setTier(Integer tier) {
    this.tier = tier;
  }

  public Apartment prestige(@Nullable Integer prestige) {
    this.prestige = prestige;
    return this;
  }

  /**
   * Get prestige
   * @return prestige
   */
  
  @Schema(name = "prestige", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("prestige")
  public @Nullable Integer getPrestige() {
    return prestige;
  }

  public void setPrestige(@Nullable Integer prestige) {
    this.prestige = prestige;
  }

  public Apartment rooms(@Nullable Integer rooms) {
    this.rooms = rooms;
    return this;
  }

  /**
   * Get rooms
   * @return rooms
   */
  
  @Schema(name = "rooms", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rooms")
  public @Nullable Integer getRooms() {
    return rooms;
  }

  public void setRooms(@Nullable Integer rooms) {
    this.rooms = rooms;
  }

  public Apartment status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public Apartment upkeep(@Nullable ApartmentUpkeep upkeep) {
    this.upkeep = upkeep;
    return this;
  }

  /**
   * Get upkeep
   * @return upkeep
   */
  @Valid 
  @Schema(name = "upkeep", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("upkeep")
  public @Nullable ApartmentUpkeep getUpkeep() {
    return upkeep;
  }

  public void setUpkeep(@Nullable ApartmentUpkeep upkeep) {
    this.upkeep = upkeep;
  }

  public Apartment createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @NotNull @Valid 
  @Schema(name = "createdAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("createdAt")
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public Apartment updatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   */
  @Valid 
  @Schema(name = "updatedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("updatedAt")
  public @Nullable OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Apartment apartment = (Apartment) o;
    return Objects.equals(this.apartmentId, apartment.apartmentId) &&
        Objects.equals(this.playerId, apartment.playerId) &&
        Objects.equals(this.locationId, apartment.locationId) &&
        Objects.equals(this.tier, apartment.tier) &&
        Objects.equals(this.prestige, apartment.prestige) &&
        Objects.equals(this.rooms, apartment.rooms) &&
        Objects.equals(this.status, apartment.status) &&
        Objects.equals(this.upkeep, apartment.upkeep) &&
        Objects.equals(this.createdAt, apartment.createdAt) &&
        Objects.equals(this.updatedAt, apartment.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(apartmentId, playerId, locationId, tier, prestige, rooms, status, upkeep, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Apartment {\n");
    sb.append("    apartmentId: ").append(toIndentedString(apartmentId)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    locationId: ").append(toIndentedString(locationId)).append("\n");
    sb.append("    tier: ").append(toIndentedString(tier)).append("\n");
    sb.append("    prestige: ").append(toIndentedString(prestige)).append("\n");
    sb.append("    rooms: ").append(toIndentedString(rooms)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    upkeep: ").append(toIndentedString(upkeep)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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

