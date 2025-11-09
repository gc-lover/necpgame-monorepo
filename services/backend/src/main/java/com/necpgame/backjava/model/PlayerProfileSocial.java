package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PlayerProfileSocial
 */

@JsonTypeName("PlayerProfile_social")

public class PlayerProfileSocial {

  @Valid
  private List<UUID> friends = new ArrayList<>();

  @Valid
  private List<UUID> blocked = new ArrayList<>();

  public PlayerProfileSocial friends(List<UUID> friends) {
    this.friends = friends;
    return this;
  }

  public PlayerProfileSocial addFriendsItem(UUID friendsItem) {
    if (this.friends == null) {
      this.friends = new ArrayList<>();
    }
    this.friends.add(friendsItem);
    return this;
  }

  /**
   * Get friends
   * @return friends
   */
  @Valid 
  @Schema(name = "friends", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("friends")
  public List<UUID> getFriends() {
    return friends;
  }

  public void setFriends(List<UUID> friends) {
    this.friends = friends;
  }

  public PlayerProfileSocial blocked(List<UUID> blocked) {
    this.blocked = blocked;
    return this;
  }

  public PlayerProfileSocial addBlockedItem(UUID blockedItem) {
    if (this.blocked == null) {
      this.blocked = new ArrayList<>();
    }
    this.blocked.add(blockedItem);
    return this;
  }

  /**
   * Get blocked
   * @return blocked
   */
  @Valid 
  @Schema(name = "blocked", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("blocked")
  public List<UUID> getBlocked() {
    return blocked;
  }

  public void setBlocked(List<UUID> blocked) {
    this.blocked = blocked;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerProfileSocial playerProfileSocial = (PlayerProfileSocial) o;
    return Objects.equals(this.friends, playerProfileSocial.friends) &&
        Objects.equals(this.blocked, playerProfileSocial.blocked);
  }

  @Override
  public int hashCode() {
    return Objects.hash(friends, blocked);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerProfileSocial {\n");
    sb.append("    friends: ").append(toIndentedString(friends)).append("\n");
    sb.append("    blocked: ").append(toIndentedString(blocked)).append("\n");
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

