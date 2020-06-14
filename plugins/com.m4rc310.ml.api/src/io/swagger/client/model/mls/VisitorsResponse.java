
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
    "user_id",
    "date_from",
    "date_to",
    "total_visits",
    "visits_detail"
})
public class VisitorsResponse {

    @JsonProperty("user_id")
    private Integer userId;
    @JsonProperty("date_from")
    private String dateFrom;
    @JsonProperty("date_to")
    private String dateTo;
    @JsonProperty("total_visits")
    private Integer totalVisits;
    @JsonProperty("visits_detail")
    private List<VisitsDetail> visitsDetail = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("user_id")
    public Integer getUserId() {
        return userId;
    }

    @JsonProperty("user_id")
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @JsonProperty("date_from")
    public String getDateFrom() {
        return dateFrom;
    }

    @JsonProperty("date_from")
    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    @JsonProperty("date_to")
    public String getDateTo() {
        return dateTo;
    }

    @JsonProperty("date_to")
    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    @JsonProperty("total_visits")
    public Integer getTotalVisits() {
        return totalVisits;
    }

    @JsonProperty("total_visits")
    public void setTotalVisits(Integer totalVisits) {
        this.totalVisits = totalVisits;
    }

    @JsonProperty("visits_detail")
    public List<VisitsDetail> getVisitsDetail() {
        return visitsDetail;
    }

    @JsonProperty("visits_detail")
    public void setVisitsDetail(List<VisitsDetail> visitsDetail) {
        this.visitsDetail = visitsDetail;
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
