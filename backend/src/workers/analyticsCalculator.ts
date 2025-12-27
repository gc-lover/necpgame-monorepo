import { Worker, Job, Queue, QueueScheduler } from 'bullmq';
import { logger } from '../utils/logger';
import { analyticsService } from '../services/analyticsService';
import { sessionService } from '../services/sessionService';
import { WebSocketEvents } from '../types/websocket';
import { redis } from '../config/redis';
import { Op } from 'sequelize';

export interface AnalyticsCalculationJob {
  type: 'conversation_metrics' | 'sentiment_distribution' | 'agent_performance' | 'hourly_effectiveness' | 'payment_failures' | 'dashboard' | 'full_recalc';
  dateRange?: {
    startDate: Date;
    endDate: Date;
  };
  campaignId?: string;
  agentId?: string;
  forceRecalc?: boolean;
  priority?: 'high' | 'normal' | 'low';
}

export interface RecurringAnalyticsJob {
  type: 'hourly' | 'daily' | 'weekly' | 'monthly';
  priority?: 'high' | 'normal' | 'low';
}

export class AnalyticsCalculatorWorker {
  private worker: Worker;
  private recurringWorker: Worker;
  private analyticsQueue: Queue;
  private recurringQueue: Queue;
  private scheduler: QueueScheduler;

  // Cache for analytics results
  private analyticsCache = new Map<string, { data: any; timestamp: Date; ttl: number }>();

  constructor() {
    // Main analytics calculation queue
    this.analyticsQueue = new Queue('analytics-calculation', {
      connection: redis.duplicate(),
      defaultJobOptions: {
        removeOnComplete: 20,
        removeOnFail: 10,
        attempts: 3,
        backoff: {
          type: 'exponential',
          delay: 10000,
        },
      },
    });

    // Recurring analytics queue
    this.recurringQueue = new Queue('recurring-analytics', {
      connection: redis.duplicate(),
      defaultJobOptions: {
        removeOnComplete: 10,
        removeOnFail: 5,
        attempts: 2,
        backoff: {
          type: 'exponential',
          delay: 30000,
        },
      },
    });

    // Scheduler for recurring jobs
    this.scheduler = new QueueScheduler('recurring-analytics', {
      connection: redis.duplicate(),
    });

    this.initializeWorker();
    this.initializeRecurringWorker();
    this.scheduleRecurringJobs();
  }

  private initializeWorker(): void {
    this.worker = new Worker(
      'analytics-calculation',
      async (job: Job<AnalyticsCalculationJob>) => {
        return await this.processAnalyticsJob(job);
      },
      {
        connection: redis.duplicate(),
        concurrency: 3, // Process 3 analytics jobs concurrently
        limiter: {
          max: 5,
          duration: 1000, // Max 5 jobs per second
        },
      }
    );

    this.worker.on('completed', (job) => {
      logger.info('Analytics calculation job completed', {
        jobId: job.id,
        type: job.data.type,
        campaignId: job.data.campaignId,
        duration: job.finishedOn ? job.finishedOn - job.processedOn : 0
      });
    });

    this.worker.on('failed', (job, err) => {
      logger.error('Analytics calculation job failed', {
        jobId: job.id,
        type: job?.data?.type,
        error: err.message
      });
    });
  }

  private initializeRecurringWorker(): void {
    this.recurringWorker = new Worker(
      'recurring-analytics',
      async (job: Job<RecurringAnalyticsJob>) => {
        return await this.processRecurringAnalyticsJob(job);
      },
      {
        connection: redis.duplicate(),
        concurrency: 1, // Process recurring jobs sequentially
        limiter: {
          max: 2,
          duration: 1000, // Max 2 recurring jobs per second
        },
      }
    );

    this.recurringWorker.on('completed', (job) => {
      logger.info('Recurring analytics job completed', {
        jobId: job.id,
        type: job.data.type,
        duration: job.finishedOn ? job.finishedOn - job.processedOn : 0
      });
    });

    this.recurringWorker.on('failed', (job, err) => {
      logger.error('Recurring analytics job failed', {
        jobId: job.id,
        type: job?.data?.type,
        error: err.message
      });
    });
  }

  private scheduleRecurringJobs(): void {
    // Schedule hourly analytics updates
    this.recurringQueue.add(
      'hourly-analytics',
      { type: 'hourly', priority: 'normal' },
      {
        repeat: {
          every: 3600000, // 1 hour
          immediately: false
        },
        jobId: 'hourly-analytics-schedule',
        removeOnComplete: 5,
        removeOnFail: 3
      }
    );

    // Schedule daily analytics updates
    this.recurringQueue.add(
      'daily-analytics',
      { type: 'daily', priority: 'normal' },
      {
        repeat: {
          every: 86400000, // 24 hours
          immediately: false
        },
        jobId: 'daily-analytics-schedule',
        removeOnComplete: 5,
        removeOnFail: 3
      }
    );

    // Schedule weekly analytics updates
    this.recurringQueue.add(
      'weekly-analytics',
      { type: 'weekly', priority: 'normal' },
      {
        repeat: {
          every: 604800000, // 7 days
          immediately: false
        },
        jobId: 'weekly-analytics-schedule',
        removeOnComplete: 5,
        removeOnFail: 3
      }
    );

    logger.info('Recurring analytics jobs scheduled');
  }

  private async processAnalyticsJob(job: Job<AnalyticsCalculationJob>): Promise<any> {
    const { type, dateRange, campaignId, agentId, forceRecalc = false } = job.data;

    logger.info('Processing analytics job', {
      jobId: job.id,
      type,
      campaignId,
      agentId,
      dateRange,
      forceRecalc
    });

    try {
      const cacheKey = this.generateCacheKey(type, dateRange, campaignId, agentId);
      const cachedResult = this.getCachedResult(cacheKey);

      // Return cached result if available and not forcing recalc
      if (cachedResult && !forceRecalc) {
        logger.debug('Returning cached analytics result', { type, cacheKey });
        return cachedResult.data;
      }

      let result: any;

      switch (type) {
        case 'conversation_metrics':
          result = await this.calculateConversationMetrics(dateRange, campaignId);
          break;

        case 'sentiment_distribution':
          result = await this.calculateSentimentDistribution(dateRange, campaignId);
          break;

        case 'agent_performance':
          result = await this.calculateAgentPerformance(dateRange, agentId);
          break;

        case 'hourly_effectiveness':
          result = await this.calculateHourlyEffectiveness(dateRange, campaignId);
          break;

        case 'payment_failures':
          result = await this.calculatePaymentFailures(dateRange, campaignId);
          break;

        case 'dashboard':
          result = await this.calculateDashboardAnalytics(dateRange);
          break;

        case 'full_recalc':
          result = await this.performFullRecalculation(dateRange);
          break;

        default:
          throw new Error(`Unknown analytics type: ${type}`);
      }

      // Cache the result
      this.setCachedResult(cacheKey, result);

      // Emit WebSocket event for real-time updates
      const io = (global as any).io;
      if (io) {
        io.emitGlobal(WebSocketEvents.ANALYTICS_UPDATED, {
          type,
          data: result,
          timestamp: new Date()
        });
      }

      logger.info('Analytics calculation completed', {
        type,
        resultSize: JSON.stringify(result).length,
        calculatedAt: new Date()
      });

      return result;

    } catch (error) {
      logger.error('Analytics calculation failed', {
        type,
        error: error instanceof Error ? error.message : 'Unknown error'
      });

      // Emit error event
      const io = (global as any).io;
      if (io) {
        io.emitGlobal(WebSocketEvents.ERROR, {
          message: `Analytics calculation failed: ${error instanceof Error ? error.message : 'Unknown error'}`,
          code: 'ANALYTICS_CALCULATION_ERROR',
          timestamp: new Date()
        });
      }

      throw error;
    }
  }

  private async processRecurringAnalyticsJob(job: Job<RecurringAnalyticsJob>): Promise<any> {
    const { type } = job.data;

    logger.info('Processing recurring analytics job', {
      jobId: job.id,
      type
    });

    try {
      const dateRange = this.getDateRangeForRecurringType(type);
      const forceRecalc = true; // Always force recalc for recurring jobs

      switch (type) {
        case 'hourly':
          await this.addAnalyticsJob({
            type: 'hourly_effectiveness',
            dateRange,
            forceRecalc,
            priority: 'normal'
          });
          break;

        case 'daily':
          await this.addAnalyticsJob({
            type: 'dashboard',
            dateRange,
            forceRecalc,
            priority: 'normal'
          });
          await this.addAnalyticsJob({
            type: 'agent_performance',
            dateRange,
            forceRecalc,
            priority: 'normal'
          });
          break;

        case 'weekly':
          await this.addAnalyticsJob({
            type: 'full_recalc',
            dateRange,
            forceRecalc,
            priority: 'low'
          });
          break;

        case 'monthly':
          // Monthly aggregations can be added here
          break;

        default:
          throw new Error(`Unknown recurring analytics type: ${type}`);
      }

      logger.info('Recurring analytics jobs scheduled', { type });

      return {
        type,
        scheduledAt: new Date(),
        dateRange
      };

    } catch (error) {
      logger.error('Recurring analytics job failed', {
        type,
        error: error instanceof Error ? error.message : 'Unknown error'
      });
      throw error;
    }
  }

  private async calculateConversationMetrics(dateRange?: { startDate: Date; endDate: Date }, campaignId?: string): Promise<any> {
    const whereClause: any = {};

    if (dateRange) {
      whereClause.createdAt = {
        [Op.between]: [dateRange.startDate, dateRange.endDate]
      };
    }

    if (campaignId) {
      whereClause.campaignId = campaignId;
    }

    const sessions = await sessionService.listSessions({
      ...whereClause,
      limit: 10000 // Large limit for analytics
    });

    const totalConversations = sessions.length;
    const successfulConversations = sessions.filter(s => s.result === 'successful').length;
    const failedConversations = sessions.filter(s => s.result === 'failed').length;
    const avgDuration = sessions.reduce((sum, s) => sum + (s.duration || 0), 0) / totalConversations || 0;

    const successRate = totalConversations > 0 ? (successfulConversations / totalConversations) * 100 : 0;

    return {
      totalConversations,
      successfulConversations,
      failedConversations,
      successRate: Math.round(successRate * 100) / 100,
      avgDuration: Math.round(avgDuration),
      dateRange,
      campaignId,
      calculatedAt: new Date()
    };
  }

  private async calculateSentimentDistribution(dateRange?: { startDate: Date; endDate: Date }, campaignId?: string): Promise<any> {
    const whereClause: any = {};

    if (dateRange) {
      whereClause.createdAt = {
        [Op.between]: [dateRange.startDate, dateRange.endDate]
      };
    }

    if (campaignId) {
      whereClause.campaignId = campaignId;
    }

    const sessions = await sessionService.listSessions({
      ...whereClause,
      limit: 10000
    });

    const sentimentCounts = {
      positive: 0,
      neutral: 0,
      negative: 0
    };

    sessions.forEach(session => {
      if (session.sentimentAnalysis?.overall) {
        sentimentCounts[session.sentimentAnalysis.overall]++;
      }
    });

    const total = Object.values(sentimentCounts).reduce((sum, count) => sum + count, 0);

    return {
      distribution: {
        positive: {
          count: sentimentCounts.positive,
          percentage: total > 0 ? Math.round((sentimentCounts.positive / total) * 10000) / 100 : 0
        },
        neutral: {
          count: sentimentCounts.neutral,
          percentage: total > 0 ? Math.round((sentimentCounts.neutral / total) * 10000) / 100 : 0
        },
        negative: {
          count: sentimentCounts.negative,
          percentage: total > 0 ? Math.round((sentimentCounts.negative / total) * 10000) / 100 : 0
        }
      },
      totalSessions: total,
      dateRange,
      campaignId,
      calculatedAt: new Date()
    };
  }

  private async calculateAgentPerformance(dateRange?: { startDate: Date; endDate: Date }, agentId?: string): Promise<any> {
    // This would require agent/session relationships to be implemented
    // For now, return mock data structure
    const performance = {
      totalCalls: 0,
      successfulCalls: 0,
      avgCallDuration: 0,
      protocolComplianceRate: 0,
      customerSatisfactionScore: 0,
      conversionRate: 0,
      dateRange,
      agentId,
      calculatedAt: new Date()
    };

    // TODO: Implement actual agent performance calculation
    logger.debug('Agent performance calculation placeholder', { agentId, dateRange });

    return performance;
  }

  private async calculateHourlyEffectiveness(dateRange?: { startDate: Date; endDate: Date }, campaignId?: string): Promise<any> {
    const whereClause: any = {};

    if (dateRange) {
      whereClause.createdAt = {
        [Op.between]: [dateRange.startDate, dateRange.endDate]
      };
    }

    if (campaignId) {
      whereClause.campaignId = campaignId;
    }

    const sessions = await sessionService.listSessions({
      ...whereClause,
      limit: 10000
    });

    const hourlyStats = Array.from({ length: 24 }, (_, hour) => ({
      hour,
      totalCalls: 0,
      successfulCalls: 0,
      avgDuration: 0,
      successRate: 0
    }));

    sessions.forEach(session => {
      const hour = new Date(session.createdAt).getHours();
      const hourStat = hourlyStats[hour];

      hourStat.totalCalls++;
      if (session.result === 'successful') {
        hourStat.successfulCalls++;
      }
      hourStat.avgDuration += session.duration || 0;
    });

    // Calculate averages and rates
    hourlyStats.forEach(stat => {
      if (stat.totalCalls > 0) {
        stat.avgDuration = Math.round(stat.avgDuration / stat.totalCalls);
        stat.successRate = Math.round((stat.successfulCalls / stat.totalCalls) * 10000) / 100;
      }
    });

    return {
      hourlyStats,
      dateRange,
      campaignId,
      calculatedAt: new Date()
    };
  }

  private async calculatePaymentFailures(dateRange?: { startDate: Date; endDate: Date }, campaignId?: string): Promise<any> {
    // This would analyze payment-related conversations and failures
    // For now, return mock data structure
    const paymentFailures = {
      totalPaymentAttempts: 0,
      failedPayments: 0,
      failureRate: 0,
      commonFailureReasons: [],
      dateRange,
      campaignId,
      calculatedAt: new Date()
    };

    // TODO: Implement actual payment failure analysis
    logger.debug('Payment failure calculation placeholder', { campaignId, dateRange });

    return paymentFailures;
  }

  private async calculateDashboardAnalytics(dateRange?: { startDate: Date; endDate: Date }): Promise<any> {
    // Calculate all dashboard metrics at once
    const [conversationMetrics, sentimentDistribution, hourlyEffectiveness] = await Promise.all([
      this.calculateConversationMetrics(dateRange),
      this.calculateSentimentDistribution(dateRange),
      this.calculateHourlyEffectiveness(dateRange)
    ]);

    return {
      conversationMetrics,
      sentimentDistribution,
      hourlyEffectiveness,
      dateRange,
      calculatedAt: new Date()
    };
  }

  private async performFullRecalculation(dateRange?: { startDate: Date; endDate: Date }): Promise<any> {
    logger.info('Performing full analytics recalculation', { dateRange });

    // Clear cache
    this.analyticsCache.clear();

    // Recalculate all analytics types
    const results = await Promise.allSettled([
      this.calculateConversationMetrics(dateRange),
      this.calculateSentimentDistribution(dateRange),
      this.calculateAgentPerformance(dateRange),
      this.calculateHourlyEffectiveness(dateRange),
      this.calculatePaymentFailures(dateRange),
      this.calculateDashboardAnalytics(dateRange)
    ]);

    const successful = results.filter(r => r.status === 'fulfilled').length;
    const failed = results.filter(r => r.status === 'rejected').length;

    logger.info('Full recalculation completed', {
      total: results.length,
      successful,
      failed
    });

    return {
      fullRecalculation: true,
      results: results.map((result, index) => ({
        type: ['conversation_metrics', 'sentiment_distribution', 'agent_performance', 'hourly_effectiveness', 'payment_failures', 'dashboard'][index],
        success: result.status === 'fulfilled',
        data: result.status === 'fulfilled' ? result.value : null,
        error: result.status === 'rejected' ? result.reason : null
      })),
      dateRange,
      calculatedAt: new Date()
    };
  }

  private getDateRangeForRecurringType(type: RecurringAnalyticsJob['type']): { startDate: Date; endDate: Date } {
    const now = new Date();
    let startDate: Date;

    switch (type) {
      case 'hourly':
        startDate = new Date(now.getTime() - 60 * 60 * 1000); // Last hour
        break;
      case 'daily':
        startDate = new Date(now.getTime() - 24 * 60 * 60 * 1000); // Last 24 hours
        break;
      case 'weekly':
        startDate = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000); // Last 7 days
        break;
      case 'monthly':
        startDate = new Date(now.getTime() - 30 * 24 * 60 * 60 * 1000); // Last 30 days
        break;
      default:
        startDate = new Date(now.getTime() - 24 * 60 * 60 * 1000); // Default to last 24 hours
    }

    return {
      startDate,
      endDate: now
    };
  }

  private generateCacheKey(type: string, dateRange?: { startDate: Date; endDate: Date }, campaignId?: string, agentId?: string): string {
    const parts = [type];

    if (dateRange) {
      parts.push(dateRange.startDate.toISOString(), dateRange.endDate.toISOString());
    }

    if (campaignId) {
      parts.push(`campaign:${campaignId}`);
    }

    if (agentId) {
      parts.push(`agent:${agentId}`);
    }

    return parts.join('|');
  }

  private getCachedResult(cacheKey: string): { data: any; timestamp: Date; ttl: number } | null {
    const cached = this.analyticsCache.get(cacheKey);
    if (!cached) return null;

    const age = Date.now() - cached.timestamp.getTime();
    if (age > cached.ttl) {
      this.analyticsCache.delete(cacheKey);
      return null;
    }

    return cached;
  }

  private setCachedResult(cacheKey: string, data: any, ttl: number = 300000): void { // 5 minutes default TTL
    this.analyticsCache.set(cacheKey, {
      data,
      timestamp: new Date(),
      ttl
    });
  }

  // Public methods for adding jobs

  public async addAnalyticsJob(jobData: AnalyticsCalculationJob): Promise<Job<AnalyticsCalculationJob>> {
    const priority = jobData.priority === 'high' ? 1 : jobData.priority === 'low' ? 3 : 2;

    return await this.analyticsQueue.add(
      'calculate-analytics',
      jobData,
      {
        priority,
        removeOnComplete: 20,
        removeOnFail: 10
      }
    );
  }

  public async addRecurringAnalyticsJob(jobData: RecurringAnalyticsJob): Promise<Job<RecurringAnalyticsJob>> {
    const priority = jobData.priority === 'high' ? 1 : jobData.priority === 'low' ? 3 : 2;

    return await this.recurringQueue.add(
      'recurring-analytics',
      jobData,
      {
        priority,
        removeOnComplete: 10,
        removeOnFail: 5
      }
    );
  }

  // Queue management methods

  public async getQueueStats() {
    const analyticsStats = await this.analyticsQueue.getJobCounts();
    const recurringStats = await this.recurringQueue.getJobCounts();

    return {
      analyticsCalculation: analyticsStats,
      recurringAnalytics: recurringStats
    };
  }

  public async clearCache(): Promise<void> {
    this.analyticsCache.clear();
    logger.info('Analytics cache cleared');
  }

  public async pauseQueues(): Promise<void> {
    await Promise.all([
      this.analyticsQueue.pause(),
      this.recurringQueue.pause()
    ]);
    logger.info('Analytics queues paused');
  }

  public async resumeQueues(): Promise<void> {
    await Promise.all([
      this.analyticsQueue.resume(),
      this.recurringQueue.resume()
    ]);
    logger.info('Analytics queues resumed');
  }

  public async close(): Promise<void> {
    logger.info('Closing analytics calculator workers...');

    await this.worker.close();
    await this.recurringWorker.close();
    await this.scheduler.close();
    await this.analyticsQueue.close();
    await this.recurringQueue.close();

    this.analyticsCache.clear();

    logger.info('Analytics calculator workers closed');
  }
}
