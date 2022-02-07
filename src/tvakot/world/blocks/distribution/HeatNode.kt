package tvakot.world.blocks.distribution

import arc.Core
import arc.graphics.g2d.Draw
import arc.graphics.g2d.TextureRegion
import mindustry.graphics.Drawf
import tvakot.world.blocks.TvaHeatBlock


open class HeatNode(name: String) : TvaHeatBlock(name){
    var blendRegion: Array<TextureRegion> = Array(2) {Core.atlas.find("error")}
    init {
        solid = false
        conveyorPlacement = true
        heatLoss = 0.0
    }
    override fun load() {
        super.load()
        blendRegion[0] = Core.atlas.find("$name-blend")
        blendRegion[1] = Core.atlas.find("$name-blend-heated")
    }
    inner class HeatNodeBuild : TvaHeatBlockBuild(){
        override fun updateTile() {
            super.updateTile()
            dumpHeat(this, dumpSpeed)
        }
        override fun draw() {
            Draw.rect(region, x, y)
            for(c in 0..3){
                val a = nearby(c)
                if(a !is TvaHeatBlockBuild) continue
                Draw.rect(blendRegion[0], x, y, c * 90f)
                Drawf.liquid(blendRegion[1], x, y, heatFullness(), heatColor(),c * 90f)
            }
        }
    }
}