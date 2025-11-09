package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.Friend;
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
 * FriendListResponse
 */


public class FriendListResponse {

  @Valid
  private List<@Valid Friend> friends = new ArrayList<>();

  private @Nullable Integer total;

  public FriendListResponse friends(List<@Valid Friend> friends) {
    this.friends = friends;
    return this;
  }

  public FriendListResponse addFriendsItem(Friend friendsItem) {
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
  public List<@Valid Friend> getFriends() {
    return friends;
  }

  public void setFriends(List<@Valid Friend> friends) {
    this.friends = friends;
  }

  public FriendListResponse total(@Nullable Integer total) {
    this.total = total;
    return this;
  }

  /**
   * Get total
   * @return total
   */
  
  @Schema(name = "total", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total")
  public @Nullable Integer getTotal() {
    return total;
  }

  public void setTotal(@Nullable Integer total) {
    this.total = total;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FriendListResponse friendListResponse = (FriendListResponse) o;
    return Objects.equals(this.friends, friendListResponse.friends) &&
        Objects.equals(this.total, friendListResponse.total);
  }

  @Override
  public int hashCode() {
    return Objects.hash(friends, total);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FriendListResponse {\n");
    sb.append("    friends: ").append(toIndentedString(friends)).append("\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
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

