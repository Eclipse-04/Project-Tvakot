package tvakot.content

import arc.graphics.Color
import arc.graphics.g2d.Draw
import arc.graphics.g2d.Fill
import arc.math.Angles
import arc.math.Interp
import arc.math.Mathf
import arc.math.Rand
import mindustry.ctype.ContentList
import mindustry.entities.Effect
import mindustry.graphics.Drawf
import mindustry.graphics.Pal
import tvakot.graphic.GraphicUtil.Companion.cameraXOffset
import tvakot.graphic.GraphicUtil.Companion.cameraYOffset

class TvaFx : ContentList {
    private val rand = Rand()
    override fun load() {
        forgeSmoke = Effect(100f) { e ->
            val interp = Interp.smooth
            Draw.color(Color.valueOf("676593").lerp(Color.white, interp.apply(e.fin())))
            Draw.alpha(e.fout())
            Angles.randLenVectors(e.id.toLong(), 1, 5f) {x, y ->
                Fill.circle(e.x + cameraXOffset(e.x, e.fin() / 3) + x,
                    e.y + cameraYOffset(e.y, e.fin() / 3) + y,
                    e.fin() * 5f)
            }
        }
        smelterSmoke = Effect(120f) { e ->
            val interp = Interp.smooth
            Draw.color(Color.valueOf("7685aa").lerp(Color.white, interp.apply(e.fin())))
            Draw.alpha(e.fout())
            Angles.randLenVectors(e.id.toLong(), 1, 5f) {x, y ->
                Fill.circle(e.x + cameraXOffset(e.x, e.fin() / 3) + x,
                    e.y + cameraYOffset(e.y, e.fin() / 3) + y,
                    e.fin() * 5f)
            }
        }
        surgeRicochetShoot = Effect(20f){ e ->
            Draw.color(Pal.surge)
            for (i in Mathf.signs) {
                Drawf.tri(e.x, e.y, 7f * e.fout(), 22f, e.rotation + 90f * i)
                Drawf.tri(e.x, e.y, 7f * e.fout(), 22f, e.rotation + 20f * i)
            }
        }
    }
    companion object {
        lateinit var forgeSmoke: Effect
        lateinit var smelterSmoke: Effect
        lateinit var surgeRicochetShoot: Effect
    }
}