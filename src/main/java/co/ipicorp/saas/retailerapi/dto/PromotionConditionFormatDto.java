package co.ipicorp.saas.retailerapi.dto;

import java.io.Serializable;

import grass.micro.apps.model.dto.StatusTimestampDto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * PromotionConditionFormatDto. <<< Detail note.
 * 
 * @author ntduong
 * @access public
 */
public class PromotionConditionFormatDto extends StatusTimestampDto implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6194329580287024443L;

	@JsonProperty(value = "name")
	private String name;

    /**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PromotionConditionFormatDto [name=" + name + "]";
	}

}
