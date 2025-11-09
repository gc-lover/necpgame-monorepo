package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * ChannelModeratorsRequest
 */


public class ChannelModeratorsRequest {

  @Valid
  private List<UUID> add = new ArrayList<>();

  @Valid
  private List<UUID> remove = new ArrayList<>();

  public ChannelModeratorsRequest add(List<UUID> add) {
    this.add = add;
    return this;
  }

  public ChannelModeratorsRequest addAddItem(UUID addItem) {
    if (this.add == null) {
      this.add = new ArrayList<>();
    }
    this.add.add(addItem);
    return this;
  }

  /**
   * Get add
   * @return add
   */
  @Valid 
  @Schema(name = "add", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("add")
  public List<UUID> getAdd() {
    return add;
  }

  public void setAdd(List<UUID> add) {
    this.add = add;
  }

  public ChannelModeratorsRequest remove(List<UUID> remove) {
    this.remove = remove;
    return this;
  }

  public ChannelModeratorsRequest addRemoveItem(UUID removeItem) {
    if (this.remove == null) {
      this.remove = new ArrayList<>();
    }
    this.remove.add(removeItem);
    return this;
  }

  /**
   * Get remove
   * @return remove
   */
  @Valid 
  @Schema(name = "remove", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("remove")
  public List<UUID> getRemove() {
    return remove;
  }

  public void setRemove(List<UUID> remove) {
    this.remove = remove;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChannelModeratorsRequest channelModeratorsRequest = (ChannelModeratorsRequest) o;
    return Objects.equals(this.add, channelModeratorsRequest.add) &&
        Objects.equals(this.remove, channelModeratorsRequest.remove);
  }

  @Override
  public int hashCode() {
    return Objects.hash(add, remove);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChannelModeratorsRequest {\n");
    sb.append("    add: ").append(toIndentedString(add)).append("\n");
    sb.append("    remove: ").append(toIndentedString(remove)).append("\n");
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

