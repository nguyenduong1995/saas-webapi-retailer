package co.ipicorp.saas.retailerapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

import grass.micro.apps.model.dto.StatusTimestampDto;

@JsonInclude(Include.NON_NULL)
public class RetailerWarehouseImportTicketDto extends StatusTimestampDto implements Serializable {
    
    private static final long serialVersionUID = 3802891288838342037L;

    @JsonProperty("retailerId")
    private Integer retailerId;

    @JsonProperty("importTicketCode")
    private String importTicketCode;

    @JsonProperty("importType")
    private Integer importType;
    
    @JsonProperty("importPerson")
    private String importPerson;
    
    @JsonProperty("description")
    private String description;

    @JsonProperty("importDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime importDate;

    @JsonProperty("total")
    private Double total;

    public Integer getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(Integer retailerId) {
        this.retailerId = retailerId;
    }

    public String getImportTicketCode() {
        return importTicketCode;
    }

    public void setImportTicketCode(String importTicketCode) {
        this.importTicketCode = importTicketCode;
    }

    public Integer getImportType() {
        return importType;
    }

    public void setImportType(Integer importType) {
        this.importType = importType;
    }

    public String getImportPerson() {
        return importPerson;
    }

    public void setImportPerson(String importPerson) {
        this.importPerson = importPerson;
    }

    public LocalDateTime getImportDate() {
        return importDate;
    }

    public void setImportDate(LocalDateTime importDate) {
        this.importDate = importDate;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
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
        return "RetailerWarehouseImportTicketDto [retailerId=" + retailerId + ", importTicketCode=" + importTicketCode + ", importType=" + importType
                + ", importPerson=" + importPerson + ", importDate=" + importDate + ", total=" + total + "]";
    }
    
}
