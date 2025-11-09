package com.necpgame.backjava.service;

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
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;

@Validated
public interface InvestmentsService {

    GetInvestmentOpportunities200Response getInvestmentOpportunities(
        String type,
        String riskLevel,
        BigDecimal minRoi,
        int page,
        int pageSize
    );

    Investment invest(InvestRequest investRequest);

    InvestmentDetailed getInvestment(UUID investmentId);

    WithdrawInvestment200Response withdrawInvestment(UUID investmentId, WithdrawInvestmentRequest request);

    Portfolio getPortfolio(UUID characterId);

    PortfolioAnalysis getPortfolioAnalysis(UUID characterId);

    ROICalculation calculateRoi(CalculateROIRequest request);

    GetInvestmentFunds200Response getInvestmentFunds();
}


