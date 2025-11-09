package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.adminservice.model.LoginScreenDataNewsInner;
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
 * LoginScreenData
 */


public class LoginScreenData {

  private @Nullable String motd;

  @Valid
  private List<@Valid LoginScreenDataNewsInner> news = new ArrayList<>();

  /**
   * Gets or Sets serverStatus
   */
  public enum ServerStatusEnum {
    ONLINE("ONLINE"),
    
    MAINTENANCE("MAINTENANCE"),
    
    OFFLINE("OFFLINE");

    private final String value;

    ServerStatusEnum(String value) {
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
    public static ServerStatusEnum fromValue(String value) {
      for (ServerStatusEnum b : ServerStatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ServerStatusEnum serverStatus;

  private @Nullable String version;

  public LoginScreenData motd(@Nullable String motd) {
    this.motd = motd;
    return this;
  }

  /**
   * Message of the day
   * @return motd
   */
  
  @Schema(name = "motd", description = "Message of the day", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("motd")
  public @Nullable String getMotd() {
    return motd;
  }

  public void setMotd(@Nullable String motd) {
    this.motd = motd;
  }

  public LoginScreenData news(List<@Valid LoginScreenDataNewsInner> news) {
    this.news = news;
    return this;
  }

  public LoginScreenData addNewsItem(LoginScreenDataNewsInner newsItem) {
    if (this.news == null) {
      this.news = new ArrayList<>();
    }
    this.news.add(newsItem);
    return this;
  }

  /**
   * Get news
   * @return news
   */
  @Valid 
  @Schema(name = "news", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("news")
  public List<@Valid LoginScreenDataNewsInner> getNews() {
    return news;
  }

  public void setNews(List<@Valid LoginScreenDataNewsInner> news) {
    this.news = news;
  }

  public LoginScreenData serverStatus(@Nullable ServerStatusEnum serverStatus) {
    this.serverStatus = serverStatus;
    return this;
  }

  /**
   * Get serverStatus
   * @return serverStatus
   */
  
  @Schema(name = "server_status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("server_status")
  public @Nullable ServerStatusEnum getServerStatus() {
    return serverStatus;
  }

  public void setServerStatus(@Nullable ServerStatusEnum serverStatus) {
    this.serverStatus = serverStatus;
  }

  public LoginScreenData version(@Nullable String version) {
    this.version = version;
    return this;
  }

  /**
   * Get version
   * @return version
   */
  
  @Schema(name = "version", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("version")
  public @Nullable String getVersion() {
    return version;
  }

  public void setVersion(@Nullable String version) {
    this.version = version;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LoginScreenData loginScreenData = (LoginScreenData) o;
    return Objects.equals(this.motd, loginScreenData.motd) &&
        Objects.equals(this.news, loginScreenData.news) &&
        Objects.equals(this.serverStatus, loginScreenData.serverStatus) &&
        Objects.equals(this.version, loginScreenData.version);
  }

  @Override
  public int hashCode() {
    return Objects.hash(motd, news, serverStatus, version);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoginScreenData {\n");
    sb.append("    motd: ").append(toIndentedString(motd)).append("\n");
    sb.append("    news: ").append(toIndentedString(news)).append("\n");
    sb.append("    serverStatus: ").append(toIndentedString(serverStatus)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
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

