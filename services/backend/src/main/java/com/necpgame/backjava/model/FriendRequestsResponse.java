package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.FriendRequest;
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
 * FriendRequestsResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class FriendRequestsResponse {

  @Valid
  private List<@Valid FriendRequest> incoming = new ArrayList<>();

  @Valid
  private List<@Valid FriendRequest> outgoing = new ArrayList<>();

  public FriendRequestsResponse incoming(List<@Valid FriendRequest> incoming) {
    this.incoming = incoming;
    return this;
  }

  public FriendRequestsResponse addIncomingItem(FriendRequest incomingItem) {
    if (this.incoming == null) {
      this.incoming = new ArrayList<>();
    }
    this.incoming.add(incomingItem);
    return this;
  }

  /**
   * Get incoming
   * @return incoming
   */
  @Valid 
  @Schema(name = "incoming", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("incoming")
  public List<@Valid FriendRequest> getIncoming() {
    return incoming;
  }

  public void setIncoming(List<@Valid FriendRequest> incoming) {
    this.incoming = incoming;
  }

  public FriendRequestsResponse outgoing(List<@Valid FriendRequest> outgoing) {
    this.outgoing = outgoing;
    return this;
  }

  public FriendRequestsResponse addOutgoingItem(FriendRequest outgoingItem) {
    if (this.outgoing == null) {
      this.outgoing = new ArrayList<>();
    }
    this.outgoing.add(outgoingItem);
    return this;
  }

  /**
   * Get outgoing
   * @return outgoing
   */
  @Valid 
  @Schema(name = "outgoing", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("outgoing")
  public List<@Valid FriendRequest> getOutgoing() {
    return outgoing;
  }

  public void setOutgoing(List<@Valid FriendRequest> outgoing) {
    this.outgoing = outgoing;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FriendRequestsResponse friendRequestsResponse = (FriendRequestsResponse) o;
    return Objects.equals(this.incoming, friendRequestsResponse.incoming) &&
        Objects.equals(this.outgoing, friendRequestsResponse.outgoing);
  }

  @Override
  public int hashCode() {
    return Objects.hash(incoming, outgoing);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FriendRequestsResponse {\n");
    sb.append("    incoming: ").append(toIndentedString(incoming)).append("\n");
    sb.append("    outgoing: ").append(toIndentedString(outgoing)).append("\n");
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

