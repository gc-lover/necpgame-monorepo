package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.Presence;
import java.util.HashMap;
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
 * Friend
 */


public class Friend {

  private @Nullable String playerId;

  private @Nullable String nickname;

  private @Nullable String platform;

  /**
   * Gets or Sets relationship
   */
  public enum RelationshipEnum {
    FRIEND("FRIEND"),
    
    FAVORITE("FAVORITE"),
    
    FOLLOWING("FOLLOWING");

    private final String value;

    RelationshipEnum(String value) {
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
    public static RelationshipEnum fromValue(String value) {
      for (RelationshipEnum b : RelationshipEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RelationshipEnum relationship;

  private @Nullable Presence presence;

  private @Nullable Boolean favorite;

  @Valid
  private Map<String, Object> metadata = new HashMap<>();

  public Friend playerId(@Nullable String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("playerId")
  public @Nullable String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(@Nullable String playerId) {
    this.playerId = playerId;
  }

  public Friend nickname(@Nullable String nickname) {
    this.nickname = nickname;
    return this;
  }

  /**
   * Get nickname
   * @return nickname
   */
  
  @Schema(name = "nickname", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nickname")
  public @Nullable String getNickname() {
    return nickname;
  }

  public void setNickname(@Nullable String nickname) {
    this.nickname = nickname;
  }

  public Friend platform(@Nullable String platform) {
    this.platform = platform;
    return this;
  }

  /**
   * Get platform
   * @return platform
   */
  
  @Schema(name = "platform", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("platform")
  public @Nullable String getPlatform() {
    return platform;
  }

  public void setPlatform(@Nullable String platform) {
    this.platform = platform;
  }

  public Friend relationship(@Nullable RelationshipEnum relationship) {
    this.relationship = relationship;
    return this;
  }

  /**
   * Get relationship
   * @return relationship
   */
  
  @Schema(name = "relationship", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relationship")
  public @Nullable RelationshipEnum getRelationship() {
    return relationship;
  }

  public void setRelationship(@Nullable RelationshipEnum relationship) {
    this.relationship = relationship;
  }

  public Friend presence(@Nullable Presence presence) {
    this.presence = presence;
    return this;
  }

  /**
   * Get presence
   * @return presence
   */
  @Valid 
  @Schema(name = "presence", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("presence")
  public @Nullable Presence getPresence() {
    return presence;
  }

  public void setPresence(@Nullable Presence presence) {
    this.presence = presence;
  }

  public Friend favorite(@Nullable Boolean favorite) {
    this.favorite = favorite;
    return this;
  }

  /**
   * Get favorite
   * @return favorite
   */
  
  @Schema(name = "favorite", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("favorite")
  public @Nullable Boolean getFavorite() {
    return favorite;
  }

  public void setFavorite(@Nullable Boolean favorite) {
    this.favorite = favorite;
  }

  public Friend metadata(Map<String, Object> metadata) {
    this.metadata = metadata;
    return this;
  }

  public Friend putMetadataItem(String key, Object metadataItem) {
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
    Friend friend = (Friend) o;
    return Objects.equals(this.playerId, friend.playerId) &&
        Objects.equals(this.nickname, friend.nickname) &&
        Objects.equals(this.platform, friend.platform) &&
        Objects.equals(this.relationship, friend.relationship) &&
        Objects.equals(this.presence, friend.presence) &&
        Objects.equals(this.favorite, friend.favorite) &&
        Objects.equals(this.metadata, friend.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, nickname, platform, relationship, presence, favorite, metadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Friend {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    nickname: ").append(toIndentedString(nickname)).append("\n");
    sb.append("    platform: ").append(toIndentedString(platform)).append("\n");
    sb.append("    relationship: ").append(toIndentedString(relationship)).append("\n");
    sb.append("    presence: ").append(toIndentedString(presence)).append("\n");
    sb.append("    favorite: ").append(toIndentedString(favorite)).append("\n");
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

