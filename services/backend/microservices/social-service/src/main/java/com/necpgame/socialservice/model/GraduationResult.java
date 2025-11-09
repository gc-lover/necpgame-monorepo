package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * GraduationResult
 */


public class GraduationResult {

  private @Nullable UUID relationshipId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime graduationDate;

  /**
   * Gets or Sets finalGrade
   */
  public enum FinalGradeEnum {
    EXCELLENT("EXCELLENT"),
    
    GOOD("GOOD"),
    
    SATISFACTORY("SATISFACTORY");

    private final String value;

    FinalGradeEnum(String value) {
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
    public static FinalGradeEnum fromValue(String value) {
      for (FinalGradeEnum b : FinalGradeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable FinalGradeEnum finalGrade;

  @Valid
  private List<String> abilitiesMastered = new ArrayList<>();

  private JsonNullable<String> titleEarned = JsonNullable.<String>undefined();

  private @Nullable String mentorRecommendation;

  private @Nullable Boolean canBecomeMentor;

  public GraduationResult relationshipId(@Nullable UUID relationshipId) {
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

  public GraduationResult graduationDate(@Nullable OffsetDateTime graduationDate) {
    this.graduationDate = graduationDate;
    return this;
  }

  /**
   * Get graduationDate
   * @return graduationDate
   */
  @Valid 
  @Schema(name = "graduation_date", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("graduation_date")
  public @Nullable OffsetDateTime getGraduationDate() {
    return graduationDate;
  }

  public void setGraduationDate(@Nullable OffsetDateTime graduationDate) {
    this.graduationDate = graduationDate;
  }

  public GraduationResult finalGrade(@Nullable FinalGradeEnum finalGrade) {
    this.finalGrade = finalGrade;
    return this;
  }

  /**
   * Get finalGrade
   * @return finalGrade
   */
  
  @Schema(name = "final_grade", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("final_grade")
  public @Nullable FinalGradeEnum getFinalGrade() {
    return finalGrade;
  }

  public void setFinalGrade(@Nullable FinalGradeEnum finalGrade) {
    this.finalGrade = finalGrade;
  }

  public GraduationResult abilitiesMastered(List<String> abilitiesMastered) {
    this.abilitiesMastered = abilitiesMastered;
    return this;
  }

  public GraduationResult addAbilitiesMasteredItem(String abilitiesMasteredItem) {
    if (this.abilitiesMastered == null) {
      this.abilitiesMastered = new ArrayList<>();
    }
    this.abilitiesMastered.add(abilitiesMasteredItem);
    return this;
  }

  /**
   * Get abilitiesMastered
   * @return abilitiesMastered
   */
  
  @Schema(name = "abilities_mastered", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("abilities_mastered")
  public List<String> getAbilitiesMastered() {
    return abilitiesMastered;
  }

  public void setAbilitiesMastered(List<String> abilitiesMastered) {
    this.abilitiesMastered = abilitiesMastered;
  }

  public GraduationResult titleEarned(String titleEarned) {
    this.titleEarned = JsonNullable.of(titleEarned);
    return this;
  }

  /**
   * Get titleEarned
   * @return titleEarned
   */
  
  @Schema(name = "title_earned", example = "Master of Combat", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title_earned")
  public JsonNullable<String> getTitleEarned() {
    return titleEarned;
  }

  public void setTitleEarned(JsonNullable<String> titleEarned) {
    this.titleEarned = titleEarned;
  }

  public GraduationResult mentorRecommendation(@Nullable String mentorRecommendation) {
    this.mentorRecommendation = mentorRecommendation;
    return this;
  }

  /**
   * Get mentorRecommendation
   * @return mentorRecommendation
   */
  
  @Schema(name = "mentor_recommendation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mentor_recommendation")
  public @Nullable String getMentorRecommendation() {
    return mentorRecommendation;
  }

  public void setMentorRecommendation(@Nullable String mentorRecommendation) {
    this.mentorRecommendation = mentorRecommendation;
  }

  public GraduationResult canBecomeMentor(@Nullable Boolean canBecomeMentor) {
    this.canBecomeMentor = canBecomeMentor;
    return this;
  }

  /**
   * Студент может стать mentor
   * @return canBecomeMentor
   */
  
  @Schema(name = "can_become_mentor", description = "Студент может стать mentor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("can_become_mentor")
  public @Nullable Boolean getCanBecomeMentor() {
    return canBecomeMentor;
  }

  public void setCanBecomeMentor(@Nullable Boolean canBecomeMentor) {
    this.canBecomeMentor = canBecomeMentor;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GraduationResult graduationResult = (GraduationResult) o;
    return Objects.equals(this.relationshipId, graduationResult.relationshipId) &&
        Objects.equals(this.graduationDate, graduationResult.graduationDate) &&
        Objects.equals(this.finalGrade, graduationResult.finalGrade) &&
        Objects.equals(this.abilitiesMastered, graduationResult.abilitiesMastered) &&
        equalsNullable(this.titleEarned, graduationResult.titleEarned) &&
        Objects.equals(this.mentorRecommendation, graduationResult.mentorRecommendation) &&
        Objects.equals(this.canBecomeMentor, graduationResult.canBecomeMentor);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(relationshipId, graduationDate, finalGrade, abilitiesMastered, hashCodeNullable(titleEarned), mentorRecommendation, canBecomeMentor);
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
    sb.append("class GraduationResult {\n");
    sb.append("    relationshipId: ").append(toIndentedString(relationshipId)).append("\n");
    sb.append("    graduationDate: ").append(toIndentedString(graduationDate)).append("\n");
    sb.append("    finalGrade: ").append(toIndentedString(finalGrade)).append("\n");
    sb.append("    abilitiesMastered: ").append(toIndentedString(abilitiesMastered)).append("\n");
    sb.append("    titleEarned: ").append(toIndentedString(titleEarned)).append("\n");
    sb.append("    mentorRecommendation: ").append(toIndentedString(mentorRecommendation)).append("\n");
    sb.append("    canBecomeMentor: ").append(toIndentedString(canBecomeMentor)).append("\n");
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

