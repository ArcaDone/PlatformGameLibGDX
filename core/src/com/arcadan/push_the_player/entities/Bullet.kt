package com.arcadan.push_the_player.entities

import com.arcadan.push_the_player.Level
import com.arcadan.push_the_player.util.Assets
import com.arcadan.push_the_player.util.Constants
import com.arcadan.push_the_player.util.Enums
import com.arcadan.push_the_player.util.Utilities.drawTextureRegion
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2

class Bullet(private val level: Level, private val position: Vector2, private val direction: Enums.Direction) {
    var active = true

    fun update(delta: Float) {
        when (direction) {
            Enums.Direction.LEFT -> position.x -= delta * Constants.BULLET_MOVE_SPEED
            Enums.Direction.RIGHT -> position.x += delta * Constants.BULLET_MOVE_SPEED
        }
        for (enemy in level.enemies) {

            //  Check if the bullet is within the enemy hit detection radius
            if (position.dst(enemy.position) < Constants.ENEMY_SHOT_RADIUS) {
                //  Spawn an explosion
                level.spawnExplosion(position)
                //  If so, set active = false
                active = false

                //  And decrement enemy health
                enemy.health -= 1
                //  Add the ENEMY_HIT_SCORE to the level.score
                level.score += Constants.ENEMY_HIT_SCORE
            }
        }

        //Get the world width from the level's viewport
        val worldWidth: Float = level.viewport.worldWidth
        //  Get the level's viewport's camera's horizontal position
        val cameraX: Float = level.viewport.camera.position.x

        //  If the bullet is offscreen, set active = false
        if (position.x < cameraX - worldWidth / 2 || position.x > cameraX + worldWidth / 2) {
            active = false
        }
    }

    fun render(batch: SpriteBatch?) {
        val region: TextureRegion = when (direction) {
            Enums.Direction.LEFT -> Assets.instance.bulletAssets!!.bulletLeft
            Enums.Direction.RIGHT -> Assets.instance.bulletAssets!!.bulletRight
        }
        drawTextureRegion(batch!!, region, position, Constants.BULLET_CENTER)
    }
}