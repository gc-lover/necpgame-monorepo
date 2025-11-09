package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.TechnicalApi;
import com.necpgame.backjava.model.GetNotifications200Response;
import com.necpgame.backjava.model.GetResetHistory200Response;
import com.necpgame.backjava.model.MarkAllNotificationsReadRequest;
import com.necpgame.backjava.model.PlayerResetStatus;
import com.necpgame.backjava.model.ResetExecutionResult;
import com.necpgame.backjava.model.ResetSchedule;
import com.necpgame.backjava.model.ResetStatusResponse;
import com.necpgame.backjava.model.ResetTypeStatus;
import com.necpgame.backjava.model.SendNotification200Response;
import com.necpgame.backjava.model.SendNotificationRequest;
import com.necpgame.backjava.model.TriggerResetRequest;
import com.necpgame.backjava.model.UpdateScheduleRequest;
import com.necpgame.backjava.service.TechnicalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TechnicalController implements TechnicalApi {

    private final TechnicalService technicalService;

    @Override
    public ResponseEntity<GetNotifications200Response> getNotifications(String playerId, Boolean unreadOnly, @Nullable String type, Integer page, Integer limit) {
        return ResponseEntity.ok(technicalService.getNotifications(playerId, unreadOnly, type, page, limit));
    }

    @Override
    public ResponseEntity<Object> markAllNotificationsRead(MarkAllNotificationsReadRequest markAllNotificationsReadRequest) {
        return ResponseEntity.ok(technicalService.markAllNotificationsRead(markAllNotificationsReadRequest));
    }

    @Override
    public ResponseEntity<Object> markNotificationRead(String notificationId) {
        return ResponseEntity.ok(technicalService.markNotificationRead(notificationId));
    }

    @Override
    public ResponseEntity<SendNotification200Response> sendNotification(SendNotificationRequest sendNotificationRequest) {
        return ResponseEntity.ok(technicalService.sendNotification(sendNotificationRequest));
    }

    @Override
    public ResponseEntity<PlayerResetStatus> getPlayerResetStatus(UUID playerId) {
        return ResponseEntity.ok(technicalService.getPlayerResetStatus(playerId));
    }

    @Override
    public ResponseEntity<GetResetHistory200Response> getResetHistory(@Nullable String resetType, Integer days) {
        return ResponseEntity.ok(technicalService.getResetHistory(resetType, days));
    }

    @Override
    public ResponseEntity<ResetSchedule> getResetSchedule() {
        return ResponseEntity.ok(technicalService.getResetSchedule());
    }

    @Override
    public ResponseEntity<ResetStatusResponse> getResetStatus() {
        return ResponseEntity.ok(technicalService.getResetStatus());
    }

    @Override
    public ResponseEntity<ResetTypeStatus> getResetTypeStatus(String resetType) {
        return ResponseEntity.ok(technicalService.getResetTypeStatus(resetType));
    }

    @Override
    public ResponseEntity<ResetExecutionResult> triggerReset(TriggerResetRequest triggerResetRequest) {
        return ResponseEntity.ok(technicalService.triggerReset(triggerResetRequest));
    }

    @Override
    public ResponseEntity<Void> updateResetSchedule(UpdateScheduleRequest updateScheduleRequest) {
        technicalService.updateResetSchedule(updateScheduleRequest);
        return ResponseEntity.ok().build();
    }
}

