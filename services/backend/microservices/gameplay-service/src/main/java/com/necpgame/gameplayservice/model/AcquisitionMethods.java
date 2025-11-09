package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.AcquisitionMethodsMethodsInner;
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
 * AcquisitionMethods
 */


public class AcquisitionMethods {

  private @Nullable String implantId;

  @Valid
  private List<@Valid AcquisitionMethodsMethodsInner> methods = new ArrayList<>();

  public AcquisitionMethods implantId(@Nullable String implantId) {
    this.implantId = implantId;
    return this;
  }

  /**
   * Get implantId
   * @return implantId
   */
  
  @Schema(name = "implant_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("implant_id")
  public @Nullable String getImplantId() {
    return implantId;
  }

  public void setImplantId(@Nullable String implantId) {
    this.implantId = implantId;
  }

  public AcquisitionMethods methods(List<@Valid AcquisitionMethodsMethodsInner> methods) {
    this.methods = methods;
    return this;
  }

  public AcquisitionMethods addMethodsItem(AcquisitionMethodsMethodsInner methodsItem) {
    if (this.methods == null) {
      this.methods = new ArrayList<>();
    }
    this.methods.add(methodsItem);
    return this;
  }

  /**
   * Get methods
   * @return methods
   */
  @Valid 
  @Schema(name = "methods", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("methods")
  public List<@Valid AcquisitionMethodsMethodsInner> getMethods() {
    return methods;
  }

  public void setMethods(List<@Valid AcquisitionMethodsMethodsInner> methods) {
    this.methods = methods;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AcquisitionMethods acquisitionMethods = (AcquisitionMethods) o;
    return Objects.equals(this.implantId, acquisitionMethods.implantId) &&
        Objects.equals(this.methods, acquisitionMethods.methods);
  }

  @Override
  public int hashCode() {
    return Objects.hash(implantId, methods);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AcquisitionMethods {\n");
    sb.append("    implantId: ").append(toIndentedString(implantId)).append("\n");
    sb.append("    methods: ").append(toIndentedString(methods)).append("\n");
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

