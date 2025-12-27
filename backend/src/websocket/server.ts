import { Server as SocketIOServer, Socket } from 'socket.io';
import { Server as HttpServer } from 'http';
import jwt from 'jsonwebtoken';
import { logger } from '../utils/logger';
import { authService } from '../services/authService';
import { UserRole } from '../types/auth';
import { WebSocketEvents, WebSocketEventData } from '../types/websocket';

export interface AuthenticatedSocket extends Socket {
  userId?: string;
  userRole?: UserRole;
  userPermissions?: string[];
}

export class WebSocketServer {
  private io: SocketIOServer;
  private connectedClients: Map<string, AuthenticatedSocket> = new Map();

  constructor(httpServer: HttpServer) {
    this.io = new SocketIOServer(httpServer, {
      cors: {
        origin: process.env.CORS_ORIGIN || "http://localhost:3000",
        methods: ["GET", "POST"],
        credentials: true
      },
      transports: ['websocket', 'polling']
    });

    this.initializeMiddleware();
    this.initializeEventHandlers();
  }

  private initializeMiddleware(): void {
    // JWT Authentication middleware
    this.io.use(async (socket: AuthenticatedSocket, next) => {
      try {
        const token = socket.handshake.auth.token || socket.handshake.query.token as string;

        if (!token) {
          logger.warn('WebSocket connection attempt without token', {
            socketId: socket.id,
            ip: socket.handshake.address
          });
          return next(new Error('Authentication token required'));
        }

        // Verify JWT token
        const decoded = jwt.verify(token, process.env.JWT_ACCESS_SECRET!) as any;

        // Get user details from database
        const user = await authService.getUserById(decoded.userId);
        if (!user) {
          logger.warn('WebSocket authentication failed - user not found', {
            socketId: socket.id,
            userId: decoded.userId
          });
          return next(new Error('User not found'));
        }

        // Attach user info to socket
        socket.userId = user.id;
        socket.userRole = user.role;
        socket.userPermissions = user.permissions || [];

        logger.info('WebSocket client authenticated', {
          socketId: socket.id,
          userId: user.id,
          userRole: user.role
        });

        next();
      } catch (error) {
        logger.error('WebSocket authentication error', {
          socketId: socket.id,
          error: error instanceof Error ? error.message : 'Unknown error'
        });
        next(new Error('Authentication failed'));
      }
    });

    // Rate limiting middleware (simple implementation)
    const clientConnections = new Map<string, { count: number; resetTime: number }>();

    this.io.use((socket: AuthenticatedSocket, next) => {
      const userId = socket.userId!;
      const now = Date.now();
      const windowMs = 60000; // 1 minute
      const maxConnections = 5;

      const userConnections = clientConnections.get(userId) || { count: 0, resetTime: now + windowMs };

      if (now > userConnections.resetTime) {
        userConnections.count = 0;
        userConnections.resetTime = now + windowMs;
      }

      if (userConnections.count >= maxConnections) {
        logger.warn('WebSocket rate limit exceeded', {
          socketId: socket.id,
          userId
        });
        return next(new Error('Rate limit exceeded'));
      }

      userConnections.count++;
      clientConnections.set(userId, userConnections);

      next();
    });
  }

  private initializeEventHandlers(): void {
    this.io.on(WebSocketEvents.CONNECTION, (socket: AuthenticatedSocket) => {
      const userId = socket.userId!;
      this.connectedClients.set(socket.id, socket);

      logger.info('WebSocket client connected', {
        socketId: socket.id,
        userId,
        totalClients: this.connectedClients.size
      });

      // Handle subscription events
      socket.on(WebSocketEvents.SUBSCRIBE_CAMPAIGN, (campaignId: string) => {
        this.handleSubscribeCampaign(socket, campaignId);
      });

      socket.on(WebSocketEvents.SUBSCRIBE_ANALYTICS, (filters?: any) => {
        this.handleSubscribeAnalytics(socket, filters);
      });

      socket.on(WebSocketEvents.SUBSCRIBE_SESSIONS, (filters?: any) => {
        this.handleSubscribeSessions(socket, filters);
      });

      // Handle unsubscription events
      socket.on(WebSocketEvents.UNSUBSCRIBE_CAMPAIGN, (campaignId: string) => {
        this.handleUnsubscribeCampaign(socket, campaignId);
      });

      socket.on(WebSocketEvents.UNSUBSCRIBE_ANALYTICS, () => {
        this.handleUnsubscribeAnalytics(socket);
      });

      socket.on(WebSocketEvents.UNSUBSCRIBE_SESSIONS, () => {
        this.handleUnsubscribeSessions(socket);
      });

      // Handle disconnection
      socket.on(WebSocketEvents.DISCONNECT, (reason: string) => {
        this.handleDisconnect(socket, reason);
      });

      // Send welcome message
      socket.emit('connected', {
        userId,
        timestamp: new Date(),
        message: 'Successfully connected to Telconet Voice Platform'
      });
    });
  }

  private handleSubscribeCampaign(socket: AuthenticatedSocket, campaignId: string): void {
    const roomName = `campaign:${campaignId}`;
    socket.join(roomName);

    logger.info('Client subscribed to campaign', {
      socketId: socket.id,
      userId: socket.userId,
      campaignId,
      room: roomName
    });

    socket.emit('subscribed:campaign', {
      campaignId,
      timestamp: new Date()
    });
  }

  private handleSubscribeAnalytics(socket: AuthenticatedSocket, filters?: any): void {
    const roomName = `analytics:${socket.userId}`;
    socket.join(roomName);

    // Join role-based analytics rooms if applicable
    if (socket.userRole === 'admin' || socket.userRole === 'manager') {
      socket.join('analytics:global');
    }

    logger.info('Client subscribed to analytics', {
      socketId: socket.id,
      userId: socket.userId,
      filters,
      room: roomName
    });

    socket.emit('subscribed:analytics', {
      filters,
      timestamp: new Date()
    });
  }

  private handleSubscribeSessions(socket: AuthenticatedSocket, filters?: any): void {
    const roomName = `sessions:${socket.userId}`;
    socket.join(roomName);

    // Join campaign-specific session rooms if filters specify campaign
    if (filters?.campaignId) {
      socket.join(`sessions:campaign:${filters.campaignId}`);
    }

    logger.info('Client subscribed to sessions', {
      socketId: socket.id,
      userId: socket.userId,
      filters,
      room: roomName
    });

    socket.emit('subscribed:sessions', {
      filters,
      timestamp: new Date()
    });
  }

  private handleUnsubscribeCampaign(socket: AuthenticatedSocket, campaignId: string): void {
    const roomName = `campaign:${campaignId}`;
    socket.leave(roomName);

    logger.info('Client unsubscribed from campaign', {
      socketId: socket.id,
      userId: socket.userId,
      campaignId,
      room: roomName
    });
  }

  private handleUnsubscribeAnalytics(socket: AuthenticatedSocket): void {
    const roomName = `analytics:${socket.userId}`;
    socket.leave(roomName);
    socket.leave('analytics:global');

    logger.info('Client unsubscribed from analytics', {
      socketId: socket.id,
      userId: socket.userId,
      room: roomName
    });
  }

  private handleUnsubscribeSessions(socket: AuthenticatedSocket): void {
    const roomName = `sessions:${socket.userId}`;
    socket.leave(roomName);

    // Leave all campaign-specific session rooms
    const campaignRooms = Array.from(socket.rooms).filter(room =>
      room.startsWith('sessions:campaign:')
    );
    campaignRooms.forEach(room => socket.leave(room));

    logger.info('Client unsubscribed from sessions', {
      socketId: socket.id,
      userId: socket.userId,
      room: roomName
    });
  }

  private handleDisconnect(socket: AuthenticatedSocket, reason: string): void {
    const userId = socket.userId!;
    this.connectedClients.delete(socket.id);

    logger.info('WebSocket client disconnected', {
      socketId: socket.id,
      userId,
      reason,
      remainingClients: this.connectedClients.size
    });
  }

  // Public methods for emitting events to clients

  public emitToCampaign(campaignId: string, event: WebSocketEvents, data: any): void {
    const roomName = `campaign:${campaignId}`;
    this.io.to(roomName).emit(event, data);

    logger.debug('Emitted event to campaign room', {
      room: roomName,
      event,
      dataKeys: Object.keys(data)
    });
  }

  public emitToUser(userId: string, event: WebSocketEvents, data: any): void {
    // Find all sockets for this user
    const userSockets = Array.from(this.connectedClients.values())
      .filter(socket => socket.userId === userId);

    userSockets.forEach(socket => {
      socket.emit(event, data);
    });

    logger.debug('Emitted event to user', {
      userId,
      event,
      socketCount: userSockets.length,
      dataKeys: Object.keys(data)
    });
  }

  public emitToRole(role: UserRole, event: WebSocketEvents, data: any): void {
    const roleSockets = Array.from(this.connectedClients.values())
      .filter(socket => socket.userRole === role);

    roleSockets.forEach(socket => {
      socket.emit(event, data);
    });

    logger.debug('Emitted event to role', {
      role,
      event,
      socketCount: roleSockets.length,
      dataKeys: Object.keys(data)
    });
  }

  public emitGlobal(event: WebSocketEvents, data: any): void {
    this.io.emit(event, data);

    logger.debug('Emitted global event', {
      event,
      dataKeys: Object.keys(data)
    });
  }

  public getConnectedClientsCount(): number {
    return this.connectedClients.size;
  }

  public getClientsByUser(userId: string): AuthenticatedSocket[] {
    return Array.from(this.connectedClients.values())
      .filter(socket => socket.userId === userId);
  }

  public getClientsByRole(role: UserRole): AuthenticatedSocket[] {
    return Array.from(this.connectedClients.values())
      .filter(socket => socket.userRole === role);
  }

  public async close(): Promise<void> {
    logger.info('Closing WebSocket server...');

    // Disconnect all clients
    for (const socket of this.connectedClients.values()) {
      socket.disconnect(true);
    }

    this.connectedClients.clear();
    await new Promise<void>((resolve) => {
      this.io.close(() => {
        logger.info('WebSocket server closed');
        resolve();
      });
    });
  }
}
