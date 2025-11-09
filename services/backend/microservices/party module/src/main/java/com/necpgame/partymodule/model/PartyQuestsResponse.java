package com.necpgame.partymodule.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.partymodule.model.QuestSyncEvent;
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
 * PartyQuestsResponse
 */


public class PartyQuestsResponse {

  @Valid
  private List<@Valid QuestSyncEvent> quests = new ArrayList<>();

  public PartyQuestsResponse quests(List<@Valid QuestSyncEvent> quests) {
    this.quests = quests;
    return this;
  }

  public PartyQuestsResponse addQuestsItem(QuestSyncEvent questsItem) {
    if (this.quests == null) {
      this.quests = new ArrayList<>();
    }
    this.quests.add(questsItem);
    return this;
  }

  /**
   * Get quests
   * @return quests
   */
  @Valid 
  @Schema(name = "quests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quests")
  public List<@Valid QuestSyncEvent> getQuests() {
    return quests;
  }

  public void setQuests(List<@Valid QuestSyncEvent> quests) {
    this.quests = quests;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PartyQuestsResponse partyQuestsResponse = (PartyQuestsResponse) o;
    return Objects.equals(this.quests, partyQuestsResponse.quests);
  }

  @Override
  public int hashCode() {
    return Objects.hash(quests);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PartyQuestsResponse {\n");
    sb.append("    quests: ").append(toIndentedString(quests)).append("\n");
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

