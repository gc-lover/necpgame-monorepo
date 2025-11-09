package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * РРЅРґРёРІРёРґСѓР°Р»СЊРЅРѕРµ СЌРЅРµСЂРіРµС‚РёС‡РµСЃРєРѕРµ РѕРіСЂР°РЅРёС‡РµРЅРёРµ РёРјРїР»Р°РЅС‚Р°
 */

@Schema(name = "IndividualLimit", description = "РРЅРґРёРІРёРґСѓР°Р»СЊРЅРѕРµ СЌРЅРµСЂРіРµС‚РёС‡РµСЃРєРѕРµ РѕРіСЂР°РЅРёС‡РµРЅРёРµ РёРјРїР»Р°РЅС‚Р°")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:51:47.912860600+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class IndividualLimit {

  private UUID implantId;

  private Float limit;

  private Float usage;

  private Boolean canExceed;

  public IndividualLimit() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public IndividualLimit(UUID implantId, Float limit, Float usage, Boolean canExceed) {
    this.implantId = implantId;
    this.limit = limit;
    this.usage = usage;
    this.canExceed = canExceed;
  }

  public IndividualLimit implantId(UUID implantId) {
    this.implantId = implantId;
    return this;
  }

  /**
   * РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ РёРјРїР»Р°РЅС‚Р°
   * @return implantId
   */
  @NotNull @Valid 
  @Schema(name = "implant_id", description = "РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ РёРјРїР»Р°РЅС‚Р°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("implant_id")
  public UUID getImplantId() {
    return implantId;
  }

  public void setImplantId(UUID implantId) {
    this.implantId = implantId;
  }

  public IndividualLimit limit(Float limit) {
    this.limit = limit;
    return this;
  }

  /**
   * РРЅРґРёРІРёРґСѓР°Р»СЊРЅС‹Р№ Р»РёРјРёС‚ СЌРЅРµСЂРіРёРё
   * minimum: 0
   * @return limit
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "limit", description = "РРЅРґРёРІРёРґСѓР°Р»СЊРЅС‹Р№ Р»РёРјРёС‚ СЌРЅРµСЂРіРёРё", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("limit")
  public Float getLimit() {
    return limit;
  }

  public void setLimit(Float limit) {
    this.limit = limit;
  }

  public IndividualLimit usage(Float usage) {
    this.usage = usage;
    return this;
  }

  /**
   * РўРµРєСѓС‰РµРµ РёСЃРїРѕР»СЊР·РѕРІР°РЅРёРµ СЌРЅРµСЂРіРёРё
   * minimum: 0
   * @return usage
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "usage", description = "РўРµРєСѓС‰РµРµ РёСЃРїРѕР»СЊР·РѕРІР°РЅРёРµ СЌРЅРµСЂРіРёРё", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("usage")
  public Float getUsage() {
    return usage;
  }

  public void setUsage(Float usage) {
    this.usage = usage;
  }

  public IndividualLimit canExceed(Boolean canExceed) {
    this.canExceed = canExceed;
    return this;
  }

  /**
   * РњРѕР¶РЅРѕ Р»Рё РїСЂРµРІС‹СЃРёС‚СЊ РёРЅРґРёРІРёРґСѓР°Р»СЊРЅС‹Р№ Р»РёРјРёС‚
   * @return canExceed
   */
  @NotNull 
  @Schema(name = "can_exceed", description = "РњРѕР¶РЅРѕ Р»Рё РїСЂРµРІС‹СЃРёС‚СЊ РёРЅРґРёРІРёРґСѓР°Р»СЊРЅС‹Р№ Р»РёРјРёС‚", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("can_exceed")
  public Boolean getCanExceed() {
    return canExceed;
  }

  public void setCanExceed(Boolean canExceed) {
    this.canExceed = canExceed;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndividualLimit individualLimit = (IndividualLimit) o;
    return Objects.equals(this.implantId, individualLimit.implantId) &&
        Objects.equals(this.limit, individualLimit.limit) &&
        Objects.equals(this.usage, individualLimit.usage) &&
        Objects.equals(this.canExceed, individualLimit.canExceed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(implantId, limit, usage, canExceed);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndividualLimit {\n");
    sb.append("    implantId: ").append(toIndentedString(implantId)).append("\n");
    sb.append("    limit: ").append(toIndentedString(limit)).append("\n");
    sb.append("    usage: ").append(toIndentedString(usage)).append("\n");
    sb.append("    canExceed: ").append(toIndentedString(canExceed)).append("\n");
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

