/**
 * LoginController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.controller;

import co.ipicorp.saas.core.model.Account;
import co.ipicorp.saas.core.model.AccountType;
import co.ipicorp.saas.core.service.AccountService;
import co.ipicorp.saas.nrms.model.Retailer;
import co.ipicorp.saas.nrms.service.RetailerService;
import co.ipicorp.saas.retailerapi.form.LoginForm;
import co.ipicorp.saas.retailerapi.security.RetailerSessionInfo;
import co.ipicorp.saas.retailerapi.util.Constants;
import co.ipicorp.saas.retailerapi.util.ControllerAction;
import co.ipicorp.saas.retailerapi.util.DtoFetchingUtils;
import co.ipicorp.saas.retailerapi.util.ErrorCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grass.micro.apps.annotation.NoRequiredAuth;
import grass.micro.apps.component.SystemConfiguration;
import grass.micro.apps.util.SystemUtils;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.component.SessionPool;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.dto.RpcResponse;
import grass.micro.apps.web.util.RequestUtils;

/**
 * LoginController. <<< Detail note.
 * 
 * @author hieumicro
 * @access public
 */
@RestController
public class LoginController {

    @Autowired
    private SessionPool<RetailerSessionInfo> pool;

    @Autowired
    private RetailerService retailerService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ErrorsKeyConverter errorsProcessor;

    @RequestMapping(value = ControllerAction.APP_LOGIN_ACTION, method = RequestMethod.POST)
    @NoRequiredAuth
    public ResponseEntity<?> login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginForm form, BindingResult errors) {
        AppControllerSupport support = new AppControllerSupport() {

            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                processLogin(request, response, form, errors, getRpcResponse());
            }
        };

        return support.doSupport(request, response, errors, errorsProcessor);
    }

    private void processLogin(HttpServletRequest request, HttpServletResponse response, LoginForm loginForm, Errors errors, RpcResponse rpcResponse) {
        if (!errors.hasErrors()) {
            Account account = LoginController.this.accountService.login(loginForm.getLoginName(), loginForm.getPassword(), Arrays.asList(AccountType.RETAILER, AccountType.STAFF_OF_RETAILER));

            // validate user
            if (account != null && AccountType.RETAILER.equals(account.getAccountType())) {
                if (!errors.hasErrors()) {
                    Retailer retailer = LoginController.this.retailerService.getByAccountId(account.getId());
                    if (retailer != null) {
                        LoginController.this.saveSession(request, response, account, retailer, loginForm, rpcResponse);
                        rpcResponse.addAttribute("retailer", DtoFetchingUtils.fetchRetailerInfo(retailer));
                    } else {
                        errors.reject(ErrorCode.APP_1101_ACCOUNT_NOT_EXIST);
                    }
                }
            } else {
                errors.reject(ErrorCode.APP_1101_ACCOUNT_NOT_EXIST);
            }
        }
    }

    /**
     * @param retailer
     * @return
     */
    /*
     * Build SessionInfo object from Logged-in User and it into Session.
     * 
     * @param request {@link HttpServletRequest}
     * 
     * @param loginUser {@link User}
     */
    private void saveSession(HttpServletRequest request, HttpServletResponse response, Account account, Retailer retailer, LoginForm loginForm,
            RpcResponse result) {
        String sessionId = request.getSession(true).getId();

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("loginName", account.getLoginName());
        map.put("email", account.getEmail());
        map.put("sessionId", sessionId);

        // int timeout = SystemConfiguration.getInstance().getInt(PropertiesConstants.APP_SESSION_TIMEOUT_KEY,
        // PropertiesConstants.APP_DEFAULT_SESSION_TIMEOUT);
        String branchName = SystemConfiguration.getInstance().getProperty(Constants.APP_DEFAULT_SITE_NAME_KEY);
        String token = SystemUtils.getInstance().createJWT("A_C_" + account.getId() + "_RT_" + retailer.getId(), branchName, "RT" + retailer.getId(), map, -1);
        result.addAttribute("token", token);
        RetailerSessionInfo sessionInfo = new RetailerSessionInfo();
        sessionInfo.setUsername(account.getLoginName());
        sessionInfo.setToken(token);
        sessionInfo.setRetailer(retailer);

        this.pool.putSession(account.getLoginName(), sessionInfo);
        RequestUtils.getInstance().setSessionInfo(request, sessionInfo, Constants.APP_SESSION_INFO_KEY);
    }
}
