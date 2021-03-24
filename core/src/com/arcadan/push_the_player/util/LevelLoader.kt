package com.arcadan.push_the_player.util

import com.arcadan.push_the_player.Level
import com.arcadan.push_the_player.entities.*
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array

object LevelLoader {
    val TAG = LevelLoader::class.java.toString()
    @JvmStatic
    fun load(levelNum: Int): Level {
        val level = Level(levelNum)
        populatePlatforms(level, levelNum)
        loadNonPlatformEntities(level, levelNum)
        return level
    }

    private fun populatePlatforms(levelName: Level, levelNum: Int) {
        val mapLayer = Assets.instance.getTileMap(levelNum.toString()).layers["Platforms"]
        if (mapLayer != null) {
            for (mapObject in mapLayer.objects) {
                val platformArray = Array<Platform>()
                val x = mapObject.properties.get("x", Float::class.java)
                val y = mapObject.properties.get("y", Float::class.java)
                val width = mapObject.properties.get("width", Float::class.java)
                val height = mapObject.properties.get("height", Float::class.java)
                //  Make a new platform with the dimensions we loaded
                // Remember that the y position we loaded is the platform bottom, not top
                val platform = Platform(x, y + height, width, height)

                //  Add the platform to the platformArray
                platformArray.add(platform)
                //  Add all the platforms from platformArray to the level
                levelName.platforms.addAll(platformArray)
            }
        }
    }

    private fun loadNonPlatformEntities(levelName: Level, levelNum: Int) {
        val starterPlayer = Assets.instance.getTileMap(levelNum.toString()).layers["StarterPlayer"]
        val endPortal = Assets.instance.getTileMap(levelNum.toString()).layers["EndPortal"]
        val enemies = Assets.instance.getTileMap(levelNum.toString()).layers["Enemies"]
        val powerUps = Assets.instance.getTileMap(levelNum.toString()).layers["PowerUps"]
        if (starterPlayer != null) {
            for (player in starterPlayer.objects) {
                //  Get the lower left corner of the object
                // Remember to use safeGetFloat()
                var lowerLeftCorner = Vector2()
                val x = player.properties.get("x", Float::class.java)
                val y = player.properties.get("y", Float::class.java)
                lowerLeftCorner = Vector2(x, y)
                // If so, add Player's eye position to find her spawn position
                val myPlayerPosition = lowerLeftCorner.add(Constants.PLAYER_EYE_POSITION)
                Gdx.app.log(TAG, "Loaded Player at $myPlayerPosition")

                // Add our new Player to the level
                levelName.player = Player(myPlayerPosition, levelName)
            }
        }
        if (endPortal != null) {
            for (portal in endPortal.objects) {
                //  Get the lower left corner of the object
                // Remember to use safeGetFloat()
                var lowerLeftCorner = Vector2()
                val x = portal.properties.get("x", Float::class.java)
                val y = portal.properties.get("y", Float::class.java)
                lowerLeftCorner = Vector2(x, y)
                val exitPortalPosition = lowerLeftCorner.add(Constants.EXIT_PORTAL_CENTER)
                Gdx.app.log(TAG, "Loaded the exit portal at $exitPortalPosition")
                levelName.exitPortal = ExitPortal(exitPortalPosition)
            }
        }
        if (enemies != null) {
            for (enemyObj in enemies.objects) {
                val x = enemyObj.properties.get("x", Float::class.java)
                val y = enemyObj.properties.get("y", Float::class.java)
                val height = MathUtils.random(5f, 10f)
                val width = MathUtils.random(0f, 30f)
                val movementPattern = Platform(x, y + height, width, height)
                Gdx.app.log(TAG, "Loaded an enemy on that platform")
                //  If so, create a new enemy on the platform
                val enemy = Enemy(movementPattern)

                //  Add that enemy to the list of enemies in the level
                levelName.enemies.add(enemy)
            }
        }
        if (powerUps != null) {
            for (powerUp in powerUps.objects) {
                //  Get the lower left corner of the object
                // Remember to use safeGetFloat()
                var lowerLeftCorner = Vector2()
                val x = powerUp.properties.get("x", Float::class.java)
                val y = powerUp.properties.get("y", Float::class.java)
                lowerLeftCorner = Vector2(x, y)
                val powerupPosition = lowerLeftCorner.add(Constants.POWERUP_CENTER)
                Gdx.app.log(TAG, "Loaded a powerup at $powerupPosition")
                levelName.powerups.add(Powerup(powerupPosition))
            }
        }
    }
}