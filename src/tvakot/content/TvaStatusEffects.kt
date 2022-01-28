package tvakot.content

import mindustry.content.StatusEffects
import mindustry.ctype.ContentList
import mindustry.graphics.Pal
import mindustry.type.StatusEffect

class TvaStatusEffects : ContentList {
    override fun load() {
        pulseShock = object : StatusEffect("pulse-shocked") {
            init {
                speedMultiplier = 0.3f
                reloadMultiplier = 0.7f
                transitionDamage = 10f
                damage = 0.1f
                color = Pal.lancerLaser
            }
        }
        StatusEffects.wet.affinities.add(pulseShock)
    }
    companion object {
        lateinit var pulseShock : StatusEffect
    }
}