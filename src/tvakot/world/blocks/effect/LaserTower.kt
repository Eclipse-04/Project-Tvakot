package tvakot.world.blocks.effect

import arc.graphics.Color
import arc.graphics.g2d.Draw
import arc.graphics.g2d.Fill
import arc.graphics.g2d.Lines
import arc.math.Mathf
import arc.util.Time
import mindustry.content.Fx
import mindustry.content.Fx.chainLightning
import mindustry.entities.Damage
import mindustry.gen.Building
import mindustry.gen.Bullet
import mindustry.graphics.Drawf
import mindustry.graphics.Layer
import mindustry.graphics.Pal
import mindustry.world.Block
import tvakot.content.TvaBullets

open class LaserTower(name: String) : Block(name){
    var reloadTime = 0f
    var lightningColor: Color = Pal.lancerLaser
    var range = 220f
    var damageHit = 10f
    var rotDrawSpeed = 4f
    var sectors = 3
    var fulPerSector = 0.7f
    var orbRadius = 2f
    var sectorStroke = 1f
    var sectorOffset = 1f
    init{
        update = true
        solid = true
        hasItems = false
        hasPower = true
        consumesPower = true
        configurable = true
        saveConfig = true
    }
    inner class LaserTowerBuild : Building() {
        private var reload = 0f
        private var warmup = 0f
        private var orbRadiusDraw = 0f
        private var otherTo : Building = this
        override fun updateTile() {
            warmup = if(efficiency() > 0){
                Mathf.lerp(warmup, 1f, 0.05f)
            }else{
                Mathf.lerp(warmup, 0f, 0.1f)
            }
            orbRadiusDraw = Mathf.lerp(orbRadiusDraw, efficiency(), 0.05f)
            reload += edelta() * warmup
            if(reload >= reloadTime && otherTo.isValid){
                val tarRot = angleTo(otherTo)
                val tarDst = dst(otherTo)
                val hitBullet = object : Bullet(){
                    init{
                        damage = damageHit
                        type = TvaBullets.LaserTowerBulletType
                    }
                }
                Damage.collideLine(hitBullet, team, Fx.none, x, y, tarRot, tarDst)
                reload = 0f
                chainLightning.at(x, y, 0f, lightningColor, otherTo)
            }
        }
        override fun drawSelect() {
            super.drawSelect()
            if(otherTo.isValid){
                Drawf.dashLine(team.color, x, y, otherTo.x, otherTo.y)
                Drawf.select(otherTo.x, otherTo.y, size.toFloat() * 4, team.color)
                Drawf.dashCircle(x, y, range, team.color)
            }
        }
        override fun drawConfigure() {
            super.drawConfigure()
            drawSelect()
        }
        override fun onConfigureTileTapped(other : Building): Boolean {
            return when(other.block){
                this.block -> {
                    val dst = dst(other)
                    if(dst < range && team == other.team) otherTo = other
                    false
                }
                else -> {
                    deselect()
                    true
                }
            }
        }
        override fun draw(){
            super.draw()
            Draw.z(Layer.effect)
            Draw.color(lightningColor)
            Draw.alpha(orbRadiusDraw + 0.1f)
            Fill.circle(x, y, orbRadius * orbRadiusDraw)
            Lines.stroke(sectorStroke)
            for (i in 1..sectors){
                Lines.swirl(
                    x, y,
                    orbRadius * orbRadiusDraw + sectorOffset,
                    fulPerSector / sectors,
                    i * 360f / sectors + Time.time * rotDrawSpeed
                )
            }
        }
    }
}