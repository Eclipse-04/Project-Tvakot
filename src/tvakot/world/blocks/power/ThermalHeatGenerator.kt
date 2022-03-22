package tvakot.world.blocks.power

import arc.Core
import arc.graphics.Color
import arc.math.Mathf
import mindustry.content.Fx
import mindustry.entities.Effect
import mindustry.game.Team
import mindustry.graphics.Drawf
import mindustry.graphics.Pal
import mindustry.ui.Bar
import mindustry.world.Tile
import mindustry.world.meta.Attribute
import mindustry.world.meta.Stat
import mindustry.world.meta.StatUnit


open class ThermalHeatGenerator(name: String) : HeatGenerator(name){
    var generateEffect: Effect = Fx.none
    var effectChance = 0.05f
    var attribute: Attribute = Attribute.heat
    override fun setStats() {
        super.setStats()
        stats.add(Stat.tiles, attribute, floating, (size * size).toFloat(), false)
    }

    override fun setBars() {
        super.setBars()
        if(hasItems) bars.remove("generate")
        if(hasLiquids) bars.remove("generate-liquid")
        bars.add("generate") {entity: HeatGeneratorBuild ->
            Bar(
                { Core.bundle.format("bar.tvakot-baseHeatGenerate", entity.productionEfficiency *  heatGenerate * 60) },
                { Pal.powerBar },
                { entity.productionEfficiency}
            )
        }
    }
    override fun setHeatStats() {
        stats.add(Stat.basePowerGeneration, Core.bundle.format("stats.tvakot-heatPerSec", heatGenerate * 60f), StatUnit.seconds)
    }
    override fun drawPlace(x: Int, y: Int, rotation: Int, valid: Boolean) {
        super.drawPlace(x, y, rotation, valid)
        drawPlaceText(Core.bundle.formatFloat("bar.efficiency", sumAttribute(attribute, x, y) * 100, 1), x, y, valid)
    }
    override fun canPlaceOn(tile: Tile, team: Team, rotation: Int): Boolean {
        //make sure there's heat at this location
        return tile.getLinkedTilesAs(this, tempTiles).sumf { other: Tile ->
            other.floor().attributes[attribute]
        } > 0.01f
    }
    inner class ThermalHeatGeneratorBuild : HeatGeneratorBuild() {
        var sum = 0f
        override fun appectHeat(source: TvaHeatBlockBuild?): Boolean {
            return true
        }
        override fun updateTile() {
            super.updateTile()
            productionEfficiency = sum + attribute.env()
            if (productionEfficiency > 0.1f && Mathf.chanceDelta(effectChance.toDouble())) {
                generateEffect.at(x + Mathf.range(3f), y + Mathf.range(3f))
            }
        }
        override fun canGenerate(): Boolean {
            return true
        }
        override fun drawLight() {
            Drawf.light(
                team, x, y,
                (40f + Mathf.absin(10f, 5f)) * Math.min(productionEfficiency, 2f) * size,
                Color.scarlet, 0.4f
            )
        }
        override fun onProximityAdded() {
            sum = sumAttribute(attribute, tile.x.toInt(), tile.y.toInt())
        }
    }
}