package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.CollectionProgress;
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
 * CollectionProgressResponse
 */


public class CollectionProgressResponse {

  private @Nullable String playerId;

  @Valid
  private List<@Valid CollectionProgress> collections = new ArrayList<>();

  public CollectionProgressResponse playerId(@Nullable String playerId) {
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

  public CollectionProgressResponse collections(List<@Valid CollectionProgress> collections) {
    this.collections = collections;
    return this;
  }

  public CollectionProgressResponse addCollectionsItem(CollectionProgress collectionsItem) {
    if (this.collections == null) {
      this.collections = new ArrayList<>();
    }
    this.collections.add(collectionsItem);
    return this;
  }

  /**
   * Get collections
   * @return collections
   */
  @Valid 
  @Schema(name = "collections", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("collections")
  public List<@Valid CollectionProgress> getCollections() {
    return collections;
  }

  public void setCollections(List<@Valid CollectionProgress> collections) {
    this.collections = collections;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CollectionProgressResponse collectionProgressResponse = (CollectionProgressResponse) o;
    return Objects.equals(this.playerId, collectionProgressResponse.playerId) &&
        Objects.equals(this.collections, collectionProgressResponse.collections);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, collections);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CollectionProgressResponse {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    collections: ").append(toIndentedString(collections)).append("\n");
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

