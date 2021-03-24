package com.arcadan.push_the_player.entities

import com.arcadan.push_the_player.Level
import com.arcadan.push_the_player.util.Assets
import com.arcadan.push_the_player.util.Constants
import com.arcadan.push_the_player.util.Constants.DEBUG_MODE
import com.arcadan.push_the_player.util.Constants.PLAYER_EYE_HEIGHT
import com.arcadan.push_the_player.util.Constants.PLAYER_HEIGHT
import com.arcadan.push_the_player.util.Constants.PLAYER_STANCE_WIDTH
import com.arcadan.push_the_player.util.Enums
import com.arcadan.push_the_player.util.Utilities.drawTextureRegion
import com.arcadan.push_the_player.util.Utilities.secondsSince
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.TimeUtils

class Player(var spawnLocation: Vector2, var level: Level) {
    private val collisionRectangle = Rectangle(0f, 0f, PLAYER_STANCE_WIDTH, PLAYER_HEIGHT)
    var jumpButtonPressed = false
    var leftButtonPressed = false
    var rightButtonPressed = false
    var position: Vector2 = Vector2()
    var lastFramePosition: Vector2 = Vector2()
    var velocity: Vector2 = Vector2()
    var facing: Enums.Direction? = null
    var jumpState: Enums.JumpState? = null
    var walkState: Enums.WalkState? = null
    var jumpStartTime: Long = 0
    var walkStartTime: Long = 0
    var ammo = 0
        private set
    var lives = 0
        private set

    private fun respawn() {
        position.set(spawnLocation)
        lastFramePosition.set(spawnLocation)
        velocity.setZero()
        jumpState = Enums.JumpState.FALLING
        facing = Enums.Direction.RIGHT
        walkState = Enums.WalkState.NOT_WALKING
    }

    fun update(delta: Float, platforms: Array<Platform>) {
        lastFramePosition.set(position)
        velocity.y -= Constants.GRAVITY
        position.mulAdd(velocity, delta)
        if (position.y < Constants.KILL_PLANE) {
            lives--
            respawn()
        }

        // Land on/fall off platforms
        if (jumpState != Enums.JumpState.JUMPING) {
            if (jumpState != Enums.JumpState.RECOILING) {
                jumpState = Enums.JumpState.FALLING
            }
            if (!DEBUG_MODE) {
                handlePeteCollision(Assets.instance.currentLevel.toInt())
            }
            for (platform in platforms) {
                if (landedOnPlatform(platform)) {
                    jumpState = Enums.JumpState.GROUNDED
                    velocity.y = 0f
                    velocity.x = 0f
                    position.y = platform.top + PLAYER_EYE_HEIGHT
                }
            }
        }

        // Collide with enemies
        // Define Player bounding rectangle
        // Use Player's constants for height and stance width
        val myPlayerBounds = Rectangle(
                position.x - PLAYER_STANCE_WIDTH / 2,
                position.y - PLAYER_EYE_HEIGHT,
                PLAYER_STANCE_WIDTH,
                PLAYER_HEIGHT)
        for (enemy in level.enemies) {
            // Define enemy bounding rectangle
            // You'll want to define an enemy collision radius constant
            val enemyBounds = Rectangle(
                    enemy.position.x - Constants.ENEMY_COLLISION_RADIUS,
                    enemy.position.y - Constants.ENEMY_COLLISION_RADIUS,
                    2 * Constants.ENEMY_COLLISION_RADIUS,
                    2 * Constants.ENEMY_COLLISION_RADIUS
            )

            // If Player overlaps an enemy, log the direction from which she hit it
            if (myPlayerBounds.overlaps(enemyBounds)) {
                if (position.x < enemy.position.x) {
                    recoilFromEnemy(Enums.Direction.LEFT)
                    Gdx.app.log(TAG, "Hit an enemy from the left")
                } else {
                    recoilFromEnemy(Enums.Direction.RIGHT)
                    Gdx.app.log(TAG, "Hit an enemy from the right")
                }
            }
        }

        // Move left/right
        if (jumpState != Enums.JumpState.RECOILING) {
            val left = Gdx.input.isKeyPressed(Input.Keys.LEFT) || leftButtonPressed
            val right = Gdx.input.isKeyPressed(Input.Keys.RIGHT) || rightButtonPressed
            if (left && !right) {
                moveLeft(delta)
            } else if (right && !left) {
                moveRight(delta)
            } else {
                walkState = Enums.WalkState.NOT_WALKING
            }
        }

        //Jump
        if (Gdx.input.isKeyPressed(Input.Keys.Z) || jumpButtonPressed) {
            // Add a switch statement. If the jump key is pressed and GG is GROUNDED, then startJump()
            // If she's JUMPING, then continueJump()
            // If she's falling, then don't do anything
            when (jumpState) {
                Enums.JumpState.GROUNDED -> startJump()
                Enums.JumpState.JUMPING -> continueJump()
            }
        } else {
            endJump()
        }


        //  Check if Player should pick up a powerup
        // This is a tough one. Check for overlaps similar to how we detect collisions with enemies, then remove any picked up powerups (and update Player's ammo count)
        // Remember to check out the solution project if you run into trouble!
        val powerups = level.powerups
        powerups.begin()
        for (i in 0 until powerups.size) {
            val powerup = powerups[i]
            val powerupBounds = Rectangle(
                    powerup.position.x - Constants.POWERUP_CENTER.x,
                    powerup.position.y - Constants.POWERUP_CENTER.y,
                    Assets.instance.powerupAssets!!.powerup.regionWidth.toFloat(),
                    Assets.instance.powerupAssets!!.powerup.regionHeight.toFloat()
            )
            if (myPlayerBounds.overlaps(powerupBounds)) {
                ammo += Constants.POWERUP_AMMO
                //  Add the POWERUP_SCORE to the level.score
                level.score += Constants.POWERUP_SCORE
                powerups.removeIndex(i)
            }
        }
        powerups.end()

        // Shoot
        // Check to see if shoot key has been pressed
        // You can make it whatever you want, but I've been using the 'x' key
        // You'll want to use Gdx.input.isKeyJustPressed()
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            shoot()
        }
    }

    fun shoot() {
        if (ammo > 0) {
            ammo--
            // Create a Vector2 to hold the position of a new bullet

            // Set the bullet's position in the case where Player is facing right
            // bullet position =  Player's position + cannon offset
            val bulletPosition: Vector2 = if (facing == Enums.Direction.RIGHT) {
                Vector2(
                        position.x + Constants.PLAYER_CANNON_OFFSET.x,
                        position.y + Constants.PLAYER_CANNON_OFFSET.y
                )
            } else {
                Vector2(
                        position.x - Constants.PLAYER_CANNON_OFFSET.x,
                        position.y + Constants.PLAYER_CANNON_OFFSET.y
                )
            }
            level.spawnBullet(bulletPosition, facing)
        }
    }

    private fun landedOnPlatform(platform: Platform): Boolean {
        var leftFootIn = false
        var rightFootIn = false
        var straddle = false

        // First check if Player's feet were above the platform top last frame and below the platform top this frame
        if (lastFramePosition.y - PLAYER_EYE_HEIGHT >= platform.top &&
                position.y - PLAYER_EYE_HEIGHT < platform.top) {

            //  If so, find the position of Player's left and right toes
            val leftFoot = position.x - PLAYER_STANCE_WIDTH / 2
            val rightFoot = position.x + PLAYER_STANCE_WIDTH / 2

            //  See if either of Player's toes are on the platform
            leftFootIn = platform.left < leftFoot && platform.right > leftFoot
            rightFootIn = platform.left < rightFoot && platform.right > rightFoot

            //  See if Player is straddling the platform
            straddle = platform.left > leftFoot && platform.right < rightFoot
        }

        //  Return whether or not Player had landed on the platform
        return leftFootIn || rightFootIn || straddle
    }

    private fun moveLeft(delta: Float) {
        //If we're GROUNDED and not WALKING, save the walkStartTime
        if (jumpState == Enums.JumpState.GROUNDED && walkState != Enums.WalkState.WALKING) {
            walkStartTime = TimeUtils.nanoTime()
        }
        walkState = Enums.WalkState.WALKING
        facing = Enums.Direction.LEFT
        position.x -= delta * Constants.PLAYER_MOVE_SPEED
    }

    private fun moveRight(delta: Float) {
        //If we're GROUNDED and not WALKING, save the walkStartTime
        if (jumpState == Enums.JumpState.GROUNDED && walkState != Enums.WalkState.WALKING) {
            walkStartTime = TimeUtils.nanoTime()
        }
        walkState = Enums.WalkState.WALKING
        facing = Enums.Direction.RIGHT
        position.x += delta * Constants.PLAYER_MOVE_SPEED
    }

    private fun startJump() {
        jumpState = Enums.JumpState.JUMPING
        jumpStartTime = TimeUtils.nanoTime()
        continueJump()
    }

    private fun continueJump() {
        if (jumpState == Enums.JumpState.JUMPING) {
            if (secondsSince(jumpStartTime) < Constants.MAX_JUMP_DURATION) {
                velocity.y = Constants.JUMP_SPEED
            } else {
                endJump()
            }
        }
    }

    private fun endJump() {
        if (jumpState == Enums.JumpState.JUMPING) {
            jumpState = Enums.JumpState.FALLING
        }
    }

    private fun recoilFromEnemy(direction: Enums.Direction) {
        jumpState = Enums.JumpState.RECOILING
        velocity.y = Constants.KNOCKBACK_VELOCITY.y
        if (direction == Enums.Direction.LEFT) {
            velocity.x = -Constants.KNOCKBACK_VELOCITY.x
        } else {
            velocity.x = Constants.KNOCKBACK_VELOCITY.x
        }
    }

    fun render(batch: SpriteBatch?) {
        var region: TextureRegion = Assets.instance.myPlayerAssets!!.standingRight
        if (facing == Enums.Direction.RIGHT && jumpState != Enums.JumpState.GROUNDED) {
            val walkTimeSeconds = MathUtils.nanoToSec * (TimeUtils.nanoTime() - walkStartTime)
            region = Assets.instance.myPlayerAssets!!.jumpRight.getKeyFrame(walkTimeSeconds) as TextureRegion
        } else if (facing == Enums.Direction.RIGHT && walkState == Enums.WalkState.NOT_WALKING) {
            val walkTimeSeconds = MathUtils.nanoToSec * (TimeUtils.nanoTime() - walkStartTime)
            region = Assets.instance.myPlayerAssets!!.idleRight.getKeyFrame(walkTimeSeconds) as TextureRegion
        } else if (facing == Enums.Direction.RIGHT && walkState == Enums.WalkState.WALKING) {
            //  Calculate how long we've been walking in seconds
            val walkTimeSeconds = MathUtils.nanoToSec * (TimeUtils.nanoTime() - walkStartTime)
            //  Select the correct frame from the walking right animation
            region = Assets.instance.myPlayerAssets!!.runningRightAnimation.getKeyFrame(walkTimeSeconds) as TextureRegion
        } else if (facing == Enums.Direction.LEFT && jumpState != Enums.JumpState.GROUNDED) {
            val walkTimeSeconds = MathUtils.nanoToSec * (TimeUtils.nanoTime() - walkStartTime)
            region = Assets.instance.myPlayerAssets!!.jumpLeft.getKeyFrame(walkTimeSeconds) as TextureRegion
        } else if (facing == Enums.Direction.LEFT && walkState == Enums.WalkState.NOT_WALKING) {
            val walkTimeSeconds = MathUtils.nanoToSec * (TimeUtils.nanoTime() - walkStartTime)
            region = Assets.instance.myPlayerAssets!!.idleLeft.getKeyFrame(walkTimeSeconds) as TextureRegion
        } else if (facing == Enums.Direction.LEFT && walkState == Enums.WalkState.WALKING) {
            //  Calculate how long we've been walking in seconds
            val walkTimeSeconds = MathUtils.nanoToSec * (TimeUtils.nanoTime() - walkStartTime)
            // Select the correct frame from the walking left animation
            region = Assets.instance.myPlayerAssets!!.runningLeftAnimation.getKeyFrame(walkTimeSeconds) as TextureRegion
        }
        drawTextureRegion(batch!!, region, position, Constants.PLAYER_EYE_POSITION)
    }

    private fun handlePeteCollision(levelNum: Int) {
        var peteCells = whichCellsDoesPeteCover(levelNum)
        peteCells = filterOutNonTiledCells(peteCells)
        for (cell in peteCells) {
            val cellLevelX = cell.cellX * CELL_SIZE
            val cellLevelY = cell.cellY * CELL_SIZE
            val intersection = Rectangle()
            collisionRectangle.setPosition(position.x, position.y)
            Intersector.intersectRectangles(collisionRectangle, Rectangle(cellLevelX, cellLevelY,
                    CELL_SIZE, CELL_SIZE), intersection)
            if (intersection.getHeight() < intersection.getWidth()) {
                position.y = intersection.getY() + intersection.getHeight()
                jumpState = Enums.JumpState.GROUNDED
                velocity.y = 0f
                // Zero horizontal velocity
                velocity.x = 0f
                //                position.y = intersection.getY() + Constants.PLAYER_EYE_HEIGHT;
            } else if (intersection.getWidth() < intersection.getHeight()) {
                if (intersection.getX() == position.x) {
                    position.x = intersection.getX() + intersection.getWidth()
                }
                if (intersection.getX() > position.x) {
                    position.x = intersection.getX() - PLAYER_STANCE_WIDTH
                }
            }
        }
    }

    private fun whichCellsDoesPeteCover(levelNum: Int): Array<CollisionCell> {
        val x = position.x
        val y = position.y
        val cellsCovered = Array<CollisionCell>()

        // Gets the cell location from grid of (40 x 30).
        val cellX = x / CELL_SIZE
        val cellY = y / CELL_SIZE

        // Gets the bottom left cell that Pete covers (may very well be the only cell)
        // 1.38 for example yields 1. Also, 1.78 yields 1.
        val bottomLeftCellX = MathUtils.floor(cellX)
        val bottomLeftCellY = MathUtils.floor(cellY)
        val tiledMapTileLayer = Assets.instance.getTileMap(levelNum.toString()).layers[0] as TiledMapTileLayer

        // Always add bottom left cell as this will (at least) always overlap. It may even be the only one.
        cellsCovered.add(CollisionCell(tiledMapTileLayer.getCell(bottomLeftCellX, bottomLeftCellY),
                bottomLeftCellX, bottomLeftCellY))

        // If there's overlap in both directions, therefore top right cell.
        // Add top right collision cell.
        if (cellX % 1 != 0f && cellY % 1 != 0f) {
            val topRightCellX = bottomLeftCellX + 1
            val topRightCellY = bottomLeftCellY + 1
            cellsCovered.add(CollisionCell(tiledMapTileLayer.getCell(topRightCellX, topRightCellY),
                    topRightCellX, topRightCellY))
        }

        // If there's overlap in both directions, therefore bottom right cell.
        // Add bottom right collision cell.
        if (cellX % 1 != 0f) {
            val bottomRightCellX = bottomLeftCellX + 1
            cellsCovered.add(CollisionCell(tiledMapTileLayer.getCell(bottomRightCellX, bottomLeftCellY),
                    bottomRightCellX, bottomLeftCellY))
        }

        // If there's overlap in both directions, therefore top left cell.
        // Add top left collision cell.
        if (cellX % 1 != 0f) {
            val topLeftCellY = bottomLeftCellY + 1
            cellsCovered.add(CollisionCell(tiledMapTileLayer.getCell(bottomLeftCellX, topLeftCellY),
                    bottomLeftCellX, topLeftCellY))
        }
        return cellsCovered
    }

    private fun filterOutNonTiledCells(cells: Array<CollisionCell>): Array<CollisionCell> {
        val iter: MutableIterator<CollisionCell> = cells.iterator()
        while (iter.hasNext()) {
            val collisionCell = iter.next()
            if (collisionCell.isEmpty) {
                iter.remove()
            }
        }
        return cells
    }

    companion object {
        private const val CELL_SIZE = 16f
        val TAG: String = Player::class.java.name
    }

    init {
        ammo = Constants.INITIAL_AMMO
        lives = Constants.INITIAL_LIVES
        respawn()
    }
}