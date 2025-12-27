import { Worker, Job, Queue } from 'bullmq';
import { logger } from '../utils/logger';
import { audioProcessingService } from '../services/audioProcessingService';
import { sessionService } from '../services/sessionService';
import { WebSocketEvents } from '../types/websocket';
import { redis } from '../config/redis';
import { storageService } from '../services/storageService';

export interface AudioProcessingJob {
  sessionId: string;
  audioFileId: string;
  audioUrl?: string;
  priority?: 'high' | 'normal' | 'low';
  retryCount?: number;
  maxRetries?: number;
}

export interface BatchAudioProcessingJob {
  jobs: AudioProcessingJob[];
  batchId: string;
  priority?: 'high' | 'normal' | 'low';
}

export class AudioProcessorWorker {
  private worker: Worker;
  private batchWorker: Worker;
  private audioProcessingQueue: Queue;
  private batchProcessingQueue: Queue;

  constructor() {
    // Main audio processing queue
    this.audioProcessingQueue = new Queue('audio-processing', {
      connection: redis.duplicate(),
      defaultJobOptions: {
        removeOnComplete: 50,
        removeOnFail: 20,
        attempts: 3,
        backoff: {
          type: 'exponential',
          delay: 5000,
        },
      },
    });

    // Batch processing queue
    this.batchProcessingQueue = new Queue('batch-audio-processing', {
      connection: redis.duplicate(),
      defaultJobOptions: {
        removeOnComplete: 10,
        removeOnFail: 5,
        attempts: 2,
        backoff: {
          type: 'exponential',
          delay: 10000,
        },
      },
    });

    this.initializeWorker();
    this.initializeBatchWorker();
  }

  private initializeWorker(): void {
    this.worker = new Worker(
      'audio-processing',
      async (job: Job<AudioProcessingJob>) => {
        return await this.processAudioJob(job);
      },
      {
        connection: redis.duplicate(),
        concurrency: 3, // Process 3 audio files concurrently
        limiter: {
          max: 10,
          duration: 1000, // Max 10 jobs per second
        },
      }
    );

    this.worker.on('completed', (job) => {
      logger.info('Audio processing job completed', {
        jobId: job.id,
        sessionId: job.data.sessionId,
        audioFileId: job.data.audioFileId,
        duration: job.finishedOn ? job.finishedOn - job.processedOn : 0
      });
    });

    this.worker.on('failed', (job, err) => {
      logger.error('Audio processing job failed', {
        jobId: job.id,
        sessionId: job?.data?.sessionId,
        audioFileId: job?.data?.audioFileId,
        error: err.message,
        attemptsMade: job?.attemptsMade,
        attemptsRemaining: job?.opts.attempts - (job?.attemptsMade || 0)
      });
    });

    this.worker.on('stalled', (job) => {
      logger.warn('Audio processing job stalled', {
        jobId: job.id,
        sessionId: job?.data?.sessionId
      });
    });
  }

  private initializeBatchWorker(): void {
    this.batchWorker = new Worker(
      'batch-audio-processing',
      async (job: Job<BatchAudioProcessingJob>) => {
        return await this.processBatchAudioJob(job);
      },
      {
        connection: redis.duplicate(),
        concurrency: 1, // Process one batch at a time
        limiter: {
          max: 2,
          duration: 1000, // Max 2 batches per second
        },
      }
    );

    this.batchWorker.on('completed', (job) => {
      logger.info('Batch audio processing job completed', {
        jobId: job.id,
        batchId: job.data.batchId,
        jobCount: job.data.jobs.length,
        duration: job.finishedOn ? job.finishedOn - job.processedOn : 0
      });
    });

    this.batchWorker.on('failed', (job, err) => {
      logger.error('Batch audio processing job failed', {
        jobId: job.id,
        batchId: job?.data?.batchId,
        error: err.message,
        attemptsMade: job?.attemptsMade
      });
    });
  }

  private async processAudioJob(job: Job<AudioProcessingJob>): Promise<any> {
    const { sessionId, audioFileId, audioUrl, priority = 'normal', retryCount = 0, maxRetries = 3 } = job.data;

    logger.info('Processing audio job', {
      jobId: job.id,
      sessionId,
      audioFileId,
      priority,
      retryCount
    });

    try {
      // Get audio file URL if not provided
      let audioFileUrl = audioUrl;
      if (!audioFileUrl) {
        const audioFile = await storageService.getFileMetadata(audioFileId);
        audioFileUrl = await storageService.generateSignedUrl(audioFileId, 3600); // 1 hour expiry
      }

      if (!audioFileUrl) {
        throw new Error(`Audio file not found: ${audioFileId}`);
      }

      // Step 1: Extract transcription
      logger.debug('Extracting transcription', { sessionId, audioFileId });
      const transcription = await audioProcessingService.extractTranscription(audioFileUrl);

      // Step 2: Analyze sentiment
      logger.debug('Analyzing sentiment', { sessionId, audioFileId });
      const sentimentAnalysis = await audioProcessingService.analyzeSentiment(transcription);

      // Step 3: Detect protocol compliance
      logger.debug('Detecting protocol compliance', { sessionId, audioFileId });
      const protocolCompliance = await audioProcessingService.detectProtocolCompliance(transcription);

      // Step 4: Generate summary
      logger.debug('Generating summary', { sessionId, audioFileId });
      const summary = await audioProcessingService.generateSummary(transcription, sentimentAnalysis);

      // Step 5: Update session with processing results
      await sessionService.updateSession(sessionId, {
        transcription,
        sentimentAnalysis,
        protocolCompliance,
        summary,
        audioProcessingStatus: 'completed',
        audioProcessingCompletedAt: new Date()
      });

      // Step 6: Emit WebSocket event for real-time updates
      const io = (global as any).io;
      if (io) {
        io.emitToUser('*', WebSocketEvents.AUDIO_PROCESSED, {
          sessionId,
          transcription,
          sentiment: sentimentAnalysis,
          protocolCompliance,
          summary,
          timestamp: new Date()
        });
      }

      logger.info('Audio processing completed successfully', {
        sessionId,
        audioFileId,
        transcriptionLength: transcription?.length || 0,
        sentiment: sentimentAnalysis?.overall,
        protocolCompliance,
        summaryLength: summary?.length || 0
      });

      return {
        sessionId,
        audioFileId,
        transcription,
        sentimentAnalysis,
        protocolCompliance,
        summary,
        processedAt: new Date()
      };

    } catch (error) {
      logger.error('Audio processing failed', {
        sessionId,
        audioFileId,
        error: error instanceof Error ? error.message : 'Unknown error',
        retryCount
      });

      // Update session with error status
      await sessionService.updateSession(sessionId, {
        audioProcessingStatus: 'failed',
        audioProcessingError: error instanceof Error ? error.message : 'Unknown error'
      });

      // Retry logic
      if (retryCount < maxRetries) {
        logger.info('Retrying audio processing', {
          sessionId,
          audioFileId,
          retryCount: retryCount + 1,
          maxRetries
        });

        // Add retry job with higher priority
        await this.addJob({
          ...job.data,
          priority: 'high',
          retryCount: retryCount + 1
        });
      }

      throw error;
    }
  }

  private async processBatchAudioJob(job: Job<BatchAudioProcessingJob>): Promise<any> {
    const { jobs, batchId, priority = 'normal' } = job.data;

    logger.info('Processing batch audio job', {
      jobId: job.id,
      batchId,
      jobCount: jobs.length,
      priority
    });

    const results = [];
    const errors = [];

    // Process jobs sequentially to avoid overwhelming the system
    for (const audioJob of jobs) {
      try {
        const result = await this.processAudioJob({
          ...job,
          data: audioJob
        } as Job<AudioProcessingJob>);
        results.push(result);
      } catch (error) {
        logger.error('Batch audio job failed', {
          batchId,
          sessionId: audioJob.sessionId,
          error: error instanceof Error ? error.message : 'Unknown error'
        });
        errors.push({
          sessionId: audioJob.sessionId,
          error: error instanceof Error ? error.message : 'Unknown error'
        });
      }

      // Small delay between jobs to prevent overwhelming
      await new Promise(resolve => setTimeout(resolve, 100));
    }

    logger.info('Batch audio processing completed', {
      batchId,
      totalJobs: jobs.length,
      successfulJobs: results.length,
      failedJobs: errors.length
    });

    return {
      batchId,
      totalJobs: jobs.length,
      successfulJobs: results.length,
      failedJobs: errors.length,
      results,
      errors,
      processedAt: new Date()
    };
  }

  // Public methods for adding jobs to the queue

  public async addJob(jobData: AudioProcessingJob): Promise<Job<AudioProcessingJob>> {
    const priority = jobData.priority === 'high' ? 1 : jobData.priority === 'low' ? 3 : 2;

    return await this.audioProcessingQueue.add(
      'process-audio',
      jobData,
      {
        priority,
        delay: jobData.priority === 'high' ? 0 : 1000, // High priority jobs start immediately
        removeOnComplete: 50,
        removeOnFail: 20
      }
    );
  }

  public async addBatchJob(jobData: BatchAudioProcessingJob): Promise<Job<BatchAudioProcessingJob>> {
    const priority = jobData.priority === 'high' ? 1 : jobData.priority === 'low' ? 3 : 2;

    return await this.batchProcessingQueue.add(
      'process-batch-audio',
      jobData,
      {
        priority,
        delay: jobData.priority === 'high' ? 0 : 2000, // Batch jobs have slightly longer delay
        removeOnComplete: 10,
        removeOnFail: 5
      }
    );
  }

  public async addMultipleJobs(jobs: AudioProcessingJob[]): Promise<Job<AudioProcessingJob>[]> {
    const bullJobs = jobs.map(jobData => ({
      name: 'process-audio',
      data: jobData,
      opts: {
        priority: jobData.priority === 'high' ? 1 : jobData.priority === 'low' ? 3 : 2,
        delay: jobData.priority === 'high' ? 0 : 1000,
        removeOnComplete: 50,
        removeOnFail: 20
      }
    }));

    return await this.audioProcessingQueue.addBulk(bullJobs);
  }

  // Queue management methods

  public async getQueueStats() {
    const mainQueueStats = await this.audioProcessingQueue.getJobCounts();
    const batchQueueStats = await this.batchProcessingQueue.getJobCounts();

    return {
      audioProcessing: mainQueueStats,
      batchProcessing: batchQueueStats
    };
  }

  public async pauseQueues(): Promise<void> {
    await Promise.all([
      this.audioProcessingQueue.pause(),
      this.batchProcessingQueue.pause()
    ]);
    logger.info('Audio processing queues paused');
  }

  public async resumeQueues(): Promise<void> {
    await Promise.all([
      this.audioProcessingQueue.resume(),
      this.batchProcessingQueue.resume()
    ]);
    logger.info('Audio processing queues resumed');
  }

  public async close(): Promise<void> {
    logger.info('Closing audio processor workers...');

    await this.worker.close();
    await this.batchWorker.close();
    await this.audioProcessingQueue.close();
    await this.batchProcessingQueue.close();

    logger.info('Audio processor workers closed');
  }
}
