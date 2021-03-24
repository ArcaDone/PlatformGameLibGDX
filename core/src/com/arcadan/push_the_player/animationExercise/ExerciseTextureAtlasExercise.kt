package com.arcadan.push_the_player.animationExercise

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetErrorListener
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion

class ExerciseTextureAtlasExercise : ApplicationAdapter(), AssetErrorListener {
    //  Add an AssetManager
    private var assetManager: AssetManager? = null
    var batch: SpriteBatch? = null

    //  Add an AtlasRegion to hold the standing right sprite
    var standingRight: AtlasRegion? = null
    override fun create() {
        batch = SpriteBatch()

        //  Initialize your AssetManager
        assetManager = AssetManager()

        //  Set this as the AssetManager's error listener
        assetManager!!.setErrorListener(this)

        //  tell the AssetManager to load the TextureAtlas with name ATLAS
        assetManager!!.load(ATLAS, TextureAtlas::class.java)

        //  Call finishLoading() on your AssetManager
        assetManager!!.finishLoading()

        //  Get the TextureAtlas from the asset manager
        val atlas = assetManager!!.get<TextureAtlas>(ATLAS)

        //  Populate your AtlasRegion using findRegion() on your Atlas
        standingRight = atlas.findRegion(STANDING_RIGHT)
    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch!!.begin()

        // Don't do this! This results in drawing the entire texture atlas, not just the region you're interested in
        // batch.draw(standingRight.getTexture(), 0, 0);


        //  Draw the standing right AtlasRegion
        // Hint, you need to use the somewhat horrific version of draw with signature:
        // draw (Texture texture, float x, float y, float originX, float originY, float width, float height, float scaleX,
        // float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY)
        batch!!.draw(
                standingRight!!.texture, 0f, 0f, 0f, 0f,
                standingRight!!.regionWidth.toFloat(),
                standingRight!!.regionHeight.toFloat(), 1f, 1f, 0f,
                standingRight!!.regionX,
                standingRight!!.regionY,
                standingRight!!.regionWidth,
                standingRight!!.regionHeight,
                false,
                false)
        batch!!.end()
    }

    override fun error(asset: AssetDescriptor<*>, throwable: Throwable) {
        Gdx.app.error(TAG, "Couldn't load asset: " + asset.fileName, throwable)
    }

    override fun dispose() {
        batch!!.dispose()
        //  Dispose of the AssetManager
        assetManager!!.dispose()
    }

    companion object {
        val TAG = ExerciseTextureAtlasExercise::class.java.name
        private const val ATLAS = "images/arcadan.pack.atlas"
        private const val STANDING_RIGHT = "standing-right"
    }
}