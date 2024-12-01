package com.enmanuelbergling.pathpower.ui.wallpaper

import androidx.annotation.DrawableRes
import com.enmanuelbergling.pathpower.R

data class Wallpaper(
    @DrawableRes val image: Int,
    val label: String,
)

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
        R.drawable.bunny1,
        "A really bad bunny :)"
    ),
    Wallpaper(
        R.drawable.cat_gossiping,
        "Gossiping"
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
        R.drawable.groot_making_friends,
        "You know who i am"
    ),
    Wallpaper(
        R.drawable.lakers,
        "Lakers"
    ),
    Wallpaper(
        R.drawable.night,
        "Night"
    ),
    Wallpaper(
        R.drawable.peter,
        "Peter"
    ),
    Wallpaper(
        R.drawable.planets,
        "Planets"
    ),
    Wallpaper(
        R.drawable.some_cats,
        "Some cats"
    ),
    Wallpaper(
        R.drawable.tom,
        "Tom"
    ),
)
