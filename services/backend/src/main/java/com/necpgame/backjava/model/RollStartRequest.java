package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.LootItem;
import com.necpgame.backjava.model.RollParticipant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RollStartRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class RollStartRequest {

  private UUID resultId;

  private LootItem item;

  @Valid
  private List<@Valid RollParticipant> participants = new ArrayList<>();

  public RollStartRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RollStartRequest(UUID resultId, LootItem item) {
    this.resultId = resultId;
    this.item = item;
  }

  public RollStartRequest resultId(UUID resultId) {
    this.resultId = resultId;
    return this;
  }

  /**
   * Get resultId
   * @return resultId
   */
  @NotNull @Valid 
  @Schema(name = "resultId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("resultId")
  public UUID getResultId() {
    return resultId;
  }

  public void setResultId(UUID resultId) {
    this.resultId = resultId;
  }

  public RollStartRequest item(LootItem item) {
    this.item = item;
    return this;
  }

  /**
   * Get item
   * @return item
   */
  @NotNull @Valid 
  @Schema(name = "item", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("item")
  public LootItem getItem() {
    return item;
  }

  public void setItem(LootItem item) {
    this.item = item;
  }

  public RollStartRequest participants(List<@Valid RollParticipant> participants) {
    this.participants = participants;
    return this;
  }

  public RollStartRequest addParticipantsItem(RollParticipant participantsItem) {
    if (this.participants == null) {
      this.participants = new ArrayList<>();
    }
    this.participants.add(participantsItem);
    return this;
  }

  /**
   * Get participants
   * @return participants
   */
  @Valid 
  @Schema(name = "participants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("participants")
  public List<@Valid RollParticipant> getParticipants() {
    return participants;
  }

  public void setParticipants(List<@Valid RollParticipant> participants) {
    this.participants = participants;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RollStartRequest rollStartRequest = (RollStartRequest) o;
    return Objects.equals(this.resultId, rollStartRequest.resultId) &&
        Objects.equals(this.item, rollStartRequest.item) &&
        Objects.equals(this.participants, rollStartRequest.participants);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resultId, item, participants);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RollStartRequest {\n");
    sb.append("    resultId: ").append(toIndentedString(resultId)).append("\n");
    sb.append("    item: ").append(toIndentedString(item)).append("\n");
    sb.append("    participants: ").append(toIndentedString(participants)).append("\n");
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

