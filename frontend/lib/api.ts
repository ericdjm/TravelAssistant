import axios from 'axios';
import { TravelQuery, TravelRecommendation, SearchResponse } from '@/types';

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8000';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const travelApi = {
  // Health check
  healthCheck: async () => {
    const response = await api.get('/health');
    return response.data;
  },

  // Get all recommendations
  getRecommendations: async (): Promise<TravelRecommendation[]> => {
    const response = await api.get('/recommendations');
    return response.data;
  },

  // Search destinations
  searchDestinations: async (query: TravelQuery): Promise<SearchResponse> => {
    const response = await api.post('/search', query);
    return response.data;
  },

  // Get recommendation by destination
  getRecommendationByDestination: async (destination: string): Promise<TravelRecommendation> => {
    const response = await api.get(`/recommendations/${encodeURIComponent(destination)}`);
    return response.data;
  },
};

export default api;