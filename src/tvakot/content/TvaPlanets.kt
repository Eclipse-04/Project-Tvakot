package tvakot.content

import arc.func.Prov
import arc.graphics.Color
import mindustry.content.Planets
import mindustry.ctype.ContentList
import mindustry.graphics.Pal
import mindustry.graphics.g3d.HexMesh
import mindustry.graphics.g3d.HexSkyMesh
import mindustry.graphics.g3d.MultiMesh
import mindustry.type.Planet
import tvakot.maps.AlexonPlanetGenerator

class TvaPlanets : ContentList {
    override fun load() {
        alexon = object : Planet("alexon", Planets.sun, 1f, 3) {
            init {
                generator = AlexonPlanetGenerator()
                meshLoader = Prov { HexMesh(this, 6) }
                atmosphereColor = Color.valueOf("75aafaae")
                startSector = 21
                alwaysUnlocked = true
                landCloudColor = atmosphereColor.cpy().a(0.5f)
                cloudMeshLoader = Prov {
                    MultiMesh(
                        HexSkyMesh(this, 11, 0.15f, 0.13f, 5, Color().set(Color.gray).mul(0.9f).a(0.75f), 2, 0.45f, 0.9f, 0.38f),
                        HexSkyMesh(this, 69, 0.9f, 0.11f, 5, Color().set(Pal.lancerLaser).mul(0.9f).a(0.3f), 2, 0.45f, 0.3f, 0.32f),
                        HexSkyMesh(this, 1, 0.6f, 0.16f, 5, Color.white.cpy().lerp(Color.gray, 0.55f).a(0.75f), 2, 0.45f, 1f, 0.41f)
                    )
                }
            }
        }
    }
    companion object {
        lateinit var alexon: Planet
    }
}