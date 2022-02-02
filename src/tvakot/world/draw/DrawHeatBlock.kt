package tvakot.world.draw

import arc.Core
import arc.graphics.g2d.Draw
import arc.graphics.g2d.TextureRegion
import arc.math.Rand
import mindustry.graphics.Drawf
import mindustry.world.Block
import tvakot.world.blocks.crafting.HeatCrafter


open class DrawHeatBlock {

    val rand = Rand()
    lateinit var heated: TextureRegion

    open fun draw(build: HeatCrafter.HeatCrafterBuild) {
        Draw.rect(build.block.region, build.x, build.y, if (build.block.rotate) build.rotdeg() else 0f)
        Drawf.liquid(heated, build.x, build.y, build.heatFullness(), build.heatColor())
    }

    fun drawLight(build: HeatCrafter.HeatCrafterBuild) {}

    open fun load(block: Block) {
        val a = block.name
        heated = Core.atlas.find("$a-heated")
    }

    fun icons(block: Block): Array<TextureRegion> {
        return arrayOf(block.region)
    }

}