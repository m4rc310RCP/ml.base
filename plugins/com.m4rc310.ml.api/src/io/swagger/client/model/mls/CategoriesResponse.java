
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
    "path_from_root",
    "shipping_modes",
    "name",
    "prediction_probability",
    "id"
})
public class CategoriesResponse {

    @JsonProperty("path_from_root")
    private List<PathFromRoot> pathFromRoot = null;
    @JsonProperty("shipping_modes")
    private List<String> shippingModes = null;
    @JsonProperty("name")
    private String name;
    @JsonProperty("prediction_probability")
    private Double predictionProbability;
    @JsonProperty("id")
    private String id;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("path_from_root")
    public List<PathFromRoot> getPathFromRoot() {
        return pathFromRoot;
    }

    @JsonProperty("path_from_root")
    public void setPathFromRoot(List<PathFromRoot> pathFromRoot) {
        this.pathFromRoot = pathFromRoot;
    }

    @JsonProperty("shipping_modes")
    public List<String> getShippingModes() {
        return shippingModes;
    }

    @JsonProperty("shipping_modes")
    public void setShippingModes(List<String> shippingModes) {
        this.shippingModes = shippingModes;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("prediction_probability")
    public Double getPredictionProbability() {
        return predictionProbability;
    }

    @JsonProperty("prediction_probability")
    public void setPredictionProbability(Double predictionProbability) {
        this.predictionProbability = predictionProbability;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
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
