package tvakot.world.blocks.distribution

import arc.Core
import arc.graphics.g2d.Draw
import arc.graphics.g2d.TextureRegion
import arc.math.geom.Geometry
import arc.util.Eachable
import mindustry.Vars
import mindustry.entities.units.BuildPlan
import mindustry.graphics.Drawf
import tvakot.graphic.GraphicUtil
import tvakot.world.blocks.TvaHeatBlock


open class HeatNode(name: String) : TvaHeatBlock(name){
    var blendRegion: Array<TextureRegion?> = Array(16) {Core.atlas.find("error")}
    var blendHeatRegion: Array<TextureRegion?> = Array(16) {Core.atlas.find("error")}
    init {
        solid = false
        conveyorPlacement = true
        heatLoss = 0.0
    }
    override fun load() {
        super.load()
        blendRegion = GraphicUtil.getRegions(Core.atlas.find("$name-tile"), 8, 2)
        blendHeatRegion = GraphicUtil.getRegions(Core.atlas.find("$name-tile-heated"), 8, 2)
    }
    override fun drawRequestRegion(req: BuildPlan, list: Eachable<BuildPlan>) {
        val scl = Vars.tilesize * req.animScale
        var spriteIndex = 0
        for (i in 0..3) {
            val p = Geometry.d4((4 - i) % 4).cpy().add(req.x, req.y)
            if (Vars.world.build(p.x, p.y) is HeatNodeBuild) {
                spriteIndex += 1 shl i
            } else {
                val f = booleanArrayOf(false)
                list.each { plan: BuildPlan ->
                    if (!f[0] && plan.x == p.x && plan.y == p.y) {
                        f[0] = true
                    }
                }
                if (f[0]) {
                    spriteIndex += 1 shl i
                }
            }
        }
        Draw.rect(blendRegion.get(spriteIndex), req.drawx(), req.drawy(), scl, scl)
    }
    inner class HeatNodeBuild : TvaHeatBlockBuild(){
        var spriteIndex = 0

        override fun updateTile() {
            super.updateTile()
            dumpHeat(this, dumpSpeed)
        }
        override fun draw() {
            Draw.rect(blendRegion[spriteIndex], x, y)
            Drawf.liquid(blendHeatRegion[spriteIndex], x, y, heatFullness(), heatColor())
        }

        override fun onProximityUpdate() {
            super.onProximityUpdate()
            spriteIndex = 0
            for(i in 0 until 4){
                if(nearby((4-i)%4) is TvaHeatBlockBuild){
                    spriteIndex += 1 shl i
                }
            }
        }

    }
}