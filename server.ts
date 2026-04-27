import express from "express";
import path from "path";

const app = express();
const PORT = 3000;

app.get("/", (req, res) => {
  res.send(`
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Developer Dashboard | Lumina Motion</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;700;900&display=swap" rel="stylesheet">
        <style>
            body { font-family: 'Roboto', sans-serif; background-color: #1C1B1F; color: #E6E1E5; }
            .glow { box-shadow: 0 0 20px rgba(208, 188, 255, 0.2); }
        </style>
    </head>
    <body class="p-8">
        <div class="max-w-3xl mx-auto space-y-12">
            <!-- Header -->
            <header class="text-center">
                <div class="w-24 h-24 bg-[#D0BCFF] text-[#381E72] rounded-[32px] mx-auto flex items-center justify-center shadow-2xl mb-6">
                    <svg class="w-12 h-12" fill="currentColor" viewBox="0 0 24 24"><path d="M8 5v14l11-7z" /></svg>
                </div>
                <h1 class="text-5xl font-black italic tracking-tighter text-[#D0BCFF]">LUMINA</h1>
                <p class="text-[#CAC4D0] mt-2 uppercase tracking-[0.3em] font-bold text-xs">Android Build Dashboard</p>
            </header>

            <!-- Status Card -->
            <section class="bg-[#2B2930] rounded-[48px] p-10 border border-[#49454F] glow">
                <h2 class="text-3xl font-bold mb-6">Project Ready</h2>
                <p class="text-[#CAC4D0] mb-8 leading-relaxed">
                    Your <b>Jetpack Compose</b> project is fully initialized. The architecture is optimized for mobile-first video editing with Material 3 Expressive UI.
                </p>
                <div class="space-y-4">
                    <div class="flex items-center gap-4 bg-[#1C1B1F] p-4 rounded-3xl">
                        <div class="w-3 h-3 bg-green-400 rounded-full animate-pulse"></div>
                        <span class="text-sm font-medium">Native Android Architecture (Gradle 8.2)</span>
                    </div>
                    <div class="flex items-center gap-4 bg-[#1C1B1F] p-4 rounded-3xl">
                        <div class="w-3 h-3 bg-green-400 rounded-full"></div>
                        <span class="text-sm font-medium">Jetpack Compose & MD3 Expressive</span>
                    </div>
                    <div class="flex items-center gap-4 bg-[#1C1B1F] p-4 rounded-3xl">
                        <div class="w-3 h-3 bg-[#D0BCFF] rounded-full"></div>
                        <span class="text-sm font-medium">GitHub Actions CI/CD Configured</span>
                    </div>
                </div>
            </section>

            <!-- Guide -->
            <section class="space-y-6">
                <h3 class="text-xs font-black uppercase tracking-[0.4em] text-[#CAC4D0]">How to Build APK</h3>
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div class="bg-[#49454F]/20 p-6 rounded-3xl border border-[#49454F]">
                        <p class="text-sm font-bold mb-2 text-[#D0BCFF]">1. Push to GitHub</p>
                        <p class="text-xs text-[#CAC4D0]">Commit your changes and push them to your repository.</p>
                    </div>
                    <div class="bg-[#49454F]/20 p-6 rounded-3xl border border-[#49454F]">
                        <p class="text-sm font-bold mb-2 text-[#D0BCFF]">2. Download Artifact</p>
                        <p class="text-xs text-[#CAC4D0]">Check 'Actions' tab on GitHub to download the generated debug-apk.</p>
                    </div>
                </div>
            </section>
            
            <footer class="text-center pt-12 border-t border-[#49454F]">
                <p class="text-[10px] text-gray-500 font-mono">APP_VERSION: 1.0.0-ANDROID-STABLE</p>
            </footer>
        </div>
    </body>
    </html>
  `);
});

app.listen(PORT, "0.0.0.0", () => {
  console.log(`Dashboard running on http://localhost:${PORT}`);
});
