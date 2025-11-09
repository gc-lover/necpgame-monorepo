package com.necpgame.backjava.model;

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
 * РўСЂРµР±РѕРІР°РЅРёСЏ РґР»СЏ РґРѕСЃС‚СѓРїР° (РµСЃР»Рё accessible&#x3D;false)
 */

@Schema(name = "ConnectedLocation_requirements", description = "РўСЂРµР±РѕРІР°РЅРёСЏ РґР»СЏ РґРѕСЃС‚СѓРїР° (РµСЃР»Рё accessible=false)")
@JsonTypeName("ConnectedLocation_requirements")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T21:22:04.712198900+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class ConnectedLocationRequirements {

  private @Nullable Integer minLevel;

  @Valid
  private List<String> requiredQuests = new ArrayList<>();

  public ConnectedLocationRequirements minLevel(@Nullable Integer minLevel) {
    this.minLevel = minLevel;
    return this;
  }

  /**
   * РњРёРЅРёРјР°Р»СЊРЅС‹Р№ СѓСЂРѕРІРµРЅСЊ
   * @return minLevel
   */
  
  @Schema(name = "minLevel", description = "РњРёРЅРёРјР°Р»СЊРЅС‹Р№ СѓСЂРѕРІРµРЅСЊ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("minLevel")
  public @Nullable Integer getMinLevel() {
    return minLevel;
  }

  public void setMinLevel(@Nullable Integer minLevel) {
    this.minLevel = minLevel;
  }

  public ConnectedLocationRequirements requiredQuests(List<String> requiredQuests) {
    this.requiredQuests = requiredQuests;
    return this;
  }

  public ConnectedLocationRequirements addRequiredQuestsItem(String requiredQuestsItem) {
    if (this.requiredQuests == null) {
      this.requiredQuests = new ArrayList<>();
    }
    this.requiredQuests.add(requiredQuestsItem);
    return this;
  }

  /**
   * РўСЂРµР±СѓРµРјС‹Рµ Р·Р°РІРµСЂС€РµРЅРЅС‹Рµ РєРІРµСЃС‚С‹
   * @return requiredQuests
   */
  
  @Schema(name = "requiredQuests", description = "РўСЂРµР±СѓРµРјС‹Рµ Р·Р°РІРµСЂС€РµРЅРЅС‹Рµ РєРІРµСЃС‚С‹", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requiredQuests")
  public List<String> getRequiredQuests() {
    return requiredQuests;
  }

  public void setRequiredQuests(List<String> requiredQuests) {
    this.requiredQuests = requiredQuests;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConnectedLocationRequirements connectedLocationRequirements = (ConnectedLocationRequirements) o;
    return Objects.equals(this.minLevel, connectedLocationRequirements.minLevel) &&
        Objects.equals(this.requiredQuests, connectedLocationRequirements.requiredQuests);
  }

  @Override
  public int hashCode() {
    return Objects.hash(minLevel, requiredQuests);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConnectedLocationRequirements {\n");
    sb.append("    minLevel: ").append(toIndentedString(minLevel)).append("\n");
    sb.append("    requiredQuests: ").append(toIndentedString(requiredQuests)).append("\n");
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

