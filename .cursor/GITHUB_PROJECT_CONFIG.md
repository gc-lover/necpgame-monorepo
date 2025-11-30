# GitHub Project Configuration

**Единый источник параметров проекта для всех агентов**

## Project Parameters

Все агенты используют эти параметры для работы с GitHub Project через MCP:

- **Owner Type:** `user`
- **Owner:** `gc-lover`
- **Project Number:** `1`
- **Project Node ID:** `PVT_kwHODCWAw84BIyie`
- **Status Field ID:** `239690516`
- **Repository:** `gc-lover/necpgame-monorepo`

## Usage in Commands

В командах агентов использовать эти значения:

```javascript
mcp_github_list_project_items({
  owner_type: 'user',        // из этого конфига
  owner: 'gc-lover',         // из этого конфига
  project_number: 1,         // из этого конфига
  query: 'Status:"{Agent} - Todo" OR Status:"{Agent} - In Progress"'
});
```
**Note:** Не используй `is:issue` в query - `list_project_items` работает только с issues. Не указывай `fields` - вернутся все поля.

**Важно:** Если параметры проекта изменятся, обновить их здесь и во всех командах агентов.

## Field IDs

- **Status Field ID:** `239690516` (использовать как число в update_project_item)
- **Status Field Node ID:** `PVTSSF_lAHODCWAw84BIyiezg5JYxQ`

## Status Option IDs

**Полный список всех статусов с их ID**

**См. также:** `.cursor/STATUS_HANDOFF_GUIDE.md` - подробное руководство по работе со статусами

### Константы для использования:

```javascript
// Поле Status
const STATUS_FIELD_ID = 239690516;

// Универсальные статусы
const STATUS_OPTIONS = {
  // Универсальные
  'Todo': 'f75ad846',
  'Done': '98236657',
  
  // Idea Writer
  'Idea Writer - Todo': '17634e7f',
  'Idea Writer - In Progress': 'd9960d37',
  'Idea Writer - Blocked': '482a66ed',
  'Idea Writer - Review': '578a1575',
  'Idea Writer - Returned': 'ec26fd29',
  
  // Architect
  'Architect - Todo': '799d8a69',
  'Architect - In Progress': '02b1119e',
  'Architect - Blocked': 'fd94d2d1',
  'Architect - Review': '2c2a7b69',
  'Architect - Returned': '96c824c5',
  
  // API Designer
  'API Designer - Todo': '3eddfee3',
  'API Designer - In Progress': 'ff20e8f2',
  'API Designer - Blocked': '1394d650',
  'API Designer - Review': '193b473f',
  'API Designer - Returned': 'd0352ed3',
  
  // Database
  'Database - Todo': '58644d24',
  'Database - In Progress': '91d49623',
  'Database - Blocked': '7e03e676',
  'Database - Review': '7387ce92',
  'Database - Returned': '4272fcd7',
  
  // Backend
  'Backend - Todo': '72d37d44',
  'Backend - In Progress': '7bc9d20f',
  'Backend - Blocked': '504999e1',
  'Backend - Review': '8b8c3ffb',
  'Backend - Returned': '40f37190',
  
  // Network
  'Network - Todo': '944246f3',
  'Network - In Progress': '88b75a08',
  'Network - Blocked': '76383925',
  'Network - Review': '8492a63e',
  'Network - Returned': '1daf88e8',
  
  // Security
  'Security - Todo': '3212ee50',
  'Security - In Progress': '187ede76',
  'Security - Blocked': '3c7abfe7',
  'Security - Review': 'a32255fb',
  'Security - Returned': 'cb38d85c',
  
  // DevOps
  'DevOps - Todo': 'ea62d00f',
  'DevOps - In Progress': 'f5a718a4',
  'DevOps - Blocked': '99bf9af4',
  'DevOps - Review': '01d658cb',
  'DevOps - Returned': '96b3e4b0',
  
  // Performance
  'Performance - Todo': 'cdcab9ea',
  'Performance - In Progress': '1674ad2c',
  'Performance - Blocked': '83b14e01',
  'Performance - Review': 'dcee0fa9',
  'Performance - Returned': '00ac59f9',
  
  // UE5
  'UE5 - Todo': 'fa5905fb',
  'UE5 - In Progress': '9396f45a',
  'UE5 - Blocked': '5a8471c6',
  'UE5 - Review': 'e12e027b',
  'UE5 - Returned': '855f4872',
  
  // UI/UX
  'UI/UX - Todo': '49689997',
  'UI/UX - In Progress': 'dae97d56',
  'UI/UX - Blocked': 'c8feb6dd',
  'UI/UX - Review': '77405901',
  'UI/UX - Returned': '278add0a',
  
  // Content Writer
  'Content Writer - Todo': 'c62b60d3',
  'Content Writer - In Progress': 'cf5cf6bb',
  'Content Writer - Blocked': '412fab7f',
  'Content Writer - Review': '1e4df448',
  'Content Writer - Returned': 'f4a7797e',
  
  // QA
  'QA - Todo': '86ca422e',
  'QA - In Progress': '251c89a6',
  'QA - Blocked': '13612214',
  'QA - Review': 'e7fc0d6e',
  'QA - Returned': '6ccc53b0',
  
  // Game Balance
  'Game Balance - Todo': 'd48c0835',
  'Game Balance - In Progress': 'a67748e9',
  'Game Balance - Blocked': 'ca2710b7',
  'Game Balance - Review': '85b1d983',
  'Game Balance - Returned': 'dd88fe5d',
  
  // Release
  'Release - Todo': 'ef037f05',
  'Release - In Progress': '67671b7e',
  'Release - Blocked': 'c9874e66',
  'Release - Review': 'fe2fc469'
};
```

**Использование:**
```javascript
updated_field: {
  id: STATUS_FIELD_ID,
  value: STATUS_OPTIONS['Architect - In Progress']
}
```

## Project Details

- **Project Name:** NECPGAME Development
- **Project Node ID:** `PVT_kwHODCWAw84BIyie`
- **Project Number:** 1
- **Owner:** gc-lover

