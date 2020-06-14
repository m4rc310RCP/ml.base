/*
 * MercadoLibre API
 * MercadoLibre API Documentation.
 *
 * OpenAPI spec version: 0.1
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package io.swagger.client.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * Credit
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-08-06T14:21:46.668-03:00")
public class Credit {
  @JsonProperty("consumed")
  private Integer consumed = null;

  @JsonProperty("credit_level_id")
  private String creditLevelId = null;

  public Credit consumed(Integer consumed) {
    this.consumed = consumed;
    return this;
  }

   /**
   * Get consumed
   * @return consumed
  **/
  @ApiModelProperty(value = "")
  public Integer getConsumed() {
    return consumed;
  }

  public void setConsumed(Integer consumed) {
    this.consumed = consumed;
  }

  public Credit creditLevelId(String creditLevelId) {
    this.creditLevelId = creditLevelId;
    return this;
  }

   /**
   * Get creditLevelId
   * @return creditLevelId
  **/
  @ApiModelProperty(value = "")
  public String getCreditLevelId() {
    return creditLevelId;
  }

  public void setCreditLevelId(String creditLevelId) {
    this.creditLevelId = creditLevelId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Credit credit = (Credit) o;
    return Objects.equals(this.consumed, credit.consumed) &&
        Objects.equals(this.creditLevelId, credit.creditLevelId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(consumed, creditLevelId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Credit {\n");
    
    sb.append("    consumed: ").append(toIndentedString(consumed)).append("\n");
    sb.append("    creditLevelId: ").append(toIndentedString(creditLevelId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

