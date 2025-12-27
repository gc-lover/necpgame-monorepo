import { io, Socket } from 'socket.io-client';
import { WebSocketEvents, WebSocketEventData, RealTimeUpdate } from '@/types';
import { authService } from './authService';
import { logger } from '@/utils/logger';

export class WebSocketService {
  private socket: Socket | null = null;
  private reconnectAttempts = 0;
  private maxReconnectAttempts = parseInt(import.meta.env.VITE_WS_RECONNECT_ATTEMPTS || '5');
  private reconnectDelay = parseInt(import.meta.env.VITE_WS_RECONNECT_DELAY || '1000');
  private isConnected = false;
  private eventListeners: Map<string, Set<(data: any) => void>> = new Map();

  constructor() {
    this.initializeEventListeners();
  }

  /**
   * Connect to WebSocket server
   */
  async connect(): Promise<void> {
    if (this.socket?.connected) {
      logger.info('WebSocket already connected');
      return;
    }

    try {
      const token = authService.getStoredTokens()?.accessToken;
      if (!token) {
        throw new Error('No authentication token available');
      }

      const wsUrl = import.meta.env.VITE_WS_URL || 'ws://localhost:8000';

      logger.info('Connecting to WebSocket server', { url: wsUrl });

      this.socket = io(wsUrl, {
        auth: {
          token
        },
        transports: ['websocket', 'polling'],
        timeout: 10000,
        forceNew: true,
        reconnection: true,
        reconnectionAttempts: this.maxReconnectAttempts,
        reconnectionDelay: this.reconnectDelay,
        reconnectionDelayMax: 5000,
        randomizationFactor: 0.5
      });

      this.setupSocketListeners();

      return new Promise((resolve, reject) => {
        const timeout = setTimeout(() => {
          reject(new Error('WebSocket connection timeout'));
        }, 10000);

        this.socket!.on('connect', () => {
          clearTimeout(timeout);
          this.isConnected = true;
          this.reconnectAttempts = 0;
          logger.info('WebSocket connected successfully');
          resolve();
        });

        this.socket!.on('connect_error', (error) => {
          clearTimeout(timeout);
          logger.error('WebSocket connection failed', { error: error.message });
          reject(error);
        });
      });
    } catch (error) {
      logger.error('Failed to initialize WebSocket connection', { error });
      throw error;
    }
  }

  /**
   * Disconnect from WebSocket server
   */
  async disconnect(): Promise<void> {
    if (!this.socket) return;

    logger.info('Disconnecting from WebSocket server');

    return new Promise((resolve) => {
      this.socket!.disconnect();
      this.socket = null;
      this.isConnected = false;
      this.reconnectAttempts = 0;

      // Clear all event listeners
      this.eventListeners.clear();

      logger.info('WebSocket disconnected');
      resolve();
    });
  }

  /**
   * Check if WebSocket is connected
   */
  isWebSocketConnected(): boolean {
    return this.isConnected && this.socket?.connected === true;
  }

  /**
   * Subscribe to campaign updates
   */
  subscribeToCampaign(campaignId: string): void {
    if (!this.isWebSocketConnected()) {
      logger.warn('Cannot subscribe to campaign - WebSocket not connected', { campaignId });
      return;
    }

    logger.info('Subscribing to campaign updates', { campaignId });
    this.socket!.emit(WebSocketEvents.SUBSCRIBE_CAMPAIGN, campaignId);
  }

  /**
   * Unsubscribe from campaign updates
   */
  unsubscribeFromCampaign(campaignId: string): void {
    if (!this.isWebSocketConnected()) {
      logger.warn('Cannot unsubscribe from campaign - WebSocket not connected', { campaignId });
      return;
    }

    logger.info('Unsubscribing from campaign updates', { campaignId });
    this.socket!.emit(WebSocketEvents.UNSUBSCRIBE_CAMPAIGN, campaignId);
  }

  /**
   * Subscribe to analytics updates
   */
  subscribeToAnalytics(filters?: Record<string, any>): void {
    if (!this.isWebSocketConnected()) {
      logger.warn('Cannot subscribe to analytics - WebSocket not connected');
      return;
    }

    logger.info('Subscribing to analytics updates', { filters });
    this.socket!.emit(WebSocketEvents.SUBSCRIBE_ANALYTICS, filters);
  }

  /**
   * Unsubscribe from analytics updates
   */
  unsubscribeFromAnalytics(): void {
    if (!this.isWebSocketConnected()) {
      logger.warn('Cannot unsubscribe from analytics - WebSocket not connected');
      return;
    }

    logger.info('Unsubscribing from analytics updates');
    this.socket!.emit(WebSocketEvents.UNSUBSCRIBE_ANALYTICS);
  }

  /**
   * Subscribe to session updates
   */
  subscribeToSessions(filters?: Record<string, any>): void {
    if (!this.isWebSocketConnected()) {
      logger.warn('Cannot subscribe to sessions - WebSocket not connected');
      return;
    }

    logger.info('Subscribing to session updates', { filters });
    this.socket!.emit(WebSocketEvents.SUBSCRIBE_SESSIONS, filters);
  }

  /**
   * Unsubscribe from session updates
   */
  unsubscribeFromSessions(): void {
    if (!this.isWebSocketConnected()) {
      logger.warn('Cannot unsubscribe from sessions - WebSocket not connected');
      return;
    }

    logger.info('Unsubscribing from session updates');
    this.socket!.emit(WebSocketEvents.UNSUBSCRIBE_SESSIONS);
  }

  /**
   * Add event listener
   */
  on<T extends keyof WebSocketEventData>(
    event: T,
    callback: (data: WebSocketEventData[T]) => void
  ): () => void {
    if (!this.eventListeners.has(event)) {
      this.eventListeners.set(event, new Set());
    }

    const listeners = this.eventListeners.get(event)!;
    listeners.add(callback as (data: any) => void);

    // Return unsubscribe function
    return () => {
      listeners.delete(callback as (data: any) => void);
      if (listeners.size === 0) {
        this.eventListeners.delete(event);
      }
    };
  }

  /**
   * Remove event listener
   */
  off<T extends keyof WebSocketEventData>(
    event: T,
    callback: (data: WebSocketEventData[T]) => void
  ): void {
    const listeners = this.eventListeners.get(event);
    if (listeners) {
      listeners.delete(callback as (data: any) => void);
      if (listeners.size === 0) {
        this.eventListeners.delete(event);
      }
    }
  }

  /**
   * Emit custom event (for sending data to server if needed)
   */
  emit(event: string, data?: any): void {
    if (!this.isWebSocketConnected()) {
      logger.warn('Cannot emit event - WebSocket not connected', { event });
      return;
    }

    this.socket!.emit(event, data);
  }

  /**
   * Get connection status
   */
  getConnectionStatus(): {
    connected: boolean;
    reconnectAttempts: number;
    lastConnectedAt?: Date;
    lastDisconnectedAt?: Date;
  } {
    return {
      connected: this.isConnected,
      reconnectAttempts: this.reconnectAttempts,
      lastConnectedAt: this.socket?.connected ? new Date() : undefined,
      lastDisconnectedAt: !this.socket?.connected ? new Date() : undefined
    };
  }

  private setupSocketListeners(): void {
    if (!this.socket) return;

    // Connection events
    this.socket.on('connect', () => {
      this.isConnected = true;
      this.reconnectAttempts = 0;
      logger.info('WebSocket connected', { socketId: this.socket!.id });
      this.emitLocalEvent('connected', { socketId: this.socket!.id });
    });

    this.socket.on('disconnect', (reason) => {
      this.isConnected = false;
      logger.info('WebSocket disconnected', { reason });
      this.emitLocalEvent('disconnected', { reason });
    });

    this.socket.on('connect_error', (error) => {
      this.reconnectAttempts++;
      logger.error('WebSocket connection error', {
        error: error.message,
        attempt: this.reconnectAttempts,
        maxAttempts: this.maxReconnectAttempts
      });
      this.emitLocalEvent('connection_error', { error: error.message, attempt: this.reconnectAttempts });
    });

    this.socket.on('reconnect', (attemptNumber) => {
      this.isConnected = true;
      logger.info('WebSocket reconnected', { attemptNumber });
      this.emitLocalEvent('reconnected', { attemptNumber });
    });

    this.socket.on('reconnect_attempt', (attemptNumber) => {
      logger.info('WebSocket reconnection attempt', { attemptNumber });
      this.emitLocalEvent('reconnect_attempt', { attemptNumber });
    });

    this.socket.on('reconnect_error', (error) => {
      logger.error('WebSocket reconnection error', { error: error.message });
      this.emitLocalEvent('reconnect_error', { error: error.message });
    });

    this.socket.on('reconnect_failed', () => {
      logger.error('WebSocket reconnection failed');
      this.emitLocalEvent('reconnect_failed', {});
    });

    // Business events
    this.setupBusinessEventListeners();
  }

  private setupBusinessEventListeners(): void {
    if (!this.socket) return;

    // Campaign events
    this.socket.on(WebSocketEvents.CAMPAIGN_STATUS, (data: WebSocketEventData[WebSocketEvents.CAMPAIGN_STATUS]) => {
      logger.debug('Campaign status update received', data);
      this.emitLocalEvent(WebSocketEvents.CAMPAIGN_STATUS, data);
    });

    this.socket.on(WebSocketEvents.CAMPAIGN_UPDATED, (data: WebSocketEventData[WebSocketEvents.CAMPAIGN_UPDATED]) => {
      logger.debug('Campaign updated', data);
      this.emitLocalEvent(WebSocketEvents.CAMPAIGN_UPDATED, data);
    });

    // Session events
    this.socket.on(WebSocketEvents.SESSION_CREATED, (data: WebSocketEventData[WebSocketEvents.SESSION_CREATED]) => {
      logger.debug('Session created', data);
      this.emitLocalEvent(WebSocketEvents.SESSION_CREATED, data);
    });

    this.socket.on(WebSocketEvents.SESSION_UPDATED, (data: WebSocketEventData[WebSocketEvents.SESSION_UPDATED]) => {
      logger.debug('Session updated', data);
      this.emitLocalEvent(WebSocketEvents.SESSION_UPDATED, data);
    });

    this.socket.on(WebSocketEvents.SESSION_COMPLETED, (data: WebSocketEventData[WebSocketEvents.SESSION_COMPLETED]) => {
      logger.debug('Session completed', data);
      this.emitLocalEvent(WebSocketEvents.SESSION_COMPLETED, data);
    });

    // Analytics events
    this.socket.on(WebSocketEvents.ANALYTICS_UPDATED, (data: WebSocketEventData[WebSocketEvents.ANALYTICS_UPDATED]) => {
      logger.debug('Analytics updated', data);
      this.emitLocalEvent(WebSocketEvents.ANALYTICS_UPDATED, data);
    });

    // Call events
    this.socket.on(WebSocketEvents.CALL_RINGING, (data: WebSocketEventData[WebSocketEvents.CALL_RINGING]) => {
      logger.debug('Call ringing', data);
      this.emitLocalEvent(WebSocketEvents.CALL_RINGING, data);
    });

    this.socket.on(WebSocketEvents.CALL_ANSWERED, (data: WebSocketEventData[WebSocketEvents.CALL_ANSWERED]) => {
      logger.debug('Call answered', data);
      this.emitLocalEvent(WebSocketEvents.CALL_ANSWERED, data);
    });

    this.socket.on(WebSocketEvents.CALL_ENDED, (data: WebSocketEventData[WebSocketEvents.CALL_ENDED]) => {
      logger.debug('Call ended', data);
      this.emitLocalEvent(WebSocketEvents.CALL_ENDED, data);
    });

    // Audio processing events
    this.socket.on(WebSocketEvents.AUDIO_PROCESSED, (data: WebSocketEventData[WebSocketEvents.AUDIO_PROCESSED]) => {
      logger.debug('Audio processed', data);
      this.emitLocalEvent(WebSocketEvents.AUDIO_PROCESSED, data);
    });

    // Error events
    this.socket.on(WebSocketEvents.ERROR, (data: WebSocketEventData[WebSocketEvents.ERROR]) => {
      logger.error('WebSocket error received', data);
      this.emitLocalEvent(WebSocketEvents.ERROR, data);
    });

    // Welcome message
    this.socket.on('connected', (data) => {
      logger.info('WebSocket welcome message received', data);
      this.emitLocalEvent('connected', data);
    });
  }

  private emitLocalEvent(event: string, data: any): void {
    const listeners = this.eventListeners.get(event);
    if (listeners) {
      listeners.forEach(callback => {
        try {
          callback(data);
        } catch (error) {
          logger.error('Error in WebSocket event listener', { event, error });
        }
      });
    }
  }

  private initializeEventListeners(): void {
    // Initialize common event listener maps
    const events = [
      'connected',
      'disconnected',
      'connection_error',
      'reconnected',
      'reconnect_attempt',
      'reconnect_error',
      'reconnect_failed',
      WebSocketEvents.CAMPAIGN_STATUS,
      WebSocketEvents.CAMPAIGN_UPDATED,
      WebSocketEvents.SESSION_CREATED,
      WebSocketEvents.SESSION_UPDATED,
      WebSocketEvents.SESSION_COMPLETED,
      WebSocketEvents.ANALYTICS_UPDATED,
      WebSocketEvents.CALL_RINGING,
      WebSocketEvents.CALL_ANSWERED,
      WebSocketEvents.CALL_ENDED,
      WebSocketEvents.AUDIO_PROCESSED,
      WebSocketEvents.ERROR
    ];

    events.forEach(event => {
      this.eventListeners.set(event, new Set());
    });
  }
}

// Create singleton instance
export const webSocketService = new WebSocketService();
