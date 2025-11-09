package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.GameQuestRewardsReputation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * GameQuestRewards
 */


public class GameQuestRewards {

  private @Nullable Integer experience;

  private @Nullable Integer money;

  @Valid
  private List<String> items = new ArrayList<>();

  private JsonNullable<GameQuestRewardsReputation> reputation = JsonNullable.<GameQuestRewardsReputation>undefined();

  public GameQuestRewards experience(@Nullable Integer experience) {
    this.experience = experience;
    return this;
  }

  /**
   * Опыт за выполнение квеста
   * minimum: 0
   * @return experience
   */
  @Min(value = 0) 
  @Schema(name = "experience", example = "100", description = "Опыт за выполнение квеста", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("experience")
  public @Nullable Integer getExperience() {
    return experience;
  }

  public void setExperience(@Nullable Integer experience) {
    this.experience = experience;
  }

  public GameQuestRewards money(@Nullable Integer money) {
    this.money = money;
    return this;
  }

  /**
   * Деньги (eddies) за выполнение квеста
   * minimum: 0
   * @return money
   */
  @Min(value = 0) 
  @Schema(name = "money", example = "200", description = "Деньги (eddies) за выполнение квеста", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("money")
  public @Nullable Integer getMoney() {
    return money;
  }

  public void setMoney(@Nullable Integer money) {
    this.money = money;
  }

  public GameQuestRewards items(List<String> items) {
    this.items = items;
    return this;
  }

  public GameQuestRewards addItemsItem(String itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(itemsItem);
    return this;
  }

  /**
   * Список ID предметов в награду
   * @return items
   */
  
  @Schema(name = "items", example = "[]", description = "Список ID предметов в награду", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("items")
  public List<String> getItems() {
    return items;
  }

  public void setItems(List<String> items) {
    this.items = items;
  }

  public GameQuestRewards reputation(GameQuestRewardsReputation reputation) {
    this.reputation = JsonNullable.of(reputation);
    return this;
  }

  /**
   * Get reputation
   * @return reputation
   */
  @Valid 
  @Schema(name = "reputation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputation")
  public JsonNullable<GameQuestRewardsReputation> getReputation() {
    return reputation;
  }

  public void setReputation(JsonNullable<GameQuestRewardsReputation> reputation) {
    this.reputation = reputation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameQuestRewards gameQuestRewards = (GameQuestRewards) o;
    return Objects.equals(this.experience, gameQuestRewards.experience) &&
        Objects.equals(this.money, gameQuestRewards.money) &&
        Objects.equals(this.items, gameQuestRewards.items) &&
        equalsNullable(this.reputation, gameQuestRewards.reputation);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(experience, money, items, hashCodeNullable(reputation));
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
    sb.append("class GameQuestRewards {\n");
    sb.append("    experience: ").append(toIndentedString(experience)).append("\n");
    sb.append("    money: ").append(toIndentedString(money)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    reputation: ").append(toIndentedString(reputation)).append("\n");
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

