package tvakot.content

import mindustry.content.Fx
import mindustry.ctype.ContentList
import mindustry.gen.Sounds
import mindustry.type.Weapon

class TvaWeapons : ContentList{
    override fun load() {
        smallRapidRocket = Weapon("tvakot-mini-rocket-launcher").apply{
            shots = 2
            shotDelay = 2f
            spacing = 1.75f
            reload = 10f
            x = 7f
            y = -5.25f
            shootSound = Sounds.missile
            inaccuracy = 10f
            bullet = TvaBullets.standardHomingMissle
        }

        diarrhea = (Weapon("tvakot-diarrhea").apply{
            reload = 180f
            y = -5f
            x = 0f
            shots = 5
            spacing = 72f
            shotDelay = 3f
            rotate = true
            shootSound = Sounds.missile
            ejectEffect = Fx.shootBig
            bullet = TvaBullets.standardHomingMissle
        })
    }
    companion object {
        lateinit var smallRapidRocket: Weapon
        lateinit var diarrhea: Weapon
    }
}