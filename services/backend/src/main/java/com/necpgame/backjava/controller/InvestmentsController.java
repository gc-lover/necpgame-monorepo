package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.InvestmentsApi;
import com.necpgame.backjava.model.CalculateROIRequest;
import com.necpgame.backjava.model.GetInvestmentFunds200Response;
import com.necpgame.backjava.model.GetInvestmentOpportunities200Response;
import com.necpgame.backjava.model.InvestRequest;
import com.necpgame.backjava.model.Investment;
import com.necpgame.backjava.model.InvestmentDetailed;
import com.necpgame.backjava.model.Portfolio;
import com.necpgame.backjava.model.PortfolioAnalysis;
import com.necpgame.backjava.model.ROICalculation;
import com.necpgame.backjava.model.WithdrawInvestment200Response;
import com.necpgame.backjava.model.WithdrawInvestmentRequest;
import com.necpgame.backjava.service.InvestmentsService;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class InvestmentsController implements InvestmentsApi {

    private final InvestmentsService investmentsService;

    @Override
    public ResponseEntity<GetInvestmentOpportunities200Response> getInvestmentOpportunities(
        String type,
        String riskLevel,
        BigDecimal minRoi,
        Integer page,
        Integer pageSize
    ) {
        int resolvedPage = page != null ? page : 1;
        int resolvedPageSize = pageSize != null ? pageSize : 20;
        GetInvestmentOpportunities200Response response = investmentsService.getInvestmentOpportunities(
            type,
            riskLevel,
            minRoi,
            resolvedPage,
            resolvedPageSize
        );
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Investment> invest(InvestRequest investRequest) {
        Investment investment = investmentsService.invest(investRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(investment);
    }

    @Override
    public ResponseEntity<InvestmentDetailed> getInvestment(UUID investmentId) {
        InvestmentDetailed investment = investmentsService.getInvestment(investmentId);
        return ResponseEntity.ok(investment);
    }

    @Override
    public ResponseEntity<WithdrawInvestment200Response> withdrawInvestment(
        UUID investmentId,
        WithdrawInvestmentRequest withdrawInvestmentRequest
    ) {
        WithdrawInvestment200Response response = investmentsService.withdrawInvestment(investmentId, withdrawInvestmentRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Portfolio> getPortfolio(UUID characterId) {
        Portfolio portfolio = investmentsService.getPortfolio(characterId);
        return ResponseEntity.ok(portfolio);
    }

    @Override
    public ResponseEntity<PortfolioAnalysis> getPortfolioAnalysis(UUID characterId) {
        PortfolioAnalysis analysis = investmentsService.getPortfolioAnalysis(characterId);
        return ResponseEntity.ok(analysis);
    }

    @Override
    public ResponseEntity<ROICalculation> calculateROI(CalculateROIRequest calculateROIRequest) {
        ROICalculation calculation = investmentsService.calculateRoi(calculateROIRequest);
        return ResponseEntity.ok(calculation);
    }

    @Override
    public ResponseEntity<GetInvestmentFunds200Response> getInvestmentFunds() {
        GetInvestmentFunds200Response response = investmentsService.getInvestmentFunds();
        return ResponseEntity.ok(response);
    }
}



