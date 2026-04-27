/**
 * @license
 * SPDX-License-Identifier: Apache-2.0
 */

import React from 'react';
import Markdown from 'react-markdown';
import { X } from 'lucide-react';

const GUIDE_CONTENT = `
# Lumina Motion User Guide

Welcome to the future of mobile motion design.

## Getting Started
1. **Add Layers**: Use the (+) button to add Text, Shapes, or Media.
2. **Timeline**: Drag the playhead to scrub through time.
3. **Keyframes**: Tap the diamond icon next to any property to add a keyframe.
4. **Parenting**: Link layers to a **Null Object** to control multiple layers at once.

## Pro Tips
- **Camera Layer**: Add a camera to pan, zoom, and rotate around your 3D workspace.
- **Dynamic Color**: Your UI adapts to your project's primary colors (MD3).
- **Expressive Motion**: Change easing curves in the Inspector for organic movement.

## Support
Need help? Contact us at support@luminamotion.app or visit our community forum.
`;

export function Documentation({ onClose }: { onClose: () => void }) {
  return (
    <div className="fixed inset-0 z-[100] flex items-center justify-center bg-black/60 backdrop-blur-md p-4 lg:p-12">
      <div className="relative w-full max-w-4xl h-full max-h-[80vh] bg-[#111111] border border-white/10 rounded-3xl flex flex-col shadow-2xl overflow-hidden">
        <div className="flex items-center justify-between p-6 border-b border-white/10">
          <h2 className="text-xl font-bold tracking-tight text-white">Documentation & Guide</h2>
          <button onClick={onClose} className="h-10 w-10 rounded-full bg-white/5 flex items-center justify-center hover:bg-white/10 transition-colors">
            <X className="h-6 w-6" />
          </button>
        </div>
        <div className="flex-1 overflow-y-auto p-8 lg:p-12 prose prose-invert prose-indigo max-w-none">
          <div className="markdown-body">
            <Markdown>{GUIDE_CONTENT}</Markdown>
          </div>
        </div>
      </div>
    </div>
  );
}
