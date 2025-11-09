package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.MatchSearchRequestPartyContextPartiesInner;
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
 * Информация о party, участвующих в подборе.
 */

@Schema(name = "MatchSearchRequest_partyContext", description = "Информация о party, участвующих в подборе.")
@JsonTypeName("MatchSearchRequest_partyContext")

public class MatchSearchRequestPartyContext {

  @Valid
  private List<@Valid MatchSearchRequestPartyContextPartiesInner> parties = new ArrayList<>();

  public MatchSearchRequestPartyContext parties(List<@Valid MatchSearchRequestPartyContextPartiesInner> parties) {
    this.parties = parties;
    return this;
  }

  public MatchSearchRequestPartyContext addPartiesItem(MatchSearchRequestPartyContextPartiesInner partiesItem) {
    if (this.parties == null) {
      this.parties = new ArrayList<>();
    }
    this.parties.add(partiesItem);
    return this;
  }

  /**
   * Get parties
   * @return parties
   */
  @Valid 
  @Schema(name = "parties", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("parties")
  public List<@Valid MatchSearchRequestPartyContextPartiesInner> getParties() {
    return parties;
  }

  public void setParties(List<@Valid MatchSearchRequestPartyContextPartiesInner> parties) {
    this.parties = parties;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MatchSearchRequestPartyContext matchSearchRequestPartyContext = (MatchSearchRequestPartyContext) o;
    return Objects.equals(this.parties, matchSearchRequestPartyContext.parties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(parties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MatchSearchRequestPartyContext {\n");
    sb.append("    parties: ").append(toIndentedString(parties)).append("\n");
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

