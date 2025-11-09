package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.necpgame.backjava.model.UseItem200ResponseEffectsInner;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * UseItem200Response
 */

@JsonTypeName("useItem_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T20:50:45.778329200+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class UseItem200Response {

  private Boolean success;

  private String message;

  @Valid
  private List<@Valid UseItem200ResponseEffectsInner> effects = new ArrayList<>();

  public UseItem200Response() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UseItem200Response(Boolean success, String message, List<@Valid UseItem200ResponseEffectsInner> effects) {
    this.success = success;
    this.message = message;
    this.effects = effects;
  }

  public UseItem200Response success(Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  @NotNull 
  @Schema(name = "success", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("success")
  public Boolean getSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  public UseItem200Response message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   */
  @NotNull 
  @Schema(name = "message", example = "РџСЂРµРґРјРµС‚ СѓСЃРїРµС€РЅРѕ РёСЃРїРѕР»СЊР·РѕРІР°РЅ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public UseItem200Response effects(List<@Valid UseItem200ResponseEffectsInner> effects) {
    this.effects = effects;
    return this;
  }

  public UseItem200Response addEffectsItem(UseItem200ResponseEffectsInner effectsItem) {
    if (this.effects == null) {
      this.effects = new ArrayList<>();
    }
    this.effects.add(effectsItem);
    return this;
  }

  /**
   * РџСЂРёРјРµРЅРµРЅРЅС‹Рµ СЌС„С„РµРєС‚С‹ РѕС‚ РїСЂРµРґРјРµС‚Р°
   * @return effects
   */
  @NotNull @Valid 
  @Schema(name = "effects", example = "[{\"type\":\"heal\",\"value\":50,\"description\":\"Р’РѕСЃСЃС‚Р°РЅРѕРІР»РµРЅРѕ 50 HP\"}]", description = "РџСЂРёРјРµРЅРµРЅРЅС‹Рµ СЌС„С„РµРєС‚С‹ РѕС‚ РїСЂРµРґРјРµС‚Р°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("effects")
  public List<@Valid UseItem200ResponseEffectsInner> getEffects() {
    return effects;
  }

  public void setEffects(List<@Valid UseItem200ResponseEffectsInner> effects) {
    this.effects = effects;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UseItem200Response useItem200Response = (UseItem200Response) o;
    return Objects.equals(this.success, useItem200Response.success) &&
        Objects.equals(this.message, useItem200Response.message) &&
        Objects.equals(this.effects, useItem200Response.effects);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, message, effects);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UseItem200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    effects: ").append(toIndentedString(effects)).append("\n");
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



