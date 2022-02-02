package tvakot.world.blocks.power

import arc.func.Boolf
import mindustry.type.Item
import mindustry.world.consumers.ConsumeItemFilter


open class BurnerHeatGenerator(name: String) : HeatGenerator(name) {
    var default = true
    var minItemEfficiency = 0.3f

    fun defaultConsume(){
        consumes.add(ConsumeItemFilter(Boolf { item: Item ->
            return@Boolf getItemEfficiency(item) >= minItemEfficiency
        })).update(false).optional(true, false)
    }
    init {
        if(default){
            defaultConsume()
        }
    }
    override fun getItemEfficiency(item: Item): Float {
        return item.flammability
    }
    inner class BurnerHeatGeneratorBuild : HeatGeneratorBuild()
}