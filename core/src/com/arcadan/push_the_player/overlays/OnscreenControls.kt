package com.arcadan.push_the_player.overlays

import com.arcadan.push_the_player.entities.Player
import com.arcadan.push_the_player.util.Assets
import com.arcadan.push_the_player.util.Constants
import com.arcadan.push_the_player.util.Utilities.drawTextureRegion
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport

class OnscreenControls : InputAdapter() {
    @JvmField
    val viewport: Viewport

    @JvmField
    var player: Player? = null
    private val moveLeftCenter: Vector2
    private val moveRightCenter: Vector2
    private val shootCenter: Vector2
    private val jumpCenter: Vector2
    private var moveLeftPointer = 0
    private var moveRightPointer = 0
    private var jumpPointer = 0

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val viewportPosition = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
        when {
            viewportPosition.dst(shootCenter) < Constants.BUTTON_RADIUS -> {

                //  Call shoot() on Player
                player!!.shoot()
            }
            viewportPosition.dst(jumpCenter) < Constants.BUTTON_RADIUS -> {

                //  Save the jumpPointer and set myPlayer.jumpButtonPressed = true
                jumpPointer = pointer
                player!!.jumpButtonPressed = true
            }
            viewportPosition.dst(moveLeftCenter) < Constants.BUTTON_RADIUS -> {

                //  Save the moveLeftPointer, and set myPlayer.leftButtonPressed = true
                moveLeftPointer = pointer
                player!!.leftButtonPressed = true
            }
            viewportPosition.dst(moveRightCenter) < Constants.BUTTON_RADIUS -> {

                //  Save the moveRightPointer, and set myPlayer.rightButtonPressed = true
                moveRightPointer = pointer
                player!!.rightButtonPressed = true
            }
        }
        return super.touchDown(screenX, screenY, pointer, button)
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        val viewportPosition = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
        if (pointer == moveLeftPointer && viewportPosition.dst(moveRightCenter) < Constants.BUTTON_RADIUS) {

            //  Handle the case where the left button touch has been dragged to the right button
            // Inform Player that the left button is no longer pressed
            player!!.leftButtonPressed = false

            // Inform Player that the right button is now pressed
            player!!.rightButtonPressed = true

            // Zero moveLeftPointer
            moveLeftPointer = 0

            // Save moveRightPointer
            moveRightPointer = pointer
        }
        if (pointer == moveRightPointer && viewportPosition.dst(moveLeftCenter) < Constants.BUTTON_RADIUS) {

            //  Handle the case where the right button touch has been dragged to the left button
            player!!.rightButtonPressed = false
            player!!.leftButtonPressed = true
            moveRightPointer = 0
            moveLeftPointer = pointer
        }
        return super.touchDragged(screenX, screenY, pointer)
    }

    fun render(batch: SpriteBatch) {
        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        if (!Gdx.input.isTouched(jumpPointer)) {
            player!!.jumpButtonPressed = false
            jumpPointer = 0
        }

        //  If the moveLeftPointer is no longer touched, inform Player and zero moveLeftPointer
        if (!Gdx.input.isTouched(moveLeftPointer)) {
            player!!.leftButtonPressed = false
            moveLeftPointer = 0
        }

        //  Do the same for moveRightPointer
        if (!Gdx.input.isTouched(moveRightPointer)) {
            player!!.rightButtonPressed = false
            moveRightPointer = 0
        }
        drawTextureRegion(
                batch,
                Assets.instance.onscreenControlsAssets!!.moveLeft,
                moveLeftCenter,
                Constants.BUTTON_CENTER
        )
        drawTextureRegion(
                batch,
                Assets.instance.onscreenControlsAssets!!.moveRight,
                moveRightCenter,
                Constants.BUTTON_CENTER
        )
        drawTextureRegion(
                batch,
                Assets.instance.onscreenControlsAssets!!.shoot,
                shootCenter,
                Constants.BUTTON_CENTER
        )
        drawTextureRegion(
                batch,
                Assets.instance.onscreenControlsAssets!!.jump,
                jumpCenter,
                Constants.BUTTON_CENTER
        )
        batch.end()
    }

    fun recalculateButtonPositions() {
        moveLeftCenter[Constants.BUTTON_RADIUS * 3 / 4] = Constants.BUTTON_RADIUS
        moveRightCenter[Constants.BUTTON_RADIUS * 2] = Constants.BUTTON_RADIUS * 3 / 4
        shootCenter[viewport.worldWidth - Constants.BUTTON_RADIUS * 2f] = Constants.BUTTON_RADIUS * 3 / 4
        jumpCenter[viewport.worldWidth - Constants.BUTTON_RADIUS * 3 / 4] = Constants.BUTTON_RADIUS
    }

    init {
        viewport = ExtendViewport(
                Constants.ONSCREEN_CONTROLS_VIEWPORT_SIZE,
                Constants.ONSCREEN_CONTROLS_VIEWPORT_SIZE)
        moveLeftCenter = Vector2()
        moveRightCenter = Vector2()
        shootCenter = Vector2()
        jumpCenter = Vector2()
        recalculateButtonPositions()
    }
}