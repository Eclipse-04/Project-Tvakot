package tvakot

import tvakot.content.*
import mindustry.mod.*

class Tvakot : Mod(){

    init{

    }

    override fun loadContent(){
        TvaBlocks().load()
        TvaBullets().load()
    }
}
