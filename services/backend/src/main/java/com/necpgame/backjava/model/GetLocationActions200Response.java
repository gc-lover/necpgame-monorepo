package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.necpgame.backjava.model.LocationAction;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetLocationActions200Response
 */

@JsonTypeName("getLocationActions_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T21:22:04.712198900+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class GetLocationActions200Response {

  @Valid
  private List<@Valid LocationAction> actions = new ArrayList<>();

  public GetLocationActions200Response() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GetLocationActions200Response(List<@Valid LocationAction> actions) {
    this.actions = actions;
  }

  public GetLocationActions200Response actions(List<@Valid LocationAction> actions) {
    this.actions = actions;
    return this;
  }

  public GetLocationActions200Response addActionsItem(LocationAction actionsItem) {
    if (this.actions == null) {
      this.actions = new ArrayList<>();
    }
    this.actions.add(actionsItem);
    return this;
  }

  /**
   * РЎРїРёСЃРѕРє РґРѕСЃС‚СѓРїРЅС‹С… РґРµР№СЃС‚РІРёР№
   * @return actions
   */
  @NotNull @Valid 
  @Schema(name = "actions", description = "РЎРїРёСЃРѕРє РґРѕСЃС‚СѓРїРЅС‹С… РґРµР№СЃС‚РІРёР№", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("actions")
  public List<@Valid LocationAction> getActions() {
    return actions;
  }

  public void setActions(List<@Valid LocationAction> actions) {
    this.actions = actions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetLocationActions200Response getLocationActions200Response = (GetLocationActions200Response) o;
    return Objects.equals(this.actions, getLocationActions200Response.actions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(actions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetLocationActions200Response {\n");
    sb.append("    actions: ").append(toIndentedString(actions)).append("\n");
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

