package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.UUID;
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
 * MentorshipRelationship
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class MentorshipRelationship {

  private @Nullable UUID relationshipId;

  private @Nullable UUID studentId;

  private @Nullable String mentorId;

  private @Nullable String type;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    PENDING("PENDING"),
    
    ACTIVE("ACTIVE"),
    
    GRADUATED("GRADUATED"),
    
    TERMINATED("TERMINATED");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  private @Nullable Integer lessonsCompleted;

  private @Nullable Integer lessonsTotal;

  private @Nullable BigDecimal progressPercentage;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<OffsetDateTime> graduatedAt = JsonNullable.<OffsetDateTime>undefined();

  public MentorshipRelationship relationshipId(@Nullable UUID relationshipId) {
    this.relationshipId = relationshipId;
    return this;
  }

  /**
   * Get relationshipId
   * @return relationshipId
   */
  @Valid 
  @Schema(name = "relationship_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relationship_id")
  public @Nullable UUID getRelationshipId() {
    return relationshipId;
  }

  public void setRelationshipId(@Nullable UUID relationshipId) {
    this.relationshipId = relationshipId;
  }

  public MentorshipRelationship studentId(@Nullable UUID studentId) {
    this.studentId = studentId;
    return this;
  }

  /**
   * Get studentId
   * @return studentId
   */
  @Valid 
  @Schema(name = "student_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("student_id")
  public @Nullable UUID getStudentId() {
    return studentId;
  }

  public void setStudentId(@Nullable UUID studentId) {
    this.studentId = studentId;
  }

  public MentorshipRelationship mentorId(@Nullable String mentorId) {
    this.mentorId = mentorId;
    return this;
  }

  /**
   * Get mentorId
   * @return mentorId
   */
  
  @Schema(name = "mentor_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mentor_id")
  public @Nullable String getMentorId() {
    return mentorId;
  }

  public void setMentorId(@Nullable String mentorId) {
    this.mentorId = mentorId;
  }

  public MentorshipRelationship type(@Nullable String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable String getType() {
    return type;
  }

  public void setType(@Nullable String type) {
    this.type = type;
  }

  public MentorshipRelationship status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public MentorshipRelationship lessonsCompleted(@Nullable Integer lessonsCompleted) {
    this.lessonsCompleted = lessonsCompleted;
    return this;
  }

  /**
   * Get lessonsCompleted
   * @return lessonsCompleted
   */
  
  @Schema(name = "lessons_completed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lessons_completed")
  public @Nullable Integer getLessonsCompleted() {
    return lessonsCompleted;
  }

  public void setLessonsCompleted(@Nullable Integer lessonsCompleted) {
    this.lessonsCompleted = lessonsCompleted;
  }

  public MentorshipRelationship lessonsTotal(@Nullable Integer lessonsTotal) {
    this.lessonsTotal = lessonsTotal;
    return this;
  }

  /**
   * Get lessonsTotal
   * @return lessonsTotal
   */
  
  @Schema(name = "lessons_total", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lessons_total")
  public @Nullable Integer getLessonsTotal() {
    return lessonsTotal;
  }

  public void setLessonsTotal(@Nullable Integer lessonsTotal) {
    this.lessonsTotal = lessonsTotal;
  }

  public MentorshipRelationship progressPercentage(@Nullable BigDecimal progressPercentage) {
    this.progressPercentage = progressPercentage;
    return this;
  }

  /**
   * Get progressPercentage
   * @return progressPercentage
   */
  @Valid 
  @Schema(name = "progress_percentage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("progress_percentage")
  public @Nullable BigDecimal getProgressPercentage() {
    return progressPercentage;
  }

  public void setProgressPercentage(@Nullable BigDecimal progressPercentage) {
    this.progressPercentage = progressPercentage;
  }

  public MentorshipRelationship startedAt(@Nullable OffsetDateTime startedAt) {
    this.startedAt = startedAt;
    return this;
  }

  /**
   * Get startedAt
   * @return startedAt
   */
  @Valid 
  @Schema(name = "started_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("started_at")
  public @Nullable OffsetDateTime getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(@Nullable OffsetDateTime startedAt) {
    this.startedAt = startedAt;
  }

  public MentorshipRelationship graduatedAt(OffsetDateTime graduatedAt) {
    this.graduatedAt = JsonNullable.of(graduatedAt);
    return this;
  }

  /**
   * Get graduatedAt
   * @return graduatedAt
   */
  @Valid 
  @Schema(name = "graduated_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("graduated_at")
  public JsonNullable<OffsetDateTime> getGraduatedAt() {
    return graduatedAt;
  }

  public void setGraduatedAt(JsonNullable<OffsetDateTime> graduatedAt) {
    this.graduatedAt = graduatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MentorshipRelationship mentorshipRelationship = (MentorshipRelationship) o;
    return Objects.equals(this.relationshipId, mentorshipRelationship.relationshipId) &&
        Objects.equals(this.studentId, mentorshipRelationship.studentId) &&
        Objects.equals(this.mentorId, mentorshipRelationship.mentorId) &&
        Objects.equals(this.type, mentorshipRelationship.type) &&
        Objects.equals(this.status, mentorshipRelationship.status) &&
        Objects.equals(this.lessonsCompleted, mentorshipRelationship.lessonsCompleted) &&
        Objects.equals(this.lessonsTotal, mentorshipRelationship.lessonsTotal) &&
        Objects.equals(this.progressPercentage, mentorshipRelationship.progressPercentage) &&
        Objects.equals(this.startedAt, mentorshipRelationship.startedAt) &&
        equalsNullable(this.graduatedAt, mentorshipRelationship.graduatedAt);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(relationshipId, studentId, mentorId, type, status, lessonsCompleted, lessonsTotal, progressPercentage, startedAt, hashCodeNullable(graduatedAt));
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
    sb.append("class MentorshipRelationship {\n");
    sb.append("    relationshipId: ").append(toIndentedString(relationshipId)).append("\n");
    sb.append("    studentId: ").append(toIndentedString(studentId)).append("\n");
    sb.append("    mentorId: ").append(toIndentedString(mentorId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    lessonsCompleted: ").append(toIndentedString(lessonsCompleted)).append("\n");
    sb.append("    lessonsTotal: ").append(toIndentedString(lessonsTotal)).append("\n");
    sb.append("    progressPercentage: ").append(toIndentedString(progressPercentage)).append("\n");
    sb.append("    startedAt: ").append(toIndentedString(startedAt)).append("\n");
    sb.append("    graduatedAt: ").append(toIndentedString(graduatedAt)).append("\n");
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

