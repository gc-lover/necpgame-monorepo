package com.necpgame.authservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
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
 * BlacklistStatusResponse
 */


public class BlacklistStatusResponse {

  private @Nullable String tokenId;

  private @Nullable Boolean blacklisted;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime blacklistedAt;

  private @Nullable String comment;

  public BlacklistStatusResponse tokenId(@Nullable String tokenId) {
    this.tokenId = tokenId;
    return this;
  }

  /**
   * Get tokenId
   * @return tokenId
   */
  
  @Schema(name = "token_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("token_id")
  public @Nullable String getTokenId() {
    return tokenId;
  }

  public void setTokenId(@Nullable String tokenId) {
    this.tokenId = tokenId;
  }

  public BlacklistStatusResponse blacklisted(@Nullable Boolean blacklisted) {
    this.blacklisted = blacklisted;
    return this;
  }

  /**
   * Get blacklisted
   * @return blacklisted
   */
  
  @Schema(name = "blacklisted", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("blacklisted")
  public @Nullable Boolean getBlacklisted() {
    return blacklisted;
  }

  public void setBlacklisted(@Nullable Boolean blacklisted) {
    this.blacklisted = blacklisted;
  }

  public BlacklistStatusResponse blacklistedAt(@Nullable OffsetDateTime blacklistedAt) {
    this.blacklistedAt = blacklistedAt;
    return this;
  }

  /**
   * Get blacklistedAt
   * @return blacklistedAt
   */
  @Valid 
  @Schema(name = "blacklisted_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("blacklisted_at")
  public @Nullable OffsetDateTime getBlacklistedAt() {
    return blacklistedAt;
  }

  public void setBlacklistedAt(@Nullable OffsetDateTime blacklistedAt) {
    this.blacklistedAt = blacklistedAt;
  }

  public BlacklistStatusResponse comment(@Nullable String comment) {
    this.comment = comment;
    return this;
  }

  /**
   * Get comment
   * @return comment
   */
  
  @Schema(name = "comment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("comment")
  public @Nullable String getComment() {
    return comment;
  }

  public void setComment(@Nullable String comment) {
    this.comment = comment;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BlacklistStatusResponse blacklistStatusResponse = (BlacklistStatusResponse) o;
    return Objects.equals(this.tokenId, blacklistStatusResponse.tokenId) &&
        Objects.equals(this.blacklisted, blacklistStatusResponse.blacklisted) &&
        Objects.equals(this.blacklistedAt, blacklistStatusResponse.blacklistedAt) &&
        Objects.equals(this.comment, blacklistStatusResponse.comment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tokenId, blacklisted, blacklistedAt, comment);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BlacklistStatusResponse {\n");
    sb.append("    tokenId: ").append(toIndentedString(tokenId)).append("\n");
    sb.append("    blacklisted: ").append(toIndentedString(blacklisted)).append("\n");
    sb.append("    blacklistedAt: ").append(toIndentedString(blacklistedAt)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
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

