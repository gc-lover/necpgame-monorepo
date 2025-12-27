export enum WebSocketEvents {
  // Client to server events
  CONNECTION = 'connection',
  DISCONNECT = 'disconnect',
  SUBSCRIBE_CAMPAIGN = 'subscribe:campaign',
  SUBSCRIBE_ANALYTICS = 'subscribe:analytics',
  SUBSCRIBE_SESSIONS = 'subscribe:sessions',
  UNSUBSCRIBE_CAMPAIGN = 'unsubscribe:campaign',
  UNSUBSCRIBE_ANALYTICS = 'unsubscribe:analytics',
  UNSUBSCRIBE_SESSIONS = 'unsubscribe:sessions',

  // Server to client events
  CAMPAIGN_STATUS = 'campaign:status',
  CAMPAIGN_UPDATED = 'campaign:updated',
  SESSION_CREATED = 'session:created',
  SESSION_UPDATED = 'session:updated',
  SESSION_COMPLETED = 'session:completed',
  ANALYTICS_UPDATED = 'analytics:updated',
  CALL_RINGING = 'call:ringing',
  CALL_ANSWERED = 'call:answered',
  CALL_ENDED = 'call:ended',
  AUDIO_PROCESSED = 'audio:processed',
  ERROR = 'error'
}

export interface WebSocketEventData {
  [WebSocketEvents.CAMPAIGN_STATUS]: {
    campaignId: string;
    status: 'draft' | 'scheduled' | 'running' | 'paused' | 'completed' | 'cancelled';
    progress?: number;
    totalContacts?: number;
    processedContacts?: number;
    timestamp: Date;
  };

  [WebSocketEvents.CAMPAIGN_UPDATED]: {
    campaignId: string;
    changes: Record<string, any>;
    timestamp: Date;
  };

  [WebSocketEvents.SESSION_CREATED]: {
    sessionId: string;
    campaignId?: string;
    customerId?: string;
    startTime: Date;
    status: 'ringing' | 'answered' | 'completed';
  };

  [WebSocketEvents.SESSION_UPDATED]: {
    sessionId: string;
    changes: Record<string, any>;
    timestamp: Date;
  };

  [WebSocketEvents.SESSION_COMPLETED]: {
    sessionId: string;
    duration: number;
    result: 'successful' | 'failed' | 'hung_up' | 'no_answer';
    summary?: string;
    timestamp: Date;
  };

  [WebSocketEvents.ANALYTICS_UPDATED]: {
    type: 'conversation' | 'sentiment' | 'agent_performance' | 'hourly_effectiveness';
    data: any;
    timestamp: Date;
  };

  [WebSocketEvents.CALL_RINGING]: {
    sessionId: string;
    phoneNumber: string;
    customerName?: string;
    campaignId?: string;
  };

  [WebSocketEvents.CALL_ANSWERED]: {
    sessionId: string;
    duration: number;
    timestamp: Date;
  };

  [WebSocketEvents.CALL_ENDED]: {
    sessionId: string;
    duration: number;
    result: 'successful' | 'failed' | 'hung_up';
    timestamp: Date;
  };

  [WebSocketEvents.AUDIO_PROCESSED]: {
    sessionId: string;
    transcription?: string;
    sentiment?: {
      overall: 'positive' | 'neutral' | 'negative';
      confidence: number;
    };
    protocolCompliance?: boolean;
    summary?: string;
    timestamp: Date;
  };

  [WebSocketEvents.ERROR]: {
    message: string;
    code?: string;
    timestamp: Date;
  };
}
