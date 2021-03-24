package com.arcadan.push_the_player.overlays

import com.arcadan.push_the_player.util.Assets
import com.arcadan.push_the_player.util.Constants
import com.arcadan.push_the_player.util.Utilities.drawTextureRegion
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport

class HudStuff {
    private val font: BitmapFont
    @JvmField
    val viewport: Viewport

    fun render(batch: SpriteBatch, lives: Int, ammo: Int, score: Int) {
        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()

        //  Draw Player's score and ammo count in the top left of the viewport
        val hudString = """
               ${Constants.HUD_SCORE_LABEL}$score
               ${Constants.HUD_AMMO_LABEL}$ammo
               """.trimIndent()
        font.draw(batch, hudString, Constants.HUD_MARGIN, viewport.worldHeight - Constants.HUD_MARGIN)

        //  Draw a tiny Player in the top right for each life left
        val standingRight: TextureRegion = Assets.instance.myPlayerAssets!!.standingRight
        for (i in 1..lives) {
            val drawPosition = Vector2(
                    viewport.worldWidth - i * (Constants.HUD_MARGIN / 2 + standingRight.regionWidth),
                    viewport.worldHeight - Constants.HUD_MARGIN - standingRight.regionHeight
            )
            drawTextureRegion(
                    batch,
                    standingRight,
                    drawPosition
            )
        }
        batch.end()
    }

    init {
        viewport = ExtendViewport(Constants.HUD_VIEWPORT_SIZE, Constants.HUD_VIEWPORT_SIZE)
        font = BitmapFont()
        font.data.setScale(1f)
    }
}