package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.necpgame.backjava.model.GameLocation;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetLocations200Response
 */

@JsonTypeName("getLocations_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T21:22:04.712198900+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class GetLocations200Response {

  @Valid
  private List<@Valid GameLocation> locations = new ArrayList<>();

  private Integer total;

  public GetLocations200Response() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GetLocations200Response(List<@Valid GameLocation> locations, Integer total) {
    this.locations = locations;
    this.total = total;
  }

  public GetLocations200Response locations(List<@Valid GameLocation> locations) {
    this.locations = locations;
    return this;
  }

  public GetLocations200Response addLocationsItem(GameLocation locationsItem) {
    if (this.locations == null) {
      this.locations = new ArrayList<>();
    }
    this.locations.add(locationsItem);
    return this;
  }

  /**
   * РЎРїРёСЃРѕРє РґРѕСЃС‚СѓРїРЅС‹С… Р»РѕРєР°С†РёР№
   * @return locations
   */
  @NotNull @Valid 
  @Schema(name = "locations", description = "РЎРїРёСЃРѕРє РґРѕСЃС‚СѓРїРЅС‹С… Р»РѕРєР°С†РёР№", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("locations")
  public List<@Valid GameLocation> getLocations() {
    return locations;
  }

  public void setLocations(List<@Valid GameLocation> locations) {
    this.locations = locations;
  }

  public GetLocations200Response total(Integer total) {
    this.total = total;
    return this;
  }

  /**
   * РћР±С‰РµРµ РєРѕР»РёС‡РµСЃС‚РІРѕ Р»РѕРєР°С†РёР№
   * @return total
   */
  @NotNull 
  @Schema(name = "total", example = "15", description = "РћР±С‰РµРµ РєРѕР»РёС‡РµСЃС‚РІРѕ Р»РѕРєР°С†РёР№", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("total")
  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetLocations200Response getLocations200Response = (GetLocations200Response) o;
    return Objects.equals(this.locations, getLocations200Response.locations) &&
        Objects.equals(this.total, getLocations200Response.total);
  }

  @Override
  public int hashCode() {
    return Objects.hash(locations, total);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetLocations200Response {\n");
    sb.append("    locations: ").append(toIndentedString(locations)).append("\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
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

