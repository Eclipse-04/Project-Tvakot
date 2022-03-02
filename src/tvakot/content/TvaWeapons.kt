package tvakot.content

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
    }
    companion object {
        lateinit var smallRapidRocket: Weapon
    }
}