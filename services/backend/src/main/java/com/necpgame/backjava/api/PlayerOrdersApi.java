package com.necpgame.backjava.api;

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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;

@Validated
@Tag(name = "Player Orders", description = "Управление заказами между игроками")
public interface PlayerOrdersApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    String ROOT = "/gameplay/social/player-orders";

    @Operation(
        operationId = "getAvailableOrders",
        summary = "Получить доступные заказы",
        tags = {"Orders"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Доступные заказы", content = @Content(schema = @Schema(implementation = GetAvailableOrders200Response.class)))
        },
        security = {@SecurityRequirement(name = "BearerAuth")}
    )
    @RequestMapping(method = RequestMethod.GET, value = ROOT, produces = {"application/json"})
    ResponseEntity<GetAvailableOrders200Response> getAvailableOrders(
        @Parameter(name = "type") @RequestParam(value = "type", required = false) String type,
        @Parameter(name = "min_payment") @RequestParam(value = "min_payment", required = false) Integer minPayment,
        @Parameter(name = "difficulty") @RequestParam(value = "difficulty", required = false) String difficulty,
        @Parameter(name = "page") @RequestParam(value = "page", required = false, defaultValue = "1") @Min(1) Integer page,
        @Parameter(name = "page_size") @RequestParam(value = "page_size", required = false, defaultValue = "20") @Min(1) @Max(100) Integer pageSize
    );

    @Operation(
        operationId = "createPlayerOrder",
        summary = "Создать заказ",
        tags = {"Orders"},
        responses = {
            @ApiResponse(responseCode = "201", description = "Заказ создан", content = @Content(schema = @Schema(implementation = PlayerOrder.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
        },
        security = {@SecurityRequirement(name = "BearerAuth")}
    )
    @RequestMapping(method = RequestMethod.POST, value = ROOT, consumes = {"application/json"}, produces = {"application/json"})
    ResponseEntity<PlayerOrder> createPlayerOrder(@Parameter(description = "", required = true) @Valid @RequestBody CreateOrderRequest createOrderRequest);

    @Operation(
        operationId = "getPlayerOrder",
        summary = "Получить детали заказа",
        tags = {"Orders"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Детали заказа", content = @Content(schema = @Schema(implementation = PlayerOrderDetailed.class))),
            @ApiResponse(responseCode = "404", description = "Заказ не найден")
        },
        security = {@SecurityRequirement(name = "BearerAuth")}
    )
    @RequestMapping(method = RequestMethod.GET, value = ROOT + "/{order_id}", produces = {"application/json"})
    ResponseEntity<PlayerOrderDetailed> getPlayerOrder(@Parameter(name = "order_id", required = true) @PathVariable("order_id") UUID orderId);

    @Operation(
        operationId = "acceptPlayerOrder",
        summary = "Принять заказ",
        tags = {"Order Execution"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Заказ принят"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "409", description = "Заказ невозможно принять")
        },
        security = {@SecurityRequirement(name = "BearerAuth")}
    )
    @RequestMapping(method = RequestMethod.POST, value = ROOT + "/{order_id}/accept", consumes = {"application/json"})
    ResponseEntity<Void> acceptPlayerOrder(
        @Parameter(name = "order_id", required = true) @PathVariable("order_id") UUID orderId,
        @Parameter(description = "", required = true) @Valid @RequestBody AcceptPlayerOrderRequest acceptPlayerOrderRequest
    );

    @Operation(
        operationId = "completePlayerOrder",
        summary = "Завершить заказ",
        tags = {"Order Execution"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Заказ завершен", content = @Content(schema = @Schema(implementation = OrderCompletionResult.class))),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав"),
            @ApiResponse(responseCode = "409", description = "Статус заказа не позволяет завершение")
        },
        security = {@SecurityRequirement(name = "BearerAuth")}
    )
    @RequestMapping(method = RequestMethod.POST, value = ROOT + "/{order_id}/complete", consumes = {"application/json"}, produces = {"application/json"})
    ResponseEntity<OrderCompletionResult> completePlayerOrder(
        @Parameter(name = "order_id", required = true) @PathVariable("order_id") UUID orderId,
        @Parameter(description = "", required = true) @Valid @RequestBody CompletePlayerOrderRequest completePlayerOrderRequest
    );

    @Operation(
        operationId = "cancelPlayerOrder",
        summary = "Отменить заказ",
        tags = {"Orders"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Заказ отменен"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав"),
            @ApiResponse(responseCode = "409", description = "Заказ уже завершен или отменен")
        },
        security = {@SecurityRequirement(name = "BearerAuth")}
    )
    @RequestMapping(method = RequestMethod.POST, value = ROOT + "/{order_id}/cancel", consumes = {"application/json"})
    ResponseEntity<Void> cancelPlayerOrder(
        @Parameter(name = "order_id", required = true) @PathVariable("order_id") UUID orderId,
        @Parameter(description = "", required = true) @Valid @RequestBody CancelPlayerOrderRequest cancelPlayerOrderRequest
    );

    @Operation(
        operationId = "getCreatedOrders",
        summary = "Получить заказы созданные персонажем",
        tags = {"Orders"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Созданные заказы", content = @Content(schema = @Schema(implementation = GetCreatedOrders200Response.class)))
        },
        security = {@SecurityRequirement(name = "BearerAuth")}
    )
    @RequestMapping(method = RequestMethod.GET, value = ROOT + "/character/{character_id}/created", produces = {"application/json"})
    ResponseEntity<GetCreatedOrders200Response> getCreatedOrders(
        @Parameter(name = "character_id", required = true) @PathVariable("character_id") UUID characterId,
        @Parameter(name = "status") @RequestParam(value = "status", required = false) String status
    );

    @Operation(
        operationId = "getExecutingOrders",
        summary = "Получить заказы, выполняемые персонажем",
        tags = {"Order Execution"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Выполняемые заказы", content = @Content(schema = @Schema(implementation = GetCreatedOrders200Response.class)))
        },
        security = {@SecurityRequirement(name = "BearerAuth")}
    )
    @RequestMapping(method = RequestMethod.GET, value = ROOT + "/character/{character_id}/executing", produces = {"application/json"})
    ResponseEntity<GetCreatedOrders200Response> getExecutingOrders(
        @Parameter(name = "character_id", required = true) @PathVariable("character_id") UUID characterId
    );

    @Operation(
        operationId = "executeOrderViaNPC",
        summary = "Выполнить заказ через нанятого NPC",
        tags = {"Order Execution"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Заказ принят NPC", content = @Content(schema = @Schema(implementation = ExecuteOrderViaNPC200Response.class))),
            @ApiResponse(responseCode = "409", description = "Заказ уже принят другим исполнителем")
        },
        security = {@SecurityRequirement(name = "BearerAuth")}
    )
    @RequestMapping(method = RequestMethod.POST, value = ROOT + "/{order_id}/execute-via-npc", consumes = {"application/json"}, produces = {"application/json"})
    ResponseEntity<ExecuteOrderViaNPC200Response> executeOrderViaNPC(
        @Parameter(name = "order_id", required = true) @PathVariable("order_id") UUID orderId,
        @Parameter(description = "", required = true) @Valid @RequestBody ExecuteOrderViaNPCRequest executeOrderViaNPCRequest
    );

    @Operation(
        operationId = "getExecutorReputation",
        summary = "Получить репутацию исполнителя",
        tags = {"Order Economy"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Репутация исполнителя", content = @Content(schema = @Schema(implementation = ExecutorReputation.class)))
        },
        security = {@SecurityRequirement(name = "BearerAuth")}
    )
    @RequestMapping(method = RequestMethod.GET, value = ROOT + "/reputation/{character_id}", produces = {"application/json"})
    ResponseEntity<ExecutorReputation> getExecutorReputation(
        @Parameter(name = "character_id", required = true) @PathVariable("character_id") UUID characterId
    );

    @Operation(
        operationId = "getOrdersMarket",
        summary = "Получить рыночную статистику заказов",
        tags = {"Order Economy"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Рыночная статистика", content = @Content(schema = @Schema(implementation = GetOrdersMarket200Response.class)))
        },
        security = {@SecurityRequirement(name = "BearerAuth")}
    )
    @RequestMapping(method = RequestMethod.GET, value = ROOT + "/market", produces = {"application/json"})
    ResponseEntity<GetOrdersMarket200Response> getOrdersMarket();
}


