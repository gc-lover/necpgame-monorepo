package com.necpgame.backjava.service.impl;

import com.necpgame.backjava.model.AttachmentClaimRequest;
import com.necpgame.backjava.model.AttachmentClaimResponse;
import com.necpgame.backjava.model.CODPaymentRequest;
import com.necpgame.backjava.model.CODPaymentResponse;
import com.necpgame.backjava.model.MailDetail;
import com.necpgame.backjava.model.MailFlagRequest;
import com.necpgame.backjava.model.MailHistoryResponse;
import com.necpgame.backjava.model.MailListResponse;
import com.necpgame.backjava.model.MailReturnRequest;
import com.necpgame.backjava.model.MailSendRequest;
import com.necpgame.backjava.model.MailSettings;
import com.necpgame.backjava.model.MailSettingsUpdateRequest;
import com.necpgame.backjava.model.MailStats;
import com.necpgame.backjava.model.SystemMailBatchRequest;
import com.necpgame.backjava.model.SystemMailRequest;
import com.necpgame.backjava.service.MailService;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    private UnsupportedOperationException error() {
        return new UnsupportedOperationException("Mail service is not implemented yet");
    }

    @Override
    public MailHistoryResponse mailHistoryGet(String type, Integer page, Integer pageSize) {
        throw error();
    }

    @Override
    public MailListResponse mailInboxGet(Boolean unread, Boolean attachments, String from, Boolean system, Integer page, Integer pageSize) {
        throw error();
    }

    @Override
    public AttachmentClaimResponse mailMailIdAttachmentsClaimPost(String mailId, AttachmentClaimRequest attachmentClaimRequest) {
        throw error();
    }

    @Override
    public CODPaymentResponse mailMailIdCodPayPost(String mailId, CODPaymentRequest coDPaymentRequest) {
        throw error();
    }

    @Override
    public Void mailMailIdDelete(String mailId) {
        throw error();
    }

    @Override
    public Void mailMailIdFlagPost(String mailId, MailFlagRequest mailFlagRequest) {
        throw error();
    }

    @Override
    public MailDetail mailMailIdGet(String mailId) {
        throw error();
    }

    @Override
    public Void mailMailIdReadPost(String mailId) {
        throw error();
    }

    @Override
    public Void mailMailIdResendPost(String mailId) {
        throw error();
    }

    @Override
    public Void mailMailIdReturnPost(String mailId, MailReturnRequest mailReturnRequest) {
        throw error();
    }

    @Override
    public MailListResponse mailOutboxGet(String status, Integer page, Integer pageSize) {
        throw error();
    }

    @Override
    public MailDetail mailPost(MailSendRequest mailSendRequest) {
        throw error();
    }

    @Override
    public MailSettings mailSettingsGet() {
        throw error();
    }

    @Override
    public Void mailSettingsPut(MailSettingsUpdateRequest mailSettingsUpdateRequest) {
        throw error();
    }

    @Override
    public MailStats mailStatsGet(String range) {
        throw error();
    }

    @Override
    public Void mailSystemBatchPost(SystemMailBatchRequest systemMailBatchRequest) {
        throw error();
    }

    @Override
    public Void mailSystemPost(SystemMailRequest systemMailRequest) {
        throw error();
    }
}


