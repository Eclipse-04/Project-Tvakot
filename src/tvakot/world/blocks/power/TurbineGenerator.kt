package tvakot.world.blocks.power

import arc.Core
import arc.graphics.g2d.Draw
import arc.graphics.g2d.Fill
import arc.graphics.g2d.TextureRegion
import arc.math.Mathf
import arc.math.Rand
import arc.util.Log
import arc.util.Time
import mindustry.graphics.Drawf
import mindustry.graphics.Pal
import mindustry.type.Liquid
import mindustry.ui.Bar
import mindustry.world.blocks.power.PowerGenerator
import mindustry.world.consumers.ConsumeLiquid
import mindustry.world.consumers.ConsumeType

open class TurbineGenerator(name: String) : PowerGenerator(name) {
    var minLiquidRequired = 80f
    var bubbles = 20
    var spread = 3f
    var timeScl = 60f
    var recurrence = 1f
    var radius = 3f
    var rotorAmount = 1
    init {
        hasLiquids = true
        hasItems = false
    }
    var rotor: Array<TextureRegion?> = arrayOf(null, null)
    lateinit var top: TextureRegion
    override fun load() {
        super.load()
        rotor[0] = Core.atlas.find("$name-rotor0")
        rotor[1] = Core.atlas.find("$name-rotor1")
        top = Core.atlas.find("$name-top")
    }
    override fun setBars() {
        super.setBars()
        bars.add("heat-satisfaction") { entity: TurbineGenerator.TurbineGeneratorBuild ->
            Bar(
                { Core.bundle.format("bar.tvakot-heat-satisfaction", (entity.satisfaction() * 100).toInt()) },
                { Pal.accent },
                { entity.satisfaction() }
            )
        }
    }
    inner class TurbineGeneratorBuild : GeneratorBuild() {
        val input: Liquid = consumes.get<ConsumeLiquid>(ConsumeType.liquid).liquid
        var warmup = 0f
        var totalTime = 0f
        var rand = Rand()
        fun satisfaction(): Float{
            return Mathf.clamp(liquids.get(input) / minLiquidRequired)
        }
        override fun updateTile() {
            super.updateTile()
            productionEfficiency = satisfaction()
            warmup = Mathf.lerp(warmup, productionEfficiency, 0.1f)
            totalTime += warmup * 10
            Log.info(rotor.size)
            Log.info(rotorAmount)
        }

        override fun draw() {
            super.draw()
            if(rotor[0]!!.found())Drawf.spinSprite(rotor[0], x, y, totalTime)
            if(rotor[1]!!.found())Drawf.spinSprite(rotor[1], x, y, -totalTime)
            rand.setSeed(pos().toLong())
            for (i in 0 until bubbles) {
                val xB: Float = rand.range(spread)
                val yB: Float = rand.range(spread)
                val life: Float = 1f - (Time.time / timeScl + rand.random(warmup)) % recurrence
                if (life > 0) {
                    Draw.color(input.color, life * (liquids.get(input) / liquidCapacity) * 3f)
                    Fill.circle(xB + x, yB + y, (1 - life) * radius)
                }
            }
            Draw.color()
            Draw.alpha(1f)
            Draw.rect(top, x, y)
        }
    }
}