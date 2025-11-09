package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ImplantInstallRequest
 */


public class ImplantInstallRequest {

  private String characterId;

  private String implantId;

  private @Nullable String ripperDocId;

  public ImplantInstallRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ImplantInstallRequest(String characterId, String implantId) {
    this.characterId = characterId;
    this.implantId = implantId;
  }

  public ImplantInstallRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public ImplantInstallRequest implantId(String implantId) {
    this.implantId = implantId;
    return this;
  }

  /**
   * Get implantId
   * @return implantId
   */
  @NotNull 
  @Schema(name = "implant_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("implant_id")
  public String getImplantId() {
    return implantId;
  }

  public void setImplantId(String implantId) {
    this.implantId = implantId;
  }

  public ImplantInstallRequest ripperDocId(@Nullable String ripperDocId) {
    this.ripperDocId = ripperDocId;
    return this;
  }

  /**
   * ID риппердока, устанавливающего имплант
   * @return ripperDocId
   */
  
  @Schema(name = "ripper_doc_id", description = "ID риппердока, устанавливающего имплант", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ripper_doc_id")
  public @Nullable String getRipperDocId() {
    return ripperDocId;
  }

  public void setRipperDocId(@Nullable String ripperDocId) {
    this.ripperDocId = ripperDocId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImplantInstallRequest implantInstallRequest = (ImplantInstallRequest) o;
    return Objects.equals(this.characterId, implantInstallRequest.characterId) &&
        Objects.equals(this.implantId, implantInstallRequest.implantId) &&
        Objects.equals(this.ripperDocId, implantInstallRequest.ripperDocId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, implantId, ripperDocId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImplantInstallRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    implantId: ").append(toIndentedString(implantId)).append("\n");
    sb.append("    ripperDocId: ").append(toIndentedString(ripperDocId)).append("\n");
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

