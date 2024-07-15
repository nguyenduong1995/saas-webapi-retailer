/**
 * ControllerAction.java
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.util;

/**
 * ControllerAction. <<< Detail note.
 * 
 * @author hieumicro
 * @access public
 */
public class ControllerAction {

    public static final String APP_ERROR_ACTION = "/error";
    public static final String APP_COMMON_ACTION = "/common";
    public static final String APP_LOGIN_ACTION = "/login";
    public static final String APP_LOGOUT_ACTION = "/logout";

    /*
     * RETAILER ANONYMOUS ACTION
     */
    public static final String APP_AUTH_FORGOT_PASSWORD_ACTION = "/forgot-password";
    public static final String APP_AUTH_RESET_PASSWORD_ACTION = "/reset-password";
    public static final String APP_AUTH_RESET_PASSWORD_CHECK_KEY_ACTION = "/reset-password/check";

    /*
     * RETAILER WAREHOUSE
     */
    public static final String APP_RETAILER_WAREHOUSE_ITEM = "/retailers/warehouses/items";
    public static final String APP_RETAILER_WAREHOUSE_ITEM_HISTORY = "/retailers/warehouses/items/histories";
    public static final String APP_RETAILER_WAREHOUSE_IMPORT_TICKET_ACTION = "/retailers/warehouses/importTickets";
    public static final String APP_RETAILER_WAREHOUSE_IMPORT_TICKET_DETAIL_ACTION = "/retailers/warehouses/importTickets/{id}";
    
    /*
     * ORDER SELLOUT
     */
    public static final String APP_ORDER_SELLOUT_STATE_ACTION = "/orders/sellout/state";
    public static final String APP_ORDER_SELLOUT_ACTION = "/orders/sellout";
    public static final String APP_ORDER_SELLOUT_DETAIL_ACTION = "/orders/sellout/{id}";
    public static final String APP_ORDER_SELLOUT_DETAIL_REWARD_ACTION = "/orders/sellout/{id}/rewards";
    public static final String APP_ORDER_SELLOUT_COUNT_STATE_ACTION = "/orders/sellout/countAndGroupStateByRetailer";
    
    /*
     * Order SELLIN action
     */
    public static final String APP_ORDER_SELLIN_ACTION = "/orders/sellin";
    public static final String APP_ORDER_SELLIN_DETAIL_REWARD_ACTION = "/orders/sellin/{id}/rewards";
    public static final String APP_ORDER_SELLIN_ACTION_SEARCH = "/orders/sellin/search";
    
    /*
     * Retailer Information
     */
    public static final String APP_RETAILER_INVOICE_ACTION = "/invoiceInfo";
    
    /*
     * PRODUCT VARIATION
     */
    public static final String APP_PRODUCT_VARIATION_GET_TOP_ACTION = "/productVariations/top/{number}";
    public static final String APP_PRODUCT_VARIATION_SEARCH_ACTION = "/productVariations/search";
    public static final String APP_PRODUCT_VARIATION_COUNT_IN_WAREHOUSE_ACTION = "/productVariations/{id}/countInWarehouse";
    public static final String APP_PRODUCT_VARIATION_CATEGORY_ACTION = "/productVariations/{id}/categories";
    
    /*
     * Retailer Action
     */
    public static final String APP_RETAILER_ACTION = "/retailer";
    
    /*
     * Consumer Action
     */
    public static final String APP_CONSUMER_ACTION = "/consumer";
    
    /*
     * Promotion Action
     */
    public static final String APP_PROMOTION_ACTION = "/promotions";
    public static final String APP_PROMOTION_DETAIL_ACTION = "/promotions/{id}";
    public static final String APP_PROMOTION_CHECKCART_SELLIN_ACTION = "/promotions/checkCartSellin";
    public static final String APP_PROMOTION_CHECKCART_SELLOUT_ACTION = "/promotions/checkCartSellout";
}
