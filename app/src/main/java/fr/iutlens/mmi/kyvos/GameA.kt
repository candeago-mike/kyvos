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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import fr.iutlens.mmi.kyvos.game.Game
import fr.iutlens.mmi.kyvos.game.sprite.SubMap
import fr.iutlens.mmi.kyvos.game.sprite.TiledArea
import fr.iutlens.mmi.kyvos.game.sprite.rotate
import fr.iutlens.mmi.kyvos.game.sprite.tiledArea
import fr.iutlens.mmi.kyvos.game.sprite.toMutableTileMap
import fr.iutlens.mmi.kyvos.game.transform.Constraint
import fr.iutlens.mmi.kyvos.game.transform.GenericTransform
import fr.iutlens.mmi.kyvos.utils.loadSpritesheet
import kotlinx.coroutines.delay
import java.lang.reflect.Array.set
import kotlin.math.floor

fun makeGameA(perdu : ()->Unit): Game { //13sur13
    val map = """
            111111111111111111111111111
            111111111111111111111111111
            111111111111111111111111111
            111111111111111111111111111
            111111111111111111111111111
            111111111111111111111111111
            111111111111111111111111111
            111111111111111111111111111
            111111111111111111111111111
            111111111111111111111111111
            111111111111111111111111111
            111111111111111111111111111
            11111111111189a111111111111
            111111111111efg111111111111
            111111111111klm111111111111
            111111111111111111111111111
            111111111111111111111111111
            111111111111111111111111111
            111111111111111111111111111
            111111111111111111111111111
            111111111111111111111111111
            111111111111111111111111111
            111111111111111111111111111
            111111111111111111111111111
            111111111111111111111111111
            111111111111111111111111111
            111111111111111111111111111
        """.trimIndent().toMutableTileMap(
        "123456"+
                "789abc"+
                "defghi"+
                "jklmno"
    )


    val pieces = listOf(
            """
        040
        444
        """.trimIndent().toMutableTileMap(
                "123456"+
                        "789abc"+
                        "defghi"+
                        "jklmn0"
            ),
            """
        40
        44
        04
        """.trimIndent().toMutableTileMap(
                "123456"+
                        "789abc"+
                        "defghi"+
                        "jklmn0"
            ),
        """
        44
        44
        """.trimIndent().toMutableTileMap(
                "123456"+
                        "789abc"+
                        "defghi"+
                        "jklmn0"
            ),
        """
        04
        44
        40
        """.trimIndent().toMutableTileMap(
            "123456"+
                    "789abc"+
                    "defghi"+
                    "jklmn0"
        ),
        """
        40
        40
        44
        """.trimIndent().toMutableTileMap(
            "123456"+
                    "789abc"+
                    "defghi"+
                    "jklmn0"
        ),
        """
        04
        04
        44
        """.trimIndent().toMutableTileMap(
            "123456"+
                    "789abc"+
                    "defghi"+
                    "jklmn0"
        ),
        """
        4
        4
        4
        4
        """.trimIndent().toMutableTileMap(
            "123456"+
                    "789abc"+
                    "defghi"+
                    "jklmn0"
        ),
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
        3
        3
        3
        3
        """.trimIndent().toMutableTileMap(
            "123456"+
                    "789abc"+
                    "defghi"+
                    "jklmn0"
        ),"""
        050
        555
        """.trimIndent().toMutableTileMap(
            "123456"+
                    "789abc"+
                    "defghi"+
                    "jklmn0"
        ),
        """
        50
        55
        05
        """.trimIndent().toMutableTileMap(
            "123456"+
                    "789abc"+
                    "defghi"+
                    "jklmn0"
        ),

        """
        55
        55
        """.trimIndent().toMutableTileMap(
            "123456"+
                    "789abc"+
                    "defghi"+
                    "jklmn0"
        ),
        """
        05
        55
        50
        """.trimIndent().toMutableTileMap(
            "123456"+
                    "789abc"+
                    "defghi"+
                    "jklmn0"
        ),
        """
        50
        50
        55
        """.trimIndent().toMutableTileMap(
            "123456"+
                    "789abc"+
                    "defghi"+
                    "jklmn0"
        ),
        """
        05
        05
        55
        """.trimIndent().toMutableTileMap(
            "123456"+
                    "789abc"+
                    "defghi"+
                    "jklmn0"
        ),
        """
        5
        5
        5
        5
        """.trimIndent().toMutableTileMap(
            "123456"+
                    "789abc"+
                    "defghi"+
                    "jklmn0"
        ),"""
        020
        222
        """.trimIndent().toMutableTileMap(
            "123456"+
                    "789abc"+
                    "defghi"+
                    "jklmn0"
        ),
        """
        20
        22
        02
        """.trimIndent().toMutableTileMap(
            "123456"+
                    "789abc"+
                    "defghi"+
                    "jklmn0"
        ),

        """
        22
        22
        """.trimIndent().toMutableTileMap(
            "123456"+
                    "789abc"+
                    "defghi"+
                    "jklmn0"
        ),
        """
        02
        22
        20
        """.trimIndent().toMutableTileMap(
            "123456"+
                    "789abc"+
                    "defghi"+
                    "jklmn0"
        ),
        """
        20
        20
        22
        """.trimIndent().toMutableTileMap(
            "123456"+
                    "789abc"+
                    "defghi"+
                    "jklmn0"
        ),
        """
        02
        02
        22
        """.trimIndent().toMutableTileMap(
            "123456"+
                    "789abc"+
                    "defghi"+
                    "jklmn0"
        ),
        """
        2
        2
        2
        2
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

    val sousMap_rotation_gauche = SubMap(map, 9, 15) { x, y -> (-y+26) to (x+9) }
    val sousMap_rotation180 = SubMap(map, 9, 15) { x, y -> (8 - x + 9) to (14 - y + 12) }
    val sousMap_rotaiondroit = SubMap(map, 9, 15) { x, y -> (y) to (-x+17) }
    val sousMap_vertcal = SubMap(map, 9, 15) { x, y -> (x+9) to y }


    val tableau_map = listOf(sousMap_vertcal,sousMap_rotaiondroit,sousMap_rotation180,sousMap_rotation_gauche)
    var indice_piece = 0
    var indice_map = 0
    var angle_cible = 0

    var tileMap = R.drawable.decor.tiledArea(tableau_map[indice_map])
    val tileMap_affiche = R.drawable.decor.tiledArea(map)
    tileMap_affiche.x0=-9f*tileMap_affiche.w


    var pieceArea = R.drawable.decor.tiledArea(pieces[indice_piece])
    fun pieceSuivante() {
        indice_piece = (0 until pieces.size).random() // Génère un indice aléatoire entre 0 et pieces.size - 1
        pieceArea = R.drawable.decor.tiledArea(pieces[indice_piece])
        pieceArea.x0 = 3f * tileMap.w
    }


    fun Mapsuivante(){
        indice_map = (indice_map+1).mod(tableau_map.size)
        tileMap = R.drawable.decor.tiledArea(tableau_map[indice_map])
    }

    pieceArea.x0 = 3f*tileMap.w
    fun TiledArea.possible(x: Float, y: Float): Boolean {
        val i = floor(x / w).toInt()
        val j = floor(y / h).toInt()
        for (di in 0..<sizeX)
            for (dj in 0 ..<sizeY)
                if(codeBlock(get(di,dj))  &&
                    codeBlock(tileMap[i+di,j+dj]))
                    return false
                else if (i+di !in 0 until tableau_map[indice_map].sizeX || j+dj !in 0 until tableau_map[indice_map].sizeY) return false
        return true
    }

    fun TiledArea.pose() {
        val i = floor(x0 / w).toInt()
        val j = floor(y0 / h).toInt()
        // if (i !in 0 until sizeX || j !in 0 until sizeY) return false
        for (di in 0..<sizeX)
            for (dj in 0 ..<sizeY)
                if(codeBlock(get(di,dj)))
                    tableau_map[indice_map][i+di,j+dj] = get(di,dj)
    }

    fun TiledArea.dash(): TiledArea {
        var compteur = 0
        val j = floor(y0 / h).toInt()

        for (i in (j + pieceArea.sizeY -1) until tableau_map[indice_map].sizeY) {
            if (possible(pieceArea.x0, i.toFloat() * h)) {
                compteur++
            } else {
                break
            }
        }

        pieceArea.y0 += compteur * tileMap.h
        return pieceArea
    }



    fun gravite(x: Int, y: Int) {
        for (j in y downTo 1) { // On commence à la ligne supprimée et on descend
            for (i in 0..<tableau_map[indice_map].sizeX) {
                tableau_map[indice_map][i, j] = tableau_map[indice_map][i, j - 1] // Décale chaque bloc vers le bas
            }
        }
        // Remplit la première ligne avec des 0 (vide)
        for (i in 0..<tableau_map[indice_map].sizeX) {
            tableau_map[indice_map][i, 0] = 0
        }
    }

    fun resetLigne(x: Int, y: Int) {
        for (i in 0..<9) {
            tableau_map[indice_map][(x - i), y] = 0
        }
        gravite(x,y)

    }

    fun checkLigne() {
        for (j in 0..<tableau_map[indice_map].sizeY) {
            var count = 0
            for (i in 0..<tableau_map[indice_map].sizeX) {
                if (tableau_map[indice_map].get(i, j) == 1 || tableau_map[indice_map].get(i, j) == 2 || tableau_map[indice_map].get(i, j) == 3 || tableau_map[indice_map].get(i, j) == 4) {
                    count += 1
                } else {
                    count = 0
                }
                if (count == 9) {
                    resetLigne(i, j)
                }
            }
        }
    }

    return Game(
        background = tileMap_affiche,
        spriteList = pieceArea,
        transform = GenericTransform(
            Constraint.Fill(tileMap)
        ),
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
            if (!pause) {
                val nextX = pieceArea.x0 + dx * tileMap.w
                val nextY = pieceArea.y0 + dy.coerceAtLeast(0f) * tileMap.h
                if (pieceArea.possible(nextX, nextY)) {
                    pieceArea.x0 = nextX
                    pieceArea.y0 = nextY
                    invalidate()
                }
            }
        }

        invalidate()
        animationDelayMs = 10

            update = {
                if (!pause) {
                    val nextY = pieceArea.y0 + tileMap.h*0.025f
                    val nextY_test = pieceArea.y0 + 1f*tileMap.h
                    if (angle_cible != tileMap_affiche.angle.toInt()) {
                        tileMap_affiche.angle = (tileMap_affiche.angle+5f)%360
                    }

                    if (pieceArea.possible(4f * tileMap.w, 1f * tileMap.h)) {
                        if (pieceArea.possible(pieceArea.x0, nextY_test)) {
                            pieceArea.y0 = nextY
                        } else {
                            pieceArea.pose()
                            checkLigne()
                            pieceSuivante()
                            it.spriteList = pieceArea

                            Mapsuivante()
                            angle_cible = (angle_cible+90)%360
                            invalidate()
                        }
                    } else {
                        perdu()
                    }
                }
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
            .size(75.dp)
            .clickable { onClick() }
    )
}

@Composable
fun BouttonPlay( modifier: Modifier = Modifier,onClick: () -> Unit) {
    Image(
        painter = painterResource(id = R.drawable.bouton_jouer),
        contentDescription = "Bouton Jouer",
        modifier = modifier
            .size(250.dp)
            .clickable { onClick() },
    )
}

@Composable
fun ButtonAide( modifier: Modifier = Modifier,onAide: () -> Unit){
    Image(
        painter = painterResource(id = R.drawable.bouton_aide),
        contentDescription = "Help Button",
        modifier = modifier
            .size(50.dp)
            .clickable { onAide() },
    )}

@Composable
fun BouttonReglage( modifier: Modifier = Modifier,onClick: () -> Unit){
    Image(
        painter = painterResource(id = R.drawable.bouton_reglages),
        contentDescription = "Bouton Réglage",
        modifier = modifier
            .size(50.dp)
            .clickable { onClick() },
    )
}

@Preview
@Composable
fun pageReglage(onClick:()->Unit={},onMute:()->Unit={}) {
    Dialog(onDismissRequest = { }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0A0B0C).copy(alpha = 0.5f))
                .padding(16.dp)
        ) {

            BoutonFermer(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick =onClick
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFF578382).copy(alpha = 0.9f),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                    ) // Contenu avec opacité
                    .padding(16.dp)
                    .align(Alignment.Center)
            ) {
                Text(
                    text = "Parameters",
                    fontSize = 32.sp,
                    color = Color(0xFF0A0B0C),
                    fontFamily = fontperso
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Text(text = "Music : ",
                        fontSize = 16.sp,
                        color =Color(0xFF0A0B0C),
                        fontFamily = fontperso)
                    BoutonMusique(
                        onClick=onMute,
                        modifier = Modifier.size(24.dp)
                    )
                }
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
fun BoutonFermer(modifier: Modifier = Modifier,onClick: () -> Unit){
    Image(
        painter = painterResource(id = R.drawable.bouton_x),
        contentDescription = "Close the parameters",
        modifier = modifier
            .size(50.dp)
            .clickable { onClick() },
    )
}
@Composable
fun BoutonMusique(modifier: Modifier = Modifier, onClick: () -> Unit){
    Image(
        painter = painterResource(id = R.drawable.note_musique),
        contentDescription = "Bouton Musique",
        modifier = modifier
            .size(50.dp)
            .clickable { onClick() }
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

@Composable
fun BoutonCredits(modifier: Modifier = Modifier,onCredits: () -> Unit){
    Image(
        painter = painterResource(id = R.drawable.bouton_credits),
        contentDescription = "Bouton Credits",
        modifier = modifier
            .size(150.dp)
            .clickable { onCredits() }
            .padding(top = 100.dp)
    )
}

@Composable
fun BoutonYes(modifier: Modifier = Modifier,onClick: () -> Unit){
    Image(
        painter = painterResource(id = R.drawable.bouton_yes),
        contentDescription = "Bouton Yes",
        modifier = modifier
            .size(80.dp)
            .clickable { onClick() },
    )
}

@Composable
fun BoutonNo(modifier: Modifier = Modifier,onClick: () -> Unit){
    Image(
        painter = painterResource(id = R.drawable.bouton_no),
        contentDescription = "Bouton No",
        modifier = modifier
            .size(80.dp)
            .clickable { onClick() },
    )
}


@Composable
fun Credits(modifier: Modifier = Modifier, onClick: () -> Unit={}){
    Box(Modifier
        .fillMaxSize()
        .background(Color(0xFF0A0B0C),)
    ) {
        BoutonFermer(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick =onClick
        )
        Text(
            text = "Credits",
            fontSize = 55.sp,
            color = Color(0xFF578382),
            fontFamily = fontperso,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 50.dp)
        )
        Text(
            text = "Kyvos, a Tetris-inspired game by Cléa Portolan and Mike Candeago",
            fontSize = 18.sp,
            color = Color(0xFFFFF9F0),
            fontFamily = fontperso,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(bottom = 300.dp)
                .padding(horizontal = 25.dp)
        )
        Text(
            text = "Game development : Mike Candeago \n Design & UI : Cléa Portolan \n Music & Sound Effects : Mattéo Portolan \n Special thanks to Vincent Dubois",
            fontSize = 18.sp,
            color = Color(0xFFFFF9F0),
            fontFamily = fontperso,
            lineHeight = 40.sp,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(horizontal = 25.dp)
        )
        Text(
            text = "Powered by Android Studio",
            fontSize = 18.sp,
            color = Color(0xFFFFF9F0),
            fontFamily = fontperso,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 250.dp)
                .padding(horizontal = 25.dp)

        )
        Text(
            text = "Thanks for playing !",
            fontSize = 22.sp,
            color = Color(0xFF578382),
            fontFamily = fontperso,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 50.dp)
                .padding(horizontal = 25.dp)

        )
    }
}

@Composable
fun BoutonHome(modifier: Modifier = Modifier,onHome: () -> Unit){
    Image(
        painter = painterResource(id = R.drawable.icone_home),
        contentDescription = "Button Home",
        modifier = modifier
            .size(50.dp)
            .clickable { onHome() },
    )
}

@Preview
@Composable
fun Aide(modifier : Modifier = Modifier, onClick: () -> Unit={}) {
    Box(Modifier
        .fillMaxSize()
        .background(Color(0xFF0A0B0C))
    ){
        Image(
            painter = painterResource(id = R.drawable.help),
            contentDescription = "Button Home",
            modifier = modifier
                .fillMaxSize()
        )
        BoutonFermer(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick =onClick
        )
    }
}

@Composable
fun QuitHome(modifier: Modifier = Modifier, onYes: () -> Unit={},onNo: () -> Unit={}){
    Box(Modifier
        .fillMaxSize()
        .background(Color(0xFF0A0B0C).copy(alpha = 0.8f)) // Fond semi-transparent
    ) {
        Text(
            text = "Are you sure to quit the game ?",
            fontSize = 32.sp,
            color = Color(0xFFFFF9F0),
            fontFamily = fontperso,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = -50.dp),
            textAlign = TextAlign.Center
        )
        Row (modifier = Modifier.align(Alignment.Center)
        ) {
            Image(
                painter = painterResource(id = R.drawable.bouton_yes),
                contentDescription = "Button Yes",
                modifier= Modifier
                    .size(120.dp)
                    .offset(x = -20.dp, y = 70.dp)
                    .clickable { onYes() },
            )

            Image(
                painter = painterResource(id = R.drawable.bouton_no),
                contentDescription = "Button No",
                modifier= Modifier
                    .size(120.dp)
                    .offset(x = 20.dp, y = 70.dp)
                    .clickable { onNo() },
            )
        }

    }
}
@Preview
@Composable
fun GameOver(onYes: () -> Unit={},onNo: () -> Unit={}){
    Box(Modifier
        .fillMaxSize()
        .background(Color(0xFF0A0B0C).copy(alpha = 0.8f)) // Fond semi-transparent
    ){

        Text(text = "GAME OVER",
            fontSize = 55.sp,
            color = Color(0xFFE05B11),
            fontFamily = fontperso,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 150.dp))

        Text(text = "Play again ?",
            fontSize = 36.sp,
            color = Color(0xFFFFF9F0),
            fontFamily = fontperso,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 200.dp))
        Image(
            painter = painterResource(id = R.drawable.pieces_cassees),
            contentDescription = "Bouton Musique",
            modifier = Modifier
                .size(400.dp)
                .align(Alignment.BottomCenter)
                .offset(y = 10.dp)
        )
        Row (modifier = Modifier.align(Alignment.Center)
        ) {
            BoutonYes(
                modifier= Modifier
                    .size(120.dp)
                    .offset(x = -20.dp, y = -10.dp),
                onClick=onYes
            )

            BoutonNo(
                modifier= Modifier
                    .size(120.dp)
                    .offset(x = 20.dp, y = -10.dp),
                onClick=onNo
            )
        }

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
    Video(
        R.raw.fond_kyvos,
        Modifier.fillMaxSize()
    )
}


enum class GameState{HOME,PLAYING,REGLAGE,PERDU,AIDE,CREDITS,QUITHOME}

@Preview
@Composable
fun GameAPreview() {
    var gameState by remember { mutableStateOf(GameState.HOME) }

    LocalContext.current.loadSpritesheet(R.drawable.decor, 6, 4, 1)
    LocalContext.current.loadSpritesheet(R.drawable.perso, 6, 4)
    val game = makeGameA{}
    Box(Modifier.fillMaxSize()) {
        game.View(
            modifier = Modifier
                .fillMaxSize()
                .background(androidx.compose.ui.graphics.Color(0xFF0A0B0C),)
        )
        val action1 = game.padAction ?: return@Box
        BouttonReglage(
            modifier = Modifier
                .size(75.dp)
                .padding(16.dp)
                .align(Alignment.TopEnd),
            onClick = {}
        )
        ButtonAide (
            modifier = Modifier
                .size(75.dp)
                .padding(16.dp)
                .align(Alignment.TopEnd),

            onAide= {}
        )
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.BottomStart)
                .offset(y = (+35).dp),
            contentAlignment = Alignment.Center // Centre le bouton dans le Pad
        ) {
            Pad(
                Modifier.matchParentSize(),
                action = action1
            )

            ButtonRotation(
                modifier = Modifier
                    .size(75.dp)
                    .align(Alignment.Center)
                    .offset(y = (-35).dp),
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