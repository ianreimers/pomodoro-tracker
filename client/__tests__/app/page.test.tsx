import React from 'react';
import { render, screen } from '@testing-library/react';
import Home from '@/app/page';
import ProviderWrapper from '@/__tests__/test-utils/ProviderWrapper';

describe('Home Page Test', () => {
  it('should render session and settings tab', () => {
    render(<Home />, { wrapper: ProviderWrapper });

    const tabs = screen.getAllByRole('tab');
    expect(tabs.length).toBe(2);

    expect(tabs[0]).toHaveTextContent(/session/i);
    expect(tabs[1]).toHaveTextContent(/settings/i);
  });
});
