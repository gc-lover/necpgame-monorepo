import { render, screen } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import { MentorExtendedCard } from '../MentorExtendedCard';

describe('MentorExtendedCard', () => {
  it('renders mentor info and abilities', () => {
    const onRequest = vi.fn();
    const mentor = {
      mentor_id: 'mnt_001',
      name: 'Morgan Blackhand',
      type: 'COMBAT' as const,
      specialization: 'Solo Combat Expert',
      mentor_rank: 'LEGENDARY' as const,
      legendary: true,
      skill_level: 20,
      reputation: 92,
      success_rate: 88,
      unique_abilities: ['Blade Mastery', 'Adrenal Overdrive'],
      student_slots: 3,
      current_students: 1,
      bond_strength: 64,
    };

    render(<MentorExtendedCard mentor={mentor} onRequest={onRequest} />);

    expect(screen.getByText('Morgan Blackhand')).toBeInTheDocument();
    expect(screen.getByText(/Blade Mastery/i)).toBeInTheDocument();
    expect(screen.getByText(/Запросить обучение/i)).toBeInTheDocument();
  });
});

