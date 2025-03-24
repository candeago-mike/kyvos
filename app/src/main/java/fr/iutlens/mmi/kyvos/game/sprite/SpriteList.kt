package fr.iutlens.mmi.kyvos.game.sprite

import android.graphics.RectF
import androidx.compose.ui.graphics.drawscope.DrawScope

operator fun<T: Sprite> Iterable<T>.get(x: Float, y: Float) = firstOrNull { it.boundingBox.contains(x,y) }
fun<T: Sprite> Iterable<T>.paint(drawScope: DrawScope, frameCount: Long){
    for(sprite in this) sprite.paint(drawScope,frameCount)
}

fun<T: Sprite> Iterable<T>.boundingBox() = this.map{it.boundingBox}
    .reduce { result, box -> result.apply { union(box)} }

fun<T: Sprite> Iterable<T>.update() {for(sprite in this) sprite.update()}

/**
 * Liste de sprites, utilisable à la fois comme un sprite et une liste
 *
 * @property list
 * @constructor Crée une SpriteList à partir d'un liste de sprite
 */
open class SpriteList<T: Sprite>(open val list: Iterable<T>) : Sprite, Iterable<T> by list{
    override fun paint(drawScope: DrawScope, elapsed: Long)  = list.paint(drawScope,elapsed)
    override val boundingBox: RectF get() = list.boundingBox()
    override fun update()  = list.update()
}

/**
 * Liste de sprite modifiable
 *
 * @property list
 * @constructor Create empty Mutable sprite list
 */
class MutableSpriteList<T: Sprite>(override val list: MutableList<T>) : SpriteList<T>(list), MutableList<T> by list {
    override fun iterator() = list.iterator()
}

fun<T: Sprite> Iterable<T>.asSpriteList() = SpriteList(this)
fun<T: Sprite> MutableList<T>.asMutableSpriteList() = MutableSpriteList(this)

/**
 * Crée une liste de sprite (SpriteList) à partir du ou des sprites passés en paramètre
 *
 * @param sprite
 */
fun<T: Sprite> spriteListOf(vararg sprite : T) = SpriteList(sprite.toList())

/**
 * Crée une liste de sprite modifiable (MutableSpriteList) à partir du ou des sprites passés en paramètre
 *
 * @param sprite
 */
fun<T: Sprite> mutableSpriteListOf(vararg sprite : T) = MutableSpriteList(sprite.toMutableList())