/**
 * RetailerWarehouseImportTicketController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.controller;

import co.ipicorp.saas.core.web.util.CommonUtils;
import co.ipicorp.saas.nrms.model.ImportType;
import co.ipicorp.saas.nrms.model.ProductVariation;
import co.ipicorp.saas.nrms.model.Retailer;
import co.ipicorp.saas.nrms.model.RetailerWarehouse;
import co.ipicorp.saas.nrms.model.RetailerWarehouseDailyRemain;
import co.ipicorp.saas.nrms.model.RetailerWarehouseImportTicket;
import co.ipicorp.saas.nrms.model.RetailerWarehouseImportTicketItem;
import co.ipicorp.saas.nrms.model.RetailerWarehouseItem;
import co.ipicorp.saas.nrms.model.RetailerWarehouseItemHistory;
import co.ipicorp.saas.nrms.model.RetailerWarehouseTotalItem;
import co.ipicorp.saas.nrms.model.dto.RetailerWarehouseImportTicketSearchCondition;
import co.ipicorp.saas.nrms.service.ProductVariationService;
import co.ipicorp.saas.nrms.service.RetailerWarehouseDailyRemainService;
import co.ipicorp.saas.nrms.service.RetailerWarehouseImportTicketItemService;
import co.ipicorp.saas.nrms.service.RetailerWarehouseImportTicketService;
import co.ipicorp.saas.nrms.service.RetailerWarehouseItemHistoryService;
import co.ipicorp.saas.nrms.service.RetailerWarehouseItemService;
import co.ipicorp.saas.nrms.service.RetailerWarehouseService;
import co.ipicorp.saas.nrms.service.RetailerWarehouseTotalItemService;
import co.ipicorp.saas.nrms.web.util.ResourceUrlResolver;
import co.ipicorp.saas.retailerapi.dto.RetailerWarehouseImportTicketDto;
import co.ipicorp.saas.retailerapi.form.RetailerWarehouseImportTicketForm;
import co.ipicorp.saas.retailerapi.form.RetailerWarehouseImportTicketItemForm;
import co.ipicorp.saas.retailerapi.form.RetailerWarehouseImportTicketSearchForm;
import co.ipicorp.saas.retailerapi.form.validator.ProductVariationExistedValidator;
import co.ipicorp.saas.retailerapi.form.validator.RetailerWarehouseImportTicketSearchValidator;
import co.ipicorp.saas.retailerapi.security.RetailerSessionInfo;
import co.ipicorp.saas.retailerapi.util.Constants;
import co.ipicorp.saas.retailerapi.util.ControllerAction;
import co.ipicorp.saas.retailerapi.util.DtoFetchingUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grass.micro.apps.annotation.GetBody;
import grass.micro.apps.annotation.Logged;
import grass.micro.apps.annotation.Validation;
import grass.micro.apps.model.base.Status;
import grass.micro.apps.util.SystemUtils;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerCreationSupport;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.util.RequestUtils;
import io.swagger.annotations.Api;

/**
 * RetailerWarehouseImportTicketController.
 * <<< Detail note.
 * @author hieumicro
 * @access public
 */
@RestController
@Api(tags = "Retailer Warehouse Import Ticket APIs", description = "all APIs relate to create/search/detail retailer warehouse import ticket")
public class RetailerWarehouseImportTicketController {
    
    private static final String IMPORT_TICKET_CODE_PREFIX = "RW";
    private static final String IMPORT_TICKET_CODE_MIDDLE = "IMP";

    @Autowired
    private ErrorsKeyConverter errorsProcessor;
    
    @Autowired
    private RetailerWarehouseImportTicketService retailerWarehouseImportTicketService;
    
    @Autowired
    private RetailerWarehouseImportTicketItemService retailerWarehouseImportTicketItemService;
    
    @Autowired
    private ProductVariationService productVariationService;
    
    @Autowired
    private RetailerWarehouseItemService retailerWarehouseItemService;
    
    @Autowired
    private RetailerWarehouseItemHistoryService retailerWarehouseItemHistoryService;
    
    @Autowired
    private RetailerWarehouseTotalItemService retailerWarehouseTotalItemService;
    
    @Autowired
    private RetailerWarehouseService retailerWarehouseService;
    
    @Autowired
    private RetailerWarehouseDailyRemainService rwhDailyRemainService;
    
    @GetMapping(value = ControllerAction.APP_RETAILER_WAREHOUSE_IMPORT_TICKET_ACTION + "/search")
    @Validation(validators = { RetailerWarehouseImportTicketSearchValidator.class } )
    @Logged
    public ResponseEntity<?> searchRetailerWarehouseImportTicket(HttpServletRequest request, HttpServletResponse response, 
            @GetBody RetailerWarehouseImportTicketSearchForm form) {
        AppControllerSupport support = new AppControllerSupport() {

            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                RetailerSessionInfo info = (RetailerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
                int retailerId = info.getRetailer().getId();
                DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                
                RetailerWarehouseImportTicketSearchCondition condition = new RetailerWarehouseImportTicketSearchCondition();
                condition.setLimitSearch(true);
                condition.setSegment(form.getSegment());
                condition.setOffset(form.getOffset());
                condition.setImportTicketCode(form.getImportTicketCode());
                condition.setFromDate(LocalDate.parse(form.getFromDate(), df));
                condition.setToDate(LocalDate.parse(form.getToDate(), df));
                condition.setRetailerId(retailerId);

                long count = RetailerWarehouseImportTicketController.this.retailerWarehouseImportTicketService.count(condition);
                getRpcResponse().addAttribute("count", count);
                
                if (count > form.getSegment()) {
                    List<RetailerWarehouseImportTicket> retailerWarehouseImportTickets = RetailerWarehouseImportTicketController.this.retailerWarehouseImportTicketService.search(condition);
                    List<RetailerWarehouseImportTicketDto> retailerWarehouseImportTicketDtos = DtoFetchingUtils.fetchRetailerWarehouseImportTicket(retailerWarehouseImportTickets);
                    getRpcResponse().addAttribute("tickets", retailerWarehouseImportTicketDtos);
                } else {
                    getRpcResponse().addAttribute("tickets", new ArrayList<>());
                }
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
    
    @GetMapping(value = ControllerAction.APP_RETAILER_WAREHOUSE_IMPORT_TICKET_DETAIL_ACTION)
    @Logged
    public ResponseEntity<?> getRetailerImportTicketDetail(HttpServletRequest request, HttpServletResponse response, 
            @PathVariable("id") Integer ticketId) {
        AppControllerSupport support = new AppControllerSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                RetailerSessionInfo info = (RetailerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
                int retailerId = info.getRetailer().getId();
                
                RetailerWarehouseImportTicket ticket = retailerWarehouseImportTicketService.getActivated(ticketId);
                if (ticket == null) {
                    errors.reject("Không tìm thấy phiếu nhập");
                } else if (ticket.getRetailerId() != retailerId){
                    errors.reject("Phiếu nhập này không thuộc về bạn");
                }
                
                RetailerWarehouseImportTicketDto dto = DtoFetchingUtils.fetchRetailerWarehouseImportTicket(ticket);
                getRpcResponse().addAttribute("importTicket", dto);
                
                List<Map<String, Object>> items = retailerWarehouseImportTicketItemService.getByTicketId(ticket.getId());
                for (Map<String, Object> item : items) {
                    String url = ResourceUrlResolver.getInstance().resolveProductUrl(1, Objects.toString(item.get("image")));
                    item.put("image", url);
                }
                getRpcResponse().addAttribute("items", items);
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
   
    @PostMapping(value = ControllerAction.APP_RETAILER_WAREHOUSE_IMPORT_TICKET_ACTION)
    @Validation(validators = { ProductVariationExistedValidator.class })
    @ResponseBody
    @Logged
    public ResponseEntity<?> createRetailerWarehouseImportTicket(HttpServletRequest request, HttpServletResponse response,
                @RequestBody RetailerWarehouseImportTicketForm form) {
        AppControllerSupport support = new AppControllerCreationSupport() {

            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                RetailerSessionInfo info = (RetailerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY); 
                Retailer retailer = info.getRetailer();
                RetailerWarehouse retailerWarehouse = retailerWarehouseService.getByRetailerId(retailer.getId());
                
                if (retailerWarehouse == null) {
                    retailerWarehouseService.createDefaultForRetailer(retailer.getId());  
                }
                
                RetailerWarehouseImportTicket retailerWarehouseImportTicket = processSaveRetailerWarehouseImportTicket(form, retailer, retailerWarehouse);
                getRpcResponse().addAttribute("retailerWarehouseImportTicket", DtoFetchingUtils.fetchRetailerWarehouseImportTicket(retailerWarehouseImportTicket));
            }
        };
        
        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }

    protected RetailerWarehouseImportTicket processSaveRetailerWarehouseImportTicket(RetailerWarehouseImportTicketForm form, Retailer retailer, RetailerWarehouse retailerWarehouse) {
        RetailerWarehouseImportTicket ticket = this.retailerWarehouseImportTicketService.create(mappingRetailerWarehouseImportTicketFromForm(form, retailer, retailerWarehouse));
        System.err.println("XXX: " + ticket.getImportType());
        ticket.setImportTicketCode(CommonUtils.generateCode(IMPORT_TICKET_CODE_PREFIX, ticket.getRetailerId() , IMPORT_TICKET_CODE_MIDDLE , ticket.getId()));
        List<RetailerWarehouseImportTicketItem> retailerWarehouseImportTicketItems = importRetailerWarehouseImportTicket(ticket, form.getRetailerWarehouseImportTicketItems());

        createOrUpdateRetailerWarehouseItems(retailerWarehouseImportTicketItems);
        Double total = retailerWarehouseImportTicketItems.stream().collect(Collectors.summingDouble(RetailerWarehouseImportTicketItem::getTotal));
        ticket.setTotal(total);
        ticket = this.retailerWarehouseImportTicketService.updatePartial(ticket);
        return ticket;
    }

    private List<RetailerWarehouseItem> createOrUpdateRetailerWarehouseItems(List<RetailerWarehouseImportTicketItem> retailerWarehouseImportTicketItems) {
        List<RetailerWarehouseItem> retailerWarehouseItems = new ArrayList<RetailerWarehouseItem>();
        for (RetailerWarehouseImportTicketItem retailerWarehouseImportTicketItem : retailerWarehouseImportTicketItems) {
            Integer retailerId = retailerWarehouseImportTicketItem.getRetailerId();
            Integer retailerWarehouseId = retailerWarehouseImportTicketItem.getRetailerWarehouseId();
            Integer productId = retailerWarehouseImportTicketItem.getProductId();
            Integer productVariationId =retailerWarehouseImportTicketItem.getProductVariationId();
            String sku = retailerWarehouseImportTicketItem.getSku();
            Integer amount = retailerWarehouseImportTicketItem.getAmount();
            Integer oldAmount = 0;
            RetailerWarehouseItem item = this.retailerWarehouseItemService.getByRetailerWarehouseImportTicketItemInfo(retailerId, retailerWarehouseId, productId, productVariationId, sku);
                                                                                                
            if (item != null) {
                //update total amount
                oldAmount = item.getAmount();
                item.setAmount(oldAmount + amount);
                item = this.retailerWarehouseItemService.updatePartial(item);
            } else {
                // create new warehouse record
                ProductVariation productVariation = new ProductVariation(productId, productVariationId, sku);
                item = this.retailerWarehouseItemService.create(new RetailerWarehouseItem(retailerId, retailerWarehouseId,productId, productVariation,sku, amount));
            }
            
            retailerWarehouseItems.add(item);
            
            //create or update history
            createOrUpdateRetailerWarehouseItemHistory(item.getId(), retailerWarehouseImportTicketItem, oldAmount);
            createRetailerWarehouseTotalItem(retailerId, productId, productVariationId, sku, amount, item.getAmount());
            createOrUpdateRetailerWarehouseDailyRemain(retailerId, retailerWarehouseId, productId, productVariationId, sku, item.getAmount());
        }
        return retailerWarehouseItems;
        
    }

    private void createRetailerWarehouseTotalItem(Integer retailerId, Integer productId, Integer productVariationId, String sku, Integer amount, Integer totalAmount) {
        RetailerWarehouseTotalItem retailerWarehouseTotalItem = this.retailerWarehouseTotalItemService.getByRetailerWarehouseTotalItemInfo(retailerId, productId, productVariationId);
        if (retailerWarehouseTotalItem == null) {
            retailerWarehouseTotalItem = new RetailerWarehouseTotalItem();
            retailerWarehouseTotalItem.setRetailerId(retailerId);
            retailerWarehouseTotalItem.setSku(sku);
            retailerWarehouseTotalItem.setProductId(productId);
            retailerWarehouseTotalItem.setProductVariationId(productVariationId);
        }
        retailerWarehouseTotalItem.setAmount(totalAmount);
        retailerWarehouseTotalItem.setAmountAvailable(totalAmount - retailerWarehouseTotalItem.getAmountInOrders());
        retailerWarehouseTotalItem.setAmountImport(retailerWarehouseTotalItem.getAmountImport() + amount);
        this.retailerWarehouseTotalItemService.saveOrUpdate(retailerWarehouseTotalItem);
    }

    private void createOrUpdateRetailerWarehouseItemHistory(Integer retailerWarehouseItemId, RetailerWarehouseImportTicketItem retailerWarehouseImportTicketItem,
            Integer oldAmount) {
        Integer amount = retailerWarehouseImportTicketItem.getAmount();
        
        RetailerWarehouseItemHistory retailerWarehouseItemHistory = new RetailerWarehouseItemHistory();
        String props = "retailerId,retailerWarehouseId,productId,productVariationId,sku,amount,importTicketCode,importTicketId";
        SystemUtils.getInstance().copyProperties(retailerWarehouseImportTicketItem, retailerWarehouseItemHistory, props.split(","));
        retailerWarehouseItemHistory.setRetailerWarehouseItemId(retailerWarehouseItemId);
        retailerWarehouseItemHistory.setChangeDate(LocalDateTime.now());
        retailerWarehouseItemHistory.setAmount(amount + oldAmount);
        retailerWarehouseItemHistory.setChangeType(1);
        retailerWarehouseItemHistory.setChangeAmount(amount);
        retailerWarehouseItemHistory.setOldAmount(oldAmount);
        retailerWarehouseItemHistory.setExtraData(new LinkedHashMap<>());
        this.retailerWarehouseItemHistoryService.create(retailerWarehouseItemHistory);
        
    }

    private List<RetailerWarehouseImportTicketItem> importRetailerWarehouseImportTicket(RetailerWarehouseImportTicket retailerWarehouseImportTicket,
            List<RetailerWarehouseImportTicketItemForm> retailerWarehouseImportTicketItemsForm) {
        List<RetailerWarehouseImportTicketItem> retailerWarehouseImportTicketItems = new ArrayList<RetailerWarehouseImportTicketItem>();
        for (RetailerWarehouseImportTicketItemForm form : retailerWarehouseImportTicketItemsForm) {
            RetailerWarehouseImportTicketItem item = this.retailerWarehouseImportTicketItemService.create(mappingRetailerWarehouseImportTicketItemFromForm(retailerWarehouseImportTicket, form));
            retailerWarehouseImportTicketItems.add(item);
        }
        return retailerWarehouseImportTicketItems;
    }

    private RetailerWarehouseImportTicketItem mappingRetailerWarehouseImportTicketItemFromForm(RetailerWarehouseImportTicket retailerWarehouseImportTicket,
            RetailerWarehouseImportTicketItemForm retailerWarehouseImportTicketItemForm) {
        RetailerWarehouseImportTicketItem item = new RetailerWarehouseImportTicketItem();
        item.setRetailerId(retailerWarehouseImportTicket.getRetailerId());
        item.setRetailerWarehouseId(retailerWarehouseImportTicket.getRetailerWarehouseId());
        item.setImportTicketId(retailerWarehouseImportTicket.getId());
        item.setImportTicketCode(retailerWarehouseImportTicket.getImportTicketCode());
        item.setAmount(retailerWarehouseImportTicketItemForm.getAmount());
        ProductVariation productVariation = this.productVariationService.get(retailerWarehouseImportTicketItemForm.getProductVariationId());
        item.setProductId(productVariation.getProductId());
        item.setProductVariationId(productVariation.getId());
        item.setSku(productVariation.getSku());
        item.setIncomePrice(productVariation.getIncomePrice());
        item.setTotal(item.getAmount() * item.getIncomePrice());
        item.setStatus(Status.ACTIVE);
        return item;
    }

    private RetailerWarehouseImportTicket mappingRetailerWarehouseImportTicketFromForm(RetailerWarehouseImportTicketForm form, Retailer retailer, RetailerWarehouse retailerWarehouse) {
        RetailerWarehouseImportTicket ticket = new RetailerWarehouseImportTicket();
        ticket.setRetailerId(retailer.getId());
        ticket.setRetailerWarehouseId(retailerWarehouse.getId());
        ticket.setImportType(ImportType.OUTSIDE);
        ticket.setImportPerson(retailer.getName());
        ticket.setDescription(form.getDescription());
        ticket.setImportDate(LocalDateTime.now());
        ticket.setImportTicketCode(IMPORT_TICKET_CODE_PREFIX + System.currentTimeMillis());
        ticket.setStatus(Status.ACTIVE);
        ticket.setExtraData(new LinkedHashMap<>());
        return ticket;
    }
    
    private void createOrUpdateRetailerWarehouseDailyRemain(Integer retailerId, Integer warehouseId, Integer productId, Integer productVariationId, String sku, Integer amount) {
        RetailerWarehouseDailyRemain item = this.rwhDailyRemainService.getInWarehouseByProductInfo(warehouseId, productId, productVariationId);
        if (item == null) {
            item = new RetailerWarehouseDailyRemain();
            item.setRetailerId(retailerId);
            item.setRetailerWarehouseId(warehouseId);
            item.setProductId(productId);
            item.setProductVariationId(productVariationId);
            item.setSku(sku);
            item.setAmount(0);
            item.setAmountImport(0);
        }
        
        item.setAmount(amount + item.getAmount());
        item.setAmountImport(item.getAmountImport() + amount);
        
        this.rwhDailyRemainService.saveOrUpdate(item);
    }

}
