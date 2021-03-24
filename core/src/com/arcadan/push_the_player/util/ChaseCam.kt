package com.arcadan.push_the_player.util

import com.arcadan.push_the_player.entities.Player
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Camera

class ChaseCam {
    @JvmField
    var camera: Camera? = null
    @JvmField
    var target: Player? = null
    private var following = true
    fun update(delta: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            following = !following
        }
        if (following) {
            camera!!.position.x = target!!.position.x
            camera!!.position.y = target!!.position.y
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                camera!!.position.x -= delta * Constants.CHASE_CAM_MOVE_SPEED
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                camera!!.position.x += delta * Constants.CHASE_CAM_MOVE_SPEED
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                camera!!.position.y += delta * Constants.CHASE_CAM_MOVE_SPEED
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                camera!!.position.y -= delta * Constants.CHASE_CAM_MOVE_SPEED
            }
        }
    }
}