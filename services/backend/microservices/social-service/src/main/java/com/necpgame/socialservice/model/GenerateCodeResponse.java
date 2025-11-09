package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.ReferralStats;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GenerateCodeResponse
 */


public class GenerateCodeResponse {

  private @Nullable String code;

  private @Nullable String shareUrl;

  private @Nullable String qrcodeUrl;

  private @Nullable ReferralStats stats;

  public GenerateCodeResponse code(@Nullable String code) {
    this.code = code;
    return this;
  }

  /**
   * Get code
   * @return code
   */
  
  @Schema(name = "code", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("code")
  public @Nullable String getCode() {
    return code;
  }

  public void setCode(@Nullable String code) {
    this.code = code;
  }

  public GenerateCodeResponse shareUrl(@Nullable String shareUrl) {
    this.shareUrl = shareUrl;
    return this;
  }

  /**
   * Get shareUrl
   * @return shareUrl
   */
  
  @Schema(name = "shareUrl", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("shareUrl")
  public @Nullable String getShareUrl() {
    return shareUrl;
  }

  public void setShareUrl(@Nullable String shareUrl) {
    this.shareUrl = shareUrl;
  }

  public GenerateCodeResponse qrcodeUrl(@Nullable String qrcodeUrl) {
    this.qrcodeUrl = qrcodeUrl;
    return this;
  }

  /**
   * Get qrcodeUrl
   * @return qrcodeUrl
   */
  
  @Schema(name = "qrcodeUrl", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("qrcodeUrl")
  public @Nullable String getQrcodeUrl() {
    return qrcodeUrl;
  }

  public void setQrcodeUrl(@Nullable String qrcodeUrl) {
    this.qrcodeUrl = qrcodeUrl;
  }

  public GenerateCodeResponse stats(@Nullable ReferralStats stats) {
    this.stats = stats;
    return this;
  }

  /**
   * Get stats
   * @return stats
   */
  @Valid 
  @Schema(name = "stats", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stats")
  public @Nullable ReferralStats getStats() {
    return stats;
  }

  public void setStats(@Nullable ReferralStats stats) {
    this.stats = stats;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenerateCodeResponse generateCodeResponse = (GenerateCodeResponse) o;
    return Objects.equals(this.code, generateCodeResponse.code) &&
        Objects.equals(this.shareUrl, generateCodeResponse.shareUrl) &&
        Objects.equals(this.qrcodeUrl, generateCodeResponse.qrcodeUrl) &&
        Objects.equals(this.stats, generateCodeResponse.stats);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, shareUrl, qrcodeUrl, stats);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenerateCodeResponse {\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    shareUrl: ").append(toIndentedString(shareUrl)).append("\n");
    sb.append("    qrcodeUrl: ").append(toIndentedString(qrcodeUrl)).append("\n");
    sb.append("    stats: ").append(toIndentedString(stats)).append("\n");
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

