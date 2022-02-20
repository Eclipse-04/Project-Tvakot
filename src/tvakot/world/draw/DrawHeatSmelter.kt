package tvakot.world.draw

import arc.Core
import arc.graphics.Color
import arc.graphics.g2d.Draw
import arc.graphics.g2d.Fill
import arc.graphics.g2d.TextureRegion
import arc.math.Mathf
import arc.util.Time
import mindustry.graphics.Drawf
import mindustry.graphics.Layer
import mindustry.world.Block
import tvakot.world.blocks.crafting.HeatCrafter
import kotlin.math.max


class DrawHeatSmelter : DrawHeatBlock() {
    var flameColor: Color = Color.valueOf("ffc999")
    var top: TextureRegion? = null
    var top1: TextureRegion? = null
    var lightRadius = 60f
    var lightAlpha = 0.65f
    var lightSinScl = 10f
    var lightSinMag = 5f
    var flameRadius = 3f
    var flameRadiusIn = 1.9f
    var flameRadiusScl = 5f
    var flameRadiusMag = 2f
    var flameRadiusInMag = 1f

    override fun load(block: Block) {
        super.load(block)
        top = Core.atlas.find(block.name + "-top")
        top1 = Core.atlas.find(block.name + "-top1")
        block.clipSize = max(block.clipSize, (lightRadius + lightSinMag) * 2f * block.size)
    }

    override fun draw(build: HeatCrafter.HeatCrafterBuild) {
        super.draw(build)
        if (build.warmup > 0f && flameColor.a > 0.001f) {
            val g = 0.3f
            val r = 0.06f
            val cr = Mathf.random(0.1f)
            Draw.z(Layer.block + 0.01f)
            Draw.alpha(build.warmup)
            Draw.rect(top, build.x, build.y)
            Draw.alpha((1f - g + Mathf.absin(Time.time, 8f, g) + Mathf.random(r) - r) * build.warmup)
            Draw.tint(flameColor)
            Fill.circle(build.x, build.y, flameRadius + Mathf.absin(Time.time, flameRadiusScl, flameRadiusMag) + cr)
            Draw.color(1f, 1f, 1f, build.warmup)
            Fill.circle(build.x, build.y, flameRadiusIn + Mathf.absin(Time.time, flameRadiusScl, flameRadiusInMag) + cr)
            Draw.color()
            if(top1 != null && top1!!.found()) Draw.rect(top1, build.x, build.y)
        }
    }

    override fun drawLight(build: HeatCrafter.HeatCrafterBuild) {
            Drawf.light( build.team,  build.x,  build.y,  (lightRadius + Mathf.absin(lightSinScl, lightSinMag)) * build.warmup * build.block.size,  flameColor,  lightAlpha
        )
    }
}