package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import com.necpgame.backjava.model.QuestObjective;
import com.necpgame.backjava.model.QuestRequirements;
import com.necpgame.backjava.model.QuestRewards;
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
 * Quest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T20:50:05.709666800+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class Quest {

  private String id;

  private String name;

  private String description;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    MAIN("main"),
    
    SIDE("side"),
    
    CONTRACT("contract"),
    
    DAILY("daily");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TypeEnum type;

  private Integer level;

  private QuestRewards rewards;

  @Valid
  private List<@Valid QuestObjective> objectives = new ArrayList<>();

  private @Nullable QuestRequirements requirements;

  private @Nullable String giver;

  private @Nullable String location;

  private Boolean isRepeatable = false;

  private JsonNullable<Integer> timeLimit = JsonNullable.<Integer>undefined();

  public Quest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Quest(String id, String name, String description, TypeEnum type, Integer level, QuestRewards rewards, List<@Valid QuestObjective> objectives) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.type = type;
    this.level = level;
    this.rewards = rewards;
    this.objectives = objectives;
  }

  public Quest id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  @NotNull 
  @Schema(name = "id", example = "quest_find_trader", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Quest name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull 
  @Schema(name = "name", example = "РќР°Р№С‚Рё С‚РѕСЂРіРѕРІС†Р°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Quest description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  @NotNull 
  @Schema(name = "description", example = "РќР°Р№РґРёС‚Рµ С‚РѕСЂРіРѕРІС†Р° Р”Р¶РµР№РєР° РІ СЂР°Р№РѕРЅРµ Watson", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Quest type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @NotNull 
  @Schema(name = "type", example = "side", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public Quest level(Integer level) {
    this.level = level;
    return this;
  }

  /**
   * Get level
   * minimum: 1
   * @return level
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "level", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("level")
  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }

  public Quest rewards(QuestRewards rewards) {
    this.rewards = rewards;
    return this;
  }

  /**
   * Get rewards
   * @return rewards
   */
  @NotNull @Valid 
  @Schema(name = "rewards", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("rewards")
  public QuestRewards getRewards() {
    return rewards;
  }

  public void setRewards(QuestRewards rewards) {
    this.rewards = rewards;
  }

  public Quest objectives(List<@Valid QuestObjective> objectives) {
    this.objectives = objectives;
    return this;
  }

  public Quest addObjectivesItem(QuestObjective objectivesItem) {
    if (this.objectives == null) {
      this.objectives = new ArrayList<>();
    }
    this.objectives.add(objectivesItem);
    return this;
  }

  /**
   * Get objectives
   * @return objectives
   */
  @NotNull @Valid 
  @Schema(name = "objectives", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("objectives")
  public List<@Valid QuestObjective> getObjectives() {
    return objectives;
  }

  public void setObjectives(List<@Valid QuestObjective> objectives) {
    this.objectives = objectives;
  }

  public Quest requirements(@Nullable QuestRequirements requirements) {
    this.requirements = requirements;
    return this;
  }

  /**
   * Get requirements
   * @return requirements
   */
  @Valid 
  @Schema(name = "requirements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requirements")
  public @Nullable QuestRequirements getRequirements() {
    return requirements;
  }

  public void setRequirements(@Nullable QuestRequirements requirements) {
    this.requirements = requirements;
  }

  public Quest giver(@Nullable String giver) {
    this.giver = giver;
    return this;
  }

  /**
   * NPC ID РєРѕС‚РѕСЂС‹Р№ РґР°РµС‚ РєРІРµСЃС‚
   * @return giver
   */
  
  @Schema(name = "giver", example = "npc_trader_joe", description = "NPC ID РєРѕС‚РѕСЂС‹Р№ РґР°РµС‚ РєРІРµСЃС‚", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("giver")
  public @Nullable String getGiver() {
    return giver;
  }

  public void setGiver(@Nullable String giver) {
    this.giver = giver;
  }

  public Quest location(@Nullable String location) {
    this.location = location;
    return this;
  }

  /**
   * Р›РѕРєР°С†РёСЏ РіРґРµ РјРѕР¶РЅРѕ РїСЂРёРЅСЏС‚СЊ РєРІРµСЃС‚
   * @return location
   */
  
  @Schema(name = "location", description = "Р›РѕРєР°С†РёСЏ РіРґРµ РјРѕР¶РЅРѕ РїСЂРёРЅСЏС‚СЊ РєРІРµСЃС‚", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location")
  public @Nullable String getLocation() {
    return location;
  }

  public void setLocation(@Nullable String location) {
    this.location = location;
  }

  public Quest isRepeatable(Boolean isRepeatable) {
    this.isRepeatable = isRepeatable;
    return this;
  }

  /**
   * Get isRepeatable
   * @return isRepeatable
   */
  
  @Schema(name = "isRepeatable", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isRepeatable")
  public Boolean getIsRepeatable() {
    return isRepeatable;
  }

  public void setIsRepeatable(Boolean isRepeatable) {
    this.isRepeatable = isRepeatable;
  }

  public Quest timeLimit(Integer timeLimit) {
    this.timeLimit = JsonNullable.of(timeLimit);
    return this;
  }

  /**
   * Р›РёРјРёС‚ РІСЂРµРјРµРЅРё РІ РјРёРЅСѓС‚Р°С…
   * @return timeLimit
   */
  
  @Schema(name = "timeLimit", description = "Р›РёРјРёС‚ РІСЂРµРјРµРЅРё РІ РјРёРЅСѓС‚Р°С…", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timeLimit")
  public JsonNullable<Integer> getTimeLimit() {
    return timeLimit;
  }

  public void setTimeLimit(JsonNullable<Integer> timeLimit) {
    this.timeLimit = timeLimit;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Quest quest = (Quest) o;
    return Objects.equals(this.id, quest.id) &&
        Objects.equals(this.name, quest.name) &&
        Objects.equals(this.description, quest.description) &&
        Objects.equals(this.type, quest.type) &&
        Objects.equals(this.level, quest.level) &&
        Objects.equals(this.rewards, quest.rewards) &&
        Objects.equals(this.objectives, quest.objectives) &&
        Objects.equals(this.requirements, quest.requirements) &&
        Objects.equals(this.giver, quest.giver) &&
        Objects.equals(this.location, quest.location) &&
        Objects.equals(this.isRepeatable, quest.isRepeatable) &&
        equalsNullable(this.timeLimit, quest.timeLimit);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, type, level, rewards, objectives, requirements, giver, location, isRepeatable, hashCodeNullable(timeLimit));
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
    sb.append("class Quest {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
    sb.append("    objectives: ").append(toIndentedString(objectives)).append("\n");
    sb.append("    requirements: ").append(toIndentedString(requirements)).append("\n");
    sb.append("    giver: ").append(toIndentedString(giver)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    isRepeatable: ").append(toIndentedString(isRepeatable)).append("\n");
    sb.append("    timeLimit: ").append(toIndentedString(timeLimit)).append("\n");
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


