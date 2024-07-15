package co.ipicorp.saas.retailerapi.util;

import co.ipicorp.saas.nrms.model.Promotion;
import co.ipicorp.saas.nrms.model.Retailer;
import co.ipicorp.saas.nrms.model.RetailerWarehouseImportTicket;
import co.ipicorp.saas.nrms.web.util.ResourceUrlResolver;
import co.ipicorp.saas.retailerapi.dto.PromotionDto;
import co.ipicorp.saas.retailerapi.dto.RetailerWarehouseImportTicketDto;

import org.apache.commons.lang.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import grass.micro.apps.component.SystemConfiguration;
import grass.micro.apps.util.SystemUtils;
import grass.micro.apps.web.util.CommonFetchingUtils;

public class DtoFetchingUtils {

    public static List<RetailerWarehouseImportTicketDto> fetchRetailerWarehouseImportTicket(
            List<RetailerWarehouseImportTicket> retailerWarehouseImportTickets) {
        if (retailerWarehouseImportTickets == null) {
            return new ArrayList<>();
        }

        List<RetailerWarehouseImportTicketDto> result = new LinkedList<>();
        for (RetailerWarehouseImportTicket retailerWarehouseImportTicket : retailerWarehouseImportTickets) {
            result.add(fetchRetailerWarehouseImportTicket(retailerWarehouseImportTicket));
        }

        return result;
    }

    public static RetailerWarehouseImportTicketDto fetchRetailerWarehouseImportTicket(RetailerWarehouseImportTicket retailerWarehouseImportTicket) {
        RetailerWarehouseImportTicketDto result = new RetailerWarehouseImportTicketDto();
        CommonFetchingUtils.fetchStatusTimestamp(result, retailerWarehouseImportTicket);
        String props = "retailerId,importTicketCode,description,importType,importPerson,importDate,total";
        SystemUtils.getInstance().copyProperties(retailerWarehouseImportTicket, result, props.split(","));
        return result;
    }

    /**
     * @param entities
     * @return
     */
    public static List<?> fetchPromotions(List<Promotion> promotions) {
        if (promotions == null) {
            return new ArrayList<>();
        }

        List<PromotionDto> result = new LinkedList<>();
        for (Promotion promotion : promotions) {
            result.add(fetchPromotion(promotion));
        }

        return result;
    }
    
    /**
     * @param PromotionDto
     * @return
     */
    public static PromotionDto fetchPromotion(Promotion promotion) {
        PromotionDto result = new PromotionDto();
        CommonFetchingUtils.fetchStatusTimestamp(result, promotion);
        String props = "id,promotionCode,promotionType,name,content,startDate,endDate,preparationDate,manualEndDate,promotionState,"
                + "conditionFormatId,rewardFormatId,subjectType,conditionComparitionType,limitationClaimType,display";
        SystemUtils.getInstance().copyProperties(promotion, result, props.split(","));
        
        String banner = promotion.getBanner();
        banner = ResourceUrlResolver.getInstance().resolvePromotionUrl(1, banner);
        result.setBanner(banner);
        return result;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> fetchRetailerInfo(Retailer retailer) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", retailer.getId());
        map.put("mobile", retailer.getMobile());
        map.put("name", retailer.getName());
        map.put("email", retailer.getEmail());
        map.put("retailerSign", retailer.getRetailerSign());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        map.put("birthday", formatter.format(retailer.getBirthday()));
        map.put("fullAddress", retailer.getFullAddress());
        map.put("mobile", retailer.getMobile());
        map.put("cityId", retailer.getCityId());
        map.put("districtId", retailer.getDistrictId());
        map.put("wardId", retailer.getWardId());
        
        map.put("address", retailer.getAddress());
        
        String avatar = StringUtils.isNotBlank(retailer.getImage()) ? retailer.getImage() : Constants.APP_DEFAULT_AVATAR_FILENAME;
        avatar = ResourceUrlResolver.getInstance().resolveRetailerUrl(1, avatar);
        map.put("avatar", avatar);
        
        int maxNearestSearch = SystemConfiguration.getInstance().getInt(Constants.APP_RETAILER_PRODUCT_MAX_KEYWORDS_KEY,
                Constants.APP_RETAILER_PRODUCT_MAX_KEYWORDS);
        List<String> keywords = (List<String>) retailer.getExtraData().get(Constants.APP_RETAILER_PRODUCT_SEARCH_KEYWORDS_KEY);
        if (keywords != null && keywords.size() > 0) {
            keywords = keywords.subList(0, Math.min(maxNearestSearch, keywords.size()));
        } else {
            keywords = new LinkedList<>();
        }
        
        map.put("keywords", keywords);
        return map;
    }
}
