package com.necpgame.socialservice.model;

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
 * PlayerOrderCancellation
 */


public class PlayerOrderCancellation {

  private String reason;

  private Boolean refundEscrow = true;

  private @Nullable String notes;

  private Boolean notifyInvitees = true;

  public PlayerOrderCancellation() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderCancellation(String reason) {
    this.reason = reason;
  }

  public PlayerOrderCancellation reason(String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Причина отмены.
   * @return reason
   */
  @NotNull 
  @Schema(name = "reason", description = "Причина отмены.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reason")
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public PlayerOrderCancellation refundEscrow(Boolean refundEscrow) {
    this.refundEscrow = refundEscrow;
    return this;
  }

  /**
   * Get refundEscrow
   * @return refundEscrow
   */
  
  @Schema(name = "refundEscrow", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("refundEscrow")
  public Boolean getRefundEscrow() {
    return refundEscrow;
  }

  public void setRefundEscrow(Boolean refundEscrow) {
    this.refundEscrow = refundEscrow;
  }

  public PlayerOrderCancellation notes(@Nullable String notes) {
    this.notes = notes;
    return this;
  }

  /**
   * Get notes
   * @return notes
   */
  
  @Schema(name = "notes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notes")
  public @Nullable String getNotes() {
    return notes;
  }

  public void setNotes(@Nullable String notes) {
    this.notes = notes;
  }

  public PlayerOrderCancellation notifyInvitees(Boolean notifyInvitees) {
    this.notifyInvitees = notifyInvitees;
    return this;
  }

  /**
   * Get notifyInvitees
   * @return notifyInvitees
   */
  
  @Schema(name = "notifyInvitees", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notifyInvitees")
  public Boolean getNotifyInvitees() {
    return notifyInvitees;
  }

  public void setNotifyInvitees(Boolean notifyInvitees) {
    this.notifyInvitees = notifyInvitees;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderCancellation playerOrderCancellation = (PlayerOrderCancellation) o;
    return Objects.equals(this.reason, playerOrderCancellation.reason) &&
        Objects.equals(this.refundEscrow, playerOrderCancellation.refundEscrow) &&
        Objects.equals(this.notes, playerOrderCancellation.notes) &&
        Objects.equals(this.notifyInvitees, playerOrderCancellation.notifyInvitees);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reason, refundEscrow, notes, notifyInvitees);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderCancellation {\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    refundEscrow: ").append(toIndentedString(refundEscrow)).append("\n");
    sb.append("    notes: ").append(toIndentedString(notes)).append("\n");
    sb.append("    notifyInvitees: ").append(toIndentedString(notifyInvitees)).append("\n");
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

