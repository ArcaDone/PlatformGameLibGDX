package com.arcadan.push_the_player.util

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2

object Constants {
    @JvmField
    var DEBUG_MODE = false

    // World/Camera
    @JvmField
    val BACKGROUND_COLOR: Color = Color.SKY
    // Set a WORLD_SIZE
    /**
     * We'll draw our sprites at their natural size, so this is the number of pixels of our Pixel
     * art that will fit on the screen. We're going to use this size to initialize both dimensions
     * of an ExtendViewport, and we'll run the game in landscape mode, so this will really end up
     * specifying the height of the world. We recommend 128.
     */
    const val WORLD_SIZE = 160f
    const val KILL_PLANE = -100f
    const val GRAVITY = WORLD_SIZE / 10
    const val CHASE_CAM_MOVE_SPEED = 128f
    const val TEXTURE_ATLAS = "images/arcadan.pack.atlas"

    //Player
    @JvmField
    val PLAYER_EYE_POSITION = Vector2(16f, 17f)
    const val PLAYER_EYE_HEIGHT = 16.0f
    const val PLAYER_STANCE_WIDTH = 21.0f
    const val PLAYER_HEIGHT = 23.0f
    const val PLAYER_MOVE_SPEED = WORLD_SIZE / 2

    //  Add constant for offset between Player's eye position, and the barrel of her cannon
    // Looks to be about (12, -7) to me
    @JvmField
    val PLAYER_CANNON_OFFSET = Vector2(12f, -7f)
    @JvmField
    val KNOCKBACK_VELOCITY = Vector2(200f, 200f)
    const val JUMP_SPEED = 2.3f * WORLD_SIZE

    // Meaning how long you can hold the jump key to continue to jump higher. 0.15 seconds works well
    const val MAX_JUMP_DURATION = .1f
    const val NEW_STANDING_RIGHT = "adventurer-idle-00-right"
    const val NEW_STANDING_LEFT = "adventurer-idle-00-left"
    const val JUMPING_RIGHT_0 = "adventurer-jump-00-right"
    const val JUMPING_RIGHT_1 = "adventurer-jump-01-right"
    const val JUMPING_RIGHT_2 = "adventurer-jump-02-right"
    const val JUMPING_RIGHT_3 = "adventurer-jump-03-right"
    const val JUMPING_LEFT_0 = "adventurer-jump-00-left"
    const val JUMPING_LEFT_1 = "adventurer-jump-01-left"
    const val JUMPING_LEFT_2 = "adventurer-jump-02-left"
    const val JUMPING_LEFT_3 = "adventurer-jump-03-left"
    const val IDLE_RIGHT_1 = "adventurer-idle-00-right"
    const val IDLE_RIGHT_2 = "adventurer-idle-01-right"
    const val IDLE_LEFT_1 = "adventurer-idle-00-left"
    const val IDLE_LEFT_2 = "adventurer-idle-01-left"
    const val WALKING_RIGHT_1 = "adventurer-run3-00-right"
    const val WALKING_RIGHT_2 = "adventurer-run3-01-right"
    const val WALKING_RIGHT_3 = "adventurer-run3-02-right"
    const val WALKING_RIGHT_4 = "adventurer-run3-03-right"
    const val WALKING_RIGHT_5 = "adventurer-run3-04-right"
    const val WALKING_RIGHT_6 = "adventurer-run3-05-right"
    const val WALKING_LEFT_1 = "adventurer-run3-00-left"
    const val WALKING_LEFT_2 = "adventurer-run3-01-left"
    const val WALKING_LEFT_3 = "adventurer-run3-02-left"
    const val WALKING_LEFT_4 = "adventurer-run3-03-left"
    const val WALKING_LEFT_5 = "adventurer-run3-04-left"
    const val WALKING_LEFT_6 = "adventurer-run3-05-left"

    // Something like 0.25 works well.
    const val IDLE_LOOP_DURATION = 0.45f
    const val JUMP_LOOP_DURATION = 0.1f
    const val RUN_LOOP_DURATION = 0.09f

    //  Add constant for Player's initial ammo
    const val INITIAL_AMMO = 10
    const val INITIAL_LIVES = 3

    // Platform
    //Add String constant for the name of the platform sprite
    const val PLATFORM_SPRITE = "platform"

    //Add a constant holding the size of the stretchable edges in the platform 9 patch
    // (8 pixels)
    const val PLATFORM_EDGE = 8

    // Enemy
    const val ENEMY_SPRITE = "enemy"
    @JvmField
    val ENEMY_CENTER = Vector2(14f, 22f)
    const val ENEMY_MOVEMENT_SPEED = 10f
    const val ENEMY_BOB_AMPLITUDE = 2f
    const val ENEMY_BOB_PERIOD = 3.0f
    const val ENEMY_COLLISION_RADIUS = 15f

    //  Add constant for enemy health (5 works well)
    const val ENEMY_HEALTH = 5

    //  Add constant for enemy hit detection radius (17 works well)
    const val ENEMY_SHOT_RADIUS = 17f

    //Bullets
    const val BULLET_RIGHT = "bullet-right"
    const val BULLET_LEFT = "bullet-left"
    @JvmField
    val BULLET_CENTER = Vector2(3f, 2f)
    const val BULLET_MOVE_SPEED = 150f

    //  Note the constants we've added for the explosions
    const val EXPLOSION_LARGE = "explosion-large"
    const val EXPLOSION_MEDIUM = "explosion-medium"
    const val EXPLOSION_SMALL = "explosion-small"
    @JvmField
    val EXPLOSION_CENTER = Vector2(8f, 8f)
    const val EXPLOSION_DURATION = 0.5f

    //  Note the constants we've added for the powerups
    const val POWERUP_SPRITE = "powerup"
    @JvmField
    val POWERUP_CENTER = Vector2(7f, 5f)

    //  Add constant for how much ammo a powerup contains
    const val POWERUP_AMMO = 10

    // Exit Portal
    const val EXIT_PORTAL_SPRITE_1 = "exit-portal-1"
    const val EXIT_PORTAL_SPRITE_2 = "exit-portal-2"
    const val EXIT_PORTAL_SPRITE_3 = "exit-portal-3"
    const val EXIT_PORTAL_SPRITE_4 = "exit-portal-4"
    const val EXIT_PORTAL_SPRITE_5 = "exit-portal-5"
    const val EXIT_PORTAL_SPRITE_6 = "exit-portal-6"
    @JvmField
    val EXIT_PORTAL_CENTER = Vector2(31f, 31f)
    const val EXIT_PORTAL_RADIUS = 28f

    // A tenth of a second gives a nice animation
    const val EXIT_PORTAL_FRAME_DURATION = 0.1f

    // Something like (200, 200) is fine
    val EXIT_PORTAL_DEFAULT_LOCATION = Vector2(200f, 200f)

    // HUD
    const val HUD_VIEWPORT_SIZE = 480f
    const val HUD_MARGIN = 20f
    const val HUD_AMMO_LABEL = "Ammo: "
    const val HUD_SCORE_LABEL = "Score: "

    // Onscreen Controls
    //  Review the constants we've added for the on-screen controls
    const val ONSCREEN_CONTROLS_VIEWPORT_SIZE = 200f
    const val MOVE_LEFT_BUTTON = "button-move-left"
    const val MOVE_RIGHT_BUTTON = "button-move-right"
    const val SHOOT_BUTTON = "button-shoot"
    const val JUMP_BUTTON = "button-jump"
    @JvmField
    val BUTTON_CENTER = Vector2(15f, 15f)
    const val BUTTON_RADIUS = 32f

    // Victory/Game Over screens
    const val LEVEL_END_DURATION = 2f
    const val VICTORY_MESSAGE = "You are the Winrar!"
    const val GAME_OVER_MESSAGE = "Game Over, Taaaac"
    const val EXPLOSION_COUNT = 500
    const val ENEMY_COUNT = 200
    const val FONT_FILE = "font/header.fnt"

    // Scoring
    const val ENEMY_KILL_SCORE = 100
    const val ENEMY_HIT_SCORE = 25
    const val POWERUP_SCORE = 50
}