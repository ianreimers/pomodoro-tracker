import SessionTimer from '@/components/session-timer';
import { render, screen, fireEvent, act } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import ProviderWrapper from '@/__tests__/test-utils/ProviderWrapper';

describe('Pomodoro Session Timer', () => {
  beforeEach(() => {
    jest.useFakeTimers();
    window.HTMLMediaElement.prototype.play = jest.fn();
    window.HTMLMediaElement.prototype.pause = jest.fn();
    window.HTMLMediaElement.prototype.load = jest.fn();
    //window.HTMLMediaElement.prototype.currentTime = 0;
  });

  afterEach(() => {
    //jest.runOnlyPendingTimers();
    jest.clearAllTimers();
    jest.useRealTimers();
  });

  it('initializes correctly and displays time in hh:mm:ss format', () => {
    render(<SessionTimer />, { wrapper: ProviderWrapper });

    const hours = screen.getByLabelText('hours');
    const minutes = screen.getByLabelText('minutes');
    const seconds = screen.getByLabelText('seconds');

    // Check for exact format and ensure the text contains only digits
    expect(hours.textContent).toMatch(/\d{2}/);
    expect(minutes.textContent).toMatch(/\d{2}/);
    expect(seconds.textContent).toMatch(/\d{2}/);

    // Check the inital state
    expect(hours.textContent).toBe('00');
    expect(minutes.textContent).toBe('25');
    expect(seconds.textContent).toBe('00');
  });

  it('should initially display "Task" as the session type', () => {
    render(<SessionTimer />, { wrapper: ProviderWrapper });

    const sessionType = screen.getByRole('heading');

    expect(sessionType).toHaveTextContent(/task/i);
  });

  it('should initially render play button', () => {
    render(<SessionTimer />, { wrapper: ProviderWrapper });

    const playButton = screen.getByLabelText('Start Pomodoro Timer');
    expect(playButton).toBeInTheDocument();
  });

  it('should toggle the timer button on and off', async () => {
    const user = userEvent.setup({ advanceTimers: jest.advanceTimersByTime });
    render(<SessionTimer />, { wrapper: ProviderWrapper });

    const startToggleButton = screen.getByLabelText(/start pomodoro timer/i);
    await act(() => user.click(startToggleButton));
    expect(startToggleButton).toHaveAccessibleName(/stop pomodoro timer/i);
    await act(() => user.click(startToggleButton));
    expect(startToggleButton).toHaveAccessibleName(/start pomodoro timer/i);
  });

  it('should reflect time change when the start button is clicked', async () => {
    const user = userEvent.setup({ advanceTimers: jest.advanceTimersByTime });
    render(<SessionTimer />, { wrapper: ProviderWrapper });

    const startToggleButton = screen.getByLabelText(/start pomodoro timer/i);
    const hours = screen.getByLabelText('hours');
    const minutes = screen.getByLabelText('minutes');
    const seconds = screen.getByLabelText('seconds');

    expect(
      `${hours.textContent}:${minutes.textContent}:${seconds.textContent}`
    ).toBe('00:25:00');

    // Start the timer
    await act(() => user.click(startToggleButton));
    act(() => jest.advanceTimersByTime(10000));

    expect(
      `${hours.textContent}:${minutes.textContent}:${seconds.textContent}`
    ).toBe('00:24:50');

    // Pause the timer
    await act(() => user.click(startToggleButton));
    act(() => jest.advanceTimersByTime(30000)); // Advance by another 30 seconds
    expect(
      `${hours.textContent}:${minutes.textContent}:${seconds.textContent}`
    ).toBe('00:24:50');
  });

  it('should transistion to short break session when task time is finished', async () => {
    const user = userEvent.setup({ advanceTimers: jest.advanceTimersByTime });

    render(<SessionTimer />, { wrapper: ProviderWrapper });

    const startToggleButton = screen.getByLabelText(/start pomodoro timer/i);

    await user.click(startToggleButton);

    act(() => jest.advanceTimersByTime(60000 * 25));
    expect(screen.getByLabelText('minutes').textContent).toBe('00');
    expect(screen.getByLabelText('seconds').textContent).toBe('00');
    act(() => jest.advanceTimersByTime(1000));
    expect(screen.getByRole('heading')).toHaveTextContent(/short break/i);
    expect(screen.getByLabelText('minutes').textContent).toBe('10');
    act(() => jest.advanceTimersByTime(60000 * 5));
    expect(screen.getByLabelText('minutes').textContent).toBe('05');
    act(() => jest.advanceTimersByTime(60000 * 5));
    expect(screen.getByLabelText('minutes').textContent).toBe('00');

    //expect(screen.getByText(/short break/i)).toBeInTheDocument();
  });

  it('should switch to long break session after 4 completed task session', async () => {
    const user = userEvent.setup({ advanceTimers: jest.advanceTimersByTime });

    render(<SessionTimer />, { wrapper: ProviderWrapper });
    const startToggleButton = screen.getByLabelText(/start pomodoro timer/i);

    // Start the timer
    await act(() => user.click(startToggleButton));

    // Simulate the completions of full pomodoros + 1 task to get to the long break
    for (let i = 0; i < 7; ++i) {
      // If its even, then advance by full task duration (25 mins)
      if (i % 2 == 0) {
        act(() => jest.advanceTimersByTime(60000 * 25));
        act(() => jest.advanceTimersByTime(1000));
        continue;
      }

      // It is odd, advance by short break duration (10 mins)
      act(() => jest.advanceTimersByTime(60000 * 10));
      act(() => jest.advanceTimersByTime(1000));
    }

    act(() => jest.advanceTimersByTime(1000));
    expect(screen.getByRole('heading')).toHaveTextContent(/long break/i);
  });
});
