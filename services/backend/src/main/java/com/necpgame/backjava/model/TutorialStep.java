package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.Objects;

/**
 * TutorialStep - С€Р°Рі С‚СѓС‚РѕСЂРёР°Р»Р°
 */
@Schema(description = "РЁР°Рі С‚СѓС‚РѕСЂРёР°Р»Р°")
public class TutorialStep {

    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("hint")
    private String hint;

    @Schema(description = "РЈРЅРёРєР°Р»СЊРЅС‹Р№ РёРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ С€Р°РіР°", required = true)
    @NotNull
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Schema(description = "Р—Р°РіРѕР»РѕРІРѕРє С€Р°РіР°", required = true)
    @NotNull
    @Size(min = 1, max = 100)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Schema(description = "РћРїРёСЃР°РЅРёРµ С€Р°РіР°", required = true)
    @NotNull
    @Size(min = 10, max = 500)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Schema(description = "РџРѕРґСЃРєР°Р·РєР° РґР»СЏ РёРіСЂРѕРєР°", required = true)
    @NotNull
    @Size(min = 1, max = 200)
    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TutorialStep that = (TutorialStep) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(title, that.title) &&
               Objects.equals(description, that.description) &&
               Objects.equals(hint, that.hint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, hint);
    }

    @Override
    public String toString() {
        return "TutorialStep{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", hint='" + hint + '\'' +
                '}';
    }
}

