/**
 * ProductController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.controller;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.ipicorp.saas.nrms.model.Brand;
import co.ipicorp.saas.nrms.model.ProductVariation;
import co.ipicorp.saas.nrms.model.Retailer;
import co.ipicorp.saas.nrms.model.WarehouseTotalItem;
import co.ipicorp.saas.nrms.model.dto.ProductVariationSearchCondition;
import co.ipicorp.saas.nrms.service.ProductResourceService;
import co.ipicorp.saas.nrms.service.ProductService;
import co.ipicorp.saas.nrms.service.ProductVariationService;
import co.ipicorp.saas.nrms.service.RetailerService;
import co.ipicorp.saas.nrms.service.WarehouseTotalItemService;
import co.ipicorp.saas.nrms.web.dto.ProductVariationDto;
import co.ipicorp.saas.nrms.web.util.DtoFetchingUtils;
import co.ipicorp.saas.retailerapi.form.ProductVariationSearchForm;
import co.ipicorp.saas.retailerapi.security.RetailerSessionInfo;
import co.ipicorp.saas.retailerapi.util.Constants;
import co.ipicorp.saas.retailerapi.util.ControllerAction;
import grass.micro.apps.annotation.GetBody;
import grass.micro.apps.annotation.Logged;
import grass.micro.apps.annotation.NoRequiredAuth;
import grass.micro.apps.component.SystemConfiguration;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerListingSupport;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.util.RequestUtils;

/**
 * ProductController. <<< Detail note.
 * 
 * @author hieumicro
 * @access public
 */
@SuppressWarnings("unchecked")
@RestController
public class ProductVariationController {

	public static final long APP_TOP_PRODUCT_CACHE_TIME = 5 * 60 * 60 * 1000;

	@Autowired
	private ErrorsKeyConverter errorsProcessor;

	@Autowired
	private RetailerService retailerService;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductVariationService pvService;

	@Autowired
	private ProductResourceService productResourceService;

	@Autowired
	private WarehouseTotalItemService whtiService;

	@RequestMapping(value = ControllerAction.APP_PRODUCT_VARIATION_GET_TOP_ACTION, method = RequestMethod.GET)
	@Logged
	@NoRequiredAuth
	public ResponseEntity<?> getTopProduct(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "number") final Integer number) {
		AppControllerSupport support = new AppControllerListingSupport() {
			@Override
			public String getAttributeName() {
				return "products";
			}

			@Override
			public List<? extends Serializable> getEntityList(HttpServletRequest request, HttpServletResponse response,
					Errors errors, ErrorsKeyConverter errorsProcessor) {
				return pvService.getTopProduct(10);
			}

			@Override
			public List<?> fetchEntitiesToDtos(List<? extends Serializable> entities) {
				DtoFetchingUtils.setProductVariationService(pvService);
				DtoFetchingUtils.setProductResourceService(productResourceService);
				return DtoFetchingUtils.fetchProductVariations((List<ProductVariation>) entities);
			}
		};

		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	@GetMapping(value = ControllerAction.APP_PRODUCT_VARIATION_SEARCH_ACTION)
	@Logged
	public ResponseEntity<?> searchProductVariation(HttpServletRequest request, HttpServletResponse response,
			@GetBody ProductVariationSearchForm form) {
		AppControllerSupport support = new AppControllerSupport() {
			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				ProductVariationSearchCondition condition = new ProductVariationSearchCondition();
				condition.setOffset(form.getOffset());
				condition.setSegment(form.getSegment());
				condition.setLimitSearch(true);
				condition.setKeyword(form.getKeyword());
				long total = pvService.count(condition);
				getRpcResponse().addAttribute("count", total);

				List<ProductVariationDto> dtos = new LinkedList<>();
				if (total > form.getSegment()) {
					List<ProductVariation> productVariations = ProductVariationController.this.pvService
							.search(condition);
					List<Integer> productIds = productVariations.stream().map(pv -> pv.getProductId())
							.collect(Collectors.toList());

					Map<Integer, Brand> brands = productService.getBrandsByProductIds(productIds);
					DtoFetchingUtils.setProductResourceService(productResourceService);
					dtos = DtoFetchingUtils.fetchProductVariations(productVariations);
					for (ProductVariationDto dto : dtos) {
						dto.setBrandName(brands.get(dto.getProductId()).getName());
					}
				}

				if (StringUtils.isNotBlank(form.getKeyword())) {
					RetailerSessionInfo info = (RetailerSessionInfo) RequestUtils.getInstance().getSessionInfo(request,
							Constants.APP_SESSION_INFO_KEY);
					trackingSearchKeyword(request, info.getRetailer(), form.getKeyword());
				}
				getRpcResponse().addAttribute("products", dtos);
			}

			private void trackingSearchKeyword(HttpServletRequest request, Retailer retailer, String keyword) {
				List<String> keywords = new LinkedList<>();
				retailer = retailerService.get(retailer.getId());
				List<String> tmp = (List<String>) retailer.getExtraData()
						.get(Constants.APP_RETAILER_PRODUCT_SEARCH_KEYWORDS_KEY);
				if (tmp != null) {
					keywords.addAll(tmp);
				}

				keywords.add(0, keyword);
				TreeSet<String> tree = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
				keywords.removeIf(word -> !tree.add(word));

				int maxNearestSearch = SystemConfiguration.getInstance().getInt(
						Constants.APP_RETAILER_PRODUCT_MAX_KEYWORDS_KEY, Constants.APP_RETAILER_PRODUCT_MAX_KEYWORDS);
				if (keywords.size() > maxNearestSearch) {
					keywords.remove(keywords.size() - 1);
				}

				retailer.getExtraData().put(Constants.APP_RETAILER_PRODUCT_SEARCH_KEYWORDS_KEY, keywords);
				retailerService.updatePartial(retailer);
			}
		};
		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	@RequestMapping(value = ControllerAction.APP_PRODUCT_VARIATION_COUNT_IN_WAREHOUSE_ACTION, method = RequestMethod.GET)
	@Logged
	public ResponseEntity<?> countProductInWarehouse(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "id") final Integer productVariationId) {
		AppControllerSupport support = new AppControllerSupport() {
			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				ProductVariation productVariation = pvService.get(productVariationId);
				if (productVariation == null) {
					getRpcResponse().addAttribute("countInWarehouse", 0);
					return;
				}

				WarehouseTotalItem item = whtiService.getBySellinItemInfo(productVariation.getProductId(),
						productVariationId);

				int countInWarehouse = 0;
				if (item != null) {
					countInWarehouse = item.getAmountAvailable();
				}

				getRpcResponse().addAttribute("countInWarehouse", countInWarehouse);
			}
		};
		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}
	
}
