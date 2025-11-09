package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.ChannelToggle;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ChannelConfig
 */


public class ChannelConfig {

  private @Nullable ChannelToggle inGameBanner;

  private @Nullable ChannelToggle modal;

  private @Nullable ChannelToggle chat;

  private @Nullable ChannelToggle email;

  private @Nullable ChannelToggle push;

  private @Nullable ChannelToggle webPortal;

  public ChannelConfig inGameBanner(@Nullable ChannelToggle inGameBanner) {
    this.inGameBanner = inGameBanner;
    return this;
  }

  /**
   * Get inGameBanner
   * @return inGameBanner
   */
  @Valid 
  @Schema(name = "inGameBanner", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("inGameBanner")
  public @Nullable ChannelToggle getInGameBanner() {
    return inGameBanner;
  }

  public void setInGameBanner(@Nullable ChannelToggle inGameBanner) {
    this.inGameBanner = inGameBanner;
  }

  public ChannelConfig modal(@Nullable ChannelToggle modal) {
    this.modal = modal;
    return this;
  }

  /**
   * Get modal
   * @return modal
   */
  @Valid 
  @Schema(name = "modal", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modal")
  public @Nullable ChannelToggle getModal() {
    return modal;
  }

  public void setModal(@Nullable ChannelToggle modal) {
    this.modal = modal;
  }

  public ChannelConfig chat(@Nullable ChannelToggle chat) {
    this.chat = chat;
    return this;
  }

  /**
   * Get chat
   * @return chat
   */
  @Valid 
  @Schema(name = "chat", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("chat")
  public @Nullable ChannelToggle getChat() {
    return chat;
  }

  public void setChat(@Nullable ChannelToggle chat) {
    this.chat = chat;
  }

  public ChannelConfig email(@Nullable ChannelToggle email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
   */
  @Valid 
  @Schema(name = "email", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("email")
  public @Nullable ChannelToggle getEmail() {
    return email;
  }

  public void setEmail(@Nullable ChannelToggle email) {
    this.email = email;
  }

  public ChannelConfig push(@Nullable ChannelToggle push) {
    this.push = push;
    return this;
  }

  /**
   * Get push
   * @return push
   */
  @Valid 
  @Schema(name = "push", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("push")
  public @Nullable ChannelToggle getPush() {
    return push;
  }

  public void setPush(@Nullable ChannelToggle push) {
    this.push = push;
  }

  public ChannelConfig webPortal(@Nullable ChannelToggle webPortal) {
    this.webPortal = webPortal;
    return this;
  }

  /**
   * Get webPortal
   * @return webPortal
   */
  @Valid 
  @Schema(name = "webPortal", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("webPortal")
  public @Nullable ChannelToggle getWebPortal() {
    return webPortal;
  }

  public void setWebPortal(@Nullable ChannelToggle webPortal) {
    this.webPortal = webPortal;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChannelConfig channelConfig = (ChannelConfig) o;
    return Objects.equals(this.inGameBanner, channelConfig.inGameBanner) &&
        Objects.equals(this.modal, channelConfig.modal) &&
        Objects.equals(this.chat, channelConfig.chat) &&
        Objects.equals(this.email, channelConfig.email) &&
        Objects.equals(this.push, channelConfig.push) &&
        Objects.equals(this.webPortal, channelConfig.webPortal);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inGameBanner, modal, chat, email, push, webPortal);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChannelConfig {\n");
    sb.append("    inGameBanner: ").append(toIndentedString(inGameBanner)).append("\n");
    sb.append("    modal: ").append(toIndentedString(modal)).append("\n");
    sb.append("    chat: ").append(toIndentedString(chat)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    push: ").append(toIndentedString(push)).append("\n");
    sb.append("    webPortal: ").append(toIndentedString(webPortal)).append("\n");
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

