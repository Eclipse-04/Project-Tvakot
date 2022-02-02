package tvakot.world.blocks.defensive.turret

import arc.Core
import arc.graphics.Color
import arc.math.Mathf
import mindustry.entities.bullet.BulletType
import mindustry.graphics.Pal
import mindustry.ui.Bar
import mindustry.world.blocks.defense.turrets.ItemTurret
import mindustry.world.meta.BlockStatus


open class OverdriveTurret(name: String) : ItemTurret(name){
    var overdriveMax = 0f
    var heatPerShot = 0f
    var coolingAmount = 0f
    override fun setBars() {
        super.setBars()
        bars.add("efficiency") { entity: OverdriveTurretBuild ->
            Bar(
                {
                    if(entity.canShoot){
                        return@Bar Core.bundle.get("bar.tvakot-heatoverload")
                    }else return@Bar Core.bundle.get("bar.tvakot-overheat")
                },
                {entity.getBarColor()},
                {entity.overdriveCurrent / overdriveMax}
            )
        }
    }
    inner class OverdriveTurretBuild : ItemTurretBuild() {
        var overdriveCurrent = 0f
        var canShoot = true
        fun getBarColor(): Color {
            val a = Color.valueOf("ffd37f")
            return a.lerp(Pal.redderDust, overdriveCurrent / overdriveMax)
        }
        override fun updateTile(){
            super.updateTile()
            heat = overdriveCurrent
            when(Mathf.clamp(overdriveCurrent / overdriveMax)){
                1f -> canShoot = false
                0f -> canShoot = true
            }
            if(overdriveCurrent >= 0) {
                overdriveCurrent -= if (canShoot) {
                    coolingAmount
                } else coolingAmount * 3
            }
        }

        override fun shoot(type: BulletType) {
            super.shoot(type)
            overdriveCurrent += heatPerShot
        }

        override fun baseReloadSpeed(): Float {
            return efficiency() * if(canShoot) 1f else 0f
        }

        override fun status() : BlockStatus{
            if(!canShoot){
                return BlockStatus.noOutput
            }
            return cons.status()
        }
    }
}