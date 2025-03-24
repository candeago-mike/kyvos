package fr.iutlens.mmi.kyvos.utils

import android.content.Context
import android.graphics.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import java.util.HashMap

/**
 * SpriteSheet représente une feuille de sprites, c'est à dire une image découpée
 * en plusieurs cases, que l'on peut utiliser comme autant d'images distinctes en y
 * accédant par un numéro
 *
 * @property bitmap image à découper
 * @property sizeX nombre de colonnes
 * @property sizeY nombre de lignes
 * @constructor Create empty Sprite sheet
 */
class SpriteSheet(val bitmap: Bitmap, val sizeX: Int, val sizeY: Int, val padding: Int) {

    private val _rawSpriteWidth = bitmap.width/sizeX
    private val _rawSpriteHeight = bitmap.height/sizeY
    /**
     * SpriteWidth : largeur d'un sprite
     */
    val spriteWidth = _rawSpriteWidth-padding*2

    /**
     * SpriteHeight : hauteur d'u sprite
     */
    val spriteHeight = _rawSpriteHeight-padding*2

    /**
     * sprite : tableau des sprites
     */
    private val sprite: Array<Bitmap?> = Array(sizeX * sizeY) {
        val i = it % sizeX
        val j = it / sizeX
        createCroppedBitmap(bitmap,
            i * _rawSpriteWidth+padding,
            j * _rawSpriteHeight+padding,
            spriteWidth, spriteHeight)
    }

    /**
     * Obtient un sprite de la feuille
     *
     * @param ndx position du sprite dans la feuille
     */
    operator fun get(ndx: Int) = sprite[ndx]

    /**
     * Affiche l'image
     *
     * @param drawScope contexte de dessin
     * @param ndx numéro de l'image dans la feuille
     * @param x abscisse
     * @param y ordonnée
     */
    fun paint(drawScope: DrawScope, ndx: Int, x: Float, y: Float) {
        get(ndx)?.let{drawScope.drawImage(it.asImageBitmap(),  Offset(x,y))}
    }

    companion object {
        private val map =  HashMap<Int, SpriteSheet>()

        fun load(id: Int, sizeX: Int, sizeY: Int, padding : Int , context: Context) {
            loadImage(context, id)?.let {
                map[id] = SpriteSheet(it, sizeX, sizeY,padding)
            } ?: throw NoSuchElementException("Image resource not fount (id=$id)")
        }

        operator fun get(id: Int): SpriteSheet? {
            return map[id]
        }
    }
}

/**
 * Charge et découpe une image en feuille de sprite
 *
 * @param id identifiant de la ressource image
 * @param sizeX nombre de colonnes
 * @param sizeY nombre de lignes
 * @param padding nombre de pixels à ignorer sur chaque bord de sprite
 */
fun Context.loadSpritesheet(id: Int, sizeX: Int, sizeY: Int, padding: Int = 0) =
    SpriteSheet.load(id, sizeX, sizeY, padding,this)