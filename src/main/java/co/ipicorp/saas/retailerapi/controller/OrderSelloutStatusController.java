/**
 * OrderSelloutController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.controller;

import co.ipicorp.saas.core.web.util.CommonUtils;
import co.ipicorp.saas.nrms.model.OrderSellout;
import co.ipicorp.saas.nrms.model.OrderSelloutItem;
import co.ipicorp.saas.nrms.model.OrderSelloutPromotionLimitationDetailReward;
import co.ipicorp.saas.nrms.model.OrderSelloutStateChangeHistory;
import co.ipicorp.saas.nrms.model.OrderSelloutStatus;
import co.ipicorp.saas.nrms.model.ProductVariation;
import co.ipicorp.saas.nrms.model.Retailer;
import co.ipicorp.saas.nrms.model.RetailerWarehouse;
import co.ipicorp.saas.nrms.model.RetailerWarehouseDailyRemain;
import co.ipicorp.saas.nrms.model.RetailerWarehouseExportTicket;
import co.ipicorp.saas.nrms.model.RetailerWarehouseExportTicketItem;
import co.ipicorp.saas.nrms.model.RetailerWarehouseItem;
import co.ipicorp.saas.nrms.model.RetailerWarehouseItemHistory;
import co.ipicorp.saas.nrms.model.RetailerWarehouseTotalItem;
import co.ipicorp.saas.nrms.service.OrderSelloutItemService;
import co.ipicorp.saas.nrms.service.OrderSelloutPromotionLimitationDetailRewardService;
import co.ipicorp.saas.nrms.service.OrderSelloutService;
import co.ipicorp.saas.nrms.service.OrderSelloutStateChangeHistoryService;
import co.ipicorp.saas.nrms.service.ProductVariationService;
import co.ipicorp.saas.nrms.service.RetailerService;
import co.ipicorp.saas.nrms.service.RetailerWarehouseDailyRemainService;
import co.ipicorp.saas.nrms.service.RetailerWarehouseExportTicketItemService;
import co.ipicorp.saas.nrms.service.RetailerWarehouseExportTicketService;
import co.ipicorp.saas.nrms.service.RetailerWarehouseImportTicketService;
import co.ipicorp.saas.nrms.service.RetailerWarehouseItemHistoryService;
import co.ipicorp.saas.nrms.service.RetailerWarehouseItemService;
import co.ipicorp.saas.nrms.service.RetailerWarehouseService;
import co.ipicorp.saas.nrms.service.RetailerWarehouseTotalItemService;
import co.ipicorp.saas.nrms.service.util.ServiceUtils;
import co.ipicorp.saas.retailerapi.form.SelloutCancelForm;
import co.ipicorp.saas.retailerapi.form.SelloutIdForm;
import co.ipicorp.saas.retailerapi.security.RetailerSessionInfo;
import co.ipicorp.saas.retailerapi.util.Constants;
import co.ipicorp.saas.retailerapi.util.ControllerAction;
import co.ipicorp.saas.retailerapi.validator.AmountItemRemainValidator;
import co.ipicorp.saas.retailerapi.validator.OrderSelloutExistValidator;
import co.ipicorp.saas.retailerapi.validator.OrderSelloutStateApprovedExistValidator;
import co.ipicorp.saas.retailerapi.validator.OrderSelloutStateDeliveredExistValidator;
import co.ipicorp.saas.retailerapi.validator.OrderSelloutStateNewExistValidator;
import co.ipicorp.saas.retailerapi.validator.RetailerExistedValidator;
import co.ipicorp.saas.retailerapi.validator.RetailerWarehouseExistedValidator;
import co.ipicorp.saas.retailerapi.validator.RetailerWarehouseTotalItemExistValidator;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grass.micro.apps.annotation.Logged;
import grass.micro.apps.annotation.NoRequiredAuth;
import grass.micro.apps.annotation.Validation;
import grass.micro.apps.model.base.Status;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerCreationSupport;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.dto.RpcResponse;
import grass.micro.apps.web.util.RequestUtils;

/**
 * OrderSelloutController. <<< Detail note.
 * 
 * @author nt.duong
 * @access public
 */
@RestController
public class OrderSelloutStatusController {

	private static final String RETAILER_WAREHOUSE_PREFIX = "RW";
	private static final String EXPORT_TICKET_CODE_MIDDLE = "EXP";
    public static final String IMPORT_TICKET_CODE_MIDDLE = "IMP";

	@Autowired
	private ErrorsKeyConverter errorsProcessor;

	@Autowired
	private OrderSelloutService osService;

	@Autowired
	private OrderSelloutItemService osiService;

	@Autowired
	private RetailerWarehouseTotalItemService rwhtiService;

	@Autowired
	private OrderSelloutStateChangeHistoryService osscHistoryService;

	@Autowired
	private RetailerWarehouseService rwhService;

	@Autowired
	private RetailerWarehouseItemService rwhItemService;

	@Autowired
	private RetailerWarehouseItemHistoryService retailerWarehouseItemHistoryService;

	@Autowired
	private RetailerWarehouseExportTicketService rwhetService;
	
	@Autowired
    private RetailerWarehouseImportTicketService rwhImportTicketService;

	@Autowired
	private RetailerWarehouseExportTicketItemService retailerWarehouseExportTicketItemService;

	@Autowired
	private RetailerService retailerService;

	@Autowired
	private ProductVariationService productVariationService;
	
	@Autowired
	private RetailerWarehouseDailyRemainService rwhDailyRemainService;

	@Autowired
    private OrderSelloutPromotionLimitationDetailRewardService rewardService;
	
	@PostMapping(value = ControllerAction.APP_ORDER_SELLOUT_STATE_ACTION + "/approved")
	@Validation(validators = { OrderSelloutExistValidator.class, AmountItemRemainValidator.class,
			OrderSelloutStateNewExistValidator.class })
	@NoRequiredAuth
	@Logged
	public ResponseEntity<?> stateAprroved(HttpServletRequest request, HttpServletResponse response,
			@RequestBody SelloutIdForm form, BindingResult errors) {
		AppControllerSupport support = new AppControllerCreationSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {

				OrderSelloutStatusController.this.doChangeStatus(form, OrderSelloutStatus.APPROVED, getRpcResponse(),
						(BindingResult) errors);
				getRpcResponse().addAttribute("message", "successful");

			}
		};

		return support.doSupport(request, null, errors, errorsProcessor);
	}

	@PutMapping(value = ControllerAction.APP_ORDER_SELLOUT_STATE_ACTION + "/delivered")
	@Validation(validators = { OrderSelloutExistValidator.class, OrderSelloutStateApprovedExistValidator.class,
			RetailerWarehouseTotalItemExistValidator.class })
	@NoRequiredAuth
	@Logged
	public ResponseEntity<?> stateDelivered(HttpServletRequest request, HttpServletResponse response,
			@RequestBody SelloutIdForm form, BindingResult errors) {
		AppControllerSupport support = new AppControllerCreationSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {

				OrderSelloutStatusController.this.doChangeStatus(form, OrderSelloutStatus.DELIVERED, getRpcResponse(),
						(BindingResult) errors);
				getRpcResponse().addAttribute("message", "successful");
			}
		};

		return support.doSupport(request, null, errors, errorsProcessor);
	}

	@PutMapping(value = ControllerAction.APP_ORDER_SELLOUT_STATE_ACTION + "/canceled")
	@Validation(validators = { OrderSelloutExistValidator.class })
	@NoRequiredAuth
	@Logged
	public ResponseEntity<?> stateCanceled(HttpServletRequest request, HttpServletResponse response,
			@RequestBody SelloutCancelForm form, BindingResult errors) {
		AppControllerSupport support = new AppControllerCreationSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {

				OrderSelloutStatusController.this.doChangeStatus(form, OrderSelloutStatus.CANCELED, getRpcResponse(),
						(BindingResult) errors);
				getRpcResponse().addAttribute("message", "successful");

			}
		};

		return support.doSupport(request, null, errors, errorsProcessor);
	}

	@PostMapping(value = ControllerAction.APP_ORDER_SELLOUT_STATE_ACTION + "/finish")
	@Validation(validators = { OrderSelloutExistValidator.class, OrderSelloutStateDeliveredExistValidator.class, RetailerExistedValidator.class })
	@NoRequiredAuth
	@Logged
	public ResponseEntity<?> stateFinish(HttpServletRequest request, HttpServletResponse response,
			@RequestBody SelloutIdForm form, BindingResult errors) {
		AppControllerSupport support = new AppControllerCreationSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {

				OrderSelloutStatusController.this.doChangeStatus(form, OrderSelloutStatus.FINISH, getRpcResponse(),
						(BindingResult) errors);
				getRpcResponse().addAttribute("message", "successful");

			}
		};

		return support.doSupport(request, null, errors, errorsProcessor);
	}

	@PostMapping(value = ControllerAction.APP_ORDER_SELLOUT_STATE_ACTION + "/return")
	@Validation(validators = { OrderSelloutExistValidator.class})
	@NoRequiredAuth
	@Logged
	public ResponseEntity<?> stateReturn(HttpServletRequest request, HttpServletResponse response,
			@RequestBody SelloutCancelForm form, BindingResult errors) {
		AppControllerSupport support = new AppControllerCreationSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {

				OrderSelloutStatusController.this.doChangeStatus(form, OrderSelloutStatus.RETURN, getRpcResponse(),
						(BindingResult) errors);
				getRpcResponse().addAttribute("message", "successful");

			}
		};

		return support.doSupport(request, null, errors, errorsProcessor);
	}

	@SuppressWarnings("rawtypes")
	protected void doChangeStatus(SelloutIdForm form, Enum state, RpcResponse rpcResponse, BindingResult errors) {

		OrderSellout orderSellout = this.osService.getActivated(form.getSelloutId());
		List<OrderSelloutItem> orderSelloutItems = this.osiService.getAllBySelloutId(form.getSelloutId());

		switch (state.toString()) {
		case "APPROVED":
			orderSellout.setOrderStatus(OrderSelloutStatus.APPROVED.toString());
			this.stateApproved(orderSellout, orderSelloutItems);
			break;
		case "DELIVERED":
			orderSellout.setOrderStatus(OrderSelloutStatus.DELIVERED.toString());
			this.stateDelivered(orderSellout, orderSelloutItems);
			break;
		case "CANCELED":
			String oldStatus = orderSellout.getOrderStatus();
			orderSellout.setCancelReason(((SelloutCancelForm)form).getReason());
			orderSellout.setOrderStatus(OrderSelloutStatus.CANCELED.toString());
			orderSellout.setFinishDate(LocalDateTime.now());
			this.stateCanceled(orderSellout, orderSelloutItems, oldStatus);
			break;
		case "FINISH":
			orderSellout.setOrderStatus(OrderSelloutStatus.FINISH.toString());
			orderSellout.setFinishDate(LocalDateTime.now());
			this.stateFinish(orderSellout, orderSelloutItems);
			break;
		case "RETURN":
		    orderSellout.setCancelReason(((SelloutCancelForm)form).getReason());
			orderSellout.setOrderStatus(OrderSelloutStatus.RETURN.toString());
			orderSellout.setFinishDate(LocalDateTime.now());
			this.stateReturn(orderSellout, orderSelloutItems);
			break;
		}

		this.osService.updatePartial(orderSellout);

		return;
	}

	private void stateApproved(OrderSellout orderSellout, List<OrderSelloutItem> orderSelloutItems) {
		List<RetailerWarehouseTotalItem> retailerWarehouseTotalItems = new ArrayList<RetailerWarehouseTotalItem>();

		Map<Integer, RetailerWarehouseTotalItem> productWarehouseMap = new LinkedHashMap<Integer, RetailerWarehouseTotalItem>();
		for (OrderSelloutItem item : orderSelloutItems) {
			RetailerWarehouseTotalItem retailerWarehouseTotalItem = this.rwhtiService
					.getByRetailerWarehouseTotalItemInfo(orderSellout.getRetailerId(), item.getProductId(),
							item.getProductVariationId());

			retailerWarehouseTotalItems.add(mapping(retailerWarehouseTotalItem, item));
			productWarehouseMap.put(item.getProductVariationId(), retailerWarehouseTotalItem);
		}
		
		List<OrderSelloutPromotionLimitationDetailReward> rewards = rewardService.getByOrderId(orderSellout.getId());
        if (CollectionUtils.isNotEmpty(rewards)) {
            for (OrderSelloutPromotionLimitationDetailReward reward : rewards) {
                RetailerWarehouseTotalItem retailerWarehouseTotalItem = productWarehouseMap.get(reward.getRewardProductVariationId()); 
                if (retailerWarehouseTotalItem == null) {
                    retailerWarehouseTotalItem = this.rwhtiService.getByRetailerWarehouseTotalItemInfo(
                            orderSellout.getRetailerId(),
                            reward.getRewardProductId(),
                            reward.getRewardProductVariationId());
                    retailerWarehouseTotalItems.add(retailerWarehouseTotalItem);
                }
                mappingOrderSelloutPromotionLimitationDetailReward(retailerWarehouseTotalItem, reward);
            }
        }

		this.rwhtiService.saveAll(retailerWarehouseTotalItems);
		this.createHistory(orderSellout, OrderSelloutStatus.NEW.toString(),
				OrderSelloutStatus.APPROVED.toString());
	}

	private RetailerWarehouseTotalItem mappingOrderSelloutPromotionLimitationDetailReward(RetailerWarehouseTotalItem retailerWarehouseTotalItem,
            OrderSelloutPromotionLimitationDetailReward reward) {
	    Integer amountAvailable = retailerWarehouseTotalItem.getAmountAvailable();
        retailerWarehouseTotalItem.setAmountAvailable(amountAvailable - reward.getRewardAmount());
        retailerWarehouseTotalItem.setAmountInOrders(retailerWarehouseTotalItem.getAmountInOrders() + reward.getRewardAmount());

        return retailerWarehouseTotalItem;
    }

    private void stateDelivered(OrderSellout orderSellout, List<OrderSelloutItem> orderSelloutItems) {
        this.createHistory(orderSellout, OrderSelloutStatus.APPROVED.toString(), OrderSelloutStatus.DELIVERED.toString());
        
        // 1. Get Retailer and warehouse
		RetailerWarehouse retailerWarehouse = this.rwhService.getByRetailerId(orderSellout.getRetailerId());
		Retailer retailer = this.retailerService.getActivated(orderSellout.getRetailerId());
		
		// 2. Create Export Ticket
		RetailerWarehouseExportTicket exportTicket = this.createRetailerWarehouseExportTicket(retailer, orderSellout, retailerWarehouse.getId());
		
		// 3. Summing Order item and reward items
		Map<Integer, Integer> itemCounter = new HashMap<>();
        Map<Integer, Object> productMap = new HashMap<>();
        ServiceUtils.getInstance().summingOrderItemAndRewardItem(rewardService, orderSellout, orderSelloutItems, itemCounter, productMap);
        
        // 4. Create Export Ticket Items
        List<RetailerWarehouseExportTicketItem> items = new ArrayList<RetailerWarehouseExportTicketItem>();
        for (Map.Entry<Integer, Object> entry : productMap.entrySet()) {
            RetailerWarehouseExportTicketItem item = this.createExportTicketItem(exportTicket, itemCounter, entry);
            items.add(item);
        }
        
        // 5. Update warehouse
        this.createOrUpdateRetailerWarehouseItem(exportTicket, retailerWarehouse, orderSellout, items);
        
    }

	private void createOrUpdateRetailerWarehouseItem(RetailerWarehouseExportTicket ticket,
	        RetailerWarehouse retailerWarehouse, OrderSellout orderSellout, List<RetailerWarehouseExportTicketItem> items) {
	    
	    List<RetailerWarehouseTotalItem> retailerWarehouseTotalItems = new ArrayList<RetailerWarehouseTotalItem>();
        List<RetailerWarehouseItem> retailerWarehouseItems = new ArrayList<RetailerWarehouseItem>();
        List<RetailerWarehouseItemHistory> itemHistory = new ArrayList<RetailerWarehouseItemHistory>();
        
	    for (RetailerWarehouseExportTicketItem item : items) {
	        // update RetailerWarehouseTotalItem
	        RetailerWarehouseTotalItem whTotalItem = this.rwhtiService.getByRetailerWarehouseTotalItemInfo(
                    orderSellout.getRetailerId(), item.getProductId(), item.getProductVariationId());
            
            whTotalItem.setAmountExport(whTotalItem.getAmountExport() + item.getAmount());
            whTotalItem.setAmountInOrders(whTotalItem.getAmountInOrders() - item.getAmount());
            whTotalItem.setAmount(whTotalItem.getAmount() - item.getAmount());
            retailerWarehouseTotalItems.add(whTotalItem);
            
            // update RetailerWarehouseItem
            RetailerWarehouseItem whItem = this.rwhItemService.getByRetailerWarehouseItemInfo(
                    orderSellout.getRetailerId(), retailerWarehouse.getId(), item.getProductId(), item.getProductVariationId());
            
            Integer oldAmount = whItem.getAmount();
            whItem.setAmount(oldAmount - item.getAmount());
            retailerWarehouseItems.add(whItem);

            // create RetailerWarehouseItemHistory
            RetailerWarehouseItemHistory history = createRetailerWarehouseItemHistory(orderSellout, whItem, item, ticket, whItem.getId(), oldAmount);
            itemHistory.add(history);
            
            // update RetailerWarehouseDailyRemain
            RetailerWarehouseDailyRemain rWhDailyRemain = this.rwhDailyRemainService.getInWarehouseByProductInfo(
                    retailerWarehouse.getId(), item.getProductId(), item.getProductVariationId());
            
            if (rWhDailyRemain == null) {
                rWhDailyRemain = new RetailerWarehouseDailyRemain();
                rWhDailyRemain.setRetailerId(retailerWarehouse.getRetailerId());
                rWhDailyRemain.setRetailerWarehouseId(retailerWarehouse.getId());
                rWhDailyRemain.setProductId(item.getProductId());
                rWhDailyRemain.setProductVariationId(item.getProductVariationId());
                rWhDailyRemain.setSku(item.getSku());
                rWhDailyRemain.setAmount(0);
                rWhDailyRemain.setAmountImport(0);
            }
            
            rWhDailyRemain.setAmountExport(rWhDailyRemain.getAmountExport() + item.getAmount());
            rWhDailyRemain.setAmount(rWhDailyRemain.getAmount() - item.getAmount());
            rWhDailyRemain = this.rwhDailyRemainService.saveOrUpdate(rWhDailyRemain);
        }
	    
        this.rwhtiService.saveAll(retailerWarehouseTotalItems);
        this.rwhItemService.saveAll(retailerWarehouseItems);
        this.retailerWarehouseItemHistoryService.saveAll(itemHistory);
    }

    private RetailerWarehouseExportTicketItem createExportTicketItem(RetailerWarehouseExportTicket ticket, Map<Integer, Integer> itemCounter,
            Entry<Integer, Object> entry) {
	    RetailerWarehouseExportTicketItem item = new RetailerWarehouseExportTicketItem();
        item.setRetailerId(ticket.getRetailerId());
        item.setRetailerWarehouseId(ticket.getRetailerWarehouseId());
        item.setExportTicketId(ticket.getId());
        item.setExportTicketCode(ticket.getExportTicketCode());
        item.setStatus(Status.ACTIVE);
        
        Object value = entry.getValue();
        if (value instanceof OrderSelloutItem) {
            OrderSelloutItem orderItem = (OrderSelloutItem) value;
            item.setProductId(orderItem.getProductId());
            item.setProductVariationId(orderItem.getProductVariationId());
            item.setSku(orderItem.getSku());
            
            item.setPrice(orderItem.getUnitPrice());
            item.setTotal(orderItem.getTotalCost());
        } else {
            OrderSelloutPromotionLimitationDetailReward reward = (OrderSelloutPromotionLimitationDetailReward) value;
            item.setProductId(reward.getRewardProductId());
            item.setProductVariationId(reward.getRewardProductVariationId());
            ProductVariation pv = productVariationService.get(reward.getRewardProductVariationId());
            item.setSku(pv.getSku());
            item.setAmount(reward.getRewardAmount());
            item.setPrice(reward.getUnitPrice());
            item.setTotal(reward.getRewardValue());
        }
      
        item.setAmount(itemCounter.get(item.getProductVariationId()));
        item = this.retailerWarehouseExportTicketItemService.create(item);
        
        return item;
    }

    private void stateCanceled(OrderSellout orderSellout, List<OrderSelloutItem> orderSelloutItems, String oldStatus) {
		List<RetailerWarehouseTotalItem> retailerWarehouseTotalItems = new ArrayList<RetailerWarehouseTotalItem>();

		if (!OrderSelloutStatus.NEW.toString().equalsIgnoreCase(oldStatus)) {
		    for (OrderSelloutItem item : orderSelloutItems) {
	            RetailerWarehouseTotalItem tItem = this.rwhtiService.getByRetailerWarehouseTotalItemInfo(
	                    orderSellout.getRetailerId(), item.getProductId(), item.getProductVariationId());
	            tItem.setAmountInOrders(tItem.getAmountInOrders() - item.getTotalAmount());
	            tItem.setAmountAvailable(tItem.getAmountAvailable() + item.getTotalAmount());
	            retailerWarehouseTotalItems.add(tItem);
	        }
		    
		    this.rwhtiService.saveAll(retailerWarehouseTotalItems);
		}
		
		this.createHistory(orderSellout, oldStatus, OrderSelloutStatus.CANCELED.toString());
	}

	private void stateFinish(OrderSellout orderSellout, List<OrderSelloutItem> orderSelloutItems) {
		this.createHistory(orderSellout, OrderSelloutStatus.DELIVERED.toString(),
				OrderSelloutStatus.FINISH.toString());
	}

	private void stateReturn(OrderSellout orderSellout, List<OrderSelloutItem> orderSelloutItems) {
		this.createHistory(orderSellout, OrderSelloutStatus.DELIVERED.toString(),
				OrderSelloutStatus.RETURN.toString());
		
		// CREATE IMPORT TICKET
		this.rwhImportTicketService.createFromOrderSelloutReturn(orderSellout);
	}

	private RetailerWarehouseExportTicket createRetailerWarehouseExportTicket(Retailer retailer,
			OrderSellout orderSellout, Integer retailerWarehouseId) {
		RetailerWarehouseExportTicket ticket = new RetailerWarehouseExportTicket();
		ticket.setRetailerId(retailer.getId());
		ticket.setRetailerWarehouseId(retailerWarehouseId);
		ticket.setExportTicketCode(RETAILER_WAREHOUSE_PREFIX + System.currentTimeMillis());
		ticket.setExportType(RetailerWarehouseExportTicket.EXPORT_TYPE_ORDER_SELLOUT);
		ticket.setExportPerson("Từ đơn hàng " + orderSellout.getOrderCode());
        ticket.setDescription("Từ đơn hàng " + orderSellout.getOrderCode());
		ticket.setExportDate(LocalDateTime.now());
		ticket.setTotal(orderSellout.getFinalCost());
		ticket.setExtraData(new LinkedHashMap<>());
		ticket.setStatus(Status.ACTIVE);
		ticket = this.rwhetService.create(ticket);
		
		String ticketCode = CommonUtils.generateCode(RETAILER_WAREHOUSE_PREFIX, retailer.getId(), EXPORT_TICKET_CODE_MIDDLE, ticket.getId());
		ticket.setExportTicketCode(ticketCode);
        this.rwhetService.updatePartial(ticket);
        
		return ticket;
	}

	private RetailerWarehouseItemHistory createRetailerWarehouseItemHistory(OrderSellout orderSellout,
			RetailerWarehouseItem retailerWarehouseItem, RetailerWarehouseExportTicketItem item,
			RetailerWarehouseExportTicket retailerWhExpTicket, Integer retailerWarehouseItemId, Integer oldAmount) {
		RetailerWarehouseItemHistory rwhItemHistory = new RetailerWarehouseItemHistory();
		rwhItemHistory.setRetailerId(orderSellout.getRetailerId());
		rwhItemHistory.setRetailerWarehouseId(retailerWarehouseItem.getRetailerWarehouseId());
		rwhItemHistory.setRetailerWarehouseItemId(retailerWarehouseItemId);
		rwhItemHistory.setProductId(retailerWarehouseItem.getProductId());
		rwhItemHistory.setProductVariationId(retailerWarehouseItem.getProductVariation().getId());
		rwhItemHistory.setSku(retailerWarehouseItem.getSku());
		rwhItemHistory.setChangeDate(LocalDateTime.now());
		rwhItemHistory.setChangeType(RetailerWarehouseItemHistory.CHANGE_TYPE_EXPORT);
		rwhItemHistory.setOldAmount(oldAmount);
		rwhItemHistory.setChangeAmount(item.getAmount());
		rwhItemHistory.setAmount(oldAmount - item.getAmount());
		rwhItemHistory.setExportTicketId(retailerWhExpTicket.getId());
		rwhItemHistory.setExportTicketCode(retailerWhExpTicket.getExportTicketCode());
		rwhItemHistory.setOrderSelloutId(orderSellout.getId());
		rwhItemHistory.setOrderSelloutCode(orderSellout.getOrderCode());
		rwhItemHistory.setExtraData(new LinkedHashMap<>());

		return rwhItemHistory;
	}
	
	private RetailerWarehouseTotalItem mapping(RetailerWarehouseTotalItem retailerWarehouseTotalItem,
			OrderSelloutItem item) {
		Integer amountAvailable = retailerWarehouseTotalItem.getAmountAvailable();
		retailerWarehouseTotalItem.setAmountAvailable(amountAvailable - item.getTotalAmount());
		retailerWarehouseTotalItem.setAmountInOrders(retailerWarehouseTotalItem.getAmountInOrders() + item.getTotalAmount());

		return retailerWarehouseTotalItem;
	}

	private OrderSelloutStateChangeHistory createHistory(OrderSellout orderSellout, String fromState,
			String toState) {
		OrderSelloutStateChangeHistory history = new OrderSelloutStateChangeHistory();
		history.setSelloutOrderId(orderSellout.getId());
		history.setSelloutOrderCode(orderSellout.getOrderCode());
		history.setChangeDate(LocalDateTime.now());
		history.setFromState(fromState);
		history.setToState(toState);
		history = this.osscHistoryService.create(history);
		return history;
	}

	@RequestMapping(value = ControllerAction.APP_ORDER_SELLOUT_COUNT_STATE_ACTION, method = RequestMethod.GET)
	@Logged
	public ResponseEntity<?> countAndGroupStateByRetailer(HttpServletRequest request, HttpServletResponse response) {
		RetailerSessionInfo info = (RetailerSessionInfo) RequestUtils.getInstance().getSessionInfo(request,
				Constants.APP_SESSION_INFO_KEY);
		final int retailerId = info.getRetailer().getId();
		AppControllerSupport support = new AppControllerSupport() {
			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				countAndGroupStateByRetailer(request, response, retailerId, errors, getRpcResponse());
			}
		};

		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	private void countAndGroupStateByRetailer(HttpServletRequest request, HttpServletResponse response, int retailerId,
			Errors errors, RpcResponse rpcResponse) {
		Map<String, Long> result = this.osService.countAndGroupByRetailerId(retailerId);
		rpcResponse.addAttributes(result);
	}

}
