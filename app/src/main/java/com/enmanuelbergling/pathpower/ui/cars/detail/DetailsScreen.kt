package com.enmanuelbergling.pathpower.ui.cars.detail

import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import coil.compose.AsyncImage
import com.enmanuelbergling.path_power.ui.list.BeehiveGridCells
import com.enmanuelbergling.path_power.ui.list.LazyBeehiveVerticalGrid
import com.enmanuelbergling.path_power.ui.shape.Hexagon
import com.enmanuelbergling.pathpower.ui.cars.LocalSharedTransitionScope
import com.enmanuelbergling.pathpower.ui.cars.model.CarModel
import com.enmanuelbergling.pathpower.ui.cars.model.HexagonField
import kotlinx.serialization.Serializable

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun AnimatedContentScope.DetailsScreen(
    carModel: CarModel,
    onBack: () -> Unit,
) {
    val fields = remember {
        mutableStateListOf<HexagonField>()
    }

    LaunchedEffect(key1 = Unit) {
        val labeledFields = carModel.getLabeledFields()
        fields.addAll(labeledFields)

        fields.add(fields.lastIndex - 1, HexagonField.ColorField(Color(carModel.color)))

        val carField = HexagonField.Car(carModel.imageResource, carModel.name)

        fields.add(3, carField)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = carModel.name) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    Color.Transparent
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "arrow back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyBeehiveVerticalGrid(
            items = fields,
            gridCells = BeehiveGridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalAlignment = Alignment.CenterVertically,
            centerHorizontal = true,
            key = { rowIndex, hexagonFields -> rowIndex to hexagonFields }
        ) { field ->
            when (field) {
                is HexagonField.Car -> {
                    val sharedTransitionScope = LocalSharedTransitionScope.current!!
                    HexCarUi(
                        image = field.image,
                        name = field.name,
                        modifier = Modifier
                                then with(sharedTransitionScope) {
                            Modifier.sharedElement(
                                rememberSharedContentState(key = carModel.imageResource),
                                this@DetailsScreen,
                                boundsTransform = { _, _ ->
                                    spring(
                                        Spring.DampingRatioMediumBouncy,
                                        Spring.StiffnessLow
                                    )
                                }
                            )
                        }
                            .fillMaxSize()
                            .graphicsLayer(
                                scaleX = 1.05f,
                                scaleY = 1.05f
                            )
                    )
                }

                is HexagonField.ColorField -> HexColorUi(
                    color = field.color,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(
                            scaleX = .95f,
                            scaleY = .95f
                        )
                )

                is HexagonField.LabeledField -> HexLabelUi(
                    label = field.label,
                    value = field.value,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(
                            scaleX = .95f,
                            scaleY = .95f
                        )
                )
            }
        }
    }
}

@Composable
fun HexColorUi(color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(color, Hexagon)
    )
}

@Composable
fun HexLabelUi(label: String, value: Int, modifier: Modifier = Modifier) {
    OutlinedCard(shape = Hexagon, modifier = modifier) {
        Text(
            text = label,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = value.toString(),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun HexCarUi(image: Int, name: String, modifier: Modifier = Modifier) {
    ElevatedCard(shape = Hexagon, modifier = modifier) {
        AsyncImage(
            model = image,
            contentDescription = "car image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

fun CarModel.toDestination() = CarDetailsScreenDestination(
    imageResource = imageResource,
    name = name,
    velocity = velocity,
    kindness = kindness,
    popularity = popularity,
    funny = funny,
    wheels = wheels,
    color = color
)

@Serializable
data class CarDetailsScreenDestination(
    @DrawableRes val imageResource: Int,
    val name: String,
    @IntRange(0, 10) val velocity: Int,
    @IntRange(0, 10) val kindness: Int,
    @IntRange(0, 10) val popularity: Int,
    @IntRange(0, 10) val funny: Int,
    @IntRange(0, 10) val wheels: Int,
    val color: Int,
) {
    fun toCar() =
        CarModel(imageResource, name, velocity, kindness, popularity, funny, wheels, color)
}

fun NavGraphBuilder.detailsScreen(onBack: () -> Unit) {
    composable<CarDetailsScreenDestination> { backStackEntry ->
        val destination = backStackEntry.toRoute<CarDetailsScreenDestination>()
        DetailsScreen(carModel = destination.toCar(), onBack)
    }
}

