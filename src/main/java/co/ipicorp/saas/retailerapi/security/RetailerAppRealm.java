/**
 * AppRealm.java
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.security;

import co.ipicorp.saas.core.web.security.AppRealm;

/**
 * AppRealm.
 * <<< Detail note.
 * @author hieumicro
 */
public class RetailerAppRealm extends AppRealm {

    @Override
    public String getSaasAuthorizationCacheName() {
        return "RetailerAuthorizationCache";
    }

    @Override
    public String getRealmName() {
        return "retailerRealm";
    }

}