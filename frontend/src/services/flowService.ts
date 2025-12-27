import { apiClient } from './api';
import {
  Flow,
  FlowVersion,
  FlowNode,
  FlowConnection,
  FlowVariable,
  TrainingData,
  PaginatedResponse,
  FilterOptions
} from '@/types';
import { logger } from '@/utils/logger';

export interface CreateFlowData {
  name: string;
  description?: string;
  type: string;
  category?: string;
  tags?: string[];
}

export interface UpdateFlowData extends Partial<CreateFlowData> {
  isActive?: boolean;
}

export interface CreateFlowVersionData {
  version: string;
  nodes: FlowNode[];
  connections: FlowConnection[];
  variables?: FlowVariable[];
  settings?: Record<string, any>;
}

export interface UpdateFlowVersionData extends Partial<CreateFlowVersionData> {
  status?: string;
}

export class FlowService {
  private static readonly BASE_URL = '/api/v1/flows';

  /**
   * Get paginated list of flows
   */
  static async getFlows(filters?: FilterOptions): Promise<PaginatedResponse<Flow>> {
    try {
      const params = new URLSearchParams();

      if (filters?.page) params.append('page', filters.page.toString());
      if (filters?.limit) params.append('limit', filters.limit.toString());
      if (filters?.search) params.append('search', filters.search);
      if (filters?.type?.length) params.append('type', filters.type.join(','));
      if (filters?.dateRange) {
        params.append('startDate', filters.dateRange.startDate.toISOString());
        params.append('endDate', filters.dateRange.endDate.toISOString());
      }
      if (filters?.sortBy) params.append('sortBy', filters.sortBy);
      if (filters?.sortOrder) params.append('sortOrder', filters.sortOrder || 'desc');

      const response = await apiClient.get<PaginatedResponse<Flow>>(
        `${this.BASE_URL}?${params.toString()}`
      );

      return response.data.data;
    } catch (error) {
      logger.error('Failed to fetch flows', { filters, error });
      throw error;
    }
  }

  /**
   * Get single flow by ID
   */
  static async getFlow(id: string): Promise<Flow> {
    try {
      const response = await apiClient.get<Flow>(`${this.BASE_URL}/${id}`);
      return response.data.data;
    } catch (error) {
      logger.error('Failed to fetch flow', { flowId: id, error });
      throw error;
    }
  }

  /**
   * Create new flow
   */
  static async createFlow(data: CreateFlowData): Promise<Flow> {
    try {
      logger.info('Creating new flow', { name: data.name, type: data.type });

      const response = await apiClient.post<Flow>(this.BASE_URL, data);
      const flow = response.data.data;

      logger.info('Flow created successfully', {
        flowId: flow.id,
        name: flow.name
      });

      return flow;
    } catch (error) {
      logger.error('Failed to create flow', { data, error });
      throw error;
    }
  }

  /**
   * Update existing flow
   */
  static async updateFlow(id: string, data: UpdateFlowData): Promise<Flow> {
    try {
      logger.info('Updating flow', { flowId: id, updates: Object.keys(data) });

      const response = await apiClient.put<Flow>(`${this.BASE_URL}/${id}`, data);
      const flow = response.data.data;

      logger.info('Flow updated successfully', { flowId: id });

      return flow;
    } catch (error) {
      logger.error('Failed to update flow', { flowId: id, data, error });
      throw error;
    }
  }

  /**
   * Delete flow
   */
  static async deleteFlow(id: string): Promise<void> {
    try {
      logger.info('Deleting flow', { flowId: id });

      await apiClient.delete(`${this.BASE_URL}/${id}`);

      logger.info('Flow deleted successfully', { flowId: id });
    } catch (error) {
      logger.error('Failed to delete flow', { flowId: id, error });
      throw error;
    }
  }

  /**
   * Duplicate flow
   */
  static async duplicateFlow(id: string, name?: string): Promise<Flow> {
    try {
      logger.info('Duplicating flow', { flowId: id, newName: name });

      const response = await apiClient.post<Flow>(`${this.BASE_URL}/${id}/duplicate`, { name });
      const flow = response.data.data;

      logger.info('Flow duplicated successfully', {
        originalId: id,
        newId: flow.id,
        name: flow.name
      });

      return flow;
    } catch (error) {
      logger.error('Failed to duplicate flow', { flowId: id, error });
      throw error;
    }
  }

  /**
   * Get flow versions
   */
  static async getFlowVersions(flowId: string): Promise<FlowVersion[]> {
    try {
      const response = await apiClient.get<FlowVersion[]>(`${this.BASE_URL}/${flowId}/versions`);
      return response.data.data;
    } catch (error) {
      logger.error('Failed to fetch flow versions', { flowId, error });
      throw error;
    }
  }

  /**
   * Get specific flow version
   */
  static async getFlowVersion(flowId: string, versionId: string): Promise<FlowVersion> {
    try {
      const response = await apiClient.get<FlowVersion>(`${this.BASE_URL}/${flowId}/versions/${versionId}`);
      return response.data.data;
    } catch (error) {
      logger.error('Failed to fetch flow version', { flowId, versionId, error });
      throw error;
    }
  }

  /**
   * Create new flow version
   */
  static async createFlowVersion(flowId: string, data: CreateFlowVersionData): Promise<FlowVersion> {
    try {
      logger.info('Creating flow version', { flowId, version: data.version });

      const response = await apiClient.post<FlowVersion>(`${this.BASE_URL}/${flowId}/versions`, data);
      const version = response.data.data;

      logger.info('Flow version created successfully', {
        flowId,
        versionId: version.id,
        version: version.version
      });

      return version;
    } catch (error) {
      logger.error('Failed to create flow version', { flowId, data, error });
      throw error;
    }
  }

  /**
   * Update flow version
   */
  static async updateFlowVersion(flowId: string, versionId: string, data: UpdateFlowVersionData): Promise<FlowVersion> {
    try {
      logger.info('Updating flow version', { flowId, versionId, updates: Object.keys(data) });

      const response = await apiClient.put<FlowVersion>(
        `${this.BASE_URL}/${flowId}/versions/${versionId}`,
        data
      );

      const version = response.data.data;

      logger.info('Flow version updated successfully', { flowId, versionId });

      return version;
    } catch (error) {
      logger.error('Failed to update flow version', { flowId, versionId, error });
      throw error;
    }
  }

  /**
   * Delete flow version
   */
  static async deleteFlowVersion(flowId: string, versionId: string): Promise<void> {
    try {
      logger.info('Deleting flow version', { flowId, versionId });

      await apiClient.delete(`${this.BASE_URL}/${flowId}/versions/${versionId}`);

      logger.info('Flow version deleted successfully', { flowId, versionId });
    } catch (error) {
      logger.error('Failed to delete flow version', { flowId, versionId, error });
      throw error;
    }
  }

  /**
   * Train flow version
   */
  static async trainFlowVersion(flowId: string, versionId: string, trainingData?: TrainingData): Promise<FlowVersion> {
    try {
      logger.info('Training flow version', { flowId, versionId });

      const response = await apiClient.post<FlowVersion>(
        `${this.BASE_URL}/${flowId}/versions/${versionId}/train`,
        { trainingData }
      );

      const version = response.data.data;

      logger.info('Flow version training started', { flowId, versionId });

      return version;
    } catch (error) {
      logger.error('Failed to train flow version', { flowId, versionId, error });
      throw error;
    }
  }

  /**
   * Publish flow version
   */
  static async publishFlowVersion(flowId: string, versionId: string): Promise<FlowVersion> {
    try {
      logger.info('Publishing flow version', { flowId, versionId });

      const response = await apiClient.post<FlowVersion>(
        `${this.BASE_URL}/${flowId}/versions/${versionId}/publish`
      );

      const version = response.data.data;

      logger.info('Flow version published successfully', {
        flowId,
        versionId,
        version: version.version
      });

      return version;
    } catch (error) {
      logger.error('Failed to publish flow version', { flowId, versionId, error });
      throw error;
    }
  }

  /**
   * Get active flow version
   */
  static async getActiveFlowVersion(flowId: string): Promise<FlowVersion | null> {
    try {
      const response = await apiClient.get<FlowVersion | null>(`${this.BASE_URL}/${flowId}/active-version`);
      return response.data.data;
    } catch (error) {
      logger.error('Failed to get active flow version', { flowId, error });
      throw error;
    }
  }

  /**
   * Validate flow configuration
   */
  static async validateFlow(flowId: string, versionId: string): Promise<{
    isValid: boolean;
    errors: string[];
    warnings: string[];
  }> {
    try {
      const response = await apiClient.post<{
        isValid: boolean;
        errors: string[];
        warnings: string[];
      }>(`${this.BASE_URL}/${flowId}/versions/${versionId}/validate`);

      return response.data.data;
    } catch (error) {
      logger.error('Failed to validate flow', { flowId, versionId, error });
      throw error;
    }
  }

  /**
   * Export flow
   */
  static async exportFlow(flowId: string, format: 'json' | 'yaml' = 'json'): Promise<any> {
    try {
      const response = await apiClient.get(
        `${this.BASE_URL}/${flowId}/export?format=${format}`,
        {
          responseType: format === 'json' ? 'json' : 'blob'
        }
      );

      return response.data.data || response.data;
    } catch (error) {
      logger.error('Failed to export flow', { flowId, format, error });
      throw error;
    }
  }

  /**
   * Import flow
   */
  static async importFlow(data: any, format: 'json' | 'yaml' = 'json'): Promise<Flow> {
    try {
      logger.info('Importing flow', { format });

      const response = await apiClient.post<Flow>(`${this.BASE_URL}/import`, {
        data,
        format
      });

      const flow = response.data.data;

      logger.info('Flow imported successfully', {
        flowId: flow.id,
        name: flow.name
      });

      return flow;
    } catch (error) {
      logger.error('Failed to import flow', { format, error });
      throw error;
    }
  }

  /**
   * Get flow templates
   */
  static async getFlowTemplates(): Promise<Array<{
    id: string;
    name: string;
    description: string;
    type: string;
    category: string;
    preview?: string;
    config: Record<string, any>;
  }>> {
    try {
      const response = await apiClient.get<Array<{
        id: string;
        name: string;
        description: string;
        type: string;
        category: string;
        preview?: string;
        config: Record<string, any>;
      }>>(`${this.BASE_URL}/templates`);

      return response.data.data;
    } catch (error) {
      logger.error('Failed to fetch flow templates', { error });
      throw error;
    }
  }

  /**
   * Create flow from template
   */
  static async createFromTemplate(templateId: string, data: CreateFlowData): Promise<Flow> {
    try {
      logger.info('Creating flow from template', { templateId, name: data.name });

      const response = await apiClient.post<Flow>(`${this.BASE_URL}/templates/${templateId}/create`, data);
      const flow = response.data.data;

      logger.info('Flow created from template', {
        templateId,
        flowId: flow.id,
        name: flow.name
      });

      return flow;
    } catch (error) {
      logger.error('Failed to create flow from template', { templateId, error });
      throw error;
    }
  }

  /**
   * Get flow analytics
   */
  static async getFlowAnalytics(flowId: string, dateRange?: { startDate: Date; endDate: Date }): Promise<{
    totalSessions: number;
    successfulSessions: number;
    averageDuration: number;
    popularPaths: Array<{ path: string[]; count: number }>;
    dropOffPoints: Array<{ nodeId: string; dropOffRate: number }>;
  }> {
    try {
      const params = new URLSearchParams();
      if (dateRange) {
        params.append('startDate', dateRange.startDate.toISOString());
        params.append('endDate', dateRange.endDate.toISOString());
      }

      const response = await apiClient.get<{
        totalSessions: number;
        successfulSessions: number;
        averageDuration: number;
        popularPaths: Array<{ path: string[]; count: number }>;
        dropOffPoints: Array<{ nodeId: string; dropOffRate: number }>;
      }>(`${this.BASE_URL}/${flowId}/analytics?${params.toString()}`);

      return response.data.data;
    } catch (error) {
      logger.error('Failed to fetch flow analytics', { flowId, error });
      throw error;
    }
  }
}

// Export singleton instance
export const flowService = FlowService;
