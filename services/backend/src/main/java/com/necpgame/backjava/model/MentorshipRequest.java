package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * MentorshipRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class MentorshipRequest {

  private UUID studentId;

  private String mentorId;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    COMBAT("COMBAT"),
    
    TECH("TECH"),
    
    NETRUNNING("NETRUNNING"),
    
    SOCIAL("SOCIAL"),
    
    ECONOMY("ECONOMY"),
    
    MEDICAL("MEDICAL");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TypeEnum type;

  private @Nullable Integer paymentOffered;

  public MentorshipRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MentorshipRequest(UUID studentId, String mentorId, TypeEnum type) {
    this.studentId = studentId;
    this.mentorId = mentorId;
    this.type = type;
  }

  public MentorshipRequest studentId(UUID studentId) {
    this.studentId = studentId;
    return this;
  }

  /**
   * Get studentId
   * @return studentId
   */
  @NotNull @Valid 
  @Schema(name = "student_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("student_id")
  public UUID getStudentId() {
    return studentId;
  }

  public void setStudentId(UUID studentId) {
    this.studentId = studentId;
  }

  public MentorshipRequest mentorId(String mentorId) {
    this.mentorId = mentorId;
    return this;
  }

  /**
   * Get mentorId
   * @return mentorId
   */
  @NotNull 
  @Schema(name = "mentor_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("mentor_id")
  public String getMentorId() {
    return mentorId;
  }

  public void setMentorId(String mentorId) {
    this.mentorId = mentorId;
  }

  public MentorshipRequest type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @NotNull 
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public MentorshipRequest paymentOffered(@Nullable Integer paymentOffered) {
    this.paymentOffered = paymentOffered;
    return this;
  }

  /**
   * Get paymentOffered
   * @return paymentOffered
   */
  
  @Schema(name = "payment_offered", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("payment_offered")
  public @Nullable Integer getPaymentOffered() {
    return paymentOffered;
  }

  public void setPaymentOffered(@Nullable Integer paymentOffered) {
    this.paymentOffered = paymentOffered;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MentorshipRequest mentorshipRequest = (MentorshipRequest) o;
    return Objects.equals(this.studentId, mentorshipRequest.studentId) &&
        Objects.equals(this.mentorId, mentorshipRequest.mentorId) &&
        Objects.equals(this.type, mentorshipRequest.type) &&
        Objects.equals(this.paymentOffered, mentorshipRequest.paymentOffered);
  }

  @Override
  public int hashCode() {
    return Objects.hash(studentId, mentorId, type, paymentOffered);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MentorshipRequest {\n");
    sb.append("    studentId: ").append(toIndentedString(studentId)).append("\n");
    sb.append("    mentorId: ").append(toIndentedString(mentorId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    paymentOffered: ").append(toIndentedString(paymentOffered)).append("\n");
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

