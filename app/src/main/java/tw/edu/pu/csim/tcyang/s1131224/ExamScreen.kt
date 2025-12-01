package tw.edu.pu.csim.tcyang.s1131224

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class ExamScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExamScreenContent()
        }
    }
}

@Composable
fun ExamScreenContent() {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val density = context.resources.displayMetrics.density

    var score by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Yellow),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.happy),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .background(Color.White, shape = CircleShape),
                contentScale = ContentScale.Crop
            )

            Text(text = "系級: 資管二A  姓名: 周子洋", fontSize = 18.sp, color = Color.Black)
            Text(
                text = "螢幕大小: ${screenWidth * density}px × ${screenHeight * density}px",
                fontSize = 16.sp
            )
            Text(text = "成績: $score 分", fontSize = 16.sp)
        }
    }
}