/**
 * RetailerController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import co.ipicorp.saas.core.model.Account;
import co.ipicorp.saas.core.model.City;
import co.ipicorp.saas.core.model.District;
import co.ipicorp.saas.core.model.Ward;
import co.ipicorp.saas.core.service.AccountService;
import co.ipicorp.saas.core.service.CityService;
import co.ipicorp.saas.core.service.DistrictService;
import co.ipicorp.saas.core.service.WardService;
import co.ipicorp.saas.core.web.components.FileStorageService;
import co.ipicorp.saas.nrms.model.Retailer;
import co.ipicorp.saas.nrms.model.RetailerInvoiceInfo;
import co.ipicorp.saas.nrms.service.RetailerInvoiceInfoService;
import co.ipicorp.saas.nrms.service.RetailerService;
import co.ipicorp.saas.nrms.web.util.ResourceUrlResolver;
import co.ipicorp.saas.retailerapi.form.RetailerChangePasswordForm;
import co.ipicorp.saas.retailerapi.form.RetailerImageForm;
import co.ipicorp.saas.retailerapi.form.RetailerInvoiceUpdateForm;
import co.ipicorp.saas.retailerapi.form.RetailerUpdateForm;
import co.ipicorp.saas.retailerapi.form.validator.RetailerChangePasswordValidator;
import co.ipicorp.saas.retailerapi.security.RetailerSessionInfo;
import co.ipicorp.saas.retailerapi.util.Constants;
import co.ipicorp.saas.retailerapi.util.ControllerAction;
import co.ipicorp.saas.retailerapi.util.DtoFetchingUtils;
import co.ipicorp.saas.retailerapi.util.ErrorCode;
import grass.micro.apps.annotation.AppJsonSchema;
import grass.micro.apps.annotation.Logged;
import grass.micro.apps.annotation.Validation;
import grass.micro.apps.model.util.EntityUpdateTracker;
import grass.micro.apps.util.SystemUtils;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerCreationSupport;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.exception.HttpNotFoundException;
import grass.micro.apps.web.util.RequestUtils;

/**
 * RetailerController.
 * <<< Detail note.
 * @author hieumicro
 * @access public
 */
@RestController
public class RetailerController {
    
    @Autowired
    private ErrorsKeyConverter errorsProcessor;
    
    @Autowired
    private RetailerInvoiceInfoService riService;
    
    @Autowired
    private RetailerService retailerService;
    
    @Autowired
    private CityService cityService;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private WardService wardService;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    @Autowired
    AccountService accountService;
    
    @GetMapping(value = ControllerAction.APP_RETAILER_INVOICE_ACTION)
    @Logged
    public ResponseEntity<?> getRetailerInvoice(HttpServletRequest request, HttpServletResponse response) {
        AppControllerSupport support = new AppControllerCreationSupport() {
            
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
                RetailerSessionInfo info = (RetailerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
                final int retailerId = info.getRetailer().getId();
                RetailerInvoiceInfo invoiceInfo = riService.getByRetailerId(retailerId);
                getRpcResponse().addAttribute("invoiceInfo", co.ipicorp.saas.nrms.web.util.DtoFetchingUtils.fetchRetailerInvoiceInfo(invoiceInfo));
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
    
    @PutMapping(value = ControllerAction.APP_RETAILER_INVOICE_ACTION)
    @Logged
    public ResponseEntity<?> updateRetailerInvoice(HttpServletRequest request, HttpServletResponse response,  @RequestBody RetailerInvoiceUpdateForm form,
            BindingResult errors) {
        AppControllerSupport support = new AppControllerCreationSupport() {
            
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
                RetailerSessionInfo info = (RetailerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
                final int retailerId = info.getRetailer().getId();
                RetailerInvoiceInfo invoiceInfo = RetailerController.this.doUpdateRetailerInvoice(retailerId, form, (BindingResult) errors);
                
                getRpcResponse().addAttribute("invoiceInfo", co.ipicorp.saas.nrms.web.util.DtoFetchingUtils.fetchRetailerInvoiceInfo(invoiceInfo));
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
    
    @PutMapping(value = ControllerAction.APP_RETAILER_ACTION)
    @Logged
    public ResponseEntity<?> updateRetailer(HttpServletRequest request, HttpServletResponse response, @RequestBody RetailerUpdateForm form,
            BindingResult errors) {
        AppControllerSupport support = new AppControllerCreationSupport() {

            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                try {
                	RetailerSessionInfo info = (RetailerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
                    final int retailerId = info.getRetailer().getId();
                    Retailer retailer = RetailerController.this.doUpdateRetailer(retailerId, form, (BindingResult) errors);
                    getRpcResponse().addAttribute("retailer", DtoFetchingUtils.fetchRetailerInfo(retailer));
                } catch (Exception ex) {
                    // do nothing
                }
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }

    @PutMapping(value = ControllerAction.APP_RETAILER_ACTION + "/image")
    @Logged
    public ResponseEntity<?> changeAvatar(HttpServletRequest request, HttpServletResponse response, @RequestBody RetailerImageForm form,
            BindingResult errors) {
        AppControllerSupport support = new AppControllerCreationSupport() {

            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                try {
                	RetailerSessionInfo info = (RetailerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
                    final int retailerId = info.getRetailer().getId();
                    Retailer retailer = RetailerController.this.processUploadAndUpdateImage(1, retailerId, form, (BindingResult) errors);
                    getRpcResponse().addAttribute("retailer", DtoFetchingUtils.fetchRetailerInfo(retailer));
                } catch (Exception ex) {
                    // do nothing
                }
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
    
    @PutMapping(value = ControllerAction.APP_RETAILER_ACTION + "/change_password")
    @Validation(schema = @AppJsonSchema("/schema/change_password.json"), validators = {RetailerChangePasswordValidator.class})
    @Logged
    public ResponseEntity<?> changePassword(HttpServletRequest request, HttpServletResponse response, @RequestBody RetailerChangePasswordForm form,
            BindingResult errors) {
        AppControllerSupport support = new AppControllerCreationSupport() {

            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                
            	RetailerSessionInfo info = (RetailerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
                Account account = RetailerController.this.accountService.getActivated(info.getRetailer().getAccountId());
                String enscriptedPasswd = SystemUtils.getInstance().generatePassword(form.getCurrentPassword(), account.getSalt());
                if (!enscriptedPasswd.equals(account.getPassword())) {
                    errors.reject(ErrorCode.APP_2056_OLD_PASSWORD_NOT_MATCH, ErrorCode.APP_2056_OLD_PASSWORD_NOT_MATCH);
                    return;
                }
                
                account.setPassword(SystemUtils.getInstance().generatePassword(form.getNewPassword(), account.getSalt()));
                EntityUpdateTracker.getInstance().track(Account.class, account.getId(), account.getUpdateCount());
                
                try {
                	RetailerController.this.accountService.update(account);
                    getRpcResponse().addAttribute("message", "success");
                } catch (Exception ex) {
                    errors.reject(ErrorCode.APP_1000_SYSTEM_ERROR, ErrorCode.APP_1000_SYSTEM_ERROR);
                }
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
    
    private RetailerInvoiceInfo doUpdateRetailerInvoice(Integer retailerId, RetailerInvoiceUpdateForm form, BindingResult errors) {
    	RetailerInvoiceInfo invoiceInfo = this.riService.getByRetailerId(retailerId);
    	if (invoiceInfo == null || invoiceInfo.isDeleted()) {
            throw new HttpNotFoundException("Can not find the retailer invoice with id " + retailerId);
        }
    	
    	invoiceInfo.setTel(StringUtils.isNotEmpty(form.getTel()) ? form.getTel() : invoiceInfo.getTel());
    	invoiceInfo.setBankName(StringUtils.isNotEmpty(form.getBankName()) ? form.getBankName() : invoiceInfo.getBankName());
    	invoiceInfo.setBankBranch(StringUtils.isNotEmpty(form.getBankBranch()) ? form.getBankBranch() : invoiceInfo.getBankBranch());
    	invoiceInfo.setBankAccountNo(StringUtils.isNotEmpty(form.getBankAccountNo()) ? form.getBankAccountNo() : invoiceInfo.getBankAccountNo());
    	invoiceInfo.setBankAccountName(StringUtils.isNotEmpty(form.getBankAccountName()) ? form.getBankAccountName() : invoiceInfo.getBankAccountName());
    	this.riService.updatePartial(invoiceInfo);
    	
    	return invoiceInfo;
    }
    
    protected Retailer doUpdateRetailer(Integer retailerId, RetailerUpdateForm form, BindingResult errors) throws Exception {
        Retailer retailer = this.retailerService.get(retailerId);
        if (retailer == null || retailer.isDeleted()) {
            throw new HttpNotFoundException("Can not find the retailer with id " + retailerId);
        }
        
        retailer.setName(StringUtils.isNotEmpty(form.getFullName()) ? form.getFullName() : retailer.getName());
        retailer.setEmail(form.getEmail());
        retailer.setRetailerSign(StringUtils.isNotEmpty(form.getRetailerSign()) ? form.getRetailerSign() : retailer.getRetailerSign());
        retailer.setBirthday(form.getBirthday() != null ? form.getBirthday() : retailer.getBirthday());
        retailer.setMobile(StringUtils.isNotEmpty(form.getMobile()) ? form.getMobile() : retailer.getMobile());
        retailer.setAddress(StringUtils.isNotEmpty(form.getAddress()) ? form.getAddress() : retailer.getAddress());
        retailer.setCityId(form.getCityId() != null ? form.getCityId() : retailer.getCityId());
        retailer.setDistrictId(form.getDistrictId() != null ? form.getDistrictId() : retailer.getDistrictId());
        retailer.setWardId(form.getWardId() != null ? form.getWardId() : retailer.getWardId());
        retailer.setFullAddress(buildFullAddress(form.getAddress(), retailer.getCityId(), retailer.getDistrictId(), retailer.getWardId()));

        retailer = this.retailerService.updatePartial(retailer);
        return retailer;
    }
    
    private String buildFullAddress(String address, Integer cityId, Integer districtId, Integer wardId) {
        City city = this.cityService.get(cityId);
        District district = this.districtService.get(districtId);
        Ward ward = this.wardService.get(wardId);
        return address + "," + ward.getName() + "," + district.getName() + "," + city.getName();
    }
    
    protected Retailer processUploadAndUpdateImage(Integer customerId, Integer retailerId, RetailerImageForm form, BindingResult errors) throws Exception {
    	Retailer retailer = this.retailerService.getActivated(retailerId);
    	if (retailer == null) {
            throw new HttpNotFoundException("Can not find the retailer with id " + retailerId);
        }
    	
        if (StringUtils.isNotEmpty(form.getImage())) {
            //delete old image
            if (StringUtils.isNotEmpty(form.getImage())) {
                this.fileStorageService.deleteFile(retailer.getImage());
            }
            String imagePath = uploadImageToFTP(form.getImage(), customerId, retailerId);
            String extension = "png";
            try {
                extension = form.getImage().split(";")[0].split("/")[1];
            } catch (Exception ex) {
                // do nothing
            }
            

            String path = imagePath + "." + extension;
            retailer.setImage(path);
        }
        
        retailer = this.retailerService.updatePartial(retailer);
        return retailer;
    }

    /**
     * @param image
     * @return
     */
    private String uploadImageToFTP(String image, Integer customerId, Integer retailerId) {
        String location = ResourceUrlResolver.getInstance().resolveFtpRetailerPath(customerId, "");
        String fileName = SystemUtils.getInstance().generateCode("C", customerId, "R", retailerId);

        this.fileStorageService.storFile(image, location, fileName);
        return fileName;
    }
    
}
