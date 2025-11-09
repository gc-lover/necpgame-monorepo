package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.ChannelTypeInfo;
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
 * ChannelCatalog
 */


public class ChannelCatalog {

  @Valid
  private List<@Valid ChannelTypeInfo> channels = new ArrayList<>();

  @Valid
  private List<@Valid ChannelTypeInfo> combatChannels = new ArrayList<>();

  public ChannelCatalog() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ChannelCatalog(List<@Valid ChannelTypeInfo> channels) {
    this.channels = channels;
  }

  public ChannelCatalog channels(List<@Valid ChannelTypeInfo> channels) {
    this.channels = channels;
    return this;
  }

  public ChannelCatalog addChannelsItem(ChannelTypeInfo channelsItem) {
    if (this.channels == null) {
      this.channels = new ArrayList<>();
    }
    this.channels.add(channelsItem);
    return this;
  }

  /**
   * Get channels
   * @return channels
   */
  @NotNull @Valid 
  @Schema(name = "channels", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("channels")
  public List<@Valid ChannelTypeInfo> getChannels() {
    return channels;
  }

  public void setChannels(List<@Valid ChannelTypeInfo> channels) {
    this.channels = channels;
  }

  public ChannelCatalog combatChannels(List<@Valid ChannelTypeInfo> combatChannels) {
    this.combatChannels = combatChannels;
    return this;
  }

  public ChannelCatalog addCombatChannelsItem(ChannelTypeInfo combatChannelsItem) {
    if (this.combatChannels == null) {
      this.combatChannels = new ArrayList<>();
    }
    this.combatChannels.add(combatChannelsItem);
    return this;
  }

  /**
   * Get combatChannels
   * @return combatChannels
   */
  @Valid 
  @Schema(name = "combatChannels", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("combatChannels")
  public List<@Valid ChannelTypeInfo> getCombatChannels() {
    return combatChannels;
  }

  public void setCombatChannels(List<@Valid ChannelTypeInfo> combatChannels) {
    this.combatChannels = combatChannels;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChannelCatalog channelCatalog = (ChannelCatalog) o;
    return Objects.equals(this.channels, channelCatalog.channels) &&
        Objects.equals(this.combatChannels, channelCatalog.combatChannels);
  }

  @Override
  public int hashCode() {
    return Objects.hash(channels, combatChannels);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChannelCatalog {\n");
    sb.append("    channels: ").append(toIndentedString(channels)).append("\n");
    sb.append("    combatChannels: ").append(toIndentedString(combatChannels)).append("\n");
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

