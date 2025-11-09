package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.PlayerOrdersApi;
import com.necpgame.backjava.entity.enums.PlayerOrderDifficulty;
import com.necpgame.backjava.entity.enums.PlayerOrderStatus;
import com.necpgame.backjava.entity.enums.PlayerOrderType;
import com.necpgame.backjava.model.AcceptPlayerOrderRequest;
import com.necpgame.backjava.model.CancelPlayerOrderRequest;
import com.necpgame.backjava.model.CompletePlayerOrderRequest;
import com.necpgame.backjava.model.CreateOrderRequest;
import com.necpgame.backjava.model.ExecuteOrderViaNPC200Response;
import com.necpgame.backjava.model.ExecuteOrderViaNPCRequest;
import com.necpgame.backjava.model.ExecutorReputation;
import com.necpgame.backjava.model.GetAvailableOrders200Response;
import com.necpgame.backjava.model.GetCreatedOrders200Response;
import com.necpgame.backjava.model.GetOrdersMarket200Response;
import com.necpgame.backjava.model.OrderCompletionResult;
import com.necpgame.backjava.model.PlayerOrder;
import com.necpgame.backjava.model.PlayerOrderDetailed;
import com.necpgame.backjava.service.PlayerOrderService;
import jakarta.validation.Valid;
import java.util.Locale;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayerOrdersController implements PlayerOrdersApi {

    private final PlayerOrderService playerOrderService;

    public PlayerOrdersController(PlayerOrderService playerOrderService) {
        this.playerOrderService = playerOrderService;
    }

    @Override
    public ResponseEntity<GetAvailableOrders200Response> getAvailableOrders(String type, Integer minPayment, String difficulty, Integer page, Integer pageSize) {
        PlayerOrderType orderType = parseEnum(type, PlayerOrderType.class);
        PlayerOrderDifficulty orderDifficulty = parseEnum(difficulty, PlayerOrderDifficulty.class);
        GetAvailableOrders200Response response = playerOrderService.getAvailableOrders(orderType, minPayment, orderDifficulty, page, pageSize);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<PlayerOrder> createPlayerOrder(@Valid CreateOrderRequest createOrderRequest) {
        PlayerOrder order = playerOrderService.createPlayerOrder(createOrderRequest);
        return ResponseEntity.status(201).body(order);
    }

    @Override
    public ResponseEntity<PlayerOrderDetailed> getPlayerOrder(UUID orderId) {
        PlayerOrderDetailed order = playerOrderService.getPlayerOrder(orderId);
        return ResponseEntity.ok(order);
    }

    @Override
    public ResponseEntity<Void> acceptPlayerOrder(UUID orderId, @Valid AcceptPlayerOrderRequest acceptPlayerOrderRequest) {
        playerOrderService.acceptPlayerOrder(orderId, acceptPlayerOrderRequest);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<OrderCompletionResult> completePlayerOrder(UUID orderId, @Valid CompletePlayerOrderRequest completePlayerOrderRequest) {
        OrderCompletionResult result = playerOrderService.completePlayerOrder(orderId, completePlayerOrderRequest);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Void> cancelPlayerOrder(UUID orderId, @Valid CancelPlayerOrderRequest cancelPlayerOrderRequest) {
        playerOrderService.cancelPlayerOrder(orderId, cancelPlayerOrderRequest);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<GetCreatedOrders200Response> getCreatedOrders(UUID characterId, String status) {
        PlayerOrderStatus orderStatus = parseEnum(status, PlayerOrderStatus.class);
        GetCreatedOrders200Response response = playerOrderService.getCreatedOrders(characterId, orderStatus);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<GetCreatedOrders200Response> getExecutingOrders(UUID characterId) {
        GetCreatedOrders200Response response = playerOrderService.getExecutingOrders(characterId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ExecuteOrderViaNPC200Response> executeOrderViaNPC(UUID orderId, @Valid ExecuteOrderViaNPCRequest executeOrderViaNPCRequest) {
        ExecuteOrderViaNPC200Response response = playerOrderService.executeOrderViaNpc(orderId, executeOrderViaNPCRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ExecutorReputation> getExecutorReputation(UUID characterId) {
        ExecutorReputation reputation = playerOrderService.getExecutorReputation(characterId);
        return ResponseEntity.ok(reputation);
    }

    @Override
    public ResponseEntity<GetOrdersMarket200Response> getOrdersMarket() {
        GetOrdersMarket200Response response = playerOrderService.getOrdersMarket();
        return ResponseEntity.ok(response);
    }

    private <E extends Enum<E>> E parseEnum(String value, Class<E> enumType) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        try {
            return Enum.valueOf(enumType, value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}


