package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.Subchannel;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SubchannelMutationRequestOperationsInner
 */

@JsonTypeName("SubchannelMutationRequest_operations_inner")

public class SubchannelMutationRequestOperationsInner {

  /**
   * Gets or Sets action
   */
  public enum ActionEnum {
    CREATE("create"),
    
    UPDATE("update"),
    
    DELETE("delete");

    private final String value;

    ActionEnum(String value) {
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
    public static ActionEnum fromValue(String value) {
      for (ActionEnum b : ActionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ActionEnum action;

  private Subchannel subchannel;

  public SubchannelMutationRequestOperationsInner() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SubchannelMutationRequestOperationsInner(ActionEnum action, Subchannel subchannel) {
    this.action = action;
    this.subchannel = subchannel;
  }

  public SubchannelMutationRequestOperationsInner action(ActionEnum action) {
    this.action = action;
    return this;
  }

  /**
   * Get action
   * @return action
   */
  @NotNull 
  @Schema(name = "action", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("action")
  public ActionEnum getAction() {
    return action;
  }

  public void setAction(ActionEnum action) {
    this.action = action;
  }

  public SubchannelMutationRequestOperationsInner subchannel(Subchannel subchannel) {
    this.subchannel = subchannel;
    return this;
  }

  /**
   * Get subchannel
   * @return subchannel
   */
  @NotNull @Valid 
  @Schema(name = "subchannel", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("subchannel")
  public Subchannel getSubchannel() {
    return subchannel;
  }

  public void setSubchannel(Subchannel subchannel) {
    this.subchannel = subchannel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SubchannelMutationRequestOperationsInner subchannelMutationRequestOperationsInner = (SubchannelMutationRequestOperationsInner) o;
    return Objects.equals(this.action, subchannelMutationRequestOperationsInner.action) &&
        Objects.equals(this.subchannel, subchannelMutationRequestOperationsInner.subchannel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(action, subchannel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SubchannelMutationRequestOperationsInner {\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    subchannel: ").append(toIndentedString(subchannel)).append("\n");
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

