import UserSettingsForm from '@/components/user-settings-form';
import { render, screen, within } from '@testing-library/react';
import React from 'react';
import ProviderWrapper from '../test-utils/ProviderWrapper';
import userEvent from '@testing-library/user-event';

describe('User Settings Form', () => {
  function getInputValuesFromFieldset(
    fieldsetName: string,
    inputNames: string[]
  ) {
    const fieldset = screen.getByRole('group', { name: fieldsetName });
    return inputNames.map((name) =>
      within(fieldset).getByRole('spinbutton', { name })
    );
  }

  it('should initilize fields to defaults', () => {
    render(<UserSettingsForm />, { wrapper: ProviderWrapper });

    const [taskHoursInput, taskMinutesInput] = getInputValuesFromFieldset(
      'Task Time',
      ['Hours', 'Minutes']
    );
    const [shortBreakHoursInput, shortBreakMinutesInput] =
      getInputValuesFromFieldset('Short Break Time', ['Hours', 'Minutes']);
    const [longBreakHoursInput, longBreakMinutesInput] =
      getInputValuesFromFieldset('Long Break Time', ['Hours', 'Minutes']);

    const pomodoroIntervalInput = screen.getByRole('spinbutton', {
      name: 'Pomodoro Interval',
    });
    const soundSelect = screen.getByRole('combobox', { name: 'Sound' });
    const soundSlider = screen.getByRole('slider');

    expect(taskHoursInput).toHaveValue(0);
    expect(taskMinutesInput).toHaveValue(25);
    expect(shortBreakHoursInput).toHaveValue(0);
    expect(shortBreakMinutesInput).toHaveValue(10);
    expect(longBreakHoursInput).toHaveValue(0);
    expect(longBreakMinutesInput).toHaveValue(20);
    expect(pomodoroIntervalInput).toHaveValue(4);
    expect(soundSelect).toHaveTextContent('Bells');
    expect(soundSlider).toBeInTheDocument();
    // Ensure no form error messages being displayed
    expect(screen.queryByRole('paragraph')).not.toBeInTheDocument();
  });

  it('should display an error message when both hours and minutes are 0', async () => {
    const user = userEvent.setup();
    render(<UserSettingsForm />, { wrapper: ProviderWrapper });

    const [taskHoursInput, taskMinutesInput] = getInputValuesFromFieldset(
      'Task Time',
      ['Hours', 'Minutes']
    );
    const [shortBreakHoursInput, shortBreakMinutesInput] =
      getInputValuesFromFieldset('Short Break Time', ['Hours', 'Minutes']);
    const [longBreakHoursInput, longBreakMinutesInput] =
      getInputValuesFromFieldset('Long Break Time', ['Hours', 'Minutes']);

    const pomodoroIntervalInput = screen.getByRole('spinbutton', {
      name: 'Pomodoro Interval',
    });
    const submitBtn = screen.getByRole('button', { name: 'Submit' });

    // Input 0 into the hour and minute fields
    await user.clear(taskHoursInput);
    await user.clear(taskMinutesInput);
    await user.type(taskHoursInput, '0');
    await user.type(taskMinutesInput, '0');
    await user.click(submitBtn);
    expect(screen.getByRole('paragraph').textContent).toMatch(
      /task.*greater than 0/i
    );
    // Add a accepted value back into the input to remove error
    await user.clear(taskHoursInput);
    await user.type(taskHoursInput, '1');

    // Input 0 into the hour and minute fields
    await user.clear(shortBreakHoursInput);
    await user.clear(shortBreakMinutesInput);
    await user.type(shortBreakHoursInput, '0');
    await user.type(shortBreakMinutesInput, '0');
    await user.click(submitBtn);
    expect(screen.getByRole('paragraph').textContent).toMatch(
      /short.*greater than 0/i
    );
    // Add a accepted value back into the input to remove error
    await user.clear(shortBreakHoursInput);
    await user.type(shortBreakHoursInput, '1');

    // Input 0 into the hour and minute fields
    await user.clear(longBreakHoursInput);
    await user.clear(longBreakMinutesInput);
    await user.type(longBreakHoursInput, '0');
    await user.type(longBreakMinutesInput, '0');
    await user.click(submitBtn);
    expect(screen.getByRole('paragraph').textContent).toMatch(
      /long.*greater than 0/i
    );
    // Add a accepted value back into the input to remove error
    await user.clear(longBreakHoursInput);
    await user.type(longBreakHoursInput, '1');
  });
});
