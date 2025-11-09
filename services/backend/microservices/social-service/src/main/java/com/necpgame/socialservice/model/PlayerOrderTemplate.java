package com.necpgame.socialservice.model;

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
 * PlayerOrderTemplate
 */


public class PlayerOrderTemplate {

  private String templateId;

  /**
   * Тип шаблона заказа.
   */
  public enum TemplateEnum {
    COMBAT("combat"),
    
    HACKER("hacker"),
    
    ECONOMY("economy"),
    
    SOCIAL("social"),
    
    EXPLORATION("exploration");

    private final String value;

    TemplateEnum(String value) {
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
    public static TemplateEnum fromValue(String value) {
      for (TemplateEnum b : TemplateEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TemplateEnum template;

  private String title;

  private String description;

  private String defaultBrief;

  @Valid
  private List<String> recommendedGuarantees = new ArrayList<>();

  public PlayerOrderTemplate() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderTemplate(String templateId, TemplateEnum template, String title, String description, String defaultBrief) {
    this.templateId = templateId;
    this.template = template;
    this.title = title;
    this.description = description;
    this.defaultBrief = defaultBrief;
  }

  public PlayerOrderTemplate templateId(String templateId) {
    this.templateId = templateId;
    return this;
  }

  /**
   * Идентификатор шаблона, синхронизированный с content-service.
   * @return templateId
   */
  @NotNull 
  @Schema(name = "templateId", description = "Идентификатор шаблона, синхронизированный с content-service.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("templateId")
  public String getTemplateId() {
    return templateId;
  }

  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }

  public PlayerOrderTemplate template(TemplateEnum template) {
    this.template = template;
    return this;
  }

  /**
   * Тип шаблона заказа.
   * @return template
   */
  @NotNull 
  @Schema(name = "template", description = "Тип шаблона заказа.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("template")
  public TemplateEnum getTemplate() {
    return template;
  }

  public void setTemplate(TemplateEnum template) {
    this.template = template;
  }

  public PlayerOrderTemplate title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Название шаблона для UI.
   * @return title
   */
  @NotNull 
  @Schema(name = "title", description = "Название шаблона для UI.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public PlayerOrderTemplate description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Краткое описание задач и рекомендуемых гарантий.
   * @return description
   */
  @NotNull 
  @Schema(name = "description", description = "Краткое описание задач и рекомендуемых гарантий.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public PlayerOrderTemplate defaultBrief(String defaultBrief) {
    this.defaultBrief = defaultBrief;
    return this;
  }

  /**
   * Предзаполненный текст брифа.
   * @return defaultBrief
   */
  @NotNull 
  @Schema(name = "defaultBrief", description = "Предзаполненный текст брифа.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("defaultBrief")
  public String getDefaultBrief() {
    return defaultBrief;
  }

  public void setDefaultBrief(String defaultBrief) {
    this.defaultBrief = defaultBrief;
  }

  public PlayerOrderTemplate recommendedGuarantees(List<String> recommendedGuarantees) {
    this.recommendedGuarantees = recommendedGuarantees;
    return this;
  }

  public PlayerOrderTemplate addRecommendedGuaranteesItem(String recommendedGuaranteesItem) {
    if (this.recommendedGuarantees == null) {
      this.recommendedGuarantees = new ArrayList<>();
    }
    this.recommendedGuarantees.add(recommendedGuaranteesItem);
    return this;
  }

  /**
   * Рекомендуемые гарантии для заказа этого типа.
   * @return recommendedGuarantees
   */
  
  @Schema(name = "recommendedGuarantees", description = "Рекомендуемые гарантии для заказа этого типа.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recommendedGuarantees")
  public List<String> getRecommendedGuarantees() {
    return recommendedGuarantees;
  }

  public void setRecommendedGuarantees(List<String> recommendedGuarantees) {
    this.recommendedGuarantees = recommendedGuarantees;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderTemplate playerOrderTemplate = (PlayerOrderTemplate) o;
    return Objects.equals(this.templateId, playerOrderTemplate.templateId) &&
        Objects.equals(this.template, playerOrderTemplate.template) &&
        Objects.equals(this.title, playerOrderTemplate.title) &&
        Objects.equals(this.description, playerOrderTemplate.description) &&
        Objects.equals(this.defaultBrief, playerOrderTemplate.defaultBrief) &&
        Objects.equals(this.recommendedGuarantees, playerOrderTemplate.recommendedGuarantees);
  }

  @Override
  public int hashCode() {
    return Objects.hash(templateId, template, title, description, defaultBrief, recommendedGuarantees);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderTemplate {\n");
    sb.append("    templateId: ").append(toIndentedString(templateId)).append("\n");
    sb.append("    template: ").append(toIndentedString(template)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    defaultBrief: ").append(toIndentedString(defaultBrief)).append("\n");
    sb.append("    recommendedGuarantees: ").append(toIndentedString(recommendedGuarantees)).append("\n");
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

