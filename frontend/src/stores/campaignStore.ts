import { create } from 'zustand';
import { devtools } from 'zustand/middleware';
import { Campaign, CampaignContact, CampaignStatistics } from '@/types';
import { campaignService } from '@/services/campaignService';
import { webSocketService } from '@/services/websocket';
import { WebSocketEvents } from '@/types';
import { logger } from '@/utils/logger';

interface CampaignState {
  // State
  campaigns: Campaign[];
  selectedCampaign: Campaign | null;
  campaignContacts: CampaignContact[];
  isLoading: boolean;
  error: string | null;
  lastUpdated: Date | null;

  // Actions
  fetchCampaigns: (filters?: any) => Promise<void>;
  fetchCampaign: (id: string) => Promise<void>;
  selectCampaign: (campaign: Campaign | null) => void;
  createCampaign: (data: any) => Promise<Campaign>;
  updateCampaign: (id: string, data: any) => Promise<void>;
  deleteCampaign: (id: string) => Promise<void>;
  startCampaign: (id: string) => Promise<void>;
  pauseCampaign: (id: string) => Promise<void>;
  stopCampaign: (id: string) => Promise<void>;
  duplicateCampaign: (id: string, name?: string) => Promise<Campaign>;
  updateCampaignStatus: (campaignId: string, status: string, progress?: number) => void;
  fetchCampaignContacts: (campaignId: string) => Promise<void>;
  addContacts: (campaignId: string, contacts: any[]) => Promise<void>;
  updateContact: (campaignId: string, contactId: string, updates: any) => Promise<void>;
  removeContact: (campaignId: string, contactId: string) => Promise<void>;
  clearError: () => void;
  reset: () => void;
}

export const useCampaignStore = create<CampaignState>()(
  devtools(
    (set, get) => ({
      // Initial state
      campaigns: [],
      selectedCampaign: null,
      campaignContacts: [],
      isLoading: false,
      error: null,
      lastUpdated: null,

      // Actions
      fetchCampaigns: async (filters) => {
        set({ isLoading: true, error: null });

        try {
          const response = await campaignService.getCampaigns(filters);
          const campaigns = response.data;

          set({
            campaigns,
            isLoading: false,
            lastUpdated: new Date()
          });

          logger.info('Campaigns fetched successfully', { count: campaigns.length });
        } catch (error) {
          const errorMessage = error instanceof Error ? error.message : 'Failed to fetch campaigns';

          set({
            isLoading: false,
            error: errorMessage
          });

          logger.error('Failed to fetch campaigns', { error: errorMessage });
        }
      },

      fetchCampaign: async (id: string) => {
        set({ isLoading: true, error: null });

        try {
          const campaign = await campaignService.getCampaign(id);

          set((state) => ({
            campaigns: state.campaigns.map(c => c.id === id ? campaign : c),
            selectedCampaign: campaign,
            isLoading: false,
            lastUpdated: new Date()
          }));

          // Subscribe to real-time updates for this campaign
          webSocketService.subscribeToCampaign(id);

          logger.info('Campaign fetched successfully', { campaignId: id });
        } catch (error) {
          const errorMessage = error instanceof Error ? error.message : 'Failed to fetch campaign';

          set({
            isLoading: false,
            error: errorMessage
          });

          logger.error('Failed to fetch campaign', { campaignId: id, error: errorMessage });
        }
      },

      selectCampaign: (campaign: Campaign | null) => {
        set({ selectedCampaign: campaign });

        // Subscribe to real-time updates for the selected campaign
        if (campaign) {
          webSocketService.subscribeToCampaign(campaign.id);
        }
      },

      createCampaign: async (data) => {
        set({ isLoading: true, error: null });

        try {
          const campaign = await campaignService.createCampaign(data);

          set((state) => ({
            campaigns: [campaign, ...state.campaigns],
            isLoading: false,
            lastUpdated: new Date()
          }));

          logger.info('Campaign created successfully', { campaignId: campaign.id });
          return campaign;
        } catch (error) {
          const errorMessage = error instanceof Error ? error.message : 'Failed to create campaign';

          set({
            isLoading: false,
            error: errorMessage
          });

          logger.error('Failed to create campaign', { error: errorMessage });
          throw error;
        }
      },

      updateCampaign: async (id: string, data) => {
        set({ isLoading: true, error: null });

        try {
          const updatedCampaign = await campaignService.updateCampaign(id, data);

          set((state) => ({
            campaigns: state.campaigns.map(c => c.id === id ? updatedCampaign : c),
            selectedCampaign: state.selectedCampaign?.id === id ? updatedCampaign : state.selectedCampaign,
            isLoading: false,
            lastUpdated: new Date()
          }));

          logger.info('Campaign updated successfully', { campaignId: id });
        } catch (error) {
          const errorMessage = error instanceof Error ? error.message : 'Failed to update campaign';

          set({
            isLoading: false,
            error: errorMessage
          });

          logger.error('Failed to update campaign', { campaignId: id, error: errorMessage });
          throw error;
        }
      },

      deleteCampaign: async (id: string) => {
        set({ isLoading: true, error: null });

        try {
          await campaignService.deleteCampaign(id);

          set((state) => ({
            campaigns: state.campaigns.filter(c => c.id !== id),
            selectedCampaign: state.selectedCampaign?.id === id ? null : state.selectedCampaign,
            campaignContacts: state.selectedCampaign?.id === id ? [] : state.campaignContacts,
            isLoading: false,
            lastUpdated: new Date()
          }));

          // Unsubscribe from real-time updates
          webSocketService.unsubscribeFromCampaign(id);

          logger.info('Campaign deleted successfully', { campaignId: id });
        } catch (error) {
          const errorMessage = error instanceof Error ? error.message : 'Failed to delete campaign';

          set({
            isLoading: false,
            error: errorMessage
          });

          logger.error('Failed to delete campaign', { campaignId: id, error: errorMessage });
          throw error;
        }
      },

      startCampaign: async (id: string) => {
        try {
          const campaign = await campaignService.startCampaign(id);

          set((state) => ({
            campaigns: state.campaigns.map(c => c.id === id ? campaign : c),
            selectedCampaign: state.selectedCampaign?.id === id ? campaign : state.selectedCampaign,
            lastUpdated: new Date()
          }));

          logger.info('Campaign started successfully', { campaignId: id });
        } catch (error) {
          logger.error('Failed to start campaign', { campaignId: id, error });
          throw error;
        }
      },

      pauseCampaign: async (id: string) => {
        try {
          const campaign = await campaignService.pauseCampaign(id);

          set((state) => ({
            campaigns: state.campaigns.map(c => c.id === id ? campaign : c),
            selectedCampaign: state.selectedCampaign?.id === id ? campaign : state.selectedCampaign,
            lastUpdated: new Date()
          }));

          logger.info('Campaign paused successfully', { campaignId: id });
        } catch (error) {
          logger.error('Failed to pause campaign', { campaignId: id, error });
          throw error;
        }
      },

      stopCampaign: async (id: string) => {
        try {
          const campaign = await campaignService.stopCampaign(id);

          set((state) => ({
            campaigns: state.campaigns.map(c => c.id === id ? campaign : c),
            selectedCampaign: state.selectedCampaign?.id === id ? campaign : state.selectedCampaign,
            lastUpdated: new Date()
          }));

          logger.info('Campaign stopped successfully', { campaignId: id });
        } catch (error) {
          logger.error('Failed to stop campaign', { campaignId: id, error });
          throw error;
        }
      },

      duplicateCampaign: async (id: string, name?: string) => {
        try {
          const duplicatedCampaign = await campaignService.duplicateCampaign(id, name);

          set((state) => ({
            campaigns: [duplicatedCampaign, ...state.campaigns],
            lastUpdated: new Date()
          }));

          logger.info('Campaign duplicated successfully', {
            originalId: id,
            newId: duplicatedCampaign.id
          });

          return duplicatedCampaign;
        } catch (error) {
          logger.error('Failed to duplicate campaign', { campaignId: id, error });
          throw error;
        }
      },

      updateCampaignStatus: (campaignId: string, status: string, progress?: number) => {
        set((state) => ({
          campaigns: state.campaigns.map(c =>
            c.id === campaignId
              ? { ...c, status: status as any, statistics: { ...c.statistics, progress } }
              : c
          ),
          selectedCampaign: state.selectedCampaign?.id === campaignId
            ? { ...state.selectedCampaign, status: status as any, statistics: { ...state.selectedCampaign.statistics, progress } }
            : state.selectedCampaign,
          lastUpdated: new Date()
        }));

        logger.debug('Campaign status updated locally', { campaignId, status, progress });
      },

      fetchCampaignContacts: async (campaignId: string) => {
        set({ isLoading: true, error: null });

        try {
          const response = await campaignService.getCampaignContacts(campaignId);
          const contacts = response.data;

          set({
            campaignContacts: contacts,
            isLoading: false,
            lastUpdated: new Date()
          });

          logger.info('Campaign contacts fetched successfully', {
            campaignId,
            count: contacts.length
          });
        } catch (error) {
          const errorMessage = error instanceof Error ? error.message : 'Failed to fetch campaign contacts';

          set({
            isLoading: false,
            error: errorMessage
          });

          logger.error('Failed to fetch campaign contacts', { campaignId, error: errorMessage });
        }
      },

      addContacts: async (campaignId: string, contacts) => {
        try {
          const result = await campaignService.addContacts(campaignId, contacts);

          // Refresh contacts after adding
          await get().fetchCampaignContacts(campaignId);

          logger.info('Contacts added to campaign', {
            campaignId,
            added: result.added,
            failed: result.failed
          });
        } catch (error) {
          logger.error('Failed to add contacts to campaign', { campaignId, error });
          throw error;
        }
      },

      updateContact: async (campaignId: string, contactId: string, updates) => {
        try {
          const updatedContact = await campaignService.updateContactStatus(campaignId, contactId, updates.status, updates.notes);

          set((state) => ({
            campaignContacts: state.campaignContacts.map(c =>
              c.id === contactId ? updatedContact : c
            ),
            lastUpdated: new Date()
          }));

          logger.info('Contact updated successfully', { campaignId, contactId });
        } catch (error) {
          logger.error('Failed to update contact', { campaignId, contactId, error });
          throw error;
        }
      },

      removeContact: async (campaignId: string, contactId: string) => {
        try {
          await campaignService.removeContact(campaignId, contactId);

          set((state) => ({
            campaignContacts: state.campaignContacts.filter(c => c.id !== contactId),
            lastUpdated: new Date()
          }));

          logger.info('Contact removed from campaign', { campaignId, contactId });
        } catch (error) {
          logger.error('Failed to remove contact', { campaignId, contactId, error });
          throw error;
        }
      },

      clearError: () => {
        set({ error: null });
      },

      reset: () => {
        set({
          campaigns: [],
          selectedCampaign: null,
          campaignContacts: [],
          isLoading: false,
          error: null,
          lastUpdated: null
        });
      }
    }),
    {
      name: 'campaign-store'
    }
  )
);

// WebSocket event listeners for real-time updates
webSocketService.on(WebSocketEvents.CAMPAIGN_STATUS, (data) => {
  useCampaignStore.getState().updateCampaignStatus(data.campaignId, data.status, data.progress);
});

webSocketService.on(WebSocketEvents.CAMPAIGN_UPDATED, (data) => {
  // Update campaign in the list
  const store = useCampaignStore.getState();
  store.updateCampaign(data.campaignId, data.changes).catch(error => {
    logger.error('Failed to update campaign from WebSocket', { campaignId: data.campaignId, error });
  });
});

// Selectors
export const useCampaigns = () => useCampaignStore((state) => state.campaigns);
export const useSelectedCampaign = () => useCampaignStore((state) => state.selectedCampaign);
export const useCampaignContacts = () => useCampaignStore((state) => state.campaignContacts);
export const useCampaignLoading = () => useCampaignStore((state) => state.isLoading);
export const useCampaignError = () => useCampaignStore((state) => state.error);

// Helper hooks
export const useCampaignActions = () => useCampaignStore((state) => ({
  fetchCampaigns: state.fetchCampaigns,
  fetchCampaign: state.fetchCampaign,
  selectCampaign: state.selectCampaign,
  createCampaign: state.createCampaign,
  updateCampaign: state.updateCampaign,
  deleteCampaign: state.deleteCampaign,
  startCampaign: state.startCampaign,
  pauseCampaign: state.pauseCampaign,
  stopCampaign: state.stopCampaign,
  duplicateCampaign: state.duplicateCampaign,
  fetchCampaignContacts: state.fetchCampaignContacts,
  addContacts: state.addContacts,
  updateContact: state.updateContact,
  removeContact: state.removeContact,
  clearError: state.clearError,
  reset: state.reset
}));
