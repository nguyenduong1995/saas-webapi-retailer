package co.ipicorp.saas.retailerapi.util;

public class Constants {

    public static final String APP_DEFAULT_SITE_NAME_KEY = "app.name";
    // START - FORGOT PASSWORD
    public static final String APP_FORGOTPASSWORD_EXPIRATION_KEY = "forgotpassword.expiration";
    public static final String APP_FORGOT_PASSWORD_EMAIL_TITLE_KEY = "forgotpassword.email.title";
    public static final String APP_RESET_URL_KEY = "resetpassword.url";
    public static final String APP_FORGOT_PASSWORD_TEMPLATE_KEY = "resetpassword.url";
    
    public static final int    APP_DEFAULT_FORGOTPASSWORD_EXPIRATION = 24;
    public static final String APP_DEFAULT_FORGOT_PASSWORD_TEMPLATE = "/forgot_password.html";
    public static final String APP_DEFAULT_FORGOT_PASSWORD_EMAIL_TITLE = "Change your password";
    // END - FORGOT PASSWORD
    
    public static final String APP_SETTING_MAIL_SUPPORT_KEY = "mail.support";
    public static final String APP_SETTING_REGISTRATION_SUPPORT = "registration.support";
    
    // SESSION INFO
    public static final String APP_SESSION_INFO_KEY = "saas-webapi-retailer-session-info";
    
    // HAZELCASE
    public static final String APP_HAZELCAST_SESSION_POOL_KEY = "hazelcast.sessionpool.key";
    public static final String APP_HAZELCAST_SESSION_POOL = "saas-webapi-retailer-session-pool";
    
    // RETAILER EXTRA DATA
    public static final String APP_RETAILER_PRODUCT_SEARCH_KEYWORDS_KEY = "PRODUCT_SEARCH_KEYWORDS";
    public static final String APP_RETAILER_PRODUCT_MAX_KEYWORDS_KEY = "product.search.keywords.max";
    public static final int APP_RETAILER_PRODUCT_MAX_KEYWORDS = 4;
    
    // DEFAULT VALUE
    public static final String APP_DEFAULT_AVATAR_FILENAME = "default.png";
}
