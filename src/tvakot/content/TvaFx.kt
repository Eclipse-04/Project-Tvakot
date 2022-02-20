package tvakot.content

import arc.graphics.Color
import arc.graphics.g2d.Draw
import arc.graphics.g2d.Fill
import arc.math.Interp
import mindustry.ctype.ContentList
import mindustry.entities.Effect

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
        smallSmoke = Effect(100f) { e ->
            Draw.color(e.color)
            Draw.alpha(e.fout())
            Fill.circle(e.x, e.y + e.fin() * 30, e.fin() * 4f)
        }
    }
    companion object {
        lateinit var xaoForgeSmoke: Effect
        lateinit var smelterSmoke: Effect
        lateinit var smallSmoke: Effect
    }
}