package com.arcadan.push_the_player.entities

import com.arcadan.push_the_player.util.Assets
import com.arcadan.push_the_player.util.Constants
import com.arcadan.push_the_player.util.Utilities.drawTextureRegion
import com.arcadan.push_the_player.util.Utilities.secondsSince
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.TimeUtils

class ExitPortal(val position: Vector2) {
    private val startTime: Long = TimeUtils.nanoTime()
    fun render(batch: SpriteBatch?) {
        val elapsedTime = secondsSince(startTime)
        val region = Assets.instance.exitPortalAssets!!.exitPortal.getKeyFrame(elapsedTime, true) as TextureRegion
        drawTextureRegion(batch!!, region, position, Constants.EXIT_PORTAL_CENTER)
    }
}