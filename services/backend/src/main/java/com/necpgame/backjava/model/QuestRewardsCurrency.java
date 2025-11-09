package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * QuestRewardsCurrency
 */

@JsonTypeName("QuestRewards_currency")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class QuestRewardsCurrency {

  private @Nullable Integer eddies;

  public QuestRewardsCurrency eddies(@Nullable Integer eddies) {
    this.eddies = eddies;
    return this;
  }

  /**
   * Get eddies
   * @return eddies
   */
  
  @Schema(name = "eddies", example = "10000", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("eddies")
  public @Nullable Integer getEddies() {
    return eddies;
  }

  public void setEddies(@Nullable Integer eddies) {
    this.eddies = eddies;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestRewardsCurrency questRewardsCurrency = (QuestRewardsCurrency) o;
    return Objects.equals(this.eddies, questRewardsCurrency.eddies);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eddies);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestRewardsCurrency {\n");
    sb.append("    eddies: ").append(toIndentedString(eddies)).append("\n");
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

