package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.Friend;
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
 * GetFriends200Response
 */

@JsonTypeName("getFriends_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetFriends200Response {

  @Valid
  private List<@Valid Friend> friends = new ArrayList<>();

  public GetFriends200Response friends(List<@Valid Friend> friends) {
    this.friends = friends;
    return this;
  }

  public GetFriends200Response addFriendsItem(Friend friendsItem) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetFriends200Response getFriends200Response = (GetFriends200Response) o;
    return Objects.equals(this.friends, getFriends200Response.friends);
  }

  @Override
  public int hashCode() {
    return Objects.hash(friends);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetFriends200Response {\n");
    sb.append("    friends: ").append(toIndentedString(friends)).append("\n");
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

