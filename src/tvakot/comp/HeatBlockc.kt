package tvakot.comp

import arc.math.geom.Position
import arc.util.Time
import mindustry.gen.Building
import tvakot.world.modules.TvakotHeatModule

interface HeatBlockc : Position{
    fun appectHeat(): Boolean{
        return true
    }
    fun heatModule(): TvakotHeatModule? {
        return null
    }
    fun addHeat(amount: Float, multiply: Float){
        heatModule()!!.heat += amount * multiply
    }
    fun removeHeat(amount: Float, multiply: Float){
        heatModule()!!.heat -= amount * multiply
    }
    fun updateHeat(percent: Float){
        heatModule()!!.heat -= heatModule()!!.heat * percent * Time.delta
    }
    fun consumeHeatValid(): Boolean {
        return false
    }
    fun giveHeat(to: Building, percent: Float){
        if(to is HeatBlockc && to.heatModule()!!.heat < heatModule()!!.heat && to.appectHeat()) {
            val giveAmount = heatModule()!!.heat * percent
            to.heatModule()!!.heat += giveAmount
            heatModule()!!.heat -= giveAmount
        }
    }
    fun transferHeat(from: Building, to: Building, percent: Float){
        if(from is HeatBlockc && to is HeatBlockc && to.heatModule()!!.heat < from.heatModule()!!.heat && to.appectHeat()) {
            val giveAmount = from.heatModule()!!.heat * percent
            to.heatModule()!!.heat += giveAmount
            from.heatModule()!!.heat -= giveAmount
        }
    }
    fun dumpHeat(building: Building, percent: Float){
        val proximity = building.proximity
        if(proximity.size == 0) return
        val dump = building.cdump
        for(i in 0..proximity.size){
            building.incrementDump(proximity.size)
            val other: Building? = proximity.get((i + dump) % proximity.size)
            if(other != null){
                giveHeat(other, percent)
            }
        }
    }
}
