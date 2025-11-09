package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.ChannelType;
import java.util.HashMap;
import java.util.Map;
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
 * JoinChannelRequest
 */


public class JoinChannelRequest {

  private @Nullable String channelId;

  private ChannelType channelType;

  private @Nullable String inviteCode;

  private @Nullable UUID partyId;

  private @Nullable UUID guildId;

  @Valid
  private Map<String, String> metadata = new HashMap<>();

  public JoinChannelRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public JoinChannelRequest(ChannelType channelType) {
    this.channelType = channelType;
  }

  public JoinChannelRequest channelId(@Nullable String channelId) {
    this.channelId = channelId;
    return this;
  }

  /**
   * Get channelId
   * @return channelId
   */
  
  @Schema(name = "channelId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("channelId")
  public @Nullable String getChannelId() {
    return channelId;
  }

  public void setChannelId(@Nullable String channelId) {
    this.channelId = channelId;
  }

  public JoinChannelRequest channelType(ChannelType channelType) {
    this.channelType = channelType;
    return this;
  }

  /**
   * Get channelType
   * @return channelType
   */
  @NotNull @Valid 
  @Schema(name = "channelType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("channelType")
  public ChannelType getChannelType() {
    return channelType;
  }

  public void setChannelType(ChannelType channelType) {
    this.channelType = channelType;
  }

  public JoinChannelRequest inviteCode(@Nullable String inviteCode) {
    this.inviteCode = inviteCode;
    return this;
  }

  /**
   * Get inviteCode
   * @return inviteCode
   */
  
  @Schema(name = "inviteCode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("inviteCode")
  public @Nullable String getInviteCode() {
    return inviteCode;
  }

  public void setInviteCode(@Nullable String inviteCode) {
    this.inviteCode = inviteCode;
  }

  public JoinChannelRequest partyId(@Nullable UUID partyId) {
    this.partyId = partyId;
    return this;
  }

  /**
   * Get partyId
   * @return partyId
   */
  @Valid 
  @Schema(name = "partyId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("partyId")
  public @Nullable UUID getPartyId() {
    return partyId;
  }

  public void setPartyId(@Nullable UUID partyId) {
    this.partyId = partyId;
  }

  public JoinChannelRequest guildId(@Nullable UUID guildId) {
    this.guildId = guildId;
    return this;
  }

  /**
   * Get guildId
   * @return guildId
   */
  @Valid 
  @Schema(name = "guildId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guildId")
  public @Nullable UUID getGuildId() {
    return guildId;
  }

  public void setGuildId(@Nullable UUID guildId) {
    this.guildId = guildId;
  }

  public JoinChannelRequest metadata(Map<String, String> metadata) {
    this.metadata = metadata;
    return this;
  }

  public JoinChannelRequest putMetadataItem(String key, String metadataItem) {
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
  public Map<String, String> getMetadata() {
    return metadata;
  }

  public void setMetadata(Map<String, String> metadata) {
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
    JoinChannelRequest joinChannelRequest = (JoinChannelRequest) o;
    return Objects.equals(this.channelId, joinChannelRequest.channelId) &&
        Objects.equals(this.channelType, joinChannelRequest.channelType) &&
        Objects.equals(this.inviteCode, joinChannelRequest.inviteCode) &&
        Objects.equals(this.partyId, joinChannelRequest.partyId) &&
        Objects.equals(this.guildId, joinChannelRequest.guildId) &&
        Objects.equals(this.metadata, joinChannelRequest.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(channelId, channelType, inviteCode, partyId, guildId, metadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class JoinChannelRequest {\n");
    sb.append("    channelId: ").append(toIndentedString(channelId)).append("\n");
    sb.append("    channelType: ").append(toIndentedString(channelType)).append("\n");
    sb.append("    inviteCode: ").append(toIndentedString(inviteCode)).append("\n");
    sb.append("    partyId: ").append(toIndentedString(partyId)).append("\n");
    sb.append("    guildId: ").append(toIndentedString(guildId)).append("\n");
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

