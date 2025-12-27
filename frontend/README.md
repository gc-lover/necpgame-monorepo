# Telconet Voice Automation Platform - Frontend

A modern React TypeScript frontend application for AI-powered voice automation platform.

## 🚀 Features

- **Real-time Dashboard**: Live campaign monitoring and analytics
- **Flow Builder**: Visual drag-and-drop conversation flow designer
- **Campaign Management**: Create and manage voice campaigns
- **Audio Processing**: Real-time transcription and sentiment analysis
- **WebSocket Integration**: Real-time updates and notifications
- **Responsive Design**: Mobile-first design with Tailwind CSS
- **Type Safety**: Full TypeScript support with strict typing

## 🛠️ Tech Stack

- **React 18** - UI framework
- **TypeScript** - Type safety
- **Vite** - Build tool and dev server
- **Tailwind CSS** - Styling
- **React Router** - Client-side routing
- **TanStack Query** - Data fetching and caching
- **Zustand** - State management
- **Socket.IO** - Real-time communication
- **React Flow** - Flow builder visualization
- **Recharts** - Data visualization
- **Wavesurfer.js** - Audio waveform visualization
- **React Hook Form** - Form management
- **Zod** - Schema validation

## 📋 Prerequisites

- Node.js 18+ and npm
- Backend API running on `http://localhost:8000`
- WebSocket server running on `ws://localhost:8000`

## 🚀 Getting Started

1. **Install dependencies:**
   ```bash
   npm install
   ```

2. **Environment setup:**
   ```bash
   cp env.example .env
   # Edit .env with your configuration
   ```

3. **Start development server:**
   ```bash
   npm run dev
   ```

4. **Open in browser:**
   ```
   http://localhost:3000
   ```

## 📜 Available Scripts

- `npm run dev` - Start development server
- `npm run build` - Build for production
- `npm run preview` - Preview production build
- `npm run lint` - Run ESLint
- `npm run lint:fix` - Fix ESLint issues
- `npm run format` - Format code with Prettier
- `npm run type-check` - Run TypeScript type checking
- `npm run test` - Run tests
- `npm run test:ui` - Run tests with UI

## 📁 Project Structure

```
src/
├── components/          # Reusable UI components
│   ├── common/         # Generic components (Button, Input, etc.)
│   ├── charts/         # Chart components
│   ├── campaigns/      # Campaign-specific components
│   ├── flows/          # Flow builder components
│   ├── conversations/  # Conversation components
│   └── layouts/        # Layout components
├── hooks/              # Custom React hooks
├── pages/              # Page components
├── services/           # API and external service integrations
├── stores/             # Zustand state stores
├── types/              # TypeScript type definitions
├── utils/              # Utility functions
├── test/               # Test utilities and setup
├── App.tsx             # Main app component
├── main.tsx            # App entry point
└── index.css           # Global styles
```

## 🔧 Configuration

### Environment Variables

Copy `env.example` to `.env` and configure:

```env
# API Configuration
VITE_API_URL=http://localhost:8000
VITE_WS_URL=ws://localhost:8000

# Feature Flags
VITE_ENABLE_ANALYTICS=true
VITE_ENABLE_WEBSOCKET=true

# UI Configuration
VITE_DEFAULT_THEME=light
```

### Tailwind CSS

Custom theme colors and utilities are defined in `tailwind.config.js`.

## 🧪 Testing

```bash
# Run all tests
npm run test

# Run tests with coverage
npm run test:run -- --coverage

# Run tests in UI mode
npm run test:ui
```

## 📦 Build & Deployment

```bash
# Build for production
npm run build

# Preview production build
npm run preview
```

The built files will be in the `dist` directory.

## 🔒 Security

- All API calls include JWT authentication
- WebSocket connections are authenticated
- Input validation using Zod schemas
- CSRF protection on forms
- Content Security Policy headers

## 🌐 Browser Support

- Chrome 90+
- Firefox 88+
- Safari 14+
- Edge 90+

## 🤝 Contributing

1. Follow the existing code style
2. Write tests for new features
3. Update documentation as needed
4. Use conventional commits

## 📄 License

MIT License - see LICENSE file for details.

## 📞 Support

For support and questions:
- Create an issue on GitHub
- Contact the development team

---

Built with ❤️ for Telconet Voice Automation Platform
