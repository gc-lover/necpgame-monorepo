// User and Authentication Types
export interface User {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  role: UserRole;
  permissions: string[];
  isActive: boolean;
  lastLoginAt?: Date;
  createdAt: Date;
  updatedAt: Date;
}

export enum UserRole {
  ADMIN = 'admin',
  MANAGER = 'manager',
  AGENT = 'agent',
  VIEWER = 'viewer'
}

export interface Permission {
  id: string;
  name: string;
  description: string;
  resource: string;
  action: string;
  createdAt: Date;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  user: User;
  accessToken: string;
  refreshToken: string;
  expiresIn: number;
}

export interface RegisterRequest {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
}

export interface AuthTokens {
  accessToken: string;
  refreshToken: string;
}

// Campaign Types
export interface Campaign {
  id: string;
  name: string;
  description?: string;
  status: CampaignStatus;
  type: CampaignType;
  flowId: string;
  flowVersionId?: string;
  rules: CampaignRules;
  settings: CampaignSettings;
  statistics: CampaignStatistics;
  createdBy: string;
  createdAt: Date;
  updatedAt: Date;
  scheduledAt?: Date;
  startedAt?: Date;
  completedAt?: Date;
}

export enum CampaignStatus {
  DRAFT = 'draft',
  SCHEDULED = 'scheduled',
  RUNNING = 'running',
  PAUSED = 'paused',
  COMPLETED = 'completed',
  CANCELLED = 'cancelled'
}

export enum CampaignType {
  DEBT_COLLECTION = 'debt_collection',
  PAYMENT_REMINDER = 'payment_reminder',
  CUSTOMER_SERVICE = 'customer_service',
  SURVEY = 'survey',
  MARKETING = 'marketing'
}

export interface CampaignRules {
  maxAttempts: number;
  timeWindows: TimeWindow[];
  retryStrategy: RetryStrategy;
  stopConditions: StopCondition[];
}

export interface TimeWindow {
  daysOfWeek: number[]; // 0 = Sunday, 1 = Monday, etc.
  startTime: Date;
  endTime: Date;
}

export interface RetryStrategy {
  delays: number[]; // delays in minutes between attempts
  exponentialBackoff: boolean;
  maxDelay: number;
}

export interface StopCondition {
  type: 'successful_payment' | 'contact_limit' | 'time_limit' | 'budget_limit';
  value: number;
}

export interface CampaignSettings {
  priority: 'low' | 'normal' | 'high';
  concurrentCalls: number;
  recordingEnabled: boolean;
  transcriptionEnabled: boolean;
  sentimentAnalysisEnabled: boolean;
  protocolComplianceEnabled: boolean;
}

export interface CampaignStatistics {
  totalContacts: number;
  processedContacts: number;
  successfulContacts: number;
  failedContacts: number;
  pendingContacts: number;
  successRate: number;
  averageCallDuration: number;
  totalCallDuration: number;
}

export interface CampaignContact {
  id: string;
  campaignId: string;
  customerId?: string;
  phoneNumber: string;
  customerName?: string;
  debtAmount?: number;
  lastPaymentDate?: Date;
  status: ContactStatus;
  attemptCount: number;
  lastAttemptAt?: Date;
  nextAttemptAt?: Date;
  notes?: string;
  metadata?: Record<string, any>;
}

export enum ContactStatus {
  PENDING = 'pending',
  PROCESSING = 'processing',
  COMPLETED = 'completed',
  FAILED = 'failed',
  SKIPPED = 'skipped'
}

// Flow and Conversation Types
export interface Flow {
  id: string;
  name: string;
  description?: string;
  type: FlowType;
  category: string;
  tags: string[];
  isActive: boolean;
  versions: FlowVersion[];
  createdBy: string;
  createdAt: Date;
  updatedAt: Date;
}

export enum FlowType {
  VOICE = 'voice',
  CHAT = 'chat',
  HYBRID = 'hybrid'
}

export interface FlowVersion {
  id: string;
  flowId: string;
  version: string;
  status: FlowVersionStatus;
  nodes: FlowNode[];
  connections: FlowConnection[];
  variables: FlowVariable[];
  settings: FlowSettings;
  trainingData?: TrainingData;
  createdBy: string;
  createdAt: Date;
  updatedAt: Date;
  publishedAt?: Date;
}

export enum FlowVersionStatus {
  DRAFT = 'draft',
  TRAINING = 'training',
  TRAINED = 'trained',
  PUBLISHED = 'published',
  ARCHIVED = 'archived'
}

export interface FlowNode {
  id: string;
  type: FlowNodeType;
  position: { x: number; y: number };
  data: FlowNodeData;
  style?: Record<string, any>;
}

export enum FlowNodeType {
  START = 'start',
  MESSAGE = 'message',
  QUESTION = 'question',
  CONDITION = 'condition',
  ACTION = 'action',
  TRANSFER = 'transfer',
  END = 'end'
}

export interface FlowNodeData {
  label: string;
  message?: string;
  options?: string[];
  condition?: string;
  action?: string;
  variables?: Record<string, any>;
  settings?: Record<string, any>;
}

export interface FlowConnection {
  id: string;
  source: string;
  target: string;
  sourceHandle?: string;
  targetHandle?: string;
  label?: string;
}

export interface FlowVariable {
  id: string;
  name: string;
  type: 'string' | 'number' | 'boolean' | 'date' | 'array' | 'object';
  defaultValue?: any;
  required: boolean;
  description?: string;
}

export interface FlowSettings {
  language: string;
  voice?: string;
  speed?: number;
  timeout?: number;
  maxRetries?: number;
  fallbackMessage?: string;
}

export interface TrainingData {
  examples: TrainingExample[];
  intents: Intent[];
  entities: Entity[];
  metrics?: TrainingMetrics;
}

export interface TrainingExample {
  input: string;
  intent: string;
  entities: { entity: string; value: string; start: number; end: number }[];
  response?: string;
}

export interface Intent {
  name: string;
  description?: string;
  examples: string[];
}

export interface Entity {
  name: string;
  type: 'system' | 'custom';
  values: string[];
}

export interface TrainingMetrics {
  accuracy: number;
  precision: number;
  recall: number;
  f1Score: number;
}

// Session and Conversation Types
export interface Session {
  id: string;
  campaignId?: string;
  contactId?: string;
  customerId?: string;
  phoneNumber: string;
  customerName?: string;
  status: SessionStatus;
  startTime: Date;
  endTime?: Date;
  duration?: number;
  result?: CallResult;
  flowId?: string;
  flowVersionId?: string;
  messages: Message[];
  audioFileId?: string;
  transcription?: string;
  sentimentAnalysis?: SentimentAnalysis;
  protocolCompliance?: ProtocolCompliance;
  summary?: string;
  metadata?: Record<string, any>;
  createdAt: Date;
  updatedAt: Date;
}

export enum SessionStatus {
  RINGING = 'ringing',
  ANSWERED = 'answered',
  ACTIVE = 'active',
  COMPLETED = 'completed',
  FAILED = 'failed',
  HUNG_UP = 'hung_up',
  NO_ANSWER = 'no_answer'
}

export enum CallResult {
  SUCCESSFUL = 'successful',
  FAILED = 'failed',
  HUNG_UP = 'hung_up',
  NO_ANSWER = 'no_answer',
  TRANSFERRED = 'transferred'
}

export interface Message {
  id: string;
  sessionId: string;
  type: MessageType;
  content: string;
  sender: 'agent' | 'customer' | 'system';
  timestamp: Date;
  metadata?: Record<string, any>;
}

export enum MessageType {
  TEXT = 'text',
  AUDIO = 'audio',
  SYSTEM = 'system'
}

export interface SentimentAnalysis {
  overall: 'positive' | 'neutral' | 'negative';
  confidence: number;
  segments: SentimentSegment[];
  trend: 'improving' | 'stable' | 'declining';
}

export interface SentimentSegment {
  start: number;
  end: number;
  sentiment: 'positive' | 'neutral' | 'negative';
  confidence: number;
  text?: string;
}

export interface ProtocolCompliance {
  compliant: boolean;
  violations: ProtocolViolation[];
  score: number;
  requiredSteps: string[];
  completedSteps: string[];
}

export interface ProtocolViolation {
  type: string;
  description: string;
  timestamp: number;
  severity: 'low' | 'medium' | 'high';
}

// Analytics Types
export interface AnalyticsData {
  conversationMetrics: ConversationMetrics;
  sentimentDistribution: SentimentDistribution;
  agentPerformance: AgentPerformance[];
  hourlyEffectiveness: HourlyEffectiveness[];
  paymentFailures: PaymentFailureData;
  dashboard: DashboardData;
  dateRange: DateRange;
  generatedAt: Date;
}

export interface ConversationMetrics {
  totalConversations: number;
  successfulConversations: number;
  failedConversations: number;
  successRate: number;
  averageDuration: number;
  totalDuration: number;
}

export interface SentimentDistribution {
  positive: { count: number; percentage: number };
  neutral: { count: number; percentage: number };
  negative: { count: number; percentage: number };
}

export interface AgentPerformance {
  agentId: string;
  agentName: string;
  totalCalls: number;
  successfulCalls: number;
  averageDuration: number;
  protocolComplianceRate: number;
  customerSatisfactionScore: number;
  conversionRate: number;
}

export interface HourlyEffectiveness {
  hour: number;
  totalCalls: number;
  successfulCalls: number;
  averageDuration: number;
  successRate: number;
}

export interface PaymentFailureData {
  totalPaymentAttempts: number;
  failedPayments: number;
  failureRate: number;
  commonFailureReasons: string[];
}

export interface DashboardData {
  kpis: KPI[];
  charts: ChartData[];
  recentActivity: Activity[];
}

export interface KPI {
  label: string;
  value: number | string;
  change?: number;
  changeType?: 'increase' | 'decrease' | 'neutral';
  format?: 'number' | 'percentage' | 'currency' | 'duration';
}

export interface ChartData {
  id: string;
  type: 'line' | 'bar' | 'pie' | 'area';
  title: string;
  data: any[];
  config?: Record<string, any>;
}

export interface Activity {
  id: string;
  type: 'call_started' | 'call_completed' | 'campaign_started' | 'campaign_completed';
  description: string;
  timestamp: Date;
  metadata?: Record<string, any>;
}

// Audio and File Types
export interface AudioFile {
  id: string;
  sessionId: string;
  filename: string;
  originalName: string;
  mimeType: string;
  size: number;
  duration?: number;
  url: string;
  signedUrl?: string;
  uploadedAt: Date;
  processedAt?: Date;
  metadata?: Record<string, any>;
}

export interface AudioProcessingResult {
  sessionId: string;
  transcription?: string;
  sentiment?: SentimentAnalysis;
  protocolCompliance?: ProtocolCompliance;
  summary?: string;
  duration?: number;
  confidence?: number;
}

// Webhook Types
export interface Webhook {
  id: string;
  name: string;
  url: string;
  events: WebhookEvent[];
  secret: string;
  isActive: boolean;
  headers?: Record<string, string>;
  retryPolicy: RetryPolicy;
  createdAt: Date;
  updatedAt: Date;
}

export enum WebhookEvent {
  CAMPAIGN_STARTED = 'campaign.started',
  CAMPAIGN_COMPLETED = 'campaign.completed',
  SESSION_STARTED = 'session.started',
  SESSION_COMPLETED = 'session.completed',
  CALL_ANSWERED = 'call.answered',
  CALL_ENDED = 'call.ended',
  PAYMENT_RECEIVED = 'payment.received',
  PROTOCOL_VIOLATION = 'protocol.violation'
}

export interface RetryPolicy {
  maxAttempts: number;
  backoffStrategy: 'linear' | 'exponential';
  baseDelay: number;
  maxDelay: number;
}

export interface WebhookDelivery {
  id: string;
  webhookId: string;
  event: WebhookEvent;
  payload: any;
  status: 'pending' | 'delivered' | 'failed';
  attempts: WebhookAttempt[];
  createdAt: Date;
  deliveredAt?: Date;
}

export interface WebhookAttempt {
  id: string;
  deliveryId: string;
  attemptNumber: number;
  status: 'success' | 'failed';
  statusCode?: number;
  response?: string;
  error?: string;
  attemptedAt: Date;
  duration: number;
}

// System Configuration Types
export interface SystemConfig {
  id: string;
  key: string;
  value: any;
  type: 'string' | 'number' | 'boolean' | 'object' | 'array';
  description?: string;
  isPublic: boolean;
  updatedAt: Date;
  updatedBy: string;
}

// API Response Types
export interface ApiResponse<T = any> {
  data: T;
  message?: string;
  success: boolean;
  timestamp: Date;
}

export interface PaginatedResponse<T> extends ApiResponse<T[]> {
  pagination: {
    page: number;
    limit: number;
    total: number;
    totalPages: number;
    hasNext: boolean;
    hasPrev: boolean;
  };
}

export interface ApiError {
  message: string;
  code: string;
  details?: any;
  timestamp: Date;
}

// Form Types
export interface FormField {
  name: string;
  label: string;
  type: 'text' | 'email' | 'password' | 'number' | 'select' | 'textarea' | 'checkbox' | 'radio' | 'file' | 'date' | 'time';
  required?: boolean;
  placeholder?: string;
  options?: { label: string; value: any }[];
  validation?: Record<string, any>;
  disabled?: boolean;
  hidden?: boolean;
}

export interface FormData {
  [key: string]: any;
}

// Utility Types
export interface DateRange {
  startDate: Date;
  endDate: Date;
}

export interface FilterOptions {
  dateRange?: DateRange;
  campaignId?: string;
  status?: string[];
  type?: string[];
  search?: string;
  sortBy?: string;
  sortOrder?: 'asc' | 'desc';
}

export interface PaginationOptions {
  page?: number;
  limit?: number;
}

// WebSocket Types
export interface WebSocketMessage {
  type: string;
  payload: any;
  timestamp: Date;
}

export interface RealTimeUpdate {
  type: 'campaign' | 'session' | 'analytics' | 'system';
  action: 'created' | 'updated' | 'deleted' | 'status_changed';
  data: any;
  timestamp: Date;
}

// UI State Types
export interface UIState {
  sidebarOpen: boolean;
  theme: 'light' | 'dark';
  loading: boolean;
  notifications: Notification[];
  modals: ModalState[];
}

export interface Notification {
  id: string;
  type: 'success' | 'error' | 'warning' | 'info';
  title: string;
  message?: string;
  duration?: number;
  action?: {
    label: string;
    onClick: () => void;
  };
}

export interface ModalState {
  id: string;
  type: string;
  props: Record<string, any>;
  isOpen: boolean;
}

// Export all types
export type {
  User,
  Permission,
  Campaign,
  CampaignContact,
  Flow,
  FlowVersion,
  Session,
  AnalyticsData,
  AudioFile,
  Webhook,
  SystemConfig,
  ApiResponse,
  PaginatedResponse,
  ApiError,
  WebSocketMessage,
  RealTimeUpdate,
  UIState
};
