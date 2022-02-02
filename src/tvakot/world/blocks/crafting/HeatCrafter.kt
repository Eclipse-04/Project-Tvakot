package tvakot.world.blocks.crafting

import arc.Core
import arc.math.Mathf
import arc.struct.EnumSet
import mindustry.content.Fx
import mindustry.entities.Effect
import mindustry.type.ItemStack
import mindustry.type.LiquidStack
import mindustry.ui.Bar
import mindustry.world.meta.*
import tvakot.world.blocks.TvaHeatBlock
import tvakot.world.draw.DrawHeatBlock


open class HeatCrafter(name: String) : TvaHeatBlock(name){
    var minHeatRequire = 100f
    var craftTime = 60f
    var warmupSpeed = 0.02f
    var updateEffect: Effect = Fx.none
    var updateEffectChance = 0.04
    var outputItems: Array<ItemStack>? = null
    var outputLiquid: LiquidStack? = null
    var craftEffect: Effect = Fx.pulverizeSmall
    var drawerCustom: DrawHeatBlock = DrawHeatBlock()
    init {
        super.init()
        flags = EnumSet.of(BlockFlag.factory)
        hasItems = true
        outputsLiquid = outputLiquid != null
    }
    override fun load() {
        super.load()
        drawerCustom.load(this)
    }
    override fun setStats() {
        stats.timePeriod = craftTime
        super.setStats()
        stats.add(Stat.productionTime, craftTime / 60f, StatUnit.seconds)
        if(outputItems != null){
            stats.add(Stat.output, StatValues.items(craftTime, *outputItems!!))
        }
        if (outputLiquid != null) {
            stats.add(Stat.output, outputLiquid!!.liquid, outputLiquid!!.amount * (60f / craftTime), true)
        }
    }

    override fun setBars() {
        super.setBars()
        bars.add("heat-satisfaction") { entity: HeatCrafter.HeatCrafterBuild ->
            Bar(
                { Core.bundle.format("bar.tvakot-heat-satisfaction", (entity.heatSatisfaction() * 100).toInt()) },
                { entity.heatColor() },
                { entity.heatSatisfaction() }
            )
        }
    }
    inner class HeatCrafterBuild : TvaHeatBlockBuild() {
        var progress = 0f
        var totalProgress = 0f
        var warmup = 0f
        override fun consumeHeatValid(): Boolean {
            return heatModule.heat >= minHeatRequire
        }
        override fun shouldConsume(): Boolean {
            if (outputItems != null) {
                for (output: ItemStack in outputItems!!) {
                    if (items[output.item] + output.amount > itemCapacity) {
                        return false
                    }
                }
            }
            return (outputLiquid == null || liquids[outputLiquid!!.liquid] < liquidCapacity - 0.001f) && enabled && heatModule.heat > 0.001f
        }
        fun heatSatisfaction(): Float{
            return Mathf.clamp(heatModule.heat / minHeatRequire)
        }
        fun craft() {
            consume()
            if (outputItems != null) {
                for (output in outputItems!!) {
                    for (i in 0 until output.amount) {
                        offload(output.item)
                    }
                }
            }
            if (outputLiquid != null) {
                handleLiquid(this, outputLiquid!!.liquid, outputLiquid!!.amount)
            }
            craftEffect.at(x, y)
            progress %= 1f
        }
        override fun updateTile() {
            super.updateTile()
            if (consValid() && consumeHeatValid()) {
                progress += getProgressIncrease(craftTime)
                totalProgress += delta()
                removeHeat(customConsume.heat, delta())
                warmup = Mathf.approachDelta(warmup, 1f, warmupSpeed)
                if (Mathf.chanceDelta(updateEffectChance)) {
                    updateEffect.at(x + Mathf.range(size * 4f), y + Mathf.range(size * 4))
                }
            } else {
                warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed)
            }
            if (progress >= 1f) {
                craft()
            }
            dumpOutputs()
        }
        fun dumpOutputs() {
            if (outputItems != null && timer(timerDump, dumpTime / timeScale)) {
                for (output in outputItems!!) {
                    dump(output.item)
                }
            }
            if (outputLiquid != null) {
                dumpLiquid(outputLiquid!!.liquid)
            }
        }

        override fun draw() {
            drawerCustom.draw(this)
        }

        override fun status(): BlockStatus {
            return if(heatModule.heat < minHeatRequire) BlockStatus.noInput else cons.status()
        }
    }
}
