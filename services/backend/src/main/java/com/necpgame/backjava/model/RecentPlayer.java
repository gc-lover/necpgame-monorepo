package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RecentPlayer
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class RecentPlayer {

  private @Nullable String playerId;

  private @Nullable String nickname;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime metAt;

  /**
   * Gets or Sets context
   */
  public enum ContextEnum {
    MATCH("MATCH"),
    
    TRADE("TRADE"),
    
    CLAN("CLAN"),
    
    OTHER("OTHER");

    private final String value;

    ContextEnum(String value) {
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
    public static ContextEnum fromValue(String value) {
      for (ContextEnum b : ContextEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ContextEnum context;

  private @Nullable BigDecimal score;

  public RecentPlayer playerId(@Nullable String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("playerId")
  public @Nullable String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(@Nullable String playerId) {
    this.playerId = playerId;
  }

  public RecentPlayer nickname(@Nullable String nickname) {
    this.nickname = nickname;
    return this;
  }

  /**
   * Get nickname
   * @return nickname
   */
  
  @Schema(name = "nickname", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nickname")
  public @Nullable String getNickname() {
    return nickname;
  }

  public void setNickname(@Nullable String nickname) {
    this.nickname = nickname;
  }

  public RecentPlayer metAt(@Nullable OffsetDateTime metAt) {
    this.metAt = metAt;
    return this;
  }

  /**
   * Get metAt
   * @return metAt
   */
  @Valid 
  @Schema(name = "metAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metAt")
  public @Nullable OffsetDateTime getMetAt() {
    return metAt;
  }

  public void setMetAt(@Nullable OffsetDateTime metAt) {
    this.metAt = metAt;
  }

  public RecentPlayer context(@Nullable ContextEnum context) {
    this.context = context;
    return this;
  }

  /**
   * Get context
   * @return context
   */
  
  @Schema(name = "context", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("context")
  public @Nullable ContextEnum getContext() {
    return context;
  }

  public void setContext(@Nullable ContextEnum context) {
    this.context = context;
  }

  public RecentPlayer score(@Nullable BigDecimal score) {
    this.score = score;
    return this;
  }

  /**
   * Get score
   * @return score
   */
  @Valid 
  @Schema(name = "score", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("score")
  public @Nullable BigDecimal getScore() {
    return score;
  }

  public void setScore(@Nullable BigDecimal score) {
    this.score = score;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RecentPlayer recentPlayer = (RecentPlayer) o;
    return Objects.equals(this.playerId, recentPlayer.playerId) &&
        Objects.equals(this.nickname, recentPlayer.nickname) &&
        Objects.equals(this.metAt, recentPlayer.metAt) &&
        Objects.equals(this.context, recentPlayer.context) &&
        Objects.equals(this.score, recentPlayer.score);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, nickname, metAt, context, score);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RecentPlayer {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    nickname: ").append(toIndentedString(nickname)).append("\n");
    sb.append("    metAt: ").append(toIndentedString(metAt)).append("\n");
    sb.append("    context: ").append(toIndentedString(context)).append("\n");
    sb.append("    score: ").append(toIndentedString(score)).append("\n");
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

