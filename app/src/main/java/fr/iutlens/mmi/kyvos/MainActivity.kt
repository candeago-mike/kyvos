package fr.iutlens.mmi.kyvos

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.iutlens.mmi.kyvos.ui.theme.MyApplicationTheme
import fr.iutlens.mmi.kyvos.utils.Music
import fr.iutlens.mmi.kyvos.utils.Music.musicMuted
import fr.iutlens.mmi.kyvos.utils.Music.soundMuted
import fr.iutlens.mmi.kyvos.utils.loadSound

import fr.iutlens.mmi.kyvos.utils.loadSpritesheet

class MainActivity : ComponentActivity() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadSpritesheet(R.drawable.decor, 6, 4, 1)
        loadSpritesheet(R.drawable.perso, 6, 4)

        loadSound(R.raw.game_over)
        loadSound(R.raw.bloc)
        loadSound(R.raw.boutons)
        loadSound(R.raw.retour)


        setContent {
            MyApplicationTheme {

                var gameState by remember { mutableStateOf(GameState.HOME) }
                var stateRetour by remember { mutableStateOf(GameState.HOME) }
                var game by remember {  mutableStateOf(makeGameA{ gameState = GameState.PERDU}) }

                val isPaused = (gameState == GameState.REGLAGE)
                if (gameState == GameState.PLAYING || gameState == GameState.REGLAGE || gameState == GameState.PERDU || gameState== GameState.AIDE || gameState == GameState.QUITHOME) {

                    Box(Modifier.fillMaxSize()) {
                        game.View(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFF0A0B0C))
                        )

                        if (!isPaused) { // Désactive les interactions si le jeu est en pause
                            if(gameState==GameState.PLAYING){
                                Music(R.raw.kyvos_in_game)
                            }
                            Text(
                                text = "Score : ${game.vrai_score}",
                                fontSize = 24.sp,
                                fontFamily = fontperso,
                                color = Color.White,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .background(Color.Black.copy(alpha = 0.5f), shape = RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                                    .align(Alignment.TopCenter)
                            )
                            BoutonHome(
                                modifier= Modifier
                                    .align(Alignment.TopStart),
                                onHome ={
                                    gameState = GameState.QUITHOME
                                    game.pause = true
                                    Music.playSound(R.raw.boutons)
                                }
                            )
                            BouttonReglage(
                                modifier = Modifier
                                    .size(75.dp)
                                    .padding(16.dp)
                                    .align(Alignment.TopEnd),
                                onClick = {
                                    stateRetour=gameState
                                    gameState = GameState.REGLAGE
                                    game.pause = true
                                    Music.playSound(R.raw.boutons)
                                }
                            )
                            ButtonAide(
                                modifier = Modifier
                                    .size(75.dp)
                                    .padding(16.dp)
                                    .offset(x = (-55).dp)
                                    .align(Alignment.TopEnd),
                                onAide = {
                                    stateRetour=gameState
                                     gameState = GameState.AIDE
                                    game.pause =true
                                    Music.playSound(R.raw.boutons)
                                }
                            )

                            Box(
                                modifier = Modifier
                                    .size(275.dp)
                                    .align(Alignment.BottomStart)
                                    .offset(y = (+65).dp,x=(+30).dp),
                                contentAlignment = Alignment.Center // Centre le bouton dans le Pad
                            ) {
                                Pad(Modifier.matchParentSize()){offset -> game.padAction?.let { it(offset) } }

                                ButtonRotation(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .align(Alignment.Center)
                                        .offset(y = (-40).dp),
                                    onClick = {
                                        game.onRotate?.let { it(game, Offset.Zero) }
                                        game.invalidate()
                                    }
                                )
                            }

                            BoutonPawh(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .size(120.dp)
                                    .padding(32.dp)
                                    .offset(
                                        y = (-0).dp,
                                    ), // Décale légèrement vers le haut
                                onClick = {
                                    game.onDash?.let { it(game, Offset.Zero) }
                                    game.invalidate()
                                })


                        }
                        if (gameState == GameState.REGLAGE) {
                            pageReglage (
                                onClick={
                                    gameState = stateRetour
                                    game.pause = false
                                    game.invalidate()
                                    Music.playSound(R.raw.boutons)
                                }
                            )

                            Music(R.raw.lobby)
                        }
                        else if(gameState == GameState.PERDU){
                            GameOver(onYes = {gameState=GameState.PLAYING
                                game = makeGameA{ gameState = GameState.PERDU}}, onNo = {gameState=GameState.HOME
                                game = makeGameA{ gameState = GameState.PERDU}},
                                score = game.vrai_score)
                            Music(R.raw.lobby)
                            Music.playSound(R.raw.game_over)
                        }else if (gameState == GameState.AIDE) {
                            Music(R.raw.lobby)
                            Aide(
                                onClick={
                                    gameState = stateRetour
                                    game.pause =false
                                    game.invalidate()
                                    Music.playSound(R.raw.boutons)
                                }
                            )

                        }else if (gameState == GameState.QUITHOME){
                            Music(R.raw.lobby)
                            QuitHome(onNo = {gameState=GameState.PLAYING
                                game.pause = false
                                game.invalidate()},
                                onYes = {gameState=GameState.HOME
                                    game = makeGameA{ gameState = GameState.PERDU}}
                            )
                        }
                    }
                }else{
                    Box {
                        Accueil (
                            onClick = {
                                gameState = GameState.PLAYING
                                Music.playSound(R.raw.boutons)
                            })
                        Music(R.raw.kyvos_in_game)
                        BouttonReglage(
                            modifier = Modifier
                                .size(75.dp)
                                .padding(16.dp)
                                .align(Alignment.TopEnd),
                            onClick = {
                                game.pause = true
                                stateRetour = gameState
                                gameState = GameState.REGLAGE
                                Music.playSound(R.raw.boutons)
                            }
                        )
                        ButtonAide(
                            modifier = Modifier
                                .size(75.dp)
                                .padding(16.dp)
                                .offset(x = (-55).dp)
                                .align(Alignment.TopEnd),
                            onAide = {
                                game.pause = true
                                stateRetour = gameState
                                gameState = GameState.AIDE
                                Music.playSound(R.raw.boutons)
                            }
                        )
                        BoutonCredits(
                            modifier = Modifier
                                .align(Alignment.BottomCenter),
                            onCredits = {gameState = GameState.CREDITS
                                Music.playSound(R.raw.boutons)
                            }
                        )
                        if (gameState==GameState.CREDITS) {
                            Credits(
                                onClick = {gameState = GameState.HOME}
                            )
                        }
                    }

                }
            }

        }}    override fun onPause() {
        super.onPause()
        musicMuted = true
        soundMuted=true
    }
}






