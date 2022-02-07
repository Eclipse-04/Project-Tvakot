package tvakot.content

import arc.graphics.Color
import mindustry.ctype.ContentList
import mindustry.type.Item

class TvaItems : ContentList{
    override fun load() {
        xaopnen = object : Item("xaopnen"){
            init {
                color = Color.valueOf("7685aa")
                hardness = 2
            }
        }
        xaopnenBar = object : Item("xaopnen-bar"){
            init {
                color = Color.valueOf("5c567a")
            }
        }
        heatModule = object : Item("heat-module"){
            init {
                color = Color.valueOf("b87a5c")
            }
        }
    }
    companion object {
        lateinit var xaopnen: Item
        lateinit var xaopnenBar: Item
        lateinit var heatModule: Item
    }
}