package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.CreateCombatSessionRequestSettings;
import com.necpgame.gameplayservice.model.ParticipantInit;
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
 * CreateCombatSessionRequest
 */


public class CreateCombatSessionRequest {

  /**
   * Gets or Sets combatType
   */
  public enum CombatTypeEnum {
    PVE("PVE"),
    
    PVP_DUEL("PVP_DUEL"),
    
    PVP_ARENA("PVP_ARENA"),
    
    RAID_BOSS("RAID_BOSS"),
    
    EXTRACTION("EXTRACTION");

    private final String value;

    CombatTypeEnum(String value) {
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
    public static CombatTypeEnum fromValue(String value) {
      for (CombatTypeEnum b : CombatTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private CombatTypeEnum combatType;

  @Valid
  private List<@Valid ParticipantInit> participants = new ArrayList<>();

  private @Nullable String locationId;

  private @Nullable CreateCombatSessionRequestSettings settings;

  public CreateCombatSessionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateCombatSessionRequest(CombatTypeEnum combatType, List<@Valid ParticipantInit> participants) {
    this.combatType = combatType;
    this.participants = participants;
  }

  public CreateCombatSessionRequest combatType(CombatTypeEnum combatType) {
    this.combatType = combatType;
    return this;
  }

  /**
   * Get combatType
   * @return combatType
   */
  @NotNull 
  @Schema(name = "combat_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("combat_type")
  public CombatTypeEnum getCombatType() {
    return combatType;
  }

  public void setCombatType(CombatTypeEnum combatType) {
    this.combatType = combatType;
  }

  public CreateCombatSessionRequest participants(List<@Valid ParticipantInit> participants) {
    this.participants = participants;
    return this;
  }

  public CreateCombatSessionRequest addParticipantsItem(ParticipantInit participantsItem) {
    if (this.participants == null) {
      this.participants = new ArrayList<>();
    }
    this.participants.add(participantsItem);
    return this;
  }

  /**
   * Get participants
   * @return participants
   */
  @NotNull @Valid 
  @Schema(name = "participants", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("participants")
  public List<@Valid ParticipantInit> getParticipants() {
    return participants;
  }

  public void setParticipants(List<@Valid ParticipantInit> participants) {
    this.participants = participants;
  }

  public CreateCombatSessionRequest locationId(@Nullable String locationId) {
    this.locationId = locationId;
    return this;
  }

  /**
   * ID локации, где происходит бой
   * @return locationId
   */
  
  @Schema(name = "location_id", description = "ID локации, где происходит бой", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location_id")
  public @Nullable String getLocationId() {
    return locationId;
  }

  public void setLocationId(@Nullable String locationId) {
    this.locationId = locationId;
  }

  public CreateCombatSessionRequest settings(@Nullable CreateCombatSessionRequestSettings settings) {
    this.settings = settings;
    return this;
  }

  /**
   * Get settings
   * @return settings
   */
  @Valid 
  @Schema(name = "settings", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("settings")
  public @Nullable CreateCombatSessionRequestSettings getSettings() {
    return settings;
  }

  public void setSettings(@Nullable CreateCombatSessionRequestSettings settings) {
    this.settings = settings;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateCombatSessionRequest createCombatSessionRequest = (CreateCombatSessionRequest) o;
    return Objects.equals(this.combatType, createCombatSessionRequest.combatType) &&
        Objects.equals(this.participants, createCombatSessionRequest.participants) &&
        Objects.equals(this.locationId, createCombatSessionRequest.locationId) &&
        Objects.equals(this.settings, createCombatSessionRequest.settings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(combatType, participants, locationId, settings);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateCombatSessionRequest {\n");
    sb.append("    combatType: ").append(toIndentedString(combatType)).append("\n");
    sb.append("    participants: ").append(toIndentedString(participants)).append("\n");
    sb.append("    locationId: ").append(toIndentedString(locationId)).append("\n");
    sb.append("    settings: ").append(toIndentedString(settings)).append("\n");
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

