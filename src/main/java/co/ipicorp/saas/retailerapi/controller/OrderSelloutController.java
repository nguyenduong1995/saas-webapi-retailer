/**
 * OrderSelloutStateController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.controller;

import co.ipicorp.saas.nrms.model.OrderSellout;
import co.ipicorp.saas.nrms.model.OrderSelloutPromotionLimitationDetailReward;
import co.ipicorp.saas.nrms.model.ProductResourceType;
import co.ipicorp.saas.nrms.service.OrderSelloutItemService;
import co.ipicorp.saas.nrms.service.OrderSelloutPromotionLimitationDetailItemService;
import co.ipicorp.saas.nrms.service.OrderSelloutPromotionLimitationDetailRewardService;
import co.ipicorp.saas.nrms.service.OrderSelloutPromotionLimitationService;
import co.ipicorp.saas.nrms.service.OrderSelloutPromotionService;
import co.ipicorp.saas.nrms.service.OrderSelloutService;
import co.ipicorp.saas.nrms.service.ProductResourceService;
import co.ipicorp.saas.nrms.service.ProductService;
import co.ipicorp.saas.nrms.service.ProductVariationService;
import co.ipicorp.saas.nrms.service.RetailerService;
import co.ipicorp.saas.nrms.service.UnitService;
import co.ipicorp.saas.nrms.web.dto.OrderSelloutDto;
import co.ipicorp.saas.nrms.web.util.DtoFetchingUtils;
import co.ipicorp.saas.nrms.web.util.ResourceUrlResolver;
import co.ipicorp.saas.retailerapi.security.RetailerSessionInfo;
import co.ipicorp.saas.retailerapi.util.Constants;
import co.ipicorp.saas.retailerapi.util.ControllerAction;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grass.micro.apps.annotation.Logged;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerListingSupport;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.dto.RpcResponse;
import grass.micro.apps.web.util.RequestUtils;

/**
 * OrderSelloutStateController. <<< Detail note.
 * 
 * @author hieumicro
 * @access public
 */
@RestController
@SuppressWarnings("unused")
public class OrderSelloutController {

    @Autowired
    private ErrorsKeyConverter errorsProcessor;

    @Autowired
    private RetailerService retailerService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private ProductVariationService productVariationService;
    
    @Autowired
    private UnitService unitService;
    
    @Autowired
    private OrderSelloutService orderSelloutService;

    @Autowired
    private OrderSelloutItemService orderSelloutItemService;

    @Autowired
    private ProductResourceService productResourceService;
    
    @Autowired
    private OrderSelloutPromotionService osiPromotionService;

    @Autowired
    private OrderSelloutPromotionLimitationService osiPromotionLimitationService;

    @Autowired
    private OrderSelloutPromotionLimitationDetailItemService osiPromotionLimitationDetailItemService;

    @Autowired
    private OrderSelloutPromotionLimitationDetailRewardService osiPromotionLimitationDetailRewardService;

    @RequestMapping(value = ControllerAction.APP_ORDER_SELLOUT_ACTION, method = RequestMethod.GET)
    @Logged
    public ResponseEntity<?> getOrderSelloutOfRetailer(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "status", defaultValue = "ALL") final String status) {
        RetailerSessionInfo info = (RetailerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
        final int retailerId = info.getRetailer().getId();

        AppControllerSupport support = new AppControllerListingSupport() {
            @Override
            public String getAttributeName() {
                return "orders";
            }

            @Override
            public List<? extends Serializable> getEntityList(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
                return orderSelloutService.getAllByRetailerId(retailerId);
            }

            @SuppressWarnings("unchecked")
            @Override
            public List<?> fetchEntitiesToDtos(List<? extends Serializable> entities) {
                initService();
                
                List<OrderSellout> list = (List<OrderSellout>) entities;
                List<OrderSelloutDto> orders = DtoFetchingUtils.fetchOrderSellouts(list);
                
                for (OrderSelloutDto order : orders) {
                    List<Map<String, Object>> items = orderSelloutItemService.getItemsByOderSelloutId(order.getId());
                    for (Map<String, Object> item : items) {
                        String image = (String) item.get("image");
                        item.put("image", ResourceUrlResolver.getInstance().resolveProductUrl(1, image));
                    }
                    order.setSelloutItems(items);
                }
                
                return orders;
            }
            
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }

    private void initService() {
        DtoFetchingUtils.setOrderSelloutItemService(orderSelloutItemService);
        DtoFetchingUtils.setProductService(productService);
        DtoFetchingUtils.setProductVariationService(productVariationService);
        DtoFetchingUtils.setUnitService(unitService);
        DtoFetchingUtils.setRetailerService(retailerService);
        DtoFetchingUtils.setOrderSelloutService(orderSelloutService);
        DtoFetchingUtils.setOrderSelloutItemService(orderSelloutItemService);
    }

    @RequestMapping(value = ControllerAction.APP_ORDER_SELLOUT_DETAIL_ACTION, method = RequestMethod.GET)
    @Logged
    public ResponseEntity<?> getOrderSelloutDetail(HttpServletRequest request, HttpServletResponse response,
            @PathVariable(value = "id") final Integer orderId) {
        AppControllerSupport support = new AppControllerSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                OrderSellout order = orderSelloutService.get(orderId);
                RetailerSessionInfo info = (RetailerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
                final int retailerId = info.getRetailer().getId();

                if (order == null) {
                    errors.reject("404", "Khong tim thay");
                } else {

                    if (order.getRetailerId() != retailerId) {
                        errors.reject("401", "Ban dang lay thong tin don hang cua retailer khac");
                    } else {
                        List<Map<String, Object>> items = orderSelloutItemService.getItemsByOderSelloutId(order.getId());
                        initService();
                        OrderSelloutDto result = DtoFetchingUtils.fetchOrderSellout(order);
                        
                        for (Map<String, Object> item : items) {
                            String image = (String) item.get("image");
                            item.put("image", ResourceUrlResolver.getInstance().resolveProductUrl(1, image));
                        }

                        result.setSelloutItems(items);
                        getRpcResponse().addAttribute("order", result);
                    }
                }
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }

    @GetMapping(value = ControllerAction.APP_ORDER_SELLOUT_DETAIL_REWARD_ACTION)
    @Logged
    public ResponseEntity<?> getOrderRewards(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") Integer orderId) {

        AppControllerSupport support = new AppControllerSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                getOrderRewards(request, orderId, errors, getRpcResponse());
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }

    protected void getOrderRewards(HttpServletRequest request, Integer orderId, Errors errors, RpcResponse rpcResponse) {
        List<OrderSelloutPromotionLimitationDetailReward> rewards = osiPromotionLimitationDetailRewardService.getByOrderId(orderId);

        List<Map<String, Object>> result = new LinkedList<Map<String, Object>>();
        if (CollectionUtils.isNotEmpty(rewards)) {
            List<Integer> pvIds = rewards.stream().map(p -> p.getRewardProductVariationId()).collect(Collectors.toList());
            Map<Integer, String> urls = this.productResourceService.getResourceUrlOfProductVariations(pvIds, ProductResourceType.IMAGE);

            System.err.println(rewards);
            for (OrderSelloutPromotionLimitationDetailReward reward : rewards) {
                result.add(fetchOrderSelloutPromotionLimitationDetailReward(reward, urls));
            }
        }

        rpcResponse.addAttribute("rewards", result);
    }

    private Map<String, Object> fetchOrderSelloutPromotionLimitationDetailReward(OrderSelloutPromotionLimitationDetailReward reward,
            Map<Integer, String> urls) {
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

}
