package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SetActiveTitleRequest
 */

@JsonTypeName("setActiveTitle_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class SetActiveTitleRequest {

  private @Nullable UUID titleId;

  public SetActiveTitleRequest titleId(@Nullable UUID titleId) {
    this.titleId = titleId;
    return this;
  }

  /**
   * Get titleId
   * @return titleId
   */
  @Valid 
  @Schema(name = "title_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title_id")
  public @Nullable UUID getTitleId() {
    return titleId;
  }

  public void setTitleId(@Nullable UUID titleId) {
    this.titleId = titleId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SetActiveTitleRequest setActiveTitleRequest = (SetActiveTitleRequest) o;
    return Objects.equals(this.titleId, setActiveTitleRequest.titleId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(titleId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SetActiveTitleRequest {\n");
    sb.append("    titleId: ").append(toIndentedString(titleId)).append("\n");
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

