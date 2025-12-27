import { apiClient } from './api';
import {
  Session,
  Message,
  AudioFile,
  SentimentAnalysis,
  ProtocolCompliance,
  PaginatedResponse,
  FilterOptions
} from '@/types';
import { logger } from '@/utils/logger';

export class SessionService {
  private static readonly BASE_URL = '/api/v1/sessions';

  /**
   * Get paginated list of sessions
   */
  static async getSessions(filters?: FilterOptions): Promise<PaginatedResponse<Session>> {
    try {
      const params = new URLSearchParams();

      if (filters?.page) params.append('page', filters.page.toString());
      if (filters?.limit) params.append('limit', filters.limit.toString());
      if (filters?.search) params.append('search', filters.search);
      if (filters?.campaignId) params.append('campaignId', filters.campaignId);
      if (filters?.status?.length) params.append('status', filters.status.join(','));
      if (filters?.dateRange) {
        params.append('startDate', filters.dateRange.startDate.toISOString());
        params.append('endDate', filters.dateRange.endDate.toISOString());
      }
      if (filters?.sortBy) params.append('sortBy', filters.sortBy);
      if (filters?.sortOrder) params.append('sortOrder', filters.sortOrder || 'desc');

      const response = await apiClient.get<PaginatedResponse<Session>>(
        `${this.BASE_URL}?${params.toString()}`
      );

      return response.data.data;
    } catch (error) {
      logger.error('Failed to fetch sessions', { filters, error });
      throw error;
    }
  }

  /**
   * Get single session by ID
   */
  static async getSession(id: string): Promise<Session> {
    try {
      const response = await apiClient.get<Session>(`${this.BASE_URL}/${id}`);
      return response.data.data;
    } catch (error) {
      logger.error('Failed to fetch session', { sessionId: id, error });
      throw error;
    }
  }

  /**
   * Get conversation messages for a session
   */
  static async getConversation(id: string): Promise<Message[]> {
    try {
      const response = await apiClient.get<Message[]>(`${this.BASE_URL}/${id}/conversation`);
      return response.data.data;
    } catch (error) {
      logger.error('Failed to fetch conversation', { sessionId: id, error });
      throw error;
    }
  }

  /**
   * Get audio file for a session
   */
  static async getAudioFile(id: string): Promise<AudioFile> {
    try {
      const response = await apiClient.get<AudioFile>(`${this.BASE_URL}/${id}/audio`);
      return response.data.data;
    } catch (error) {
      logger.error('Failed to fetch audio file', { sessionId: id, error });
      throw error;
    }
  }

  /**
   * Get signed URL for audio file download
   */
  static async getAudioDownloadUrl(id: string): Promise<string> {
    try {
      const response = await apiClient.get<{ url: string }>(`${this.BASE_URL}/${id}/audio/download`);
      return response.data.data.url;
    } catch (error) {
      logger.error('Failed to get audio download URL', { sessionId: id, error });
      throw error;
    }
  }

  /**
   * Get transcription for a session
   */
  static async getTranscription(id: string): Promise<string | null> {
    try {
      const response = await apiClient.get<{ transcription: string | null }>(
        `${this.BASE_URL}/${id}/transcription`
      );
      return response.data.data.transcription;
    } catch (error) {
      logger.error('Failed to fetch transcription', { sessionId: id, error });
      throw error;
    }
  }

  /**
   * Get sentiment analysis for a session
   */
  static async getSentimentAnalysis(id: string): Promise<SentimentAnalysis | null> {
    try {
      const response = await apiClient.get<{ sentimentAnalysis: SentimentAnalysis | null }>(
        `${this.BASE_URL}/${id}/sentiment`
      );
      return response.data.data.sentimentAnalysis;
    } catch (error) {
      logger.error('Failed to fetch sentiment analysis', { sessionId: id, error });
      throw error;
    }
  }

  /**
   * Get sentiment journey over time
   */
  static async getSentimentJourney(id: string): Promise<Array<{
    timestamp: Date;
    sentiment: 'positive' | 'neutral' | 'negative';
    confidence: number;
    text?: string;
  }>> {
    try {
      const response = await apiClient.get<Array<{
        timestamp: Date;
        sentiment: 'positive' | 'neutral' | 'negative';
        confidence: number;
        text?: string;
      }>>(`${this.BASE_URL}/${id}/sentiment-journey`);

      return response.data.data;
    } catch (error) {
      logger.error('Failed to fetch sentiment journey', { sessionId: id, error });
      throw error;
    }
  }

  /**
   * Get session insights
   */
  static async getSessionInsights(id: string): Promise<{
    keyInsights: string[];
    protocolCompliance: ProtocolCompliance | null;
    customerSatisfaction: number | null;
    conversationQuality: {
      score: number;
      factors: Record<string, number>;
    };
    recommendations: string[];
  }> {
    try {
      const response = await apiClient.get<{
        keyInsights: string[];
        protocolCompliance: ProtocolCompliance | null;
        customerSatisfaction: number | null;
        conversationQuality: {
          score: number;
          factors: Record<string, number>;
        };
        recommendations: string[];
      }>(`${this.BASE_URL}/${id}/insights`);

      return response.data.data;
    } catch (error) {
      logger.error('Failed to fetch session insights', { sessionId: id, error });
      throw error;
    }
  }

  /**
   * Export session data
   */
  static async exportSession(id: string, format: 'json' | 'pdf' | 'csv' = 'json'): Promise<any> {
    try {
      const response = await apiClient.get(
        `${this.BASE_URL}/${id}/export?format=${format}`,
        {
          responseType: format === 'json' ? 'json' : 'blob'
        }
      );

      return response.data.data || response.data;
    } catch (error) {
      logger.error('Failed to export session', { sessionId: id, format, error });
      throw error;
    }
  }

  /**
   * Update session metadata
   */
  static async updateSession(id: string, updates: {
    notes?: string;
    tags?: string[];
    priority?: 'low' | 'normal' | 'high';
    metadata?: Record<string, any>;
  }): Promise<Session> {
    try {
      const response = await apiClient.put<Session>(`${this.BASE_URL}/${id}`, updates);
      return response.data.data;
    } catch (error) {
      logger.error('Failed to update session', { sessionId: id, updates, error });
      throw error;
    }
  }

  /**
   * Add note to session
   */
  static async addNote(id: string, note: string, authorId?: string): Promise<void> {
    try {
      await apiClient.post(`${this.BASE_URL}/${id}/notes`, {
        note,
        authorId
      });

      logger.info('Note added to session', { sessionId: id });
    } catch (error) {
      logger.error('Failed to add note to session', { sessionId: id, error });
      throw error;
    }
  }

  /**
   * Get session notes
   */
  static async getNotes(id: string): Promise<Array<{
    id: string;
    note: string;
    authorId: string;
    authorName: string;
    createdAt: Date;
  }>> {
    try {
      const response = await apiClient.get<Array<{
        id: string;
        note: string;
        authorId: string;
        authorName: string;
        createdAt: Date;
      }>>(`${this.BASE_URL}/${id}/notes`);

      return response.data.data;
    } catch (error) {
      logger.error('Failed to fetch session notes', { sessionId: id, error });
      throw error;
    }
  }

  /**
   * Transfer session to another agent/user
   */
  static async transferSession(id: string, toUserId: string, reason?: string): Promise<Session> {
    try {
      logger.info('Transferring session', { sessionId: id, toUserId, reason });

      const response = await apiClient.post<Session>(`${this.BASE_URL}/${id}/transfer`, {
        toUserId,
        reason
      });

      const session = response.data.data;

      logger.info('Session transferred successfully', {
        sessionId: id,
        fromUserId: session.metadata?.transferredFrom,
        toUserId
      });

      return session;
    } catch (error) {
      logger.error('Failed to transfer session', { sessionId: id, toUserId, error });
      throw error;
    }
  }

  /**
   * End session manually
   */
  static async endSession(id: string, result: 'successful' | 'failed' | 'hung_up', notes?: string): Promise<Session> {
    try {
      logger.info('Ending session manually', { sessionId: id, result, notes });

      const response = await apiClient.post<Session>(`${this.BASE_URL}/${id}/end`, {
        result,
        notes
      });

      const session = response.data.data;

      logger.info('Session ended manually', { sessionId: id, result });

      return session;
    } catch (error) {
      logger.error('Failed to end session', { sessionId: id, result, error });
      throw error;
    }
  }

  /**
   * Get real-time session statistics
   */
  static async getRealtimeStats(): Promise<{
    activeSessions: number;
    queuedSessions: number;
    averageWaitTime: number;
    agentAvailability: Record<string, boolean>;
  }> {
    try {
      const response = await apiClient.get<{
        activeSessions: number;
        queuedSessions: number;
        averageWaitTime: number;
        agentAvailability: Record<string, boolean>;
      }>(`${this.BASE_URL}/realtime-stats`);

      return response.data.data;
    } catch (error) {
      logger.error('Failed to fetch realtime stats', { error });
      throw error;
    }
  }

  /**
   * Bulk update sessions
   */
  static async bulkUpdateSessions(sessionIds: string[], updates: {
    status?: string;
    priority?: 'low' | 'normal' | 'high';
    tags?: string[];
  }): Promise<{ updated: number; failed: number }> {
    try {
      logger.info('Bulk updating sessions', {
        sessionCount: sessionIds.length,
        updates: Object.keys(updates)
      });

      const response = await apiClient.put<{ updated: number; failed: number }>(
        `${this.BASE_URL}/bulk-update`,
        { sessionIds, updates }
      );

      const result = response.data.data;

      logger.info('Bulk session update completed', {
        total: sessionIds.length,
        updated: result.updated,
        failed: result.failed
      });

      return result;
    } catch (error) {
      logger.error('Failed to bulk update sessions', { error });
      throw error;
    }
  }

  /**
   * Search sessions with advanced filters
   */
  static async searchSessions(query: {
    text?: string;
    sentiment?: 'positive' | 'neutral' | 'negative';
    protocolCompliance?: boolean;
    duration?: { min?: number; max?: number };
    result?: string[];
    agentId?: string;
    customerPhone?: string;
    dateRange?: { startDate: Date; endDate: Date };
    limit?: number;
    offset?: number;
  }): Promise<PaginatedResponse<Session>> {
    try {
      const params = new URLSearchParams();

      if (query.text) params.append('text', query.text);
      if (query.sentiment) params.append('sentiment', query.sentiment);
      if (query.protocolCompliance !== undefined) params.append('protocolCompliance', query.protocolCompliance.toString());
      if (query.duration?.min) params.append('minDuration', query.duration.min.toString());
      if (query.duration?.max) params.append('maxDuration', query.duration.max.toString());
      if (query.result?.length) params.append('result', query.result.join(','));
      if (query.agentId) params.append('agentId', query.agentId);
      if (query.customerPhone) params.append('customerPhone', query.customerPhone);
      if (query.dateRange) {
        params.append('startDate', query.dateRange.startDate.toISOString());
        params.append('endDate', query.dateRange.endDate.toISOString());
      }
      if (query.limit) params.append('limit', query.limit.toString());
      if (query.offset) params.append('offset', query.offset.toString());

      const response = await apiClient.get<PaginatedResponse<Session>>(
        `${this.BASE_URL}/search?${params.toString()}`
      );

      return response.data.data;
    } catch (error) {
      logger.error('Failed to search sessions', { query, error });
      throw error;
    }
  }
}

// Export singleton instance
export const sessionService = SessionService;
