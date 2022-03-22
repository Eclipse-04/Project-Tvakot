package tvakot.world.blocks.power

import arc.Core
import arc.graphics.Color
import arc.graphics.g2d.Draw
import arc.graphics.g2d.Fill
import arc.graphics.g2d.TextureRegion
import arc.math.Interp
import arc.math.Mathf
import arc.math.Rand
import arc.util.Time
import arc.util.Tmp
import mindustry.graphics.Drawf
import mindustry.graphics.Pal
import mindustry.type.Liquid
import mindustry.ui.Bar
import mindustry.world.blocks.power.PowerGenerator
import mindustry.world.consumers.ConsumeLiquid
import mindustry.world.consumers.ConsumeType

open class TurbineGenerator(name: String) : PowerGenerator(name) {
    var minLiquidRequired = 80f
    var mistColor = Color.white
    var bubbles = 20
    var spread = 2.5f
    var timeScl = 60f
    var recurrence = 1f
    var radius = 3f
    var rotorAmount = 1
    var spinAmount = 60f
    var interp = Interp.pow3In
    init {
        hasLiquids = true
        hasItems = false
    }
    var rotor = Array(16) { Core.atlas.find("error") }
    var rotorSpeed = Array(16) { 7f }
    lateinit var top: TextureRegion
    override fun load() {
        super.load()
        for(i in rotor.indices){
            rotor[i] = Core.atlas.find("$name-rotor$i")
        }
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
            totalTime += warmup
        }

        override fun draw() {
            super.draw()
            for(i in rotor.indices){
                if(!rotor[i].found()) break
                Drawf.spinSprite(rotor[i], x, y, rotorSpeed[i] * totalTime)
            }
            rand.setSeed(pos().toLong())
            if(warmup > 0) for(b in 0 until (bubbles * warmup).toInt()){
                val life: Float = (Time.time / timeScl + rand.random(warmup)) % recurrence
                Tmp.v1.trns(rand.random(360f) + interp.apply(life) * spinAmount * warmup, life * spread * 2)
                if(life > 0){
                    Draw.color(mistColor, 1 - life)
                    Fill.circle(Tmp.v1.x + x, Tmp.v1.y + y, life * radius)
                }
            }
            Draw.color()
            Draw.alpha(1f)
            Draw.rect(top, x, y)
        }
    }
}