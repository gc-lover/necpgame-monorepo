package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.StateSnapshot;
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
 * GetStateSnapshots200Response
 */

@JsonTypeName("getStateSnapshots_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetStateSnapshots200Response {

  @Valid
  private List<@Valid StateSnapshot> snapshots = new ArrayList<>();

  public GetStateSnapshots200Response snapshots(List<@Valid StateSnapshot> snapshots) {
    this.snapshots = snapshots;
    return this;
  }

  public GetStateSnapshots200Response addSnapshotsItem(StateSnapshot snapshotsItem) {
    if (this.snapshots == null) {
      this.snapshots = new ArrayList<>();
    }
    this.snapshots.add(snapshotsItem);
    return this;
  }

  /**
   * Get snapshots
   * @return snapshots
   */
  @Valid 
  @Schema(name = "snapshots", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("snapshots")
  public List<@Valid StateSnapshot> getSnapshots() {
    return snapshots;
  }

  public void setSnapshots(List<@Valid StateSnapshot> snapshots) {
    this.snapshots = snapshots;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetStateSnapshots200Response getStateSnapshots200Response = (GetStateSnapshots200Response) o;
    return Objects.equals(this.snapshots, getStateSnapshots200Response.snapshots);
  }

  @Override
  public int hashCode() {
    return Objects.hash(snapshots);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetStateSnapshots200Response {\n");
    sb.append("    snapshots: ").append(toIndentedString(snapshots)).append("\n");
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

