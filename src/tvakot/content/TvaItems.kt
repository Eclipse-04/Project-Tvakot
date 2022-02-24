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
        xeopnax = object : Item("xeopnax"){
            init {
                color = Color.valueOf("676486")
            }
        }
        denseIngot = object : Item("dense-ingot"){
            init {
                color = Color.valueOf("9a9fb4")
            }
        }
    }
    companion object {
        lateinit var xaopnen: Item
        lateinit var xaopnenBar: Item
        lateinit var xeopnax: Item
        lateinit var denseIngot: Item
    }
}