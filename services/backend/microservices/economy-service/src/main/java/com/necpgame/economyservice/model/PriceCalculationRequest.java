package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PriceCalculationRequest
 */


public class PriceCalculationRequest {

  private UUID itemId;

  /**
   * Gets or Sets quality
   */
  public enum QualityEnum {
    POOR("POOR"),
    
    COMMON("COMMON"),
    
    UNCOMMON("UNCOMMON"),
    
    RARE("RARE"),
    
    EPIC("EPIC"),
    
    LEGENDARY("LEGENDARY");

    private final String value;

    QualityEnum(String value) {
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
    public static QualityEnum fromValue(String value) {
      for (QualityEnum b : QualityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable QualityEnum quality;

  private @Nullable Float durabilityPercentage;

  private @Nullable String region;

  private @Nullable String vendorFaction;

  @Valid
  private Map<String, Integer> characterReputation = new HashMap<>();

  /**
   * Gets or Sets transactionType
   */
  public enum TransactionTypeEnum {
    BUY("BUY"),
    
    SELL("SELL");

    private final String value;

    TransactionTypeEnum(String value) {
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
    public static TransactionTypeEnum fromValue(String value) {
      for (TransactionTypeEnum b : TransactionTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TransactionTypeEnum transactionType;

  private Integer quantity = 1;

  public PriceCalculationRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PriceCalculationRequest(UUID itemId) {
    this.itemId = itemId;
  }

  public PriceCalculationRequest itemId(UUID itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  @NotNull @Valid 
  @Schema(name = "item_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("item_id")
  public UUID getItemId() {
    return itemId;
  }

  public void setItemId(UUID itemId) {
    this.itemId = itemId;
  }

  public PriceCalculationRequest quality(@Nullable QualityEnum quality) {
    this.quality = quality;
    return this;
  }

  /**
   * Get quality
   * @return quality
   */
  
  @Schema(name = "quality", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quality")
  public @Nullable QualityEnum getQuality() {
    return quality;
  }

  public void setQuality(@Nullable QualityEnum quality) {
    this.quality = quality;
  }

  public PriceCalculationRequest durabilityPercentage(@Nullable Float durabilityPercentage) {
    this.durabilityPercentage = durabilityPercentage;
    return this;
  }

  /**
   * Get durabilityPercentage
   * minimum: 0
   * maximum: 100
   * @return durabilityPercentage
   */
  @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "durability_percentage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("durability_percentage")
  public @Nullable Float getDurabilityPercentage() {
    return durabilityPercentage;
  }

  public void setDurabilityPercentage(@Nullable Float durabilityPercentage) {
    this.durabilityPercentage = durabilityPercentage;
  }

  public PriceCalculationRequest region(@Nullable String region) {
    this.region = region;
    return this;
  }

  /**
   * Get region
   * @return region
   */
  
  @Schema(name = "region", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("region")
  public @Nullable String getRegion() {
    return region;
  }

  public void setRegion(@Nullable String region) {
    this.region = region;
  }

  public PriceCalculationRequest vendorFaction(@Nullable String vendorFaction) {
    this.vendorFaction = vendorFaction;
    return this;
  }

  /**
   * Get vendorFaction
   * @return vendorFaction
   */
  
  @Schema(name = "vendor_faction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("vendor_faction")
  public @Nullable String getVendorFaction() {
    return vendorFaction;
  }

  public void setVendorFaction(@Nullable String vendorFaction) {
    this.vendorFaction = vendorFaction;
  }

  public PriceCalculationRequest characterReputation(Map<String, Integer> characterReputation) {
    this.characterReputation = characterReputation;
    return this;
  }

  public PriceCalculationRequest putCharacterReputationItem(String key, Integer characterReputationItem) {
    if (this.characterReputation == null) {
      this.characterReputation = new HashMap<>();
    }
    this.characterReputation.put(key, characterReputationItem);
    return this;
  }

  /**
   * Репутация с фракцией vendor
   * @return characterReputation
   */
  
  @Schema(name = "character_reputation", description = "Репутация с фракцией vendor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_reputation")
  public Map<String, Integer> getCharacterReputation() {
    return characterReputation;
  }

  public void setCharacterReputation(Map<String, Integer> characterReputation) {
    this.characterReputation = characterReputation;
  }

  public PriceCalculationRequest transactionType(@Nullable TransactionTypeEnum transactionType) {
    this.transactionType = transactionType;
    return this;
  }

  /**
   * Get transactionType
   * @return transactionType
   */
  
  @Schema(name = "transaction_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("transaction_type")
  public @Nullable TransactionTypeEnum getTransactionType() {
    return transactionType;
  }

  public void setTransactionType(@Nullable TransactionTypeEnum transactionType) {
    this.transactionType = transactionType;
  }

  public PriceCalculationRequest quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * @return quantity
   */
  
  @Schema(name = "quantity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quantity")
  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PriceCalculationRequest priceCalculationRequest = (PriceCalculationRequest) o;
    return Objects.equals(this.itemId, priceCalculationRequest.itemId) &&
        Objects.equals(this.quality, priceCalculationRequest.quality) &&
        Objects.equals(this.durabilityPercentage, priceCalculationRequest.durabilityPercentage) &&
        Objects.equals(this.region, priceCalculationRequest.region) &&
        Objects.equals(this.vendorFaction, priceCalculationRequest.vendorFaction) &&
        Objects.equals(this.characterReputation, priceCalculationRequest.characterReputation) &&
        Objects.equals(this.transactionType, priceCalculationRequest.transactionType) &&
        Objects.equals(this.quantity, priceCalculationRequest.quantity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, quality, durabilityPercentage, region, vendorFaction, characterReputation, transactionType, quantity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PriceCalculationRequest {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    quality: ").append(toIndentedString(quality)).append("\n");
    sb.append("    durabilityPercentage: ").append(toIndentedString(durabilityPercentage)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    vendorFaction: ").append(toIndentedString(vendorFaction)).append("\n");
    sb.append("    characterReputation: ").append(toIndentedString(characterReputation)).append("\n");
    sb.append("    transactionType: ").append(toIndentedString(transactionType)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
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

