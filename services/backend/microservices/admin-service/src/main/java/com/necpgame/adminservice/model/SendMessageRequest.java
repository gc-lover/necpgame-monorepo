package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SendMessageRequest
 */

@JsonTypeName("sendMessage_request")

public class SendMessageRequest {

  private String characterId;

  /**
   * Gets or Sets channel
   */
  public enum ChannelEnum {
    GLOBAL("global"),
    
    LOCAL("local"),
    
    PARTY("party"),
    
    GUILD("guild"),
    
    WHISPER("whisper"),
    
    TRADE("trade"),
    
    COMBAT("combat");

    private final String value;

    ChannelEnum(String value) {
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
    public static ChannelEnum fromValue(String value) {
      for (ChannelEnum b : ChannelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ChannelEnum channel;

  private String message;

  private @Nullable String recipientId;

  private @Nullable Object formatting;

  public SendMessageRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SendMessageRequest(String characterId, ChannelEnum channel, String message) {
    this.characterId = characterId;
    this.channel = channel;
    this.message = message;
  }

  public SendMessageRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public SendMessageRequest channel(ChannelEnum channel) {
    this.channel = channel;
    return this;
  }

  /**
   * Get channel
   * @return channel
   */
  @NotNull 
  @Schema(name = "channel", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("channel")
  public ChannelEnum getChannel() {
    return channel;
  }

  public void setChannel(ChannelEnum channel) {
    this.channel = channel;
  }

  public SendMessageRequest message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   */
  @NotNull @Size(min = 1, max = 500) 
  @Schema(name = "message", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public SendMessageRequest recipientId(@Nullable String recipientId) {
    this.recipientId = recipientId;
    return this;
  }

  /**
   * Только для whisper
   * @return recipientId
   */
  
  @Schema(name = "recipient_id", description = "Только для whisper", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recipient_id")
  public @Nullable String getRecipientId() {
    return recipientId;
  }

  public void setRecipientId(@Nullable String recipientId) {
    this.recipientId = recipientId;
  }

  public SendMessageRequest formatting(@Nullable Object formatting) {
    this.formatting = formatting;
    return this;
  }

  /**
   * Rich formatting (bold, italic, etc)
   * @return formatting
   */
  
  @Schema(name = "formatting", description = "Rich formatting (bold, italic, etc)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("formatting")
  public @Nullable Object getFormatting() {
    return formatting;
  }

  public void setFormatting(@Nullable Object formatting) {
    this.formatting = formatting;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SendMessageRequest sendMessageRequest = (SendMessageRequest) o;
    return Objects.equals(this.characterId, sendMessageRequest.characterId) &&
        Objects.equals(this.channel, sendMessageRequest.channel) &&
        Objects.equals(this.message, sendMessageRequest.message) &&
        Objects.equals(this.recipientId, sendMessageRequest.recipientId) &&
        Objects.equals(this.formatting, sendMessageRequest.formatting);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, channel, message, recipientId, formatting);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SendMessageRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    channel: ").append(toIndentedString(channel)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    recipientId: ").append(toIndentedString(recipientId)).append("\n");
    sb.append("    formatting: ").append(toIndentedString(formatting)).append("\n");
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

