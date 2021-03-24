package com.arcadan.push_the_player.overlays

import com.arcadan.push_the_player.entities.Enemy
import com.arcadan.push_the_player.entities.Platform
import com.arcadan.push_the_player.util.Constants
import com.arcadan.push_the_player.util.Utilities.secondsSince
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.TimeUtils
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport

class GameOverOverlay {
    private val font: BitmapFont
    @JvmField
    val viewport: Viewport
    var enemies: Array<Enemy>? = null
    var startTime: Long = 0

    fun init() {
        startTime = TimeUtils.nanoTime()
        enemies = Array(Constants.ENEMY_COUNT)
        for (i in 0 until Constants.ENEMY_COUNT) {
            val fakePlatform = Platform(
                    MathUtils.random(viewport.worldWidth),
                    MathUtils.random(-Constants.ENEMY_CENTER.y, viewport.worldHeight
                    ), 0f, 0f)
            val enemy = Enemy(fakePlatform)
            enemies!!.add(enemy)
        }
    }

    fun render(batch: SpriteBatch) {
        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()

        //  Draw a game over message
        // Feel free to get more creative with this screen. Perhaps you could cover the screen in enemy robots?
        val timeElapsed = secondsSince(startTime)
        val enemiesToShow = (Constants.ENEMY_COUNT * (timeElapsed / (3 * Constants.LEVEL_END_DURATION))).toInt()
        for (i in 0 until enemiesToShow) {
            val enemy = enemies!![i]
            enemy.update(0f)
            enemy.render(batch)
        }
        font.draw(batch, Constants.GAME_OVER_MESSAGE, viewport.worldWidth / 2, viewport.worldHeight / 2.5f, 0f, Align.center, false)
        batch.end()
    }

    init {
        viewport = ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE)
        font = BitmapFont(Gdx.files.internal(Constants.FONT_FILE))
        font.data.setScale(1f)
    }
}