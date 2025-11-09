package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.GameQuestRewards;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GameQuest
 */


public class GameQuest {

  private String id;

  private String name;

  private String description;

  /**
   * Тип квеста: - main: основной квест - side: побочный квест - contract: контракт 
   */
  public enum TypeEnum {
    MAIN("main"),
    
    SIDE("side"),
    
    CONTRACT("contract");

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

  private @Nullable TypeEnum type;

  private Integer level;

  private String giverNpcId;

  private GameQuestRewards rewards;

  public GameQuest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GameQuest(String id, String name, String description, Integer level, String giverNpcId, GameQuestRewards rewards) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.level = level;
    this.giverNpcId = giverNpcId;
    this.rewards = rewards;
  }

  public GameQuest id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Уникальный идентификатор квеста
   * @return id
   */
  @NotNull 
  @Schema(name = "id", example = "quest-delivery-001", description = "Уникальный идентификатор квеста", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public GameQuest name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Название квеста
   * @return name
   */
  @NotNull @Size(min = 1, max = 200) 
  @Schema(name = "name", example = "Доставка груза", description = "Название квеста", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public GameQuest description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Описание квеста
   * @return description
   */
  @NotNull @Size(min = 10, max = 2000) 
  @Schema(name = "description", example = "Офицер NCPD Сара Миллер просит доставить посылку в Watson. Это простое задание для новичка.", description = "Описание квеста", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public GameQuest type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Тип квеста: - main: основной квест - side: побочный квест - contract: контракт 
   * @return type
   */
  
  @Schema(name = "type", example = "side", description = "Тип квеста: - main: основной квест - side: побочный квест - contract: контракт ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public GameQuest level(Integer level) {
    this.level = level;
    return this;
  }

  /**
   * Рекомендуемый уровень для квеста
   * minimum: 1
   * @return level
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "level", example = "1", description = "Рекомендуемый уровень для квеста", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("level")
  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }

  public GameQuest giverNpcId(String giverNpcId) {
    this.giverNpcId = giverNpcId;
    return this;
  }

  /**
   * ID NPC, дающего квест
   * @return giverNpcId
   */
  @NotNull 
  @Schema(name = "giverNpcId", example = "npc-sarah-miller", description = "ID NPC, дающего квест", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("giverNpcId")
  public String getGiverNpcId() {
    return giverNpcId;
  }

  public void setGiverNpcId(String giverNpcId) {
    this.giverNpcId = giverNpcId;
  }

  public GameQuest rewards(GameQuestRewards rewards) {
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
  public GameQuestRewards getRewards() {
    return rewards;
  }

  public void setRewards(GameQuestRewards rewards) {
    this.rewards = rewards;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameQuest gameQuest = (GameQuest) o;
    return Objects.equals(this.id, gameQuest.id) &&
        Objects.equals(this.name, gameQuest.name) &&
        Objects.equals(this.description, gameQuest.description) &&
        Objects.equals(this.type, gameQuest.type) &&
        Objects.equals(this.level, gameQuest.level) &&
        Objects.equals(this.giverNpcId, gameQuest.giverNpcId) &&
        Objects.equals(this.rewards, gameQuest.rewards);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, type, level, giverNpcId, rewards);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GameQuest {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    giverNpcId: ").append(toIndentedString(giverNpcId)).append("\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
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

