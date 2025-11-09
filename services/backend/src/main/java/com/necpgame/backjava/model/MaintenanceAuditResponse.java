package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.MaintenanceAuditEntry;
import com.necpgame.backjava.model.Page;
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
 * MaintenanceAuditResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class MaintenanceAuditResponse {

  @Valid
  private List<@Valid MaintenanceAuditEntry> entries = new ArrayList<>();

  private @Nullable Page page;

  public MaintenanceAuditResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MaintenanceAuditResponse(List<@Valid MaintenanceAuditEntry> entries) {
    this.entries = entries;
  }

  public MaintenanceAuditResponse entries(List<@Valid MaintenanceAuditEntry> entries) {
    this.entries = entries;
    return this;
  }

  public MaintenanceAuditResponse addEntriesItem(MaintenanceAuditEntry entriesItem) {
    if (this.entries == null) {
      this.entries = new ArrayList<>();
    }
    this.entries.add(entriesItem);
    return this;
  }

  /**
   * Get entries
   * @return entries
   */
  @NotNull @Valid 
  @Schema(name = "entries", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("entries")
  public List<@Valid MaintenanceAuditEntry> getEntries() {
    return entries;
  }

  public void setEntries(List<@Valid MaintenanceAuditEntry> entries) {
    this.entries = entries;
  }

  public MaintenanceAuditResponse page(@Nullable Page page) {
    this.page = page;
    return this;
  }

  /**
   * Get page
   * @return page
   */
  @Valid 
  @Schema(name = "page", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("page")
  public @Nullable Page getPage() {
    return page;
  }

  public void setPage(@Nullable Page page) {
    this.page = page;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MaintenanceAuditResponse maintenanceAuditResponse = (MaintenanceAuditResponse) o;
    return Objects.equals(this.entries, maintenanceAuditResponse.entries) &&
        Objects.equals(this.page, maintenanceAuditResponse.page);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entries, page);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaintenanceAuditResponse {\n");
    sb.append("    entries: ").append(toIndentedString(entries)).append("\n");
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
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

