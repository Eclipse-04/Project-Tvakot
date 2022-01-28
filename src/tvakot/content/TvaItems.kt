package tvakot.content

import arc.graphics.Color
import mindustry.ctype.ContentList
import mindustry.type.Item

class TvaItems : ContentList{
    override fun load() {
        xaopnen = object : Item("xaopnen"){
            init {
                color = Color.valueOf("7685aa")
            }
        }
    }
    companion object {
        lateinit var xaopnen: Item
    }
}