// Временный скрипт для обработки всех Issues
// Все Issues уже собраны через пагинацию

function processStats(issues) {
  const stats = {};
  const agents = [
    'idea-writer', 'architect', 'api-designer', 'database',
    'backend', 'network', 'devops', 'performance', 'ue5',
    'content-writer', 'qa', 'release', 'security', 'game-balance', 'stats'
  ];
  
  // Инициализируем статистику для каждого агента
  agents.forEach(agent => {
    stats[agent] = {
      total: 0,
      open: 0,
      closed: 0,
      inProgress: 0,
      returned: 0
    };
  });
  
  // Обрабатываем Issues
  issues.forEach(issue => {
    const agentLabels = issue.labels
      .map(l => l.name)
      .filter(name => name.startsWith('agent:'));
    
    agentLabels.forEach(label => {
      const agent = label.replace('agent:', '');
      if (stats[agent]) {
        stats[agent].total++;
        
        if (issue.state === 'OPEN') {
          stats[agent].open++;
          
          // Проверяем метки для детализации
          if (issue.labels.some(l => l.name === 'returned')) {
            stats[agent].returned++;
          } else {
            stats[agent].inProgress++;
          }
        } else {
          stats[agent].closed++;
        }
      }
    });
  });
  
  return stats;
}

// Экспортируем для использования
if (typeof module !== 'undefined' && module.exports) {
  module.exports = { processStats };
}



