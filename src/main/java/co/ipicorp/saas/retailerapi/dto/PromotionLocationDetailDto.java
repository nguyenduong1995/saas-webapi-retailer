/**
 * PromotionLocationDetailDto.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     thuy nguyen
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import grass.micro.apps.model.dto.StatusTimestampDto;

/**
 * PromotionLocationDetailDto.
 * <<< Detail note.
 * @author thuy nguyen
 * @access public
 */
public class PromotionLocationDetailDto extends StatusTimestampDto  implements Serializable {
    private static final long serialVersionUID = -611371582324218618L;

    @JsonProperty("name")
    private String name;
    
    @JsonProperty("promotionId")
    private Integer promotionId;
    
    @JsonProperty("locationType")
    private Integer locationType;
    
    @JsonProperty("cityId")
    private Integer cityId;
    
    @JsonProperty("districtId")
    private Integer districtId;
    
    @JsonProperty("wardId")
    private Integer wardId;
    
    @JsonProperty("regionId")
    private Integer regionId;

    

    /**
     * get value of <b>name</b>.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set value to <b>name</b>.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get value of <b>promotionId</b>.
     * @return the promotionId
     */
    public Integer getPromotionId() {
        return promotionId;
    }

    /**
     * Set value to <b>promotionId</b>.
     * @param promotionId the promotionId to set
     */
    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
    }

    /**
     * get value of <b>locationType</b>.
     * @return the locationType
     */
    public Integer getLocationType() {
        return locationType;
    }

    /**
     * Set value to <b>locationType</b>.
     * @param locationType the locationType to set
     */
    public void setLocationType(Integer locationType) {
        this.locationType = locationType;
    }

    /**
     * get value of <b>cityId</b>.
     * @return the cityId
     */
    public Integer getCityId() {
        return cityId;
    }

    /**
     * Set value to <b>cityId</b>.
     * @param cityId the cityId to set
     */
    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    /**
     * get value of <b>districtId</b>.
     * @return the districtId
     */
    public Integer getDistrictId() {
        return districtId;
    }

    /**
     * Set value to <b>districtId</b>.
     * @param districtId the districtId to set
     */
    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    /**
     * get value of <b>wardId</b>.
     * @return the wardId
     */
    public Integer getWardId() {
        return wardId;
    }

    /**
     * Set value to <b>wardId</b>.
     * @param wardId the wardId to set
     */
    public void setWardId(Integer wardId) {
        this.wardId = wardId;
    }

    /**
     * get value of <b>regionId</b>.
     * @return the regionId
     */
    public Integer getRegionId() {
        return regionId;
    }

    /**
     * Set value to <b>regionId</b>.
     * @param regionId the regionId to set
     */
    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    @Override
    public String toString() {
        return "PromotionLocationDetailDto [name=" + name + ", promotionId=" + promotionId + ", locationType=" + locationType + ", cityId=" + cityId
                + ", districtId=" + districtId + ", wardId=" + wardId + ", regionId=" + regionId + "]";
    }

}