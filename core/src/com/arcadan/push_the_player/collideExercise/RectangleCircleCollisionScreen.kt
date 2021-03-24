package com.arcadan.push_the_player.collideExercise

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.TimeUtils
import com.badlogic.gdx.utils.viewport.FitViewport

class RectangleCircleCollisionScreen : ScreenAdapter() {
    private val startTime: Long = TimeUtils.nanoTime()
    private val shapeRenderer: ShapeRenderer = ShapeRenderer()
    private val viewport: FitViewport
    private val rectangles: Array<Rectangle>
    private val circles: Array<OscillatingCircle>
    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        val elapsedTime = MathUtils.nanoToSec * TimeUtils.timeSinceNanos(startTime)
        viewport.apply()
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        shapeRenderer.projectionMatrix = viewport.camera.combined
        shapeRenderer.begin(ShapeType.Line)
        shapeRenderer.setColor(1f, 1f, 1f, 1f)
        for (rectangle in rectangles) {
            shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height)
        }
        for (oscillatingCircle in circles) {
            val circle = oscillatingCircle.getCurrentCircle(elapsedTime)
            var colliding = false
            for (rectangle in rectangles) {
                if (areColliding(rectangle, circle)) {
                    colliding = true
                    break
                }
            }
            if (colliding) {
                shapeRenderer.setColor(1f, 0f, 0f, 1f)
            } else {
                shapeRenderer.setColor(0f, 1f, 0f, 1f)
            }
            shapeRenderer.circle(circle.x, circle.y, circle.radius, circleSegments)
        }
        shapeRenderer.end()
    }

    private fun areColliding(rectangle: Rectangle, circle: Circle): Boolean {
        val containsACorner = circle.contains(rectangle.x, rectangle.y) ||  // Bottom left
                circle.contains(rectangle.x + rectangle.width, rectangle.y) ||  // Bottom right
                circle.contains(rectangle.x + rectangle.width, rectangle.y + rectangle.height) ||  // Top Right
                circle.contains(rectangle.x, rectangle.y + rectangle.height) // Top left
        val inHorizontalInterval = rectangle.x < circle.x && circle.x < rectangle.x + rectangle.width
        val inVerticalInterval = rectangle.y < circle.y && circle.y < rectangle.y + rectangle.height
        val inHorizontalNeighborhood = rectangle.x - circle.radius < circle.x && circle.x < rectangle.x + rectangle.width + circle.radius
        val inVerticalNeighborhood = rectangle.y - circle.radius < circle.y && circle.y < rectangle.y + rectangle.height + circle.radius
        val touchingAnEdge = inHorizontalInterval && inVerticalNeighborhood ||
                inHorizontalNeighborhood && inVerticalInterval
        return containsACorner || touchingAnEdge
    }

    companion object {
        private const val WORLD_SIZE = 100f
        private const val circleSegments = 64
    }

    init {
        viewport = FitViewport(WORLD_SIZE, WORLD_SIZE)
        rectangles = Array(arrayOf(
                Rectangle(40f, 40f, 20f, 20f),  // Middle
                Rectangle(10f, 40f, 10f, 20f),  // Left
                Rectangle(70f, 45f, 20f, 10f) // Right
        ))
        circles = Array(arrayOf(
                OscillatingCircle(50f, 65f, 7f, 0f, 40f, 3f),  // High horizontal
                OscillatingCircle(50f, 35f, 7f, 0f, 40f, 3.1f),  // Low horizontal
                OscillatingCircle(50f, 50f, 3f, MathUtils.PI / 4, 40f, 5f),  // Diagonal
                OscillatingCircle(25f, 50f, 5f, 0f, 11f, 7f)))
    }
}