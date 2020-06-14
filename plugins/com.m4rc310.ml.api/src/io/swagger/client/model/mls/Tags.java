
package io.swagger.client.model.mls;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "catalog_required",
    "allow_variations",
    "defines_picture",
    "hidden",
    "multivalued",
    "variation_attribute",
    "used_hidden",
    "validate",
    "read_only"
})
public class Tags {

    @JsonProperty("catalog_required")
    private Boolean catalogRequired;
    @JsonProperty("allow_variations")
    private Boolean allowVariations;
    @JsonProperty("defines_picture")
    private Boolean definesPicture;
    @JsonProperty("hidden")
    private Boolean hidden;
    @JsonProperty("multivalued")
    private Boolean multivalued;
    @JsonProperty("variation_attribute")
    private Boolean variationAttribute;
    @JsonProperty("used_hidden")
    private Boolean usedHidden;
    @JsonProperty("validate")
    private Boolean validate;
    @JsonProperty("read_only")
    private Boolean readOnly;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("catalog_required")
    public Boolean getCatalogRequired() {
        return catalogRequired;
    }

    @JsonProperty("catalog_required")
    public void setCatalogRequired(Boolean catalogRequired) {
        this.catalogRequired = catalogRequired;
    }

    @JsonProperty("allow_variations")
    public Boolean getAllowVariations() {
        return allowVariations;
    }

    @JsonProperty("allow_variations")
    public void setAllowVariations(Boolean allowVariations) {
        this.allowVariations = allowVariations;
    }

    @JsonProperty("defines_picture")
    public Boolean getDefinesPicture() {
        return definesPicture;
    }

    @JsonProperty("defines_picture")
    public void setDefinesPicture(Boolean definesPicture) {
        this.definesPicture = definesPicture;
    }

    @JsonProperty("hidden")
    public Boolean getHidden() {
        return hidden;
    }

    @JsonProperty("hidden")
    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    @JsonProperty("multivalued")
    public Boolean getMultivalued() {
        return multivalued;
    }

    @JsonProperty("multivalued")
    public void setMultivalued(Boolean multivalued) {
        this.multivalued = multivalued;
    }

    @JsonProperty("variation_attribute")
    public Boolean getVariationAttribute() {
        return variationAttribute;
    }

    @JsonProperty("variation_attribute")
    public void setVariationAttribute(Boolean variationAttribute) {
        this.variationAttribute = variationAttribute;
    }

    @JsonProperty("used_hidden")
    public Boolean getUsedHidden() {
        return usedHidden;
    }

    @JsonProperty("used_hidden")
    public void setUsedHidden(Boolean usedHidden) {
        this.usedHidden = usedHidden;
    }

    @JsonProperty("validate")
    public Boolean getValidate() {
        return validate;
    }

    @JsonProperty("validate")
    public void setValidate(Boolean validate) {
        this.validate = validate;
    }

    @JsonProperty("read_only")
    public Boolean getReadOnly() {
        return readOnly;
    }

    @JsonProperty("read_only")
    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
