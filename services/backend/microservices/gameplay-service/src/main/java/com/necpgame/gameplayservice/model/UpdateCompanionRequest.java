package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.UpdateCompanionRequestCosmetics;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * UpdateCompanionRequest
 */


public class UpdateCompanionRequest {

  private @Nullable String nickname;

  /**
   * Gets or Sets aiMode
   */
  public enum AiModeEnum {
    ASSIST("assist"),
    
    DEFEND("defend"),
    
    SENTRY("sentry"),
    
    AUTONOMOUS("autonomous");

    private final String value;

    AiModeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static AiModeEnum fromValue(String value) {
      for (AiModeEnum b : AiModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable AiModeEnum aiMode;

  private @Nullable UpdateCompanionRequestCosmetics cosmetics;

  private @Nullable String voicePackId;

  public UpdateCompanionRequest nickname(@Nullable String nickname) {
    this.nickname = nickname;
    return this;
  }

  /**
   * Get nickname
   * @return nickname
   */
  @Size(max = 32) 
  @Schema(name = "nickname", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nickname")
  public @Nullable String getNickname() {
    return nickname;
  }

  public void setNickname(@Nullable String nickname) {
    this.nickname = nickname;
  }

  public UpdateCompanionRequest aiMode(@Nullable AiModeEnum aiMode) {
    this.aiMode = aiMode;
    return this;
  }

  /**
   * Get aiMode
   * @return aiMode
   */
  
  @Schema(name = "aiMode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("aiMode")
  public @Nullable AiModeEnum getAiMode() {
    return aiMode;
  }

  public void setAiMode(@Nullable AiModeEnum aiMode) {
    this.aiMode = aiMode;
  }

  public UpdateCompanionRequest cosmetics(@Nullable UpdateCompanionRequestCosmetics cosmetics) {
    this.cosmetics = cosmetics;
    return this;
  }

  /**
   * Get cosmetics
   * @return cosmetics
   */
  @Valid 
  @Schema(name = "cosmetics", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cosmetics")
  public @Nullable UpdateCompanionRequestCosmetics getCosmetics() {
    return cosmetics;
  }

  public void setCosmetics(@Nullable UpdateCompanionRequestCosmetics cosmetics) {
    this.cosmetics = cosmetics;
  }

  public UpdateCompanionRequest voicePackId(@Nullable String voicePackId) {
    this.voicePackId = voicePackId;
    return this;
  }

  /**
   * Get voicePackId
   * @return voicePackId
   */
  
  @Schema(name = "voicePackId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("voicePackId")
  public @Nullable String getVoicePackId() {
    return voicePackId;
  }

  public void setVoicePackId(@Nullable String voicePackId) {
    this.voicePackId = voicePackId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateCompanionRequest updateCompanionRequest = (UpdateCompanionRequest) o;
    return Objects.equals(this.nickname, updateCompanionRequest.nickname) &&
        Objects.equals(this.aiMode, updateCompanionRequest.aiMode) &&
        Objects.equals(this.cosmetics, updateCompanionRequest.cosmetics) &&
        Objects.equals(this.voicePackId, updateCompanionRequest.voicePackId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nickname, aiMode, cosmetics, voicePackId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateCompanionRequest {\n");
    sb.append("    nickname: ").append(toIndentedString(nickname)).append("\n");
    sb.append("    aiMode: ").append(toIndentedString(aiMode)).append("\n");
    sb.append("    cosmetics: ").append(toIndentedString(cosmetics)).append("\n");
    sb.append("    voicePackId: ").append(toIndentedString(voicePackId)).append("\n");
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

