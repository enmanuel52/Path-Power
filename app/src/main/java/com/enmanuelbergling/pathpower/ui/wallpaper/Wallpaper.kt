package com.enmanuelbergling.pathpower.ui.wallpaper

import androidx.annotation.DrawableRes
import com.enmanuelbergling.pathpower.R

data class Wallpaper(
    @DrawableRes val image: Int,
    val label: String,
){
    val key:String
        get() = label
}

val WALLPAPERS = listOf(
    Wallpaper(
        R.drawable._626,
        "626"
    ),
    Wallpaper(
        R.drawable.avengers,
        "Avengers"
    ),
    Wallpaper(
        R.drawable.avicii1,
        "Colorful Avicii"
    ),
    Wallpaper(
        R.drawable.avicii_burning,
        "Burning fast"
    ),
    Wallpaper(
        R.drawable.black_cat,
        "Black cat"
    ),
    Wallpaper(
        R.drawable._626_tears,
        "626 crying"
    ),
    Wallpaper(
        R.drawable.bob1,
        "Bob"
    ),
    Wallpaper(
        R.drawable.deadpool,
        "Deadpool being he"
    ),
    Wallpaper(
        R.drawable.dizzy_cat,
        "Dizzy cat"
    ),
    Wallpaper(
        R.drawable.eric,
        "Eric"
    ),
    Wallpaper(
        R.drawable.feelings,
        "Feelings"
    ),
    Wallpaper(
        R.drawable.freddy,
        "Freddy"
    ),
    Wallpaper(
        R.drawable.peter,
        "Peter"
    ),
    Wallpaper(
        R.drawable.futurama,
        "Futurama"
    ),
    Wallpaper(
        R.drawable.garfield,
        "Garfield"
    ),
    Wallpaper(
        R.drawable.tom,
        "Tom"
    ),
    Wallpaper(
        R.drawable.homero,
        "Homero"
    ),
    Wallpaper(
        R.drawable.packman,
        "Packman"
    ),
    Wallpaper(
        R.drawable.patric2,
        "Patricio"
    ),
    Wallpaper(
        R.drawable.walter,
        "Walter"
    ),
    Wallpaper(
        R.drawable.no_idea,
        "No idea"
    ),
)
