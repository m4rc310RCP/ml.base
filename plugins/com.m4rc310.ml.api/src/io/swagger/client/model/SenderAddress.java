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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * SenderAddress
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-08-06T14:21:46.668-03:00")
public class SenderAddress {
  @JsonProperty("id")
  private Integer id = null;

  @JsonProperty("address_line")
  private String addressLine = null;

  @JsonProperty("street_name")
  private String streetName = null;

  @JsonProperty("street_number")
  private String streetNumber = null;

  @JsonProperty("comment")
  private String comment = null;

  @JsonProperty("zip_code")
  private String zipCode = null;

  @JsonProperty("city")
  private City city = null;

  @JsonProperty("state")
  private State state = null;

  @JsonProperty("country")
  private Country country = null;

  @JsonProperty("neighborhood")
  private Neighborhood neighborhood = null;

  @JsonProperty("municipality")
  private Municipality municipality = null;

  @JsonProperty("agency")
  private Object agency = null;

  @JsonProperty("types")
  private List<String> types = null;

  @JsonProperty("latitude")
  private Double latitude = null;

  @JsonProperty("longitude")
  private Double longitude = null;

  @JsonProperty("geolocation_type")
  private String geolocationType = null;

  public SenderAddress id(Integer id) {
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

  public SenderAddress addressLine(String addressLine) {
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

  public SenderAddress streetName(String streetName) {
    this.streetName = streetName;
    return this;
  }

   /**
   * Get streetName
   * @return streetName
  **/
  @ApiModelProperty(value = "")
  public String getStreetName() {
    return streetName;
  }

  public void setStreetName(String streetName) {
    this.streetName = streetName;
  }

  public SenderAddress streetNumber(String streetNumber) {
    this.streetNumber = streetNumber;
    return this;
  }

   /**
   * Get streetNumber
   * @return streetNumber
  **/
  @ApiModelProperty(value = "")
  public String getStreetNumber() {
    return streetNumber;
  }

  public void setStreetNumber(String streetNumber) {
    this.streetNumber = streetNumber;
  }

  public SenderAddress comment(String comment) {
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

  public SenderAddress zipCode(String zipCode) {
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

  public SenderAddress city(City city) {
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

  public SenderAddress state(State state) {
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

  public SenderAddress country(Country country) {
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

  public SenderAddress neighborhood(Neighborhood neighborhood) {
    this.neighborhood = neighborhood;
    return this;
  }

   /**
   * Get neighborhood
   * @return neighborhood
  **/
  @ApiModelProperty(value = "")
  public Neighborhood getNeighborhood() {
    return neighborhood;
  }

  public void setNeighborhood(Neighborhood neighborhood) {
    this.neighborhood = neighborhood;
  }

  public SenderAddress municipality(Municipality municipality) {
    this.municipality = municipality;
    return this;
  }

   /**
   * Get municipality
   * @return municipality
  **/
  @ApiModelProperty(value = "")
  public Municipality getMunicipality() {
    return municipality;
  }

  public void setMunicipality(Municipality municipality) {
    this.municipality = municipality;
  }

  public SenderAddress agency(Object agency) {
    this.agency = agency;
    return this;
  }

   /**
   * Get agency
   * @return agency
  **/
  @ApiModelProperty(value = "")
  public Object getAgency() {
    return agency;
  }

  public void setAgency(Object agency) {
    this.agency = agency;
  }

  public SenderAddress types(List<String> types) {
    this.types = types;
    return this;
  }

  public SenderAddress addTypesItem(String typesItem) {
    if (this.types == null) {
      this.types = new ArrayList<String>();
    }
    this.types.add(typesItem);
    return this;
  }

   /**
   * Get types
   * @return types
  **/
  @ApiModelProperty(value = "")
  public List<String> getTypes() {
    return types;
  }

  public void setTypes(List<String> types) {
    this.types = types;
  }

  public SenderAddress latitude(Double latitude) {
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

  public SenderAddress longitude(Double longitude) {
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

  public SenderAddress geolocationType(String geolocationType) {
    this.geolocationType = geolocationType;
    return this;
  }

   /**
   * Get geolocationType
   * @return geolocationType
  **/
  @ApiModelProperty(value = "")
  public String getGeolocationType() {
    return geolocationType;
  }

  public void setGeolocationType(String geolocationType) {
    this.geolocationType = geolocationType;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SenderAddress senderAddress = (SenderAddress) o;
    return Objects.equals(this.id, senderAddress.id) &&
        Objects.equals(this.addressLine, senderAddress.addressLine) &&
        Objects.equals(this.streetName, senderAddress.streetName) &&
        Objects.equals(this.streetNumber, senderAddress.streetNumber) &&
        Objects.equals(this.comment, senderAddress.comment) &&
        Objects.equals(this.zipCode, senderAddress.zipCode) &&
        Objects.equals(this.city, senderAddress.city) &&
        Objects.equals(this.state, senderAddress.state) &&
        Objects.equals(this.country, senderAddress.country) &&
        Objects.equals(this.neighborhood, senderAddress.neighborhood) &&
        Objects.equals(this.municipality, senderAddress.municipality) &&
        Objects.equals(this.agency, senderAddress.agency) &&
        Objects.equals(this.types, senderAddress.types) &&
        Objects.equals(this.latitude, senderAddress.latitude) &&
        Objects.equals(this.longitude, senderAddress.longitude) &&
        Objects.equals(this.geolocationType, senderAddress.geolocationType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, addressLine, streetName, streetNumber, comment, zipCode, city, state, country, neighborhood, municipality, agency, types, latitude, longitude, geolocationType);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SenderAddress {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    addressLine: ").append(toIndentedString(addressLine)).append("\n");
    sb.append("    streetName: ").append(toIndentedString(streetName)).append("\n");
    sb.append("    streetNumber: ").append(toIndentedString(streetNumber)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("    zipCode: ").append(toIndentedString(zipCode)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    country: ").append(toIndentedString(country)).append("\n");
    sb.append("    neighborhood: ").append(toIndentedString(neighborhood)).append("\n");
    sb.append("    municipality: ").append(toIndentedString(municipality)).append("\n");
    sb.append("    agency: ").append(toIndentedString(agency)).append("\n");
    sb.append("    types: ").append(toIndentedString(types)).append("\n");
    sb.append("    latitude: ").append(toIndentedString(latitude)).append("\n");
    sb.append("    longitude: ").append(toIndentedString(longitude)).append("\n");
    sb.append("    geolocationType: ").append(toIndentedString(geolocationType)).append("\n");
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

