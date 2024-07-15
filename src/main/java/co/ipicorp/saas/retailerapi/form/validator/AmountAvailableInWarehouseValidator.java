package co.ipicorp.saas.retailerapi.form.validator;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import co.ipicorp.saas.nrms.model.ProductVariation;
import co.ipicorp.saas.nrms.model.WarehouseTotalItem;
import co.ipicorp.saas.nrms.service.ProductVariationService;
import co.ipicorp.saas.nrms.service.WarehouseTotalItemService;
import co.ipicorp.saas.retailerapi.form.OrderSellinCreateForm;
import co.ipicorp.saas.retailerapi.form.SellinItemForm;
import co.ipicorp.saas.retailerapi.util.ErrorCode;
import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class AmountAvailableInWarehouseValidator extends AbstractFormValidator {

	@Override
	public boolean support(Serializable form) {
		return true;
	}

	@Autowired
	private WarehouseTotalItemService warehouseTotalItemService;

	@Autowired
	private ProductVariationService productVariationService;

	@Override
	public boolean doValidate(Serializable form, Errors errors) {
		OrderSellinCreateForm orderSellinCreateForm = (OrderSellinCreateForm) form;
		return this.validate(orderSellinCreateForm, errors);
	}

	private boolean validate(OrderSellinCreateForm orderSellinCreateForm, Errors errors) {
		if (orderSellinCreateForm.getItems() != null && orderSellinCreateForm.getItems().size() > 0) {
			System.err.println(orderSellinCreateForm);
			for (SellinItemForm item : orderSellinCreateForm.getItems()) {
				ProductVariation productVariation = productVariationService.getActivated(item.getProductVariationId());
				System.err.println(productVariation);
				if (productVariation == null) {
					errors.reject(ErrorCode.APP_1901_WAREHOUSE_TOTAL_ITEM_NOT_EXIST,
							new Object[] { "ProductVariation",
									item.getProductVariationId() + "not exist in warehouse" },
							ErrorCode.APP_1901_WAREHOUSE_TOTAL_ITEM_NOT_EXIST);

					return !errors.hasErrors();

				}

				WarehouseTotalItem warehouseTotalItem = this.warehouseTotalItemService
						.getByWarehouseImportTicketItemInfo(productVariation.getProductId(), productVariation.getId(),
								productVariation.getSku());
				if (warehouseTotalItem == null) {
					errors.reject(ErrorCode.APP_1901_WAREHOUSE_TOTAL_ITEM_NOT_EXIST,
							new Object[] { "ProductVariation",
									item.getProductVariationId() + "not exist in warehouse" },
							ErrorCode.APP_1901_WAREHOUSE_TOTAL_ITEM_NOT_EXIST);

					return !errors.hasErrors();
				}

				Integer amount = warehouseTotalItem.getAmountAvailable() - item.getAmount();
				if (amount < 0) {
					errors.reject(ErrorCode.APP_1902_WAREHOUSE_AMOUNT_AVAILABLE_NOT_ENOUGH,
							new Object[] { "AmountAvailable",
									"ProductVariation" + item.getProductVariationId()
											+ "has amount order sellin item more than amount available in Warehouse is"
											+ Math.abs(amount) },
							ErrorCode.APP_1902_WAREHOUSE_AMOUNT_AVAILABLE_NOT_ENOUGH);

					return !errors.hasErrors();
				}

			}
		}

		return !errors.hasErrors();
	}
}
