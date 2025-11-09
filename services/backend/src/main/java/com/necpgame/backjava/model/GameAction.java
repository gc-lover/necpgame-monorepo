package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.Objects;

/**
 * GameAction - РґРµР№СЃС‚РІРёРµ РІ РёРіСЂРµ
 */
@Schema(description = "Р”РµР№СЃС‚РІРёРµ РІ РёРіСЂРµ")
public class GameAction {

    @JsonProperty("id")
    private String id;

    @JsonProperty("label")
    private String label;

    @JsonProperty("description")
    private String description;

    @JsonProperty("enabled")
    private Boolean enabled = true;

    @Schema(description = "РЈРЅРёРєР°Р»СЊРЅС‹Р№ РёРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ РґРµР№СЃС‚РІРёСЏ", required = true)
    @NotNull
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Schema(description = "РќР°Р·РІР°РЅРёРµ РґРµР№СЃС‚РІРёСЏ РґР»СЏ РѕС‚РѕР±СЂР°Р¶РµРЅРёСЏ", required = true)
    @NotNull
    @Size(min = 1, max = 100)
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Schema(description = "РћРїРёСЃР°РЅРёРµ РґРµР№СЃС‚РІРёСЏ")
    @Size(max = 500)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Schema(description = "Р”РѕСЃС‚СѓРїРЅРѕ Р»Рё РґРµР№СЃС‚РІРёРµ РІ РґР°РЅРЅС‹Р№ РјРѕРјРµРЅС‚")
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameAction that = (GameAction) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(label, that.label) &&
               Objects.equals(description, that.description) &&
               Objects.equals(enabled, that.enabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label, description, enabled);
    }

    @Override
    public String toString() {
        return "GameAction{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}

