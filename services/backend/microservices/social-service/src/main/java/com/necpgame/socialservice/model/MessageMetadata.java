package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.MessageMetadataAttachmentsInner;
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
 * MessageMetadata
 */


public class MessageMetadata {

  /**
   * Gets or Sets messageType
   */
  public enum MessageTypeEnum {
    TEXT("TEXT"),
    
    SYSTEM("SYSTEM"),
    
    EMOTE("EMOTE");

    private final String value;

    MessageTypeEnum(String value) {
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
    public static MessageTypeEnum fromValue(String value) {
      for (MessageTypeEnum b : MessageTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable MessageTypeEnum messageType;

  private @Nullable Boolean edited;

  private @Nullable Boolean pinned;

  @Valid
  private List<@Valid MessageMetadataAttachmentsInner> attachments = new ArrayList<>();

  public MessageMetadata messageType(@Nullable MessageTypeEnum messageType) {
    this.messageType = messageType;
    return this;
  }

  /**
   * Get messageType
   * @return messageType
   */
  
  @Schema(name = "messageType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("messageType")
  public @Nullable MessageTypeEnum getMessageType() {
    return messageType;
  }

  public void setMessageType(@Nullable MessageTypeEnum messageType) {
    this.messageType = messageType;
  }

  public MessageMetadata edited(@Nullable Boolean edited) {
    this.edited = edited;
    return this;
  }

  /**
   * Get edited
   * @return edited
   */
  
  @Schema(name = "edited", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("edited")
  public @Nullable Boolean getEdited() {
    return edited;
  }

  public void setEdited(@Nullable Boolean edited) {
    this.edited = edited;
  }

  public MessageMetadata pinned(@Nullable Boolean pinned) {
    this.pinned = pinned;
    return this;
  }

  /**
   * Get pinned
   * @return pinned
   */
  
  @Schema(name = "pinned", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pinned")
  public @Nullable Boolean getPinned() {
    return pinned;
  }

  public void setPinned(@Nullable Boolean pinned) {
    this.pinned = pinned;
  }

  public MessageMetadata attachments(List<@Valid MessageMetadataAttachmentsInner> attachments) {
    this.attachments = attachments;
    return this;
  }

  public MessageMetadata addAttachmentsItem(MessageMetadataAttachmentsInner attachmentsItem) {
    if (this.attachments == null) {
      this.attachments = new ArrayList<>();
    }
    this.attachments.add(attachmentsItem);
    return this;
  }

  /**
   * Get attachments
   * @return attachments
   */
  @Valid 
  @Schema(name = "attachments", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attachments")
  public List<@Valid MessageMetadataAttachmentsInner> getAttachments() {
    return attachments;
  }

  public void setAttachments(List<@Valid MessageMetadataAttachmentsInner> attachments) {
    this.attachments = attachments;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MessageMetadata messageMetadata = (MessageMetadata) o;
    return Objects.equals(this.messageType, messageMetadata.messageType) &&
        Objects.equals(this.edited, messageMetadata.edited) &&
        Objects.equals(this.pinned, messageMetadata.pinned) &&
        Objects.equals(this.attachments, messageMetadata.attachments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageType, edited, pinned, attachments);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MessageMetadata {\n");
    sb.append("    messageType: ").append(toIndentedString(messageType)).append("\n");
    sb.append("    edited: ").append(toIndentedString(edited)).append("\n");
    sb.append("    pinned: ").append(toIndentedString(pinned)).append("\n");
    sb.append("    attachments: ").append(toIndentedString(attachments)).append("\n");
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

