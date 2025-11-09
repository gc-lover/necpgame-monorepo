package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.ScheduleTemplateSlot;
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
 * ScheduleTemplate
 */


public class ScheduleTemplate {

  private String templateId;

  /**
   * Gets or Sets templateType
   */
  public enum TemplateTypeEnum {
    VENDOR("vendor"),
    
    GUARD("guard"),
    
    ENTERTAINER("entertainer"),
    
    CORPORATE("corporate"),
    
    UNDERGROUND("underground"),
    
    QUEST_UNIQUE("quest_unique");

    private final String value;

    TemplateTypeEnum(String value) {
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
    public static TemplateTypeEnum fromValue(String value) {
      for (TemplateTypeEnum b : TemplateTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TemplateTypeEnum templateType;

  private @Nullable String description;

  private @Nullable Boolean supportsEvents;

  @Valid
  private List<@Valid ScheduleTemplateSlot> slots = new ArrayList<>();

  public ScheduleTemplate() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ScheduleTemplate(String templateId, TemplateTypeEnum templateType, List<@Valid ScheduleTemplateSlot> slots) {
    this.templateId = templateId;
    this.templateType = templateType;
    this.slots = slots;
  }

  public ScheduleTemplate templateId(String templateId) {
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

  public ScheduleTemplate templateType(TemplateTypeEnum templateType) {
    this.templateType = templateType;
    return this;
  }

  /**
   * Get templateType
   * @return templateType
   */
  @NotNull 
  @Schema(name = "templateType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("templateType")
  public TemplateTypeEnum getTemplateType() {
    return templateType;
  }

  public void setTemplateType(TemplateTypeEnum templateType) {
    this.templateType = templateType;
  }

  public ScheduleTemplate description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public ScheduleTemplate supportsEvents(@Nullable Boolean supportsEvents) {
    this.supportsEvents = supportsEvents;
    return this;
  }

  /**
   * Get supportsEvents
   * @return supportsEvents
   */
  
  @Schema(name = "supportsEvents", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("supportsEvents")
  public @Nullable Boolean getSupportsEvents() {
    return supportsEvents;
  }

  public void setSupportsEvents(@Nullable Boolean supportsEvents) {
    this.supportsEvents = supportsEvents;
  }

  public ScheduleTemplate slots(List<@Valid ScheduleTemplateSlot> slots) {
    this.slots = slots;
    return this;
  }

  public ScheduleTemplate addSlotsItem(ScheduleTemplateSlot slotsItem) {
    if (this.slots == null) {
      this.slots = new ArrayList<>();
    }
    this.slots.add(slotsItem);
    return this;
  }

  /**
   * Get slots
   * @return slots
   */
  @NotNull @Valid 
  @Schema(name = "slots", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("slots")
  public List<@Valid ScheduleTemplateSlot> getSlots() {
    return slots;
  }

  public void setSlots(List<@Valid ScheduleTemplateSlot> slots) {
    this.slots = slots;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ScheduleTemplate scheduleTemplate = (ScheduleTemplate) o;
    return Objects.equals(this.templateId, scheduleTemplate.templateId) &&
        Objects.equals(this.templateType, scheduleTemplate.templateType) &&
        Objects.equals(this.description, scheduleTemplate.description) &&
        Objects.equals(this.supportsEvents, scheduleTemplate.supportsEvents) &&
        Objects.equals(this.slots, scheduleTemplate.slots);
  }

  @Override
  public int hashCode() {
    return Objects.hash(templateId, templateType, description, supportsEvents, slots);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScheduleTemplate {\n");
    sb.append("    templateId: ").append(toIndentedString(templateId)).append("\n");
    sb.append("    templateType: ").append(toIndentedString(templateType)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    supportsEvents: ").append(toIndentedString(supportsEvents)).append("\n");
    sb.append("    slots: ").append(toIndentedString(slots)).append("\n");
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

