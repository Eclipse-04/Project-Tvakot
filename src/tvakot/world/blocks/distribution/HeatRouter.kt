package tvakot.world.blocks.distribution

import tvakot.world.blocks.TvaHeatBlock

open class HeatRouter(name: String) : TvaHeatBlock(name){
    init {
        outputHeat = true
    }
    inner class HeatRouterBuild : TvaHeatBlockBuild() {
        override fun updateTile() {
            super.updateTile()
            dumpHeat(this, dumpSpeed)
        }
    }
}