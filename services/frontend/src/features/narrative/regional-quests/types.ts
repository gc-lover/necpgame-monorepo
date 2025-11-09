export interface DailyQuest {
  questId: string
  name: string
  region: string
  difficulty: 'EASY' | 'NORMAL' | 'HARD'
  objective: string
  reward: string
  resetsAt: string
}

export interface WeeklyQuest {
  questId: string
  name: string
  region: string
  recommendedPower: number
  description: string
  reward: string
}

export interface RegionalQuest {
  questId: string
  name: string
  region: string
  minLevel: number
  summary: string
  faction: string
  repeatable: boolean
}

export interface WorldQuest {
  questId: string
  name: string
  faction: string
  description: string
  regionImpact: string
}

export interface QuestAvailability {
  dailySlotsAvailable: number
  dailySlotsUsed: number
  weeklySlotsAvailable: number
  weeklySlotsUsed: number
  resetsAt: string
}


