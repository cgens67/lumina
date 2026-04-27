# Lumina Motion - Project Roadmap

## Phase 1: MVP (Completed)
- [x] Expressive MD3 Design System
- [x] Core Timeline Architecture (Multi-layer)
- [x] Basic Transform Inspector (Position, Scale, Rotation)
- [x] Project State Management
- [x] Multi-language Support (EN, ES, ZH, FR)
- [x] Onboarding Flow

## Phase 2: Refinement (In Progress)
- [ ] Real-time Keyframe Interpolation (Linear, Ease-in, Ease-out, Bezier)
- [ ] Layer Parenting (Null Objects logic)
- [ ] Virtual Camera System (3D transformations)
- [ ] Effect Pipeline (Blur, Glow, Color Correction)

## Phase 3: Advanced Features
- [ ] Audio Waveform Visualization
- [ ] Vector Shape Pen Tool
- [ ] Masking & Luma Matte Support
- [ ] Preset Expression System (Javascript-based animation)

## Phase 4: Platform & Export
- [ ] High-performance WebGL Render Engine
- [ ] MP4/WebM Export (via MediaRecorder API)
- [ ] Cloud-sync for Projects
- [ ] APK Build via GitHub Actions (CI/CD Pipeline set up)

---

## Technical Documentation

### State Management
The app uses a centralized state pattern with a custom React hook `useAppStore`. This ensures consistent state across the preview, timeline, and inspector.

### Rendering Engine
Currently emulated via CSS/Motion. Production version will leverage a custom `<canvas>` layer with WebGL for Alight Motion-grade performance and blending modes.

### Keyframing
Keyframes are stored as an array of `{ time, value }` within each property. The system calculates the current value by interpolating between the two nearest keyframes relative to `currentTime`.
