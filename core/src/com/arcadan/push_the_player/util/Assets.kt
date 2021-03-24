package com.arcadan.push_the_player.util

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetErrorListener
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Disposable

class Assets private constructor() : Disposable, AssetErrorListener {
    @JvmField
    var myPlayerAssets: PlayerAssets? = null
    @JvmField
    var platformAssets: PlatformAssets? = null
    @JvmField
    var enemyAssets: EnemyAssets? = null
    @JvmField
    var tileMap: TiledMap? = null
    @JvmField
    var currentLevel = "1"
    @JvmField
    var bulletAssets: BulletAssets? = null
    @JvmField
    var explosionAssets: ExplosionAssets? = null
    @JvmField
    var powerupAssets: PowerupAssets? = null
    @JvmField
    var exitPortalAssets: ExitPortalAssets? = null
    @JvmField
    var onscreenControlsAssets: OnscreenControlsAssets? = null
    private var assetManager: AssetManager? = null
    fun init(assetManager: AssetManager) {
        this.assetManager = assetManager
        assetManager.setErrorListener(this)
        assetManager.load(Constants.TEXTURE_ATLAS, TextureAtlas::class.java)
        assetManager.load("Level1.tmx", TiledMap::class.java)
        assetManager.load("Level2.tmx", TiledMap::class.java)
        assetManager.finishLoading()
        val atlas = assetManager.get<TextureAtlas>(Constants.TEXTURE_ATLAS)
        myPlayerAssets = PlayerAssets(atlas)

        // Initialize platformAssets, passing in the atlas
        platformAssets = PlatformAssets(atlas)

        //Initialize enemyAssets
        enemyAssets = EnemyAssets(atlas)

        // Initialize bulletAssets, explosionAssets, and powerupAssets
        bulletAssets = BulletAssets(atlas)
        explosionAssets = ExplosionAssets(atlas)
        powerupAssets = PowerupAssets(atlas)
        exitPortalAssets = ExitPortalAssets(atlas)
        onscreenControlsAssets = OnscreenControlsAssets(atlas)
        tileMap = assetManager.get("Level1.tmx")
    }

    override fun error(asset: AssetDescriptor<*>, throwable: Throwable) {
        Gdx.app.error(TAG, "Couldn't load asset: " + asset.fileName, throwable)
    }

    override fun dispose() {
        assetManager!!.dispose()
    }

    inner class PlayerAssets(atlas: TextureAtlas) {
        val standingLeft: AtlasRegion = atlas.findRegion(Constants.NEW_STANDING_LEFT)

        @JvmField
        val standingRight: AtlasRegion = atlas.findRegion(Constants.NEW_STANDING_RIGHT)
        val walkingLeft: AtlasRegion = atlas.findRegion(Constants.WALKING_LEFT_2)
        val walkingRight: AtlasRegion = atlas.findRegion(Constants.WALKING_RIGHT_2)

        @JvmField
        val runningLeftAnimation: Animation<*>
        @JvmField
        val runningRightAnimation: Animation<*>
        @JvmField
        val idleLeft: Animation<*>
        @JvmField
        val idleRight: Animation<*>
        @JvmField
        val jumpLeft: Animation<*>
        @JvmField
        val jumpRight: Animation<*>

        init {

            //jump
            val jumpLeftFrames = Array<AtlasRegion?>()
            jumpLeftFrames.add(atlas.findRegion(Constants.JUMPING_LEFT_1))
            jumpLeftFrames.add(atlas.findRegion(Constants.JUMPING_LEFT_0))
            jumpLeftFrames.add(atlas.findRegion(Constants.JUMPING_LEFT_1))
            jumpLeftFrames.add(atlas.findRegion(Constants.JUMPING_LEFT_2))
            jumpLeftFrames.add(atlas.findRegion(Constants.JUMPING_LEFT_3))
            jumpLeft = Animation<Any?>(Constants.JUMP_LOOP_DURATION, jumpLeftFrames, Animation.PlayMode.NORMAL)
            val jumpRightFrames = Array<AtlasRegion?>()
            jumpRightFrames.add(atlas.findRegion(Constants.JUMPING_RIGHT_1))
            jumpRightFrames.add(atlas.findRegion(Constants.JUMPING_RIGHT_0))
            jumpRightFrames.add(atlas.findRegion(Constants.JUMPING_RIGHT_1))
            jumpRightFrames.add(atlas.findRegion(Constants.JUMPING_RIGHT_2))
            jumpRightFrames.add(atlas.findRegion(Constants.JUMPING_RIGHT_3))
            jumpRight = Animation<Any?>(Constants.JUMP_LOOP_DURATION, jumpRightFrames, Animation.PlayMode.NORMAL)


            //Idle left
            val idleLeftFrames = Array<AtlasRegion?>()
            idleLeftFrames.add(atlas.findRegion(Constants.IDLE_LEFT_1))
            idleLeftFrames.add(atlas.findRegion(Constants.IDLE_LEFT_2))
            idleLeft = Animation<Any?>(Constants.IDLE_LOOP_DURATION, idleLeftFrames, Animation.PlayMode.LOOP)

            //Idle right
            val idleRightFrames = Array<AtlasRegion?>()
            idleRightFrames.add(atlas.findRegion(Constants.IDLE_RIGHT_1))
            idleRightFrames.add(atlas.findRegion(Constants.IDLE_RIGHT_2))
            idleRight = Animation<Any?>(Constants.IDLE_LOOP_DURATION, idleRightFrames, Animation.PlayMode.LOOP)


            //Create an Array of AtlasRegions to hold the walking left frames
            val walkingLeftFrames = Array<AtlasRegion?>()

            //Add the proper frames to the array
            walkingLeftFrames.add(atlas.findRegion(Constants.WALKING_LEFT_2))
            walkingLeftFrames.add(atlas.findRegion(Constants.WALKING_LEFT_1))
            walkingLeftFrames.add(atlas.findRegion(Constants.WALKING_LEFT_2))
            walkingLeftFrames.add(atlas.findRegion(Constants.WALKING_LEFT_3))
            walkingLeftFrames.add(atlas.findRegion(Constants.WALKING_LEFT_4))
            walkingLeftFrames.add(atlas.findRegion(Constants.WALKING_LEFT_5))
            walkingLeftFrames.add(atlas.findRegion(Constants.WALKING_LEFT_6))

            //Create the run left animation
            runningLeftAnimation = Animation<Any?>(Constants.RUN_LOOP_DURATION, walkingLeftFrames, Animation.PlayMode.LOOP)

            //Do the same with the run right frames
            val walkingRightFrames = Array<AtlasRegion?>()
            walkingRightFrames.add(atlas.findRegion(Constants.WALKING_RIGHT_2))
            walkingRightFrames.add(atlas.findRegion(Constants.WALKING_RIGHT_1))
            walkingRightFrames.add(atlas.findRegion(Constants.WALKING_RIGHT_2))
            walkingRightFrames.add(atlas.findRegion(Constants.WALKING_RIGHT_3))
            walkingRightFrames.add(atlas.findRegion(Constants.WALKING_RIGHT_4))
            walkingRightFrames.add(atlas.findRegion(Constants.WALKING_RIGHT_5))
            walkingRightFrames.add(atlas.findRegion(Constants.WALKING_RIGHT_6))
            runningRightAnimation = Animation<Any?>(Constants.RUN_LOOP_DURATION, walkingRightFrames, Animation.PlayMode.LOOP)
        }
    }

    inner class PlatformAssets(atlas: TextureAtlas) {
        // Add a NinePatch member
        @JvmField
        val platformNinePatch: NinePatch

        init {
            // Find the AtlasRegion holding the platform
            val region = atlas.findRegion(Constants.PLATFORM_SPRITE)
            //  Turn that AtlasRegion into a NinePatch using the edge constant you defined
            val edge = Constants.PLATFORM_EDGE
            platformNinePatch = NinePatch(region, edge, edge, edge, edge)
        }
    }

    inner class EnemyAssets(atlas: TextureAtlas) {
        @JvmField
        val enemy: AtlasRegion = atlas.findRegion(Constants.ENEMY_SPRITE)

    }

    inner class BulletAssets(atlas: TextureAtlas) {
        // Add an AtlasRegion to hold the bullet sprite
        @JvmField
        val bulletRight: AtlasRegion = atlas.findRegion(Constants.BULLET_RIGHT)

        @JvmField
        val bulletLeft: AtlasRegion = atlas.findRegion(Constants.BULLET_LEFT)

        init {
            // Find the bullet atlas region
        }
    }

    inner class ExplosionAssets(atlas: TextureAtlas) {
        //  Add an Animation
        @JvmField
        val explosion: Animation<*>

        init {

            //  Populate the explosion animation
            // First find the appropriate AtlasRegions
            // Then pack them into an animation with the correct frame duration
            val explosionRegions = Array<AtlasRegion?>()
            explosionRegions.add(atlas.findRegion(Constants.EXPLOSION_LARGE))
            explosionRegions.add(atlas.findRegion(Constants.EXPLOSION_MEDIUM))
            explosionRegions.add(atlas.findRegion(Constants.EXPLOSION_SMALL))
            explosion = Animation<Any?>(Constants.EXPLOSION_DURATION / explosionRegions.size,
                    explosionRegions, Animation.PlayMode.NORMAL)
        }
    }

    inner class PowerupAssets(atlas: TextureAtlas) {
        //  Add an AtlasRegion to hold the powerup sprite
        @JvmField
        val powerup: AtlasRegion = atlas.findRegion(Constants.POWERUP_SPRITE)

        init {
            // Find the powerup atlas region
        }
    }

    inner class ExitPortalAssets(atlas: TextureAtlas) {
        @JvmField
        val exitPortal: Animation<*>

        init {
            val exitPortal1 = atlas.findRegion(Constants.EXIT_PORTAL_SPRITE_1)
            val exitPortal2 = atlas.findRegion(Constants.EXIT_PORTAL_SPRITE_2)
            val exitPortal3 = atlas.findRegion(Constants.EXIT_PORTAL_SPRITE_3)
            val exitPortal4 = atlas.findRegion(Constants.EXIT_PORTAL_SPRITE_4)
            val exitPortal5 = atlas.findRegion(Constants.EXIT_PORTAL_SPRITE_5)
            val exitPortal6 = atlas.findRegion(Constants.EXIT_PORTAL_SPRITE_6)
            val exitPortalFrames = Array<AtlasRegion?>()
            exitPortalFrames.addAll(exitPortal1, exitPortal2, exitPortal3, exitPortal4, exitPortal5, exitPortal6)
            // Go define that constant in Constants.java if you haven't already
            exitPortal = Animation<Any?>(Constants.EXIT_PORTAL_FRAME_DURATION, exitPortalFrames)
        }
    }

    inner class OnscreenControlsAssets(atlas: TextureAtlas) {
        @JvmField
        val moveRight: AtlasRegion = atlas.findRegion(Constants.MOVE_RIGHT_BUTTON)

        @JvmField
        val moveLeft: AtlasRegion = atlas.findRegion(Constants.MOVE_LEFT_BUTTON)

        @JvmField
        val shoot: AtlasRegion = atlas.findRegion(Constants.SHOOT_BUTTON)

        @JvmField
        val jump: AtlasRegion = atlas.findRegion(Constants.JUMP_BUTTON)

    }

    fun getTileMap(levelNumber: String): TiledMap {
        val level = "Level$levelNumber.tmx"
        currentLevel = levelNumber
        tileMap = assetManager!!.get(level)
        return assetManager!!.get(level)
    }

    companion object {
        val TAG: String = Assets::class.java.name

        @JvmField
        val instance = Assets()
    }
}