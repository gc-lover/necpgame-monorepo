import { Worker, Job, Queue, QueueScheduler } from 'bullmq';
import { logger } from '../utils/logger';
import { campaignService } from '../services/campaignService';
import { sessionService } from '../services/sessionService';
import { callEngineService } from '../integrations/telephony/callEngineService';
import { WebSocketEvents } from '../types/websocket';
import { redis } from '../config/redis';
import { CampaignStatus, CampaignContact, CampaignRules } from '../types/campaign';

export interface CampaignExecutionJob {
  campaignId: string;
  action: 'start' | 'process_contacts' | 'check_status' | 'pause' | 'resume' | 'stop';
  contactIds?: string[];
  priority?: 'high' | 'normal' | 'low';
}

export interface ContactCallJob {
  campaignId: string;
  contactId: string;
  phoneNumber: string;
  customerName?: string;
  attemptNumber: number;
  maxAttempts: number;
  priority?: 'high' | 'normal' | 'low';
}

export class CampaignExecutorWorker {
  private worker: Worker;
  private contactWorker: Worker;
  private campaignExecutionQueue: Queue;
  private contactCallQueue: Queue;
  private scheduler: QueueScheduler;

  constructor() {
    // Campaign execution queue (for campaign lifecycle management)
    this.campaignExecutionQueue = new Queue('campaign-execution', {
      connection: redis.duplicate(),
      defaultJobOptions: {
        removeOnComplete: 20,
        removeOnFail: 10,
        attempts: 3,
        backoff: {
          type: 'exponential',
          delay: 5000,
        },
      },
    });

    // Contact call queue (for individual contact processing)
    this.contactCallQueue = new Queue('contact-calls', {
      connection: redis.duplicate(),
      defaultJobOptions: {
        removeOnComplete: 100,
        removeOnFail: 50,
        attempts: 2,
        backoff: {
          type: 'exponential',
          delay: 30000, // Longer delay for call retries
        },
      },
    });

    // Scheduler for recurring jobs
    this.scheduler = new QueueScheduler('campaign-execution', {
      connection: redis.duplicate(),
    });

    this.initializeWorker();
    this.initializeContactWorker();
  }

  private initializeWorker(): void {
    this.worker = new Worker(
      'campaign-execution',
      async (job: Job<CampaignExecutionJob>) => {
        return await this.processCampaignJob(job);
      },
      {
        connection: redis.duplicate(),
        concurrency: 2, // Process 2 campaigns concurrently
        limiter: {
          max: 5,
          duration: 1000, // Max 5 jobs per second
        },
      }
    );

    this.worker.on('completed', (job) => {
      logger.info('Campaign execution job completed', {
        jobId: job.id,
        campaignId: job.data.campaignId,
        action: job.data.action,
        duration: job.finishedOn ? job.finishedOn - job.processedOn : 0
      });
    });

    this.worker.on('failed', (job, err) => {
      logger.error('Campaign execution job failed', {
        jobId: job.id,
        campaignId: job?.data?.campaignId,
        action: job?.data?.action,
        error: err.message
      });
    });
  }

  private initializeContactWorker(): void {
    this.contactWorker = new Worker(
      'contact-calls',
      async (job: Job<ContactCallJob>) => {
        return await this.processContactCallJob(job);
      },
      {
        connection: redis.duplicate(),
        concurrency: 5, // Process up to 5 calls concurrently
        limiter: {
          max: 10,
          duration: 1000, // Max 10 call jobs per second
        },
      }
    );

    this.contactWorker.on('completed', (job) => {
      logger.info('Contact call job completed', {
        jobId: job.id,
        campaignId: job.data.campaignId,
        contactId: job.data.contactId,
        attemptNumber: job.data.attemptNumber,
        duration: job.finishedOn ? job.finishedOn - job.processedOn : 0
      });
    });

    this.contactWorker.on('failed', (job, err) => {
      logger.error('Contact call job failed', {
        jobId: job.id,
        campaignId: job?.data?.campaignId,
        contactId: job?.data?.contactId,
        attemptNumber: job?.data?.attemptNumber,
        error: err.message
      });
    });
  }

  private async processCampaignJob(job: Job<CampaignExecutionJob>): Promise<any> {
    const { campaignId, action, contactIds, priority = 'normal' } = job.data;

    logger.info('Processing campaign job', {
      jobId: job.id,
      campaignId,
      action,
      contactCount: contactIds?.length || 0
    });

    try {
      switch (action) {
        case 'start':
          return await this.startCampaign(campaignId);

        case 'process_contacts':
          return await this.processContacts(campaignId, contactIds);

        case 'check_status':
          return await this.checkCampaignStatus(campaignId);

        case 'pause':
          return await this.pauseCampaign(campaignId);

        case 'resume':
          return await this.resumeCampaign(campaignId);

        case 'stop':
          return await this.stopCampaign(campaignId);

        default:
          throw new Error(`Unknown campaign action: ${action}`);
      }
    } catch (error) {
      logger.error('Campaign job processing failed', {
        campaignId,
        action,
        error: error instanceof Error ? error.message : 'Unknown error'
      });

      // Emit error event
      const io = (global as any).io;
      if (io) {
        io.emitToCampaign(campaignId, WebSocketEvents.ERROR, {
          message: `Campaign ${action} failed: ${error instanceof Error ? error.message : 'Unknown error'}`,
          code: 'CAMPAIGN_EXECUTION_ERROR',
          timestamp: new Date()
        });
      }

      throw error;
    }
  }

  private async startCampaign(campaignId: string): Promise<any> {
    logger.info('Starting campaign', { campaignId });

    // Get campaign details
    const campaign = await campaignService.getCampaign(campaignId);
    if (!campaign) {
      throw new Error(`Campaign not found: ${campaignId}`);
    }

    if (campaign.status !== 'scheduled') {
      throw new Error(`Cannot start campaign with status: ${campaign.status}`);
    }

    // Update campaign status to running
    await campaignService.updateCampaignStatus(campaignId, 'running');

    // Get all contacts for the campaign
    const contacts = await campaignService.getCampaignContacts(campaignId);

    // Schedule contact processing
    await this.scheduleContactProcessing(campaignId, contacts);

    // Emit status update
    const io = (global as any).io;
    if (io) {
      io.emitToCampaign(campaignId, WebSocketEvents.CAMPAIGN_STATUS, {
        campaignId,
        status: 'running',
        progress: 0,
        totalContacts: contacts.length,
        processedContacts: 0,
        timestamp: new Date()
      });
    }

    // Schedule status check job
    await this.scheduleStatusCheck(campaignId);

    logger.info('Campaign started successfully', {
      campaignId,
      contactCount: contacts.length
    });

    return {
      campaignId,
      status: 'running',
      totalContacts: contacts.length,
      startedAt: new Date()
    };
  }

  private async processContacts(campaignId: string, contactIds?: string[]): Promise<any> {
    logger.debug('Processing contacts for campaign', { campaignId, contactCount: contactIds?.length });

    const campaign = await campaignService.getCampaign(campaignId);
    if (!campaign || campaign.status !== 'running') {
      logger.warn('Campaign not running, skipping contact processing', { campaignId, status: campaign?.status });
      return { skipped: true, reason: 'Campaign not running' };
    }

    let contacts: CampaignContact[];
    if (contactIds) {
      contacts = contactIds.map(id => ({ id } as CampaignContact));
    } else {
      contacts = await campaignService.getCampaignContacts(campaignId);
    }

    const processedContacts = [];
    const failedContacts = [];

    for (const contact of contacts) {
      try {
        await this.addContactCallJob({
          campaignId,
          contactId: contact.id,
          phoneNumber: contact.phoneNumber,
          customerName: contact.customerName,
          attemptNumber: 1,
          maxAttempts: campaign.rules?.maxAttempts || 3,
          priority: this.determineCallPriority(contact, campaign.rules)
        });
        processedContacts.push(contact.id);
      } catch (error) {
        logger.error('Failed to schedule contact call', {
          campaignId,
          contactId: contact.id,
          error: error instanceof Error ? error.message : 'Unknown error'
        });
        failedContacts.push({ contactId: contact.id, error: error instanceof Error ? error.message : 'Unknown error' });
      }
    }

    logger.info('Contact processing completed', {
      campaignId,
      totalContacts: contacts.length,
      processedContacts: processedContacts.length,
      failedContacts: failedContacts.length
    });

    return {
      campaignId,
      totalContacts: contacts.length,
      processedContacts: processedContacts.length,
      failedContacts: failedContacts.length,
      processedAt: new Date()
    };
  }

  private async checkCampaignStatus(campaignId: string): Promise<any> {
    logger.debug('Checking campaign status', { campaignId });

    const campaign = await campaignService.getCampaign(campaignId);
    if (!campaign) {
      throw new Error(`Campaign not found: ${campaignId}`);
    }

    const stats = await campaignService.getCampaignAnalytics(campaignId);
    const progress = stats.totalContacts > 0 ? (stats.processedContacts / stats.totalContacts) * 100 : 0;

    // Check if campaign should be completed
    if (stats.processedContacts >= stats.totalContacts && campaign.status === 'running') {
      await campaignService.updateCampaignStatus(campaignId, 'completed');

      // Emit completion event
      const io = (global as any).io;
      if (io) {
        io.emitToCampaign(campaignId, WebSocketEvents.CAMPAIGN_STATUS, {
          campaignId,
          status: 'completed',
          progress: 100,
          totalContacts: stats.totalContacts,
          processedContacts: stats.processedContacts,
          timestamp: new Date()
        });
      }

      logger.info('Campaign completed automatically', { campaignId, stats });
    } else {
      // Emit progress update
      const io = (global as any).io;
      if (io) {
        io.emitToCampaign(campaignId, WebSocketEvents.CAMPAIGN_STATUS, {
          campaignId,
          status: campaign.status,
          progress,
          totalContacts: stats.totalContacts,
          processedContacts: stats.processedContacts,
          timestamp: new Date()
        });
      }
    }

    return {
      campaignId,
      status: campaign.status,
      progress,
      stats,
      checkedAt: new Date()
    };
  }

  private async pauseCampaign(campaignId: string): Promise<any> {
    logger.info('Pausing campaign', { campaignId });

    await campaignService.updateCampaignStatus(campaignId, 'paused');

    // Pause all pending contact call jobs
    await this.contactCallQueue.pause();

    // Emit status update
    const io = (global as any).io;
    if (io) {
      io.emitToCampaign(campaignId, WebSocketEvents.CAMPAIGN_STATUS, {
        campaignId,
        status: 'paused',
        timestamp: new Date()
      });
    }

    return {
      campaignId,
      status: 'paused',
      pausedAt: new Date()
    };
  }

  private async resumeCampaign(campaignId: string): Promise<any> {
    logger.info('Resuming campaign', { campaignId });

    await campaignService.updateCampaignStatus(campaignId, 'running');

    // Resume contact call jobs
    await this.contactCallQueue.resume();

    // Emit status update
    const io = (global as any).io;
    if (io) {
      io.emitToCampaign(campaignId, WebSocketEvents.CAMPAIGN_STATUS, {
        campaignId,
        status: 'running',
        timestamp: new Date()
      });
    }

    return {
      campaignId,
      status: 'running',
      resumedAt: new Date()
    };
  }

  private async stopCampaign(campaignId: string): Promise<any> {
    logger.info('Stopping campaign', { campaignId });

    await campaignService.updateCampaignStatus(campaignId, 'cancelled');

    // Remove all pending jobs for this campaign
    await this.removeCampaignJobs(campaignId);

    // Emit status update
    const io = (global as any).io;
    if (io) {
      io.emitToCampaign(campaignId, WebSocketEvents.CAMPAIGN_STATUS, {
        campaignId,
        status: 'cancelled',
        timestamp: new Date()
      });
    }

    return {
      campaignId,
      status: 'cancelled',
      stoppedAt: new Date()
    };
  }

  private async processContactCallJob(job: Job<ContactCallJob>): Promise<any> {
    const { campaignId, contactId, phoneNumber, customerName, attemptNumber, maxAttempts } = job.data;

    logger.info('Processing contact call', {
      jobId: job.id,
      campaignId,
      contactId,
      phoneNumber,
      attemptNumber,
      maxAttempts
    });

    try {
      // Check if campaign is still running
      const campaign = await campaignService.getCampaign(campaignId);
      if (!campaign || campaign.status !== 'running') {
        logger.warn('Campaign not running, skipping call', { campaignId, contactId, status: campaign?.status });
        return { skipped: true, reason: 'Campaign not running' };
      }

      // Check if contact has already been processed successfully
      const existingSession = await sessionService.getSessionByContactAndCampaign(contactId, campaignId);
      if (existingSession && existingSession.result === 'successful') {
        logger.info('Contact already processed successfully, skipping', { contactId, campaignId });
        return { skipped: true, reason: 'Already processed successfully' };
      }

      // Check time windows and rules
      if (!this.isWithinTimeWindow(campaign.rules)) {
        logger.info('Outside time window, rescheduling call', { campaignId, contactId });
        await this.rescheduleCall(job.data, 3600000); // Reschedule in 1 hour
        return { rescheduled: true, reason: 'Outside time window' };
      }

      // Initiate call
      const callResult = await callEngineService.initiateCall({
        phoneNumber,
        customerName,
        campaignId,
        contactId,
        flowId: campaign.flowId
      });

      // Create session record
      const session = await sessionService.createSession({
        campaignId,
        contactId,
        phoneNumber,
        customerName,
        attemptNumber,
        callId: callResult.callId,
        status: 'ringing'
      });

      // Emit call events
      const io = (global as any).io;
      if (io) {
        io.emitToCampaign(campaignId, WebSocketEvents.CALL_RINGING, {
          sessionId: session.id,
          phoneNumber,
          customerName,
          campaignId
        });
      }

      logger.info('Contact call initiated successfully', {
        campaignId,
        contactId,
        sessionId: session.id,
        phoneNumber,
        attemptNumber
      });

      return {
        campaignId,
        contactId,
        sessionId: session.id,
        phoneNumber,
        attemptNumber,
        callId: callResult.callId,
        initiatedAt: new Date()
      };

    } catch (error) {
      logger.error('Contact call failed', {
        campaignId,
        contactId,
        phoneNumber,
        attemptNumber,
        error: error instanceof Error ? error.message : 'Unknown error'
      });

      // Handle retry logic
      if (attemptNumber < maxAttempts) {
        logger.info('Retrying contact call', {
          campaignId,
          contactId,
          attemptNumber: attemptNumber + 1,
          maxAttempts
        });

        await this.addContactCallJob({
          ...job.data,
          attemptNumber: attemptNumber + 1,
          priority: 'high' // Increase priority for retries
        });
      } else {
        // Mark contact as failed
        await campaignService.updateContactStatus(campaignId, contactId, 'failed', attemptNumber);

        logger.warn('Contact call failed permanently', {
          campaignId,
          contactId,
          phoneNumber,
          totalAttempts: attemptNumber
        });
      }

      throw error;
    }
  }

  private async scheduleContactProcessing(campaignId: string, contacts: CampaignContact[]): Promise<void> {
    // Split contacts into batches to avoid overwhelming the queue
    const batchSize = 50;
    for (let i = 0; i < contacts.length; i += batchSize) {
      const batch = contacts.slice(i, i + batchSize);
      const contactIds = batch.map(c => c.id);

      await this.addCampaignJob({
        campaignId,
        action: 'process_contacts',
        contactIds,
        priority: 'normal'
      });

      // Small delay between batches
      await new Promise(resolve => setTimeout(resolve, 500));
    }

    logger.info('Contact processing scheduled', {
      campaignId,
      totalContacts: contacts.length,
      batches: Math.ceil(contacts.length / batchSize)
    });
  }

  private async scheduleStatusCheck(campaignId: string): Promise<void> {
    // Schedule recurring status checks every 5 minutes
    await this.campaignExecutionQueue.add(
      'check-campaign-status',
      { campaignId, action: 'check_status' },
      {
        repeat: {
          every: 300000, // 5 minutes
          immediately: true
        },
        jobId: `status-check-${campaignId}`,
        removeOnComplete: 5,
        removeOnFail: 3
      }
    );

    logger.debug('Status check scheduled', { campaignId });
  }

  private determineCallPriority(contact: CampaignContact, rules?: CampaignRules): 'high' | 'normal' | 'low' {
    // Priority based on debt amount, payment history, etc.
    if (contact.debtAmount && contact.debtAmount > 10000) {
      return 'high';
    }
    if (contact.lastPaymentDate) {
      const daysSinceLastPayment = (Date.now() - new Date(contact.lastPaymentDate).getTime()) / (1000 * 60 * 60 * 24);
      if (daysSinceLastPayment > 90) {
        return 'high';
      }
    }
    return 'normal';
  }

  private isWithinTimeWindow(rules?: CampaignRules): boolean {
    if (!rules?.timeWindows) return true;

    const now = new Date();
    const currentHour = now.getHours();
    const currentDay = now.getDay(); // 0 = Sunday, 1 = Monday, etc.

    return rules.timeWindows.some(window => {
      const startHour = window.startTime.getHours();
      const endHour = window.endTime.getHours();
      const daysOfWeek = window.daysOfWeek;

      return daysOfWeek.includes(currentDay) &&
             currentHour >= startHour &&
             currentHour <= endHour;
    });
  }

  private async rescheduleCall(callData: ContactCallJob, delayMs: number): Promise<void> {
    await this.contactCallQueue.add(
      'contact-call',
      callData,
      {
        delay: delayMs,
        priority: callData.priority === 'high' ? 1 : callData.priority === 'low' ? 3 : 2,
        removeOnComplete: 100,
        removeOnFail: 50
      }
    );
  }

  private async removeCampaignJobs(campaignId: string): Promise<void> {
    // Remove all pending jobs for this campaign
    const campaignJobs = await this.campaignExecutionQueue.getJobs(['active', 'waiting', 'delayed']);
    const contactJobs = await this.contactCallQueue.getJobs(['active', 'waiting', 'delayed']);

    const jobsToRemove = [
      ...campaignJobs.filter(job => job.data.campaignId === campaignId),
      ...contactJobs.filter(job => job.data.campaignId === campaignId)
    ];

    for (const job of jobsToRemove) {
      await job.remove();
    }

    logger.info('Campaign jobs removed', { campaignId, removedJobs: jobsToRemove.length });
  }

  // Public methods for adding jobs

  public async addCampaignJob(jobData: CampaignExecutionJob): Promise<Job<CampaignExecutionJob>> {
    const priority = jobData.priority === 'high' ? 1 : jobData.priority === 'low' ? 3 : 2;

    return await this.campaignExecutionQueue.add(
      'campaign-execution',
      jobData,
      {
        priority,
        removeOnComplete: 20,
        removeOnFail: 10
      }
    );
  }

  public async addContactCallJob(jobData: ContactCallJob): Promise<Job<ContactCallJob>> {
    const priority = jobData.priority === 'high' ? 1 : jobData.priority === 'low' ? 3 : 2;

    return await this.contactCallQueue.add(
      'contact-call',
      jobData,
      {
        priority,
        delay: jobData.priority === 'high' ? 0 : 5000, // High priority calls start immediately
        removeOnComplete: 100,
        removeOnFail: 50
      }
    );
  }

  // Queue management methods

  public async getQueueStats() {
    const campaignStats = await this.campaignExecutionQueue.getJobCounts();
    const contactStats = await this.contactCallQueue.getJobCounts();

    return {
      campaignExecution: campaignStats,
      contactCalls: contactStats
    };
  }

  public async pauseAllQueues(): Promise<void> {
    await Promise.all([
      this.campaignExecutionQueue.pause(),
      this.contactCallQueue.pause()
    ]);
    logger.info('All campaign queues paused');
  }

  public async resumeAllQueues(): Promise<void> {
    await Promise.all([
      this.campaignExecutionQueue.resume(),
      this.contactCallQueue.resume()
    ]);
    logger.info('All campaign queues resumed');
  }

  public async close(): Promise<void> {
    logger.info('Closing campaign executor workers...');

    await this.worker.close();
    await this.contactWorker.close();
    await this.scheduler.close();
    await this.campaignExecutionQueue.close();
    await this.contactCallQueue.close();

    logger.info('Campaign executor workers closed');
  }
}
