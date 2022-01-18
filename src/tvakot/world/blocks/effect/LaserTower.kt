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
    init{
        update = true
        solid = true
        hasItems = true
        hasPower = true
        consumesPower = true
        configurable = true
    }
    inner class LaserTowerBuild : Building() {
        private var reload = 0f
        private var warmup = 0f
        private var orbRadius = 2f
        private var otherTo : Building = this
        override fun updateTile() {
            warmup = if(efficiency() > 0){
                Mathf.lerp(warmup, 1f, 0.1f)
            }else{
                Mathf.lerp(warmup, 0f, 0.1f)
            }
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
            Fill.circle(x, y, orbRadius * warmup * efficiency())
            for (i in 1..4){
                Lines.swirl(x, y, orbRadius * 1.8f * warmup * efficiency(), 0.15f, i * 90f + Time.time * 4)
            }
        }
    }
}