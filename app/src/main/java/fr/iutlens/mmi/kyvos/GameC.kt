package fr.iutlens.mmi.kyvos


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.iutlens.mmi.kyvos.game.Game
import fr.iutlens.mmi.kyvos.game.sprite.BasicSprite
import fr.iutlens.mmi.kyvos.ui.theme.MyApplicationTheme
import fr.iutlens.mmi.kyvos.game.sprite.spriteListOf
import fr.iutlens.mmi.kyvos.game.sprite.tiledArea
import fr.iutlens.mmi.kyvos.game.sprite.toMutableTileMap
import fr.iutlens.mmi.kyvos.game.transform.Constraint
import fr.iutlens.mmi.kyvos.game.transform.GenericTransform
import fr.iutlens.mmi.kyvos.utils.loadSpritesheet

fun makeGameC(): Game {
    val map = """
            33333333333333333333333333333333333
            33222222222222222222222222222222233
            33222222222222222222222222222222233
            33222222222222222222222222222222233
            33222222222222222222222222222222233
            33222222222222221222222222222222233
            33222222222221111222222222222222233
            33222222222221112222222222222222233
            33333333333333333333333333333333333
            33333333333333333333333333333333333
        """.trimIndent().toMutableTileMap(
        "123"
    )
    val tileMap = R.drawable.decor.tiledArea(map)

    val sprite = BasicSprite(R.drawable.perso,3.5f*tileMap.w,2f*tileMap.h)
    val game = Game(background = tileMap,
        spriteList = spriteListOf(sprite),
        transform = GenericTransform(
            Constraint.Focus(tileMap,sprite,10)
        )
    )

    game.animationDelayMs = 20
    game.update = { it ->
        it.joystickPosition?.let { position ->
            if (!position.isCentered){
                sprite.x += position.x*tileMap.w/4
                sprite.y += position.y*tileMap.h/4
            }
        }
        it.invalidate()
    }
    game.animationDelayMs = 20

    return game
}


@Preview(showBackground = true)
@Composable
fun GameCPreview() {
    LocalContext.current.loadSpritesheet(R.drawable.decor, 3, 1)
    LocalContext.current.loadSpritesheet(R.drawable.perso, 3, 1)
    val game = makeGameC()
    MyApplicationTheme {
        Box(Modifier.fillMaxSize()){
            game.View(modifier = Modifier
                .fillMaxSize()
                .background(Color.Black))

                Joystick(modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                ) { game.joystickPosition = it }
            }
        }
}