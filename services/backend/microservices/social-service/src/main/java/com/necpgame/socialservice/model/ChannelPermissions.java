package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.RolePermission;
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
 * ChannelPermissions
 */


public class ChannelPermissions {

  @Valid
  private List<@Valid RolePermission> canRead = new ArrayList<>();

  @Valid
  private List<@Valid RolePermission> canWrite = new ArrayList<>();

  @Valid
  private List<@Valid RolePermission> canModerate = new ArrayList<>();

  public ChannelPermissions canRead(List<@Valid RolePermission> canRead) {
    this.canRead = canRead;
    return this;
  }

  public ChannelPermissions addCanReadItem(RolePermission canReadItem) {
    if (this.canRead == null) {
      this.canRead = new ArrayList<>();
    }
    this.canRead.add(canReadItem);
    return this;
  }

  /**
   * Get canRead
   * @return canRead
   */
  @Valid 
  @Schema(name = "canRead", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("canRead")
  public List<@Valid RolePermission> getCanRead() {
    return canRead;
  }

  public void setCanRead(List<@Valid RolePermission> canRead) {
    this.canRead = canRead;
  }

  public ChannelPermissions canWrite(List<@Valid RolePermission> canWrite) {
    this.canWrite = canWrite;
    return this;
  }

  public ChannelPermissions addCanWriteItem(RolePermission canWriteItem) {
    if (this.canWrite == null) {
      this.canWrite = new ArrayList<>();
    }
    this.canWrite.add(canWriteItem);
    return this;
  }

  /**
   * Get canWrite
   * @return canWrite
   */
  @Valid 
  @Schema(name = "canWrite", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("canWrite")
  public List<@Valid RolePermission> getCanWrite() {
    return canWrite;
  }

  public void setCanWrite(List<@Valid RolePermission> canWrite) {
    this.canWrite = canWrite;
  }

  public ChannelPermissions canModerate(List<@Valid RolePermission> canModerate) {
    this.canModerate = canModerate;
    return this;
  }

  public ChannelPermissions addCanModerateItem(RolePermission canModerateItem) {
    if (this.canModerate == null) {
      this.canModerate = new ArrayList<>();
    }
    this.canModerate.add(canModerateItem);
    return this;
  }

  /**
   * Get canModerate
   * @return canModerate
   */
  @Valid 
  @Schema(name = "canModerate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("canModerate")
  public List<@Valid RolePermission> getCanModerate() {
    return canModerate;
  }

  public void setCanModerate(List<@Valid RolePermission> canModerate) {
    this.canModerate = canModerate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChannelPermissions channelPermissions = (ChannelPermissions) o;
    return Objects.equals(this.canRead, channelPermissions.canRead) &&
        Objects.equals(this.canWrite, channelPermissions.canWrite) &&
        Objects.equals(this.canModerate, channelPermissions.canModerate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(canRead, canWrite, canModerate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChannelPermissions {\n");
    sb.append("    canRead: ").append(toIndentedString(canRead)).append("\n");
    sb.append("    canWrite: ").append(toIndentedString(canWrite)).append("\n");
    sb.append("    canModerate: ").append(toIndentedString(canModerate)).append("\n");
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

