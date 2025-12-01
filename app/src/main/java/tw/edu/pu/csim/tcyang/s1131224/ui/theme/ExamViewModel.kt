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
fun RoleIconsScreenWithCollision() {
    val configuration = LocalConfiguration.current
    val screenHeightPx = with(LocalDensity.current) { configuration.screenHeightDp.dp.toPx() }
    val screenWidthPx = with(LocalDensity.current) { configuration.screenWidthDp.dp.toPx() }

    // 服務圖示列表
    val serviceIcons = listOf(
        R.drawable.service0,
        R.drawable.service1,
        R.drawable.service2,
        R.drawable.service3
    )

    var serviceIconRes by remember { mutableStateOf(serviceIcons.random()) }
    var yOffset by remember { mutableStateOf(0f) }
    var xOffset by remember { mutableStateOf(screenWidthPx / 2 - 50f) } // 水平中間，假設圖示寬度100px

    // 角色圖示位置與大小
    var rolePositions by remember { mutableStateOf(mapOf<String, Offset>()) }
    val roleSize = 300.dp

    // 碰撞訊息
    var collisionMessage by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {

        // 顯示碰撞訊息
        Text(
            text = collisionMessage,
            color = Color.Red,
            modifier = Modifier.align(Alignment.TopCenter).padding(16.dp)
        )

        // 四個固定角色圖示
        Image(
            painter = painterResource(id = R.drawable.role0),
            contentDescription = "嬰幼兒",
            modifier = Modifier
                .size(roleSize)
                .align(Alignment.TopStart)
                .onGloballyPositioned { layoutCoordinates ->
                    rolePositions =
                        rolePositions + ("嬰幼兒" to layoutCoordinates.boundsInParent().topLeft)
                }
                .offset(y = configuration.screenHeightDp.dp / 2 - 150.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.role1),
            contentDescription = "兒童",
            modifier = Modifier
                .size(roleSize)
                .align(Alignment.TopEnd)
                .onGloballyPositioned { layoutCoordinates ->
                    rolePositions =
                        rolePositions + ("兒童" to layoutCoordinates.boundsInParent().topLeft)
                }
                .offset(y = configuration.screenHeightDp.dp / 2 - 150.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.role2),
            contentDescription = "成人",
            modifier = Modifier
                .size(roleSize)
                .align(Alignment.BottomStart)
                .onGloballyPositioned { layoutCoordinates ->
                    rolePositions =
                        rolePositions + ("成人" to layoutCoordinates.boundsInParent().topLeft)
                }
        )

        Image(
            painter = painterResource(id = R.drawable.role3),
            contentDescription = "一般民眾",
            modifier = Modifier
                .size(roleSize)
                .align(Alignment.BottomEnd)
                .onGloballyPositioned { layoutCoordinates ->
                    rolePositions =
                        rolePositions + ("一般民眾" to layoutCoordinates.boundsInParent().topLeft)
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
                        val newX = xOffset + dragAmount.x
                        xOffset = newX.coerceIn(0f, screenWidthPx - 100f)
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
            rolePositions.forEach { (roleName, pos) ->
                if (xOffset + serviceSizePx > pos.x && xOffset < pos.x + roleSizePx &&
                    yOffset + serviceSizePx > pos.y && yOffset < pos.y + roleSizePx
                ) {
                    collisionMessage = "碰撞 $roleName"
                }


                // 碰到底部
                if (yOffset > screenHeightPx - serviceSizePx) {
                    collisionMessage = "掉到最下方"
                    yOffset = 0f
                    xOffset = screenWidthPx / 2 - serviceSizePx / 2
                    serviceIconRes = serviceIcons.random()
                }
            }
        }
    }
}