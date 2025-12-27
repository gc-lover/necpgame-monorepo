import { apiClient } from './api';
import {
  Campaign,
  CampaignContact,
  CampaignStatistics,
  PaginatedResponse,
  FilterOptions,
  DateRange
} from '@/types';
import { logger } from '@/utils/logger';

export interface CreateCampaignData {
  name: string;
  description?: string;
  type: string;
  flowId: string;
  rules: {
    maxAttempts: number;
    timeWindows?: Array<{
      daysOfWeek: number[];
      startTime: string;
      endTime: string;
    }>;
    retryStrategy?: {
      delays: number[];
      exponentialBackoff?: boolean;
      maxDelay?: number;
    };
  };
  settings: {
    priority?: 'low' | 'normal' | 'high';
    concurrentCalls?: number;
    recordingEnabled?: boolean;
    transcriptionEnabled?: boolean;
    sentimentAnalysisEnabled?: boolean;
    protocolComplianceEnabled?: boolean;
  };
  scheduledAt?: string;
}

export interface UpdateCampaignData extends Partial<CreateCampaignData> {
  status?: string;
}

export interface CampaignAnalytics {
  campaignId: string;
  statistics: CampaignStatistics;
  contacts: CampaignContact[];
  dateRange: DateRange;
}

export class CampaignService {
  private static readonly BASE_URL = '/api/v1/campaigns';

  /**
   * Get paginated list of campaigns
   */
  static async getCampaigns(filters?: FilterOptions): Promise<PaginatedResponse<Campaign>> {
    try {
      const params = new URLSearchParams();

      if (filters?.page) params.append('page', filters.page.toString());
      if (filters?.limit) params.append('limit', filters.limit.toString());
      if (filters?.search) params.append('search', filters.search);
      if (filters?.status?.length) params.append('status', filters.status.join(','));
      if (filters?.type?.length) params.append('type', filters.type.join(','));
      if (filters?.dateRange) {
        params.append('startDate', filters.dateRange.startDate.toISOString());
        params.append('endDate', filters.dateRange.endDate.toISOString());
      }
      if (filters?.sortBy) params.append('sortBy', filters.sortBy);
      if (filters?.sortOrder) params.append('sortOrder', filters.sortOrder || 'desc');

      const response = await apiClient.get<PaginatedResponse<Campaign>>(
        `${this.BASE_URL}?${params.toString()}`
      );

      return response.data.data;
    } catch (error) {
      logger.error('Failed to fetch campaigns', { filters, error });
      throw error;
    }
  }

  /**
   * Get single campaign by ID
   */
  static async getCampaign(id: string): Promise<Campaign> {
    try {
      const response = await apiClient.get<Campaign>(`${this.BASE_URL}/${id}`);
      return response.data.data;
    } catch (error) {
      logger.error('Failed to fetch campaign', { campaignId: id, error });
      throw error;
    }
  }

  /**
   * Create new campaign
   */
  static async createCampaign(data: CreateCampaignData): Promise<Campaign> {
    try {
      logger.info('Creating new campaign', { name: data.name, type: data.type });

      const response = await apiClient.post<Campaign>(this.BASE_URL, data);
      const campaign = response.data.data;

      logger.info('Campaign created successfully', {
        campaignId: campaign.id,
        name: campaign.name
      });

      return campaign;
    } catch (error) {
      logger.error('Failed to create campaign', { data, error });
      throw error;
    }
  }

  /**
   * Update existing campaign
   */
  static async updateCampaign(id: string, data: UpdateCampaignData): Promise<Campaign> {
    try {
      logger.info('Updating campaign', { campaignId: id, updates: Object.keys(data) });

      const response = await apiClient.put<Campaign>(`${this.BASE_URL}/${id}`, data);
      const campaign = response.data.data;

      logger.info('Campaign updated successfully', { campaignId: id });

      return campaign;
    } catch (error) {
      logger.error('Failed to update campaign', { campaignId: id, data, error });
      throw error;
    }
  }

  /**
   * Delete campaign
   */
  static async deleteCampaign(id: string): Promise<void> {
    try {
      logger.info('Deleting campaign', { campaignId: id });

      await apiClient.delete(`${this.BASE_URL}/${id}`);

      logger.info('Campaign deleted successfully', { campaignId: id });
    } catch (error) {
      logger.error('Failed to delete campaign', { campaignId: id, error });
      throw error;
    }
  }

  /**
   * Start campaign
   */
  static async startCampaign(id: string): Promise<Campaign> {
    try {
      logger.info('Starting campaign', { campaignId: id });

      const response = await apiClient.post<Campaign>(`${this.BASE_URL}/${id}/start`);
      const campaign = response.data.data;

      logger.info('Campaign started successfully', { campaignId: id });

      return campaign;
    } catch (error) {
      logger.error('Failed to start campaign', { campaignId: id, error });
      throw error;
    }
  }

  /**
   * Pause campaign
   */
  static async pauseCampaign(id: string): Promise<Campaign> {
    try {
      logger.info('Pausing campaign', { campaignId: id });

      const response = await apiClient.post<Campaign>(`${this.BASE_URL}/${id}/pause`);
      const campaign = response.data.data;

      logger.info('Campaign paused successfully', { campaignId: id });

      return campaign;
    } catch (error) {
      logger.error('Failed to pause campaign', { campaignId: id, error });
      throw error;
    }
  }

  /**
   * Stop campaign
   */
  static async stopCampaign(id: string): Promise<Campaign> {
    try {
      logger.info('Stopping campaign', { campaignId: id });

      const response = await apiClient.post<Campaign>(`${this.BASE_URL}/${id}/stop`);
      const campaign = response.data.data;

      logger.info('Campaign stopped successfully', { campaignId: id });

      return campaign;
    } catch (error) {
      logger.error('Failed to stop campaign', { campaignId: id, error });
      throw error;
    }
  }

  /**
   * Duplicate campaign
   */
  static async duplicateCampaign(id: string, name?: string): Promise<Campaign> {
    try {
      logger.info('Duplicating campaign', { campaignId: id, newName: name });

      const response = await apiClient.post<Campaign>(`${this.BASE_URL}/${id}/duplicate`, { name });
      const campaign = response.data.data;

      logger.info('Campaign duplicated successfully', {
        originalId: id,
        newId: campaign.id,
        name: campaign.name
      });

      return campaign;
    } catch (error) {
      logger.error('Failed to duplicate campaign', { campaignId: id, error });
      throw error;
    }
  }

  /**
   * Get campaign analytics
   */
  static async getCampaignAnalytics(id: string, dateRange?: DateRange): Promise<CampaignAnalytics> {
    try {
      const params = new URLSearchParams();
      if (dateRange) {
        params.append('startDate', dateRange.startDate.toISOString());
        params.append('endDate', dateRange.endDate.toISOString());
      }

      const response = await apiClient.get<CampaignAnalytics>(
        `${this.BASE_URL}/${id}/analytics?${params.toString()}`
      );

      return response.data.data;
    } catch (error) {
      logger.error('Failed to fetch campaign analytics', { campaignId: id, error });
      throw error;
    }
  }

  /**
   * Get campaign report
   */
  static async getCampaignReport(id: string, format: 'json' | 'csv' | 'pdf' = 'json'): Promise<any> {
    try {
      const response = await apiClient.get(
        `${this.BASE_URL}/${id}/report?format=${format}`,
        {
          responseType: format === 'json' ? 'json' : 'blob'
        }
      );

      return response.data.data || response.data;
    } catch (error) {
      logger.error('Failed to fetch campaign report', { campaignId: id, format, error });
      throw error;
    }
  }

  /**
   * Get campaign contacts
   */
  static async getCampaignContacts(id: string, filters?: FilterOptions): Promise<PaginatedResponse<CampaignContact>> {
    try {
      const params = new URLSearchParams();

      if (filters?.page) params.append('page', filters.page.toString());
      if (filters?.limit) params.append('limit', filters.limit.toString());
      if (filters?.search) params.append('search', filters.search);
      if (filters?.status?.length) params.append('status', filters.status.join(','));

      const response = await apiClient.get<PaginatedResponse<CampaignContact>>(
        `${this.BASE_URL}/${id}/contacts?${params.toString()}`
      );

      return response.data.data;
    } catch (error) {
      logger.error('Failed to fetch campaign contacts', { campaignId: id, error });
      throw error;
    }
  }

  /**
   * Add contacts to campaign
   */
  static async addContacts(id: string, contacts: Array<{
    phoneNumber: string;
    customerName?: string;
    debtAmount?: number;
    metadata?: Record<string, any>;
  }>): Promise<{ added: number; failed: number }> {
    try {
      logger.info('Adding contacts to campaign', {
        campaignId: id,
        contactCount: contacts.length
      });

      const response = await apiClient.post<{ added: number; failed: number }>(
        `${this.BASE_URL}/${id}/contacts`,
        { contacts }
      );

      const result = response.data.data;

      logger.info('Contacts added to campaign', {
        campaignId: id,
        added: result.added,
        failed: result.failed
      });

      return result;
    } catch (error) {
      logger.error('Failed to add contacts to campaign', { campaignId: id, error });
      throw error;
    }
  }

  /**
   * Upload contacts CSV file
   */
  static async uploadContactsCSV(id: string, file: File, onProgress?: (progress: number) => void): Promise<{
    added: number;
    failed: number;
    errors?: string[];
  }> {
    try {
      logger.info('Uploading contacts CSV', {
        campaignId: id,
        fileName: file.name,
        fileSize: file.size
      });

      const response = await apiClient.upload(
        `${this.BASE_URL}/${id}/contacts/upload`,
        file,
        onProgress
      );

      const result = response.data.data;

      logger.info('Contacts CSV uploaded successfully', {
        campaignId: id,
        added: result.added,
        failed: result.failed
      });

      return result;
    } catch (error) {
      logger.error('Failed to upload contacts CSV', { campaignId: id, error });
      throw error;
    }
  }

  /**
   * Update contact status
   */
  static async updateContactStatus(
    campaignId: string,
    contactId: string,
    status: string,
    notes?: string
  ): Promise<CampaignContact> {
    try {
      const response = await apiClient.put<CampaignContact>(
        `${this.BASE_URL}/${campaignId}/contacts/${contactId}`,
        { status, notes }
      );

      return response.data.data;
    } catch (error) {
      logger.error('Failed to update contact status', {
        campaignId,
        contactId,
        status,
        error
      });
      throw error;
    }
  }

  /**
   * Remove contact from campaign
   */
  static async removeContact(campaignId: string, contactId: string): Promise<void> {
    try {
      logger.info('Removing contact from campaign', { campaignId, contactId });

      await apiClient.delete(`${this.BASE_URL}/${campaignId}/contacts/${contactId}`);

      logger.info('Contact removed from campaign', { campaignId, contactId });
    } catch (error) {
      logger.error('Failed to remove contact', { campaignId, contactId, error });
      throw error;
    }
  }

  /**
   * Get campaign templates
   */
  static async getCampaignTemplates(): Promise<Array<{
    id: string;
    name: string;
    description: string;
    type: string;
    config: Record<string, any>;
  }>> {
    try {
      const response = await apiClient.get<Array<{
        id: string;
        name: string;
        description: string;
        type: string;
        config: Record<string, any>;
      }>>(`${this.BASE_URL}/templates`);

      return response.data.data;
    } catch (error) {
      logger.error('Failed to fetch campaign templates', { error });
      throw error;
    }
  }

  /**
   * Create campaign from template
   */
  static async createFromTemplate(templateId: string, data: Partial<CreateCampaignData>): Promise<Campaign> {
    try {
      logger.info('Creating campaign from template', { templateId, name: data.name });

      const response = await apiClient.post<Campaign>(`${this.BASE_URL}/templates/${templateId}/create`, data);
      const campaign = response.data.data;

      logger.info('Campaign created from template', {
        templateId,
        campaignId: campaign.id,
        name: campaign.name
      });

      return campaign;
    } catch (error) {
      logger.error('Failed to create campaign from template', { templateId, error });
      throw error;
    }
  }
}

// Export singleton instance
export const campaignService = CampaignService;
