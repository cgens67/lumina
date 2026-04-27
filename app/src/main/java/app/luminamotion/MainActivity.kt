package app.luminamotion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import android.widget.Toast

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
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF1C1B1F))) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Image/Logo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White.copy(alpha = 0.05f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.MotionPhotosAuto, null, modifier = Modifier.size(80.dp), tint = Color(0xFF00FF85))
                    Text("LUMINA MOTION", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.03f), RoundedCornerShape(28.dp))
                    .padding(28.dp)
            ) {
                Text(
                    "\"Where do I start?\"",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    "Tutorial Project • 1.9MB",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "This tutorial walks you through everything. From adding layers to applying advanced keyframe animations and effects. You'll be a pro in no time!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f),
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(32.dp))
                
                Button(
                    onClick = { /* Tutorial */ },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.1f))
                ) {
                    Text("WATCH TUTORIAL", color = Color.White, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { /* Download */ },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.1f))
                ) {
                    Text("DOWNLOAD PROJECT", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Floating Action Button
        LargeFloatingActionButton(
            onClick = onNewProject,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            containerColor = Color(0xFF00FF85),
            shape = CircleShape
        ) {
            Icon(Icons.Default.Add, contentDescription = "New Project", tint = Color.Black, modifier = Modifier.size(32.dp))
        }
    }
}

@Composable
fun ProjectSetupScreen(onBack: () -> Unit, onCreate: (ProjectConfig) -> Unit) {
    var name by remember { mutableStateOf("Untitled Project") }
    var selectedRatio by remember { mutableFloatStateOf(1f) }
    var selectedRes by remember { mutableStateOf("1080p (FHD)") }
    var selectedFPS by remember { mutableIntStateOf(30) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C1B1F))
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.Close, null, tint = Color.White)
            }
            Row {
                Text("PROJECT", fontWeight = FontWeight.Black, color = Color(0xFF00D1FF), style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.width(24.dp))
                Text("ELEMENT", fontWeight = FontWeight.Black, color = Color.Gray, style = MaterialTheme.typography.labelLarge)
            }
            Spacer(modifier = Modifier.width(48.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White, fontWeight = FontWeight.Bold),
            trailingIcon = { Icon(Icons.Default.Create, null, tint = Color.Gray) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White.copy(alpha = 0.2f),
                unfocusedBorderColor = Color.White.copy(alpha = 0.1f)
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Ratio Selection Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val ratios = listOf(16/9f to "16:9", 9/16f to "9:16", 4/5f to "4:5", 1f to "1:1", 4/3f to "4:3")
            ratios.forEach { (valRatio, label) ->
                val isSelected = selectedRatio == valRatio
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) Color(0xFF00D1FF) else Color.White.copy(alpha = 0.05f))
                        .clickable { selectedRatio = valRatio },
                    contentAlignment = Alignment.Center
                ) {
                    Text(label, color = if (isSelected) Color.Black else Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        PropertyDropdown("Resolution", selectedRes)
        PropertyDropdown("Frame Rate", "$selectedFPS fps")
        PropertyDropdown("Background", "Light Grey")

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { onCreate(ProjectConfig(name, selectedRatio, selectedRes, selectedFPS)) },
            modifier = Modifier.fillMaxWidth().height(64.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.05f)),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
        ) {
            Text("CREATE PROJECT", fontWeight = FontWeight.Black, color = Color.White, letterSpacing = 2.sp)
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
    val pinch = activeEffects.contains("Pinch/Bulge")

    val infiniteTransition = rememberInfiniteTransition(label = "oscillate")
    val oscillateY by infiniteTransition.animateFloat(
        initialValue = -15f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "y"
    )
    
    val oscillateRotation by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )
    
    val currentOscillateOffset = if (oscillate) oscillateY else 0f
    val currentRotation = if (oscillate) oscillateRotation else 0f

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
            modifier = Modifier
                .offset(
                    x = (offset.x.dp / 8),
                    y = (offset.y.dp / 8) + currentOscillateOffset.dp
                )
                .graphicsLayer(
                    rotationZ = currentRotation,
                    scaleX = if (pinch) 1.2f else 1f,
                    scaleY = if (pinch) 1.2f else 1f
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.Center) {
                // Secondary shadow for Motion Blur mock
                if (motionBlur) {
                    Surface(
                        modifier = Modifier.size(100.dp).offset(x = 10.dp),
                        color = Color(0xFF00FF85).copy(alpha = 0.2f),
                        shape = RoundedCornerShape(12.dp)
                    ) {}
                }
                
                Surface(
                    modifier = Modifier.size(100.dp),
                    color = Color(0xFF00FF85).copy(alpha = if (blurAmount > 0.dp) 0.6f else 1f),
                    shape = RoundedCornerShape(12.dp),
                    border = if (blurAmount > 0.dp) BorderStroke(blurAmount / 2, Color.White.copy(alpha = 0.3f)) else null
                ) {
                    if (blurAmount > 0.dp) {
                        Box(modifier = Modifier.fillMaxSize().background(Color.White.copy(alpha = 0.1f)))
                    }
                    if (pinch) {
                        // Tiny mock for bulge effect center
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .align(Alignment.Center)
                                .background(Color.White.copy(alpha = 0.4f), CircleShape)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                if (oscillate) "~~~ OSCILLATING ~~~" else "LUMINA MOTION", 
                color = if (motionBlur) Color.White.copy(alpha = 0.4f) else Color.White, 
                fontWeight = FontWeight.Black, 
                style = MaterialTheme.typography.labelSmall,
                letterSpacing = 2.sp
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

data class Layer(
    val id: String,
    val name: String,
    val type: LayerType,
    val color: Color,
    val effects: Set<String> = emptySet()
)

enum class LayerType {
    Shape, Media, Text, Audio
}

@Composable
fun MainEditorScreen(config: ProjectConfig, onExit: () -> Unit) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }
    var currentTime by remember { mutableLongStateOf(0L) }
    var showAddMenu by remember { mutableStateOf(false) }
    var showEffectBrowser by remember { mutableStateOf(false) }
    var selectedLayerId by remember { mutableStateOf<String?>(null) }
    
    var layers by remember { mutableStateOf(listOf(
        Layer("1", "Background", LayerType.Shape, Color(0xFF444444)),
        Layer("2", "Tutorial Text", LayerType.Text, Color(0xFFD0BCFF))
    )) }

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
                    Text(config.name, style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                }
                Row {
                    IconButton(onClick = { Toast.makeText(context, "Project Settings", Toast.LENGTH_SHORT).show() }) {
                        Icon(Icons.Default.Settings, null, tint = Color.LightGray)
                    }
                    TextButton(
                        onClick = { Toast.makeText(context, "Exporting 1080p MP4...", Toast.LENGTH_SHORT).show() },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF00D1FF))
                    ) {
                        Icon(Icons.Default.FileUpload, null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("EXPORT", fontWeight = FontWeight.Black)
                    }
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
                // Determine active effects from selected layer
                val activeEffects = layers.find { it.id == selectedLayerId }?.effects ?: emptySet()
                PreviewArea(activeEffects)
            }

            // Playback and Editing Controls
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF121212))
            ) {
                // Secondary Controls Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row {
                        IconButton(onClick = { /* Undo */ }) { Icon(Icons.Default.Undo, null, tint = Color.LightGray, modifier = Modifier.size(20.dp)) }
                        IconButton(onClick = { /* Redo */ }) { Icon(Icons.Default.Redo, null, tint = Color.LightGray, modifier = Modifier.size(20.dp)) }
                    }
                    Text("0:00:00.0", color = Color.White, style = MaterialTheme.typography.labelMedium)
                    Row {
                        IconButton(onClick = { /* Grid */ }) { Icon(Icons.Default.Grid4x4, null, tint = Color.LightGray, modifier = Modifier.size(20.dp)) }
                        IconButton(onClick = { /* Zoom */ }) { Icon(Icons.Default.ZoomIn, null, tint = Color.LightGray, modifier = Modifier.size(20.dp)) }
                    }
                }

                // Playback controls
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { currentTime = 0 }) { Icon(Icons.Default.SkipPrevious, null, tint = Color.White) }
                    IconButton(onClick = { /* Step Back */ }) { Icon(Icons.Default.ChevronLeft, null, tint = Color.White) }
                    
                    Surface(
                        onClick = { isPlaying = !isPlaying },
                        modifier = Modifier.size(48.dp),
                        shape = CircleShape,
                        color = Color.White
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, null, tint = Color.Black)
                        }
                    }
                    
                    IconButton(onClick = { /* Step Forward */ }) { Icon(Icons.Default.ChevronRight, null, tint = Color.White) }
                    IconButton(onClick = { /* Skip End */ }) { Icon(Icons.Default.SkipNext, null, tint = Color.White) }
                }

                // Timeline Layers
                Column(
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                        .padding(bottom = 56.dp) // Space for bottom nav
                ) {
                    layers.reversed().forEach { layer ->
                        LayerRow(
                            layer = layer,
                            isSelected = selectedLayerId == layer.id,
                            onClick = { selectedLayerId = layer.id }
                        )
                    }
                }

                // Property Tray (Only visible when a layer is selected)
                AnimatedVisibility(
                    visible = selectedLayerId != null,
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                ) {
                    val currentLayer = layers.find { it.id == selectedLayerId }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF242426))
                            .padding(8.dp)
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier.height(140.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            item { PropertyButton(Icons.Default.Palette, "Color & Fill") }
                            item { PropertyButton(Icons.Default.FilterFrames, "Border & Shadow") }
                            item { PropertyButton(Icons.Default.Opacity, "Blending") }
                            item { PropertyButton(Icons.Default.Transform, "Move & Transform") }
                            item { 
                                PropertyButton(
                                    Icons.Default.AutoAwesome, 
                                    "Effects", 
                                    Color(0xFF00FF85),
                                    onClick = { showEffectBrowser = true }
                                ) 
                            }
                            item { PropertyButton(Icons.Default.Edit, "Edit ${currentLayer?.type ?: "Layer"}") }
                        }
                    }
                }
            }
        }

        // Bottom Navigation Overlay
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            BottomNavBar(selectedTab) { 
                selectedTab = it 
            }
        }

        // Floating Add Button
        LargeFloatingActionButton(
            onClick = { showAddMenu = true },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp),
            containerColor = Color(0xFF00FF85),
            shape = CircleShape
        ) {
            Icon(Icons.Default.Add, null, tint = Color.Black)
        }

        // Add Menu
        if (showAddMenu) {
            AddMenuOverlay(
                onDismiss = { showAddMenu = false },
                onAdd = { type, name, color ->
                    layers = layers + Layer(layers.size.toString(), name, type, color)
                    showAddMenu = false
                }
            )
        }

        // Effect Browser
        if (showEffectBrowser) {
            EffectBrowserOverlay(
                onDismiss = { showEffectBrowser = false; selectedTab = 0 },
                onEffectAdded = { effectName ->
                    layers = layers.map { 
                        if (it.id == selectedLayerId) it.copy(effects = it.effects + effectName) else it
                    }
                }
            )
        }
    }
}

@Composable
fun LayerRow(layer: Layer, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(if (isSelected) Color.White.copy(alpha = 0.1f) else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            when(layer.type) {
                LayerType.Shape -> Icons.Default.Category
                LayerType.Media -> Icons.Default.Image
                LayerType.Text -> Icons.Default.Title
                LayerType.Audio -> Icons.Default.MusicNote
            },
            null,
            tint = if (isSelected) Color.White else Color.Gray,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            layer.name,
            color = if (isSelected) Color.White else Color.Gray,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.width(100.dp)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(28.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(layer.color.copy(alpha = 0.3f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(4.dp)
                    .background(layer.color)
            )
        }
        if (layer.effects.isNotEmpty()) {
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.Default.AutoAwesome, null, tint = Color(0xFF00FF85), modifier = Modifier.size(12.dp))
        }
    }
}

@Composable
fun AddMenuOverlay(onDismiss: () -> Unit, onAdd: (LayerType, String, Color) -> Unit) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f))
            .clickable { onDismiss() }
            .zIndex(100f),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF242426), RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .padding(24.dp)
                .clickable(enabled = false) {}
        ) {
            Text("Add Layer", fontWeight = FontWeight.Black, color = Color.White, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                AddMenuItem(Icons.Default.Category, "Shape", Color(0xFF00FF85)) {
                    onAdd(LayerType.Shape, "Rectangle 1", Color(0xFF00FF85))
                }
                AddMenuItem(Icons.Default.Image, "Media", Color(0xFF00D1FF)) {
                    onAdd(LayerType.Media, "Video 01", Color(0xFF00D1FF))
                }
                AddMenuItem(Icons.Default.Audiotrack, "Audio", Color(0xFFD0BCFF)) {
                    onAdd(LayerType.Audio, "Audio Track", Color(0xFFD0BCFF))
                }
                AddMenuItem(Icons.Default.Title, "Text", Color(0xFFFFD600)) {
                    onAdd(LayerType.Text, "New Text", Color(0xFFFFD600))
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
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
                val categories = listOf(
                    "Color & Light" to Color(0xFF4CAF50),
                    "Blur" to Color(0xFF2196F3),
                    "Drawing & Edge" to Color(0xFF00BCD4),
                    "Distortion/Warp" to Color(0xFFF44336),
                    "Procedural" to Color(0xFFFF9800),
                    "3D" to Color(0xFF9C27B0),
                    "Move/Transform" to Color(0xFFE91E63),
                    "Text" to Color(0xFFFFD600),
                    "Correction" to Color(0xFF607D8B),
                    "Opacity" to Color(0xFF795548)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(categories) { item ->
                        val (name, color) = item
                        Box(
                            modifier = Modifier
                                .height(80.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(color.copy(alpha = 0.08f))
                                .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                .clickable { selectedCategory = name },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                name, 
                                color = Color.White, 
                                fontWeight = FontWeight.Bold, 
                                style = MaterialTheme.typography.labelSmall,
                                textAlign = TextAlign.Center, 
                                modifier = Modifier.padding(8.dp)
                            )
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

@Composable
fun PropertyButton(icon: ImageVector, label: String, tint: Color = Color.White, onClick: () -> Unit = {}) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable { onClick() },
        color = Color.White.copy(alpha = 0.05f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(4.dp)
        ) {
            Icon(icon, null, tint = tint, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray, maxLines = 1)
        }
    }
}
