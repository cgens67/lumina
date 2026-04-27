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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LuminaTheme {
                MainEditorScreen()
            }
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
fun MainEditorScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        TopBar()
        Box(modifier = Modifier.weight(1f)) {
            PreviewArea()
        }
        TimelineArea()
        BottomNavBar()
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            Spacer(modifier = Modifier.width(16.dp))
            Text("Untitled Project", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text("Export", color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }
}

@Composable
fun PreviewArea() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(200.dp)) {
            drawCircle(color = Color(0xFFD0BCFF).copy(alpha = 0.2f), radius = size.minDimension / 1.5f)
            drawCircle(color = Color(0xFFD0BCFF).copy(alpha = 0.1f), radius = size.minDimension / 2f)
        }
        Text("PREVIEW", color = Color(0xFFD0BCFF).copy(alpha = 0.5f), fontWeight = FontWeight.Bold)
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
fun BottomNavBar() {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Layers, contentDescription = null) },
            label = { Text("Layers") },
            selected = true,
            onClick = {}
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Brush, contentDescription = null) },
            label = { Text("Effects") },
            selected = false,
            onClick = {}
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = null) },
            label = { Text("Project") },
            selected = false,
            onClick = {}
        )
    }
}
