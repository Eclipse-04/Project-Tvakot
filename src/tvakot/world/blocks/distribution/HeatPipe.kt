package tvakot.world.blocks.distribution

import tvakot.world.blocks.TvaHeatBlock

open class HeatPipe(name: String) : TvaHeatBlock(name) {
    init {
        rotate = true
    }
    inner class HeatPipeBuild : TvaHeatBlockBuild() {
        override fun appectHeat(source: TvaHeatBlockBuild?): Boolean {
            return (source is HeatNode.HeatNodeBuild || source is HeatPipeBuild) && source == this.back()
        }
        override fun updateTile() {
            super.updateTile()
            if (front() is HeatPipeBuild || front() is HeatNode.HeatNodeBuild){
                giveHeat(this, front(), dumpSpeed)
            }
        }
    }
}