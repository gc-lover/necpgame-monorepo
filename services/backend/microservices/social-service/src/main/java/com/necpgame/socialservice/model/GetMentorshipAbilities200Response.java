package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.socialservice.model.MentorAbility;
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
 * GetMentorshipAbilities200Response
 */

@JsonTypeName("getMentorshipAbilities_200_response")

public class GetMentorshipAbilities200Response {

  @Valid
  private List<@Valid MentorAbility> availableAbilities = new ArrayList<>();

  @Valid
  private List<@Valid MentorAbility> learnedAbilities = new ArrayList<>();

  public GetMentorshipAbilities200Response availableAbilities(List<@Valid MentorAbility> availableAbilities) {
    this.availableAbilities = availableAbilities;
    return this;
  }

  public GetMentorshipAbilities200Response addAvailableAbilitiesItem(MentorAbility availableAbilitiesItem) {
    if (this.availableAbilities == null) {
      this.availableAbilities = new ArrayList<>();
    }
    this.availableAbilities.add(availableAbilitiesItem);
    return this;
  }

  /**
   * Get availableAbilities
   * @return availableAbilities
   */
  @Valid 
  @Schema(name = "available_abilities", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("available_abilities")
  public List<@Valid MentorAbility> getAvailableAbilities() {
    return availableAbilities;
  }

  public void setAvailableAbilities(List<@Valid MentorAbility> availableAbilities) {
    this.availableAbilities = availableAbilities;
  }

  public GetMentorshipAbilities200Response learnedAbilities(List<@Valid MentorAbility> learnedAbilities) {
    this.learnedAbilities = learnedAbilities;
    return this;
  }

  public GetMentorshipAbilities200Response addLearnedAbilitiesItem(MentorAbility learnedAbilitiesItem) {
    if (this.learnedAbilities == null) {
      this.learnedAbilities = new ArrayList<>();
    }
    this.learnedAbilities.add(learnedAbilitiesItem);
    return this;
  }

  /**
   * Get learnedAbilities
   * @return learnedAbilities
   */
  @Valid 
  @Schema(name = "learned_abilities", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("learned_abilities")
  public List<@Valid MentorAbility> getLearnedAbilities() {
    return learnedAbilities;
  }

  public void setLearnedAbilities(List<@Valid MentorAbility> learnedAbilities) {
    this.learnedAbilities = learnedAbilities;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetMentorshipAbilities200Response getMentorshipAbilities200Response = (GetMentorshipAbilities200Response) o;
    return Objects.equals(this.availableAbilities, getMentorshipAbilities200Response.availableAbilities) &&
        Objects.equals(this.learnedAbilities, getMentorshipAbilities200Response.learnedAbilities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(availableAbilities, learnedAbilities);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetMentorshipAbilities200Response {\n");
    sb.append("    availableAbilities: ").append(toIndentedString(availableAbilities)).append("\n");
    sb.append("    learnedAbilities: ").append(toIndentedString(learnedAbilities)).append("\n");
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

