package tvakot

import tvakot.content.*
import mindustry.mod.*

class Tvakot : Mod(){

    init{

    }

    override fun loadContent(){
        TvaStatusEffects().load()
        TvaBullets().load()
        TvaItems().load()
        TvaBlocks().load()
        TvaTechTree().load()
    }
}
