package tvakot.content

import arc.func.Prov
import arc.graphics.Color
import mindustry.content.Fx
import mindustry.ctype.ContentList
import mindustry.entities.bullet.LaserBoltBulletType
import mindustry.gen.Sounds
import mindustry.graphics.Pal
import mindustry.type.UnitType
import mindustry.type.Weapon
import mindustry.type.ammo.PowerAmmoType
import tvakot.entities.units.DroneUnitEntity


class TvaUnitTypes : ContentList{
    override fun load() {
        healDrone = object : UnitType("heal-drone") {
            init {
                speed = 3.4f
                accel = 0.07f
                drag = 0.1f
                flying = true
                rotateSpeed = 8.5f
                health = 105f
                engineOffset = 3f
                engineSize = 2f
                faceTarget = true
                constructor = Prov{ DroneUnitEntity() }
                engineColor = Pal.heal
                ammoType = PowerAmmoType(1000f)
                isCounted = false
                weapons.add(Weapon("drone-healer-cannon").apply {
                    reload = 20f
                    x = 0f
                    mirror = false
                    shootSound = Sounds.lasershoot
                    ejectEffect = Fx.none
                    bullet = LaserBoltBulletType(5.2f, 3f).apply{
                        lifetime = 18f
                        shootEffect = Fx.none
                        healPercent = 5f
                        collidesTeam = true
                        backColor = Pal.heal
                        frontColor = Color.white
                    }
                })
            }
        }
    }
    companion object {
        lateinit var healDrone: UnitType
    }
}