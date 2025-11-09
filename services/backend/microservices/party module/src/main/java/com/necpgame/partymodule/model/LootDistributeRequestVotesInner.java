package com.necpgame.partymodule.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LootDistributeRequestVotesInner
 */

@JsonTypeName("LootDistributeRequest_votes_inner")

public class LootDistributeRequestVotesInner {

  private @Nullable String memberId;

  /**
   * Gets or Sets choice
   */
  public enum ChoiceEnum {
    NEED("NEED"),
    
    GREED("GREED"),
    
    PASS("PASS");

    private final String value;

    ChoiceEnum(String value) {
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
    public static ChoiceEnum fromValue(String value) {
      for (ChoiceEnum b : ChoiceEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ChoiceEnum choice;

  public LootDistributeRequestVotesInner memberId(@Nullable String memberId) {
    this.memberId = memberId;
    return this;
  }

  /**
   * Get memberId
   * @return memberId
   */
  
  @Schema(name = "memberId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("memberId")
  public @Nullable String getMemberId() {
    return memberId;
  }

  public void setMemberId(@Nullable String memberId) {
    this.memberId = memberId;
  }

  public LootDistributeRequestVotesInner choice(@Nullable ChoiceEnum choice) {
    this.choice = choice;
    return this;
  }

  /**
   * Get choice
   * @return choice
   */
  
  @Schema(name = "choice", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("choice")
  public @Nullable ChoiceEnum getChoice() {
    return choice;
  }

  public void setChoice(@Nullable ChoiceEnum choice) {
    this.choice = choice;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootDistributeRequestVotesInner lootDistributeRequestVotesInner = (LootDistributeRequestVotesInner) o;
    return Objects.equals(this.memberId, lootDistributeRequestVotesInner.memberId) &&
        Objects.equals(this.choice, lootDistributeRequestVotesInner.choice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(memberId, choice);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootDistributeRequestVotesInner {\n");
    sb.append("    memberId: ").append(toIndentedString(memberId)).append("\n");
    sb.append("    choice: ").append(toIndentedString(choice)).append("\n");
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

