package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.QuestProgress;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AcceptQuest200Response
 */

@JsonTypeName("acceptQuest_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T20:50:05.709666800+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class AcceptQuest200Response {

  private @Nullable Boolean success;

  private @Nullable String message;

  private @Nullable QuestProgress quest;

  public AcceptQuest200Response success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public AcceptQuest200Response message(@Nullable String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   */
  
  @Schema(name = "message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message")
  public @Nullable String getMessage() {
    return message;
  }

  public void setMessage(@Nullable String message) {
    this.message = message;
  }

  public AcceptQuest200Response quest(@Nullable QuestProgress quest) {
    this.quest = quest;
    return this;
  }

  /**
   * Get quest
   * @return quest
   */
  @Valid 
  @Schema(name = "quest", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quest")
  public @Nullable QuestProgress getQuest() {
    return quest;
  }

  public void setQuest(@Nullable QuestProgress quest) {
    this.quest = quest;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AcceptQuest200Response acceptQuest200Response = (AcceptQuest200Response) o;
    return Objects.equals(this.success, acceptQuest200Response.success) &&
        Objects.equals(this.message, acceptQuest200Response.message) &&
        Objects.equals(this.quest, acceptQuest200Response.quest);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, message, quest);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AcceptQuest200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    quest: ").append(toIndentedString(quest)).append("\n");
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


