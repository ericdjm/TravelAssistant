export interface TravelQuery {
  destination: string;
  start_date?: string;
  end_date?: string;
  budget?: number;
  preferences?: string[];
}

export interface TravelRecommendation {
  id: number;
  destination: string;
  description: string;
  estimated_cost: number;
  recommended_duration: string;
  highlights: string[];
}

export interface SearchResponse {
  results?: TravelRecommendation[];
  suggestions?: TravelRecommendation[];
  count?: number;
  message?: string;
}