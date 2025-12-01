import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import kotlinx.coroutines.delay
import tw.edu.pu.csim.tcyang.s1131224.R

@Composable
fun RoleIconsScreenWithScore() {
    val configuration = LocalConfiguration.current
    val screenHeightPx = with(LocalDensity.current) { configuration.screenHeightDp.dp.toPx() }
    val screenWidthPx = with(LocalDensity.current) { configuration.screenWidthDp.dp.toPx() }

    val serviceIcons = listOf(
        R.drawable.service0, // 極早期療育 -> 嬰幼兒
        R.drawable.service1, // 離島服務 -> 兒童
        R.drawable.service2, // 極重多障 -> 成人
        R.drawable.service3  // 輔具服務 -> 一般民眾
    )

    val serviceAnswer = mapOf(
        R.drawable.service0 to "嬰幼兒",
        R.drawable.service1 to "兒童",
        R.drawable.service2 to "成人",
        R.drawable.service3 to "一般民眾"
    )

    var serviceIconRes by remember { mutableStateOf(serviceIcons.random()) }
    var yOffset by remember { mutableStateOf(0f) }
    var xOffset by remember { mutableStateOf(screenWidthPx / 2 - 50f) }

    var rolePositions by remember { mutableStateOf(mapOf<String, Offset>()) }
    val roleSize = 300.dp

    var collisionMessage by remember { mutableStateOf("") }
    var score by remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {

        // 顯示分數與訊息
        Column(modifier = Modifier.align(Alignment.TopCenter).padding(16.dp)) {
            Text(text = "分數: $score", color = Color.Blue)
            Text(text = collisionMessage, color = Color.Red)
        }

        // 角色圖示
        Image(
            painter = painterResource(id = R.drawable.role0),
            contentDescription = "嬰幼兒",
            modifier = Modifier
                .size(roleSize)
                .align(Alignment.TopStart)
                .onGloballyPositioned { coords ->
                    rolePositions = rolePositions + ("嬰幼兒" to coords.boundsInParent().topLeft)
                }
                .offset(y = configuration.screenHeightDp.dp / 2 - 150.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.role1),
            contentDescription = "兒童",
            modifier = Modifier
                .size(roleSize)
                .align(Alignment.TopEnd)
                .onGloballyPositioned { coords ->
                    rolePositions = rolePositions + ("兒童" to coords.boundsInParent().topLeft)
                }
                .offset(y = configuration.screenHeightDp.dp / 2 - 150.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.role2),
            contentDescription = "成人",
            modifier = Modifier
                .size(roleSize)
                .align(Alignment.BottomStart)
                .onGloballyPositioned { coords ->
                    rolePositions = rolePositions + ("成人" to coords.boundsInParent().topLeft)
                }
        )
        Image(
            painter = painterResource(id = R.drawable.role3),
            contentDescription = "一般民眾",
            modifier = Modifier
                .size(roleSize)
                .align(Alignment.BottomEnd)
                .onGloballyPositioned { coords ->
                    rolePositions = rolePositions + ("一般民眾" to coords.boundsInParent().topLeft)
                }
        )

        // 掉落的服務圖示
        Image(
            painter = painterResource(id = serviceIconRes),
            contentDescription = "服務圖示",
            modifier = Modifier
                .size(100.dp)
                .offset { IntOffset(xOffset.toInt(), yOffset.toInt()) }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        xOffset = (xOffset + dragAmount.x).coerceIn(0f, screenWidthPx - 100f)
                    }
                }
        )
    }

    val density = LocalDensity.current
    val roleSizePx = with(density) { 300.dp.toPx() }
    val serviceSizePx = with(density) { 100.dp.toPx() }

    LaunchedEffect(serviceIconRes, xOffset, yOffset) {
        while (true) {
            delay(100)
            yOffset += 20f
            var collided = false

            rolePositions.forEach { (roleName, pos) ->
                if (xOffset + serviceSizePx > pos.x && xOffset < pos.x + roleSizePx &&
                    yOffset + serviceSizePx > pos.y && yOffset < pos.y + roleSizePx
                ) {
                    collided = true
                    // 判斷是否正確角色
                    if (serviceAnswer[serviceIconRes] == roleName) {
                        score += 1
                        collisionMessage = "正確碰撞 $roleName +1分"
                    } else {
                        score -= 1
                        collisionMessage = "錯誤碰撞 $roleName -1分"
                    }
                    // 重置服務圖示
                    yOffset = 0f
                    xOffset = screenWidthPx / 2 - serviceSizePx / 2
                    serviceIconRes = serviceIcons.random()
                }
            }

            if (!collided && yOffset > screenHeightPx - serviceSizePx) {
                score -= 1
                collisionMessage = "掉到最下方 -1分"
                yOffset = 0f
                xOffset = screenWidthPx / 2 - serviceSizePx / 2
                serviceIconRes = serviceIcons.random()
            }
        }
    }
}