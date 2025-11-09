package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.CodexEntry;
import java.math.BigDecimal;
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
 * GetCharacterCodex200Response
 */

@JsonTypeName("getCharacterCodex_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetCharacterCodex200Response {

  @Valid
  private List<@Valid CodexEntry> entries = new ArrayList<>();

  private @Nullable BigDecimal completionPercentage;

  public GetCharacterCodex200Response entries(List<@Valid CodexEntry> entries) {
    this.entries = entries;
    return this;
  }

  public GetCharacterCodex200Response addEntriesItem(CodexEntry entriesItem) {
    if (this.entries == null) {
      this.entries = new ArrayList<>();
    }
    this.entries.add(entriesItem);
    return this;
  }

  /**
   * Get entries
   * @return entries
   */
  @Valid 
  @Schema(name = "entries", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("entries")
  public List<@Valid CodexEntry> getEntries() {
    return entries;
  }

  public void setEntries(List<@Valid CodexEntry> entries) {
    this.entries = entries;
  }

  public GetCharacterCodex200Response completionPercentage(@Nullable BigDecimal completionPercentage) {
    this.completionPercentage = completionPercentage;
    return this;
  }

  /**
   * Get completionPercentage
   * @return completionPercentage
   */
  @Valid 
  @Schema(name = "completion_percentage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("completion_percentage")
  public @Nullable BigDecimal getCompletionPercentage() {
    return completionPercentage;
  }

  public void setCompletionPercentage(@Nullable BigDecimal completionPercentage) {
    this.completionPercentage = completionPercentage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetCharacterCodex200Response getCharacterCodex200Response = (GetCharacterCodex200Response) o;
    return Objects.equals(this.entries, getCharacterCodex200Response.entries) &&
        Objects.equals(this.completionPercentage, getCharacterCodex200Response.completionPercentage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entries, completionPercentage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetCharacterCodex200Response {\n");
    sb.append("    entries: ").append(toIndentedString(entries)).append("\n");
    sb.append("    completionPercentage: ").append(toIndentedString(completionPercentage)).append("\n");
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

