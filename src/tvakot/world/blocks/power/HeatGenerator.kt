package tvakot.world.blocks.power

import arc.Core
import arc.math.Mathf
import mindustry.Vars
import mindustry.graphics.Pal
import mindustry.type.Item
import mindustry.type.Liquid
import mindustry.ui.Bar
import mindustry.world.meta.BlockStatus
import mindustry.world.meta.Stat
import mindustry.world.meta.StatUnit
import tvakot.world.blocks.TvaHeatBlock

open class HeatGenerator(name: String) : TvaHeatBlock(name) {
    var heatGenerate = 5f
    var heatGenerateLiquid = 0f
    var generateTime = 90f
    var liquidUsage = 0f
    var minLiquidEfficiency = 0.3f

    init {
        if(liquidUsage > 0f) hasLiquids = true
        outputHeat = true
    }

    override fun setBars() {
        super.setBars()
        if(heatGenerate > 0f) bars.add("generate") {entity: HeatGeneratorBuild ->
            Bar(
                { Core.bundle.format("bar.tvakot-heatGenerate", entity.displayHeatItem() *  heatGenerate * 60) },
                { Pal.powerBar },
                { entity.productionEfficiency}
            )
        }
        if(heatGenerateLiquid > 0f) bars.add("generate-liquid") {entity: HeatGeneratorBuild ->
            Bar(
                { Core.bundle.format("bar.tvakot-heatGenerateLiquid", entity.productionLiquidEfficiency * heatGenerateLiquid * 60) },
                { Pal.powerBar },
                { entity.productionLiquidEfficiency}
            )
        }
    }
    open fun getItemEfficiency(item: Item): Float{
        return 1f
    }
    open fun getLiquidEfficiency(liquid: Liquid): Float{
        return 1f
    }
    override fun setStats() {
        super.setStats()
        if(hasItems) {
            stats.add(Stat.productionTime, generateTime / 60f, StatUnit.seconds)
        }
        setHeatStats()
    }
    open fun setHeatStats() {
        stats.add(Stat.basePowerGeneration, Core.bundle.format("stats.tvakot-heatPerSecItem", heatGenerate * 60f), StatUnit.seconds)
        stats.add(Stat.basePowerGeneration, Core.bundle.format("stats.tvakot-heatPerSecLiquid", heatGenerateLiquid * 60f), StatUnit.seconds)
    }
    open inner class HeatGeneratorBuild : TvaHeatBlockBuild() {
        var generateTimeLeft = 0f
        var productionEfficiency = 0f
        var productionLiquidEfficiency = 0f
        var warmup = 0f
        var totalTime = 0f
        fun displayHeatItem(): Float {
            return if(items.total() > 0){
                productionEfficiency
            } else 0f
        }
        override fun appectHeat(source: TvaHeatBlockBuild?): Boolean {
            return false
        }
        open fun canGenerate(): Boolean {
            return generateTimeLeft > 0f
        }
        open fun canGenerateLiquid(liquid: Liquid): Boolean {
            return this.liquids.get(liquid) >= liquidUsage * 60
        }
        override fun updateTile() {
            super.updateTile()
            generateTimeLeft -= delta()
            if(canGenerate() || productionLiquidEfficiency > 0.01f) {
                warmup = Mathf.lerp(warmup, 1f, 0.1f)
                totalTime++
            } else warmup = Mathf.lerp(warmup, 0f, 0.2f)
            if(canGenerate()) addHeat(heatGenerate, edelta() * productionEfficiency)
            if(hasLiquids) {
                var liquid: Liquid? = null
                for (other in Vars.content.liquids()) {
                    if (hasLiquids && liquids[other] >= liquidUsage * 60 && getLiquidEfficiency(other) >= minLiquidEfficiency) {
                        liquid = other
                        break
                    }
                }
                if(liquid != null) {
                    liquids.remove(liquid, edelta())
                    productionLiquidEfficiency = getLiquidEfficiency(liquid)
                    addHeat(heatGenerateLiquid, edelta() * productionLiquidEfficiency)
                } else productionLiquidEfficiency = 0f
            }
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
            if(warmup > 0.1f) return BlockStatus.active
            return BlockStatus.noInput
        }
    }
}