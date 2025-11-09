package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.CharacterActivityEntryActor;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
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
 * CharacterActivityEntry
 */


public class CharacterActivityEntry {

  private UUID activityId;

  /**
   * Gets or Sets activityType
   */
  public enum ActivityTypeEnum {
    CREATION("creation"),
    
    DELETION("deletion"),
    
    RESTORATION("restoration"),
    
    SWITCH("switch"),
    
    APPEARANCE("appearance"),
    
    STATS("stats"),
    
    SLOT("slot"),
    
    MODERATOR("moderator");

    private final String value;

    ActivityTypeEnum(String value) {
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
    public static ActivityTypeEnum fromValue(String value) {
      for (ActivityTypeEnum b : ActivityTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ActivityTypeEnum activityType;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime occurredAt;

  private CharacterActivityEntryActor actor;

  private JsonNullable<String> ipAddress = JsonNullable.<String>undefined();

  private @Nullable String location;

  private Object metadata;

  public CharacterActivityEntry() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CharacterActivityEntry(UUID activityId, ActivityTypeEnum activityType, OffsetDateTime occurredAt, CharacterActivityEntryActor actor, Object metadata) {
    this.activityId = activityId;
    this.activityType = activityType;
    this.occurredAt = occurredAt;
    this.actor = actor;
    this.metadata = metadata;
  }

  public CharacterActivityEntry activityId(UUID activityId) {
    this.activityId = activityId;
    return this;
  }

  /**
   * Get activityId
   * @return activityId
   */
  @NotNull @Valid 
  @Schema(name = "activityId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("activityId")
  public UUID getActivityId() {
    return activityId;
  }

  public void setActivityId(UUID activityId) {
    this.activityId = activityId;
  }

  public CharacterActivityEntry activityType(ActivityTypeEnum activityType) {
    this.activityType = activityType;
    return this;
  }

  /**
   * Get activityType
   * @return activityType
   */
  @NotNull 
  @Schema(name = "activityType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("activityType")
  public ActivityTypeEnum getActivityType() {
    return activityType;
  }

  public void setActivityType(ActivityTypeEnum activityType) {
    this.activityType = activityType;
  }

  public CharacterActivityEntry occurredAt(OffsetDateTime occurredAt) {
    this.occurredAt = occurredAt;
    return this;
  }

  /**
   * Get occurredAt
   * @return occurredAt
   */
  @NotNull @Valid 
  @Schema(name = "occurredAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("occurredAt")
  public OffsetDateTime getOccurredAt() {
    return occurredAt;
  }

  public void setOccurredAt(OffsetDateTime occurredAt) {
    this.occurredAt = occurredAt;
  }

  public CharacterActivityEntry actor(CharacterActivityEntryActor actor) {
    this.actor = actor;
    return this;
  }

  /**
   * Get actor
   * @return actor
   */
  @NotNull @Valid 
  @Schema(name = "actor", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("actor")
  public CharacterActivityEntryActor getActor() {
    return actor;
  }

  public void setActor(CharacterActivityEntryActor actor) {
    this.actor = actor;
  }

  public CharacterActivityEntry ipAddress(String ipAddress) {
    this.ipAddress = JsonNullable.of(ipAddress);
    return this;
  }

  /**
   * Get ipAddress
   * @return ipAddress
   */
  
  @Schema(name = "ipAddress", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ipAddress")
  public JsonNullable<String> getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(JsonNullable<String> ipAddress) {
    this.ipAddress = ipAddress;
  }

  public CharacterActivityEntry location(@Nullable String location) {
    this.location = location;
    return this;
  }

  /**
   * Geo IP or in-game terminal information
   * @return location
   */
  
  @Schema(name = "location", description = "Geo IP or in-game terminal information", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location")
  public @Nullable String getLocation() {
    return location;
  }

  public void setLocation(@Nullable String location) {
    this.location = location;
  }

  public CharacterActivityEntry metadata(Object metadata) {
    this.metadata = metadata;
    return this;
  }

  /**
   * Get metadata
   * @return metadata
   */
  @NotNull 
  @Schema(name = "metadata", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("metadata")
  public Object getMetadata() {
    return metadata;
  }

  public void setMetadata(Object metadata) {
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
    CharacterActivityEntry characterActivityEntry = (CharacterActivityEntry) o;
    return Objects.equals(this.activityId, characterActivityEntry.activityId) &&
        Objects.equals(this.activityType, characterActivityEntry.activityType) &&
        Objects.equals(this.occurredAt, characterActivityEntry.occurredAt) &&
        Objects.equals(this.actor, characterActivityEntry.actor) &&
        equalsNullable(this.ipAddress, characterActivityEntry.ipAddress) &&
        Objects.equals(this.location, characterActivityEntry.location) &&
        Objects.equals(this.metadata, characterActivityEntry.metadata);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(activityId, activityType, occurredAt, actor, hashCodeNullable(ipAddress), location, metadata);
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
    sb.append("class CharacterActivityEntry {\n");
    sb.append("    activityId: ").append(toIndentedString(activityId)).append("\n");
    sb.append("    activityType: ").append(toIndentedString(activityType)).append("\n");
    sb.append("    occurredAt: ").append(toIndentedString(occurredAt)).append("\n");
    sb.append("    actor: ").append(toIndentedString(actor)).append("\n");
    sb.append("    ipAddress: ").append(toIndentedString(ipAddress)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
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

