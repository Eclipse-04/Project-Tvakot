package tvakot.world.blocks.effect

import arc.graphics.Color
import arc.graphics.g2d.Draw
import arc.graphics.g2d.Fill
import arc.graphics.g2d.Lines
import arc.math.Mathf
import arc.util.Time
import mindustry.Vars.tilesize
import mindustry.content.Fx
import mindustry.content.Fx.chainLightning
import mindustry.entities.Damage
import mindustry.entities.bullet.BulletType
import mindustry.gen.Building
import mindustry.gen.Bullet
import mindustry.gen.Sounds
import mindustry.graphics.Drawf
import mindustry.graphics.Layer
import mindustry.graphics.Pal
import mindustry.world.Block
import mindustry.world.meta.Stat
import mindustry.world.meta.StatUnit
import tvakot.content.TvaBullets

open class LaserTower(name: String) : Block(name){
    var reloadTime = 0f
    var shootSound = Sounds.spark
    var lightningColor: Color = Pal.lancerLaser
    var range = 220f
    var damageHit = 10f
    var laserBullet: BulletType = TvaBullets.LaserTowerBulletType
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
    override fun setStats(){
        super.setStats()
        stats.add(Stat.damage, damageHit, StatUnit.perShot)
        stats.add(Stat.reload, 60f / reloadTime, StatUnit.perSecond)
        stats.add(Stat.range, range / 8, StatUnit.blocks)
    }
    override fun drawPlace(x: Int, y: Int, rotation: Int, valid: Boolean) {
        super.drawPlace(x, y, rotation, valid)
        Drawf.dashCircle(x * tilesize.toFloat(), y * tilesize.toFloat(), range, Pal.accent)
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
            val dst = dst(otherTo)
            if(reload >= reloadTime && otherTo.isValid && dst < range){
                val tarRot = angleTo(otherTo)
                val tarDst = dst(otherTo)
                val hitBullet = object : Bullet(){
                    init{
                        damage = damageHit
                        type = laserBullet
                    }
                }
                shootSound.at(x, y)
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
            }
            Drawf.dashCircle(x, y, range, team.color)
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