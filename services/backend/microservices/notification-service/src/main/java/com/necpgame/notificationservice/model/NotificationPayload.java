package com.necpgame.notificationservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.notificationservice.model.NotificationPayloadCta;
import com.necpgame.notificationservice.model.NotificationPayloadEntitiesInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * NotificationPayload
 */


public class NotificationPayload {

  private @Nullable String context;

  @Valid
  private List<@Valid NotificationPayloadEntitiesInner> entities = new ArrayList<>();

  private @Nullable NotificationPayloadCta cta;

  @Valid
  private Map<String, Object> metadata = new HashMap<>();

  public NotificationPayload context(@Nullable String context) {
    this.context = context;
    return this;
  }

  /**
   * Get context
   * @return context
   */
  
  @Schema(name = "context", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("context")
  public @Nullable String getContext() {
    return context;
  }

  public void setContext(@Nullable String context) {
    this.context = context;
  }

  public NotificationPayload entities(List<@Valid NotificationPayloadEntitiesInner> entities) {
    this.entities = entities;
    return this;
  }

  public NotificationPayload addEntitiesItem(NotificationPayloadEntitiesInner entitiesItem) {
    if (this.entities == null) {
      this.entities = new ArrayList<>();
    }
    this.entities.add(entitiesItem);
    return this;
  }

  /**
   * Get entities
   * @return entities
   */
  @Valid 
  @Schema(name = "entities", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("entities")
  public List<@Valid NotificationPayloadEntitiesInner> getEntities() {
    return entities;
  }

  public void setEntities(List<@Valid NotificationPayloadEntitiesInner> entities) {
    this.entities = entities;
  }

  public NotificationPayload cta(@Nullable NotificationPayloadCta cta) {
    this.cta = cta;
    return this;
  }

  /**
   * Get cta
   * @return cta
   */
  @Valid 
  @Schema(name = "cta", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cta")
  public @Nullable NotificationPayloadCta getCta() {
    return cta;
  }

  public void setCta(@Nullable NotificationPayloadCta cta) {
    this.cta = cta;
  }

  public NotificationPayload metadata(Map<String, Object> metadata) {
    this.metadata = metadata;
    return this;
  }

  public NotificationPayload putMetadataItem(String key, Object metadataItem) {
    if (this.metadata == null) {
      this.metadata = new HashMap<>();
    }
    this.metadata.put(key, metadataItem);
    return this;
  }

  /**
   * Get metadata
   * @return metadata
   */
  
  @Schema(name = "metadata", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metadata")
  public Map<String, Object> getMetadata() {
    return metadata;
  }

  public void setMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationPayload notificationPayload = (NotificationPayload) o;
    return Objects.equals(this.context, notificationPayload.context) &&
        Objects.equals(this.entities, notificationPayload.entities) &&
        Objects.equals(this.cta, notificationPayload.cta) &&
        Objects.equals(this.metadata, notificationPayload.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(context, entities, cta, metadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationPayload {\n");
    sb.append("    context: ").append(toIndentedString(context)).append("\n");
    sb.append("    entities: ").append(toIndentedString(entities)).append("\n");
    sb.append("    cta: ").append(toIndentedString(cta)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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

