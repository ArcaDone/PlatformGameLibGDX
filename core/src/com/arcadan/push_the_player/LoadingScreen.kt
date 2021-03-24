package com.arcadan.push_the_player

import com.arcadan.push_the_player.util.Assets
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport

class LoadingScreen(private val mixtureGame: MixtureGame) : ScreenAdapter() {
    private var shapeRenderer: ShapeRenderer? = null
    private var viewport: Viewport? = null
    private var camera: OrthographicCamera? = null
    private var progress = 0f

    override fun show() {
        super.show()
        camera = OrthographicCamera()
        camera!!.position[WORLD_WIDTH / 2, WORLD_HEIGHT / 2] = 0f
        camera!!.update()
        viewport = FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera)
        shapeRenderer = ShapeRenderer()
        Assets.instance.init(mixtureGame.assetManager)
    }

    override fun render(delta: Float) {
        super.render(delta)
        update()
        clearScreen()
        draw()
    }

    override fun dispose() {
        super.dispose()
        shapeRenderer!!.dispose()
    }

    private fun update() {
        if (mixtureGame.assetManager.update()) {
            mixtureGame.screen = GameplayScreen(mixtureGame)
        } else {
            progress = mixtureGame.assetManager.progress
        }
    }

    private fun clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }

    private fun draw() {
        shapeRenderer!!.projectionMatrix = camera!!.projection
        shapeRenderer!!.transformMatrix = camera!!.view
        shapeRenderer!!.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer!!.color = Color.WHITE
        shapeRenderer!!.rect((WORLD_WIDTH - PROGRESS_BAR_WIDTH) / 2, WORLD_HEIGHT / 2 - PROGRESS_BAR_HEIGHT / 2,
                progress * PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT)
        shapeRenderer!!.end()
    }

    companion object {
        private const val WORLD_WIDTH = 640f
        private const val WORLD_HEIGHT = 640f
        private const val PROGRESS_BAR_WIDTH = 100f
        private const val PROGRESS_BAR_HEIGHT = 25f
    }
}