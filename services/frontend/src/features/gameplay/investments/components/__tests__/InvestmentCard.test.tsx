import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { InvestmentCard } from '../InvestmentCard';

describe('InvestmentCard', () => {
  it('renders investment info', () => {
    const investment = {
      opportunityId: 'inv_001',
      name: 'Arasaka Corp',
      type: 'CORPORATE' as const,
      riskLevel: 'MEDIUM' as const,
      expectedRoi: 15,
      minInvestment: 10000,
      maxInvestment: 500000,
      fundedPercent: 42,
      dividends: 'Quarterly 3%' ,
    };
    render(<InvestmentCard investment={investment} />);
    expect(screen.getByText(/Arasaka Corp/i)).toBeInTheDocument();
    expect(screen.getByText(/CORPORATE/i)).toBeInTheDocument();
    expect(screen.getByText(/15% ROI/i)).toBeInTheDocument();
  });
});

