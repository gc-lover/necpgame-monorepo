package com.necpgame.backjava.model;

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
 * SendMail200Response
 */

@JsonTypeName("sendMail_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class SendMail200Response {

  private @Nullable String mailId;

  public SendMail200Response mailId(@Nullable String mailId) {
    this.mailId = mailId;
    return this;
  }

  /**
   * Get mailId
   * @return mailId
   */
  
  @Schema(name = "mail_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mail_id")
  public @Nullable String getMailId() {
    return mailId;
  }

  public void setMailId(@Nullable String mailId) {
    this.mailId = mailId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SendMail200Response sendMail200Response = (SendMail200Response) o;
    return Objects.equals(this.mailId, sendMail200Response.mailId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mailId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SendMail200Response {\n");
    sb.append("    mailId: ").append(toIndentedString(mailId)).append("\n");
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

