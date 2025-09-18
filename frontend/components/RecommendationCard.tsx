'use client';

import React from 'react';
import styled from 'styled-components';
import { TravelRecommendation } from '@/types';

const Card = styled.div`
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  padding: 1.5rem;
  margin-bottom: 1rem;
  transition: transform 0.2s, box-shadow 0.2s;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  }
`;

const Title = styled.h3`
  color: #333;
  margin-bottom: 0.5rem;
  font-size: 1.25rem;
`;

const Description = styled.p`
  color: #666;
  line-height: 1.6;
  margin-bottom: 1rem;
`;

const InfoGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 1rem;
  margin-bottom: 1rem;
`;

const InfoItem = styled.div`
  text-align: center;
`;

const InfoLabel = styled.div`
  font-size: 0.875rem;
  color: #888;
  margin-bottom: 0.25rem;
`;

const InfoValue = styled.div`
  font-weight: 600;
  color: #333;
`;

const HighlightsList = styled.ul`
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
`;

const HighlightItem = styled.li`
  background: #e3f2fd;
  color: #1976d2;
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.875rem;
`;

interface RecommendationCardProps {
  recommendation: TravelRecommendation;
}

export default function RecommendationCard({ recommendation }: RecommendationCardProps) {
  return (
    <Card>
      <Title>{recommendation.destination}</Title>
      <Description>{recommendation.description}</Description>
      
      <InfoGrid>
        <InfoItem>
          <InfoLabel>Estimated Cost</InfoLabel>
          <InfoValue>${recommendation.estimated_cost.toLocaleString()}</InfoValue>
        </InfoItem>
        <InfoItem>
          <InfoLabel>Duration</InfoLabel>
          <InfoValue>{recommendation.recommended_duration}</InfoValue>
        </InfoItem>
      </InfoGrid>

      <InfoLabel>Highlights:</InfoLabel>
      <HighlightsList>
        {recommendation.highlights.map((highlight, index) => (
          <HighlightItem key={index}>{highlight}</HighlightItem>
        ))}
      </HighlightsList>
    </Card>
  );
}