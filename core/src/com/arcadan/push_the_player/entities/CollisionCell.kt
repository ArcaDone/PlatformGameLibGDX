package com.arcadan.push_the_player.entities

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer

internal class CollisionCell(private val cell: TiledMapTileLayer.Cell?, val cellX: Int, val cellY: Int) {
    val isEmpty: Boolean
        get() = cell == null
}