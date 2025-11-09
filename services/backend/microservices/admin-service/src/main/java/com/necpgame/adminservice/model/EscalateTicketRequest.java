package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * EscalateTicketRequest
 */


public class EscalateTicketRequest {

  /**
   * Gets or Sets newPriority
   */
  public enum NewPriorityEnum {
    HIGH("high"),
    
    CRITICAL("critical");

    private final String value;

    NewPriorityEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static NewPriorityEnum fromValue(String value) {
      for (NewPriorityEnum b : NewPriorityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private NewPriorityEnum newPriority;

  private @Nullable String escalateToRole;

  private @Nullable String incidentId;

  private Boolean notify = true;

  public EscalateTicketRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EscalateTicketRequest(NewPriorityEnum newPriority) {
    this.newPriority = newPriority;
  }

  public EscalateTicketRequest newPriority(NewPriorityEnum newPriority) {
    this.newPriority = newPriority;
    return this;
  }

  /**
   * Get newPriority
   * @return newPriority
   */
  @NotNull 
  @Schema(name = "newPriority", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("newPriority")
  public NewPriorityEnum getNewPriority() {
    return newPriority;
  }

  public void setNewPriority(NewPriorityEnum newPriority) {
    this.newPriority = newPriority;
  }

  public EscalateTicketRequest escalateToRole(@Nullable String escalateToRole) {
    this.escalateToRole = escalateToRole;
    return this;
  }

  /**
   * Get escalateToRole
   * @return escalateToRole
   */
  
  @Schema(name = "escalateToRole", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("escalateToRole")
  public @Nullable String getEscalateToRole() {
    return escalateToRole;
  }

  public void setEscalateToRole(@Nullable String escalateToRole) {
    this.escalateToRole = escalateToRole;
  }

  public EscalateTicketRequest incidentId(@Nullable String incidentId) {
    this.incidentId = incidentId;
    return this;
  }

  /**
   * Get incidentId
   * @return incidentId
   */
  
  @Schema(name = "incidentId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("incidentId")
  public @Nullable String getIncidentId() {
    return incidentId;
  }

  public void setIncidentId(@Nullable String incidentId) {
    this.incidentId = incidentId;
  }

  public EscalateTicketRequest notify(Boolean notify) {
    this.notify = notify;
    return this;
  }

  /**
   * Get notify
   * @return notify
   */
  
  @Schema(name = "notify", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notify")
  public Boolean getNotify() {
    return notify;
  }

  public void setNotify(Boolean notify) {
    this.notify = notify;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EscalateTicketRequest escalateTicketRequest = (EscalateTicketRequest) o;
    return Objects.equals(this.newPriority, escalateTicketRequest.newPriority) &&
        Objects.equals(this.escalateToRole, escalateTicketRequest.escalateToRole) &&
        Objects.equals(this.incidentId, escalateTicketRequest.incidentId) &&
        Objects.equals(this.notify, escalateTicketRequest.notify);
  }

  @Override
  public int hashCode() {
    return Objects.hash(newPriority, escalateToRole, incidentId, notify);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EscalateTicketRequest {\n");
    sb.append("    newPriority: ").append(toIndentedString(newPriority)).append("\n");
    sb.append("    escalateToRole: ").append(toIndentedString(escalateToRole)).append("\n");
    sb.append("    incidentId: ").append(toIndentedString(incidentId)).append("\n");
    sb.append("    notify: ").append(toIndentedString(notify)).append("\n");
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

