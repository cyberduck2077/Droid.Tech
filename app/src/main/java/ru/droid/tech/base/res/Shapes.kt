package ru.droid.tech.base.res

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.droid.tech.base.theme.ShapeSchemeApp

private val smallShapesDp = 8.dp
private val mediumShapeDp: Dp = 16.dp
val ShapesApp = ShapeSchemeApp(
    smallTop = RoundedCornerShape(
        topStart = smallShapesDp,
        topEnd = smallShapesDp),

    smallAll = RoundedCornerShape(smallShapesDp),

    smallRight = RoundedCornerShape(
        topEnd = smallShapesDp,
        bottomEnd = smallShapesDp
    ),

    mediumTop = RoundedCornerShape(
        topStart = mediumShapeDp,
        topEnd = mediumShapeDp
    ),

    mediumBottom = RoundedCornerShape(
        bottomStart = mediumShapeDp,
        bottomEnd = mediumShapeDp
    ),

    mediumAll = RoundedCornerShape(mediumShapeDp),

    mediumLeft = RoundedCornerShape(
        topStart = mediumShapeDp,
        bottomStart = mediumShapeDp
    ),

    mediumRight = RoundedCornerShape(
        topEnd = mediumShapeDp,
        bottomEnd = mediumShapeDp
    ))