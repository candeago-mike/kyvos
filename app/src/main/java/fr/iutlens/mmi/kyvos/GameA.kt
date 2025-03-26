package fr.iutlens.mmi.kyvos

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import fr.iutlens.mmi.kyvos.game.Game
import fr.iutlens.mmi.kyvos.game.sprite.TiledArea
import fr.iutlens.mmi.kyvos.game.sprite.rotate
import fr.iutlens.mmi.kyvos.game.sprite.tiledArea
import fr.iutlens.mmi.kyvos.game.sprite.toMutableTileMap
import fr.iutlens.mmi.kyvos.game.transform.Constraint
import fr.iutlens.mmi.kyvos.game.transform.GenericTransform
import fr.iutlens.mmi.kyvos.utils.loadSpritesheet
import java.lang.reflect.Array.set
import kotlin.math.floor

fun makeGameA(): Game {
    val map = """
            66666666666
            61111111116
            61111111116
            61111111116
            61111111116
            61111111116
            61111111116
            61111111116
            61111111116
            61111111116
            61111111116
            61111111116
            61111111116
            611189a1116
            6111efg1116
            6111klm1116
            66666666666
            66666666666
            66666666666
            66666666666
            66666666666
            66666666666
        """.trimIndent().toMutableTileMap(
        "123456"+
                "789abc"+
                "defghi"+
                "jklmno"
    )
    val pieces = listOf(
            """
        030
        333
        """.trimIndent().toMutableTileMap(
                "123456"+
                        "789abc"+
                        "defghi"+
                        "jklmn0"
            ),
            """
        30
        33
        03
        """.trimIndent().toMutableTileMap(
                "123456"+
                        "789abc"+
                        "defghi"+
                        "jklmn0"
            ),

            """
        33
        33
        """.trimIndent().toMutableTileMap(
                "123456"+
                        "789abc"+
                        "defghi"+
                        "jklmn0"
            ),
        """
        03
        33
        30
        """.trimIndent().toMutableTileMap(
            "123456"+
                    "789abc"+
                    "defghi"+
                    "jklmn0"
        ),
        """
        30
        30
        33
        """.trimIndent().toMutableTileMap(
            "123456"+
                    "789abc"+
                    "defghi"+
                    "jklmn0"
        ),
        """
        03
        03
        33
        """.trimIndent().toMutableTileMap(
            "123456"+
                    "789abc"+
                    "defghi"+
                    "jklmn0"
        ),
        """
        3333
        """.trimIndent().toMutableTileMap(
            "123456"+
                    "789abc"+
                    "defghi"+
                    "jklmn0"
        ))
    fun codeBlock(code : Int) : Boolean {
        if (code == 23){
            return false
        }else{
            return code !in setOf(0)
        }
    }

    val tileMap = R.drawable.decor.tiledArea(map)
    var indice = 0

    var pieceArea = R.drawable.decor.tiledArea(pieces[indice])
    fun pieceSuivante(){
        indice = (indice+1).mod(pieces.size)
        pieceArea = R.drawable.decor.tiledArea(pieces[indice])
        pieceArea.x0 = 4f*tileMap.w
        pieceArea.y0 = 1f*tileMap.h
    }


    pieceArea.x0 = 4f*tileMap.w
    pieceArea.y0 = 1f*tileMap.h
    fun TiledArea.possible(x: Float, y: Float): Boolean {
        val i = floor(x / w).toInt()
        val j = floor(y / h).toInt()
        for (di in 0..<sizeX)
            for (dj in 0 ..<sizeY)
                if(codeBlock(get(di,dj))  &&
                    codeBlock(tileMap[i+di,j+dj]))
                    return false
        return true
    }

    fun TiledArea.pose() {
        val i = floor(x0 / w).toInt()
        val j = floor(y0 / h).toInt()
        // if (i !in 0 until sizeX || j !in 0 until sizeY) return false
        for (di in 0..<sizeX)
            for (dj in 0 ..<sizeY)
                if(codeBlock(get(di,dj)))
                    map[i+di,j+dj] = get(di,dj)
    }

    fun TiledArea.dash() : TiledArea{
        var compteur = 10
        for(i in pieceArea.y0.toInt()..map.sizeY){
            if(possible(x0,i.toFloat())){
                compteur+=1
            }
        }
        pieceArea.y0+=compteur.toFloat()*tileMap.h
        return pieceArea
    }


    fun gravite(x: Int, y: Int) {
        for (j in y downTo 1) { // On part du bas vers le haut
            for (i in 0 until map.sizeX) {
                if (map.get(i, j) != 0) { // Si la case contient un bloc
                    var newJ = j
                    while (newJ + 1 < map.sizeY && map.get(i, newJ + 1) == 0) { // Tant qu'il peut tomber
                        map[i, newJ + 1] = map.get(i, newJ) // Déplace le bloc vers le bas
                        map[i, newJ] = 0 // Vide l'ancienne case
                        newJ++ // Continue à descendre
                    }
                }
            }
        }
    }

    fun resetLigne(x: Int, y: Int) {
        for (i in 0..<6) {
            map[(x - i), y] = 0
        }
        gravite(x,y)
    }

    fun checkLigne() {
        for (j in 0..<map.sizeY) {
            var count = 0
            for (i in 0..<map.sizeX) {
                if (map.get(i, j) == 2) {
                    count += 1
                } else {
                    count = 0
                }
                if (count == 6) {
                    resetLigne(i, j)
                }
            }
        }
    }






    return Game(
        background = tileMap,
        spriteList = pieceArea,
        transform = GenericTransform(
            Constraint.Fill(tileMap)
        )
    ).apply {
        onRotate = {
            val x = pieceArea.x0
            val y = pieceArea.y0
            val rotatedPiece = pieceArea.rotate()
            val TiledAreaRotatedPiece = R.drawable.decor.tiledArea(rotatedPiece.toMutableTileMap())
            if (TiledAreaRotatedPiece.possible(x, y)) {
                pieceArea = R.drawable.decor.tiledArea(rotatedPiece.toMutableTileMap())
                pieceArea.x0 = x
                pieceArea.y0 = y
                spriteList = pieceArea
            }
        }
        onDash = {
            val pieceDashed = pieceArea.dash()
            spriteList = pieceDashed
            invalidate()
        }

        padAction = { (dx: Float, dy: Float) ->
            val nextX = pieceArea.x0 + dx * tileMap.w
            val nextY = pieceArea.y0 + dy * tileMap.h
            if (pieceArea.possible(nextX, nextY)) {
                pieceArea.x0 = nextX
                pieceArea.y0 = nextY
                invalidate()
            }
        }

        invalidate()
        animationDelayMs = 500

            update = {
                if (!pause) {
                    val nextY = pieceArea.y0 + tileMap.h
                    if (pieceArea.possible(pieceArea.x0, nextY)) {
                        pieceArea.y0 = nextY
                    } else {
                            pieceArea.pose()
                            pieceSuivante()
                            it.spriteList = pieceArea
                            invalidate()
                        }
                    }

                    checkLigne()
                    invalidate()
                }
            }
    }
val fontperso = FontFamily(
    Font(R.font.baijamjureemedium)
)
@Composable
fun ButtonRotation( modifier: Modifier = Modifier,onClick: () -> Unit) {
    Image(
        painter = painterResource(id = R.drawable.bouton_rotation),
        contentDescription = "Bouton Rotation",
        modifier = modifier
            .size(75.dp) // Ajuste la taille selon tes besoins
            .clickable { onClick() }
    )
}

@Composable
fun BouttonPlay( modifier: Modifier = Modifier,onClick: () -> Unit) {
    Image(
        painter = painterResource(id = R.drawable.bouton_jouer),
        contentDescription = "Bouton Jouer",
        modifier = modifier
            .size(250.dp) // Ajuste la taille selon tes besoins
            .clickable { onClick() }, // Rendre l'image cliquable
    )
}
@Composable
fun BouttonReglage( modifier: Modifier = Modifier,onClick: () -> Unit){
    Image(
        painter = painterResource(id = R.drawable.bouton_reglages),
        contentDescription = "Bouton Réglagle",
        modifier = modifier
            .size(50.dp)
            .clickable { onClick() },
    )
}
@Composable
fun pageReglage(onClick:()->Unit={}) {
    Dialog(onDismissRequest = { }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)) // Fond semi-transparent
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF578382).copy(alpha = 0.9f), shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)) // Contenu avec opacité
                    .padding(16.dp)
                    .align(Alignment.Center)
            ) {
                Text(    text = "Paramètres",
                    fontSize = 32.sp,
                    color = Color.Black,
                    fontFamily = fontperso)
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Text(    text = "Musique : ",
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontFamily = fontperso)
                    BoutonMusique(modifier = Modifier.size(24.dp))
                }
                BoutonContinue(
                    onClick = onClick
                )
            }
        }
    }
}
@Composable
fun BoutonPawh(modifier: Modifier = Modifier,onClick: () -> Unit){
    Image(
        painter = painterResource(id = R.drawable.bouton_vite),
        contentDescription = "Bouton Pawh",
        modifier = modifier
            .size(300.dp)
            .clickable { onClick() },
    )
}
@Composable
fun BoutonContinue(modifier: Modifier = Modifier,onClick: () -> Unit){
    Image(
        painter = painterResource(id = R.drawable.bouton_play),
        contentDescription = "Bouton Continuer",
        modifier = modifier
            .size(50.dp)
            .clickable { onClick() },
    )
}
@Composable
fun BoutonMusique(modifier: Modifier = Modifier){
    Image(
        painter = painterResource(id = R.drawable.note_musique),
        contentDescription = "Bouton Musique",
        modifier = modifier
            .size(50.dp)
    )
}
@Composable
fun Accueil(onClick:()->Unit={}){
    Box(Modifier.fillMaxSize()) {
        TestVideo()
        BouttonPlay(
            modifier = Modifier.align(Alignment.Center),
            onClick = onClick
        )
    }
}
//@Preview
@Composable
fun GameOver(){
    Box(Modifier
        .fillMaxSize()
        .background(Color.Black.copy(alpha = 0.5f)) // Fond semi-transparent
    ){

        Text(text = "GAME OVER",
            fontSize = 50.sp,
            color = Color.White,
            fontFamily = fontperso,
            modifier = Modifier
                .align(Alignment.Center))
            Image(
                painter = painterResource(id = R.drawable.pieces_cassees),
                contentDescription = "Bouton Musique",
                modifier = Modifier
                    .size(400.dp)
                    .align(Alignment.BottomCenter)
            )

    }
}
@Composable
fun Video(id: Int, modifier: Modifier = Modifier, isLooping: Boolean = true) {
    val uri = Uri.parse("android.resource://" + LocalContext.current.packageName + "/" + id)
    AndroidView(modifier = modifier,
        factory = { context: Context -> VideoView(context) }
    ) { view ->
        view as VideoView
        view.setOnCompletionListener { if (isLooping) it.start() }
        view.stopPlayback()
        view.setVideoURI(uri)
        view.start()
    }
}

@Composable
fun TestVideo() {
    Video(R.raw.fond_kyvos, Modifier.fillMaxSize())
}


enum class GameState{HOME,PLAYING,REGLAGE,PERDU}

@Preview
@SuppressLint("SuspiciousIndentation")
@Composable
fun GameAPreview() {
    var gameState by remember { mutableStateOf(GameState.HOME) }

    LocalContext.current.loadSpritesheet(R.drawable.decor, 6, 4, 1)
    LocalContext.current.loadSpritesheet(R.drawable.perso, 6, 4)
    val game = makeGameA()
        Box(Modifier.fillMaxSize()) {
            game.View(
                modifier = Modifier
                    .fillMaxSize()
                    .background(androidx.compose.ui.graphics.Color.Black)
            )
            val action1 = game.padAction ?: return@Box
            BouttonReglage(
                modifier = Modifier
                    .size(75.dp)
                    .padding(16.dp)
                    .align(Alignment.TopEnd),
                onClick = {
                    println("clique")
                }
            )
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                contentAlignment = Alignment.Center // Centre le bouton dans le Pad
            ) {
                Pad(
                    Modifier.matchParentSize(), // S'assure que le Pad occupe tout l'espace du Box
                    action = action1
                )

                ButtonRotation(
                    modifier = Modifier
                        .size(75.dp) // Ajuste la taille du bouton
                        .align(Alignment.Center) // Centre le bouton dans le Box
                        .offset(y = (-25).dp), // Décale légèrement vers le haut
                    onClick = {
                        game.onRotate?.let { it(game, Offset.Zero) }
                        game.invalidate()
                    }
                )
            }
            BoutonPawh(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(140.dp)
                    .padding(32.dp)
                    .offset(y = (-50).dp, x = (-20).dp), // Décale légèrement vers le haut
                onClick = {
                    game.onDash?.let { it(game,Offset.Zero) }
                    game.invalidate()
                }
            )
        }
    }

/*
Courage mike, cette pièce ne va plus arriver dans le noir !
Votre va fonctionner et ça va être le GOTY
*/