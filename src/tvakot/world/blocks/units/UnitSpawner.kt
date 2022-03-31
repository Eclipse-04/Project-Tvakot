package tvakot.world.blocks.units

import arc.Core
import arc.graphics.g2d.Draw
import arc.graphics.g2d.TextureRegion
import arc.math.Mathf
import arc.struct.Seq
import mindustry.Vars
import mindustry.Vars.tilesize
import mindustry.content.Fx
import mindustry.content.UnitTypes
import mindustry.entities.Effect
import mindustry.gen.Building
import mindustry.gen.Unit
import mindustry.graphics.Drawf
import mindustry.graphics.Layer
import mindustry.graphics.Pal
import mindustry.ui.Bar
import mindustry.ui.Fonts
import mindustry.world.Block
import tvakot.entities.units.DroneUnitEntity

open class UnitSpawner(name: String): Block(name) {
    var timeConstruct = 180f
    var constructUnit = UnitTypes.flare
    var unitAmount = 3
    var range = 0f
    var shake = 3.5f
    var spawnEffect = Fx.flakExplosion
    lateinit var topRegion: TextureRegion
    override fun setBars() {
        super.setBars()
        bars.add("progress") { entity: UnitSpawnerBuild ->
            Bar("bar.progress", Pal.accent) { entity.progress / timeConstruct}
        }
        bars.add("units") { e: UnitSpawnerBuild ->
            Bar(
                {
                    Core.bundle.format(
                        "bar.unitcap",
                        Fonts.getUnicodeStr(constructUnit.name),
                        e.units.size,
                        unitAmount
                    )
                },
                { Pal.power }
            ) { e.units.size / unitAmount.toFloat() }
        }
    }
    init {
        update = true
        hasPower = true
        hasItems = true
        solid = true
    }
    override fun drawPlace(x: Int, y: Int, rotation: Int, valid: Boolean) {
        super.drawPlace(x, y, rotation, valid)
        Drawf.dashCircle((x * tilesize).toFloat(), (y * tilesize).toFloat(), range, Pal.accent)
    }
    override fun load() {
        super.load()
        topRegion = Core.atlas.find("$name-top")
    }
    open inner class UnitSpawnerBuild: Building() {
        var progress = 0f
        var warmup = 0f
        var totalProgress = 0f
        var units = Seq<Unit>()
        override fun updateTile() {
            super.updateTile()
            warmup = if(units.size < unitAmount && consValid()) Mathf.lerp(warmup, efficiency(), 0.07f) else Mathf.lerp(warmup, 0f, 0.07f)
            if(progress >= timeConstruct && units.size < unitAmount){
                progress %= timeConstruct
                consume()
                val u = constructUnit.spawn(team, this) as DroneUnitEntity
                u.spawnerBuilding = pos()
                spawnEffect.at(this)
                units.add(u)
                Effect.shake(shake, shake, this)
            }
            if(consValid()) {
                progress += edelta() * Vars.state.rules.unitBuildSpeed(team) * warmup
                totalProgress += edelta() * Vars.state.rules.unitBuildSpeed(team) * warmup
            }
            for (i in units){
                if(i == null) continue
                if (i.dead) {
                    units.remove(i)
                }
            }
        }
        override fun drawSelect() {
            super.drawSelect()
            Drawf.dashCircle(x, y, range, team.color)
        }

        override fun shouldConsume(): Boolean {
            return super.shouldConsume() && units.size < unitAmount
        }
        override fun draw() {
            super.draw()
            Draw.draw(
                Layer.blockOver
            ) { Drawf.construct(this, constructUnit, 0f, progress / timeConstruct, warmup, totalProgress) }
            Draw.z(Layer.blockOver + 0.1f)
            Draw.rect(topRegion, x, y)
        }
    }
}