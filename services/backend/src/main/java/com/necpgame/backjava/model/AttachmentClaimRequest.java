package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * AttachmentClaimRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class AttachmentClaimRequest {

  @Valid
  private List<String> attachmentIds = new ArrayList<>();

  public AttachmentClaimRequest attachmentIds(List<String> attachmentIds) {
    this.attachmentIds = attachmentIds;
    return this;
  }

  public AttachmentClaimRequest addAttachmentIdsItem(String attachmentIdsItem) {
    if (this.attachmentIds == null) {
      this.attachmentIds = new ArrayList<>();
    }
    this.attachmentIds.add(attachmentIdsItem);
    return this;
  }

  /**
   * Get attachmentIds
   * @return attachmentIds
   */
  
  @Schema(name = "attachmentIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attachmentIds")
  public List<String> getAttachmentIds() {
    return attachmentIds;
  }

  public void setAttachmentIds(List<String> attachmentIds) {
    this.attachmentIds = attachmentIds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AttachmentClaimRequest attachmentClaimRequest = (AttachmentClaimRequest) o;
    return Objects.equals(this.attachmentIds, attachmentClaimRequest.attachmentIds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attachmentIds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AttachmentClaimRequest {\n");
    sb.append("    attachmentIds: ").append(toIndentedString(attachmentIds)).append("\n");
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

