package com.arcadan.push_the_player.entities

import com.arcadan.push_the_player.util.Assets
import com.arcadan.push_the_player.util.Constants
import com.arcadan.push_the_player.util.Utilities.drawTextureRegion
import com.arcadan.push_the_player.util.Utilities.secondsSince
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.TimeUtils

class Explosion(private val position: Vector2) {
    private val startTime: Long = TimeUtils.nanoTime()

    @JvmField
    var offset = 0f
    fun render(batch: SpriteBatch?) {
        //  Select and draw the appropriate frame of the explosion animation
        // Remember to use Utils.drawTextureRegion() and Utils.secondsSince()
        drawTextureRegion(
                batch!!,
                (Assets.instance.explosionAssets!!.explosion.getKeyFrame(secondsSince(startTime)) as TextureRegion),
                position.x - Constants.EXPLOSION_CENTER.x,
                position.y - Constants.EXPLOSION_CENTER.y
        )
    }

    fun yetToStart(): Boolean {
        return secondsSince(startTime) - offset < 0
    }

    //  Use Animation.isAnimationFinished() to find out if the explosion is done
    val isFinished: Boolean
        get() {
            //  Use Animation.isAnimationFinished() to find out if the explosion is done
            val elapsedTime = secondsSince(startTime) - offset
            return Assets.instance.explosionAssets!!.explosion.isAnimationFinished(elapsedTime)
        }
}