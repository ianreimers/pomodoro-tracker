import '@testing-library/jest-dom';
import { jest } from '@jest/globals';

// Placing this in the global context since the ProviderWrapper
// contains the AuthContext which utilzies the hook
jest.mock('next/navigation', () => ({
  useRouter() {
    return {
      push: jest.fn(),
      prefetch: jest.fn(),
      route: '/',
      pathname: '',
      query: '',
      asPath: '',
    };
  },
}));

// Needed for the user-settings-form
(global as any).ResizeObserver = jest.fn().mockImplementation(() => ({
  observe: jest.fn(),
  unobserve: jest.fn(),
  disconnect: jest.fn(),
}));
