package com.androidisland.todocompose.feature.splash.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.androidisland.todocompose.ext.usp


@Composable
fun SplashLogo(
    logoSize: Dp,
    offsetY: Dp,
    alpha: Float
) {
    val backgroundColor = MaterialTheme.colorScheme.onPrimary
    val textColor = MaterialTheme.colorScheme.primary

    val textMeasurer = rememberTextMeasurer()
    val textToDraw = "TO\nDO"
    val textStyle = TextStyle(
        fontSize = (logoSize.value / 4).usp,
        fontWeight = FontWeight.W900,
        fontFamily = FontFamily.Cursive,
        color = textColor,
    )
    val textLayoutResult = remember(textToDraw) {
        textMeasurer.measure(
            textToDraw,
            textStyle,
        )
    }

    Canvas(
        modifier = Modifier
            .width(logoSize)
            .aspectRatio(1f)
            .offset(0.dp, offsetY)
            .alpha(alpha)
    ) {
        val triangleHeight = size.height * 1f / 3f
        val squareSize = size.height * 2f / 3f
        val paddingHorizontal = (size.width - squareSize) / 2f
        val path = Path().apply {
            moveTo(paddingHorizontal, 0f)
            relativeLineTo(squareSize, 0f)
            relativeLineTo(0f, squareSize)
            relativeLineTo(-squareSize / 2f, triangleHeight)
            relativeLineTo(-squareSize / 2f, -triangleHeight)
            close()
        }

        drawIntoCanvas { canvas ->
            canvas.drawOutline(
                outline = Outline.Generic(path),
                paint = Paint().apply {
                    color = backgroundColor
                    pathEffect = PathEffect.cornerPathEffect(size.maxDimension / 10f)
                }
            )
        }

        val textSize = textLayoutResult.size
        drawText(
            textLayoutResult = textLayoutResult,
            topLeft = Offset(
                x = size.width / 2f - textSize.width / 2f,
                y = squareSize / 1.8f - textSize.height / 2f
            ),
        )
    }
}

@Preview
@Composable
fun SplashLogoPreview() {
    SplashLogo(logoSize = 120.dp, 0.dp, 1f)
}