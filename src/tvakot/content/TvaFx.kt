package tvakot.content

import arc.graphics.Color
import arc.graphics.g2d.Draw
import arc.graphics.g2d.Fill
import arc.math.Interp
import arc.math.Mathf
import mindustry.ctype.ContentList
import mindustry.entities.Effect
import mindustry.graphics.Drawf
import mindustry.graphics.Pal

class TvaFx : ContentList {
    override fun load() {
        xaoForgeSmoke = Effect(100f) { e ->
            val interp = Interp.smooth
            Draw.color(Color.valueOf("676593").lerp(Color.white, interp.apply(e.fin())))
            Draw.alpha(e.fout())
            Fill.circle(e.x, e.y + e.fin() * 40, e.fin() * 5f)
        }
        smelterSmoke = Effect(120f) { e ->
            val interp = Interp.smooth
            Draw.color(Color.valueOf("7685aa").lerp(Color.white, interp.apply(e.fin())))
            Draw.alpha(e.fout())
            Fill.circle(e.x, e.y + e.fin() * 30, e.fin() * 4f)
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
        lateinit var xaoForgeSmoke: Effect
        lateinit var smelterSmoke: Effect
        lateinit var surgeRicochetShoot: Effect
    }
}