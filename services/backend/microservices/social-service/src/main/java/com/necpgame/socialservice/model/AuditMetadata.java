package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import java.util.Arrays;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AuditMetadata
 */


public class AuditMetadata {

  private String createdBy;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  private JsonNullable<String> approvedBy = JsonNullable.<String>undefined();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<OffsetDateTime> approvedAt = JsonNullable.<OffsetDateTime>undefined();

  private JsonNullable<String> notes = JsonNullable.<String>undefined();

  public AuditMetadata() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AuditMetadata(String createdBy, OffsetDateTime createdAt) {
    this.createdBy = createdBy;
    this.createdAt = createdAt;
  }

  public AuditMetadata createdBy(String createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  /**
   * Get createdBy
   * @return createdBy
   */
  @NotNull 
  @Schema(name = "createdBy", example = "player-9841", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("createdBy")
  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public AuditMetadata createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @NotNull @Valid 
  @Schema(name = "createdAt", example = "2077-05-18T13:22:10Z", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("createdAt")
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public AuditMetadata approvedBy(String approvedBy) {
    this.approvedBy = JsonNullable.of(approvedBy);
    return this;
  }

  /**
   * Get approvedBy
   * @return approvedBy
   */
  
  @Schema(name = "approvedBy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("approvedBy")
  public JsonNullable<String> getApprovedBy() {
    return approvedBy;
  }

  public void setApprovedBy(JsonNullable<String> approvedBy) {
    this.approvedBy = approvedBy;
  }

  public AuditMetadata approvedAt(OffsetDateTime approvedAt) {
    this.approvedAt = JsonNullable.of(approvedAt);
    return this;
  }

  /**
   * Get approvedAt
   * @return approvedAt
   */
  @Valid 
  @Schema(name = "approvedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("approvedAt")
  public JsonNullable<OffsetDateTime> getApprovedAt() {
    return approvedAt;
  }

  public void setApprovedAt(JsonNullable<OffsetDateTime> approvedAt) {
    this.approvedAt = approvedAt;
  }

  public AuditMetadata notes(String notes) {
    this.notes = JsonNullable.of(notes);
    return this;
  }

  /**
   * Get notes
   * @return notes
   */
  
  @Schema(name = "notes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notes")
  public JsonNullable<String> getNotes() {
    return notes;
  }

  public void setNotes(JsonNullable<String> notes) {
    this.notes = notes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuditMetadata auditMetadata = (AuditMetadata) o;
    return Objects.equals(this.createdBy, auditMetadata.createdBy) &&
        Objects.equals(this.createdAt, auditMetadata.createdAt) &&
        equalsNullable(this.approvedBy, auditMetadata.approvedBy) &&
        equalsNullable(this.approvedAt, auditMetadata.approvedAt) &&
        equalsNullable(this.notes, auditMetadata.notes);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(createdBy, createdAt, hashCodeNullable(approvedBy), hashCodeNullable(approvedAt), hashCodeNullable(notes));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AuditMetadata {\n");
    sb.append("    createdBy: ").append(toIndentedString(createdBy)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    approvedBy: ").append(toIndentedString(approvedBy)).append("\n");
    sb.append("    approvedAt: ").append(toIndentedString(approvedAt)).append("\n");
    sb.append("    notes: ").append(toIndentedString(notes)).append("\n");
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

