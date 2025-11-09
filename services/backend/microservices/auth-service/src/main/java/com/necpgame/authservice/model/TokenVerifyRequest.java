package com.necpgame.authservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TokenVerifyRequest
 */


public class TokenVerifyRequest {

  private String token;

  private @Nullable String audience;

  public TokenVerifyRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TokenVerifyRequest(String token) {
    this.token = token;
  }

  public TokenVerifyRequest token(String token) {
    this.token = token;
    return this;
  }

  /**
   * Get token
   * @return token
   */
  @NotNull 
  @Schema(name = "token", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("token")
  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public TokenVerifyRequest audience(@Nullable String audience) {
    this.audience = audience;
    return this;
  }

  /**
   * Ожидаемая аудитория токена (для внутренних сервисов)
   * @return audience
   */
  
  @Schema(name = "audience", description = "Ожидаемая аудитория токена (для внутренних сервисов)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("audience")
  public @Nullable String getAudience() {
    return audience;
  }

  public void setAudience(@Nullable String audience) {
    this.audience = audience;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TokenVerifyRequest tokenVerifyRequest = (TokenVerifyRequest) o;
    return Objects.equals(this.token, tokenVerifyRequest.token) &&
        Objects.equals(this.audience, tokenVerifyRequest.audience);
  }

  @Override
  public int hashCode() {
    return Objects.hash(token, audience);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TokenVerifyRequest {\n");
    sb.append("    token: ").append(toIndentedString(token)).append("\n");
    sb.append("    audience: ").append(toIndentedString(audience)).append("\n");
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

