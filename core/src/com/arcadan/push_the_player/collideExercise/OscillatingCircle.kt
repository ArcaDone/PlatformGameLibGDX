package com.arcadan.push_the_player.collideExercise

import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.MathUtils

class OscillatingCircle(var originX: Float, var originY: Float, var radius: Float, var angle: Float, var magnitude: Float, var period: Float) {
    fun getCurrentCircle(elapsedTime: Float): Circle {
        val x = originX + magnitude * MathUtils.cos(angle) * MathUtils.sin(MathUtils.PI2 * elapsedTime / period)
        val y = originY + magnitude * MathUtils.sin(angle) * MathUtils.sin(MathUtils.PI2 * elapsedTime / period)
        return Circle(x, y, radius)
    }
}