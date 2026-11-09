package com.apex.editor

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.VideoView
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView

private val ApexBlack = Color(0xFF090B0D)
private val ApexPanel = Color(0xFF15191D)
private val ApexGreen = Color(0xFFB6FF35)
private val ApexMuted = Color(0xFF9AA4AD)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ApexApp() }
    }
}

@Composable
fun ApexApp() {
    val context = LocalContext.current
    var videoUri by remember { mutableStateOf<Uri?>(null) }
    var videoName by remember { mutableStateOf("No footage imported") }
    var selectedStyle by remember { mutableStateOf("NFS HEAT") }
    var vertical by remember { mutableStateOf(true) }
    var beatSync by remember { mutableStateOf(true) }
    var slowMotion by remember { mutableStateOf(true) }
    var highlightMode by remember { mutableStateOf("Overtakes + battles") }
    var status by remember { mutableStateOf("Ready for your footage") }

    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: Exception) { }
            videoUri = uri
            videoName = uri.lastPathSegment?.substringAfterLast('/') ?: "Video imported"
            status = "Footage loaded — choose your edit settings"
        }
    }

    MaterialTheme(
        colorScheme = darkColorScheme(
            background = ApexBlack,
            surface = ApexPanel,
            primary = ApexGreen,
            onPrimary = ApexBlack,
            onBackground = Color.White,
            onSurface = Color.White
        )
    ) {
        Surface(Modifier.fillMaxSize(), color = ApexBlack) {
            Column(
                Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()
                    .padding(horizontal = 18.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth().padding(top = 14.dp, bottom = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier.size(42.dp).background(ApexGreen, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("A", color = ApexBlack, fontSize = 27.sp, fontWeight = FontWeight.Black)
                    }
                    Spacer(Modifier.width(11.dp))
                    Column(Modifier.weight(1f)) {
                        Text("APEX EDITOR", fontSize = 20.sp, fontWeight = FontWeight.Black, letterSpacing = 1.5.sp)
                        Text("KARTING VIDEO STUDIO  •  BETA", color = ApexMuted, fontSize = 9.sp, letterSpacing = 1.6.sp)
                    }
                    Text("0.1", color = ApexGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Text("YOUR NEXT RACE.", fontSize = 25.sp, fontWeight = FontWeight.Black)
                Text("Turn raw laps into highlight reels.", color = ApexMuted, fontSize = 13.sp)
                Spacer(Modifier.height(15.dp))

                // Footage import / preview
                Box(
                    Modifier.fillMaxWidth().height(205.dp)
                        .background(Color(0xFF20272C), RoundedCornerShape(18.dp))
                        .border(1.dp, Color(0xFF303940), RoundedCornerShape(18.dp))
                ) {
                    if (videoUri != null) {
                        AndroidView(
                            factory = { ctx ->
                                VideoView(ctx).apply {
                                    setVideoURI(videoUri)
                                    setOnPreparedListener { mp -> mp.isLooping = true; start() }
                                }
                            },
                            update = { it.setVideoURI(videoUri) },
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Column(
                            Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.Movie, null, tint = ApexGreen, modifier = Modifier.size(38.dp))
                            Spacer(Modifier.height(8.dp))
                            Text("YOUR FOOTAGE GOES HERE", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("Import a GoPro clip to get started", color = ApexMuted, fontSize = 11.sp)
                        }
                    }
                    Surface(
                        Modifier.align(Alignment.TopStart).padding(10.dp),
                        color = Color(0xCC090B0D), shape = RoundedCornerShape(6.dp)
                    ) {
                        Text("PREVIEW", Modifier.padding(horizontal = 8.dp, vertical = 5.dp), fontSize = 9.sp, color = ApexGreen, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(videoName, Modifier.weight(1f), color = ApexMuted, fontSize = 11.sp, maxLines = 1)
                    TextButton(onClick = { picker.launch(arrayOf("video/*")) }) {
                        Icon(Icons.Default.Add, null, tint = ApexGreen)
                        Text("IMPORT", color = ApexGreen, fontWeight = FontWeight.Bold)
                    }
                }

                SectionTitle("EDIT DNA", "Pick the look")
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(listOf("NFS HEAT", "F1 CINEMATIC", "RAW RACE", "HYPE CUT")) { style ->
                        val active = style == selectedStyle
                        Box(
                            Modifier.background(if (active) ApexGreen else ApexPanel, RoundedCornerShape(10.dp))
                                .border(1.dp, if (active) ApexGreen else Color(0xFF303940), RoundedCornerShape(10.dp))
                                .clickable { selectedStyle = style }
                                .padding(horizontal = 13.dp, vertical = 11.dp)
                        ) {
                            Text(style, color = if (active) ApexBlack else Color.White, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }

                Spacer(Modifier.height(15.dp))
                SectionTitle("AI EDIT SETTINGS", "Your preferences")
                SettingRow(Icons.Default.AutoAwesome, "Find the best moments", highlightMode) {
                    highlightMode = if (highlightMode == "Overtakes + battles") "Corners + near misses" else "Overtakes + battles"
                }
                SettingRow(Icons.Default.MusicNote, "Beat-sync music", if (beatSync) "ON" else "OFF") { beatSync = !beatSync }
                SettingRow(Icons.Default.Speed, "Slow-motion moments", if (slowMotion) "ON" else "OFF") { slowMotion = !slowMotion }
                SettingRow(Icons.Default.PlayArrow, "Export format", if (vertical) "9:16 VERTICAL" else "16:9 LANDSCAPE") { vertical = !vertical }

                Spacer(Modifier.weight(1f))
                Button(
                    onClick = {
                        status = if (videoUri == null) {
                            "Import a video first"
                        } else {
                            "Settings saved. AI analysis + rendering engine is the next build step."
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ApexGreen, contentColor = ApexBlack)
                ) {
                    Icon(Icons.Default.Bolt, null)
                    Spacer(Modifier.width(8.dp))
                    Text("BUILD MY EDIT", fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                }
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, null, tint = ApexGreen, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(status, color = ApexMuted, fontSize = 10.sp)
                }
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String, subtitle: String) {
    Row(Modifier.fillMaxWidth().padding(bottom = 9.dp), verticalAlignment = Alignment.Bottom) {
        Text(title, fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
        Spacer(Modifier.weight(1f))
        Text(subtitle, fontSize = 10.sp, color = ApexMuted)
    }
}

@Composable
private fun SettingRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 5.dp)
            .background(ApexPanel, RoundedCornerShape(11.dp))
            .clickable(onClick = onClick).padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = ApexGreen, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(10.dp))
        Text(title, Modifier.weight(1f), fontSize = 12.sp, fontWeight = FontWeight.Medium)
        Text(value, color = ApexGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}
