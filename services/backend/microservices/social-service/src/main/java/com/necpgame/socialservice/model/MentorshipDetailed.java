package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.Lesson;
import com.necpgame.socialservice.model.Mentor;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * MentorshipDetailed
 */


public class MentorshipDetailed {

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

  private @Nullable Mentor mentorDetails;

  private @Nullable Lesson currentLesson;

  @Valid
  private Map<String, Integer> skillImprovements = new HashMap<>();

  @Valid
  private List<String> abilitiesLearned = new ArrayList<>();

  private @Nullable Integer bondLevel;

  public MentorshipDetailed relationshipId(@Nullable UUID relationshipId) {
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

  public MentorshipDetailed studentId(@Nullable UUID studentId) {
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

  public MentorshipDetailed mentorId(@Nullable String mentorId) {
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

  public MentorshipDetailed type(@Nullable String type) {
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

  public MentorshipDetailed status(@Nullable StatusEnum status) {
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

  public MentorshipDetailed lessonsCompleted(@Nullable Integer lessonsCompleted) {
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

  public MentorshipDetailed lessonsTotal(@Nullable Integer lessonsTotal) {
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

  public MentorshipDetailed progressPercentage(@Nullable BigDecimal progressPercentage) {
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

  public MentorshipDetailed startedAt(@Nullable OffsetDateTime startedAt) {
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

  public MentorshipDetailed graduatedAt(OffsetDateTime graduatedAt) {
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

  public MentorshipDetailed mentorDetails(@Nullable Mentor mentorDetails) {
    this.mentorDetails = mentorDetails;
    return this;
  }

  /**
   * Get mentorDetails
   * @return mentorDetails
   */
  @Valid 
  @Schema(name = "mentor_details", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mentor_details")
  public @Nullable Mentor getMentorDetails() {
    return mentorDetails;
  }

  public void setMentorDetails(@Nullable Mentor mentorDetails) {
    this.mentorDetails = mentorDetails;
  }

  public MentorshipDetailed currentLesson(@Nullable Lesson currentLesson) {
    this.currentLesson = currentLesson;
    return this;
  }

  /**
   * Get currentLesson
   * @return currentLesson
   */
  @Valid 
  @Schema(name = "current_lesson", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_lesson")
  public @Nullable Lesson getCurrentLesson() {
    return currentLesson;
  }

  public void setCurrentLesson(@Nullable Lesson currentLesson) {
    this.currentLesson = currentLesson;
  }

  public MentorshipDetailed skillImprovements(Map<String, Integer> skillImprovements) {
    this.skillImprovements = skillImprovements;
    return this;
  }

  public MentorshipDetailed putSkillImprovementsItem(String key, Integer skillImprovementsItem) {
    if (this.skillImprovements == null) {
      this.skillImprovements = new HashMap<>();
    }
    this.skillImprovements.put(key, skillImprovementsItem);
    return this;
  }

  /**
   * Улучшения навыков за время обучения
   * @return skillImprovements
   */
  
  @Schema(name = "skill_improvements", description = "Улучшения навыков за время обучения", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill_improvements")
  public Map<String, Integer> getSkillImprovements() {
    return skillImprovements;
  }

  public void setSkillImprovements(Map<String, Integer> skillImprovements) {
    this.skillImprovements = skillImprovements;
  }

  public MentorshipDetailed abilitiesLearned(List<String> abilitiesLearned) {
    this.abilitiesLearned = abilitiesLearned;
    return this;
  }

  public MentorshipDetailed addAbilitiesLearnedItem(String abilitiesLearnedItem) {
    if (this.abilitiesLearned == null) {
      this.abilitiesLearned = new ArrayList<>();
    }
    this.abilitiesLearned.add(abilitiesLearnedItem);
    return this;
  }

  /**
   * Get abilitiesLearned
   * @return abilitiesLearned
   */
  
  @Schema(name = "abilities_learned", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("abilities_learned")
  public List<String> getAbilitiesLearned() {
    return abilitiesLearned;
  }

  public void setAbilitiesLearned(List<String> abilitiesLearned) {
    this.abilitiesLearned = abilitiesLearned;
  }

  public MentorshipDetailed bondLevel(@Nullable Integer bondLevel) {
    this.bondLevel = bondLevel;
    return this;
  }

  /**
   * Get bondLevel
   * minimum: 0
   * maximum: 100
   * @return bondLevel
   */
  @Min(value = 0) @Max(value = 100) 
  @Schema(name = "bond_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bond_level")
  public @Nullable Integer getBondLevel() {
    return bondLevel;
  }

  public void setBondLevel(@Nullable Integer bondLevel) {
    this.bondLevel = bondLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MentorshipDetailed mentorshipDetailed = (MentorshipDetailed) o;
    return Objects.equals(this.relationshipId, mentorshipDetailed.relationshipId) &&
        Objects.equals(this.studentId, mentorshipDetailed.studentId) &&
        Objects.equals(this.mentorId, mentorshipDetailed.mentorId) &&
        Objects.equals(this.type, mentorshipDetailed.type) &&
        Objects.equals(this.status, mentorshipDetailed.status) &&
        Objects.equals(this.lessonsCompleted, mentorshipDetailed.lessonsCompleted) &&
        Objects.equals(this.lessonsTotal, mentorshipDetailed.lessonsTotal) &&
        Objects.equals(this.progressPercentage, mentorshipDetailed.progressPercentage) &&
        Objects.equals(this.startedAt, mentorshipDetailed.startedAt) &&
        equalsNullable(this.graduatedAt, mentorshipDetailed.graduatedAt) &&
        Objects.equals(this.mentorDetails, mentorshipDetailed.mentorDetails) &&
        Objects.equals(this.currentLesson, mentorshipDetailed.currentLesson) &&
        Objects.equals(this.skillImprovements, mentorshipDetailed.skillImprovements) &&
        Objects.equals(this.abilitiesLearned, mentorshipDetailed.abilitiesLearned) &&
        Objects.equals(this.bondLevel, mentorshipDetailed.bondLevel);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(relationshipId, studentId, mentorId, type, status, lessonsCompleted, lessonsTotal, progressPercentage, startedAt, hashCodeNullable(graduatedAt), mentorDetails, currentLesson, skillImprovements, abilitiesLearned, bondLevel);
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
    sb.append("class MentorshipDetailed {\n");
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
    sb.append("    mentorDetails: ").append(toIndentedString(mentorDetails)).append("\n");
    sb.append("    currentLesson: ").append(toIndentedString(currentLesson)).append("\n");
    sb.append("    skillImprovements: ").append(toIndentedString(skillImprovements)).append("\n");
    sb.append("    abilitiesLearned: ").append(toIndentedString(abilitiesLearned)).append("\n");
    sb.append("    bondLevel: ").append(toIndentedString(bondLevel)).append("\n");
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

