export const PersonalNpcModuleConfig = {
  name: 'social-personal-npc',
  version: '0.1.0',
  routes: [
    {
      path: '/game/personal-npc-scenarios',
      component: () => import('./pages/BlueprintsPage').then((module) => module.BlueprintsPage),
    },
    {
      path: '/game/personal-npc-scenarios/:blueprintId',
      component: () =>
        import('./pages/BlueprintDetailPage').then((module) => module.BlueprintDetailPage),
    },
    {
      path: '/game/personal-npc-scenarios/:blueprintId/execute',
      component: () =>
        import('./pages/ExecuteScenarioPage').then((module) => module.ExecuteScenarioPage),
    },
  ],
}

