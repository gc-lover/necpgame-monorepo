package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.CharacterSwitchedEventPayload;
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
 * CharacterSwitchedEvent
 */


public class CharacterSwitchedEvent {

  private String topic;

  private String producer;

  private CharacterSwitchedEventPayload payload;

  /**
   * Gets or Sets consumers
   */
  public enum ConsumersEnum {
    SESSION_SERVICE("session-service"),
    
    GAMEPLAY_SERVICE("gameplay-service"),
    
    NOTIFICATION_SERVICE("notification-service"),
    
    TELEMETRY("telemetry");

    private final String value;

    ConsumersEnum(String value) {
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
    public static ConsumersEnum fromValue(String value) {
      for (ConsumersEnum b : ConsumersEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @Valid
  private List<ConsumersEnum> consumers = new ArrayList<>();

  public CharacterSwitchedEvent() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CharacterSwitchedEvent(String topic, String producer, CharacterSwitchedEventPayload payload, List<ConsumersEnum> consumers) {
    this.topic = topic;
    this.producer = producer;
    this.payload = payload;
    this.consumers = consumers;
  }

  public CharacterSwitchedEvent topic(String topic) {
    this.topic = topic;
    return this;
  }

  /**
   * Get topic
   * @return topic
   */
  @NotNull 
  @Schema(name = "topic", example = "characters.lifecycle.switched", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("topic")
  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public CharacterSwitchedEvent producer(String producer) {
    this.producer = producer;
    return this;
  }

  /**
   * Get producer
   * @return producer
   */
  @NotNull 
  @Schema(name = "producer", example = "character-service", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("producer")
  public String getProducer() {
    return producer;
  }

  public void setProducer(String producer) {
    this.producer = producer;
  }

  public CharacterSwitchedEvent payload(CharacterSwitchedEventPayload payload) {
    this.payload = payload;
    return this;
  }

  /**
   * Get payload
   * @return payload
   */
  @NotNull @Valid 
  @Schema(name = "payload", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("payload")
  public CharacterSwitchedEventPayload getPayload() {
    return payload;
  }

  public void setPayload(CharacterSwitchedEventPayload payload) {
    this.payload = payload;
  }

  public CharacterSwitchedEvent consumers(List<ConsumersEnum> consumers) {
    this.consumers = consumers;
    return this;
  }

  public CharacterSwitchedEvent addConsumersItem(ConsumersEnum consumersItem) {
    if (this.consumers == null) {
      this.consumers = new ArrayList<>();
    }
    this.consumers.add(consumersItem);
    return this;
  }

  /**
   * Get consumers
   * @return consumers
   */
  @NotNull 
  @Schema(name = "consumers", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("consumers")
  public List<ConsumersEnum> getConsumers() {
    return consumers;
  }

  public void setConsumers(List<ConsumersEnum> consumers) {
    this.consumers = consumers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterSwitchedEvent characterSwitchedEvent = (CharacterSwitchedEvent) o;
    return Objects.equals(this.topic, characterSwitchedEvent.topic) &&
        Objects.equals(this.producer, characterSwitchedEvent.producer) &&
        Objects.equals(this.payload, characterSwitchedEvent.payload) &&
        Objects.equals(this.consumers, characterSwitchedEvent.consumers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(topic, producer, payload, consumers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterSwitchedEvent {\n");
    sb.append("    topic: ").append(toIndentedString(topic)).append("\n");
    sb.append("    producer: ").append(toIndentedString(producer)).append("\n");
    sb.append("    payload: ").append(toIndentedString(payload)).append("\n");
    sb.append("    consumers: ").append(toIndentedString(consumers)).append("\n");
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

