package com.necpgame.mailservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.mailservice.model.AttachmentCurrency;
import com.necpgame.mailservice.model.AttachmentItem;
import com.necpgame.mailservice.model.AttachmentToken;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Attachment
 */


public class Attachment {

  private @Nullable String attachmentId;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    ITEM("ITEM"),
    
    CURRENCY("CURRENCY"),
    
    TOKEN("TOKEN");

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

  private @Nullable AttachmentItem item;

  private @Nullable AttachmentCurrency currency;

  private @Nullable AttachmentToken token;

  public Attachment() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Attachment(TypeEnum type) {
    this.type = type;
  }

  public Attachment attachmentId(@Nullable String attachmentId) {
    this.attachmentId = attachmentId;
    return this;
  }

  /**
   * Get attachmentId
   * @return attachmentId
   */
  
  @Schema(name = "attachmentId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attachmentId")
  public @Nullable String getAttachmentId() {
    return attachmentId;
  }

  public void setAttachmentId(@Nullable String attachmentId) {
    this.attachmentId = attachmentId;
  }

  public Attachment type(TypeEnum type) {
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

  public Attachment item(@Nullable AttachmentItem item) {
    this.item = item;
    return this;
  }

  /**
   * Get item
   * @return item
   */
  @Valid 
  @Schema(name = "item", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("item")
  public @Nullable AttachmentItem getItem() {
    return item;
  }

  public void setItem(@Nullable AttachmentItem item) {
    this.item = item;
  }

  public Attachment currency(@Nullable AttachmentCurrency currency) {
    this.currency = currency;
    return this;
  }

  /**
   * Get currency
   * @return currency
   */
  @Valid 
  @Schema(name = "currency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currency")
  public @Nullable AttachmentCurrency getCurrency() {
    return currency;
  }

  public void setCurrency(@Nullable AttachmentCurrency currency) {
    this.currency = currency;
  }

  public Attachment token(@Nullable AttachmentToken token) {
    this.token = token;
    return this;
  }

  /**
   * Get token
   * @return token
   */
  @Valid 
  @Schema(name = "token", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("token")
  public @Nullable AttachmentToken getToken() {
    return token;
  }

  public void setToken(@Nullable AttachmentToken token) {
    this.token = token;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Attachment attachment = (Attachment) o;
    return Objects.equals(this.attachmentId, attachment.attachmentId) &&
        Objects.equals(this.type, attachment.type) &&
        Objects.equals(this.item, attachment.item) &&
        Objects.equals(this.currency, attachment.currency) &&
        Objects.equals(this.token, attachment.token);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attachmentId, type, item, currency, token);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Attachment {\n");
    sb.append("    attachmentId: ").append(toIndentedString(attachmentId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    item: ").append(toIndentedString(item)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    token: ").append(toIndentedString(token)).append("\n");
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

