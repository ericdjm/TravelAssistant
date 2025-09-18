'use client';

import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import SearchForm from '@/components/SearchForm';
import RecommendationCard from '@/components/RecommendationCard';
import { travelApi } from '@/lib/api';
import { TravelQuery, TravelRecommendation, SearchResponse } from '@/types';

const Container = styled.div`
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
  min-height: 100vh;
`;

const Header = styled.header`
  text-align: center;
  margin-bottom: 3rem;
`;

const Title = styled.h1`
  color: white;
  font-size: 3rem;
  margin-bottom: 1rem;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
`;

const Subtitle = styled.p`
  color: rgba(255, 255, 255, 0.9);
  font-size: 1.25rem;
  max-width: 600px;
  margin: 0 auto;
`;

const ResultsContainer = styled.div`
  margin-top: 2rem;
`;

const ResultsTitle = styled.h2`
  color: white;
  margin-bottom: 1rem;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.3);
`;

const Message = styled.div`
  background: #fff3cd;
  color: #856404;
  padding: 1rem;
  border-radius: 4px;
  margin-bottom: 1rem;
  border: 1px solid #ffeaa7;
`;

const ErrorMessage = styled.div`
  background: #f8d7da;
  color: #721c24;
  padding: 1rem;
  border-radius: 4px;
  margin-bottom: 1rem;
  border: 1px solid #f5c6cb;
`;

const LoadingSpinner = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 2rem;
  color: white;
  font-size: 1.1rem;
`;

export default function HomePage() {
  const [recommendations, setRecommendations] = useState<TravelRecommendation[]>([]);
  const [searchResults, setSearchResults] = useState<SearchResponse | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Load initial recommendations
  useEffect(() => {
    const loadRecommendations = async () => {
      try {
        const data = await travelApi.getRecommendations();
        setRecommendations(data);
      } catch (err) {
        console.error('Failed to load recommendations:', err);
        setError('Failed to load recommendations. Please check if the backend is running.');
      }
    };

    loadRecommendations();
  }, []);

  const handleSearch = async (query: TravelQuery) => {
    setLoading(true);
    setError(null);
    setSearchResults(null);

    try {
      const results = await travelApi.searchDestinations(query);
      setSearchResults(results);
    } catch (err) {
      console.error('Search failed:', err);
      setError('Search failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const displayRecommendations = searchResults?.results || recommendations;
  const showSuggestions = searchResults?.suggestions && !searchResults.results;

  return (
    <Container>
      <Header>
        <Title>Travel Assistant</Title>
        <Subtitle>
          Discover amazing destinations with AI-powered recommendations
          tailored to your preferences and budget.
        </Subtitle>
      </Header>

      <SearchForm onSearch={handleSearch} loading={loading} />

      {error && <ErrorMessage>{error}</ErrorMessage>}

      {loading && (
        <LoadingSpinner>
          🌍 Searching for the perfect destinations...
        </LoadingSpinner>
      )}

      {searchResults?.message && (
        <Message>{searchResults.message}</Message>
      )}

      <ResultsContainer>
        {displayRecommendations.length > 0 && (
          <>
            <ResultsTitle>
              {searchResults?.results 
                ? `Found ${searchResults.count} destination${searchResults.count !== 1 ? 's' : ''}`
                : 'Popular Destinations'
              }
            </ResultsTitle>
            {displayRecommendations.map((recommendation) => (
              <RecommendationCard
                key={recommendation.id}
                recommendation={recommendation}
              />
            ))}
          </>
        )}

        {showSuggestions && (
          <>
            <ResultsTitle>You might also like these destinations:</ResultsTitle>
            {searchResults.suggestions.map((suggestion) => (
              <RecommendationCard
                key={suggestion.id}
                recommendation={suggestion}
              />
            ))}
          </>
        )}
      </ResultsContainer>
    </Container>
  );
}