package co.ipicorp.saas.retailerapi.form;

import java.io.Serializable;

public class RetailerWarehouseImportTicketItemForm implements Serializable {

    private static final long serialVersionUID = -3332949582923093080L;

    private Integer productVariationId;
    private Integer amount;

    public Integer getProductVariationId() {
        return productVariationId;
    }

    public void setProductVariationId(Integer productVariationId) {
        this.productVariationId = productVariationId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "RetailerWarehouseImportTicketItemForm [productVariationId=" + productVariationId + ", amount=" + amount + "]";
    }

}
