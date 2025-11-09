package com.necpgame.systemservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.systemservice.model.MaintenanceWindow;
import com.necpgame.systemservice.model.Page;
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
 * MaintenanceWindowList
 */


public class MaintenanceWindowList {

  @Valid
  private List<@Valid MaintenanceWindow> windows = new ArrayList<>();

  private @Nullable Page page;

  public MaintenanceWindowList() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MaintenanceWindowList(List<@Valid MaintenanceWindow> windows) {
    this.windows = windows;
  }

  public MaintenanceWindowList windows(List<@Valid MaintenanceWindow> windows) {
    this.windows = windows;
    return this;
  }

  public MaintenanceWindowList addWindowsItem(MaintenanceWindow windowsItem) {
    if (this.windows == null) {
      this.windows = new ArrayList<>();
    }
    this.windows.add(windowsItem);
    return this;
  }

  /**
   * Get windows
   * @return windows
   */
  @NotNull @Valid 
  @Schema(name = "windows", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("windows")
  public List<@Valid MaintenanceWindow> getWindows() {
    return windows;
  }

  public void setWindows(List<@Valid MaintenanceWindow> windows) {
    this.windows = windows;
  }

  public MaintenanceWindowList page(@Nullable Page page) {
    this.page = page;
    return this;
  }

  /**
   * Get page
   * @return page
   */
  @Valid 
  @Schema(name = "page", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("page")
  public @Nullable Page getPage() {
    return page;
  }

  public void setPage(@Nullable Page page) {
    this.page = page;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MaintenanceWindowList maintenanceWindowList = (MaintenanceWindowList) o;
    return Objects.equals(this.windows, maintenanceWindowList.windows) &&
        Objects.equals(this.page, maintenanceWindowList.page);
  }

  @Override
  public int hashCode() {
    return Objects.hash(windows, page);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaintenanceWindowList {\n");
    sb.append("    windows: ").append(toIndentedString(windows)).append("\n");
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
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

