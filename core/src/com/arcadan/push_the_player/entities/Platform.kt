package com.arcadan.push_the_player.entities

import com.arcadan.push_the_player.util.Assets
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Platform(val left: Float, val top: Float, width: Float, height: Float) {
    private val bottom: Float = top - height
    val right: Float = left + width

    // This is used by the level loading code to link enemies and platforms.
    var identifier: String? = null

    fun render(batch: SpriteBatch?) {
        val width = right - left
        val height = top - bottom
        Assets.instance.platformAssets!!.platformNinePatch.draw(batch, left - 1, bottom - 1, width + 2, height + 2)
    }
}