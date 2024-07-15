package co.ipicorp.saas.retailerapi.form;

import grass.micro.apps.web.form.validator.LimittedForm;

public class ProductVariationSearchForm extends LimittedForm {

    private static final long serialVersionUID = 7982771981104194859L;
    
    private Integer categoryIdLv0;

    private Integer categoryIdLv1;

    private Integer categoryIdLv2;
    
    private String keyword;
    
    private Integer status;
    
    private Integer brandId;

    public Integer getCategoryIdLv0() {
        return categoryIdLv0;
    }

    public void setCategoryIdLv0(Integer categoryIdLv0) {
        this.categoryIdLv0 = categoryIdLv0;
    }

    public Integer getCategoryIdLv1() {
        return categoryIdLv1;
    }

    public void setCategoryIdLv1(Integer categoryIdLv1) {
        this.categoryIdLv1 = categoryIdLv1;
    }

    public Integer getCategoryIdLv2() {
        return categoryIdLv2;
    }

    public void setCategoryIdLv2(Integer categoryIdLv2) {
        this.categoryIdLv2 = categoryIdLv2;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    /**
     * get value of <b>keyword</b>.
     * @return the keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Set value to <b>keyword</b>.
     * @param keyword the keyword to set
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "ProductVariationSearchForm [categoryIdLv0=" + categoryIdLv0 + ", categoryIdLv1=" + categoryIdLv1 + ", categoryIdLv2=" + categoryIdLv2
                + ", keyword=" + keyword + ", status=" + status + ", brandId=" + brandId + "]";
    }
    
}
