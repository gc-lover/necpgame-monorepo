package com.necpgame.mailservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AttachmentToken
 */

@JsonTypeName("Attachment_token")

public class AttachmentToken {

  private @Nullable String tokenId;

  private @Nullable Integer amount;

  public AttachmentToken tokenId(@Nullable String tokenId) {
    this.tokenId = tokenId;
    return this;
  }

  /**
   * Get tokenId
   * @return tokenId
   */
  
  @Schema(name = "tokenId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tokenId")
  public @Nullable String getTokenId() {
    return tokenId;
  }

  public void setTokenId(@Nullable String tokenId) {
    this.tokenId = tokenId;
  }

  public AttachmentToken amount(@Nullable Integer amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * @return amount
   */
  
  @Schema(name = "amount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("amount")
  public @Nullable Integer getAmount() {
    return amount;
  }

  public void setAmount(@Nullable Integer amount) {
    this.amount = amount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AttachmentToken attachmentToken = (AttachmentToken) o;
    return Objects.equals(this.tokenId, attachmentToken.tokenId) &&
        Objects.equals(this.amount, attachmentToken.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tokenId, amount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AttachmentToken {\n");
    sb.append("    tokenId: ").append(toIndentedString(tokenId)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
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

