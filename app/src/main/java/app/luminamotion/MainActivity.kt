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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.ui.text.style.TextAlign
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.draw.clip

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

enum class LuminaScreen {
    Welcome, Terms, Editor
}

@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf(LuminaScreen.Welcome) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        when (currentScreen) {
            LuminaScreen.Welcome -> WelcomeScreen(
                onGetStarted = { currentScreen = LuminaScreen.Terms }
            )
            LuminaScreen.Terms -> TermsOfServiceScreen(
                onAccept = { currentScreen = LuminaScreen.Editor },
                onBack = { currentScreen = LuminaScreen.Welcome }
            )
            LuminaScreen.Editor -> MainEditorScreen(
                onExit = { currentScreen = LuminaScreen.Welcome }
            )
        }
    }
}

@Composable
fun WelcomeScreen(onGetStarted: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.MotionPhotosAuto,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "Welcome to Lumina",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Professional motion design in your pocket.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = onGetStarted,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Get Started")
        }
    }
}

@Composable
fun TermsOfServiceScreen(onAccept: () -> Unit, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
        }
        Text(
            "Terms of Service",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Text(
                "By using Lumina, you agree to:\n\n" +
                "1. Respect project ownership.\n" +
                "2. Not use for illegal content.\n" +
                "3. Allow periodic updates for features.\n\n" +
                "Lumina is currently in beta. Motion assets generated are stored locally unless synced.",
                color = Color.LightGray,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onAccept,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("I Accept & Continue")
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
fun MainEditorScreen(onExit: () -> Unit) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        TopBar(
            onBack = onExit,
            onExport = {
                Toast.makeText(context, "Exporting project...", Toast.LENGTH_SHORT).show()
            }
        )
        Box(modifier = Modifier.weight(1f)) {
            PreviewArea()
        }
        
        // Dynamic bottom area based on tab
        Box(modifier = Modifier.height(300.dp)) {
            when (selectedTab) {
                0 -> TimelineArea()
                1 -> EffectsTab()
                2 -> ProjectTab()
            }
        }
        
        BottomNavBar(selectedTab) { selectedTab = it }
    }
}

@Composable
fun EffectsTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Text("No effects added", color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {}) {
            Text("Browse Effects")
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
        SettingsRow("Resolution", "1080x1920")
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
        Text(value, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun TopBar(onBack: () -> Unit, onExport: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text("Lumina Project", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        Button(
            onClick = onExport,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text("Export", color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }
}

@Composable
fun PreviewArea() {
    var offset by remember { mutableStateOf(Offset.Zero) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Black)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offset += dragAmount
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(200.dp).offset(offset.x.dp / 5, offset.y.dp / 5)) {
            drawCircle(color = Color(0xFFD0BCFF).copy(alpha = 0.2f), radius = size.minDimension / 1.5f)
            drawCircle(color = Color(0xFFD0BCFF).copy(alpha = 0.1f), radius = size.minDimension / 2f)
        }
        Text("PREVIEW", color = Color(0xFFD0BCFF).copy(alpha = 0.5f), fontWeight = FontWeight.Bold, modifier = Modifier.offset(offset.x.dp / 10, offset.y.dp / 10))
    }
}

@Composable
fun TimelineArea() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 8.dp)
    ) {
        // Time Ruler
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .background(Color.Black.copy(alpha = 0.2f)),
            verticalAlignment = Alignment.CenterVertically
        ) {
             Text("00:00:00", modifier = Modifier.padding(start = 16.dp), style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Layers
        Column(modifier = Modifier.weight(1f)) {
            TimelineLayer("Overlay_01", Color.Blue)
            TimelineLayer("Text_Main", Color(0xFFD0BCFF))
            TimelineLayer("Background", Color.Gray)
        }
    }
}

@Composable
fun TimelineLayer(name: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.width(100.dp)) {
            Text(name, style = MaterialTheme.typography.labelMedium, maxLines = 1)
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .height(28.dp)
                .background(color.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(modifier = Modifier.size(8.dp).background(color, RoundedCornerShape(2.dp)))
        }
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
