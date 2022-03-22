package tvakot.world.blocks

import arc.Core
import arc.graphics.Color
import arc.graphics.g2d.TextureRegion
import mindustry.gen.Building
import mindustry.graphics.Drawf
import mindustry.logic.LAccess
import mindustry.ui.Bar
import mindustry.world.Block
import mindustry.world.meta.Stat
import tvakot.comp.HeatBlockc
import tvakot.world.modules.TvakotConsumeModule
import tvakot.world.modules.TvakotHeatModule

open class TvaHeatBlock(name: String) : Block(name) {
    var heatColorStart: Color = Color.valueOf("cf554e")
    var heatColorEnd: Color = Color.valueOf("ff8f40")
    var heatLoss = 0.05
    var customConsume: TvakotConsumeModule = TvakotConsumeModule()
    var heatCapacity = 300f
    var dumpSpeed = 0.07f
    var heatedRegion: TextureRegion? = null
    var outputHeat = true
    init {
        solid = true
        update = true
        sync = true
    }
    override fun setStats() {
        super.setStats()
        stats.add(Stat.heatCapacity, heatCapacity)
        stats.add(Stat.heatCapacity, Core.bundle.format("stats.tvakot-heatLoss", heatLoss * 100))
    }
    override fun load() {
        super.load()
        heatedRegion = Core.atlas.find("$name-heated")
    }
    override fun setBars() {
        super.setBars()
        bars.add("heat") { entity: TvaHeatBlock.TvaHeatBlockBuild ->
            Bar(
                {Core.bundle.get("bar.tvakot-heat")},
                {entity.heatColor()},
                {entity.heatFullness()}
            )
        }
    }

    override fun icons(): Array<TextureRegion> {
        return arrayOf(region)
    }
    open inner class TvaHeatBlockBuild : Building(), HeatBlockc {
        val heatModule: TvakotHeatModule = TvakotHeatModule()
        override fun heatModule(): TvakotHeatModule {
            return heatModule
        }
        fun heatColor(): Color{
            return heatColorStart.cpy().lerp(heatColorEnd, heatFullness())
        }
        fun heatFullness(): Float {
            return heatModule.heat / heatCapacity
        }
        override fun updateTile() {
            super.updateTile()
            updateHeat(heatLoss.toFloat() * timeScale())
            if(heatModule.heat > heatCapacity) kill()
        }
        override fun sense(sensor: LAccess): Double {
            if(sensor != LAccess.heat) return 0.0
            return heatModule.heat / heatCapacity.toDouble()
        }

        override fun draw() {
            super.draw()
            val rot = if(rotate) rotdeg() else 0f
            Drawf.liquid(heatedRegion, x, y, heatFullness(), heatColor(), rot)
            Drawf.light(this.team, x, y, 40f * heatFullness(), heatColor(), heatFullness() * 0.4f)
        }
    }
}