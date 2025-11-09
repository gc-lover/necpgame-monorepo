export interface OriginStory {
  originId: string
  name: string
  description: string
  recommendedClass: string
  startingLocation: string
}

export interface StarterQuest {
  questId: string
  name: string
  questType: string
  description: string
  rewards: string[]
}

export interface MainStoryQuest {
  questId: string
  name: string
  period: string
  chapter: number
  description: string
  objectives: string[]
}



