package com.arcadan.push_the_player.overlays

import com.arcadan.push_the_player.entities.Explosion
import com.arcadan.push_the_player.util.Constants
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport

class VictoryOverlay {

    private val font: BitmapFont

    @JvmField
    val viewport: Viewport
    var explosions: Array<Explosion>? = null
    fun init() {
        explosions = Array(Constants.EXPLOSION_COUNT)

        //  Fill the explosions array with explosions at random locations within the viewport
        // Also, set the offset of each explosion to a random float from 0 -- Constants.LEVEL_END_DURATION
        for (i in 0 until Constants.EXPLOSION_COUNT) {
            val explosion = Explosion(Vector2(
                    MathUtils.random(viewport.worldWidth),
                    MathUtils.random(viewport.worldHeight)
            ))
            explosion.offset = MathUtils.random(Constants.LEVEL_END_DURATION)
            explosions!!.add(explosion)
        }
    }

    fun render(batch: SpriteBatch) {
        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()

        //  Render the explosions/fireworks
        for (explosion in explosions!!) {
            explosion.render(batch)
        }

        //  Draw a victory message
        font.draw(batch, Constants.VICTORY_MESSAGE, viewport.worldWidth / 2, viewport.worldHeight / 2.5f, 0f, Align.center, false)
        batch.end()
    }

    companion object {
        val TAG = VictoryOverlay::class.java.name
    }

    init {
        viewport = ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE)
        font = BitmapFont(Gdx.files.internal(Constants.FONT_FILE))
        font.data.setScale(1f)
    }
}