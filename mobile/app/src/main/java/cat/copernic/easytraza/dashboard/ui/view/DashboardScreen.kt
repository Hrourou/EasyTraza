package cat.copernic.easytraza.dashboard.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cat.copernic.easytraza.R
import cat.copernic.easytraza.ui.theme.*

@Composable
fun DashboardScreen(nomUsuari: String, onNavigate: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(clrSurface)
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .background(clrSurfaceContainerHighest)
                    .clickable { onNavigate("IP") }
                    .align(Alignment.CenterStart),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = stringResource(id = R.string.settings),
                    tint = clrPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }

            androidx.compose.foundation.Image(
                painter = painterResource(id = R.drawable.logo_easytraza),
                contentDescription = stringResource(id = R.string.logo_desc),
                modifier = Modifier.height(100.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.dashboard_hello),
            fontSize = 44.sp,
            color = clrPrimary,
            letterSpacing = (-1.5).sp
        )
        Text(
            nomUsuari,
            fontSize = 38.sp,
            fontWeight = FontWeight.Black,
            color = clrPrimary,
            lineHeight = 40.sp,
            letterSpacing = (-1).sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                DashboardButton(
                    title = stringResource(id = R.string.dashboard_btn_receive),
                    icon = Icons.AutoMirrored.Filled.List,
                    backgroundIcon = Icons.AutoMirrored.Filled.List,
                    isPrimary = true,
                    modifier = Modifier.weight(1f)
                ) { onNavigate("RECEPCIONAR") }

                DashboardButton(
                    title = stringResource(id = R.string.dashboard_btn_start_lot),
                    icon = Icons.Default.PlayArrow,
                    backgroundIcon = Icons.Default.PlayArrow,
                    isPrimary = false,
                    modifier = Modifier.weight(1f)
                ) { onNavigate("LLISTA_INICIAR") }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                DashboardButton(
                    title = stringResource(id = R.string.dashboard_btn_end_lot),
                    icon = Icons.Default.CheckCircle,
                    backgroundIcon = Icons.Default.CheckCircle,
                    isPrimary = false,
                    modifier = Modifier.weight(1f)
                ) { onNavigate("FINALITZAR") }

                DashboardButton(
                    title = stringResource(id = R.string.dashboard_btn_logout),
                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                    backgroundIcon = Icons.AutoMirrored.Filled.ExitToApp,
                    isPrimary = false,
                    modifier = Modifier.weight(1f)
                ) { onNavigate("CERRAR_SESION") }
            }
        }
        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun DashboardButton(
    title: String,
    icon: ImageVector,
    backgroundIcon: ImageVector,
    isPrimary: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    val bgModifier = if (isPrimary) {
        Modifier.background(
            brush = Brush.linearGradient(
                colors = listOf(clrPrimary, clrPrimaryContainer)
            )
        )
    } else {
        Modifier.background(clrSurfaceContainerHighest)
    }
    val textColor = if (isPrimary) Color.White else clrPrimary
    val iconAlpha = if (isPrimary) 0.20f else 0.08f

    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(28.dp))
            .then(bgModifier)
            .clickable { onClick() }
            .padding(20.dp)
    ) {
        Icon(
            backgroundIcon,
            contentDescription = null,
            tint = if (isPrimary) Color.White.copy(alpha = iconAlpha) else clrPrimary.copy(alpha = iconAlpha),
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.TopEnd)
        )
        Column(modifier = Modifier.align(Alignment.BottomStart)) {
            Icon(
                icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier
                    .size(32.dp)
                    .padding(bottom = 6.dp)
            )
            Text(
                title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                lineHeight = 26.sp,
                letterSpacing = (-0.5).sp
            )
        }
    }
}
