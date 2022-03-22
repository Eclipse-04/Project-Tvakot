package tvakot.world.blocks.power

import arc.Core
import arc.func.Boolf
import arc.graphics.g2d.TextureRegion
import mindustry.graphics.Pal
import mindustry.type.Item
import mindustry.type.Liquid
import mindustry.world.consumers.ConsumeItemFilter
import mindustry.world.consumers.ConsumeLiquidFilter


open class BurnerHeatGenerator(name: String) : HeatGenerator(name) {
    var default = true
    var minItemEfficiency = 0.3f

    lateinit var flameRegion: TextureRegion

    override fun load() {
        super.load()
        flameRegion = Core.atlas.find("$name-flame")
    }

    fun defaultConsume(){
        if(generateTime > 0f) consumes.add(ConsumeItemFilter(Boolf { item: Item ->
            return@Boolf getItemEfficiency(item) >= minItemEfficiency
        })).update(false).optional(true, false)
        if(liquidUsage > 0f) consumes.add(ConsumeLiquidFilter(Boolf { liquid: Liquid ->
            return@Boolf getLiquidEfficiency(liquid) >= minLiquidEfficiency
        }, liquidUsage)).update(false).optional(true, false)
    }
    override fun init () {
        if(default){
            defaultConsume()
        }
        super.init()
    }
    override fun getItemEfficiency(item: Item): Float {
        return item.flammability
    }
    override fun getLiquidEfficiency(liquid: Liquid): Float {
        return liquid.flammability
    }
}