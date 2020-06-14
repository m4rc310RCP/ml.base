
package io.swagger.client.model.mls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "name",
    "tags",
    "hierarchy",
    "relevance",
    "value_type",
    "value_max_length",
    "attribute_group_id",
    "attribute_group_name",
    "values",
    "allowed_units",
    "default_unit",
    "type",
    "tooltip",
    "hint"
})
public class AttributesForCategoryResponse {

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("tags")
    private Tags tags;
    @JsonProperty("hierarchy")
    private String hierarchy;
    @JsonProperty("relevance")
    private Integer relevance;
    @JsonProperty("value_type")
    private String valueType;
    @JsonProperty("value_max_length")
    private Integer valueMaxLength;
    @JsonProperty("attribute_group_id")
    private String attributeGroupId;
    @JsonProperty("attribute_group_name")
    private String attributeGroupName;
    @JsonProperty("values")
    private List<Value> values = null;
    @JsonProperty("allowed_units")
    private List<AllowedUnit> allowedUnits = null;
    @JsonProperty("default_unit")
    private String defaultUnit;
    @JsonProperty("type")
    private String type;
    @JsonProperty("tooltip")
    private String tooltip;
    @JsonProperty("hint")
    private String hint;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("tags")
    public Tags getTags() {
        return tags;
    }

    @JsonProperty("tags")
    public void setTags(Tags tags) {
        this.tags = tags;
    }

    @JsonProperty("hierarchy")
    public String getHierarchy() {
        return hierarchy;
    }

    @JsonProperty("hierarchy")
    public void setHierarchy(String hierarchy) {
        this.hierarchy = hierarchy;
    }

    @JsonProperty("relevance")
    public Integer getRelevance() {
        return relevance;
    }

    @JsonProperty("relevance")
    public void setRelevance(Integer relevance) {
        this.relevance = relevance;
    }

    @JsonProperty("value_type")
    public String getValueType() {
        return valueType;
    }

    @JsonProperty("value_type")
    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    @JsonProperty("value_max_length")
    public Integer getValueMaxLength() {
        return valueMaxLength;
    }

    @JsonProperty("value_max_length")
    public void setValueMaxLength(Integer valueMaxLength) {
        this.valueMaxLength = valueMaxLength;
    }

    @JsonProperty("attribute_group_id")
    public String getAttributeGroupId() {
        return attributeGroupId;
    }

    @JsonProperty("attribute_group_id")
    public void setAttributeGroupId(String attributeGroupId) {
        this.attributeGroupId = attributeGroupId;
    }

    @JsonProperty("attribute_group_name")
    public String getAttributeGroupName() {
        return attributeGroupName;
    }

    @JsonProperty("attribute_group_name")
    public void setAttributeGroupName(String attributeGroupName) {
        this.attributeGroupName = attributeGroupName;
    }

    @JsonProperty("values")
    public List<Value> getValues() {
        return values;
    }

    @JsonProperty("values")
    public void setValues(List<Value> values) {
        this.values = values;
    }

    @JsonProperty("allowed_units")
    public List<AllowedUnit> getAllowedUnits() {
        return allowedUnits;
    }

    @JsonProperty("allowed_units")
    public void setAllowedUnits(List<AllowedUnit> allowedUnits) {
        this.allowedUnits = allowedUnits;
    }

    @JsonProperty("default_unit")
    public String getDefaultUnit() {
        return defaultUnit;
    }

    @JsonProperty("default_unit")
    public void setDefaultUnit(String defaultUnit) {
        this.defaultUnit = defaultUnit;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("tooltip")
    public String getTooltip() {
        return tooltip;
    }

    @JsonProperty("tooltip")
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    @JsonProperty("hint")
    public String getHint() {
        return hint;
    }

    @JsonProperty("hint")
    public void setHint(String hint) {
        this.hint = hint;
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
