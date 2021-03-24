package com.arcadan.push_the_player.entities

import com.arcadan.push_the_player.util.Assets
import com.arcadan.push_the_player.util.Constants
import com.arcadan.push_the_player.util.Enums
import com.arcadan.push_the_player.util.Utilities.drawTextureRegion
import com.arcadan.push_the_player.util.Utilities.secondsSince
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.TimeUtils

class Enemy(private val platform: Platform) {
    private val startTime: Long
    private val bobOffset: Float

    @JvmField
    var position: Vector2

    @JvmField
    var health: Int
    private var direction: Enums.Direction
    fun update(delta: Float) {
        when (direction) {
            Enums.Direction.LEFT -> position.x -= Constants.ENEMY_MOVEMENT_SPEED * delta
            Enums.Direction.RIGHT -> position.x += Constants.ENEMY_MOVEMENT_SPEED * delta
        }

        //  If the enemy is off the left side of the platform, set the enemy moving back to the right
        // Should also probably put the enemy back on the edge of the platform

        //  If the enemy if off the right side of the platform, set the enemy moving back to the left
        if (position.x < platform.left) {
            position.x = platform.left
            direction = Enums.Direction.RIGHT
        } else if (position.x > platform.right) {
            position.x = platform.right
            direction = Enums.Direction.LEFT
        }

        //  Figure out where in the bob cycle we're at
        // bobMultiplier = 1 + sin(2 PI elapsedTime / period)
        val elapsedTime = secondsSince(startTime)
        val bobMultiplier = 1 + MathUtils.sin(MathUtils.PI2 * elapsedTime / Constants.ENEMY_BOB_PERIOD)

        //  Set the enemy vertical position
        // y = platformTop + enemyCenter + bobAmplitude * bobMultiplier
        position.y = platform.top + Constants.ENEMY_CENTER.y + Constants.ENEMY_BOB_AMPLITUDE * bobMultiplier
    }

    fun render(batch: SpriteBatch?) {
        val region: TextureRegion = Assets.instance.enemyAssets!!.enemy
        drawTextureRegion(batch!!, region, position, Constants.ENEMY_CENTER)
    }

    init {
        direction = Enums.Direction.RIGHT
        position = Vector2(platform.left, platform.top + Constants.ENEMY_CENTER.y)
        startTime = TimeUtils.nanoTime()
        health = Constants.ENEMY_HEALTH
        bobOffset = MathUtils.random()
    }
}