import { apiClient } from './api';
import {
  AnalyticsData,
  ConversationMetrics,
  SentimentDistribution,
  AgentPerformance,
  HourlyEffectiveness,
  PaymentFailureData,
  DashboardData,
  DateRange
} from '@/types';
import { logger } from '@/utils/logger';

export class AnalyticsService {
  private static readonly BASE_URL = '/api/v1/analytics';

  /**
   * Get conversation analytics
   */
  static async getConversationAnalytics(dateRange?: DateRange, campaignId?: string): Promise<ConversationMetrics> {
    try {
      const params = new URLSearchParams();
      if (dateRange) {
        params.append('startDate', dateRange.startDate.toISOString());
        params.append('endDate', dateRange.endDate.toISOString());
      }
      if (campaignId) params.append('campaignId', campaignId);

      const response = await apiClient.get<ConversationMetrics>(
        `${this.BASE_URL}/conversations?${params.toString()}`
      );

      return response.data.data;
    } catch (error) {
      logger.error('Failed to fetch conversation analytics', { dateRange, campaignId, error });
      throw error;
    }
  }

  /**
   * Get sentiment distribution
   */
  static async getSentimentDistribution(dateRange?: DateRange, campaignId?: string): Promise<SentimentDistribution> {
    try {
      const params = new URLSearchParams();
      if (dateRange) {
        params.append('startDate', dateRange.startDate.toISOString());
        params.append('endDate', dateRange.endDate.toISOString());
      }
      if (campaignId) params.append('campaignId', campaignId);

      const response = await apiClient.get<SentimentDistribution>(
        `${this.BASE_URL}/sentiment-distribution?${params.toString()}`
      );

      return response.data.data;
    } catch (error) {
      logger.error('Failed to fetch sentiment distribution', { dateRange, campaignId, error });
      throw error;
    }
  }

  /**
   * Get agent performance metrics
   */
  static async getAgentPerformance(dateRange?: DateRange, agentId?: string): Promise<AgentPerformance[]> {
    try {
      const params = new URLSearchParams();
      if (dateRange) {
        params.append('startDate', dateRange.startDate.toISOString());
        params.append('endDate', dateRange.endDate.toISOString());
      }
      if (agentId) params.append('agentId', agentId);

      const response = await apiClient.get<AgentPerformance[]>(
        `${this.BASE_URL}/agent-performance?${params.toString()}`
      );

      return response.data.data;
    } catch (error) {
      logger.error('Failed to fetch agent performance', { dateRange, agentId, error });
      throw error;
    }
  }

  /**
   * Get collection strategy analytics
   */
  static async getCollectionStrategy(dateRange?: DateRange, campaignId?: string): Promise<{
    totalDebt: number;
    collectedAmount: number;
    collectionRate: number;
    averagePaymentDelay: number;
    paymentMethods: Record<string, number>;
    debtRanges: Array<{ range: string; count: number; collected: number }>;
  }> {
    try {
      const params = new URLSearchParams();
      if (dateRange) {
        params.append('startDate', dateRange.startDate.toISOString());
        params.append('endDate', dateRange.endDate.toISOString());
      }
      if (campaignId) params.append('campaignId', campaignId);

      const response = await apiClient.get<{
        totalDebt: number;
        collectedAmount: number;
        collectionRate: number;
        averagePaymentDelay: number;
        paymentMethods: Record<string, number>;
        debtRanges: Array<{ range: string; count: number; collected: number }>;
      }>(`${this.BASE_URL}/collection-strategy?${params.toString()}`);

      return response.data.data;
    } catch (error) {
      logger.error('Failed to fetch collection strategy analytics', { dateRange, campaignId, error });
      throw error;
    }
  }

  /**
   * Get audio processing statistics
   */
  static async getAudioProcessingStats(dateRange?: DateRange): Promise<{
    totalFiles: number;
    processedFiles: number;
    failedFiles: number;
    averageProcessingTime: number;
    transcriptionAccuracy: number;
    sentimentAccuracy: number;
    storageUsed: number;
  }> {
    try {
      const params = new URLSearchParams();
      if (dateRange) {
        params.append('startDate', dateRange.startDate.toISOString());
        params.append('endDate', dateRange.endDate.toISOString());
      }

      const response = await apiClient.get<{
        totalFiles: number;
        processedFiles: number;
        failedFiles: number;
        averageProcessingTime: number;
        transcriptionAccuracy: number;
        sentimentAccuracy: number;
        storageUsed: number;
      }>(`${this.BASE_URL}/audio-processing?${params.toString()}`);

      return response.data.data;
    } catch (error) {
      logger.error('Failed to fetch audio processing stats', { dateRange, error });
      throw error;
    }
  }

  /**
   * Get hourly effectiveness metrics
   */
  static async getHourlyEffectiveness(dateRange?: DateRange, campaignId?: string): Promise<HourlyEffectiveness[]> {
    try {
      const params = new URLSearchParams();
      if (dateRange) {
        params.append('startDate', dateRange.startDate.toISOString());
        params.append('endDate', dateRange.endDate.toISOString());
      }
      if (campaignId) params.append('campaignId', campaignId);

      const response = await apiClient.get<HourlyEffectiveness[]>(
        `${this.BASE_URL}/hourly-effectiveness?${params.toString()}`
      );

      return response.data.data;
    } catch (error) {
      logger.error('Failed to fetch hourly effectiveness', { dateRange, campaignId, error });
      throw error;
    }
  }

  /**
   * Get payment failure analytics
   */
  static async getPaymentFailures(dateRange?: DateRange, campaignId?: string): Promise<PaymentFailureData> {
    try {
      const params = new URLSearchParams();
      if (dateRange) {
        params.append('startDate', dateRange.startDate.toISOString());
        params.append('endDate', dateRange.endDate.toISOString());
      }
      if (campaignId) params.append('campaignId', campaignId);

      const response = await apiClient.get<PaymentFailureData>(
        `${this.BASE_URL}/payment-failures?${params.toString()}`
      );

      return response.data.data;
    } catch (error) {
      logger.error('Failed to fetch payment failures', { dateRange, campaignId, error });
      throw error;
    }
  }

  /**
   * Get dashboard analytics (combined metrics)
   */
  static async getDashboardAnalytics(dateRange?: DateRange): Promise<DashboardData> {
    try {
      const params = new URLSearchParams();
      if (dateRange) {
        params.append('startDate', dateRange.startDate.toISOString());
        params.append('endDate', dateRange.endDate.toISOString());
      }

      const response = await apiClient.get<DashboardData>(
        `${this.BASE_URL}/dashboard?${params.toString()}`
      );

      return response.data.data;
    } catch (error) {
      logger.error('Failed to fetch dashboard analytics', { dateRange, error });
      throw error;
    }
  }

  /**
   * Get full analytics data (all metrics combined)
   */
  static async getFullAnalytics(dateRange?: DateRange, campaignId?: string): Promise<AnalyticsData> {
    try {
      const params = new URLSearchParams();
      if (dateRange) {
        params.append('startDate', dateRange.startDate.toISOString());
        params.append('endDate', dateRange.endDate.toISOString());
      }
      if (campaignId) params.append('campaignId', campaignId);

      const response = await apiClient.get<AnalyticsData>(
        `${this.BASE_URL}/full?${params.toString()}`
      );

      return response.data.data;
    } catch (error) {
      logger.error('Failed to fetch full analytics', { dateRange, campaignId, error });
      throw error;
    }
  }

  /**
   * Export analytics report
   */
  static async exportAnalyticsReport(
    type: 'conversations' | 'sentiment' | 'agents' | 'collection' | 'audio' | 'full',
    format: 'json' | 'csv' | 'pdf' | 'xlsx' = 'json',
    dateRange?: DateRange,
    campaignId?: string
  ): Promise<any> {
    try {
      const params = new URLSearchParams();
      params.append('type', type);
      params.append('format', format);

      if (dateRange) {
        params.append('startDate', dateRange.startDate.toISOString());
        params.append('endDate', dateRange.endDate.toISOString());
      }
      if (campaignId) params.append('campaignId', campaignId);

      const response = await apiClient.get(
        `${this.BASE_URL}/export?${params.toString()}`,
        {
          responseType: format === 'json' ? 'json' : 'blob'
        }
      );

      return response.data.data || response.data;
    } catch (error) {
      logger.error('Failed to export analytics report', { type, format, error });
      throw error;
    }
  }

  /**
   * Get real-time analytics updates
   */
  static async getRealtimeAnalytics(): Promise<{
    activeSessions: number;
    callsToday: number;
    successRateToday: number;
    averageCallDuration: number;
    topPerformingAgents: Array<{ agentId: string; successRate: number }>;
    currentCampaigns: Array<{ id: string; status: string; progress: number }>;
  }> {
    try {
      const response = await apiClient.get<{
        activeSessions: number;
        callsToday: number;
        successRateToday: number;
        averageCallDuration: number;
        topPerformingAgents: Array<{ agentId: string; successRate: number }>;
        currentCampaigns: Array<{ id: string; status: string; progress: number }>;
      }>(`${this.BASE_URL}/realtime`);

      return response.data.data;
    } catch (error) {
      logger.error('Failed to fetch realtime analytics', { error });
      throw error;
    }
  }

  /**
   * Get analytics trends over time
   */
  static async getAnalyticsTrends(
    metric: 'conversations' | 'success_rate' | 'duration' | 'sentiment',
    period: 'hourly' | 'daily' | 'weekly' | 'monthly',
    dateRange: DateRange
  ): Promise<Array<{ date: string; value: number; change?: number }>> {
    try {
      const params = new URLSearchParams();
      params.append('metric', metric);
      params.append('period', period);
      params.append('startDate', dateRange.startDate.toISOString());
      params.append('endDate', dateRange.endDate.toISOString());

      const response = await apiClient.get<Array<{ date: string; value: number; change?: number }>>(
        `${this.BASE_URL}/trends?${params.toString()}`
      );

      return response.data.data;
    } catch (error) {
      logger.error('Failed to fetch analytics trends', { metric, period, dateRange, error });
      throw error;
    }
  }

  /**
   * Get comparative analytics (compare different time periods or campaigns)
   */
  static async getComparativeAnalytics(
    type: 'periods' | 'campaigns',
    items: Array<{ id: string; label: string; dateRange?: DateRange; campaignId?: string }>
  ): Promise<Array<{
    id: string;
    label: string;
    metrics: ConversationMetrics & SentimentDistribution;
  }>> {
    try {
      const response = await apiClient.post<Array<{
        id: string;
        label: string;
        metrics: ConversationMetrics & SentimentDistribution;
      }>>(`${this.BASE_URL}/compare`, { type, items });

      return response.data.data;
    } catch (error) {
      logger.error('Failed to fetch comparative analytics', { type, items, error });
      throw error;
    }
  }

  /**
   * Get predictive analytics insights
   */
  static async getPredictiveInsights(campaignId?: string): Promise<{
    predictedSuccessRate: number;
    recommendedActions: string[];
    riskFactors: Array<{ factor: string; impact: 'low' | 'medium' | 'high' }>;
    optimalCallTimes: Array<{ hour: number; expectedSuccessRate: number }>;
  }> {
    try {
      const params = new URLSearchParams();
      if (campaignId) params.append('campaignId', campaignId);

      const response = await apiClient.get<{
        predictedSuccessRate: number;
        recommendedActions: string[];
        riskFactors: Array<{ factor: string; impact: 'low' | 'medium' | 'high' }>;
        optimalCallTimes: Array<{ hour: number; expectedSuccessRate: number }>;
      }>(`${this.BASE_URL}/predictive?${params.toString()}`);

      return response.data.data;
    } catch (error) {
      logger.error('Failed to fetch predictive insights', { campaignId, error });
      throw error;
    }
  }
}

// Export singleton instance
export const analyticsService = AnalyticsService;
