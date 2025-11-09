package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CodexEntry
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class CodexEntry {

  private @Nullable String entryId;

  /**
   * Gets or Sets category
   */
  public enum CategoryEnum {
    FACTIONS("FACTIONS"),
    
    LOCATIONS("LOCATIONS"),
    
    CHARACTERS("CHARACTERS"),
    
    EVENTS("EVENTS"),
    
    TECHNOLOGY("TECHNOLOGY");

    private final String value;

    CategoryEnum(String value) {
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
    public static CategoryEnum fromValue(String value) {
      for (CategoryEnum b : CategoryEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable CategoryEnum category;

  private @Nullable String title;

  private @Nullable String content;

  private @Nullable Boolean unlocked;

  private @Nullable String unlockCondition;

  @Valid
  private List<String> relatedEntries = new ArrayList<>();

  public CodexEntry entryId(@Nullable String entryId) {
    this.entryId = entryId;
    return this;
  }

  /**
   * Get entryId
   * @return entryId
   */
  
  @Schema(name = "entry_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("entry_id")
  public @Nullable String getEntryId() {
    return entryId;
  }

  public void setEntryId(@Nullable String entryId) {
    this.entryId = entryId;
  }

  public CodexEntry category(@Nullable CategoryEnum category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  
  @Schema(name = "category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category")
  public @Nullable CategoryEnum getCategory() {
    return category;
  }

  public void setCategory(@Nullable CategoryEnum category) {
    this.category = category;
  }

  public CodexEntry title(@Nullable String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   */
  
  @Schema(name = "title", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  public @Nullable String getTitle() {
    return title;
  }

  public void setTitle(@Nullable String title) {
    this.title = title;
  }

  public CodexEntry content(@Nullable String content) {
    this.content = content;
    return this;
  }

  /**
   * Get content
   * @return content
   */
  
  @Schema(name = "content", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("content")
  public @Nullable String getContent() {
    return content;
  }

  public void setContent(@Nullable String content) {
    this.content = content;
  }

  public CodexEntry unlocked(@Nullable Boolean unlocked) {
    this.unlocked = unlocked;
    return this;
  }

  /**
   * Get unlocked
   * @return unlocked
   */
  
  @Schema(name = "unlocked", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unlocked")
  public @Nullable Boolean getUnlocked() {
    return unlocked;
  }

  public void setUnlocked(@Nullable Boolean unlocked) {
    this.unlocked = unlocked;
  }

  public CodexEntry unlockCondition(@Nullable String unlockCondition) {
    this.unlockCondition = unlockCondition;
    return this;
  }

  /**
   * Как разблокировать
   * @return unlockCondition
   */
  
  @Schema(name = "unlock_condition", description = "Как разблокировать", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unlock_condition")
  public @Nullable String getUnlockCondition() {
    return unlockCondition;
  }

  public void setUnlockCondition(@Nullable String unlockCondition) {
    this.unlockCondition = unlockCondition;
  }

  public CodexEntry relatedEntries(List<String> relatedEntries) {
    this.relatedEntries = relatedEntries;
    return this;
  }

  public CodexEntry addRelatedEntriesItem(String relatedEntriesItem) {
    if (this.relatedEntries == null) {
      this.relatedEntries = new ArrayList<>();
    }
    this.relatedEntries.add(relatedEntriesItem);
    return this;
  }

  /**
   * Get relatedEntries
   * @return relatedEntries
   */
  
  @Schema(name = "related_entries", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("related_entries")
  public List<String> getRelatedEntries() {
    return relatedEntries;
  }

  public void setRelatedEntries(List<String> relatedEntries) {
    this.relatedEntries = relatedEntries;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CodexEntry codexEntry = (CodexEntry) o;
    return Objects.equals(this.entryId, codexEntry.entryId) &&
        Objects.equals(this.category, codexEntry.category) &&
        Objects.equals(this.title, codexEntry.title) &&
        Objects.equals(this.content, codexEntry.content) &&
        Objects.equals(this.unlocked, codexEntry.unlocked) &&
        Objects.equals(this.unlockCondition, codexEntry.unlockCondition) &&
        Objects.equals(this.relatedEntries, codexEntry.relatedEntries);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entryId, category, title, content, unlocked, unlockCondition, relatedEntries);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CodexEntry {\n");
    sb.append("    entryId: ").append(toIndentedString(entryId)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    unlocked: ").append(toIndentedString(unlocked)).append("\n");
    sb.append("    unlockCondition: ").append(toIndentedString(unlockCondition)).append("\n");
    sb.append("    relatedEntries: ").append(toIndentedString(relatedEntries)).append("\n");
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

