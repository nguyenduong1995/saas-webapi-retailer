/**
 * OrderSellinController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.controller;

import co.ipicorp.saas.core.service.CityService;
import co.ipicorp.saas.core.service.RegionService;
import co.ipicorp.saas.core.web.components.FileStorageService;
import co.ipicorp.saas.nrms.model.OrderSellin;
import co.ipicorp.saas.nrms.model.OrderSellinItem;
import co.ipicorp.saas.nrms.model.OrderSellinPromotion;
import co.ipicorp.saas.nrms.model.OrderSellinPromotionLimitation;
import co.ipicorp.saas.nrms.model.OrderSellinPromotionLimitationDetailItem;
import co.ipicorp.saas.nrms.model.OrderSellinPromotionLimitationDetailReward;
import co.ipicorp.saas.nrms.model.OrderSellinStatus;
import co.ipicorp.saas.nrms.model.ProductResource;
import co.ipicorp.saas.nrms.model.ProductResourceType;
import co.ipicorp.saas.nrms.model.ProductVariation;
import co.ipicorp.saas.nrms.model.Promotion;
import co.ipicorp.saas.nrms.model.Retailer;
import co.ipicorp.saas.nrms.model.RetailerInvoiceInfo;
import co.ipicorp.saas.nrms.model.dto.OrderSellinSearchCondition;
import co.ipicorp.saas.nrms.service.OrderSellinItemService;
import co.ipicorp.saas.nrms.service.OrderSellinPromotionLimitationDetailItemService;
import co.ipicorp.saas.nrms.service.OrderSellinPromotionLimitationDetailRewardService;
import co.ipicorp.saas.nrms.service.OrderSellinPromotionLimitationService;
import co.ipicorp.saas.nrms.service.OrderSellinPromotionService;
import co.ipicorp.saas.nrms.service.OrderSellinService;
import co.ipicorp.saas.nrms.service.ProductCategoryService;
import co.ipicorp.saas.nrms.service.ProductResourceService;
import co.ipicorp.saas.nrms.service.ProductService;
import co.ipicorp.saas.nrms.service.ProductVariationService;
import co.ipicorp.saas.nrms.service.PromotionConditionFormatService;
import co.ipicorp.saas.nrms.service.PromotionLimitationItemRewardProductService;
import co.ipicorp.saas.nrms.service.PromotionLimitationItemService;
import co.ipicorp.saas.nrms.service.PromotionLimitationService;
import co.ipicorp.saas.nrms.service.PromotionLocationService;
import co.ipicorp.saas.nrms.service.PromotionParticipantRetailerService;
import co.ipicorp.saas.nrms.service.PromotionProductGroupDetailService;
import co.ipicorp.saas.nrms.service.PromotionProductGroupService;
import co.ipicorp.saas.nrms.service.PromotionRewardFormatService;
import co.ipicorp.saas.nrms.service.PromotionService;
import co.ipicorp.saas.nrms.service.PromotionStateChangeHistoryService;
import co.ipicorp.saas.nrms.service.RetailerInvoiceInfoService;
import co.ipicorp.saas.nrms.service.RetailerService;
import co.ipicorp.saas.nrms.service.UnitService;
import co.ipicorp.saas.nrms.web.dto.OrderSellinDto;
import co.ipicorp.saas.nrms.web.util.DtoFetchingUtils;
import co.ipicorp.saas.nrms.web.util.ResourceUrlResolver;
import co.ipicorp.saas.retailerapi.dto.ProductVariationRewardDto;
import co.ipicorp.saas.retailerapi.form.OrderSellinCreateForm;
import co.ipicorp.saas.retailerapi.form.OrderSellinSearchForm;
import co.ipicorp.saas.retailerapi.form.SellinItemForm;
import co.ipicorp.saas.retailerapi.form.validator.AmountAvailableInWarehouseValidator;
import co.ipicorp.saas.retailerapi.security.RetailerSessionInfo;
import co.ipicorp.saas.retailerapi.util.Constants;
import co.ipicorp.saas.retailerapi.util.ControllerAction;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grass.micro.apps.annotation.GetBody;
import grass.micro.apps.annotation.Logged;
import grass.micro.apps.annotation.Validation;
import grass.micro.apps.model.base.Status;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerCreationSupport;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.dto.RpcResponse;
import grass.micro.apps.web.util.RequestUtils;

/**
 * OrderSellinController. Controller use for Retailer make order to company. <<< Detail note.
 * 
 * @author hieumicro
 * @access public
 */
@RestController
@SuppressWarnings("unused")
public class OrderSellinController extends AbstractPromotionController {
    private Logger logger = Logger.getLogger(OrderSellinController.class);

    private static final String ORDER_SELLIN_CODE_PREFIX = "SI";

    @Autowired
    private ErrorsKeyConverter errorsProcessor;

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private RegionService regionService;

    @Autowired
    private CityService cityService;

    @Autowired
    private PromotionLocationService promotionLocationService;

    @Autowired
    private PromotionParticipantRetailerService pprService;

    @Autowired
    private PromotionLimitationService promotionLimitationService;

    @Autowired
    private PromotionLimitationItemService pliService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private PromotionRewardFormatService promotionRewardFormatService;

    @Autowired
    private PromotionConditionFormatService promotionConditionFormatService;

    @Autowired
    private PromotionProductGroupDetailService promotionProductGroupDetailService;

    @Autowired
    private PromotionProductGroupService promotionProductGroupService;

    @Autowired
    private PromotionLimitationItemRewardProductService promotionLimitationItemRewardProductService;

    @Autowired
    private PromotionStateChangeHistoryService promotionStateChangeHistoryService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private OrderSellinService orderSellinService;

    @Autowired
    private OrderSellinItemService orderSellinItemService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductVariationService productVariationService;

    @Autowired
    private UnitService unitService;

    @Autowired
    private RetailerService retailerService;

    @Autowired
    private ProductResourceService productResourceService;

    @Autowired
    private RetailerInvoiceInfoService retailerInvoiceInfoService;

    @Autowired
    private OrderSellinPromotionService osiPromotionService;;

    @Autowired
    private OrderSellinPromotionLimitationService osiPromotionLimitationService;

    @Autowired
    private OrderSellinPromotionLimitationDetailItemService osiPromotionLimitationDetailItemService;

    @Autowired
    private OrderSellinPromotionLimitationDetailRewardService osiPromotionLimitationDetailRewardService;

    @GetMapping(value = ControllerAction.APP_ORDER_SELLIN_DETAIL_REWARD_ACTION)
    @Logged
    public ResponseEntity<?> getOrderRewards(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") Integer orderId) {

        AppControllerSupport support = new AppControllerSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                RetailerSessionInfo info = (RetailerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
                int retailerId = info.getRetailer().getId();
                getOrderRewards(request, retailerId, orderId, errors, getRpcResponse());
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }

    
    protected void getOrderRewards(HttpServletRequest request, int retailerId, Integer orderId, Errors errors, RpcResponse rpcResponse) {
        List<OrderSellinPromotionLimitationDetailReward> rewards = osiPromotionLimitationDetailRewardService.getByOrderId(orderId);
        
        List<Map<String, Object>> result = new LinkedList<Map<String,Object>>();
        if (CollectionUtils.isNotEmpty(rewards)) {
            List<Integer> pvIds = rewards.stream().map(p -> p.getRewardProductVariationId()).collect(Collectors.toList());
            Map<Integer, String> urls = this.getProductResourceService().getResourceUrlOfProductVariations(pvIds, ProductResourceType.IMAGE);
            
            for (OrderSellinPromotionLimitationDetailReward reward : rewards) {
                result.add(fetchOrderSellinPromotionLimitationDetailReward(reward, urls));
            } 
        }
        
        rpcResponse.addAttribute("rewards", result);
    }


    private Map<String, Object> fetchOrderSellinPromotionLimitationDetailReward(OrderSellinPromotionLimitationDetailReward reward
            , Map<Integer, String> urls) {
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("productId", reward.getRewardProductId());
        result.put("productVariationId", reward.getRewardProductVariationId());
        result.put("productVariationName", reward.getRewardProductVariationName());
        result.put("amount", reward.getRewardAmount());
        result.put("unitAmount", reward.getUnitAmount());
        result.put("packingAmount", reward.getPackingAmount());
        result.put("packingExchangeRatio", reward.getPackingExchangeRatio());
        result.put("unitId", reward.getUnitId());
        result.put("packingId", reward.getPackingId());
        result.put("price", reward.getUnitPrice());
        result.put("packingPrice", reward.getPackingPrice());
        double itemValue = reward.getUnitPrice() * reward.getUnitAmount() + reward.getPackingPrice() * reward.getPackingAmount();
        result.put("itemValue", itemValue);
        result.put("image", ResourceUrlResolver.getInstance().resolveProductUrl(1, urls.get(reward.getRewardProductVariationId())));
        
        return result;
    }


    @GetMapping(value = ControllerAction.APP_ORDER_SELLIN_ACTION_SEARCH)
    @Logged
    public ResponseEntity<?> searchOrderSellin(HttpServletRequest request, HttpServletResponse response, @GetBody OrderSellinSearchForm form) {

        AppControllerSupport support = new AppControllerSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                RetailerSessionInfo info = (RetailerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
                int retailerId = info.getRetailer().getId();
                doSearch(request, retailerId, form, errors, getRpcResponse());
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }

    private void doSearch(HttpServletRequest request, Integer retailerId, OrderSellinSearchForm form, Errors errors, RpcResponse rpcResponse) {
        LocalDate toDate = LocalDate.now();
        LocalDate fromDate = LocalDate.now().minusDays(15);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (form.getFromDate() != null && form.getToDate() != null) {
            try {
                fromDate = LocalDate.parse(form.getFromDate(), formatter);
                toDate = LocalDate.parse(form.getToDate(), formatter);
            } catch (Exception ex) {
                // do nothing
            }
        }

        OrderSellinSearchCondition condition = new OrderSellinSearchCondition();
        condition.setRetailerId(retailerId);
        condition.setEnableCreatedDate(true);
        condition.setFromDate(fromDate);
        condition.setToDate(toDate);
        condition.setLimitSearch(true);
        condition.setSegment(form.getSegment());
        condition.setOffset(form.getOffset());
        condition.setOrderCodeEnabled(StringUtils.isNotBlank(form.getOrderCode()));
        condition.setOrderCode(form.getOrderCode());

        long count = orderSellinService.count(condition, condition.getRetailerId(), null);
        rpcResponse.addAttribute("count", count);

        if (count > form.getSegment()) {
            List<OrderSellin> orderSellins = orderSellinService.searchOrderSellin(condition, condition.getRetailerId(), null);
            this.initFetchService();
            
            List<OrderSellinDto> orderSellinDtos = DtoFetchingUtils.fetchOrderSellins(orderSellins);
            rpcResponse.addAttribute("orders", orderSellinDtos);
        }
    }


    /**
     * 
     */
    private void initFetchService() {
        DtoFetchingUtils.setOrderSellinItemService(orderSellinItemService);
        DtoFetchingUtils.setProductService(productService);
        DtoFetchingUtils.setProductResourceService(productResourceService);
        DtoFetchingUtils.setProductVariationService(productVariationService);
        DtoFetchingUtils.setUnitService(unitService);
        
        DtoFetchingUtils.setPromotionService(promotionService);
        DtoFetchingUtils.setOrderSellinPromotionService(osiPromotionService);
        DtoFetchingUtils.setOrderSellinPromotionRewardService(osiPromotionLimitationDetailRewardService);
    }

    @PostMapping(value = ControllerAction.APP_ORDER_SELLIN_ACTION)
    @Validation(validators = { AmountAvailableInWarehouseValidator.class })
    @Logged
    public ResponseEntity<?> createOrderSellin(HttpServletRequest request, HttpServletResponse response, @RequestBody OrderSellinCreateForm form,
            BindingResult errors) {

        AppControllerSupport support = new AppControllerCreationSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                RetailerSessionInfo info = (RetailerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
                int retailerId = info.getRetailer().getId();
                form.setRetailerId(retailerId);

                Map<Integer, Integer> productsInCartMap = form.getItems().stream()
                        .collect(Collectors.toMap(SellinItemForm::getProductVariationId, SellinItemForm::getAmount));

                List<Promotion> promotions = getPromotionList(request, false);

                List<Integer> productVariationIds = productsInCartMap.keySet().stream().collect(Collectors.toList());
                List<ProductVariationRewardDto> rewards = new LinkedList<ProductVariationRewardDto>();

                getPromotionSuggestionWithProductVariations(promotions, productsInCartMap, productVariationIds, rewards);
                getPromotionSuggestionsOnBill(promotions, productsInCartMap, productVariationIds, rewards);
                
                double totalDiscount = rewards.stream().map(r -> r.getDiscount()).collect(Collectors.summingDouble(Double::new));
                OrderSellin order = OrderSellinController.this.doCreateOrderSellin(form, totalDiscount, getRpcResponse(), (BindingResult) errors);

                List<Map<String, Object>> items = rewards.stream().flatMap(reward -> reward.getItems().stream()).collect(Collectors.toList());
                doLinkOrderSellinPromotionRewards(order, rewards);
                
                initFetchService();
                getRpcResponse().addAttribute("orderSellin", DtoFetchingUtils.fetchOrderSellin(order));
            }
        };

        return support.doSupport(request, null, errors, errorsProcessor);
    }

    private void doLinkOrderSellinPromotionRewards(OrderSellin order, List<ProductVariationRewardDto> rewards) {
        List<ProductVariationRewardDto> promotionRewardList = getPromotionRewardList(rewards);
        for (ProductVariationRewardDto promotionReward : promotionRewardList) {
            OrderSellinPromotion osi = this.createOrderSellinPromotion(order, promotionReward);
            List<ProductVariationRewardDto> promotionGroupRewardList = getPromotionGroupRewardList(promotionReward.getPromotionId(), rewards);
            this.createOrderSellinPromotionLimitation(osi, order, promotionGroupRewardList);
        }
    }

    private List<ProductVariationRewardDto> getPromotionRewardList(List<ProductVariationRewardDto> rewards) {
        Map<Integer, ProductVariationRewardDto> result = new LinkedHashMap<>();

        for (ProductVariationRewardDto reward : rewards) {
            ProductVariationRewardDto dto = result.get(reward.getPromotionId());
            
            if (dto == null) {
                dto = new ProductVariationRewardDto();
                dto.setPromotion(reward.getPromotion());
                result.put(reward.getPromotionId(), dto);
            }

            this.summingReward(rewards, reward, dto);
            int amountInCart = dto.getItemsInGroup().stream().map(item -> (Integer) item.get("amount")).reduce(0, (a, b) -> a + b);
            dto.setAmountInCart(amountInCart);
            logger.info("-------------------------------------- \n" + dto);
        }

        return new ArrayList<>(result.values());
    }

    /**
     * @param rewards
     * @param source
     * @param target
     */
    protected void summingReward(List<ProductVariationRewardDto> rewards, ProductVariationRewardDto source, ProductVariationRewardDto target) {
        target.setDiscount(target.getDiscount() + source.getDiscount());
        if (source.getItems() != null && !source.getItems().isEmpty()) {
            List<Map<String, Object>> items = new ArrayList<>();
            items.addAll(target.getItems());
            items.addAll(source.getItems());
            List<Map<String, Object>> filteredItems = this.filterItems(items);
            target.setItems(filteredItems);
        }

        if (source.getItemsInGroup() != null && !source.getItemsInGroup().isEmpty()) {
            List<Map<String, Object>> items = new ArrayList<>();
            items.addAll(target.getItemsInGroup());
            items.addAll(source.getItemsInGroup());
            List<Map<String, Object>> filteredItems = this.filterItems(items);
            target.setItemsInGroup(filteredItems);
        }
    }

    private List<ProductVariationRewardDto> getPromotionGroupRewardList(int promotionId, List<ProductVariationRewardDto> rewards) {
        Map<Integer, ProductVariationRewardDto> result = new LinkedHashMap<>();

        for (ProductVariationRewardDto reward : rewards) {
            if (reward.getPromotionId() != promotionId) {
                continue;
            }

            ProductVariationRewardDto dto = result.get(reward.getGroupId());
            if (dto == null) {
                dto = new ProductVariationRewardDto();
                dto.setPromotion(reward.getPromotion());
                dto.setGroupId(reward.getGroupId());
                dto.setPromotionLimitationItem(reward.getPromotionLimitationItem());
                result.put(reward.getGroupId(), dto);
            }

            this.summingReward(rewards, reward, dto);
        }

        return new ArrayList<>(result.values());
    }

    private OrderSellinPromotion createOrderSellinPromotion(OrderSellin order, ProductVariationRewardDto reward) {
        OrderSellinPromotion osiPromotion = new OrderSellinPromotion();
        osiPromotion.setOrderId(order.getId());
        osiPromotion.setOrderCode(order.getOrderCode());
        osiPromotion.setPromotionId(reward.getPromotionId());
        osiPromotion.setRetailerId(order.getRetailerId());
        osiPromotion.setStatus(Status.ACTIVE);

        osiPromotion.setOrderCost(order.getOrderCost());
        osiPromotion.setDiscount(reward.getDiscount());
        
        int productOnPromotionAmount = 0;
        double productOnPromotionCost = 0.0;
        if (CollectionUtils.isNotEmpty(reward.getItemsInGroup())) {
            productOnPromotionAmount = reward.getItemsInGroup().stream().map(item -> (Integer) item.get("amount")).reduce(0, (a, b) -> a + b);
            productOnPromotionCost = reward.getItemsInGroup().stream().map(item -> (Double) item.get("itemValue")).reduce(0.0, (a, b) -> a + b);
        }

        osiPromotion.setProductOnPromotionAmount(productOnPromotionAmount);
        osiPromotion.setProductOnPromotionCost(productOnPromotionCost);

        int rewardAmount = 0;
        double rewardValue = 0.0;
        if (CollectionUtils.isNotEmpty(reward.getItems())) {
            rewardAmount = reward.getItems().stream().map(item -> (Integer) item.get("amount")).reduce(0, (a, b) -> a + b);
            rewardValue = reward.getItems().stream().map(item -> (Double) item.get("itemValue")).reduce(0.0, (a, b) -> a + b);
        }

        osiPromotion.setRewardAmount(rewardAmount);
        osiPromotion.setRewardValue(rewardValue);

        // (double) reward.getItems().stream().map(item -> (Integer) item.get("amount")).reduce(0, (a, b) -> a + b)
        osiPromotion = osiPromotionService.create(osiPromotion);
        return osiPromotion;
    }

    private void createOrderSellinPromotionLimitation(OrderSellinPromotion osi, OrderSellin order, List<ProductVariationRewardDto> promotionGroupRewardList) {
        for (ProductVariationRewardDto reward : promotionGroupRewardList) {
            OrderSellinPromotionLimitation osilPromotion = new OrderSellinPromotionLimitation();
            osilPromotion.setOrderId(order.getId());
            osilPromotion.setOrderCode(order.getOrderCode());
            osilPromotion.setPromotionId(osi.getPromotionId());
            osilPromotion.setRetailerId(order.getRetailerId());
            osilPromotion.setLimitationId(reward.getPromotionLimitationItem().getLimitationId());
            osilPromotion.setLimitationItemId(reward.getPromotionLimitationItem().getId());
            osilPromotion.setProductGroupId(reward.getPromotionLimitationItem().getProductGroupId());
            osilPromotion.setStatus(Status.ACTIVE);

            osilPromotion.setDiscount(reward.getDiscount());
            int productOnPromotionAmount = 0;
            double productOnPromotionCost = 0.0;
            if (CollectionUtils.isNotEmpty(reward.getItemsInGroup())) {
                productOnPromotionAmount = reward.getItemsInGroup().stream().map(item -> (Integer) item.get("amount")).reduce(0, (a, b) -> a + b);
                productOnPromotionCost = reward.getItemsInGroup().stream().map(item -> (Double) item.get("itemValue")).reduce(0.0, (a, b) -> a + b);
            }

            osilPromotion.setProductOnPromotionAmount(productOnPromotionAmount);
            osilPromotion.setProductOnPromotionCost(productOnPromotionCost);

            int rewardAmount = 0;
            double rewardValue = 0.0;
            if (CollectionUtils.isNotEmpty(reward.getItems())) {
                rewardAmount = reward.getItems().stream().map(item -> (Integer) item.get("amount")).reduce(0, (a, b) -> a + b);
                rewardValue = reward.getItems().stream().map(item -> (Double) item.get("itemValue")).reduce(0.0, (a, b) -> a + b);
            }

            osilPromotion.setRewardAmount(rewardAmount);
            osilPromotion.setRewardValue(rewardValue);

            // (double) reward.getItems().stream().map(item -> (Integer) item.get("amount")).reduce(0, (a, b) -> a + b)
            osilPromotion = osiPromotionLimitationService.create(osilPromotion);
            this.createOrderSellinPromotionLimitationDetailItems(osilPromotion, order, reward);
            this.createOrderSellinPromotionLimitationDetailRewards(osilPromotion, order, reward);
        }
    }

    private void createOrderSellinPromotionLimitationDetailItems(OrderSellinPromotionLimitation osilPromotion, OrderSellin order,
            ProductVariationRewardDto reward) {
        if (CollectionUtils.isNotEmpty(reward.getItemsInGroup())) {
            
            double groupDiscount = osilPromotion.getDiscount();
            double groupCost = osilPromotion.getProductOnPromotionCost();
            boolean isLast = false;
            double currentDiscount = 0;
            int index = 0;
            for (Map<String, Object> itemInGroup : reward.getItemsInGroup()) {
                isLast = (++index >= reward.getItemsInGroup().size());
                
                OrderSellinPromotionLimitationDetailItem detail = new OrderSellinPromotionLimitationDetailItem();
                detail.setOrderSellinPromotionLimitationId(osilPromotion.getId());
                detail.setOrderId(osilPromotion.getOrderId());
                detail.setOrderCode(osilPromotion.getOrderCode());
                detail.setPromotionId(osilPromotion.getPromotionId());
                detail.setRetailerId(osilPromotion.getRetailerId());
                detail.setLimitationId(osilPromotion.getLimitationId());
                if (groupDiscount == 0) {
                    detail.setDiscount(0.0);
                } else {
                    double itemCost = (Double) itemInGroup.get("itemValue");
                    double discount = 0;
                    if (isLast) {
                        discount = groupDiscount - currentDiscount;
                    } else {
                        int percent = (int) Math.round(100 * (itemCost / groupCost));
                        discount = (int) (percent * groupDiscount / 100);
                        currentDiscount += discount;
                    }
                    
                    detail.setDiscount(discount);
                }
                
                detail.setLimitationItemId(osilPromotion.getLimitationItemId());
                detail.setProductGroupId(osilPromotion.getProductGroupId());
                detail.setStatus(Status.ACTIVE);
                
                detail.setProductId((Integer) itemInGroup.get("productId"));
                detail.setProductVariationId((Integer) itemInGroup.get("productVariationId"));
                detail.setProductVariationName((String) itemInGroup.get("productVariationName"));
                detail.setSku((String) itemInGroup.get("sku"));               
                detail.setUnitId((Integer) itemInGroup.get("unitId"));
                detail.setUnitAmount((Integer) itemInGroup.get("unitAmount"));
                detail.setPackingId((Integer) itemInGroup.get("packingId"));
                detail.setPackingAmount((Integer) itemInGroup.get("packingAmount"));
                detail.setPackingExchangeRatio((Integer) itemInGroup.get("packingExchangeRatio"));
                detail.setUnitPrice((Double) itemInGroup.get("price"));
                detail.setPackingPrice((Double) itemInGroup.get("packingPrice"));
                
                detail.setProductOnPromotionAmount(((Integer) itemInGroup.get("amount")));
                detail.setProductOnPromotionCost(((Double) itemInGroup.get("itemValue")));
                
                detail = this.osiPromotionLimitationDetailItemService.create(detail);
            }
        }
    }

    private void createOrderSellinPromotionLimitationDetailRewards(OrderSellinPromotionLimitation osilPromotion, OrderSellin order,
            ProductVariationRewardDto reward) {

        if (CollectionUtils.isNotEmpty(reward.getItems())) {
            for (Map<String, Object> rewardItem : reward.getItems()) {
                OrderSellinPromotionLimitationDetailReward detail = new OrderSellinPromotionLimitationDetailReward();
                detail.setOrderSellinPromotionLimitationId(osilPromotion.getId());
                detail.setOrderId(osilPromotion.getOrderId());
                detail.setOrderCode(osilPromotion.getOrderCode());
                detail.setPromotionId(osilPromotion.getPromotionId());
                detail.setRetailerId(osilPromotion.getRetailerId());
                detail.setLimitationId(osilPromotion.getLimitationId());
                detail.setLimitationItemId(osilPromotion.getLimitationItemId());
                detail.setProductGroupId(osilPromotion.getProductGroupId());
                detail.setStatus(Status.ACTIVE);
                
                detail.setRewardProductId((Integer) rewardItem.get("productId"));
                detail.setRewardProductVariationId((Integer) rewardItem.get("productVariationId"));
                detail.setRewardProductVariationName((String) rewardItem.get("productVariationName"));
                detail.setRewardAmount(((Integer) rewardItem.get("amount")));
                detail.setRewardValue(((Double) rewardItem.get("itemValue")));
                
                detail.setUnitId((Integer) rewardItem.get("unitId"));
                detail.setUnitAmount((Integer) rewardItem.get("unitAmount"));
                detail.setPackingId((Integer) rewardItem.get("packingId"));
                detail.setPackingAmount((Integer) rewardItem.get("packingAmount"));
                detail.setPackingExchangeRatio((Integer) rewardItem.get("packingExchangeRatio"));
                detail.setUnitPrice((Double) rewardItem.get("price"));
                detail.setPackingPrice((Double) rewardItem.get("packingPrice"));
                
                detail = this.osiPromotionLimitationDetailRewardService.create(detail);
            }
        }
    }

    protected OrderSellin doCreateOrderSellin(OrderSellinCreateForm form, double totalDiscount, RpcResponse rpcResponse, BindingResult errors) {
        OrderSellin orderSellin = new OrderSellin();
        Retailer retailer = this.retailerService.get(form.getRetailerId());
        RetailerInvoiceInfo retailerInvInfo = this.retailerInvoiceInfoService.getByRetailerId(form.getRetailerId());
        TotalItemInfo info = amountAndUriImage(form.getItems());

        orderSellin.setOrderCode(UUID.randomUUID().toString());
        orderSellin.setRetailerId(form.getRetailerId());
        orderSellin.setRetailerCode(retailer.getRetailerCode());
        orderSellin.setRetailerInvoiceName(retailerInvInfo.getRetailerInvoiceName());
        orderSellin.setTel(retailer.getMobile());
        orderSellin.setInvoiceAddress(retailerInvInfo.getAddressText());
        orderSellin.setOrderDate(LocalDateTime.now());
        orderSellin.setOrderStatus(OrderSellinStatus.NEW.toString());
        orderSellin.setPresentProductImage(info.getUriImage());
        orderSellin.setOrderCost(info.getTotalCost());
        orderSellin.setShippingFee(0.0);
        orderSellin.setVatFee(0.0);
        orderSellin.setPromotionRedeem(totalDiscount);
        orderSellin.setFinalCost(info.getTotalCost() - totalDiscount);
        LinkedHashMap<String, Object> extraData = new LinkedHashMap<String, Object>();
        extraData.put("address", retailerInvInfo.getAddressText());
        orderSellin.setExtraData(extraData);

        this.orderSellinService.create(orderSellin);
        String orderCode = DtoFetchingUtils.generateCode(ORDER_SELLIN_CODE_PREFIX, retailer.getRetailerCode(), orderSellin.getId(), 8);
        orderSellin.setOrderCode(orderCode);
        this.orderSellinService.updatePartial(orderSellin);

        List<OrderSellinItem> orderSellinItems = this.doCreateSellinItem(form.getItems(), orderSellin);
        if (orderSellinItems.size() > 0) {
            this.orderSellinItemService.saveAll(orderSellinItems);
        }

        return orderSellin;
    }

    private TotalItemInfo amountAndUriImage(List<SellinItemForm> items) {
        TotalItemInfo info = new TotalItemInfo();
        String uriImage = "";
        Double orderCost = 0.0;
        boolean hasImage = true;
        if (items == null || items.size() == 0) {
            info.setUriImage(uriImage);
            info.setTotalCost(orderCost);
            return info;
        }

        for (SellinItemForm item : items) {
            if (hasImage) {
                List<ProductResource> resources = this.productResourceService.getAllByProductVariationId(item.getProductVariationId(),
                        ProductResourceType.IMAGE);
                if (resources.size() > 0) {
                    info.setUriImage(resources.get(0).getUri());
                    hasImage = false;
                }
            }

            ProductVariation productVariation = this.productVariationService.getActivated(item.getProductVariationId());
            orderCost = orderCost + item.getAmount() * productVariation.getPrice();
        }

        info.setTotalCost(orderCost);
        return info;
    }

    private List<OrderSellinItem> doCreateSellinItem(List<SellinItemForm> items, OrderSellin orderSellin) {
        List<OrderSellinItem> orderSellinItems = new ArrayList<OrderSellinItem>();
        if (items == null || items.size() == 0) {
            return orderSellinItems;
        }

        for (SellinItemForm item : items) {
            ProductVariation productVariation = this.productVariationService.getActivated(item.getProductVariationId());
            if (productVariation == null)
                continue;
            orderSellinItems.add(mappingItem(orderSellin, productVariation, item));

        }

        return orderSellinItems;
    }

    private OrderSellinItem mappingItem(OrderSellin orderSellin, ProductVariation productVariation, SellinItemForm item) {
        OrderSellinItem orderSellinItem = new OrderSellinItem();
        orderSellinItem.setProductId(productVariation.getProductId());
        orderSellinItem.setProductVariationId(productVariation.getId());
        orderSellinItem.setSku(productVariation.getSku());
        orderSellinItem.setSellinOrderId(orderSellin.getId());
        orderSellinItem.setSellinOrderCode(orderSellin.getOrderCode());
        orderSellinItem.setUnitId(productVariation.getUnitId());
        orderSellinItem.setUnitPrice(productVariation.getPrice());
        orderSellinItem.setUnitAmount(item.getAmount());
        orderSellinItem.setPackingId(productVariation.getPackingId());
        orderSellinItem.setPackingExchangeRatio(productVariation.getPackingExchangeRatio());
        orderSellinItem.setPackingPrice(productVariation.getPackingPrice());
        orderSellinItem.setPackingAmount(0);
        orderSellinItem.setTotalAmount(item.getAmount());
        orderSellinItem.setTotalCost(item.getAmount() * productVariation.getPrice());
        orderSellinItem.setStatus(Status.ACTIVE);

        return orderSellinItem;
    }

    private class TotalItemInfo {

        private String uriImage;
        private Double totalCost;

        public String getUriImage() {
            return uriImage;
        }

        public void setUriImage(String uriImage) {
            this.uriImage = uriImage;
        }

        public Double getTotalCost() {
            return totalCost;
        }

        public void setTotalCost(Double totalCost) {
            this.totalCost = totalCost;
        }

    }

    @Override
    public CityService getCityService() {
        return this.cityService;
    }

    @Override
    public PromotionService getPromotionService() {
        return this.promotionService;
    }

    @Override
    public PromotionLimitationItemService getPromotionLimitationItemService() {
        return this.pliService;
    }

    @Override
    public ProductVariationService getProductVariationService() {
        return this.productVariationService;
    }

    @Override
    public PromotionProductGroupDetailService getPromotionProductGroupDetailService() {
        return this.promotionProductGroupDetailService;
    }

    @Override
    public PromotionLimitationItemRewardProductService getPromotionLimitationItemRewardProductService() {
        return this.promotionLimitationItemRewardProductService;
    }

    @Override
    public ProductResourceService getProductResourceService() {
        return this.productResourceService;
    }

}
