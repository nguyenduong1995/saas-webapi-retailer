/**
 * PromotionDto.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.dto;

import co.ipicorp.saas.nrms.model.SubjectType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import grass.micro.apps.model.dto.StatusTimestampDto;

/**
 * PromotionDto. <<< Detail note.
 * 
 * @author nt.duong
 * @access public
 */
public class PromotionDto extends StatusTimestampDto implements Serializable {

    private static final long serialVersionUID = 5896717246464314379L;

    @JsonProperty("promotionCode")
    private String promotionCode;

    @JsonProperty("promotionType")
    private Integer promotionType;

    @JsonProperty("name")
    private String name;

    @JsonProperty("content")
    private String content;

    @JsonProperty("banner")
    private String banner;

    @JsonProperty("startDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate startDate;

    @JsonProperty("endDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate endDate;

    @JsonProperty("preparationDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime preparationDate;
    
    @JsonProperty("manualEndDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime manualEndDate;

    @JsonProperty("promotionState")
    private String promotionState;

    @JsonProperty("conditionFormatId")
    private Integer conditionFormatId;

    @JsonProperty("rewardFormatId")
    private Integer rewardFormatId;

    @JsonProperty("subjectType")
    private SubjectType subjectType;

    @JsonProperty("conditionComparitionType")
    private Integer conditionComparitionType;

    @JsonProperty("limitationClaimType")
    private Integer limitationClaimType;

    @JsonProperty("promotionLocationDto")
    @JsonInclude(value = Include.NON_NULL)
    private PromotionLocationDto promotionLocationDto;

    @JsonProperty("promotionGroups")
    @JsonInclude(value = Include.NON_NULL)
    private List<PromotionProductGroupDto> promotionGroups;

    @JsonProperty("promotionConditionFormat")
    @JsonInclude(value = Include.NON_NULL)
    private PromotionConditionFormatDto promotionConditionFormat;

    @JsonProperty("promotionRewardFormat")
    @JsonInclude(value = Include.NON_NULL)
    private PromotionRewardFormatDto promotionRewardFormat;

    @JsonProperty("promotionLimitation")
    @JsonInclude(value = Include.NON_NULL)
    private List<PromotionLimitationDto> promotionLimitations;
    
    @JsonProperty("promotionParticipants")
    private PromotionParticipantRetailerDto promotionParticipants;

    @JsonProperty("display")
    private Boolean display;

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public Integer getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(Integer promotionType) {
        this.promotionType = promotionType;
    }

    /**
     * get value of <b>name</b>.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set value to <b>name</b>.
     * 
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getPreparationDate() {
        return preparationDate;
    }

    public void setPreparationDate(LocalDateTime preparationDate) {
        this.preparationDate = preparationDate;
    }

    public String getPromotionState() {
        return promotionState;
    }

    public void setPromotionState(String promotionState) {
        this.promotionState = promotionState;
    }

    public Integer getConditionFormatId() {
        return conditionFormatId;
    }

    public void setConditionFormatId(Integer conditionFormatId) {
        this.conditionFormatId = conditionFormatId;
    }

    public Integer getRewardFormatId() {
        return rewardFormatId;
    }

    public void setRewardFormatId(Integer rewardFormatId) {
        this.rewardFormatId = rewardFormatId;
    }

    public SubjectType getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(SubjectType subjectType) {
        this.subjectType = subjectType;
    }

    public Integer getConditionComparitionType() {
        return conditionComparitionType;
    }

    public void setConditionComparitionType(Integer conditionComparitionType) {
        this.conditionComparitionType = conditionComparitionType;
    }

    public Integer getLimitationClaimType() {
        return limitationClaimType;
    }

    public void setLimitationClaimType(Integer limitationClaimType) {
        this.limitationClaimType = limitationClaimType;
    }

    /**
     * get value of <b>promotionLocationDto</b>.
     * 
     * @return the promotionLocationDto
     */
    public PromotionLocationDto getPromotionLocationDto() {
        return promotionLocationDto;
    }

    /**
     * Set value to <b>promotionLocationDto</b>.
     * 
     * @param promotionLocationDto
     *            the promotionLocationDto to set
     */
    public void setPromotionLocationDto(PromotionLocationDto promotionLocationDto) {
        this.promotionLocationDto = promotionLocationDto;
    }

    /**
     * get value of <b>promotionGroups</b>.
     * 
     * @return the promotionGroups
     */
    public List<PromotionProductGroupDto> getPromotionGroups() {
        return promotionGroups;
    }

    /**
     * Set value to <b>promotionGroups</b>.
     * 
     * @param promotionGroups
     *            the promotionGroups to set
     */
    public void setPromotionGroups(List<PromotionProductGroupDto> promotionGroups) {
        this.promotionGroups = promotionGroups;
    }

    /**
     * get value of <b>promotionConditionFormat</b>.
     * 
     * @return the promotionConditionFormat
     */
    public PromotionConditionFormatDto getPromotionConditionFormat() {
        return promotionConditionFormat;
    }

    /**
     * Set value to <b>promotionConditionFormat</b>.
     * 
     * @param promotionConditionFormat
     *            the promotionConditionFormat to set
     */
    public void setPromotionConditionFormat(PromotionConditionFormatDto promotionConditionFormat) {
        this.promotionConditionFormat = promotionConditionFormat;
    }

    /**
     * get value of <b>promotionRewardFormat</b>.
     * 
     * @return the promotionRewardFormat
     */
    public PromotionRewardFormatDto getPromotionRewardFormat() {
        return promotionRewardFormat;
    }

    /**
     * Set value to <b>promotionRewardFormat</b>.
     * 
     * @param promotionRewardFormat
     *            the promotionRewardFormat to set
     */
    public void setPromotionRewardFormat(PromotionRewardFormatDto promotionRewardFormat) {
        this.promotionRewardFormat = promotionRewardFormat;
    }

    /**
     * get value of <b>promotionLimitation</b>.
     * 
     * @return the promotionLimitation
     */
    public List<PromotionLimitationDto> getPromotionLimitations() {
        return promotionLimitations;
    }

    /**
     * Set value to <b>promotionLimitation</b>.
     * 
     * @param promotionLimitation
     *            the promotionLimitation to set
     */
    public void setPromotionLimitations(List<PromotionLimitationDto> promotionLimitation) {
        this.promotionLimitations = promotionLimitation;
    }

    /**
     * get value of <b>content</b>.
     * 
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Set value to <b>content</b>.
     * 
     * @param content
     *            the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * get value of <b>banner</b>.
     * 
     * @return the banner
     */
    public String getBanner() {
        return banner;
    }

    /**
     * Set value to <b>banner</b>.
     * 
     * @param banner
     *            the banner to set
     */
    public void setBanner(String banner) {
        this.banner = banner;
    }

    /**
     * get value of <b>display</b>.
     * 
     * @return the display
     */
    public Boolean getDisplay() {
        return display;
    }

    /**
     * Set value to <b>display</b>.
     * 
     * @param display
     *            the display to set
     */
    public void setDisplay(Boolean display) {
        this.display = display;
    }

    /**
     * get value of <b>promotionParticipants</b>.
     * @return the promotionParticipants
     */
    public PromotionParticipantRetailerDto getPromotionParticipants() {
        return promotionParticipants;
    }

    /**
     * Set value to <b>promotionParticipants</b>.
     * @param promotionParticipants the promotionParticipants to set
     */
    public void setPromotionParticipants(PromotionParticipantRetailerDto promotionParticipants) {
        this.promotionParticipants = promotionParticipants;
    }

    /**
     * get value of <b>manualEndDate</b>.
     * @return the manualEndDate
     */
    public LocalDateTime getManualEndDate() {
        return manualEndDate;
    }

    /**
     * Set value to <b>manualEndDate</b>.
     * @param manualEndDate the manualEndDate to set
     */
    public void setManualEndDate(LocalDateTime manualEndDate) {
        this.manualEndDate = manualEndDate;
    }

    @Override
    public String toString() {
        return "PromotionDto [promotionCode=" + promotionCode + ", promotionType=" + promotionType + ", name=" + name + ", startDate=" + startDate
                + ", endDate=" + endDate + ", preparationDate=" + preparationDate + ", promotionState=" + promotionState + ", conditionFormatId="
                + conditionFormatId + ", rewardFormatId=" + rewardFormatId + ", subjectType=" + subjectType + ", conditionComparitionType="
                + conditionComparitionType + ", limitationClaimType=" + limitationClaimType + ", promotionLocationDto=" + promotionLocationDto
                + ", promotionGroups=" + promotionGroups + ", promotionConditionFormat=" + promotionConditionFormat + ", promotionRewardFormat="
                + promotionRewardFormat + ", promotionLimitations=" + promotionLimitations + "]";
    }

}
