package com.enmanuelbergling.pathpower.ui.wallpaper

import androidx.annotation.DrawableRes
import com.enmanuelbergling.pathpower.R

data class Wallpaper(
    @DrawableRes val image: Int,
    val label: String = "",
) {
    val key: String
        get() = label
}

val WALLPAPERS = listOf(
    Wallpaper(R.drawable.beach,"beach"),
    Wallpaper(R.drawable.brothers,"brother"),
    Wallpaper(R.drawable.brunch,"brunch"),
    Wallpaper(R.drawable.cheese,"cheese"),
    Wallpaper(R.drawable.couple, "couple"),
    Wallpaper(R.drawable.family,"family"),
    Wallpaper(R.drawable.guys,"guys"),
//    Wallpaper(R.drawable.having_beer,"having beer"),
    Wallpaper(R.drawable.having_pizza,"having pizza"),
    Wallpaper(R.drawable.kids,"kids"),
    Wallpaper(R.drawable.lunch_at_work,"lunch at work"),
    Wallpaper(R.drawable.lunch_selfie,"lunch selfie"),
    Wallpaper(R.drawable.picnic,"picnic"),
    Wallpaper(R.drawable.selfie1,"selfie1"),
    Wallpaper(R.drawable.style_gal,"style gal"),
    Wallpaper(R.drawable.wine_selfie,"wine selfie"),
)
