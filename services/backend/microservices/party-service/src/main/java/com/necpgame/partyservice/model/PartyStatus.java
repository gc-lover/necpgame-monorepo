package com.necpgame.partyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.partyservice.model.PartyQueueStatus;
import com.necpgame.partyservice.model.ReadyCheck;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PartyStatus
 */


public class PartyStatus {

  /**
   * Gets or Sets state
   */
  public enum StateEnum {
    IDLE("IDLE"),
    
    READY_CHECK("READY_CHECK"),
    
    MATCHMAKING("MATCHMAKING"),
    
    COMBAT("COMBAT");

    private final String value;

    StateEnum(String value) {
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
    public static StateEnum fromValue(String value) {
      for (StateEnum b : StateEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StateEnum state;

  private @Nullable String location;

  private @Nullable PartyQueueStatus queueStatus;

  private @Nullable ReadyCheck readyCheck;

  public PartyStatus state(@Nullable StateEnum state) {
    this.state = state;
    return this;
  }

  /**
   * Get state
   * @return state
   */
  
  @Schema(name = "state", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("state")
  public @Nullable StateEnum getState() {
    return state;
  }

  public void setState(@Nullable StateEnum state) {
    this.state = state;
  }

  public PartyStatus location(@Nullable String location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
   */
  
  @Schema(name = "location", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location")
  public @Nullable String getLocation() {
    return location;
  }

  public void setLocation(@Nullable String location) {
    this.location = location;
  }

  public PartyStatus queueStatus(@Nullable PartyQueueStatus queueStatus) {
    this.queueStatus = queueStatus;
    return this;
  }

  /**
   * Get queueStatus
   * @return queueStatus
   */
  @Valid 
  @Schema(name = "queueStatus", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("queueStatus")
  public @Nullable PartyQueueStatus getQueueStatus() {
    return queueStatus;
  }

  public void setQueueStatus(@Nullable PartyQueueStatus queueStatus) {
    this.queueStatus = queueStatus;
  }

  public PartyStatus readyCheck(@Nullable ReadyCheck readyCheck) {
    this.readyCheck = readyCheck;
    return this;
  }

  /**
   * Get readyCheck
   * @return readyCheck
   */
  @Valid 
  @Schema(name = "readyCheck", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("readyCheck")
  public @Nullable ReadyCheck getReadyCheck() {
    return readyCheck;
  }

  public void setReadyCheck(@Nullable ReadyCheck readyCheck) {
    this.readyCheck = readyCheck;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PartyStatus partyStatus = (PartyStatus) o;
    return Objects.equals(this.state, partyStatus.state) &&
        Objects.equals(this.location, partyStatus.location) &&
        Objects.equals(this.queueStatus, partyStatus.queueStatus) &&
        Objects.equals(this.readyCheck, partyStatus.readyCheck);
  }

  @Override
  public int hashCode() {
    return Objects.hash(state, location, queueStatus, readyCheck);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PartyStatus {\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    queueStatus: ").append(toIndentedString(queueStatus)).append("\n");
    sb.append("    readyCheck: ").append(toIndentedString(readyCheck)).append("\n");
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

