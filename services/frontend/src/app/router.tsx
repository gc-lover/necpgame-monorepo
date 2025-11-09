import { createBrowserRouter, Navigate } from 'react-router-dom'
import { GameLayout } from '../components/layout/GameLayout'
import { Header } from '../shared/components/layout/Header'
import { CharactersPage } from '../features/characters/pages/CharactersPage'
import { AuthenticationPage } from '../features/auth/authentication/pages/AuthenticationPage'
import { CharacterCreationPage } from '../features/characters/pages/CharacterCreationPage'
import { WelcomePage } from '../features/game/pages/WelcomePage'
import { GameplayPage } from '../features/game/pages/GameplayPage'
import { ActionsPage } from '../features/gameplay/actions/pages/ActionsPage'
import { InitialStatePage } from '../features/game/initial-state/pages/InitialStatePage'
import { ImplantsPage } from '../features/gameplay/implants/pages/ImplantsPage'
import { CyberpsychosisPage } from '../features/gameplay/cyberpsychosis/pages/CyberpsychosisPage'
import { QuestsPage } from '../features/gameplay/quests/pages/QuestsPage'
import { InventoryPage } from '../features/gameplay/inventory/pages/InventoryPage'
import { NPCsPage } from '../features/gameplay/npcs/pages/NPCsPage'
import { BlueprintsPage } from '../modules/social/personal-npc/pages/BlueprintsPage'
import { BlueprintDetailPage } from '../modules/social/personal-npc/pages/BlueprintDetailPage'
import { ExecuteScenarioPage } from '../modules/social/personal-npc/pages/ExecuteScenarioPage'
import { LocationsPage } from '../features/gameplay/locations/pages/LocationsPage'
import { TradingPage } from '../features/gameplay/trading/pages/TradingPage'
import { CharacterStatusPage } from '../features/character/status/pages/CharacterStatusPage'
import { EventsPage } from '../features/gameplay/events/pages/EventsPage'
import { WeaponsPage } from '../features/gameplay/weapons/pages/WeaponsPage'
import { AbilitiesPage } from '../features/gameplay/abilities/pages/AbilitiesPage'
import { AbilitiesCatalogPage } from '../features/gameplay/abilities-catalog/pages/AbilitiesCatalogPage'
import { CombatRolesPage } from '../features/gameplay/combat-roles/pages/CombatRolesPage'
import { CombatSessionPage } from '../features/gameplay/combat-session/pages/CombatSessionPage'
import { AIEnemiesPage } from '../features/gameplay/ai-enemies/pages/AIEnemiesPage'
import { CombosPage } from '../features/gameplay/combos/pages/CombosPage'
import { ExtractionPage } from '../features/gameplay/extraction/pages/ExtractionPage'
import { ImplantsCatalogPage } from '../features/gameplay/implants-catalog/pages/ImplantsCatalogPage'
import { ShootingPage } from '../features/gameplay/shooting/pages/ShootingPage'
import { CombatPage } from '../features/gameplay/combat/pages/CombatPage'
import { ClassesProgressionPage } from '../features/gameplay/classes-progression/pages/ClassesProgressionPage'
import { GlobalStatePage } from '../features/technical/global-state/pages/GlobalStatePage'
import { ReputationTiersPage } from '../features/gameplay/reputation-tiers/pages/ReputationTiersPage'
import { CraftingPage } from '../features/gameplay/crafting/pages/CraftingPage'
import { CurrenciesPage } from '../features/gameplay/currencies/pages/CurrenciesPage'
import { SkillsPage } from '../features/gameplay/skills/pages/SkillsPage'
import { PerksPage } from '../features/gameplay/perks/pages/PerksPage'
import { LeagueSystemPage } from '../features/meta/league-system/pages/LeagueSystemPage'
import { WorldStatePage } from '../features/gameplay/world-state/pages/WorldStatePage'
import { RelationshipsPage } from '../features/gameplay/relationships/pages/RelationshipsPage'
import { RomanceSystemPage } from '../features/gameplay/romance-system/pages/RomanceSystemPage'
import { ProgressionDetailedPage } from '../features/gameplay/progression-detailed/pages/ProgressionDetailedPage'
import { ProgressionBackendPage } from '../features/gameplay/progression-backend/pages/ProgressionBackendPage'
import { NarrativeCoherencePage } from '../features/narrative/narrative-coherence/pages/NarrativeCoherencePage'
import { QuestCatalogPage } from '../features/narrative/quest-catalog/pages/QuestCatalogPage'
import { StarterContentPage } from '../features/narrative/starter-content/pages/StarterContentPage'
import { RegionalQuestsPage } from '../features/narrative/regional-quests/pages/RegionalQuestsPage'
import { FactionQuestsPage } from '../features/narrative/faction-quests/pages/FactionQuestsPage'
import { GlobalStateExtendedPage } from '../features/technical/global-state-extended/pages/GlobalStateExtendedPage'
import { UISystemsPage } from '../features/technical/ui-systems/pages/UISystemsPage'
import { DisasterRecoveryPage } from '../features/technical/disaster-recovery/pages/DisasterRecoveryPage'
import { ConfigurationManagementPage } from '../features/technical/configuration-management/pages/ConfigurationManagementPage'
import { IncidentResponsePage } from '../features/technical/incident-response/pages/IncidentResponsePage'
import { VoiceChatPage } from '../features/technical/voice-chat/pages/VoiceChatPage'
import { RealtimeServerZonesPage } from '../features/technical/realtime-server-zones/pages/RealtimeServerZonesPage'
import { SessionLifecyclePage } from '../features/technical/session-lifecycle/pages/SessionLifecyclePage'
import { MatchmakingAlgorithmPage } from '../features/gameplay/matchmaking-algorithm/pages/MatchmakingAlgorithmPage'
import { AIAlgorithmsPage } from '../features/internal/ai-algorithms/pages/AIAlgorithmsPage'
import { LoreDatabasePage } from '../features/lore/lore-database/pages/LoreDatabasePage'
import { CyberspacePage } from '../features/gameplay/cyberspace/pages/CyberspacePage'
import { HackingPage } from '../features/gameplay/hacking/pages/HackingPage'
import { StealthPage } from '../features/gameplay/stealth/pages/StealthPage'
import { FreerunPage } from '../features/gameplay/freerun/pages/FreerunPage'
import { FactionWarsPage } from '../features/gameplay/faction-wars/pages/FactionWarsPage'
import { AuctionHousePage } from '../features/gameplay/auction-house/pages/AuctionHousePage'
import { StockExchangePage } from '../features/gameplay/stock-exchange/pages/StockExchangePage'
import { DnDChecksPage } from '../features/gameplay/dnd-checks/pages/DnDChecksPage'
import { NPCHiringPage } from '../features/gameplay/npc-hiring/pages/NPCHiringPage'
import { GlobalEventsPage } from '../features/gameplay/global-events/pages/GlobalEventsPage'
import { PlayerMarketPage } from '../features/gameplay/player-market/pages/PlayerMarketPage'
import { LootTablesPage } from '../features/gameplay/loot-tables/pages/LootTablesPage'
import { HackingNetworksPage } from '../features/gameplay/hacking-networks/pages/HackingNetworksPage'
import { Events2020_2040Page } from '../features/gameplay/events-2020-2040/pages/Events2020_2040Page'
import { Events2040_2060Page } from '../features/gameplay/events-2040-2060/pages/Events2040_2060Page'
import { Events2060_2077Page } from '../features/gameplay/events-2060-2077/pages/Events2060_2077Page'
import { TradingGuildsPage } from '../features/gameplay/trading-guilds/pages/TradingGuildsPage'
import { TradingRoutesPage } from '../features/gameplay/trading-routes/pages/TradingRoutesPage'
import { CurrencyExchangePage } from '../features/gameplay/currency-exchange/pages/CurrencyExchangePage'
import { ResourcesCatalogPage } from '../features/gameplay/resources-catalog/pages/ResourcesCatalogPage'
import { MentorshipPage } from '../features/gameplay/mentorship/pages/MentorshipPage'
import { FamilyRelationshipsPage } from '../features/gameplay/family-relationships/pages/FamilyRelationshipsPage'
import { EconomyAnalyticsPage } from '../features/gameplay/economy-analytics/pages/EconomyAnalyticsPage'
import { LogisticsPage } from '../features/gameplay/logistics/pages/LogisticsPage'
import { InvestmentsPage } from '../features/gameplay/investments/pages/InvestmentsPage'
import { TravelEventsPage } from '../features/gameplay/travel-events/pages/TravelEventsPage'
import { PlayerOrdersExtendedPage } from '../features/gameplay/player-orders-extended/pages/PlayerOrdersExtendedPage'
import { MentorshipExtendedPage } from '../features/gameplay/mentorship-extended/pages/MentorshipExtendedPage'
import { NPCHiringExtendedPage } from '../features/gameplay/npc-hiring-extended/pages/NPCHiringExtendedPage'
import { RandomEventsExtendedPage } from '../features/gameplay/random-events-extended/pages/RandomEventsExtendedPage'
import { WorldEventsFrameworkPage } from '../features/gameplay/world-events-framework/pages/WorldEventsFrameworkPage'
import { AuctionHouseCorePage } from '../features/gameplay/auction-house-core/pages/AuctionHouseCorePage'
import { AuctionHouseSearchPage } from '../features/gameplay/auction-house-search/pages/AuctionHouseSearchPage'
import { AuctionHouseOrdersPage } from '../features/gameplay/auction-house-orders/pages/AuctionHouseOrdersPage'
import { AuctionHouseHistoryPage } from '../features/gameplay/auction-house-history/pages/AuctionHouseHistoryPage'
import { PlayerMarketCorePage } from '../features/gameplay/player-market-core/pages/PlayerMarketCorePage'
import { PlayerMarketOrdersPage } from '../features/gameplay/player-market-orders/pages/PlayerMarketOrdersPage'
import { PlayerMarketExecutionPage } from '../features/gameplay/player-market-execution/pages/PlayerMarketExecutionPage'
import { PricingPage } from '../features/gameplay/pricing/pages/PricingPage'
import { ProductionChainsPage } from '../features/gameplay/production-chains/pages/ProductionChainsPage'
import { ContractsPage } from '../features/gameplay/contracts/pages/ContractsPage'
import { AntiCheatPage } from '../features/admin/anti-cheat/pages/AntiCheatPage'
import { ModerationPage } from '../features/admin/moderation/pages/ModerationPage'
import { EconomyEventsPage } from '../features/gameplay/economy-events/pages/EconomyEventsPage'
import { MVPContentPage } from '../features/meta/mvp-content/pages/MVPContentPage'
import { ProtectedRoute } from './components/ProtectedRoute'
import { UIDemo } from '../pages/UIDemo'
import { Box, Typography } from '@mui/material'


/**
 * Конфигурация роутинга приложения
 * 
 * Использует React Router для навигации между страницами
 * 
 * Future flags для совместимости с React Router v7
 */
export const router = createBrowserRouter(
  [
  {
    path: '/',
    element: <Navigate to="/characters" replace />,
  },
  {
    path: '/characters',
    element: <CharactersPage />,
  },
  {
    path: '/auth',
    element: <AuthenticationPage />,
  },
  {
    path: '/auth/authentication',
    element: <AuthenticationPage />,
  },
  {
    path: '/characters/create',
    element: <CharacterCreationPage />,
  },
  {
    path: '/game/welcome',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <WelcomePage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/initial-state',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <InitialStatePage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/play',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <GameplayPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/actions',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <ActionsPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/implants',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <ImplantsPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/cyberpsychosis',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <CyberpsychosisPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/quests',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <QuestsPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/inventory',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <InventoryPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/npcs',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <NPCsPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/personal-npc-scenarios',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <BlueprintsPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/personal-npc-scenarios/:blueprintId',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <BlueprintDetailPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/personal-npc-scenarios/:blueprintId/execute',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <ExecuteScenarioPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/locations',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <LocationsPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/trading',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <TradingPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/character',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <CharacterStatusPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/events',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <EventsPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/weapons',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <WeaponsPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/abilities',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <AbilitiesPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/abilities-catalog',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <AbilitiesCatalogPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/combat-roles',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <CombatRolesPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/enemies',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <AIEnemiesPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/combos',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <CombosPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/extraction',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <ExtractionPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/implants-catalog',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <ImplantsCatalogPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/shooting',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <ShootingPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/combat-system',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <CombatPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/combat',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <CombatPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/combat-session',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <CombatSessionPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/classes',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <ClassesProgressionPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/global-state',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <GlobalStatePage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/reputation',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <ReputationTiersPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/crafting',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <CraftingPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/currencies',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <CurrenciesPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/skills',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <SkillsPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/perks',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <PerksPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/league',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <LeagueSystemPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/world-state',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <WorldStatePage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/relationships',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <RelationshipsPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/romance',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <RomanceSystemPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/romance-system',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <RomanceSystemPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/progression-detailed',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <ProgressionDetailedPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/progression-backend',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <ProgressionBackendPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/narrative/coherence',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <NarrativeCoherencePage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/narrative/faction-quests',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <FactionQuestsPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/narrative/quest-catalog',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <QuestCatalogPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/narrative/starter-content',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <StarterContentPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/narrative/regional-quests',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <RegionalQuestsPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/technical/global-state-extended',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <GlobalStateExtendedPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/technical/ui-systems',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <UISystemsPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/technical/disaster-recovery',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <DisasterRecoveryPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/technical/configuration-management',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <ConfigurationManagementPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/technical/incident-response',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <IncidentResponsePage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/technical/voice-chat',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <VoiceChatPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/technical/realtime-server-zones',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <RealtimeServerZonesPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/technical/session-lifecycle',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <SessionLifecyclePage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/internal/ai-algorithms',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <AIAlgorithmsPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/lore/database',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <LoreDatabasePage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/cyberspace',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <CyberspacePage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/hacking',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <HackingPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/stealth',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <StealthPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/freerun',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <FreerunPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/faction-wars',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <FactionWarsPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/auction-house',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <AuctionHousePage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/stock-exchange',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <StockExchangePage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/dnd-checks',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <DnDChecksPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/npc-hiring',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <NPCHiringPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/global-events',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <GlobalEventsPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/player-market',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <PlayerMarketPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/loot-tables',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <LootTablesPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/hacking-networks',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <HackingNetworksPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/events-2020-2040',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <Events2020_2040Page />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/events-2040-2060',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <Events2040_2060Page />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/events-2060-2077',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <Events2060_2077Page />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/trading-guilds',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <TradingGuildsPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/trading-routes',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <TradingRoutesPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/currency-exchange',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <CurrencyExchangePage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/resources-catalog',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <ResourcesCatalogPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/mentorship',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <MentorshipPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/family-relationships',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <FamilyRelationshipsPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/economy-analytics',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <EconomyAnalyticsPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/production-chains',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <ProductionChainsPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/contracts',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <ContractsPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/economy-events',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <EconomyEventsPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/meta/mvp-content',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <MVPContentPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/admin/anti-cheat',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <AntiCheatPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/admin/moderation',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <ModerationPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/logistics',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <LogisticsPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/investments',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <InvestmentsPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/travel-events',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <TravelEventsPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/matchmaking',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <MatchmakingAlgorithmPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/player-orders-extended',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <PlayerOrdersExtendedPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/mentorship-extended',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <MentorshipExtendedPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/npc-hiring-extended',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <NPCHiringExtendedPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/random-events-extended',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <RandomEventsExtendedPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/world-events-framework',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <WorldEventsFrameworkPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/auction-house-core',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <AuctionHouseCorePage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/auction-house-search',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <AuctionHouseSearchPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/auction-house-orders',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <AuctionHouseOrdersPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/auction-house-history',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <AuctionHouseHistoryPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/player-market-core',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <PlayerMarketCorePage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/player-market-orders',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <PlayerMarketOrdersPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/player-market-execution',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <PlayerMarketExecutionPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/game/pricing',
    element: (
      <ProtectedRoute requireCharacter={true}>
        <PricingPage />
      </ProtectedRoute>
    ),
  },
  {
    path: '/ui-kit',
    element: (
      <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh', overflow: 'hidden' }}>
        <Header />
        <GameLayout>
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3, flex: 1 }}>
            <UIDemo />
          </Box>
        </GameLayout>
      </Box>
    ),
  },
  {
    path: '*',
    element: (
      <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', height: '100vh' }}>
        <Box sx={{ textAlign: 'center' }}>
          <Typography variant="h1" sx={{ fontSize: '4rem', mb: 2, color: 'primary.main' }}>
            404
          </Typography>
          <Typography variant="h6" sx={{ fontSize: '1.5rem', mb: 2, color: 'text.secondary' }}>
            Страница не найдена
          </Typography>
        </Box>
      </Box>
    ),
  },
  ],
  {
    future: {
      v7_startTransition: true,
    },
  }
)

