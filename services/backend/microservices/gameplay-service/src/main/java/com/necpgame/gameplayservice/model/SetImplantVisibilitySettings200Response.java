package com.necpgame.gameplayservice.model;

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
 * SetImplantVisibilitySettings200Response
 */

@JsonTypeName("setImplantVisibilitySettings_200_response")

public class SetImplantVisibilitySettings200Response {

  private @Nullable Boolean success;

  private @Nullable String characterId;

  private @Nullable String visibilityMode;

  public SetImplantVisibilitySettings200Response success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public SetImplantVisibilitySettings200Response characterId(@Nullable String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable String characterId) {
    this.characterId = characterId;
  }

  public SetImplantVisibilitySettings200Response visibilityMode(@Nullable String visibilityMode) {
    this.visibilityMode = visibilityMode;
    return this;
  }

  /**
   * Get visibilityMode
   * @return visibilityMode
   */
  
  @Schema(name = "visibility_mode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("visibility_mode")
  public @Nullable String getVisibilityMode() {
    return visibilityMode;
  }

  public void setVisibilityMode(@Nullable String visibilityMode) {
    this.visibilityMode = visibilityMode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SetImplantVisibilitySettings200Response setImplantVisibilitySettings200Response = (SetImplantVisibilitySettings200Response) o;
    return Objects.equals(this.success, setImplantVisibilitySettings200Response.success) &&
        Objects.equals(this.characterId, setImplantVisibilitySettings200Response.characterId) &&
        Objects.equals(this.visibilityMode, setImplantVisibilitySettings200Response.visibilityMode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, characterId, visibilityMode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SetImplantVisibilitySettings200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    visibilityMode: ").append(toIndentedString(visibilityMode)).append("\n");
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

