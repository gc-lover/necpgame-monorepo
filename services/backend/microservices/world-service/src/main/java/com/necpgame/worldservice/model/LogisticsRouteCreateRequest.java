package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.LogisticsRouteCreateRequestCargoManifestInner;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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
 * LogisticsRouteCreateRequest
 */


public class LogisticsRouteCreateRequest {

  private UUID originSettlementId;

  private UUID destinationSettlementId;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    TRADE("trade"),
    
    TAX("tax"),
    
    REINFORCEMENT("reinforcement"),
    
    AID("aid"),
    
    BLACK_OPS("black_ops");

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

  private TypeEnum type;

  private Integer requestedSecurity;

  @Valid
  private List<@Valid LogisticsRouteCreateRequestCargoManifestInner> cargoManifest = new ArrayList<>();

  private @Nullable Integer requestedEscortStrength;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime preferredDeparture;

  private @Nullable String notes;

  public LogisticsRouteCreateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LogisticsRouteCreateRequest(UUID originSettlementId, UUID destinationSettlementId, TypeEnum type, Integer requestedSecurity) {
    this.originSettlementId = originSettlementId;
    this.destinationSettlementId = destinationSettlementId;
    this.type = type;
    this.requestedSecurity = requestedSecurity;
  }

  public LogisticsRouteCreateRequest originSettlementId(UUID originSettlementId) {
    this.originSettlementId = originSettlementId;
    return this;
  }

  /**
   * Get originSettlementId
   * @return originSettlementId
   */
  @NotNull @Valid 
  @Schema(name = "originSettlementId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("originSettlementId")
  public UUID getOriginSettlementId() {
    return originSettlementId;
  }

  public void setOriginSettlementId(UUID originSettlementId) {
    this.originSettlementId = originSettlementId;
  }

  public LogisticsRouteCreateRequest destinationSettlementId(UUID destinationSettlementId) {
    this.destinationSettlementId = destinationSettlementId;
    return this;
  }

  /**
   * Get destinationSettlementId
   * @return destinationSettlementId
   */
  @NotNull @Valid 
  @Schema(name = "destinationSettlementId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("destinationSettlementId")
  public UUID getDestinationSettlementId() {
    return destinationSettlementId;
  }

  public void setDestinationSettlementId(UUID destinationSettlementId) {
    this.destinationSettlementId = destinationSettlementId;
  }

  public LogisticsRouteCreateRequest type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @NotNull 
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public LogisticsRouteCreateRequest requestedSecurity(Integer requestedSecurity) {
    this.requestedSecurity = requestedSecurity;
    return this;
  }

  /**
   * Get requestedSecurity
   * minimum: 1
   * maximum: 5
   * @return requestedSecurity
   */
  @NotNull @Min(value = 1) @Max(value = 5) 
  @Schema(name = "requestedSecurity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("requestedSecurity")
  public Integer getRequestedSecurity() {
    return requestedSecurity;
  }

  public void setRequestedSecurity(Integer requestedSecurity) {
    this.requestedSecurity = requestedSecurity;
  }

  public LogisticsRouteCreateRequest cargoManifest(List<@Valid LogisticsRouteCreateRequestCargoManifestInner> cargoManifest) {
    this.cargoManifest = cargoManifest;
    return this;
  }

  public LogisticsRouteCreateRequest addCargoManifestItem(LogisticsRouteCreateRequestCargoManifestInner cargoManifestItem) {
    if (this.cargoManifest == null) {
      this.cargoManifest = new ArrayList<>();
    }
    this.cargoManifest.add(cargoManifestItem);
    return this;
  }

  /**
   * Get cargoManifest
   * @return cargoManifest
   */
  @Valid 
  @Schema(name = "cargoManifest", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cargoManifest")
  public List<@Valid LogisticsRouteCreateRequestCargoManifestInner> getCargoManifest() {
    return cargoManifest;
  }

  public void setCargoManifest(List<@Valid LogisticsRouteCreateRequestCargoManifestInner> cargoManifest) {
    this.cargoManifest = cargoManifest;
  }

  public LogisticsRouteCreateRequest requestedEscortStrength(@Nullable Integer requestedEscortStrength) {
    this.requestedEscortStrength = requestedEscortStrength;
    return this;
  }

  /**
   * Get requestedEscortStrength
   * @return requestedEscortStrength
   */
  
  @Schema(name = "requestedEscortStrength", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requestedEscortStrength")
  public @Nullable Integer getRequestedEscortStrength() {
    return requestedEscortStrength;
  }

  public void setRequestedEscortStrength(@Nullable Integer requestedEscortStrength) {
    this.requestedEscortStrength = requestedEscortStrength;
  }

  public LogisticsRouteCreateRequest preferredDeparture(@Nullable OffsetDateTime preferredDeparture) {
    this.preferredDeparture = preferredDeparture;
    return this;
  }

  /**
   * Get preferredDeparture
   * @return preferredDeparture
   */
  @Valid 
  @Schema(name = "preferredDeparture", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("preferredDeparture")
  public @Nullable OffsetDateTime getPreferredDeparture() {
    return preferredDeparture;
  }

  public void setPreferredDeparture(@Nullable OffsetDateTime preferredDeparture) {
    this.preferredDeparture = preferredDeparture;
  }

  public LogisticsRouteCreateRequest notes(@Nullable String notes) {
    this.notes = notes;
    return this;
  }

  /**
   * Get notes
   * @return notes
   */
  
  @Schema(name = "notes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notes")
  public @Nullable String getNotes() {
    return notes;
  }

  public void setNotes(@Nullable String notes) {
    this.notes = notes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LogisticsRouteCreateRequest logisticsRouteCreateRequest = (LogisticsRouteCreateRequest) o;
    return Objects.equals(this.originSettlementId, logisticsRouteCreateRequest.originSettlementId) &&
        Objects.equals(this.destinationSettlementId, logisticsRouteCreateRequest.destinationSettlementId) &&
        Objects.equals(this.type, logisticsRouteCreateRequest.type) &&
        Objects.equals(this.requestedSecurity, logisticsRouteCreateRequest.requestedSecurity) &&
        Objects.equals(this.cargoManifest, logisticsRouteCreateRequest.cargoManifest) &&
        Objects.equals(this.requestedEscortStrength, logisticsRouteCreateRequest.requestedEscortStrength) &&
        Objects.equals(this.preferredDeparture, logisticsRouteCreateRequest.preferredDeparture) &&
        Objects.equals(this.notes, logisticsRouteCreateRequest.notes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(originSettlementId, destinationSettlementId, type, requestedSecurity, cargoManifest, requestedEscortStrength, preferredDeparture, notes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LogisticsRouteCreateRequest {\n");
    sb.append("    originSettlementId: ").append(toIndentedString(originSettlementId)).append("\n");
    sb.append("    destinationSettlementId: ").append(toIndentedString(destinationSettlementId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    requestedSecurity: ").append(toIndentedString(requestedSecurity)).append("\n");
    sb.append("    cargoManifest: ").append(toIndentedString(cargoManifest)).append("\n");
    sb.append("    requestedEscortStrength: ").append(toIndentedString(requestedEscortStrength)).append("\n");
    sb.append("    preferredDeparture: ").append(toIndentedString(preferredDeparture)).append("\n");
    sb.append("    notes: ").append(toIndentedString(notes)).append("\n");
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

