package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.FactionQuest;
import com.necpgame.backjava.model.GetAvailableFactionQuests200ResponseLockedQuestsInner;
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
 * GetAvailableFactionQuests200Response
 */

@JsonTypeName("getAvailableFactionQuests_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetAvailableFactionQuests200Response {

  @Valid
  private List<@Valid FactionQuest> availableQuests = new ArrayList<>();

  @Valid
  private List<@Valid GetAvailableFactionQuests200ResponseLockedQuestsInner> lockedQuests = new ArrayList<>();

  public GetAvailableFactionQuests200Response availableQuests(List<@Valid FactionQuest> availableQuests) {
    this.availableQuests = availableQuests;
    return this;
  }

  public GetAvailableFactionQuests200Response addAvailableQuestsItem(FactionQuest availableQuestsItem) {
    if (this.availableQuests == null) {
      this.availableQuests = new ArrayList<>();
    }
    this.availableQuests.add(availableQuestsItem);
    return this;
  }

  /**
   * Get availableQuests
   * @return availableQuests
   */
  @Valid 
  @Schema(name = "available_quests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("available_quests")
  public List<@Valid FactionQuest> getAvailableQuests() {
    return availableQuests;
  }

  public void setAvailableQuests(List<@Valid FactionQuest> availableQuests) {
    this.availableQuests = availableQuests;
  }

  public GetAvailableFactionQuests200Response lockedQuests(List<@Valid GetAvailableFactionQuests200ResponseLockedQuestsInner> lockedQuests) {
    this.lockedQuests = lockedQuests;
    return this;
  }

  public GetAvailableFactionQuests200Response addLockedQuestsItem(GetAvailableFactionQuests200ResponseLockedQuestsInner lockedQuestsItem) {
    if (this.lockedQuests == null) {
      this.lockedQuests = new ArrayList<>();
    }
    this.lockedQuests.add(lockedQuestsItem);
    return this;
  }

  /**
   * Квесты с требованиями
   * @return lockedQuests
   */
  @Valid 
  @Schema(name = "locked_quests", description = "Квесты с требованиями", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("locked_quests")
  public List<@Valid GetAvailableFactionQuests200ResponseLockedQuestsInner> getLockedQuests() {
    return lockedQuests;
  }

  public void setLockedQuests(List<@Valid GetAvailableFactionQuests200ResponseLockedQuestsInner> lockedQuests) {
    this.lockedQuests = lockedQuests;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetAvailableFactionQuests200Response getAvailableFactionQuests200Response = (GetAvailableFactionQuests200Response) o;
    return Objects.equals(this.availableQuests, getAvailableFactionQuests200Response.availableQuests) &&
        Objects.equals(this.lockedQuests, getAvailableFactionQuests200Response.lockedQuests);
  }

  @Override
  public int hashCode() {
    return Objects.hash(availableQuests, lockedQuests);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetAvailableFactionQuests200Response {\n");
    sb.append("    availableQuests: ").append(toIndentedString(availableQuests)).append("\n");
    sb.append("    lockedQuests: ").append(toIndentedString(lockedQuests)).append("\n");
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

