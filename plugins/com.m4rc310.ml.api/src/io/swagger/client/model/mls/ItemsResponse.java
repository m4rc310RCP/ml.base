
package io.swagger.client.model.mls;

import java.util.ArrayList;
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
    "seller_id",
    "query",
    "paging",
    "results",
    "orders",
    "filters",
    "available_orders",
    "available_filters"
})
public class ItemsResponse {

    @JsonProperty("seller_id")
    private String sellerId;
    @JsonProperty("query")
    private Object query;
    @JsonProperty("paging")
    private Paging paging;
    @JsonProperty("results")
    private List<String> results = new ArrayList<String>();
    @JsonProperty("orders")
    private List<Order> orders = new ArrayList<Order>();
    @JsonProperty("filters")
    private List<Object> filters = new ArrayList<Object>();
    
    
//    @JsonIgnore
    @JsonProperty("available_orders")
    private List<AvailableOrder> availableOrders = new ArrayList<AvailableOrder>();
//    @JsonIgnore
    @JsonProperty("available_filters")
    private List<AvailableFilter> availableFilters = new ArrayList<AvailableFilter>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("seller_id")
    public String getSellerId() {
        return sellerId;
    }

    @JsonProperty("seller_id")
    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    @JsonProperty("query")
    public Object getQuery() {
        return query;
    }

    @JsonProperty("query")
    public void setQuery(Object query) {
        this.query = query;
    }

    @JsonProperty("paging")
    public Paging getPaging() {
        return paging;
    }

    @JsonProperty("paging")
    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    @JsonProperty("results")
    public List<String> getResults() {
        return results;
    }

    @JsonProperty("results")
    public void setResults(List<String> results) {
        this.results = results;
    }

    @JsonProperty("orders")
    public List<Order> getOrders() {
        return orders;
    }

    @JsonProperty("orders")
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @JsonProperty("filters")
    public List<Object> getFilters() {
        return filters;
    }

    @JsonProperty("filters")
    public void setFilters(List<Object> filters) {
        this.filters = filters;
    }

    @JsonProperty("available_orders")
    public List<AvailableOrder> getAvailableOrders() {
        return availableOrders;
    }
    
    
    @JsonProperty("available_orders")
    public void setAvailableOrders(List<AvailableOrder> availableOrders) {
        this.availableOrders = availableOrders;
    }

    @JsonProperty("available_filters")
    public List<AvailableFilter> getAvailableFilters() {
        return availableFilters;
    }

    @JsonProperty("available_filters")
    public void setAvailableFilters(List<AvailableFilter> availableFilters) {
        this.availableFilters = availableFilters;
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
