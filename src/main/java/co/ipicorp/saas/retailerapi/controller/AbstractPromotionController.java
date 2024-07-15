/**
 * AsbtractPromotionController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.controller;

import co.ipicorp.saas.core.model.City;
import co.ipicorp.saas.core.service.CityService;
import co.ipicorp.saas.nrms.model.ConditionComparitionType;
import co.ipicorp.saas.nrms.model.ProductResourceType;
import co.ipicorp.saas.nrms.model.ProductVariation;
import co.ipicorp.saas.nrms.model.Promotion;
import co.ipicorp.saas.nrms.model.PromotionLimitationItem;
import co.ipicorp.saas.nrms.model.PromotionLimitationItemRewardProduct;
import co.ipicorp.saas.nrms.model.PromotionProductGroupDetail;
import co.ipicorp.saas.nrms.service.ProductResourceService;
import co.ipicorp.saas.nrms.service.ProductVariationService;
import co.ipicorp.saas.nrms.service.PromotionLimitationItemRewardProductService;
import co.ipicorp.saas.nrms.service.PromotionLimitationItemService;
import co.ipicorp.saas.nrms.service.PromotionProductGroupDetailService;
import co.ipicorp.saas.nrms.service.PromotionService;
import co.ipicorp.saas.nrms.web.util.ResourceUrlResolver;
import co.ipicorp.saas.retailerapi.dto.ProductVariationRewardDto;
import co.ipicorp.saas.retailerapi.security.RetailerSessionInfo;
import co.ipicorp.saas.retailerapi.util.Constants;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import grass.micro.apps.util.SystemUtils;
import grass.micro.apps.web.util.RequestUtils;

/**
 * AsbtractPromotionController. <<< Detail note.
 * 
 * @author hieumicro
 * @access public
 */
public abstract class AbstractPromotionController {
    private Logger logger = Logger.getLogger(AbstractPromotionController.class);

    protected List<Promotion> getPromotionList(HttpServletRequest request, boolean checkDisplay) {
        RetailerSessionInfo info = (RetailerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
        int retailerId = info.getRetailer().getId();
        int cityId = info.getRetailer().getCityId();
        City city = getCityService().get(cityId);
        int regionId = city.getRegionId();

        List<Promotion> promotions = getPromotionService().getRunningPromotionsByParticipants(Promotion.PROMOTION_TYPE_CTKM, Arrays.asList(retailerId), true);

        List<Integer> ids = promotions.stream().map(p -> p.getId()).collect(Collectors.toList());
        List<Promotion> promotionsByLocations = getPromotionService().getRunningPromotionsByLocation(Promotion.PROMOTION_TYPE_CTKM, Arrays.asList(cityId),
                Arrays.asList(regionId), true);
        for (Promotion promotion : promotionsByLocations) {
            if (!ids.contains(promotion.getId())) {
                promotions.add(promotion);
            }
        }

        logger.info("City: " + cityId);
        logger.info("Region: " + regionId);
        logger.info("promotions: " + promotions);

        if (checkDisplay) {
            promotions = promotions.stream().filter(p -> p.getDisplay()).collect(Collectors.toList());
        }
        
        return promotions;
    }

    /**
     * @param promotionSuggestionForm
     * @return
     */
    protected void getPromotionSuggestionsOnBill(List<Promotion> filters, Map<Integer, Integer> productsInCartMap, List<Integer> productVariationIds,
            List<ProductVariationRewardDto> rewards) {
        // calculate the total bill
        Double totalBill = getTotalBillOnProductsInCart(productsInCartMap);
        if (totalBill > 0) {
            // get all promotion without promotion group
            List<Integer> ids = filters.stream().map(p -> p.getId()).collect(Collectors.toList());
            List<Promotion> promotions = getPromotionService().getAllRunningPromotionWithoutPromotionGroup();
            promotions = promotions.stream().filter(p -> ids.contains(p.getId())).collect(Collectors.toList());
            
            calculateAndFetchPromotionSuggestionOnBill(promotions, rewards, totalBill, productsInCartMap);
        }
    }

    /**
     * @param promotions
     * @param totalBill
     * @return
     */
    private void calculateAndFetchPromotionSuggestionOnBill(List<Promotion> promotions, List<ProductVariationRewardDto> rewards, double totalBill,
            Map<Integer, Integer> productsInCartMap) {
        if (CollectionUtils.isNotEmpty(promotions)) {
            List<Promotion> rewardProductPromotions = promotions.stream().filter(p -> p.getRewardFormatId() == Promotion.REWARD_PRODUCT)
                    .collect(Collectors.toList());

            List<Promotion> rewardValuePromotions = promotions.stream().filter(p -> p.getRewardFormatId() == Promotion.REWARD_VALUE)
                    .collect(Collectors.toList());

            List<Promotion> rewardPercentPromotions = promotions.stream().filter(p -> p.getRewardFormatId() == Promotion.REWARD_PERCENT)
                    .collect(Collectors.toList());
            
            if (CollectionUtils.isNotEmpty(rewardProductPromotions)) {
                // Case 9, 10: Điều kiện SS: Doanh số cả đơn,  Thưởng: Tặng SP
                calculateAndFetchPromotionSuggestionOnBill_RewardProduct(rewardProductPromotions, rewards, totalBill, productsInCartMap);
            }

            if (CollectionUtils.isNotEmpty(rewardValuePromotions)) {
                // Case 12: Điều kiện SS: Doanh số cả đơn, Thưởng: Giảm giá
                calculateAndFetchPromotionSuggestionOnBill_RewardValue(rewardValuePromotions, rewards, totalBill, productsInCartMap, false);
            }

            logger.info("Promotion with Percent reward on bill:" + rewardPercentPromotions);
            if (CollectionUtils.isNotEmpty(rewardPercentPromotions)) {
                // Case 11: Điều kiện SS: Doanh số cả đơn, Thưởng: Giảm giá % (sẽ được tính cuối cùng)
                calculateAndFetchPromotionSuggestionOnBill_RewardValue(rewardPercentPromotions, rewards, totalBill, productsInCartMap, true);
            }

        }
    }

    private void calculateAndFetchPromotionSuggestionOnBill_RewardProduct(List<Promotion> promotions, List<ProductVariationRewardDto> rewards, double totalBill,
            Map<Integer, Integer> productsInCartMap) {
        for (Promotion promotion : promotions) {
            List<PromotionLimitationItem> promotionLimitationItems = getPromotionLimitationItemService().getByPromotionIdAndGroupId(promotion.getId(), null);
            sortItems(promotion, promotionLimitationItems);

            double remain = totalBill;
            for (PromotionLimitationItem limitationItem : promotionLimitationItems) {
                // Case 9: Điều kiện SS: Doanh số cả đơn, Điều kiện: Fix, Thưởng: Tặng SP
                if (promotion.getConditionComparitionType().equals(ConditionComparitionType.FIX.getValue())) {
                    if (remain >= limitationItem.getConditionFixValue()) {
                        addToReward(promotion, -1, productsInCartMap, totalBill, -1, -1, rewards, limitationItem);
                        int multiple = new Double(totalBill / limitationItem.getConditionFixValue()).intValue();
                        remain -= limitationItem.getConditionFixValue() * multiple;
                    }
                } else { // Case 10: Điều kiện SS: Doanh số cả đơn, Điều kiện: Range, Thưởng: Tặng SP
                    if (remain >= limitationItem.getConditionRangeFrom() && remain <= limitationItem.getConditionRangeTo()) {
                        addToReward(promotion, -1, productsInCartMap, totalBill, -1, -1, rewards, limitationItem);
                        break;
                    }
                }

            }
        }

    }

    private void calculateAndFetchPromotionSuggestionOnBill_RewardValue(List<Promotion> promotions, List<ProductVariationRewardDto> rewards, double totalBill,
            Map<Integer, Integer> productsInCartMap, boolean percentOnBill) {
        for (Promotion promotion : promotions) {
            List<PromotionLimitationItem> promotionLimitationItems = getPromotionLimitationItemService().getByPromotionIdAndGroupId(promotion.getId(), null);
            sortItems(promotion, promotionLimitationItems);

            logger.info("ITEM : " + promotionLimitationItems);
            double remain = totalBill;
            for (PromotionLimitationItem limitationItem : promotionLimitationItems) {
                logger.info("Remain: " + remain);
                logger.info("From: " + limitationItem.getConditionRangeFrom());
                logger.info("To: " + limitationItem.getConditionRangeTo());
                if (remain >= limitationItem.getConditionRangeFrom() && remain <= limitationItem.getConditionRangeTo()) {
                    int amount = productsInCartMap.values().stream().reduce(0, (a, b) -> a + b);
                    addToReward(promotion, -1, productsInCartMap, totalBill, -1, amount, rewards, limitationItem, percentOnBill);
                    break;
                }
            }
        }
    }

    /**
     * @param productsInCartMap
     * @return
     */
    private Double getTotalBillOnProductsInCart(Map<Integer, Integer> productsInCartMap) {
        Double totalBill = 0.0;
        if (MapUtils.isNotEmpty(productsInCartMap)) {
            for (Map.Entry<Integer, Integer> productsInCartEntry : productsInCartMap.entrySet()) {
                Integer productVariationId = productsInCartEntry.getKey();
                
                ProductVariation productVariation = getProductVariationService().get(productVariationId);
                
                Double totalPrice = productVariation.getPrice() * productsInCartEntry.getValue();
                totalBill += totalPrice;
            }
        }
        return totalBill;
    }

    /**
     * @param promotionSuggestionForm
     * @return
     */
    protected void getPromotionSuggestionWithProductVariations(List<Promotion> filters, Map<Integer, Integer> productsInCartMap,
            List<Integer> productVariationIds, List<ProductVariationRewardDto> rewards) {
        Map<Integer, List<Promotion>> promotionSuggestionMap = new HashMap<Integer, List<Promotion>>();

        List<Promotion> promotions = getPromotionService().getAllRunningPromotionByProductVariationIds(productVariationIds);
        List<Integer> ids = filters.stream().map(p -> p.getId()).collect(Collectors.toList());
        promotions = promotions.stream().filter(p -> ids.contains(p.getId())).collect(Collectors.toList());

        logger.info(promotions);
        calculateAndFetchPromotionSuggestionDetals(promotions, promotionSuggestionMap, productsInCartMap, rewards);
    }

    /**
     * @param promotions
     * @param promotionSuggestionMap
     * @param productsInCartMap
     * @return
     */
    private void calculateAndFetchPromotionSuggestionDetals(List<Promotion> promotions, Map<Integer, List<Promotion>> promotionSuggestionMap,
            Map<Integer, Integer> productsInCartMap, List<ProductVariationRewardDto> rewards) {
        if (CollectionUtils.isNotEmpty(promotions)) {
            for (Promotion promotion : promotions) {
                calculateAndFetchPromotionSuggestionDetail(promotion, productsInCartMap, promotionSuggestionMap, rewards);
            }
        }
    }

    /**
     * 
     * 
     * @param promotion
     * @param productsInCartMap
     * @param promotionSuggestionMap
     */
    private void calculateAndFetchPromotionSuggestionDetail(Promotion promotion, Map<Integer, Integer> productsInCartMap,
            Map<Integer, List<Promotion>> promotionSuggestionMap, List<ProductVariationRewardDto> rewards) {
        List<Integer> pvIds = new ArrayList<>();
        pvIds.addAll(productsInCartMap.keySet());

        List<PromotionProductGroupDetail> promotionProductGroupDetails = getPromotionProductGroupDetailService()
                .getByPromotionIdAndProductVariationIds(promotion.getId(), pvIds);
        logger.info(promotionProductGroupDetails);

        if (CollectionUtils.isNotEmpty(promotionProductGroupDetails)) {
            // Xử lí dành cho promotion dạng xét theo "Số lượng sản phẩm" - 1
            if (Promotion.CONDITION_FORMAT_PRODUCT_COUNT == promotion.getConditionFormatId()) {
                logger.info("Xử lí dành cho promotion dạng xét theo Số lượng sản phẩm - 1");
                for (PromotionProductGroupDetail promotionProductGroupDetail : promotionProductGroupDetails) {
                    calculateAndFetchPromotionWithProductVariation(promotionProductGroupDetail,
                            promotion, productsInCartMap, promotionSuggestionMap, rewards);
                }
            } else if (Promotion.CONDITION_FORMAT_PRODUCT_VALUE == promotion.getConditionFormatId()) {
                logger.info("Xử lí dành cho promotion dạng xét theo Doanh Số sản phẩm - 2");
                calculateAndFetchPromotionWithProductVariation_ProductValue(promotionProductGroupDetails,
                        promotion, productsInCartMap, promotionSuggestionMap, rewards);
            }
        }
    }

    private void calculateAndFetchPromotionWithProductVariation_ProductValue(List<PromotionProductGroupDetail> groupDetails, Promotion promotion,
            Map<Integer, Integer> productsInCartMap, Map<Integer, List<Promotion>> promotionSuggestionMap, List<ProductVariationRewardDto> rewards) {
        // 1. Lọc Group
        Map<Integer, List<Integer>> groups = new LinkedHashMap<Integer, List<Integer>>();

        for (PromotionProductGroupDetail groupDetail : groupDetails) {
            List<Integer> list = groups.get(groupDetail.getGroupId());
            if (list == null) {
                list = new LinkedList<Integer>();
            }

            list.add(groupDetail.getProductVariationId());
            groups.put(groupDetail.getGroupId(), list);
        }

        // 2. Tính doanh số giỏ hàng (theo group)
        List<Integer> pvIds = new ArrayList<>();
        pvIds.addAll(productsInCartMap.keySet());
        List<ProductVariation> productVariations = getProductVariationService().get(pvIds);
        Map<Integer, ProductVariation> productVariationMap = productVariations.stream().collect(Collectors.toMap(pv -> pv.getId(), pv -> pv));

        Map<Integer, Double> groupTotals = new LinkedHashMap<Integer, Double>();
        for (Map.Entry<Integer, List<Integer>> entry : groups.entrySet()) {
            double groupTotal = 0.0;
            Map<Integer, Integer> productInGroup = new HashMap<>();
            for (Integer productVariationId : entry.getValue()) {
                ProductVariation pv = productVariationMap.get(productVariationId);
                int amount = productsInCartMap.get(productVariationId);
                groupTotal += pv.getPrice() * amount;
                productInGroup.put(productVariationId, productsInCartMap.get(productVariationId));
            }
            groupTotals.put(entry.getKey(), groupTotal);

            // 3. Tính hạng mức item theo group của promotion
            List<PromotionLimitationItem> items = getPromotionLimitationItemService().getByPromotionIdAndGroupId(promotion.getId(), entry.getKey());
            // 4. Sắp xếp hạn mức và so sánh.
            sortItems(promotion, items);
            for (PromotionLimitationItem item : items) {
                // Case 5: Điều kiện SS: Doanh số SP, ĐK: Fix, Tặng SP (nhồi), ăn được nhiều hạn mức
                if (promotion.getConditionComparitionType().equals(ConditionComparitionType.FIX.getValue())) {
                    if (groupTotal >= item.getConditionFixValue()) {
                        addToReward(promotion, entry.getKey(), productInGroup, groupTotal, -1, -1, rewards, item);
                        int multiple = new Double(groupTotal / item.getConditionFixValue()).intValue();
                        groupTotal -= item.getConditionFixValue().intValue() * multiple;
                    }
                } else { // Case 6,7,8: Điều kiện SS: Doanh số SP, ĐK: Range, Tặng SP (nhồi), ăn hạn mức cao nhất
                    if (groupTotal >= item.getConditionRangeFrom() && groupTotal <= item.getConditionRangeTo()) {
                        addToReward(promotion, entry.getKey(), productInGroup, groupTotal, -1, -1, rewards, item);
                        break;
                    }
                }
            }
        }
    }

    /**
     * @param promotionProductGroupDetail
     * @param promotion
     * @param productsInCartMap
     * @return
     */
    private void calculateAndFetchPromotionWithProductVariation(PromotionProductGroupDetail promotionProductGroupDetail, Promotion promotion,
            Map<Integer, Integer> productsInCartMap, Map<Integer, List<Promotion>> promotionSuggestionMap, List<ProductVariationRewardDto> rewards) {

        Integer productVariationId = promotionProductGroupDetail.getProductVariationId();
        int currentAmount = productsInCartMap.get(productVariationId) != null ? productsInCartMap.get(productVariationId) : 0;
        List<PromotionLimitationItem> items = getPromotionLimitationItemService().getByPromotionIdAndGroupId(promotionProductGroupDetail.getPromotionId(),
                promotionProductGroupDetail.getGroupId());

        // Xét thưởng của 1 product variation trên danh sách hạn mức

        if (currentAmount > 0) {
            sortItems(promotion, items);

            for (PromotionLimitationItem item : items) {
                // Điều kiện SS: Số lượng, Điểu kiện FIX, 
                if (promotion.getConditionComparitionType().equals(ConditionComparitionType.FIX.getValue())) {
                    if (currentAmount >= item.getConditionFixValue().intValue()) {
                        addToReward(promotion, -1, null, 0, productVariationId, currentAmount, rewards, item);
                        
                        // Case 1: Điều kiện SS: Số lượng SP, Điểu kiện FIX, Tặng SP (được ăn nhồi cho hạn mức tiếp theo)
                        if (promotion.getRewardFormatId() == Promotion.REWARD_PRODUCT) { 
                            int multiple = currentAmount / item.getConditionFixValue().intValue();
                            currentAmount -= item.getConditionFixValue().intValue() * multiple;
                        } else { // Điều kiện SS: Số lượng SP, Điểu kiện FIX, Tặng tiền (Chỉ ăn được hạn mức cao nhất)
                            break;
                        }
                    }
                } else {  // Case 2,3,4: Điều kiện SS: Số lượng, Điểu kiện RANGE, Thưởng gì cũng chỉ xét hạng mức cao nhất 
                    if (currentAmount >= item.getConditionRangeFrom().intValue() && currentAmount <= item.getConditionRangeTo().intValue()) {
                        addToReward(promotion, -1, null, 0, productVariationId, currentAmount, rewards, item);
                        // Ăn hạn mức cao nhất rồi không kiểm tra nữa
                        break;
                    }
                }
            }
        }
    }

    private void addToReward(Promotion promotion, int groupId, Map<Integer, Integer> productInGroup, double groupTotal, int productVariationId,
            int currentAmount, List<ProductVariationRewardDto> rewards, PromotionLimitationItem item) {
        addToReward(promotion, groupId, productInGroup, groupTotal, productVariationId, currentAmount, rewards, item, false);
    }

    private void addToReward(Promotion promotion, int groupId, Map<Integer, Integer> productInGroup, double groupTotal, int productVariationId,
            int currentAmount, List<ProductVariationRewardDto> rewards, PromotionLimitationItem item, boolean percentOnBill) {
        ProductVariationRewardDto reward = new ProductVariationRewardDto();
        reward.setGroupId(item.getProductGroupId() == null ? -1 : item.getProductGroupId());
        reward.setProductVariationId(productVariationId);
        reward.setPromotion(promotion);
        reward.setAmountInCart(currentAmount);
        reward.setDiscount(0.0);
        reward.setPromotionLimitationItem(item);
        reward.setDiscountPercentOnBill(percentOnBill);
        if (productVariationId > 0) {
            Map<Integer, Integer> cart = new HashMap<>();
            cart.put(productVariationId, currentAmount);
            createItemsInGroup(cart, reward);
        }

        if (productInGroup != null && !productInGroup.isEmpty()) {
            createItemsInGroup(productInGroup, reward);
        }

        // Thưởng: Giảm giá trị: Case 4,8,12
        if (promotion.getRewardFormatId() == Promotion.REWARD_VALUE) {
            double discount = item.getRewardValue();
// TODO: Có thể trong tương lại sẽ cân nhắc đến việc xét đk FIX cho giảm giá trị            
//            if (promotion.getConditionComparitionType() == Promotion.COMPARE_TYPE_FIX) {
//                discount = discount * currentAmount;
//            }
            // Case 4, 8: Multiple per amount of cart item. Case 12: No Multiple
            if (Promotion.CONDITION_FORMAT_ORDER_VALUE != promotion.getConditionFormatId()) {
                int amount = currentAmount > 0 ? currentAmount : productInGroup.values().stream().reduce(0, (a, b) -> a + b);
                discount = discount * amount;
            }
            
            reward.setDiscount(discount);
        
        // Thưởng: Giảm %: Case 3,7,11
        } else if (promotion.getRewardFormatId() == Promotion.REWARD_PERCENT) {
            double discount = 0.0;
            if (percentOnBill) { // case 11: Xử lí bằng cách lấy % sau khi trừ toàn bộ giảm giá trước đó.
                double currentDiscount = rewards.stream().map(r -> r.getDiscount()).collect(Collectors.summingDouble(Double::new));
                discount = item.getRewardPercent() * (groupTotal - currentDiscount) / 100;
            } else {
                if (productVariationId > 0) { // Case 3, 7: Giảm % của sản phẩm hoặc nhóm sản phẩm
                    ProductVariation pv = this.getProductVariationService().get(productVariationId);
                    discount = (pv.getPrice() + currentAmount) * item.getRewardPercent() / 100;
                } else {
                    discount = groupTotal * item.getRewardPercent() / 100;
                }
            }

            reward.setDiscount(discount);
        // Case 1,2,5,6,9,10: Thưởng: Tặng sản phẩm.
        } else {
            // Case 2,6,10: Mặt định là không có bội số
            int multiple = 1;
            
            // Case 1, 5, 9 >> Điền kiện: FIX,Thưởng: Tặng sản phẩm >> Bội số
            if (promotion.getConditionComparitionType() == Promotion.COMPARE_TYPE_FIX) {
                if (productVariationId > 0 && currentAmount > 0) { // Tính trên sản phẩm đơn - Mua sản số lượng, tặng số lượng
                    multiple = currentAmount / item.getConditionFixValue().intValue();
                } else if (groupId > 0 && groupTotal > 0) {
                    multiple = new Double(groupTotal / item.getConditionFixValue()).intValue();
                }
            }
            
            List<PromotionLimitationItemRewardProduct> list = this.getPromotionLimitationItemRewardProductService()
                    .getByLimitationIdAndPromotionLimitationItemId(item.getLimitationId(), item.getId());

            List<Integer> pvIds = list.stream().map(p -> p.getProductVariationId()).collect(Collectors.toList());
            Map<Integer, String> urls = this.getProductResourceService().getResourceUrlOfProductVariations(pvIds, ProductResourceType.IMAGE);
            List<ProductVariation> pvs = this.getProductVariationService().get(pvIds);
            for (PromotionLimitationItemRewardProduct element : list) {
                ProductVariation pv = pvs.stream().filter(p -> p.getId().equals(element.getProductVariationId())).findFirst().get();

                Map<String, Object> map = new LinkedHashMap<String, Object>();
                map.put("productId", element.getProductId());
                map.put("productVariationId", element.getProductVariationId());
                map.put("productVariationName", element.getProductVariationName());
                map.put("amount", element.getAmount() * multiple);
                map.put("unitAmount", element.getUnitAmount() * multiple);
                map.put("packingAmount", element.getPackingAmount());
                map.put("packingExchangeRatio", element.getPackingExchangeRatio());
                map.put("unitId", element.getUnitId());
                map.put("packingId", element.getPackingId());
                map.put("price", pv.getPrice());
                map.put("packingPrice", pv.getPackingPrice());
                double itemValue = pv.getPrice() * element.getUnitAmount() + pv.getPackingPrice() * element.getPackingAmount();
                map.put("itemValue",  multiple * itemValue);
                map.put("image", ResourceUrlResolver.getInstance().resolveProductUrl(1, urls.get(element.getProductVariationId())));
                reward.getItems().add(map);
            }
        }

        rewards.add(reward);
    }

    /**
     * @param productVariationId
     * @param currentAmount
     * @param reward
     */
    private void createItemsInGroup(Map<Integer, Integer> cart, ProductVariationRewardDto reward) {
        List<Integer> pvIds = cart.keySet().stream().collect(Collectors.toList());
        List<ProductVariation> pvs = this.getProductVariationService().get(pvIds);

        Map<Integer, String> urls = this.getProductResourceService().getResourceUrlOfProductVariations(pvIds, ProductResourceType.IMAGE);

        logger.info("Cart: " + cart);
        for (ProductVariation pv : pvs) {
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            
            int currentAmount = cart.get(pv.getId());
            map.put("productId", pv.getProductId());
            map.put("productVariationId", pv.getId());
            map.put("productVariationName", pv.getName());
            map.put("sku", pv.getSku());
            map.put("amount", currentAmount);
            map.put("unitAmount", currentAmount);
            map.put("packingAmount", 0);
            map.put("packingExchangeRatio", pv.getPackingExchangeRatio());
            map.put("unitId", pv.getUnitId());
            map.put("packingId", pv.getPackingId());
            map.put("price", pv.getPrice());
            map.put("packingPrice", pv.getPackingPrice());
            double itemValue = pv.getPrice() * currentAmount;
            map.put("itemValue", itemValue);
            map.put("image", ResourceUrlResolver.getInstance().resolveProductUrl(1, urls.get(pv.getId())));
            reward.getItemsInGroup().add(map);
        }
    }

    /**
     * @param promotion
     * @param items
     */
    private void sortItems(final Promotion promotion, List<PromotionLimitationItem> items) {
        Collections.sort(items, (p1, p2) -> {
            double value1 = p1.getConditionFixValue();
            double value2 = p2.getConditionFixValue();
            if (promotion.getConditionComparitionType().equals(ConditionComparitionType.RANGE.getValue())) {
                value1 = p1.getConditionRangeFrom();
                value2 = p2.getConditionRangeFrom();
            }

            if (value1 > value2) {
                // Sap ep tu lon den nho
                return -1;
            } else if (value1 < value2) {
                return 1;
            } else {
                return 0;
            }
        });
    }

    protected List<Map<String, Object>> fetchPromotions(List<Promotion> promotions) {
        List<Map<String, Object>> result = new LinkedList<>();
        for (Promotion promotion : promotions) {
            result.add(fetchPromotion(promotion));
        }
        return result;
    }

    protected Map<String, Object> fetchPromotion(Promotion promotion) {
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        SystemUtils.getInstance().copyPropertiesToMap(promotion, result, "id,name,content,subjectType".split(","));
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime preparationDate = promotion.getPreparationDate() != null ? promotion.getPreparationDate() : promotion.getStartDate().atStartOfDay();
        result.put("preparationDate", df.format(preparationDate));
        result.put("startDate", df.format(promotion.getStartDate()));
        result.put("endDate", df.format(promotion.getEndDate()));
        String banner = ResourceUrlResolver.getInstance().resolvePromotionUrl(1, promotion.getBanner());
        result.put("banner", banner);
        return result;
    }

    /**
     * @param items
     * @return
     */
    protected List<Map<String, Object>> filterItems(List<Map<String, Object>> items) {
        List<Map<String, Object>> filteredItems = new LinkedList<>();
        for (Map<String, Object> item : items) {
            Integer pvId = (Integer) item.get("productVariationId");
            Map<String, Object> currentItem = null;

            try {
                currentItem = filteredItems.stream().filter(p -> pvId.equals(p.get("productVariationId"))).findFirst().get();
            } catch (Exception ex) {
            }

            if (currentItem == null) {
                filteredItems.add(item);
            } else {
                Integer currentAmount = (Integer) currentItem.getOrDefault("amount", new Integer(0));
                currentItem.put("amount", currentAmount + (Integer) item.getOrDefault("amount", new Integer(0)));

                Integer currentUnitAmount = (Integer) currentItem.getOrDefault("unitAmount", new Integer(0));
                currentItem.put("unitAmount", currentUnitAmount + (Integer) item.getOrDefault("unitAmount", new Integer(0)));

                Integer currentPackingAmount = (Integer) currentItem.getOrDefault("packingAmount", new Integer(0));
                currentItem.put("packingAmount", currentPackingAmount + (Integer) item.getOrDefault("packingAmount", new Integer(0)));

                double itemValue = (Double) currentItem.getOrDefault("itemValue", 0.0);
                currentItem.put("itemValue", itemValue + (Double) item.getOrDefault("itemValue", 0.0));
            }
        }
        return filteredItems;
    }

    abstract CityService getCityService();

    abstract PromotionService getPromotionService();

    abstract PromotionLimitationItemService getPromotionLimitationItemService();

    abstract ProductVariationService getProductVariationService();

    abstract PromotionProductGroupDetailService getPromotionProductGroupDetailService();

    abstract PromotionLimitationItemRewardProductService getPromotionLimitationItemRewardProductService();

    abstract ProductResourceService getProductResourceService();
}
