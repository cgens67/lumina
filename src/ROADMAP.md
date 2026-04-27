# Lumina Motion - Project Roadmap

## Phase 1: MVP (Completed)
- [x] Expressive MD3 Design System (Jetpack Compose)
- [x] Core Timeline Architecture (Portrait optimized)
- [x] Basic Transform Inspector
- [x] Android CI/CD Pipeline (GitHub Actions)
- [x] Essential Resources (Adaptive Icons, Vector Drawables)

## Phase 2: Refinement (In Progress)
- [ ] Motion Engine: Bezier Curve Interpolation in Compose
- [ ] Layer Parenting: Coordinate space transforms for nested layers
- [ ] Effects: GLSL Shaders for Blur/Glow in Compose Canvas
- [ ] Keyframe System: Dedicated Keyframe Store for properties

## Phase 3: Advanced Features
- [ ] Audio: Waveform decoding via MediaCodec
- [ ] Vector: Pen Tool using Path component
- [ ] Playback: Hardware-accelerated frame buffer

## Phase 4: Platform & Export
- [ ] High-performance WebGL Render Engine
- [ ] MP4/WebM Export (via MediaRecorder API)
- [ ] Cloud-sync for Projects
- [ ] APK Build via GitHub Actions (CI/CD Pipeline set up)

---

## Technical Documentation

### Android Architecture
The app is built using **Jetpack Compose**, Google's modern toolkit for building native UI. It leverages Material 3 Expressive components for a production-grade editing interface.

### UI Pattern
- **TopBar**: Project metadata and export actions.
- **PreviewArea**: Custom Canvas-based viewport for motion rendering.
- **TimelineArea**: Multi-track layer management and keyframe visualization.
- **BottomNavBar**: Tool-switching (Layers, Effects, Assets).

### CI/CD Pipeline
GitHub Actions is configured to:
1. Set up JDK 17.
2. Initialize the Gradle Wrapper.
3. Build a debug APK via `./gradlew assembleDebug`.
4. Upload the APK as a build artifact.
