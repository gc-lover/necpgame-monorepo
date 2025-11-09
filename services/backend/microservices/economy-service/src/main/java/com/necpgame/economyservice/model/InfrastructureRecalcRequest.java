package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * InfrastructureRecalcRequest
 */


public class InfrastructureRecalcRequest {

  private UUID cityId;

  @Valid
  private List<UUID> districtIds = new ArrayList<>();

  /**
   * Gets or Sets categories
   */
  public enum CategoriesEnum {
    HOUSING("housing"),
    
    TRANSIT("transit"),
    
    SECURITY("security"),
    
    ENTERTAINMENT("entertainment"),
    
    MEDICAL("medical"),
    
    BLACK_MARKET("black_market"),
    
    CIVIC("civic");

    private final String value;

    CategoriesEnum(String value) {
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
    public static CategoriesEnum fromValue(String value) {
      for (CategoriesEnum b : CategoriesEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @Valid
  private List<CategoriesEnum> categories = new ArrayList<>();

  /**
   * Gets or Sets trigger
   */
  public enum TriggerEnum {
    POPULATION_SYNC("population-sync"),
    
    MAINTENANCE("maintenance"),
    
    MANUAL("manual"),
    
    EMERGENCY("emergency");

    private final String value;

    TriggerEnum(String value) {
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
    public static TriggerEnum fromValue(String value) {
      for (TriggerEnum b : TriggerEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TriggerEnum trigger;

  @Valid
  private Map<String, Object> triggerContext = new HashMap<>();

  /**
   * Gets or Sets priority
   */
  public enum PriorityEnum {
    LOW("low"),
    
    NORMAL("normal"),
    
    HIGH("high"),
    
    URGENT("urgent");

    private final String value;

    PriorityEnum(String value) {
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
    public static PriorityEnum fromValue(String value) {
      for (PriorityEnum b : PriorityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private PriorityEnum priority = PriorityEnum.NORMAL;

  private Boolean dryRun = false;

  private @Nullable String notes;

  public InfrastructureRecalcRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public InfrastructureRecalcRequest(UUID cityId, TriggerEnum trigger) {
    this.cityId = cityId;
    this.trigger = trigger;
  }

  public InfrastructureRecalcRequest cityId(UUID cityId) {
    this.cityId = cityId;
    return this;
  }

  /**
   * Get cityId
   * @return cityId
   */
  @NotNull @Valid 
  @Schema(name = "cityId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("cityId")
  public UUID getCityId() {
    return cityId;
  }

  public void setCityId(UUID cityId) {
    this.cityId = cityId;
  }

  public InfrastructureRecalcRequest districtIds(List<UUID> districtIds) {
    this.districtIds = districtIds;
    return this;
  }

  public InfrastructureRecalcRequest addDistrictIdsItem(UUID districtIdsItem) {
    if (this.districtIds == null) {
      this.districtIds = new ArrayList<>();
    }
    this.districtIds.add(districtIdsItem);
    return this;
  }

  /**
   * Get districtIds
   * @return districtIds
   */
  @Valid 
  @Schema(name = "districtIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("districtIds")
  public List<UUID> getDistrictIds() {
    return districtIds;
  }

  public void setDistrictIds(List<UUID> districtIds) {
    this.districtIds = districtIds;
  }

  public InfrastructureRecalcRequest categories(List<CategoriesEnum> categories) {
    this.categories = categories;
    return this;
  }

  public InfrastructureRecalcRequest addCategoriesItem(CategoriesEnum categoriesItem) {
    if (this.categories == null) {
      this.categories = new ArrayList<>();
    }
    this.categories.add(categoriesItem);
    return this;
  }

  /**
   * Get categories
   * @return categories
   */
  
  @Schema(name = "categories", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("categories")
  public List<CategoriesEnum> getCategories() {
    return categories;
  }

  public void setCategories(List<CategoriesEnum> categories) {
    this.categories = categories;
  }

  public InfrastructureRecalcRequest trigger(TriggerEnum trigger) {
    this.trigger = trigger;
    return this;
  }

  /**
   * Get trigger
   * @return trigger
   */
  @NotNull 
  @Schema(name = "trigger", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("trigger")
  public TriggerEnum getTrigger() {
    return trigger;
  }

  public void setTrigger(TriggerEnum trigger) {
    this.trigger = trigger;
  }

  public InfrastructureRecalcRequest triggerContext(Map<String, Object> triggerContext) {
    this.triggerContext = triggerContext;
    return this;
  }

  public InfrastructureRecalcRequest putTriggerContextItem(String key, Object triggerContextItem) {
    if (this.triggerContext == null) {
      this.triggerContext = new HashMap<>();
    }
    this.triggerContext.put(key, triggerContextItem);
    return this;
  }

  /**
   * Get triggerContext
   * @return triggerContext
   */
  
  @Schema(name = "triggerContext", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("triggerContext")
  public Map<String, Object> getTriggerContext() {
    return triggerContext;
  }

  public void setTriggerContext(Map<String, Object> triggerContext) {
    this.triggerContext = triggerContext;
  }

  public InfrastructureRecalcRequest priority(PriorityEnum priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Get priority
   * @return priority
   */
  
  @Schema(name = "priority", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("priority")
  public PriorityEnum getPriority() {
    return priority;
  }

  public void setPriority(PriorityEnum priority) {
    this.priority = priority;
  }

  public InfrastructureRecalcRequest dryRun(Boolean dryRun) {
    this.dryRun = dryRun;
    return this;
  }

  /**
   * Get dryRun
   * @return dryRun
   */
  
  @Schema(name = "dryRun", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dryRun")
  public Boolean getDryRun() {
    return dryRun;
  }

  public void setDryRun(Boolean dryRun) {
    this.dryRun = dryRun;
  }

  public InfrastructureRecalcRequest notes(@Nullable String notes) {
    this.notes = notes;
    return this;
  }

  /**
   * Get notes
   * @return notes
   */
  
  @Schema(name = "notes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notes")
  public @Nullable String getNotes() {
    return notes;
  }

  public void setNotes(@Nullable String notes) {
    this.notes = notes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InfrastructureRecalcRequest infrastructureRecalcRequest = (InfrastructureRecalcRequest) o;
    return Objects.equals(this.cityId, infrastructureRecalcRequest.cityId) &&
        Objects.equals(this.districtIds, infrastructureRecalcRequest.districtIds) &&
        Objects.equals(this.categories, infrastructureRecalcRequest.categories) &&
        Objects.equals(this.trigger, infrastructureRecalcRequest.trigger) &&
        Objects.equals(this.triggerContext, infrastructureRecalcRequest.triggerContext) &&
        Objects.equals(this.priority, infrastructureRecalcRequest.priority) &&
        Objects.equals(this.dryRun, infrastructureRecalcRequest.dryRun) &&
        Objects.equals(this.notes, infrastructureRecalcRequest.notes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cityId, districtIds, categories, trigger, triggerContext, priority, dryRun, notes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InfrastructureRecalcRequest {\n");
    sb.append("    cityId: ").append(toIndentedString(cityId)).append("\n");
    sb.append("    districtIds: ").append(toIndentedString(districtIds)).append("\n");
    sb.append("    categories: ").append(toIndentedString(categories)).append("\n");
    sb.append("    trigger: ").append(toIndentedString(trigger)).append("\n");
    sb.append("    triggerContext: ").append(toIndentedString(triggerContext)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    dryRun: ").append(toIndentedString(dryRun)).append("\n");
    sb.append("    notes: ").append(toIndentedString(notes)).append("\n");
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

