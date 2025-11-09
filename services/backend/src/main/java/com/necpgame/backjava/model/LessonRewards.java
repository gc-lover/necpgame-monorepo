package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.openapitools.jackson.nullable.JsonNullable;
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
 * LessonRewards
 */

@JsonTypeName("Lesson_rewards")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class LessonRewards {

  @Valid
  private Map<String, Integer> skillExperience = new HashMap<>();

  private JsonNullable<String> abilityUnlock = JsonNullable.<String>undefined();

  public LessonRewards skillExperience(Map<String, Integer> skillExperience) {
    this.skillExperience = skillExperience;
    return this;
  }

  public LessonRewards putSkillExperienceItem(String key, Integer skillExperienceItem) {
    if (this.skillExperience == null) {
      this.skillExperience = new HashMap<>();
    }
    this.skillExperience.put(key, skillExperienceItem);
    return this;
  }

  /**
   * Get skillExperience
   * @return skillExperience
   */
  
  @Schema(name = "skill_experience", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill_experience")
  public Map<String, Integer> getSkillExperience() {
    return skillExperience;
  }

  public void setSkillExperience(Map<String, Integer> skillExperience) {
    this.skillExperience = skillExperience;
  }

  public LessonRewards abilityUnlock(String abilityUnlock) {
    this.abilityUnlock = JsonNullable.of(abilityUnlock);
    return this;
  }

  /**
   * Get abilityUnlock
   * @return abilityUnlock
   */
  
  @Schema(name = "ability_unlock", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ability_unlock")
  public JsonNullable<String> getAbilityUnlock() {
    return abilityUnlock;
  }

  public void setAbilityUnlock(JsonNullable<String> abilityUnlock) {
    this.abilityUnlock = abilityUnlock;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LessonRewards lessonRewards = (LessonRewards) o;
    return Objects.equals(this.skillExperience, lessonRewards.skillExperience) &&
        equalsNullable(this.abilityUnlock, lessonRewards.abilityUnlock);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(skillExperience, hashCodeNullable(abilityUnlock));
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
    sb.append("class LessonRewards {\n");
    sb.append("    skillExperience: ").append(toIndentedString(skillExperience)).append("\n");
    sb.append("    abilityUnlock: ").append(toIndentedString(abilityUnlock)).append("\n");
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

