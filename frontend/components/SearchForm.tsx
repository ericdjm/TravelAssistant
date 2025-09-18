'use client';

import React, { useState } from 'react';
import styled from 'styled-components';
import { TravelQuery } from '@/types';

const FormContainer = styled.div`
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  margin-bottom: 2rem;
`;

const FormGroup = styled.div`
  margin-bottom: 1rem;
`;

const Label = styled.label`
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 600;
  color: #333;
`;

const Input = styled.input`
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
  
  &:focus {
    outline: none;
    border-color: #007bff;
    box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.25);
  }
`;

const Button = styled.button`
  background: #007bff;
  color: white;
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  transition: background 0.2s;
  
  &:hover {
    background: #0056b3;
  }
  
  &:disabled {
    background: #ccc;
    cursor: not-allowed;
  }
`;

interface SearchFormProps {
  onSearch: (query: TravelQuery) => void;
  loading?: boolean;
}

export default function SearchForm({ onSearch, loading = false }: SearchFormProps) {
  const [query, setQuery] = useState<TravelQuery>({
    destination: '',
    start_date: '',
    end_date: '',
    budget: undefined,
    preferences: [],
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (query.destination.trim()) {
      onSearch(query);
    }
  };

  const handleInputChange = (field: keyof TravelQuery, value: string | number) => {
    setQuery(prev => ({
      ...prev,
      [field]: value,
    }));
  };

  return (
    <FormContainer>
      <h2>Plan Your Next Adventure</h2>
      <form onSubmit={handleSubmit}>
        <FormGroup>
          <Label htmlFor="destination">Destination</Label>
          <Input
            id="destination"
            type="text"
            value={query.destination}
            onChange={(e) => handleInputChange('destination', e.target.value)}
            placeholder="Where would you like to go?"
            required
          />
        </FormGroup>

        <FormGroup>
          <Label htmlFor="budget">Budget (USD)</Label>
          <Input
            id="budget"
            type="number"
            value={query.budget || ''}
            onChange={(e) => handleInputChange('budget', parseFloat(e.target.value) || 0)}
            placeholder="Enter your budget"
            min="0"
          />
        </FormGroup>

        <FormGroup>
          <Label htmlFor="start_date">Start Date</Label>
          <Input
            id="start_date"
            type="date"
            value={query.start_date}
            onChange={(e) => handleInputChange('start_date', e.target.value)}
          />
        </FormGroup>

        <FormGroup>
          <Label htmlFor="end_date">End Date</Label>
          <Input
            id="end_date"
            type="date"
            value={query.end_date}
            onChange={(e) => handleInputChange('end_date', e.target.value)}
          />
        </FormGroup>

        <Button type="submit" disabled={loading || !query.destination.trim()}>
          {loading ? 'Searching...' : 'Search Destinations'}
        </Button>
      </form>
    </FormContainer>
  );
}