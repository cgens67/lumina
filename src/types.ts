/**
 * @license
 * SPDX-License-Identifier: Apache-2.0
 */

export type Project = {
  id: string;
  name: string;
  width: number;
  height: number;
  fps: number;
  duration: number; // in seconds
};

export type LayerType = 'video' | 'image' | 'text' | 'shape' | 'audio' | 'null' | 'camera';

export type Keyframe = {
  time: number; // in seconds
  value: any;
};

export type Property = {
  name: string;
  value: any;
  keyframes?: Keyframe[];
  isAnimating: boolean;
};

export type Layer = {
  id: string;
  name: string;
  type: LayerType;
  startTime: number;
  duration: number;
  isVisible: boolean;
  isLocked: boolean;
  properties: {
    position: Property;
    scale: Property;
    rotation: Property;
    opacity: Property;
    [key: string]: Property;
  };
};

export type AppState = {
  project: Project;
  layers: Layer[];
  selectedLayerId: string | null;
  currentTime: number;
  isPlaying: boolean;
  language: 'en' | 'es' | 'zh' | 'fr';
  theme: 'dark' | 'light' | 'amoled';
  showOnboarding: boolean;
};

export const INITIAL_STATE: AppState = {
  project: {
    id: '1',
    name: 'Untitled Project',
    width: 1080,
    height: 1920,
    fps: 30,
    duration: 10,
  },
  layers: [
    {
      id: 'layer-1',
      name: 'Background',
      type: 'shape',
      startTime: 0,
      duration: 10,
      isVisible: true,
      isLocked: false,
      properties: {
        position: { name: 'Position', value: { x: 540, y: 960 }, isAnimating: false },
        scale: { name: 'Scale', value: 100, isAnimating: false },
        rotation: { name: 'Rotation', value: 0, isAnimating: false },
        opacity: { name: 'Opacity', value: 100, isAnimating: false },
      },
    },
  ],
  selectedLayerId: null,
  currentTime: 0,
  isPlaying: false,
  language: 'en',
  theme: 'dark',
  showOnboarding: true,
};
