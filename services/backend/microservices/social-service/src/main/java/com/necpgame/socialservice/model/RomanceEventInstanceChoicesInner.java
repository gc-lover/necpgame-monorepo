package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.Arrays;
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
 * RomanceEventInstanceChoicesInner
 */

@JsonTypeName("RomanceEventInstance_choices_inner")

public class RomanceEventInstanceChoicesInner {

  private @Nullable String choiceId;

  private @Nullable String text;

  private @Nullable Integer affectionChange;

  private JsonNullable<Object> skillCheck = JsonNullable.<Object>undefined();

  public RomanceEventInstanceChoicesInner choiceId(@Nullable String choiceId) {
    this.choiceId = choiceId;
    return this;
  }

  /**
   * Get choiceId
   * @return choiceId
   */
  
  @Schema(name = "choice_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("choice_id")
  public @Nullable String getChoiceId() {
    return choiceId;
  }

  public void setChoiceId(@Nullable String choiceId) {
    this.choiceId = choiceId;
  }

  public RomanceEventInstanceChoicesInner text(@Nullable String text) {
    this.text = text;
    return this;
  }

  /**
   * Get text
   * @return text
   */
  
  @Schema(name = "text", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("text")
  public @Nullable String getText() {
    return text;
  }

  public void setText(@Nullable String text) {
    this.text = text;
  }

  public RomanceEventInstanceChoicesInner affectionChange(@Nullable Integer affectionChange) {
    this.affectionChange = affectionChange;
    return this;
  }

  /**
   * Спойлер - можно не показывать
   * @return affectionChange
   */
  
  @Schema(name = "affection_change", description = "Спойлер - можно не показывать", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affection_change")
  public @Nullable Integer getAffectionChange() {
    return affectionChange;
  }

  public void setAffectionChange(@Nullable Integer affectionChange) {
    this.affectionChange = affectionChange;
  }

  public RomanceEventInstanceChoicesInner skillCheck(Object skillCheck) {
    this.skillCheck = JsonNullable.of(skillCheck);
    return this;
  }

  /**
   * Get skillCheck
   * @return skillCheck
   */
  
  @Schema(name = "skill_check", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill_check")
  public JsonNullable<Object> getSkillCheck() {
    return skillCheck;
  }

  public void setSkillCheck(JsonNullable<Object> skillCheck) {
    this.skillCheck = skillCheck;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RomanceEventInstanceChoicesInner romanceEventInstanceChoicesInner = (RomanceEventInstanceChoicesInner) o;
    return Objects.equals(this.choiceId, romanceEventInstanceChoicesInner.choiceId) &&
        Objects.equals(this.text, romanceEventInstanceChoicesInner.text) &&
        Objects.equals(this.affectionChange, romanceEventInstanceChoicesInner.affectionChange) &&
        equalsNullable(this.skillCheck, romanceEventInstanceChoicesInner.skillCheck);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(choiceId, text, affectionChange, hashCodeNullable(skillCheck));
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
    sb.append("class RomanceEventInstanceChoicesInner {\n");
    sb.append("    choiceId: ").append(toIndentedString(choiceId)).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    affectionChange: ").append(toIndentedString(affectionChange)).append("\n");
    sb.append("    skillCheck: ").append(toIndentedString(skillCheck)).append("\n");
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

