package com.necpgame.backjava.service;

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
import java.util.UUID;

public interface PlayerOrderService {

    GetAvailableOrders200Response getAvailableOrders(PlayerOrderType type, Integer minPayment, PlayerOrderDifficulty difficulty, int page, int pageSize);

    PlayerOrder createPlayerOrder(CreateOrderRequest request);

    PlayerOrderDetailed getPlayerOrder(UUID orderId);

    void acceptPlayerOrder(UUID orderId, AcceptPlayerOrderRequest request);

    OrderCompletionResult completePlayerOrder(UUID orderId, CompletePlayerOrderRequest request);

    void cancelPlayerOrder(UUID orderId, CancelPlayerOrderRequest request);

    GetCreatedOrders200Response getCreatedOrders(UUID characterId, PlayerOrderStatus status);

    GetCreatedOrders200Response getExecutingOrders(UUID characterId);

    ExecuteOrderViaNPC200Response executeOrderViaNpc(UUID orderId, ExecuteOrderViaNPCRequest request);

    ExecutorReputation getExecutorReputation(UUID characterId);

    GetOrdersMarket200Response getOrdersMarket();
}


