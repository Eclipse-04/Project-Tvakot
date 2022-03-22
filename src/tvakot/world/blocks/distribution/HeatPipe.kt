package tvakot.world.blocks.distribution

import tvakot.world.blocks.TvaHeatBlock

open class HeatPipe(name: String) : TvaHeatBlock(name) {
    init {
        rotate = true
    }
    inner class HeatPipeBuild : TvaHeatBlockBuild() {
        override fun appectHeat(source: TvaHeatBlockBuild?): Boolean {
            return (source is TvaHeatBlockBuild) && (source == this.back() || source == this.front())
        }
        override fun updateTile() {
            super.updateTile()
            if (front() is TvaHeatBlockBuild){
                giveHeat(this, front(), dumpSpeed)
            }
            if (back() is TvaHeatBlockBuild){
                giveHeat(this, back(), dumpSpeed)
            }
        }
    }
}