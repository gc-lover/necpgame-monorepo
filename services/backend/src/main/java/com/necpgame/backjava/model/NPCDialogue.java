package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import com.necpgame.backjava.model.DialogueOption;
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
 * NPCDialogue
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T20:49:00.930667100+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class NPCDialogue {

  private String npcId;

  private String text;

  @Valid
  private List<@Valid DialogueOption> options = new ArrayList<>();

  private JsonNullable<String> npcState = JsonNullable.<String>undefined();

  public NPCDialogue() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public NPCDialogue(String npcId, String text, List<@Valid DialogueOption> options) {
    this.npcId = npcId;
    this.text = text;
    this.options = options;
  }

  public NPCDialogue npcId(String npcId) {
    this.npcId = npcId;
    return this;
  }

  /**
   * Get npcId
   * @return npcId
   */
  @NotNull 
  @Schema(name = "npcId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("npcId")
  public String getNpcId() {
    return npcId;
  }

  public void setNpcId(String npcId) {
    this.npcId = npcId;
  }

  public NPCDialogue text(String text) {
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

  public NPCDialogue options(List<@Valid DialogueOption> options) {
    this.options = options;
    return this;
  }

  public NPCDialogue addOptionsItem(DialogueOption optionsItem) {
    if (this.options == null) {
      this.options = new ArrayList<>();
    }
    this.options.add(optionsItem);
    return this;
  }

  /**
   * Get options
   * @return options
   */
  @NotNull @Valid 
  @Schema(name = "options", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("options")
  public List<@Valid DialogueOption> getOptions() {
    return options;
  }

  public void setOptions(List<@Valid DialogueOption> options) {
    this.options = options;
  }

  public NPCDialogue npcState(String npcState) {
    this.npcState = JsonNullable.of(npcState);
    return this;
  }

  /**
   * Get npcState
   * @return npcState
   */
  
  @Schema(name = "npcState", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("npcState")
  public JsonNullable<String> getNpcState() {
    return npcState;
  }

  public void setNpcState(JsonNullable<String> npcState) {
    this.npcState = npcState;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NPCDialogue npCDialogue = (NPCDialogue) o;
    return Objects.equals(this.npcId, npCDialogue.npcId) &&
        Objects.equals(this.text, npCDialogue.text) &&
        Objects.equals(this.options, npCDialogue.options) &&
        equalsNullable(this.npcState, npCDialogue.npcState);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(npcId, text, options, hashCodeNullable(npcState));
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
    sb.append("class NPCDialogue {\n");
    sb.append("    npcId: ").append(toIndentedString(npcId)).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    options: ").append(toIndentedString(options)).append("\n");
    sb.append("    npcState: ").append(toIndentedString(npcState)).append("\n");
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


