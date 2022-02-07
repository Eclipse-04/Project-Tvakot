package tvakot.world.draw

import arc.Core
import arc.graphics.Color
import arc.graphics.g2d.Draw
import arc.graphics.g2d.Fill
import arc.graphics.g2d.TextureRegion
import arc.math.Mathf
import arc.util.Time
import mindustry.graphics.Drawf
import mindustry.world.Block
import mindustry.world.consumers.ConsumeLiquid
import mindustry.world.consumers.ConsumeType
import tvakot.world.blocks.crafting.HeatCrafter


class DrawBoiler : DrawHeatBlock(){
    var inLiquidR : TextureRegion? = null
    var liquidR : TextureRegion? = null
    var topR : TextureRegion? = null
    var bubbles = 20
    var spread = 3f
    var timeScl = 60f
    var recurrence = 1f
    var radius = 3f
    var mistColor = Color.valueOf("ffffff")
    override fun draw(build: HeatCrafter.HeatCrafterBuild) {
        Draw.rect(build.block.region, build.x, build.y)
        val type = build.block as HeatCrafter
        val input = type.consumes.get<ConsumeLiquid>(ConsumeType.liquid).liquid
        var lerpInput = 0f
        lerpInput = Mathf.lerp(lerpInput,build.liquids.get(input) / build.block.liquidCapacity, 0.1f)
        if(inLiquidR != null && type.consumes.has(ConsumeType.liquid)){
            Drawf.liquid(inLiquidR, build.x, build.y,
                build.liquids.get(input) / (type.liquidCapacity * 3),
                input.color
            )
        }
        rand.setSeed(build.pos().toLong())
        for (i in 0 until bubbles) {
            val x: Float = rand.range(spread)
            val y: Float = rand.range(spread)
            val life: Float = 1f - (Time.time / timeScl + rand.random(build.warmup)) % recurrence
            if (life > 0) {
                Draw.color(mistColor,(1 - life) * lerpInput * (build.heatModule.heat / (build.block as HeatCrafter).heatCapacity) * 17f)
                Fill.circle(build.x + x, build.y + y, life * radius)
            }
        }
        Draw.alpha(1f)
        Draw.color()
        if(topR != null) Draw.rect(topR, build.x, build.y)
        Drawf.liquid(heated, build.x, build.y, build.heatFullness(), build.heatColor())
    }

    override fun load(block: Block) {
        super.load(block)
        val a = block.name
        inLiquidR = Core.atlas.find("$a-inLiquid")
        liquidR = Core.atlas.find("$a-liquid")
        topR = Core.atlas.find("$a-top")
    }
}