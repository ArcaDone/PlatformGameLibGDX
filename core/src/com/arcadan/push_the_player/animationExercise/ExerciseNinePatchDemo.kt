package com.arcadan.push_the_player.animationExercise

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport

class ExerciseNinePatchDemo : ApplicationAdapter() {
    var batch: SpriteBatch? = null
    var viewport: FitViewport? = null

    //  Add a Texture for the raw platform image
    var platformTexture: Texture? = null

    //  Add a NinePatch
    var platformNinePatch: NinePatch? = null
    override fun create() {
        batch = SpriteBatch()
        viewport = FitViewport(WORLD_SIZE, WORLD_SIZE)

        //  Load the platform texture (Look for the file in android/assets)
        platformTexture = Texture("platform.png")

        //  Initialize the NinePatch using the texture and the EDGE constant
        platformNinePatch = NinePatch(platformTexture, EDGE, EDGE, EDGE, EDGE)
    }

    override fun resize(width: Int, height: Int) {
        viewport!!.update(width, height, true)
    }

    override fun render() {
        viewport!!.apply()
        Gdx.gl.glClearColor(0f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch!!.projectionMatrix = viewport!!.camera.combined
        batch!!.begin()

        //  Draw the platform texture at TEST_SIZE_2
        batch!!.draw(platformTexture,
                WORLD_SIZE * 3 / 4 - TEST_SIZE_2 / 2, WORLD_SIZE * 1 / 4 - TEST_SIZE_2 / 2,
                TEST_SIZE_2, TEST_SIZE_2)

        //  Draw the platform texture at TEST_SIZE_1
        batch!!.draw(platformTexture,
                WORLD_SIZE * 1 / 4 - TEST_SIZE_1 / 2, WORLD_SIZE * 1 / 4 - TEST_SIZE_1 / 2,
                TEST_SIZE_1, TEST_SIZE_1)

        //  Draw the nine patch at TEST_SIZE_2
        platformNinePatch!!.draw(batch,
                WORLD_SIZE * 3 / 4 - TEST_SIZE_2 / 2, WORLD_SIZE * 3 / 4 - TEST_SIZE_2 / 2,
                TEST_SIZE_2, TEST_SIZE_2)

        //  Draw the nine patch at TEST_SIZE_1
        platformNinePatch!!.draw(batch,
                WORLD_SIZE * 1 / 4 - TEST_SIZE_1 / 2, WORLD_SIZE * 3 / 4 - TEST_SIZE_1 / 2,
                TEST_SIZE_1, TEST_SIZE_1)
        batch!!.end()
    }

    override fun dispose() {
        batch!!.dispose()
        //  Dispose of the texture
        platformTexture!!.dispose()
    }

    companion object {
        private const val WORLD_SIZE = 100f
        private const val EDGE = 8
        private const val TEST_SIZE_1 = 20f
        private const val TEST_SIZE_2 = 40f
    }
}