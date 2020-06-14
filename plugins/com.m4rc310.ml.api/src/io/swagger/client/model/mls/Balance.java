
package io.swagger.client.model.mls;

import java.math.BigDecimal;
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
    "last_modified",
    "user_id",
    "total_amount",
    "pending_to_review",
    "available_balance",
    "unavailable_balance",
    "currency_id",
    "tags",
    "unavailable_balance_by_reason",
    "available_balance_by_transaction_type"
})
public class Balance {

    @JsonProperty("last_modified")
    private String lastModified;
    @JsonProperty("user_id")
    private Integer userId;
    @JsonProperty("total_amount")
    private BigDecimal totalAmount;
    @JsonProperty("pending_to_review")
    private BigDecimal pendingToReview;
    @JsonProperty("available_balance")
    private BigDecimal availableBalance;
    @JsonProperty("unavailable_balance")
    private BigDecimal unavailableBalance;
    @JsonProperty("currency_id")
    private String currencyId;
    @JsonProperty("tags")
    private List<Object> tags = null;
    @JsonProperty("unavailable_balance_by_reason")
    private List<UnavailableBalanceByReason> unavailableBalanceByReason = null;
    @JsonProperty("available_balance_by_transaction_type")
    private List<AvailableBalanceByTransactionType> availableBalanceByTransactionType = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("last_modified")
    public String getLastModified() {
        return lastModified;
    }

    @JsonProperty("last_modified")
    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    @JsonProperty("user_id")
    public Integer getUserId() {
        return userId;
    }

    @JsonProperty("user_id")
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @JsonProperty("total_amount")
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    @JsonProperty("total_amount")
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @JsonProperty("pending_to_review")
    public BigDecimal getPendingToReview() {
        return pendingToReview;
    }

    @JsonProperty("pending_to_review")
    public void setPendingToReview(BigDecimal pendingToReview) {
        this.pendingToReview = pendingToReview;
    }

    @JsonProperty("available_balance")
    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    @JsonProperty("available_balance")
    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    @JsonProperty("unavailable_balance")
    public BigDecimal getUnavailableBalance() {
        return unavailableBalance;
    }

    @JsonProperty("unavailable_balance")
    public void setUnavailableBalance(BigDecimal unavailableBalance) {
        this.unavailableBalance = unavailableBalance;
    }

    @JsonProperty("currency_id")
    public String getCurrencyId() {
        return currencyId;
    }

    @JsonProperty("currency_id")
    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    @JsonProperty("tags")
    public List<Object> getTags() {
        return tags;
    }

    @JsonProperty("tags")
    public void setTags(List<Object> tags) {
        this.tags = tags;
    }

    @JsonProperty("unavailable_balance_by_reason")
    public List<UnavailableBalanceByReason> getUnavailableBalanceByReason() {
        return unavailableBalanceByReason;
    }

    @JsonProperty("unavailable_balance_by_reason")
    public void setUnavailableBalanceByReason(List<UnavailableBalanceByReason> unavailableBalanceByReason) {
        this.unavailableBalanceByReason = unavailableBalanceByReason;
    }

    @JsonProperty("available_balance_by_transaction_type")
    public List<AvailableBalanceByTransactionType> getAvailableBalanceByTransactionType() {
        return availableBalanceByTransactionType;
    }

    @JsonProperty("available_balance_by_transaction_type")
    public void setAvailableBalanceByTransactionType(List<AvailableBalanceByTransactionType> availableBalanceByTransactionType) {
        this.availableBalanceByTransactionType = availableBalanceByTransactionType;
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
