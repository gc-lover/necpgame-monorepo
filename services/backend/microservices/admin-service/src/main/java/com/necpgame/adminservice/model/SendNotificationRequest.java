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
 * SendNotificationRequest
 */

@JsonTypeName("sendNotification_request")

public class SendNotificationRequest {

  private String playerId;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    SYSTEM("system"),
    
    QUEST("quest"),
    
    TRADE("trade"),
    
    PARTY("party"),
    
    GUILD("guild"),
    
    FRIEND("friend"),
    
    ACHIEVEMENT("achievement");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TypeEnum type;

  /**
   * Gets or Sets priority
   */
  public enum PriorityEnum {
    LOW("low"),
    
    NORMAL("normal"),
    
    HIGH("high"),
    
    URGENT("urgent");

    private final String value;

    PriorityEnum(String value) {
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
    public static PriorityEnum fromValue(String value) {
      for (PriorityEnum b : PriorityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private PriorityEnum priority = PriorityEnum.NORMAL;

  private String message;

  private @Nullable Object data;

  private Boolean sendEmail = false;

  public SendNotificationRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SendNotificationRequest(String playerId, TypeEnum type, String message) {
    this.playerId = playerId;
    this.type = type;
    this.message = message;
  }

  public SendNotificationRequest playerId(String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull 
  @Schema(name = "player_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("player_id")
  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

  public SendNotificationRequest type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @NotNull 
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public SendNotificationRequest priority(PriorityEnum priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Get priority
   * @return priority
   */
  
  @Schema(name = "priority", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("priority")
  public PriorityEnum getPriority() {
    return priority;
  }

  public void setPriority(PriorityEnum priority) {
    this.priority = priority;
  }

  public SendNotificationRequest message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   */
  @NotNull 
  @Schema(name = "message", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public SendNotificationRequest data(@Nullable Object data) {
    this.data = data;
    return this;
  }

  /**
   * Дополнительные данные (quest_id, trade_id, etc.)
   * @return data
   */
  
  @Schema(name = "data", description = "Дополнительные данные (quest_id, trade_id, etc.)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("data")
  public @Nullable Object getData() {
    return data;
  }

  public void setData(@Nullable Object data) {
    this.data = data;
  }

  public SendNotificationRequest sendEmail(Boolean sendEmail) {
    this.sendEmail = sendEmail;
    return this;
  }

  /**
   * Get sendEmail
   * @return sendEmail
   */
  
  @Schema(name = "send_email", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("send_email")
  public Boolean getSendEmail() {
    return sendEmail;
  }

  public void setSendEmail(Boolean sendEmail) {
    this.sendEmail = sendEmail;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SendNotificationRequest sendNotificationRequest = (SendNotificationRequest) o;
    return Objects.equals(this.playerId, sendNotificationRequest.playerId) &&
        Objects.equals(this.type, sendNotificationRequest.type) &&
        Objects.equals(this.priority, sendNotificationRequest.priority) &&
        Objects.equals(this.message, sendNotificationRequest.message) &&
        Objects.equals(this.data, sendNotificationRequest.data) &&
        Objects.equals(this.sendEmail, sendNotificationRequest.sendEmail);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, type, priority, message, data, sendEmail);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SendNotificationRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    sendEmail: ").append(toIndentedString(sendEmail)).append("\n");
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

