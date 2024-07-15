/**
 * PromotionProductGroupDetailDto.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     thuy nguyen
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import grass.micro.apps.model.dto.StatusTimestampDto;

/**
 * PromotionProductGroupDetailDto.
 * <<< Detail note.
 * @author thuy nguyen
 * @access public
 */
@JsonInclude(value = Include.NON_NULL)
public class PromotionProductGroupDto extends StatusTimestampDto implements Serializable {

    private static final long serialVersionUID = 8277679643546562285L;
    
    @JsonProperty("promotionId")
    @JsonInclude(value = Include.NON_NULL)
    private Integer promotionId;
    
    @JsonProperty("groupId")
    @JsonInclude(value = Include.NON_NULL)
    private Integer groupId;
    
    @JsonProperty("groupName")
    @JsonInclude(value = Include.NON_NULL)
    private String groupName;
    
    @JsonProperty("promotionProductGroupDetails")
    @JsonInclude(value = Include.NON_NULL)
    private List<PromotionProductGroupDetailDto> promotionProductGroupDetails = new ArrayList<PromotionProductGroupDetailDto>();

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
     * get value of <b>groupId</b>.
     * @return the groupId
     */
    public Integer getGroupId() {
        return groupId;
    }

    /**
     * Set value to <b>groupId</b>.
     * @param groupId the groupId to set
     */
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    /**
     * get value of <b>groupName</b>.
     * @return the groupName
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Set value to <b>groupName</b>.
     * @param groupName the groupName to set
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * get value of <b>promotionProductGroupDetails</b>.
     * @return the promotionProductGroupDetails
     */
    public List<PromotionProductGroupDetailDto> getPromotionProductGroupDetails() {
        return promotionProductGroupDetails;
    }

    /**
     * Set value to <b>promotionProductGroupDetails</b>.
     * @param promotionProductGroupDetails the promotionProductGroupDetails to set
     */
    public void setPromotionProductGroupDetails(List<PromotionProductGroupDetailDto> promotionProductGroupDetails) {
        this.promotionProductGroupDetails = promotionProductGroupDetails;
    }

    @Override
    public String toString() {
        return "PromotionProductGroupDto [promotionId=" + promotionId + ", groupId=" + groupId + ", groupName=" + groupName + ", promotionProductGroupDetails="
                + promotionProductGroupDetails + "]";
    }

}
