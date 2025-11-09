package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SocialInviteRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class SocialInviteRequest {

  private String recipientId;

  /**
   * Gets or Sets context
   */
  public enum ContextEnum {
    PARTY("PARTY"),
    
    GUILD("GUILD"),
    
    MATCH("MATCH"),
    
    OTHER("OTHER");

    private final String value;

    ContextEnum(String value) {
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
    public static ContextEnum fromValue(String value) {
      for (ContextEnum b : ContextEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ContextEnum context;

  @Valid
  private Map<String, Object> payload = new HashMap<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  public SocialInviteRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SocialInviteRequest(String recipientId, ContextEnum context) {
    this.recipientId = recipientId;
    this.context = context;
  }

  public SocialInviteRequest recipientId(String recipientId) {
    this.recipientId = recipientId;
    return this;
  }

  /**
   * Get recipientId
   * @return recipientId
   */
  @NotNull 
  @Schema(name = "recipientId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("recipientId")
  public String getRecipientId() {
    return recipientId;
  }

  public void setRecipientId(String recipientId) {
    this.recipientId = recipientId;
  }

  public SocialInviteRequest context(ContextEnum context) {
    this.context = context;
    return this;
  }

  /**
   * Get context
   * @return context
   */
  @NotNull 
  @Schema(name = "context", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("context")
  public ContextEnum getContext() {
    return context;
  }

  public void setContext(ContextEnum context) {
    this.context = context;
  }

  public SocialInviteRequest payload(Map<String, Object> payload) {
    this.payload = payload;
    return this;
  }

  public SocialInviteRequest putPayloadItem(String key, Object payloadItem) {
    if (this.payload == null) {
      this.payload = new HashMap<>();
    }
    this.payload.put(key, payloadItem);
    return this;
  }

  /**
   * Get payload
   * @return payload
   */
  
  @Schema(name = "payload", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("payload")
  public Map<String, Object> getPayload() {
    return payload;
  }

  public void setPayload(Map<String, Object> payload) {
    this.payload = payload;
  }

  public SocialInviteRequest expiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Get expiresAt
   * @return expiresAt
   */
  @Valid 
  @Schema(name = "expiresAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expiresAt")
  public @Nullable OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SocialInviteRequest socialInviteRequest = (SocialInviteRequest) o;
    return Objects.equals(this.recipientId, socialInviteRequest.recipientId) &&
        Objects.equals(this.context, socialInviteRequest.context) &&
        Objects.equals(this.payload, socialInviteRequest.payload) &&
        Objects.equals(this.expiresAt, socialInviteRequest.expiresAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recipientId, context, payload, expiresAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SocialInviteRequest {\n");
    sb.append("    recipientId: ").append(toIndentedString(recipientId)).append("\n");
    sb.append("    context: ").append(toIndentedString(context)).append("\n");
    sb.append("    payload: ").append(toIndentedString(payload)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
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

