package fr.iutlens.mmi.kyvos.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

/**
 * Music permet de jouer de la musique ou des sons pendant le jeu
 *
 */
object Music {

    /**
     * mute permet d'activer ou désactiver le son joué par l'application
     */
    var mute by mutableStateOf(true)

    /**
     * Sound pool gère les bruitages (jusqu'à 10 en simultané ici)
     */
    private val soundPool  by lazy {
        SoundPool.Builder()
            .setMaxStreams(10)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_GAME).build()
            ).build()
    }

    /**
     * Sound map est un tableau associatif permettant de faire la correspondance
     * entre les id des ressources (R.raw.jungle par exemple) et les
     * id utilisés par soundPool
     */
    private val soundMap = mutableMapOf<Int,Int>()

    /**
     * Load sound charge un fichier son (.ogg) pour être joué ensuite à la demande
     *
     * @param context
     * @param id
     */
    fun loadSound(context: Context, id: Int){
        soundMap.getOrPut(id) { soundPool.load(context,id,1) }
    }

    /**
     * Play sound joue un son précédemment chargé (ne fait rien sinon)
     *
     * @param id
     * @param leftVolume
     * @param rightVolume
     * @param priority
     * @param loop
     * @param rate
     */
    fun playSound(id: Int,
                  leftVolume: Float = 1f,
                  rightVolume: Float = 1f,
                  priority: Int = 1,
                  loop: Int = 0,
                  rate: Float = 1f
                  ){
        if (mute) return
        soundMap[id]?.let { soundId -> soundPool.play(soundId,leftVolume,rightVolume,priority, loop, rate) }
    }


    @Composable
    operator fun invoke(id: Int){
        val context = LocalContext.current
        val musicPlayer by remember(id to mute) {
            derivedStateOf {
                MediaPlayer.create(context, id).apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_GAME).build()
                    )
                }
            }
        }

        if (!mute) DisposableEffect(id) {
            musicPlayer.apply {
                isLooping = true
                start()
            }

            onDispose {
                musicPlayer.stop()
            }
        } else {
            musicPlayer.stop()
        }
    }
}

/**
 * Load sound charge un fichier son afin de l jouer plus tard
 *
 * @param id
 */
fun Context.loadSound(id: Int) = Music.loadSound(this, id)