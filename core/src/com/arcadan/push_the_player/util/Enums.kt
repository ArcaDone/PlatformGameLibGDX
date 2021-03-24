package com.arcadan.push_the_player.util

class Enums {
    enum class Direction {
        LEFT, RIGHT
    }

    enum class JumpState {
        JUMPING, FALLING, GROUNDED, RECOILING
    }

    enum class WalkState {
        NOT_WALKING, WALKING
    }
}