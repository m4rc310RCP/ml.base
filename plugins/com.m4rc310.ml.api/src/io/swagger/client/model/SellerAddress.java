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
 * SellerAddress
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-08-06T14:21:46.668-03:00")
public class SellerAddress {
  @JsonProperty("id")
  private Integer id = null;

  @JsonProperty("comment")
  private String comment = null;

  @JsonProperty("address_line")
  private String addressLine = null;

  @JsonProperty("zip_code")
  private String zipCode = null;

  @JsonProperty("city")
  private City city = null;

  @JsonProperty("state")
  private State state = null;

  @JsonProperty("country")
  private Country country = null;

  @JsonProperty("latitude")
  private Double latitude = null;

  @JsonProperty("longitude")
  private Double longitude = null;

  @JsonProperty("search_location")
  private SearchLocation searchLocation = null;

  public SellerAddress id(Integer id) {
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @ApiModelProperty(value = "")
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public SellerAddress comment(String comment) {
    this.comment = comment;
    return this;
  }

   /**
   * Get comment
   * @return comment
  **/
  @ApiModelProperty(value = "")
  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public SellerAddress addressLine(String addressLine) {
    this.addressLine = addressLine;
    return this;
  }

   /**
   * Get addressLine
   * @return addressLine
  **/
  @ApiModelProperty(value = "")
  public String getAddressLine() {
    return addressLine;
  }

  public void setAddressLine(String addressLine) {
    this.addressLine = addressLine;
  }

  public SellerAddress zipCode(String zipCode) {
    this.zipCode = zipCode;
    return this;
  }

   /**
   * Get zipCode
   * @return zipCode
  **/
  @ApiModelProperty(value = "")
  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public SellerAddress city(City city) {
    this.city = city;
    return this;
  }

   /**
   * Get city
   * @return city
  **/
  @ApiModelProperty(value = "")
  public City getCity() {
    return city;
  }

  public void setCity(City city) {
    this.city = city;
  }

  public SellerAddress state(State state) {
    this.state = state;
    return this;
  }

   /**
   * Get state
   * @return state
  **/
  @ApiModelProperty(value = "")
  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public SellerAddress country(Country country) {
    this.country = country;
    return this;
  }

   /**
   * Get country
   * @return country
  **/
  @ApiModelProperty(value = "")
  public Country getCountry() {
    return country;
  }

  public void setCountry(Country country) {
    this.country = country;
  }

  public SellerAddress latitude(Double latitude) {
    this.latitude = latitude;
    return this;
  }

   /**
   * Get latitude
   * @return latitude
  **/
  @ApiModelProperty(value = "")
  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public SellerAddress longitude(Double longitude) {
    this.longitude = longitude;
    return this;
  }

   /**
   * Get longitude
   * @return longitude
  **/
  @ApiModelProperty(value = "")
  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public SellerAddress searchLocation(SearchLocation searchLocation) {
    this.searchLocation = searchLocation;
    return this;
  }

   /**
   * Get searchLocation
   * @return searchLocation
  **/
  @ApiModelProperty(value = "")
  public SearchLocation getSearchLocation() {
    return searchLocation;
  }

  public void setSearchLocation(SearchLocation searchLocation) {
    this.searchLocation = searchLocation;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SellerAddress sellerAddress = (SellerAddress) o;
    return Objects.equals(this.id, sellerAddress.id) &&
        Objects.equals(this.comment, sellerAddress.comment) &&
        Objects.equals(this.addressLine, sellerAddress.addressLine) &&
        Objects.equals(this.zipCode, sellerAddress.zipCode) &&
        Objects.equals(this.city, sellerAddress.city) &&
        Objects.equals(this.state, sellerAddress.state) &&
        Objects.equals(this.country, sellerAddress.country) &&
        Objects.equals(this.latitude, sellerAddress.latitude) &&
        Objects.equals(this.longitude, sellerAddress.longitude) &&
        Objects.equals(this.searchLocation, sellerAddress.searchLocation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, comment, addressLine, zipCode, city, state, country, latitude, longitude, searchLocation);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SellerAddress {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("    addressLine: ").append(toIndentedString(addressLine)).append("\n");
    sb.append("    zipCode: ").append(toIndentedString(zipCode)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    country: ").append(toIndentedString(country)).append("\n");
    sb.append("    latitude: ").append(toIndentedString(latitude)).append("\n");
    sb.append("    longitude: ").append(toIndentedString(longitude)).append("\n");
    sb.append("    searchLocation: ").append(toIndentedString(searchLocation)).append("\n");
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

