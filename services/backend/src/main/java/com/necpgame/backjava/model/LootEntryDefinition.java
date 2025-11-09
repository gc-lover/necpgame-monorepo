package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.LootCondition;
import com.necpgame.backjava.model.LootEntryDefinitionQuantityRange;
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
 * LootEntryDefinition
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class LootEntryDefinition {

  private String entryId;

  private String templateId;

  private Integer weight;

  private @Nullable LootEntryDefinitionQuantityRange quantityRange;

  private @Nullable String rarityOverride;

  @Valid
  private List<@Valid LootCondition> conditions = new ArrayList<>();

  @Valid
  private List<String> tags = new ArrayList<>();

  public LootEntryDefinition() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LootEntryDefinition(String entryId, String templateId, Integer weight) {
    this.entryId = entryId;
    this.templateId = templateId;
    this.weight = weight;
  }

  public LootEntryDefinition entryId(String entryId) {
    this.entryId = entryId;
    return this;
  }

  /**
   * Get entryId
   * @return entryId
   */
  @NotNull 
  @Schema(name = "entryId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("entryId")
  public String getEntryId() {
    return entryId;
  }

  public void setEntryId(String entryId) {
    this.entryId = entryId;
  }

  public LootEntryDefinition templateId(String templateId) {
    this.templateId = templateId;
    return this;
  }

  /**
   * Get templateId
   * @return templateId
   */
  @NotNull 
  @Schema(name = "templateId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("templateId")
  public String getTemplateId() {
    return templateId;
  }

  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }

  public LootEntryDefinition weight(Integer weight) {
    this.weight = weight;
    return this;
  }

  /**
   * Get weight
   * minimum: 1
   * @return weight
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "weight", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("weight")
  public Integer getWeight() {
    return weight;
  }

  public void setWeight(Integer weight) {
    this.weight = weight;
  }

  public LootEntryDefinition quantityRange(@Nullable LootEntryDefinitionQuantityRange quantityRange) {
    this.quantityRange = quantityRange;
    return this;
  }

  /**
   * Get quantityRange
   * @return quantityRange
   */
  @Valid 
  @Schema(name = "quantityRange", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quantityRange")
  public @Nullable LootEntryDefinitionQuantityRange getQuantityRange() {
    return quantityRange;
  }

  public void setQuantityRange(@Nullable LootEntryDefinitionQuantityRange quantityRange) {
    this.quantityRange = quantityRange;
  }

  public LootEntryDefinition rarityOverride(@Nullable String rarityOverride) {
    this.rarityOverride = rarityOverride;
    return this;
  }

  /**
   * Get rarityOverride
   * @return rarityOverride
   */
  
  @Schema(name = "rarityOverride", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rarityOverride")
  public @Nullable String getRarityOverride() {
    return rarityOverride;
  }

  public void setRarityOverride(@Nullable String rarityOverride) {
    this.rarityOverride = rarityOverride;
  }

  public LootEntryDefinition conditions(List<@Valid LootCondition> conditions) {
    this.conditions = conditions;
    return this;
  }

  public LootEntryDefinition addConditionsItem(LootCondition conditionsItem) {
    if (this.conditions == null) {
      this.conditions = new ArrayList<>();
    }
    this.conditions.add(conditionsItem);
    return this;
  }

  /**
   * Get conditions
   * @return conditions
   */
  @Valid 
  @Schema(name = "conditions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("conditions")
  public List<@Valid LootCondition> getConditions() {
    return conditions;
  }

  public void setConditions(List<@Valid LootCondition> conditions) {
    this.conditions = conditions;
  }

  public LootEntryDefinition tags(List<String> tags) {
    this.tags = tags;
    return this;
  }

  public LootEntryDefinition addTagsItem(String tagsItem) {
    if (this.tags == null) {
      this.tags = new ArrayList<>();
    }
    this.tags.add(tagsItem);
    return this;
  }

  /**
   * Get tags
   * @return tags
   */
  
  @Schema(name = "tags", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tags")
  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootEntryDefinition lootEntryDefinition = (LootEntryDefinition) o;
    return Objects.equals(this.entryId, lootEntryDefinition.entryId) &&
        Objects.equals(this.templateId, lootEntryDefinition.templateId) &&
        Objects.equals(this.weight, lootEntryDefinition.weight) &&
        Objects.equals(this.quantityRange, lootEntryDefinition.quantityRange) &&
        Objects.equals(this.rarityOverride, lootEntryDefinition.rarityOverride) &&
        Objects.equals(this.conditions, lootEntryDefinition.conditions) &&
        Objects.equals(this.tags, lootEntryDefinition.tags);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entryId, templateId, weight, quantityRange, rarityOverride, conditions, tags);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootEntryDefinition {\n");
    sb.append("    entryId: ").append(toIndentedString(entryId)).append("\n");
    sb.append("    templateId: ").append(toIndentedString(templateId)).append("\n");
    sb.append("    weight: ").append(toIndentedString(weight)).append("\n");
    sb.append("    quantityRange: ").append(toIndentedString(quantityRange)).append("\n");
    sb.append("    rarityOverride: ").append(toIndentedString(rarityOverride)).append("\n");
    sb.append("    conditions: ").append(toIndentedString(conditions)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
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

