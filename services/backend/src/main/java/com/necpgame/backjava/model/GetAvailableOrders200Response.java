package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@JsonTypeName("getAvailableOrders_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetAvailableOrders200Response {

    @Valid
    @NotNull
    @Schema(name = "data", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("data")
    private List<PlayerOrder> data;

    @Valid
    @NotNull
    @Schema(name = "meta", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("meta")
    private PaginationMeta meta;

    public List<PlayerOrder> getData() {
        return data;
    }

    public void setData(List<PlayerOrder> data) {
        this.data = data;
    }

    public PaginationMeta getMeta() {
        return meta;
    }

    public void setMeta(PaginationMeta meta) {
        this.meta = meta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GetAvailableOrders200Response that = (GetAvailableOrders200Response) o;
        return Objects.equals(data, that.data)
            && Objects.equals(meta, that.meta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, meta);
    }

    @Override
    public String toString() {
        return "GetAvailableOrders200Response{" +
            "data=" + data +
            ", meta=" + meta +
            '}';
    }
}


