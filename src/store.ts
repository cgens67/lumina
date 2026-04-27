/**
 * @license
 * SPDX-License-Identifier: Apache-2.0
 */

import { useState, useCallback, useMemo } from 'react';
import { AppState, INITIAL_STATE, Layer } from './types';

export function useAppStore() {
  const [state, setState] = useState<AppState>(INITIAL_STATE);

  const setLanguage = useCallback((lang: AppState['language']) => {
    setState((prev) => ({ ...prev, language: lang }));
  }, []);

  const setTheme = useCallback((theme: AppState['theme']) => {
    setState((prev) => ({ ...prev, theme }));
  }, []);

  const closeOnboarding = useCallback(() => {
    setState((prev) => ({ ...prev, showOnboarding: false }));
  }, []);

  const selectLayer = useCallback((id: string | null) => {
    setState((prev) => ({ ...prev, selectedLayerId: id }));
  }, []);

  const updateCurrentTime = useCallback((time: number) => {
    setState((prev) => ({ ...prev, currentTime: Math.max(0, Math.min(time, prev.project.duration)) }));
  }, []);

  const togglePlayback = useCallback(() => {
    setState((prev) => ({ ...prev, isPlaying: !prev.isPlaying }));
  }, []);

  const addLayer = useCallback((layer: Omit<Layer, 'id'>) => {
    const newLayer = { ...layer, id: `layer-${Date.now()}` };
    setState((prev) => ({ ...prev, layers: [...prev.layers, newLayer] }));
  }, []);

  return {
    state,
    setLanguage,
    setTheme,
    closeOnboarding,
    selectLayer,
    updateCurrentTime,
    togglePlayback,
    addLayer,
  };
}
