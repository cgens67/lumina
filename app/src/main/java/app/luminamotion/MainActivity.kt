package app.luminamotion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.text.style.TextAlign
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.zIndex

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LuminaTheme {
                AppNavigation()
            }
        }
    }
}

data class ProjectConfig(
    val name: String = "New Project 1",
    val ratio: Float = 1f,
    val resolution: String = "1080p (FHD)",
    val frameRate: Int = 30,
    val backgroundColor: Color = Color(0xFF1C1B1F)
)

enum class LuminaScreen {
    TutorialMain, ProjectSetup, Editor
}

@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf(LuminaScreen.TutorialMain) }
    var projectConfig by remember { mutableStateOf(ProjectConfig()) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF1C1B1F)
    ) {
        Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
            when (screen) {
                LuminaScreen.TutorialMain -> TutorialMainScreen(
                    onNewProject = { currentScreen = LuminaScreen.ProjectSetup }
                )
                LuminaScreen.ProjectSetup -> ProjectSetupScreen(
                    onBack = { currentScreen = LuminaScreen.TutorialMain },
                    onCreate = { config ->
                        projectConfig = config
                        currentScreen = LuminaScreen.Editor
                    }
                )
                LuminaScreen.Editor -> MainEditorScreen(
                    config = projectConfig,
                    onExit = { currentScreen = LuminaScreen.TutorialMain }
                )
            }
        }
    }
}

@Composable
fun TutorialMainScreen(onNewProject: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Image/Logo Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.03f), RoundedCornerShape(24.dp))
                    .padding(24.dp)
            ) {
                Text(
                    "\"Where do I start?\"",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    "Posted 25 April 2026",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "New to Lumina? We've got you covered.\nStart with this project.\n\nIt's simple. It walks you through all the basics. And soon you'll realize just how amazing motion design is!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f),
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(32.dp))
                
                OutlinedButton(
                    onClick = { /* Tutorial Link */ },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                ) {
                    Text("WATCH THE TUTORIAL", color = Color.White)
                }
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { /* Download Link */ },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                ) {
                    Text("DOWNLOAD THE PROJECT", color = Color.White)
                }
            }
        }

        // Floating Plus Button for New Project
        LargeFloatingActionButton(
            onClick = onNewProject,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            containerColor = Color(0xFF00D1FF),
            shape = CircleShape
        ) {
            Icon(Icons.Default.Add, contentDescription = "New Project", tint = Color.Black)
        }
    }
}

@Composable
fun ProjectSetupScreen(onBack: () -> Unit, onCreate: (ProjectConfig) -> Unit) {
    var name by remember { mutableStateOf("New Project 1") }
    var selectedRatio by remember { mutableFloatStateOf(1f) }
    var selectedRes by remember { mutableStateOf("1080p (FHD)") }
    var selectedFPS by remember { mutableIntStateOf(30) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .background(Color(0xFF1C1B1F))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }
            Text("PROJECT", fontWeight = FontWeight.Bold, color = Color.White)
            Text("ELEMENT", fontWeight = FontWeight.Bold, color = Color.Gray)
            Spacer(modifier = Modifier.width(48.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Name Input
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
            trailingIcon = { Icon(Icons.Default.Edit, contentDescription = null, tint = Color.Gray) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White.copy(alpha = 0.2f),
                unfocusedBorderColor = Color.White.copy(alpha = 0.1f)
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Ratio Selection
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val ratios = listOf(16/9f to "16:9", 9/16f to "9:16", 4/5f to "4:5", 1f to "1:1", 4/3f to "4:3")
            ratios.forEach { (valRatio, label) ->
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (selectedRatio == valRatio) Color(0xFF00D1FF) else Color.White.copy(alpha = 0.05f))
                        .clickable { selectedRatio = valRatio },
                    contentAlignment = Alignment.Center
                ) {
                    Text(label, color = if (selectedRatio == valRatio) Color.Black else Color.White, style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Resolution & FPS Dropdowns (Simplified for UI Demo)
        PropertyDropdown("Resolution", selectedRes)
        PropertyDropdown("Frame Rate", "${selectedFPS} fps")
        PropertyDropdown("Background", "Light Grey")

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { onCreate(ProjectConfig(name, selectedRatio, selectedRes, selectedFPS)) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.1f)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("CREATE PROJECT", fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Composable
fun PropertyDropdown(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color.Gray)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(value, color = Color.White, fontWeight = FontWeight.Bold)
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.Gray)
        }
    }
}

@Composable
fun PreviewArea(activeEffects: Set<String> = emptySet()) {
    var offset by remember { mutableStateOf(Offset.Zero) }
    
    val blurAmount = if (activeEffects.contains("Gaussian Blur")) 10.dp else 0.dp
    val motionBlur = activeEffects.contains("Motion Blur")
    val oscillate = activeEffects.contains("Oscillate")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offset += dragAmount
                }
            },
        contentAlignment = Alignment.Center
    ) {
        // Mock canvas object
        Column(
            modifier = Modifier.offset(offset.x.dp / 8, offset.y.dp / 8),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(100.dp),
                color = Color(0xFF00FF85).copy(alpha = if (blurAmount > 0.dp) 0.5f else 1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (blurAmount > 0.dp) {
                    Box(modifier = Modifier.fillMaxSize().background(Color.White.copy(alpha = 0.2f)))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                if (oscillate) "~~~ OSCILLATING ~~~" else "LUMINA MOTION", 
                color = if (motionBlur) Color.White.copy(alpha = 0.5f) else Color.White, 
                fontWeight = FontWeight.Bold, 
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
fun LuminaTheme(content: @Composable () -> Unit) {
    val darkColorScheme = darkColorScheme(
        primary = Color(0xFFD0BCFF),
        onPrimary = Color(0xFF381E72),
        primaryContainer = Color(0xFFEADDFF),
        onPrimaryContainer = Color(0xFF21005D),
        surface = Color(0xFF1C1B1F),
        onSurface = Color(0xFFE6E1E5)
    )
    MaterialTheme(colorScheme = darkColorScheme, content = content)
}

@Composable
fun MainEditorScreen(config: ProjectConfig, onExit: () -> Unit) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }
    var currentTime by remember { mutableLongStateOf(0L) }
    var showAddMenu by remember { mutableStateOf(false) }
    var showEffectBrowser by remember { mutableStateOf(false) }
    var activeEffects by remember { mutableStateOf(setOf<String>()) }
    val context = LocalContext.current
    
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1C1B1F))
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onExit) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Text(config.name, style = MaterialTheme.typography.titleMedium, color = Color.White)
                }
                TextButton(
                    onClick = { Toast.makeText(context, "Exporting...", Toast.LENGTH_SHORT).show() },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFD0BCFF))
                ) {
                    Text("EXPORT", fontWeight = FontWeight.Black)
                }
            }

            // Preview Area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                PreviewArea(activeEffects)
            }

            // Playback Controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* Undo */ }) { Icon(Icons.Default.Undo, null, tint = Color.LightGray) }
                IconButton(onClick = { /* Redo */ }) { Icon(Icons.Default.Redo, null, tint = Color.LightGray) }
                Spacer(modifier = Modifier.width(16.dp))
                IconButton(onClick = { currentTime = 0 }) { Icon(Icons.Default.SkipPrevious, null, tint = Color.White) }
                FilledIconButton(
                    onClick = { isPlaying = !isPlaying },
                    modifier = Modifier.size(56.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color.White)
                ) {
                    Icon(if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, null, tint = Color.Black)
                }
                IconButton(onClick = { /* Skip to end */ }) { Icon(Icons.Default.SkipNext, null, tint = Color.White) }
                Spacer(modifier = Modifier.width(16.dp))
                IconButton(onClick = { /* Fullscreen */ }) { Icon(Icons.Default.Fullscreen, null, tint = Color.LightGray) }
            }

            // Time Display
            Text(
                "00:00:00", 
                modifier = Modifier.fillMaxWidth(), 
                textAlign = TextAlign.Center, 
                color = Color.Gray, 
                style = MaterialTheme.typography.labelSmall
            )

            // Timeline / Layer Area
            Column(
                modifier = Modifier
                    .height(280.dp)
                    .fillMaxWidth()
                    .background(Color(0xFF121212))
            ) {
                // Timeline Header
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(Icons.Default.Visibility, null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                    Box(modifier = Modifier.width(2.dp).height(20.dp).background(Color.White))
                }
                
                // Layers
                Column(modifier = Modifier.weight(1f)) {
                    LayerRow("Overlay_01", Color(0xFF2E2D6D))
                    LayerRow("Text_Main", Color(0xFF5E5478))
                    LayerRow("Background", Color(0xFF444444))
                }
            }

            // Bottom Nav
            BottomNavBar(selectedTab) { 
                selectedTab = it 
                if (it == 1) showEffectBrowser = true
            }
        }

        // Add Button (+)
        LargeFloatingActionButton(
            onClick = { showAddMenu = true },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 72.dp),
            containerColor = Color(0xFF00FF85),
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 12.dp),
            shape = CircleShape
        ) {
            Icon(Icons.Default.Add, null, tint = Color.Black)
        }

        // Add Menu Overlay
        if (showAddMenu) {
            AddMenuOverlay(onDismiss = { showAddMenu = false })
        }

        // Effect Browser
        if (showEffectBrowser) {
            EffectBrowserOverlay(
                onDismiss = { showEffectBrowser = false; selectedTab = 0 },
                onEffectAdded = { activeEffects = activeEffects + it }
            )
        }
    }
}

@Composable
fun LayerRow(name: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(name, color = Color.Gray, style = MaterialTheme.typography.labelSmall, modifier = Modifier.width(80.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(24.dp)
                .background(color.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
        ) {
            Box(modifier = Modifier.size(8.dp).align(Alignment.CenterStart).padding(start = 4.dp).background(color))
        }
    }
}

@Composable
fun AddMenuOverlay(onDismiss: () -> Unit) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f))
            .clickable { onDismiss() }
            .zIndex(100f),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF242426), RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .padding(24.dp)
                .clickable(enabled = false) {}
        ) {
            Text("Add Content", fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                AddMenuItem(Icons.Default.ShapeLine, "Shape", Color(0xFF00FF85)) {
                    Toast.makeText(context, "Shape tool active", Toast.LENGTH_SHORT).show()
                    onDismiss()
                }
                AddMenuItem(Icons.Default.Image, "Media", Color(0xFF00D1FF)) {
                    Toast.makeText(context, "Select from Gallery or Camera", Toast.LENGTH_SHORT).show()
                    onDismiss()
                }
                AddMenuItem(Icons.Default.Audiotrack, "Audio", Color(0xFFD0BCFF)) {
                    Toast.makeText(context, "Import music or recording", Toast.LENGTH_SHORT).show()
                    onDismiss()
                }
                AddMenuItem(Icons.Default.TextFormat, "Text", Color(0xFFFFD600)) {
                    Toast.makeText(context, "Text layer added", Toast.LENGTH_SHORT).show()
                    onDismiss()
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun AddMenuItem(icon: ImageVector, label: String, color: Color, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Surface(shape = CircleShape, color = color.copy(alpha = 0.1f), modifier = Modifier.size(56.dp)) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = color)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, color = Color.Gray, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun EffectBrowserOverlay(onDismiss: () -> Unit, onEffectAdded: (String) -> Unit) {
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    
    Surface(
        modifier = Modifier.fillMaxSize().zIndex(200f),
        color = Color(0xFF1C1B1F)
    ) {
        Column {
            Row(modifier = Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { if (selectedCategory == null) onDismiss() else selectedCategory = null }) { 
                    Icon(Icons.Default.ArrowBack, null, tint = Color.White) 
                }
                Text(if (selectedCategory == null) "ADD EFFECT" else selectedCategory!!, fontWeight = FontWeight.Black, color = Color.White, letterSpacing = 2.sp)
            }
            
            val localContext = LocalContext.current
            if (selectedCategory == null) {
                // ... (categories list)
                val categories = listOf(
                    "Color & Light" to Color(0xFF4CAF50),
                    "Blur" to Color(0xFF2196F3),
                    "Drawing & Edge" to Color(0xFF00BCD4),
                    "Distortion/Warp" to Color(0xFFF44336),
                    "Procedural" to Color(0xFFFF9800),
                    "3D" to Color(0xFF9C27B0),
                    "Move/Transform" to Color(0xFFE91E63),
                    "Text" to Color(0xFFFFD600)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(categories) { (name, color) ->
                        Box(
                            modifier = Modifier
                                .height(100.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(color.copy(alpha = 0.1f))
                                .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                .clickable { selectedCategory = name },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(name, color = Color.White, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp))
                        }
                    }
                }
            } else {
                // Effects in Category
                val effects = when (selectedCategory) {
                    "Blur" -> listOf("Gaussian Blur", "Motion Blur", "Lens Blur", "Box Blur")
                    "Distortion/Warp" -> listOf("Oscillate", "Pinch/Bulge", "Tile Map", "Polar Coordinates")
                    "Color & Light" -> listOf("Brightness/Contrast", "Exposure/Gamma", "Color Temperature")
                    else -> listOf("Placeholder Effect A", "Placeholder Effect B")
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(effects) { effect ->
                        Column(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(alpha = 0.05f))
                                .clickable { 
                                    Toast.makeText(localContext, "Added $effect", Toast.LENGTH_SHORT).show()
                                    onEffectAdded(effect)
                                    onDismiss() 
                                }
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .background(Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Image, null, tint = Color.DarkGray)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(effect, color = Color.White, style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProjectTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.titleMedium, color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))
        SettingsRow("Resolution", "1080p (FHD)")
        SettingsRow("Frame Rate", "30 FPS")
        SettingsRow("Duration", "00:00:15")
    }
}

@Composable
fun SettingsRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray)
        Text(value, color = Color(0xFFD0BCFF))
    }
}

@Composable
fun BottomNavBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Layers, contentDescription = null) },
            label = { Text("Layers") },
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Brush, contentDescription = null) },
            label = { Text("Effects") },
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = null) },
            label = { Text("Project") },
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) }
        )
    }
}
