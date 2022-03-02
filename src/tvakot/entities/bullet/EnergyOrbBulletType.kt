package tvakot.entities.bullet

import arc.graphics.Color
import arc.struct.Seq
import arc.util.Log
import mindustry.Vars
import mindustry.content.Fx
import mindustry.entities.Damage
import mindustry.entities.Units
import mindustry.game.Team
import mindustry.gen.*
import mindustry.gen.Unit
import mindustry.graphics.Pal

open class EnergyOrbBulletType : TvaBaseBulletType(){
    var lightningReload = 10f
    var maxTargets = 5
    var healEffect = Fx.heal
    var damageEffect = Fx.chainLightning
    var orbDamage = 0f
    var orbColor: Color = Pal.heal

    override fun update(b: Bullet) {
        super.update(b)
        if(b.timer(2, lightningReload)){
            val all = Seq<Healthc>()
            if (collidesGround || collidesAir) {
                Units.nearby(null, b.x, b.y, range) { other: Unit ->
                    if ((if (other.isFlying) collidesAir else collidesGround)) {
                        all.add(other)
                    }
                }
            }

            if (collidesGround && collidesTiles) {
                Units.nearbyBuildings(b.x, b.y, range) { b1: Building ->
                    if (b.team !== Team.derelict || Vars.state.rules.coreCapture) {
                        all.add(b1)
                    }
                }
            }

            all.sort { h: Healthc -> h.dst2(b.x, b.y) }
            val len = Math.min(all.size, maxTargets)

            for (i in 0 until len){
                var other = all[i]

                //lightning gets absorbed by plastanium
                val absorber = Damage.findAbsorber(b.team, b.x, b.y, other.x, other.y)
                if (absorber != null) {
                    other = absorber
                }
                if((other as Teamc).team() == b.team){
                    if(other.damaged() && healPercent > 0.001f){
                        other.heal(healPercent / 100f * other.maxHealth())
                        healEffect.at(other)
                        damageEffect.at(b.x, b.y, 0f, orbColor, other)
                        hitEffect.at(b.x, b.y, b.angleTo(other), orbColor)

                        if(other is Building){
                            Fx.healBlockFull.at(other.x, other.y, other.block.size.toFloat(), orbColor)
                        }
                    }
                } else {
                    if(other is Building){
                        other.damage(b.team, orbDamage)
                    }else{
                        other.damage(orbDamage)
                    }
                    if(other is Statusc){
                        other.apply(status, statusDuration)
                    }
                    hitEffect.at(other.x(), other.y(), b.angleTo(other), orbColor)
                    damageEffect.at(b.x, b.y, 0f, orbColor, other)
                    hitEffect.at(b.x, b.y, b.angleTo(other), orbColor)
                }
            }
        }
    }

}