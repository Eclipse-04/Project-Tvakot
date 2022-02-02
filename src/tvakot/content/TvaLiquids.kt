package tvakot.content

import arc.graphics.Color
import mindustry.content.StatusEffects
import mindustry.ctype.ContentList
import mindustry.type.Liquid

class TvaLiquids : ContentList {
    override fun load() {
        steam = object : Liquid("steam") {
            init {
                temperature = 0.6f
                color = Color.valueOf("ecececae")
                effect = StatusEffects.wet
            }
        }
    }
    companion object {
        lateinit var steam : Liquid
    }
}