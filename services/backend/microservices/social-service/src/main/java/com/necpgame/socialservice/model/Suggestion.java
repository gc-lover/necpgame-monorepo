package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Suggestion
 */


public class Suggestion {

  private @Nullable String playerId;

  private @Nullable String nickname;

  private @Nullable BigDecimal relevance;

  private @Nullable Integer mutualFriends;

  private @Nullable String source;

  public Suggestion playerId(@Nullable String playerId) {
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

  public Suggestion nickname(@Nullable String nickname) {
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

  public Suggestion relevance(@Nullable BigDecimal relevance) {
    this.relevance = relevance;
    return this;
  }

  /**
   * Get relevance
   * @return relevance
   */
  @Valid 
  @Schema(name = "relevance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relevance")
  public @Nullable BigDecimal getRelevance() {
    return relevance;
  }

  public void setRelevance(@Nullable BigDecimal relevance) {
    this.relevance = relevance;
  }

  public Suggestion mutualFriends(@Nullable Integer mutualFriends) {
    this.mutualFriends = mutualFriends;
    return this;
  }

  /**
   * Get mutualFriends
   * @return mutualFriends
   */
  
  @Schema(name = "mutualFriends", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mutualFriends")
  public @Nullable Integer getMutualFriends() {
    return mutualFriends;
  }

  public void setMutualFriends(@Nullable Integer mutualFriends) {
    this.mutualFriends = mutualFriends;
  }

  public Suggestion source(@Nullable String source) {
    this.source = source;
    return this;
  }

  /**
   * Get source
   * @return source
   */
  
  @Schema(name = "source", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("source")
  public @Nullable String getSource() {
    return source;
  }

  public void setSource(@Nullable String source) {
    this.source = source;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Suggestion suggestion = (Suggestion) o;
    return Objects.equals(this.playerId, suggestion.playerId) &&
        Objects.equals(this.nickname, suggestion.nickname) &&
        Objects.equals(this.relevance, suggestion.relevance) &&
        Objects.equals(this.mutualFriends, suggestion.mutualFriends) &&
        Objects.equals(this.source, suggestion.source);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, nickname, relevance, mutualFriends, source);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Suggestion {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    nickname: ").append(toIndentedString(nickname)).append("\n");
    sb.append("    relevance: ").append(toIndentedString(relevance)).append("\n");
    sb.append("    mutualFriends: ").append(toIndentedString(mutualFriends)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
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

