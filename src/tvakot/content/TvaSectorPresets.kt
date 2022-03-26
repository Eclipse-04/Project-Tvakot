package tvakot.content

import mindustry.ctype.ContentList
import mindustry.type.SectorPreset

class TvaSectorPresets : ContentList {
    override fun load() {

        surface = object : SectorPreset("surface", TvaPlanets.alexon, 21) {
            init {
                alwaysUnlocked = true
                addStartingItems = true
                captureWave = 30
                difficulty = 2f
            }
        }
        xaopnenOutpost = object : SectorPreset("xaopnen-outpost", TvaPlanets.alexon, 34) {
            init {
                captureWave = 30
                difficulty = 3f
            }
        }
    }
    companion object {
        lateinit var surface: SectorPreset
        lateinit var xaopnenOutpost: SectorPreset
    }
}