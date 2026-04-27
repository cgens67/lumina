/**
 * @license
 * SPDX-License-Identifier: Apache-2.0
 */

import React from 'react';
import { motion, AnimatePresence } from 'motion/react';
import { 
  Play, 
  Pause, 
  Plus, 
  Settings, 
  Share2, 
  Layers, 
  Maximize2, 
  ChevronLeft, 
  HelpCircle,
  Type,
  Image as ImageIcon,
  Square,
  Music,
  Video,
  Box,
  Camera,
  Trash2,
  Eye,
  EyeOff,
  Lock,
  Unlock,
  Circle
} from 'lucide-react';
import { translations } from './i18n';
import { useAppStore } from './store';
import { Layer, Project, INITIAL_STATE } from './types';
import { Documentation } from './components/Documentation';

// --- Sub-components ---

const WelcomeOnboarding = ({ onComplete, t }: { onComplete: (res: string) => void, t: any }) => {
  const [step, setStep] = React.useState<'welcome' | 'tos' | 'resolution'>('welcome');
  const [accepted, setAccepted] = React.useState(false);
  const [resolution, setResolution] = React.useState('1080x1920');

  const detectResolution = () => {
    const w = window.innerWidth;
    const h = window.innerHeight;
    if (w > h) return "1920x1080";
    return "1080x1920";
  };

  React.useEffect(() => {
    if (step === 'resolution') {
      setResolution(detectResolution());
    }
  }, [step]);

  return (
    <motion.div 
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      exit={{ opacity: 0 }}
      className="fixed inset-0 z-50 flex items-center justify-center bg-[#1C1B1F] p-6 lg:p-12 text-[#E6E1E5]"
    >
      <div className="max-w-md w-full text-center">
        <AnimatePresence mode="wait">
          {step === 'welcome' && (
            <motion.div 
              key="welcome"
              initial={{ x: 20, opacity: 0 }}
              animate={{ x: 0, opacity: 1 }}
              exit={{ x: -20, opacity: 0 }}
              className="space-y-8"
            >
              <div className="flex justify-center">
                <div className="relative h-28 w-28 rounded-[32px] bg-[#D0BCFF] flex items-center justify-center shadow-2xl shadow-[#D0BCFF]/10">
                  <Play fill="#381E72" className="h-14 w-14 text-[#381E72] ml-1" />
                </div>
              </div>
              <h1 className="text-5xl font-black tracking-tighter text-[#E6E1E5] italic">LUMINA</h1>
              <p className="text-lg text-[#CAC4D0] leading-relaxed">The most expressive mobile-first motion graphics tool.</p>
              <button 
                onClick={() => setStep('tos')}
                className="w-full rounded-full bg-[#D0BCFF] py-4 text-lg font-bold text-[#381E72] shadow-lg transition-all active:scale-95 hover:opacity-90 cursor-pointer"
              >
                Get Started
              </button>
            </motion.div>
          )}

          {step === 'tos' && (
            <motion.div 
              key="tos"
              initial={{ x: 20, opacity: 0 }}
              animate={{ x: 0, opacity: 1 }}
              exit={{ x: -20, opacity: 0 }}
              className="space-y-6 text-left"
            >
              <h2 className="text-3xl font-bold text-[#E6E1E5]">Terms of Service</h2>
              <div className="h-64 overflow-y-auto bg-[#49454F]/20 rounded-3xl p-6 text-sm text-[#CAC4D0] space-y-4 font-normal custom-scrollbar">
                <p>Welcome to Lumina Motion. By using this app, you agree to the following terms:</p>
                <ul className="list-disc pl-4 space-y-2">
                  <li>You own all content you create.</li>
                  <li>We do not store your footage on our servers.</li>
                  <li>Render performance depends on your device capabilities.</li>
                  <li>Respect copyright laws when using external assets.</li>
                </ul>
                <p>Lumina Motion is provided "as is" without warranty of any kind.</p>
              </div>
              <label className="flex items-center gap-4 cursor-pointer p-2 rounded-2xl hover:bg-white/5 transition-colors">
                <input 
                  type="checkbox" 
                  checked={accepted} 
                  onChange={(e) => setAccepted(e.target.checked)}
                  className="w-6 h-6 rounded-lg bg-[#49454F] border-none text-[#D0BCFF] focus:ring-[#D0BCFF]"
                />
                <span className="text-[#CAC4D0]">I agree to the Terms of Service</span>
              </label>
              <button 
                disabled={!accepted}
                onClick={() => setStep('resolution')}
                className={`w-full rounded-full py-4 text-lg font-bold shadow-lg transition-all active:scale-95 cursor-pointer ${
                  accepted ? 'bg-[#D0BCFF] text-[#381E72]' : 'bg-[#49454F] text-[#CAC4D0] opacity-50 cursor-not-allowed'
                }`}
              >
                Continue
              </button>
            </motion.div>
          )}

          {step === 'resolution' && (
            <motion.div 
              key="res"
              initial={{ x: 20, opacity: 0 }}
              animate={{ x: 0, opacity: 1 }}
              exit={{ x: -20, opacity: 0 }}
              className="space-y-8"
            >
              <h2 className="text-3xl font-bold text-[#E6E1E5]">Device Optimization</h2>
              <p className="text-[#CAC4D0]">We've analyzed your screen. Here is the recommended project resolution for your phone:</p>
              <div className="p-8 rounded-[40px] bg-[#EADDFF] text-[#21005D]">
                <div className="text-5xl font-black mb-2">{resolution}</div>
                <div className="text-xs font-bold uppercase tracking-widest opacity-60">High Performance Mode</div>
              </div>
              <button 
                onClick={() => onComplete(resolution)}
                className="w-full rounded-full bg-[#D0BCFF] py-4 text-lg font-bold text-[#381E72] shadow-lg transition-all active:scale-95 cursor-pointer"
              >
                Launch Editor
              </button>
            </motion.div>
          )}
        </AnimatePresence>
      </div>
    </motion.div>
  );
};

const TutorialOverlay = ({ onClose }: { onClose: () => void }) => {
  const [step, setStep] = React.useState(0);
  const steps = [
    { title: "Canvas", desc: "This is where your creation comes to life. Scale and position layers directly here.", icon: <Play className="w-8 h-8"/> },
    { title: "Timeline", desc: "Scrub the red playhead to move through time. Add keyframes to animate properties.", icon: <Layers className="w-8 h-8"/> },
    { title: "Inspector", desc: "Tweak every detail here. From effects to transform points, it's all in your hands.", icon: <Settings className="w-8 h-8"/> }
  ];

  return (
    <motion.div 
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      exit={{ opacity: 0 }}
      className="fixed inset-0 z-[60] bg-black/40 backdrop-blur-sm flex items-center justify-center p-6"
    >
      <motion.div 
        layoutId="tutorial-box"
        className="max-w-xs w-full bg-[#EADDFF] text-[#21005D] p-8 rounded-[48px] shadow-2xl space-y-6"
      >
        <div className="flex justify-between items-center">
          <span className="text-[10px] font-black uppercase tracking-[0.2em] opacity-40">Step {step + 1} of {steps.length}</span>
          <button onClick={onClose} className="p-2 hover:bg-black/5 rounded-full cursor-pointer"><Plus className="h-5 w-5 rotate-45" /></button>
        </div>
        <AnimatePresence mode="wait">
          <motion.div 
            key={step}
            initial={{ y: 10, opacity: 0 }}
            animate={{ y: 0, opacity: 1 }}
            exit={{ y: -10, opacity: 0 }}
            className="space-y-4"
          >
            <div className="bg-[#D0BCFF] w-16 h-16 rounded-2xl flex items-center justify-center text-[#381E72] shadow-xl">
              {steps[step].icon}
            </div>
            <h3 className="text-3xl font-black tracking-tight leading-none">{steps[step].title}</h3>
            <p className="text-[#21005D]/70 leading-snug">{steps[step].desc}</p>
          </motion.div>
        </AnimatePresence>
        <div className="flex gap-2">
          {step > 0 && (
            <button 
              onClick={() => setStep(s => s - 1)}
              className="flex-1 py-3 rounded-full border-2 border-[#21005D]/20 font-bold cursor-pointer"
            >
              Back
            </button>
          )}
          <button 
            onClick={() => step === steps.length - 1 ? onClose() : setStep(s => s + 1)}
            className="flex-1 bg-[#21005D] text-white py-3 rounded-full font-bold shadow-lg cursor-pointer transition-all active:scale-95"
          >
            {step === steps.length - 1 ? "Got it!" : "Next"}
          </button>
        </div>
      </motion.div>
    </motion.div>
  );
};

const IconButton = ({ children, onClick, active = false, className = "" }: any) => (
  <button 
    onClick={onClick}
    className={`flex h-12 w-12 items-center justify-center rounded-full transition-all duration-300 active:scale-90 cursor-pointer ${
      active ? 'bg-[#D0BCFF] text-[#381E72] shadow-xl' : 'text-[#CAC4D0] hover:bg-[#49454F]'
    } ${className}`}
  >
    {children}
  </button>
);

// --- Main App Component ---

export default function App() {
  const { 
    state, 
    setLanguage, 
    setTheme, 
    closeOnboarding, 
    selectLayer, 
    updateCurrentTime, 
    togglePlayback,
    addLayer
  } = useAppStore();

  const [showDocs, setShowDocs] = React.useState(false);
  const [showTutorial, setShowTutorial] = React.useState(false);
  const timelineRef = React.useRef<HTMLDivElement>(null);

  const handleTimelineClick = (e: React.MouseEvent) => {
    if (!timelineRef.current) return;
    const rect = timelineRef.current.getBoundingClientRect();
    const x = e.clientX - rect.left;
    const percentage = x / rect.width;
    updateCurrentTime(percentage * state.project.duration);
  };

  const t = translations[state.language];

  // Derived Values
  const selectedLayer = state.layers.find(l => l.id === state.selectedLayerId);

  return (
    <div className={`h-screen w-screen bg-[#1C1B1F] text-[#E6E1E5] font-sans overflow-hidden select-none flex flex-col ${state.theme}`}>
      <AnimatePresence>
        {state.showOnboarding && <WelcomeOnboarding onComplete={(res) => {
          closeOnboarding();
          setShowTutorial(true);
        }} t={t} />}
        {showDocs && <Documentation onClose={() => setShowDocs(false)} />}
        {showTutorial && <TutorialOverlay onClose={() => setShowTutorial(false)} />}
      </AnimatePresence>

      {/* --- MD3 Top App Bar --- */}
      <header className="flex h-16 items-center justify-between border-b border-[#49454F] px-4 bg-[#1C1B1F] z-20 shrink-0">
        <div className="flex items-center gap-4">
          <IconButton onClick={() => {}} className="bg-[#49454F]/30">
            <ChevronLeft className="h-5 w-5" />
          </IconButton>
          <div className="cursor-pointer" onClick={() => setShowDocs(true)}>
            <div className="flex items-center gap-2">
              <h1 className="text-lg font-black tracking-tighter italic text-[#D0BCFF]">LUMINA</h1>
              <h2 className="text-sm font-medium tracking-tight opacity-80">{state.project.name}</h2>
            </div>
            <p className="text-[10px] text-[#CAC4D0] font-mono uppercase tracking-[0.2em]">{state.project.width}x{state.project.height} • {state.project.fps} FPS</p>
          </div>
        </div>
        <div className="flex items-center gap-2">
          <div className="hidden sm:flex bg-white/5 rounded-full p-1 mr-4">
             {['en', 'es', 'zh', 'fr'].map(lang => (
               <button 
                 key={lang}
                 onClick={() => setLanguage(lang as any)}
                 className={`px-2 py-1 rounded-full text-[10px] font-bold uppercase transition-colors cursor-pointer ${state.language === lang ? 'bg-[#D0BCFF] text-[#381E72]' : 'text-gray-500 hover:text-[#CAC4D0]'}`}
               >
                 {lang}
               </button>
             ))}
          </div>
          <IconButton onClick={() => setShowDocs(true)}><HelpCircle className="h-5 w-5" /></IconButton>
          <IconButton onClick={() => setShowTutorial(true)} className="text-[#D0BCFF]"><Maximize2 className="h-5 w-5" /></IconButton>
          <button className="bg-[#EADDFF] text-[#21005D] ml-2 px-6 py-2 rounded-full text-xs font-bold uppercase tracking-widest shadow-md hover:bg-white/90 transition-all cursor-pointer active:scale-95">
            {t.export}
          </button>
        </div>
      </header>

      {/* --- Main Content Area (Portrait Stack) --- */}
      <main className="flex-1 relative flex flex-col overflow-hidden">
        
        {/* Preview Viewport (Upper part) */}
        <section className="flex-[6] flex items-center justify-center bg-black overflow-hidden relative p-4">
          <div 
            className="relative bg-black shadow-[0_0_80px_rgba(0,0,0,0.6)] overflow-hidden ring-1 ring-[#49454F] rounded-lg"
            style={{ 
              aspectRatio: `${state.project.width} / ${state.project.height}`,
              maxHeight: '100%',
              maxWidth: '100%'
             }}
          >
            {/* Visualizer / Canvas Emulator */}
            <div className="absolute inset-0 flex items-center justify-center overflow-hidden">
              <div className="absolute inset-0 bg-blue-500/5 mix-blend-overlay"></div>
              <motion.div 
                animate={{ rotate: state.currentTime * 20 }}
                className="w-3/4 h-3/4 border-2 border-[#D0BCFF]/30 border-dashed rounded-full flex items-center justify-center"
              >
                 <motion.div 
                   animate={{ scale: [1, 1.2, 1], rotate: [0, 90, 0] }}
                   transition={{ repeat: Infinity, duration: 4 }}
                   className="w-1/2 h-1/2 bg-[#D0BCFF]/10 rounded-full blur-[60px]" 
                 />
              </motion.div>
            </div>
          </div>

          {/* MD3 Playback HUD */}
          <div className="absolute bottom-6 flex items-center gap-6 bg-[#1C1B1F]/90 backdrop-blur-2xl px-8 py-3 rounded-full border border-[#49454F] shadow-2xl">
             <button onClick={() => updateCurrentTime(Math.max(0, state.currentTime - 0.1))} className="text-[#CAC4D0] hover:text-white transition-colors cursor-pointer p-2 rounded-full active:bg-[#49454F]/50">
               <ChevronLeft className="h-6 w-6" />
             </button>
             <button 
                onClick={togglePlayback} 
                className="h-16 w-16 rounded-full bg-[#D0BCFF] text-[#381E72] flex items-center justify-center hover:scale-105 transition-transform active:scale-95 shadow-lg cursor-pointer"
              >
               {state.isPlaying ? <Pause className="h-8 w-8" fill="currentColor" /> : <Play className="h-8 w-8 ml-1" fill="currentColor" />}
             </button>
             <button onClick={() => updateCurrentTime(Math.min(state.project.duration, state.currentTime + 0.1))} className="text-[#CAC4D0] hover:text-white transition-colors rotate-180 cursor-pointer p-2 rounded-full active:bg-[#49454F]/50">
               <ChevronLeft className="h-6 w-6" />
             </button>
          </div>
        </section>

        {/* --- MD3 Expressive Timeline (Bottom portion) --- */}
        <section className="flex-[4] border-t border-[#49454F] flex flex-col bg-[#1C1B1F] z-10 shadow-[0_-20px_40px_rgba(0,0,0,0.3)]">
          
          {/* Timeline Toolbar */}
          <div className="flex h-14 items-center justify-between px-6 border-b border-[#49454F]">
            <div className="flex items-center gap-6 text-[10px] font-bold uppercase tracking-[0.2em]">
              <button 
                onClick={() => selectLayer(null)}
                className={`flex items-center gap-2 transition-colors cursor-pointer ${!selectedLayer ? 'text-[#D0BCFF]' : 'text-[#CAC4D0] hover:text-white'}`}
              >
                <Layers className="h-4 w-4" />
                {t.layers}
              </button>
              <button className="text-[#CAC4D0] hover:text-white cursor-pointer transition-colors">Assets</button>
              <button className="text-[#CAC4D0] hover:text-white cursor-pointer transition-colors">Audio</button>
            </div>
            <div className="flex items-center gap-4">
              <span className="font-mono text-xs font-bold text-[#D0BCFF] bg-[#D0BCFF]/10 px-3 py-1 rounded-full">
                {state.currentTime.toFixed(2).replace('.', ':')}
              </span>
              <IconButton className="scale-75"><Settings className="h-5 w-5" /></IconButton>
            </div>
          </div>

          <div className="flex-1 flex overflow-hidden">
            {/* Layer List (Portrait rail) */}
            <div className="w-16 md:w-48 border-r border-[#49454F] overflow-y-auto bg-black/5 custom-scrollbar">
              {state.layers.map(layer => (
                <div 
                  key={layer.id}
                  onClick={() => selectLayer(layer.id)}
                  className={`group relative flex h-14 items-center ${
                    state.selectedLayerId === layer.id ? 'bg-[#D0BCFF]/10 border-l-4 border-l-[#D0BCFF]' : 'hover:bg-white/5'
                  } transition-all cursor-pointer border-b border-[#49454F] px-4 overflow-hidden`}
                >
                  <div className="flex items-center gap-3 w-full">
                    <div className="h-8 w-8 rounded-xl bg-[#49454F]/50 flex items-center justify-center shrink-0">
                      {layer.type === 'shape' && <Square className="h-4 w-4 text-[#D0BCFF]" />}
                      {layer.type === 'text' && <Type className="h-4 w-4 text-green-400" />}
                      {layer.type === 'camera' && <Camera className="h-4 w-4 text-purple-400" />}
                      {layer.type === 'null' && <Box className="h-4 w-4 text-orange-400" />}
                    </div>
                    <span className="text-[10px] font-bold uppercase tracking-widest truncate hidden md:block">
                      {layer.name}
                    </span>
                  </div>
                </div>
              ))}
            </div>

            {/* Timeline Tracks */}
            <div 
              ref={timelineRef}
              onClick={handleTimelineClick}
              className="flex-1 relative overflow-x-auto overflow-y-hidden bg-[#1C1B1F] custom-scrollbar cursor-crosshair"
            >
              <div className="sticky top-0 h-8 w-full border-b border-[#49454F] bg-[#1C1B1F] z-10">
                 <div className="relative h-full w-full">
                    {Array.from({ length: state.project.duration + 1 }).map((_, i) => (
                      <div 
                        key={i} 
                        className="absolute bottom-0 h-3 border-l border-[#49454F] text-[8px] pl-1 text-[#CAC4D0]/50 font-mono"
                        style={{ left: `${(i / state.project.duration) * 100}%` }}
                      >
                        {i}:00
                      </div>
                    ))}
                 </div>
              </div>

              <div className="relative h-full">
                {state.layers.map(layer => (
                  <div key={layer.id} className="h-14 w-full border-b border-[#49454F] flex items-center relative">
                    <motion.div 
                      layoutId={`layer-bar-${layer.id}`}
                      className={`absolute h-8 rounded-xl ${
                        state.selectedLayerId === layer.id ? 'bg-[#D0BCFF] shadow-2xl shadow-[#D0BCFF]/20' : 'bg-[#49454F]/40'
                      }`}
                      style={{ 
                        left: `${(layer.startTime / state.project.duration) * 100}%`,
                        width: `${(layer.duration / state.project.duration) * 100}%`
                      }}
                    >
                      <div className="absolute inset-0 flex items-center px-4 pointer-events-none opacity-40">
                         {state.selectedLayerId === layer.id && (
                           <>
                            <Circle className="h-2 w-2 fill-white text-white rotate-45" />
                            <div className="ml-8 h-2 w-2 fill-white text-white rotate-45"><Circle /></div>
                           </>
                         )}
                      </div>
                    </motion.div>
                  </div>
                ))}

                {/* Red Playhead */}
                <div 
                  className="absolute top-[-32px] bottom-0 w-[2px] bg-red-500 z-20 pointer-events-none shadow-[0_0_10px_rgba(239,68,68,0.5)]"
                  style={{ left: `${(state.currentTime / state.project.duration) * 100}%` }}
                >
                  <div className="absolute top-0 -left-[5px] w-0 h-0 border-l-[6px] border-l-transparent border-r-[6px] border-r-transparent border-t-[8px] border-t-red-500" />
                </div>
              </div>
            </div>
          </div>
        </section>
      </main>

      {/* --- MD3 Side Inspector (Bottom Sheet in Portrait) --- */}
      <AnimatePresence>
        {selectedLayer && (
          <motion.aside 
            initial={{ y: "100%" }}
            animate={{ y: 0 }}
            exit={{ y: "100%" }}
            transition={{ type: "spring", damping: 25, stiffness: 200 }}
            className="fixed inset-x-0 bottom-0 top-[30%] bg-[#1C1B1F] border-t border-[#49454F] z-50 flex flex-col lg:inset-y-0 lg:right-0 lg:left-auto lg:w-[400px] lg:border-l lg:border-t-0 shadow-2xl lg:translate-y-0 rounded-t-[48px] lg:rounded-none overflow-hidden"
          >
            <div className="h-8 flex justify-center items-center lg:hidden cursor-row-resize py-4" onClick={() => selectLayer(null)}>
              <div className="w-12 h-1.5 bg-[#49454F] rounded-full opacity-50"></div>
            </div>

            <div className="px-8 pb-4 pt-4 border-b border-[#49454F] flex items-center justify-between">
              <div>
                <h3 className="text-2xl font-black text-[#D0BCFF] tracking-tight">{t.properties}</h3>
                <p className="text-[10px] text-[#CAC4D0] uppercase font-bold tracking-[0.2em]">{selectedLayer.name}</p>
              </div>
              <div className="flex gap-2">
                <IconButton className="scale-90 bg-red-500/10 text-red-400 hover:bg-red-500 hover:text-white"><Trash2 className="h-5 w-5" /></IconButton>
                <IconButton onClick={() => selectLayer(null)}><Plus className="h-5 w-5 rotate-45" /></IconButton>
              </div>
            </div>

            <div className="flex-1 overflow-y-auto px-8 py-6 space-y-10 custom-scrollbar pb-32">
               {['Transform', 'Blending', 'Effects'].map(cat => (
                 <section key={cat} className="space-y-6">
                    <div className="flex items-center justify-between">
                      <h4 className="text-[10px] font-black uppercase tracking-[0.3em] text-[#CAC4D0]">{cat}</h4>
                      {cat === 'Effects' && (
                        <button className="flex items-center gap-2 bg-[#D0BCFF]/10 text-[#D0BCFF] px-4 py-2 rounded-full text-[10px] font-black uppercase hover:bg-[#D0BCFF]/20 transition-all cursor-pointer">
                          <Plus className="h-4 w-4" /> Add Effect
                        </button>
                      )}
                    </div>

                    {cat === 'Transform' && (
                      <div className="space-y-6">
                        {[
                          { name: 'Position X', val: selectedLayer.properties.position.value.x, min: 0, max: state.project.width },
                          { name: 'Position Y', val: selectedLayer.properties.position.value.y, min: 0, max: state.project.height },
                          { name: 'Scale', val: selectedLayer.properties.scale.value, min: 0, max: 200 },
                          { name: 'Rotation', val: selectedLayer.properties.rotation.value, min: -180, max: 180 },
                        ].map(prop => (
                          <div key={prop.name} className="space-y-3">
                             <div className="flex justify-between text-xs font-bold">
                               <span className="text-[#CAC4D0]">{prop.name}</span>
                               <span className="text-[#D0BCFF] font-mono">{prop.val}</span>
                             </div>
                             <div className="relative h-1.5 w-full bg-[#49454F] rounded-full overflow-visible">
                                <div 
                                  className="absolute h-1.5 bg-[#D0BCFF] rounded-full" 
                                  style={{ width: `${((prop.val - prop.min) / (prop.max - prop.min)) * 100}%` }} 
                                />
                                <div 
                                   className="absolute top-1/2 h-5 w-5 bg-[#D0BCFF] rounded-full shadow-lg cursor-pointer -translate-y-1/2 scale-110 active:scale-125 transition-transform"
                                   style={{ left: `${((prop.val - prop.min) / (prop.max - prop.min)) * 100}%`, transform: 'translate(-50%, -50%)' }}
                                />
                             </div>
                          </div>
                        ))}
                      </div>
                    )}
                 </section>
               ))}
            </div>

            {/* Bottom Actions for Inspector */}
            <div className="absolute bottom-8 inset-x-8">
              <button className="w-full bg-[#EADDFF] text-[#21005D] py-5 rounded-[28px] font-black uppercase tracking-widest shadow-2xl hover:scale-[1.02] active:scale-[0.98] transition-all cursor-pointer">
                Add Keyframe
              </button>
            </div>
          </motion.aside>
        )}
      </AnimatePresence>

      {/* --- Mobile Add Layer Button --- */}
      <motion.button 
        whileHover={{ scale: 1.1 }}
        whileTap={{ scale: 0.9 }}
        className="fixed bottom-36 right-8 h-16 w-16 rounded-full bg-indigo-600 text-white shadow-2xl shadow-indigo-600/40 flex items-center justify-center z-40 lg:hidden"
      >
        <Plus className="h-8 w-8" />
      </motion.button>

      {/* Style Overrides */}
      <style>{`
        .custom-scrollbar::-webkit-scrollbar { width: 4px; height: 4px; }
        .custom-scrollbar::-webkit-scrollbar-track { background: transparent; }
        .custom-scrollbar::-webkit-scrollbar-thumb { background: rgba(255,255,255,0.1); border-radius: 10px; }
        .custom-scrollbar::-webkit-scrollbar-thumb:hover { background: rgba(255,255,255,0.2); }
      `}</style>
    </div>
  );
}
