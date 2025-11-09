package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * DialogueOption
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T20:49:00.930667100+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class DialogueOption {

  private String id;

  private String text;

  private JsonNullable<String> requiresSkill = JsonNullable.<String>undefined();

  private JsonNullable<String> requiresItem = JsonNullable.<String>undefined();

  private Boolean available = true;

  private JsonNullable<String> consequence = JsonNullable.<String>undefined();

  public DialogueOption() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DialogueOption(String id, String text) {
    this.id = id;
    this.text = text;
  }

  public DialogueOption id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  @NotNull 
  @Schema(name = "id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public DialogueOption text(String text) {
    this.text = text;
    return this;
  }

  /**
   * Get text
   * @return text
   */
  @NotNull 
  @Schema(name = "text", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("text")
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public DialogueOption requiresSkill(String requiresSkill) {
    this.requiresSkill = JsonNullable.of(requiresSkill);
    return this;
  }

  /**
   * Get requiresSkill
   * @return requiresSkill
   */
  
  @Schema(name = "requiresSkill", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requiresSkill")
  public JsonNullable<String> getRequiresSkill() {
    return requiresSkill;
  }

  public void setRequiresSkill(JsonNullable<String> requiresSkill) {
    this.requiresSkill = requiresSkill;
  }

  public DialogueOption requiresItem(String requiresItem) {
    this.requiresItem = JsonNullable.of(requiresItem);
    return this;
  }

  /**
   * Get requiresItem
   * @return requiresItem
   */
  
  @Schema(name = "requiresItem", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requiresItem")
  public JsonNullable<String> getRequiresItem() {
    return requiresItem;
  }

  public void setRequiresItem(JsonNullable<String> requiresItem) {
    this.requiresItem = requiresItem;
  }

  public DialogueOption available(Boolean available) {
    this.available = available;
    return this;
  }

  /**
   * Get available
   * @return available
   */
  
  @Schema(name = "available", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("available")
  public Boolean getAvailable() {
    return available;
  }

  public void setAvailable(Boolean available) {
    this.available = available;
  }

  public DialogueOption consequence(String consequence) {
    this.consequence = JsonNullable.of(consequence);
    return this;
  }

  /**
   * Get consequence
   * @return consequence
   */
  
  @Schema(name = "consequence", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("consequence")
  public JsonNullable<String> getConsequence() {
    return consequence;
  }

  public void setConsequence(JsonNullable<String> consequence) {
    this.consequence = consequence;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DialogueOption dialogueOption = (DialogueOption) o;
    return Objects.equals(this.id, dialogueOption.id) &&
        Objects.equals(this.text, dialogueOption.text) &&
        equalsNullable(this.requiresSkill, dialogueOption.requiresSkill) &&
        equalsNullable(this.requiresItem, dialogueOption.requiresItem) &&
        Objects.equals(this.available, dialogueOption.available) &&
        equalsNullable(this.consequence, dialogueOption.consequence);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, text, hashCodeNullable(requiresSkill), hashCodeNullable(requiresItem), available, hashCodeNullable(consequence));
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
    sb.append("class DialogueOption {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    requiresSkill: ").append(toIndentedString(requiresSkill)).append("\n");
    sb.append("    requiresItem: ").append(toIndentedString(requiresItem)).append("\n");
    sb.append("    available: ").append(toIndentedString(available)).append("\n");
    sb.append("    consequence: ").append(toIndentedString(consequence)).append("\n");
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


