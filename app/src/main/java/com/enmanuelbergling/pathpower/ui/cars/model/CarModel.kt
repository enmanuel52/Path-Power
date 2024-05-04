package com.enmanuelbergling.pathpower.ui.cars.model

import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import androidx.compose.ui.graphics.Color
import com.enmanuelbergling.pathpower.R

data class CarModel(
    @DrawableRes val imageResource: Int,
    val name: String,
    @IntRange(0, 10) val velocity: Int,
    @IntRange(0, 10) val kindness: Int,
    @IntRange(0, 10) val popularity: Int,
    @IntRange(0, 10) val funny: Int,
    @IntRange(0, 10) val wheels: Int,
    val color: Color,
)

val ChickHicks = CarModel(
    imageResource = R.drawable.chick_hicks,
    name = "Chick Hicks",
    velocity = 6,
    kindness = 2,
    popularity = 2,
    funny = 1,
    wheels = 4,
    color = Color(0xFF7DE15E)
)
val CocHudson = CarModel(
    imageResource = R.drawable.coc_hudson,
    name = "Coc Hudson",
    velocity = 6,
    kindness = 3,
    popularity = 4,
    funny = 1,
    wheels = 4,
    color = Color(0xFF373856)
)
val CruzRamirez = CarModel(
    imageResource = R.drawable.cruz_ramirez,
    name = "Cruz Ramirez",
    velocity = 8,
    kindness = 3,
    popularity = 5,
    funny = 2,
    wheels = 4,
    color = Color(0xFFFEAE4E)
)
val Fillmore = CarModel(
    imageResource = R.drawable.fillmore,
    name = "Fillmore",
    velocity = 1,
    kindness = 9,
    popularity = 5,
    funny = 5,
    wheels = 4,
    color = Color(0xFF18B126)
)
val GreaterMate = CarModel(
    imageResource = R.drawable.greater_mate,
    name = "Mate the Greater",
    velocity = 8,
    kindness = 5,
    popularity = 5,
    funny = 7,
    wheels = 4,
    color = Color.White
)
val Guido = CarModel(
    imageResource = R.drawable.guido,
    name = "Guido",
    velocity = 1,
    kindness = 8,
    popularity = 5,
    funny = 5,
    wheels = 3,
    color = Color(0xFF71e1fb)
)
val Mack = CarModel(
    imageResource = R.drawable.mack,
    name = "Mack",
    velocity = 2,
    kindness = 7,
    popularity = 6,
    funny = 3,
    wheels = 6,
    color = Color(0xFFd93a25)
)
val Sheriff = CarModel(
    imageResource = R.drawable.sheriff,
    name = "Sheriff",
    velocity = 5,
    kindness = 3,
    popularity = 4,
    funny = 1,
    wheels = 4,
    color = Color.Black
)
val Sally = CarModel(
    imageResource = R.drawable.sally,
    name = "Sally",
    velocity = 6,
    kindness = 3,
    popularity = 4,
    funny = 2,
    wheels = 4,
    color = Color(0xFF8fb0dd)
)
val Rojo = CarModel(
    imageResource = R.drawable.rojo,
    name = "Rojo",
    velocity = 4,
    kindness = 7,
    popularity = 4,
    funny = 3,
    wheels = 4,
    color = Color(0xFFf72c04)
)
val McQueen = CarModel(
    imageResource = R.drawable.mc_queen,
    name = "McQueen",
    velocity = 10,
    kindness = 5,
    popularity = 9,
    funny = 4,
    wheels = 4,
    color = Color(0xFFc2061e)
)
val Matedor = CarModel(
    imageResource = R.drawable.matedor,
    name = "El Matedor",
    velocity = 6,
    kindness = 3,
    popularity = 5,
    funny = 8,
    wheels = 4,
    color = Color(0xFF03c9d2)
)
val Mate = CarModel(
    imageResource = R.drawable.mate,
    name = "Mate",
    velocity = 2,
    kindness = 8,
    popularity = 9,
    funny = 9,
    wheels = 4,
    color = Color(0xFFcc7e46)
)
val CARS = listOf(
    ChickHicks,
    CocHudson,
    CruzRamirez,
    Fillmore,
    GreaterMate,
    Guido,
    Mack,
    Mate,
    Matedor,
    McQueen,
    Rojo,
    Sally,
    Sheriff
)
