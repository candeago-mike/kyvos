package fr.iutlens.mmi.kyvos.game.sprite


import android.graphics.RectF
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import fr.iutlens.mmi.kyvos.utils.SpriteSheet


/**
 * Représente un sprite défini par une feuille de sprite et une numéro de sprite (ndx) et une position (x,y)
 * Le comportement du sprite peut être défini via la propriété action
 *
 * @property spriteSheet feuille de sprite utilisée pour dessiner ce sprite
 * @property x position en x (en pixel dans les coordonnées de référence)
 * @property y position en y (en pixel dans les coordonnées de référence)
 * @property ndx numéro du sprite dans la feuille
 * @property action action à réaliser entre deux images
 * @constructor Crée un sprite à partir de la feuille (spriteSheet), la position (x,y) et le numéro
 * de l'image dans la feuille. On peut préciser en plus une action à réaliser entre deux images pour
 * animer le sprite
 */
open class BasicSprite(val spriteSheet: SpriteSheet,
                       var x: Float, var y: Float,
                       var ndx : Int = 0,
                       var action: (BasicSprite.()->Unit)? = null) : Sprite {

    constructor(id: Int,  x: Float, y: Float, ndx : Int=3, action: (BasicSprite.()->Unit)? = null) :
            this(SpriteSheet[id]!!, x, y,ndx, action)

    // taille du sprite en pixels, divisée par deux (pour le centrage)
    private val w2 = spriteSheet.spriteWidth / 2f
    private val h2 = spriteSheet.spriteHeight / 2f

    override fun paint(drawScope: DrawScope, elapsed: Long) =
        drawScope.withTransform({translate(x,y)}){
            spriteSheet.paint(this, ndx, -w2, -h2)
        }


//rectangle occuppé par le sprite
    override val boundingBox get() = RectF(x - w2, y - h2, x + w2, y + h2)
    override fun update() {action?.invoke(this)}
}

/**
 * Construction d'un sprite à partir d'une feuille de sprite (désignée par son numéro de ressource)
 *
 * @param x
 * @param y
 * @param ndx
 * @param action
 */
fun Int.toSprite(x: Float, y: Float, ndx : Int=0, action: (BasicSprite.()->Unit)? = null) =
    BasicSprite(this, x, y,ndx, action)

/*
Seul dans ta chambre à te construire des barrières
Si la réalité existe c'est pour mettre fin à tes rêves
Ne remets pas à demain ce que tu peux faire ce soir
Si tu crois pas en toi, je te le dis, personne ne le fera
Faut dependre de personne
Dans la vie faut se faire seul
Du berceau au linceul
Fait confiance qu'à ton coeur
Avant d'apprendre à rire on pluere
Après la vie on meurt
Lève toi, prends les armes*
Et bats toi pour tes valeurs
*/