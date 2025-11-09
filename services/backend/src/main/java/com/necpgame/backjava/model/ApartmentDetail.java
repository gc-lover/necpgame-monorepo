package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.Apartment;
import com.necpgame.backjava.model.ApartmentUpgradeState;
import com.necpgame.backjava.model.GuestInvite;
import com.necpgame.backjava.model.LayoutPreset;
import com.necpgame.backjava.model.StorageStatus;
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
 * ApartmentDetail
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ApartmentDetail {

  private @Nullable Apartment apartment;

  @Valid
  private List<@Valid ApartmentUpgradeState> upgrades = new ArrayList<>();

  @Valid
  private List<LayoutPreset> layouts = new ArrayList<>();

  private @Nullable StorageStatus storage;

  @Valid
  private List<@Valid GuestInvite> guests = new ArrayList<>();

  public ApartmentDetail apartment(@Nullable Apartment apartment) {
    this.apartment = apartment;
    return this;
  }

  /**
   * Get apartment
   * @return apartment
   */
  @Valid 
  @Schema(name = "apartment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("apartment")
  public @Nullable Apartment getApartment() {
    return apartment;
  }

  public void setApartment(@Nullable Apartment apartment) {
    this.apartment = apartment;
  }

  public ApartmentDetail upgrades(List<@Valid ApartmentUpgradeState> upgrades) {
    this.upgrades = upgrades;
    return this;
  }

  public ApartmentDetail addUpgradesItem(ApartmentUpgradeState upgradesItem) {
    if (this.upgrades == null) {
      this.upgrades = new ArrayList<>();
    }
    this.upgrades.add(upgradesItem);
    return this;
  }

  /**
   * Get upgrades
   * @return upgrades
   */
  @Valid 
  @Schema(name = "upgrades", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("upgrades")
  public List<@Valid ApartmentUpgradeState> getUpgrades() {
    return upgrades;
  }

  public void setUpgrades(List<@Valid ApartmentUpgradeState> upgrades) {
    this.upgrades = upgrades;
  }

  public ApartmentDetail layouts(List<LayoutPreset> layouts) {
    this.layouts = layouts;
    return this;
  }

  public ApartmentDetail addLayoutsItem(LayoutPreset layoutsItem) {
    if (this.layouts == null) {
      this.layouts = new ArrayList<>();
    }
    this.layouts.add(layoutsItem);
    return this;
  }

  /**
   * Get layouts
   * @return layouts
   */
  @Valid 
  @Schema(name = "layouts", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("layouts")
  public List<LayoutPreset> getLayouts() {
    return layouts;
  }

  public void setLayouts(List<LayoutPreset> layouts) {
    this.layouts = layouts;
  }

  public ApartmentDetail storage(@Nullable StorageStatus storage) {
    this.storage = storage;
    return this;
  }

  /**
   * Get storage
   * @return storage
   */
  @Valid 
  @Schema(name = "storage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("storage")
  public @Nullable StorageStatus getStorage() {
    return storage;
  }

  public void setStorage(@Nullable StorageStatus storage) {
    this.storage = storage;
  }

  public ApartmentDetail guests(List<@Valid GuestInvite> guests) {
    this.guests = guests;
    return this;
  }

  public ApartmentDetail addGuestsItem(GuestInvite guestsItem) {
    if (this.guests == null) {
      this.guests = new ArrayList<>();
    }
    this.guests.add(guestsItem);
    return this;
  }

  /**
   * Get guests
   * @return guests
   */
  @Valid 
  @Schema(name = "guests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guests")
  public List<@Valid GuestInvite> getGuests() {
    return guests;
  }

  public void setGuests(List<@Valid GuestInvite> guests) {
    this.guests = guests;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApartmentDetail apartmentDetail = (ApartmentDetail) o;
    return Objects.equals(this.apartment, apartmentDetail.apartment) &&
        Objects.equals(this.upgrades, apartmentDetail.upgrades) &&
        Objects.equals(this.layouts, apartmentDetail.layouts) &&
        Objects.equals(this.storage, apartmentDetail.storage) &&
        Objects.equals(this.guests, apartmentDetail.guests);
  }

  @Override
  public int hashCode() {
    return Objects.hash(apartment, upgrades, layouts, storage, guests);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApartmentDetail {\n");
    sb.append("    apartment: ").append(toIndentedString(apartment)).append("\n");
    sb.append("    upgrades: ").append(toIndentedString(upgrades)).append("\n");
    sb.append("    layouts: ").append(toIndentedString(layouts)).append("\n");
    sb.append("    storage: ").append(toIndentedString(storage)).append("\n");
    sb.append("    guests: ").append(toIndentedString(guests)).append("\n");
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

