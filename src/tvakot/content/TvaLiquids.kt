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
        methanol = object : Liquid("methanol") {
            init {
                color = Color.valueOf("a3ba8dae")
                flammability = 0.8f
            }
        }
    }
    companion object {
        lateinit var steam : Liquid
        lateinit var methanol : Liquid
    }
}