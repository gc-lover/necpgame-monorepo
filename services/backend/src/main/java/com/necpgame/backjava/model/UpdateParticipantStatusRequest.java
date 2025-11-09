package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.StatusEffect;
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
 * UpdateParticipantStatusRequest
 */

@JsonTypeName("updateParticipantStatus_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class UpdateParticipantStatusRequest {

  private @Nullable Integer hp;

  @Valid
  private List<@Valid StatusEffect> statusEffects = new ArrayList<>();

  public UpdateParticipantStatusRequest hp(@Nullable Integer hp) {
    this.hp = hp;
    return this;
  }

  /**
   * Get hp
   * @return hp
   */
  
  @Schema(name = "hp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hp")
  public @Nullable Integer getHp() {
    return hp;
  }

  public void setHp(@Nullable Integer hp) {
    this.hp = hp;
  }

  public UpdateParticipantStatusRequest statusEffects(List<@Valid StatusEffect> statusEffects) {
    this.statusEffects = statusEffects;
    return this;
  }

  public UpdateParticipantStatusRequest addStatusEffectsItem(StatusEffect statusEffectsItem) {
    if (this.statusEffects == null) {
      this.statusEffects = new ArrayList<>();
    }
    this.statusEffects.add(statusEffectsItem);
    return this;
  }

  /**
   * Get statusEffects
   * @return statusEffects
   */
  @Valid 
  @Schema(name = "status_effects", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status_effects")
  public List<@Valid StatusEffect> getStatusEffects() {
    return statusEffects;
  }

  public void setStatusEffects(List<@Valid StatusEffect> statusEffects) {
    this.statusEffects = statusEffects;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateParticipantStatusRequest updateParticipantStatusRequest = (UpdateParticipantStatusRequest) o;
    return Objects.equals(this.hp, updateParticipantStatusRequest.hp) &&
        Objects.equals(this.statusEffects, updateParticipantStatusRequest.statusEffects);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hp, statusEffects);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateParticipantStatusRequest {\n");
    sb.append("    hp: ").append(toIndentedString(hp)).append("\n");
    sb.append("    statusEffects: ").append(toIndentedString(statusEffects)).append("\n");
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

