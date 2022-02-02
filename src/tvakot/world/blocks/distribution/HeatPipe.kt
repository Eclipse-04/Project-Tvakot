package tvakot.world.blocks.distribution

import arc.Core
import arc.graphics.g2d.Draw
import arc.graphics.g2d.TextureRegion
import mindustry.graphics.Drawf
import mindustry.graphics.Layer
import tvakot.world.blocks.TvaHeatBlock

open class HeatPipe(name: String) : TvaHeatBlock(name) {
    var blendRegion: TextureRegion? = null
    var blendHeatedRegion: TextureRegion? = null
    init {
        rotate = true
        solid = false
        conveyorPlacement = true
        noUpdateDisabled = true
        heatLoss = 0f
    }
    override fun load() {
        super.load()
        blendRegion = Core.atlas.find("$name-blend")
        blendHeatedRegion = Core.atlas.find("$name-blend-heated")
    }
    inner class HeatPipeBuild : TvaHeatBlockBuild() {
        override fun updateTile() {
            super.updateTile()
            if(front() != null && front().isValid){
                giveHeat(front(), dumpSpeed)
            }
            if(back() != null && back().isValid){
                giveHeat(back(), dumpSpeed)
            }
        }
        override fun draw() {
            super.draw()
            val rot = rotdeg()
            Draw.z(Layer.block + 0.001f)
            if(front() is HeatPipeBuild && front().rotation % 2 != this.rotation % 2) {
                Draw.rect(blendRegion, front().x, front().y, rot)
                Drawf.liquid(blendHeatedRegion, front().x, front().y, heatFullness(), heatColor(), rot)
            }
            if(back() is HeatPipeBuild && back().rotation % 2 != this.rotation % 2) {
                Draw.rect(blendRegion, back().x, back().y, rot)
                Drawf.liquid(blendHeatedRegion, back().x, back().y, heatFullness(), heatColor(), rot)
            }
        }
    }
}