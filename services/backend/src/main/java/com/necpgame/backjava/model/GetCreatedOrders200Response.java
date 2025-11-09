package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Objects;

@JsonTypeName("getCreatedOrders_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetCreatedOrders200Response {

    @Valid
    @Schema(name = "orders")
    @JsonProperty("orders")
    private List<PlayerOrder> orders;

    public List<PlayerOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<PlayerOrder> orders) {
        this.orders = orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GetCreatedOrders200Response that = (GetCreatedOrders200Response) o;
        return Objects.equals(orders, that.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orders);
    }

    @Override
    public String toString() {
        return "GetCreatedOrders200Response{" +
            "orders=" + orders +
            '}';
    }
}


