package com.necpgame.backjava.service.impl;

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
import org.springframework.stereotype.Service;

@Service
public class InvestmentsServiceImpl implements InvestmentsService {

    private UnsupportedOperationException error() {
        return new UnsupportedOperationException("Investments service is not implemented yet");
    }

    @Override
    public GetInvestmentOpportunities200Response getInvestmentOpportunities(String type, String riskLevel, BigDecimal minRoi, int page, int pageSize) {
        throw error();
    }

    @Override
    public Investment invest(InvestRequest investRequest) {
        throw error();
    }

    @Override
    public InvestmentDetailed getInvestment(UUID investmentId) {
        throw error();
    }

    @Override
    public WithdrawInvestment200Response withdrawInvestment(UUID investmentId, WithdrawInvestmentRequest request) {
        throw error();
    }

    @Override
    public Portfolio getPortfolio(UUID characterId) {
        throw error();
    }

    @Override
    public PortfolioAnalysis getPortfolioAnalysis(UUID characterId) {
        throw error();
    }

    @Override
    public ROICalculation calculateRoi(CalculateROIRequest request) {
        throw error();
    }

    @Override
    public GetInvestmentFunds200Response getInvestmentFunds() {
        throw error();
    }
}


