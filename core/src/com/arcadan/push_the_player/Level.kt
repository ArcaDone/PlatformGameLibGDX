package com.arcadan.push_the_player

import com.arcadan.push_the_player.entities.*
import com.arcadan.push_the_player.util.Assets
import com.arcadan.push_the_player.util.Constants
import com.arcadan.push_the_player.util.Constants.DEBUG_MODE
import com.arcadan.push_the_player.util.Enums
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.DelayedRemovalArray
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport

class Level {
    @JvmField
    var gameOver: Boolean
    @JvmField
    var victory: Boolean
    @JvmField
    var viewport: Viewport
    @JvmField
    var score: Int
    var player: Player
    var exitPortal: ExitPortal
    var platforms: Array<Platform>
        private set
    var enemies: DelayedRemovalArray<Enemy>
        private set
    private var bullets: DelayedRemovalArray<Bullet>
    private var explosions: DelayedRemovalArray<Explosion>
    var powerups: DelayedRemovalArray<Powerup>
        private set
    var orthogonalTiledMapRenderer: OrthogonalTiledMapRenderer

    constructor() {
        viewport = ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE)
        orthogonalTiledMapRenderer = OrthogonalTiledMapRenderer(Assets.instance.tileMap)
        orthogonalTiledMapRenderer.setView(viewport.camera as OrthographicCamera)
        player = Player(Vector2(50f, 50f), this)
        platforms = Array()
        enemies = DelayedRemovalArray()
        bullets = DelayedRemovalArray()
        explosions = DelayedRemovalArray()
        powerups = DelayedRemovalArray()
        exitPortal = ExitPortal(Vector2(200f, 800f))
        gameOver = false
        victory = false
        score = 0
    }

    constructor(levelNum: Int) {
        viewport = ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE)
        orthogonalTiledMapRenderer = OrthogonalTiledMapRenderer(Assets.instance.getTileMap(levelNum.toString()))
        orthogonalTiledMapRenderer.setView(viewport.camera as OrthographicCamera)
        player = Player(Vector2(50f, 50f), this)
        platforms = Array()
        enemies = DelayedRemovalArray()
        bullets = DelayedRemovalArray()
        explosions = DelayedRemovalArray()
        powerups = DelayedRemovalArray()
        exitPortal = ExitPortal(Vector2(200f, 800f))
        gameOver = false
        victory = false
        score = 0
    }

    fun update(delta: Float) {
        if (player.lives < 0) {
            gameOver = true
        } else if (player.position.dst(exitPortal.position) < Constants.EXIT_PORTAL_RADIUS) {
            victory = true
        }
        if (!gameOver && !victory) {
            // Update Player
            player.update(delta, platforms)

            // Update Bullets
            bullets.begin()
            for (bullet in bullets) {
                bullet.update(delta)
                if (!bullet.active) {
                    bullets.removeValue(bullet, false)
                }
            }
            bullets.end()

            // Update Enemies
            enemies.begin()
            for (i in 0 until enemies.size) {
                val enemy = enemies[i]
                enemy.update(delta)
                //If enemy health is less than 1, remove the enemy from the enemies collection
                if (enemy.health < 1) {
                    // Spawn an explosion at the enemy position
                    spawnExplosion(enemy.position)
                    enemies.removeIndex(i)

                    //  Add the ENEMY_KILL_SCORE to the score
                    score += Constants.ENEMY_KILL_SCORE
                }
            }
            enemies.end()

            // Remove any explosions that are finished
            explosions.begin()
            for (i in 0 until explosions.size) {
                if (explosions[i].isFinished) {
                    explosions.removeIndex(i)
                }
            }
            explosions.end()
        }
    }

    fun render(batch: SpriteBatch) {

        // Based on what you render, you create the different overlapping layers.
        // For example, if you want the EXIT portal behind the player,
        // you MUST render it before rendering the player
        viewport.apply()
        if (!DEBUG_MODE) {
            orthogonalTiledMapRenderer.setView(viewport.camera as OrthographicCamera)
            orthogonalTiledMapRenderer.render()
        }
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        for (platform in platforms) {
            platform.render(batch)
        }
        exitPortal.render(batch)
        for (powerup in powerups) {
            powerup.render(batch)
        }
        for (enemy in enemies) {
            enemy.render(batch)
        }
        player.render(batch)
        for (bullet in bullets) {
            bullet.render(batch)
        }
        for (explosion in explosions) {
            explosion.render(batch)
        }
        batch.end()
    }

    fun initializeDebugLevel() {
        player = Player(Vector2(15f, 40f), this)

        // Around (150, 150) will do fine
        exitPortal = ExitPortal(Vector2(150f, 150f))
        platforms = Array()
        bullets = DelayedRemovalArray()
        enemies = DelayedRemovalArray()
        explosions = DelayedRemovalArray()
        powerups = DelayedRemovalArray()
        platforms.add(Platform(15f, 100f, 30f, 20f))
        val enemyPlatform = Platform(75f, 90f, 100f, 65f)
        enemies.add(Enemy(enemyPlatform))
        platforms.add(enemyPlatform)
        platforms.add(Platform(35f, 55f, 50f, 20f))
        platforms.add(Platform(10f, 20f, 20f, 9f))
        powerups.add(Powerup(Vector2(20f, 110f)))
    }

    fun spawnBullet(position: Vector2?, direction: Enums.Direction?) {
        bullets.add(Bullet(this, position!!, direction!!))
    }

    fun spawnExplosion(position: Vector2?) {
        explosions.add(Explosion(position!!))
    }

    companion object {
        val TAG = Level::class.java.name
    }
}