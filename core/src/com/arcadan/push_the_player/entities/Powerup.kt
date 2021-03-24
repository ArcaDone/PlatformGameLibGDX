package com.arcadan.push_the_player.entities

import com.arcadan.push_the_player.util.Assets
import com.arcadan.push_the_player.util.Constants
import com.arcadan.push_the_player.util.Utilities.drawTextureRegion
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2

class Powerup(
        //  Set position
        //  Add a Vector2 to hold the powerup's position
        val position: Vector2) {
    fun render(batch: SpriteBatch?) {
        //  Complete render function
        val region: TextureRegion = Assets.instance.powerupAssets!!.powerup
        drawTextureRegion(batch!!, region, position, Constants.POWERUP_CENTER)
    }
}