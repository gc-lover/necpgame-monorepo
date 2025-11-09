package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * LocationDetailedAllOfEconomy
 */

@JsonTypeName("LocationDetailed_allOf_economy")

public class LocationDetailedAllOfEconomy {

  @Valid
  private List<String> mainIndustries = new ArrayList<>();

  private @Nullable String wealthLevel;

  public LocationDetailedAllOfEconomy mainIndustries(List<String> mainIndustries) {
    this.mainIndustries = mainIndustries;
    return this;
  }

  public LocationDetailedAllOfEconomy addMainIndustriesItem(String mainIndustriesItem) {
    if (this.mainIndustries == null) {
      this.mainIndustries = new ArrayList<>();
    }
    this.mainIndustries.add(mainIndustriesItem);
    return this;
  }

  /**
   * Get mainIndustries
   * @return mainIndustries
   */
  
  @Schema(name = "main_industries", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("main_industries")
  public List<String> getMainIndustries() {
    return mainIndustries;
  }

  public void setMainIndustries(List<String> mainIndustries) {
    this.mainIndustries = mainIndustries;
  }

  public LocationDetailedAllOfEconomy wealthLevel(@Nullable String wealthLevel) {
    this.wealthLevel = wealthLevel;
    return this;
  }

  /**
   * Get wealthLevel
   * @return wealthLevel
   */
  
  @Schema(name = "wealth_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("wealth_level")
  public @Nullable String getWealthLevel() {
    return wealthLevel;
  }

  public void setWealthLevel(@Nullable String wealthLevel) {
    this.wealthLevel = wealthLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LocationDetailedAllOfEconomy locationDetailedAllOfEconomy = (LocationDetailedAllOfEconomy) o;
    return Objects.equals(this.mainIndustries, locationDetailedAllOfEconomy.mainIndustries) &&
        Objects.equals(this.wealthLevel, locationDetailedAllOfEconomy.wealthLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mainIndustries, wealthLevel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LocationDetailedAllOfEconomy {\n");
    sb.append("    mainIndustries: ").append(toIndentedString(mainIndustries)).append("\n");
    sb.append("    wealthLevel: ").append(toIndentedString(wealthLevel)).append("\n");
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

