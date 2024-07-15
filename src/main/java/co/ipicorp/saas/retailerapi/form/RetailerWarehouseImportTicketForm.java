package co.ipicorp.saas.retailerapi.form;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class RetailerWarehouseImportTicketForm implements Serializable {
    
    private static final long serialVersionUID = 1124217655700937100L;
    
    @JsonProperty("importType")
    private Integer importType = 0;// FROM COMPANY
    
    @JsonProperty("extraData")
    private LinkedHashMap<String, Object> extraData = new LinkedHashMap<>();
    
    @JsonProperty("retailerWarehouseImportTicketItems")
    private List<RetailerWarehouseImportTicketItemForm> retailerWarehouseImportTicketItems = new ArrayList<RetailerWarehouseImportTicketItemForm>();
    
    @JsonProperty("description")
    private String description;
    
    public Integer getImportType() {
        return importType;
    }

    public void setImportType(Integer importType) {
        this.importType = importType;
    }

    public List<RetailerWarehouseImportTicketItemForm> getRetailerWarehouseImportTicketItems() {
        return retailerWarehouseImportTicketItems;
    }

    public void setRetailerWarehouseImportTicketItems(List<RetailerWarehouseImportTicketItemForm> retailerWarehouseImportTicketItems) {
        this.retailerWarehouseImportTicketItems = retailerWarehouseImportTicketItems;
    }

    /**
     * get value of <b>extraData</b>.
     * @return the extraData
     */
    public LinkedHashMap<String, Object> getExtraData() {
        return extraData;
    }

    /**
     * Set value to <b>extraData</b>.
     * @param extraData the extraData to set
     */
    public void setExtraData(LinkedHashMap<String, Object> extraData) {
        this.extraData = extraData;
    }

    /**
     * get value of <b>description</b>.
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set value to <b>description</b>.
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "RetailerWarehouseImportTicketForm [importType=" + importType + ", extraData=" + extraData + ", retailerWarehouseImportTicketItems="
                + retailerWarehouseImportTicketItems + "]";
    }
}
