package fr.iutlens.mmi.kyvos

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import fr.iutlens.mmi.kyvos.utils.Music.mute
import fr.iutlens.mmi.kyvos.ui.theme.MyApplicationTheme
import fr.iutlens.mmi.kyvos.utils.Music
import fr.iutlens.mmi.kyvos.utils.loadSound

import fr.iutlens.mmi.kyvos.utils.loadSpritesheet

class MainActivity : ComponentActivity() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadSpritesheet(R.drawable.decor, 6, 4, 1)
        loadSpritesheet(R.drawable.perso, 6, 4)

        loadSound(R.raw.message)


        val game = makeGameA()
        setContent {
            MyApplicationTheme {

                var gameState by remember { mutableStateOf(GameState.HOME) }

                val isPaused = (gameState == GameState.REGLAGE)
                    if (gameState == GameState.PLAYING || gameState == GameState.REGLAGE || gameState == GameState.PERDU) {
                        if (game.gagne) {

                        Box(Modifier.fillMaxSize()) {
                            game.View(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(androidx.compose.ui.graphics.Color.Black)
                            )

                            if (!isPaused) { // Désactive les interactions si le jeu est en pause
                                val action1 = game.padAction ?: return@Box
                                BouttonReglage(
                                    modifier = Modifier
                                        .size(75.dp)
                                        .padding(16.dp)
                                        .align(Alignment.TopEnd),
                                    onClick = {
                                        gameState = GameState.REGLAGE
                                        game.pause = true
                                    }
                                )
                                Box(
                                    modifier = Modifier
                                        .size(200.dp)
                                        .align(Alignment.BottomStart)
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Pad(Modifier.matchParentSize(), action = action1)

                                    ButtonRotation(
                                        modifier = Modifier
                                            .size(75.dp)
                                            .align(Alignment.Center)
                                            .offset(y = (-25).dp),
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
                                        .offset(
                                            y = (-50).dp,
                                            x = (-20).dp
                                        ), // Décale légèrement vers le haut
                                    onClick = {
                                        game.onDash?.let { it(game, Offset.Zero) }
                                        game.invalidate()
                                    }
                                )
                            }
                            if (gameState == GameState.REGLAGE) {
                                pageReglage {
                                    gameState = GameState.PLAYING
                                    game.pause = false
                                    game.invalidate()
                                }
                            }else if(gameState == GameState.PERDU){
                                GameOver()
                            }
                        }
                    } else if(game.gagne==false) {
                        gameState = GameState.PERDU
                    }
                }else{
                    Accueil { gameState = GameState.PLAYING }

                }
                Music(id = R.raw.jungle)
            }

}}    override fun onPause() {
        super.onPause()
        mute = false
    }
}




