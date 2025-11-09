package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.GuildPermission;
import com.necpgame.backjava.model.GuildRankLimits;
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
 * GuildRank
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GuildRank {

  private String rankId;

  private String name;

  private Integer priority;

  @Valid
  private List<@Valid GuildPermission> permissions = new ArrayList<>();

  private @Nullable GuildRankLimits limits;

  public GuildRank() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GuildRank(String rankId, String name, Integer priority) {
    this.rankId = rankId;
    this.name = name;
    this.priority = priority;
  }

  public GuildRank rankId(String rankId) {
    this.rankId = rankId;
    return this;
  }

  /**
   * Get rankId
   * @return rankId
   */
  @NotNull 
  @Schema(name = "rankId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("rankId")
  public String getRankId() {
    return rankId;
  }

  public void setRankId(String rankId) {
    this.rankId = rankId;
  }

  public GuildRank name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public GuildRank priority(Integer priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Get priority
   * @return priority
   */
  @NotNull 
  @Schema(name = "priority", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("priority")
  public Integer getPriority() {
    return priority;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  public GuildRank permissions(List<@Valid GuildPermission> permissions) {
    this.permissions = permissions;
    return this;
  }

  public GuildRank addPermissionsItem(GuildPermission permissionsItem) {
    if (this.permissions == null) {
      this.permissions = new ArrayList<>();
    }
    this.permissions.add(permissionsItem);
    return this;
  }

  /**
   * Get permissions
   * @return permissions
   */
  @Valid 
  @Schema(name = "permissions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("permissions")
  public List<@Valid GuildPermission> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<@Valid GuildPermission> permissions) {
    this.permissions = permissions;
  }

  public GuildRank limits(@Nullable GuildRankLimits limits) {
    this.limits = limits;
    return this;
  }

  /**
   * Get limits
   * @return limits
   */
  @Valid 
  @Schema(name = "limits", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("limits")
  public @Nullable GuildRankLimits getLimits() {
    return limits;
  }

  public void setLimits(@Nullable GuildRankLimits limits) {
    this.limits = limits;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuildRank guildRank = (GuildRank) o;
    return Objects.equals(this.rankId, guildRank.rankId) &&
        Objects.equals(this.name, guildRank.name) &&
        Objects.equals(this.priority, guildRank.priority) &&
        Objects.equals(this.permissions, guildRank.permissions) &&
        Objects.equals(this.limits, guildRank.limits);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rankId, name, priority, permissions, limits);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildRank {\n");
    sb.append("    rankId: ").append(toIndentedString(rankId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    permissions: ").append(toIndentedString(permissions)).append("\n");
    sb.append("    limits: ").append(toIndentedString(limits)).append("\n");
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

