package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.MailMessage;
import com.necpgame.backjava.model.PaginationMeta;
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
 * GetInbox200Response
 */

@JsonTypeName("getInbox_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetInbox200Response {

  @Valid
  private List<@Valid MailMessage> mail = new ArrayList<>();

  private @Nullable PaginationMeta pagination;

  public GetInbox200Response mail(List<@Valid MailMessage> mail) {
    this.mail = mail;
    return this;
  }

  public GetInbox200Response addMailItem(MailMessage mailItem) {
    if (this.mail == null) {
      this.mail = new ArrayList<>();
    }
    this.mail.add(mailItem);
    return this;
  }

  /**
   * Get mail
   * @return mail
   */
  @Valid 
  @Schema(name = "mail", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mail")
  public List<@Valid MailMessage> getMail() {
    return mail;
  }

  public void setMail(List<@Valid MailMessage> mail) {
    this.mail = mail;
  }

  public GetInbox200Response pagination(@Nullable PaginationMeta pagination) {
    this.pagination = pagination;
    return this;
  }

  /**
   * Get pagination
   * @return pagination
   */
  @Valid 
  @Schema(name = "pagination", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pagination")
  public @Nullable PaginationMeta getPagination() {
    return pagination;
  }

  public void setPagination(@Nullable PaginationMeta pagination) {
    this.pagination = pagination;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetInbox200Response getInbox200Response = (GetInbox200Response) o;
    return Objects.equals(this.mail, getInbox200Response.mail) &&
        Objects.equals(this.pagination, getInbox200Response.pagination);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mail, pagination);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetInbox200Response {\n");
    sb.append("    mail: ").append(toIndentedString(mail)).append("\n");
    sb.append("    pagination: ").append(toIndentedString(pagination)).append("\n");
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

