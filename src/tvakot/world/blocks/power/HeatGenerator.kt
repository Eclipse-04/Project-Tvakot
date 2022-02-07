package tvakot.world.blocks.power

import arc.Core
import mindustry.type.Item
import mindustry.world.meta.BlockStatus
import mindustry.world.meta.Stat
import mindustry.world.meta.StatUnit
import tvakot.world.blocks.TvaHeatBlock

open class HeatGenerator(name: String) : TvaHeatBlock(name) {
    var heatGenerate = 5f
    var generateTime = 90f
    init {
        outputHeat = true
    }
    open fun getItemEfficiency(item: Item): Float{
        return 1f
    }
    override fun setStats() {
        super.setStats()
        if(hasItems) {
            stats.add(Stat.productionTime, generateTime / 60f, StatUnit.seconds)
        }
        stats.add(Stat.basePowerGeneration, Core.bundle.format("stats.tvakot-heatPerSec", heatGenerate * 60f), StatUnit.seconds)
    }
    open inner class HeatGeneratorBuild : TvaHeatBlockBuild() {
        var generateTimeLeft = 0f
        var productionEfficiency = 1f
        override fun appectHeat(source: TvaHeatBlockBuild?): Boolean {
            return false
        }
        open fun canGenerate(): Boolean {
            return generateTimeLeft > 0f
        }
        override fun updateTile() {
            super.updateTile()
            generateTimeLeft -= delta()
            if(canGenerate()) addHeat(heatGenerate, edelta() * productionEfficiency)
            if(hasItems) {
                if(generateTimeLeft <= 0f && items.total() > 0) {
                    val consumed = items.take()
                    productionEfficiency = getItemEfficiency(consumed)
                    generateTimeLeft = generateTime
                }
            }
            dumpHeat(this, dumpSpeed)
        }
        override fun status(): BlockStatus{
            if(generateTimeLeft > 0f) return BlockStatus.active
            return BlockStatus.noInput
        }
    }
}