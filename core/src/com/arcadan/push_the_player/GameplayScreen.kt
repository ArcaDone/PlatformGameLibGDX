package com.arcadan.push_the_player

import com.arcadan.push_the_player.overlays.GameOverOverlay
import com.arcadan.push_the_player.overlays.HudStuff
import com.arcadan.push_the_player.overlays.OnscreenControls
import com.arcadan.push_the_player.overlays.VictoryOverlay
import com.arcadan.push_the_player.util.Assets
import com.arcadan.push_the_player.util.ChaseCam
import com.arcadan.push_the_player.util.Constants
import com.arcadan.push_the_player.util.Constants.DEBUG_MODE
import com.arcadan.push_the_player.util.LevelLoader.load
import com.arcadan.push_the_player.util.Utilities.secondsSince
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.TimeUtils

class GameplayScreen(var mixtureGame: MixtureGame) : ScreenAdapter() {
    var onscreenControls: OnscreenControls? = null
    var batch: SpriteBatch? = null
    var levelEndOverlayStartTime: Long = 0
    var level: Level? = null
    var chaseCam: ChaseCam? = null
    private var hud: HudStuff? = null
    private var victoryOverlay: VictoryOverlay? = null
    private var gameOverOverlay: GameOverOverlay? = null
    override fun show() {
        batch = SpriteBatch()
        chaseCam = ChaseCam()
        hud = HudStuff()
        victoryOverlay = VictoryOverlay()
        gameOverOverlay = GameOverOverlay()
        onscreenControls = OnscreenControls()
        //  Use Gdx.input.setInputProcessor() to send touch events to onscreenControls
        //  When you're done testing, use onMobile() turn off the controls when not on a mobile device
        if (onMobile()) {
            Gdx.input.inputProcessor = onscreenControls
        }
        startNewLevel()
    }

    private fun onMobile(): Boolean {
        return Gdx.app.type == Application.ApplicationType.Android || Gdx.app.type == Application.ApplicationType.iOS
    }

    override fun resize(width: Int, height: Int) {
        hud!!.viewport.update(width, height, true)
        victoryOverlay!!.viewport.update(width, height, true)
        gameOverOverlay!!.viewport.update(width, height, true)
        level!!.viewport.update(width, height, true)
        chaseCam!!.camera = level!!.viewport.camera
        //  Update the onscreenControls.viewport
        onscreenControls!!.viewport.update(width, height, true)

        //  Call recalculateButtonPositions() on the onscreenControls
        onscreenControls!!.recalculateButtonPositions()
    }

    override fun dispose() {
        Assets.instance.dispose()
    }

    override fun render(delta: Float) {
        level!!.update(delta)
        chaseCam!!.update(delta)
        Gdx.gl.glClearColor(
                Constants.BACKGROUND_COLOR.r,
                Constants.BACKGROUND_COLOR.g,
                Constants.BACKGROUND_COLOR.b,
                Constants.BACKGROUND_COLOR.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        level!!.render(batch!!)

        //  When you're done testing, use onMobile() turn off the controls when not on a mobile device
        if (onMobile()) {
            onscreenControls!!.render(batch!!)
        }
        hud!!.render(batch!!, level!!.player.lives, level!!.player.ammo, level!!.score)
        renderLevelEndOverlays(batch)
    }

    private fun renderLevelEndOverlays(batch: SpriteBatch?) {
        if (level!!.victory) {
            if (levelEndOverlayStartTime == 0L) {
                levelEndOverlayStartTime = TimeUtils.nanoTime()
                victoryOverlay!!.init()
            }
            victoryOverlay!!.render(batch!!)
            if (secondsSince(levelEndOverlayStartTime) > Constants.LEVEL_END_DURATION) {
                levelEndOverlayStartTime = 0
                levelComplete()
            }
        }

        //  Repeat the level victory logic to display the game over screen and call levelFailed()
        if (level!!.gameOver) {
            if (levelEndOverlayStartTime == 0L) {
                levelEndOverlayStartTime = TimeUtils.nanoTime()
                gameOverOverlay!!.init()
            }
            gameOverOverlay!!.render(batch!!)
            if (secondsSince(levelEndOverlayStartTime) > Constants.LEVEL_END_DURATION) {
                levelEndOverlayStartTime = 0
                levelFailed()
            }
        }
    }

    private fun startNewLevel() {
        if (DEBUG_MODE) {
            level = Level()
            level!!.initializeDebugLevel()
        } else {
//        String levelName = Constants.LEVELS[MathUtils.random(Constants.LEVELS.length - 1)];
//        level = LevelLoader.load(levelName);
            level = load(Assets.instance.currentLevel.toInt())
        }
        chaseCam!!.camera = level!!.viewport.camera
        chaseCam!!.target = level!!.player
        onscreenControls!!.player = level!!.player
        resize(Gdx.graphics.width, Gdx.graphics.height)
    }

    private fun startLevel2() {
        if (DEBUG_MODE) {
            level = Level()
            level!!.initializeDebugLevel()
        } else {
            level = load(2)
        }
        chaseCam!!.camera = level!!.viewport.camera
        chaseCam!!.target = level!!.player
        onscreenControls!!.player = level!!.player
        resize(Gdx.graphics.width, Gdx.graphics.height)
    }

    fun levelComplete() {
        startLevel2()
    }

    fun levelFailed() {
        startNewLevel()
    }

    companion object {
        val TAG = GameplayScreen::class.java.name
    }
}