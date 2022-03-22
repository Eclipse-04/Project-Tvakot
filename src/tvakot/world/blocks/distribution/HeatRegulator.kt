package tvakot.world.blocks.distribution

import arc.scene.ui.Label
import arc.scene.ui.layout.Table
import tvakot.world.blocks.TvaHeatBlock


open class HeatRegulator(name: String) : TvaHeatBlock(name) {
    //TODO fix the shitty configure (i was dumb back then)
    init {
        configurable = true
        saveConfig = true
        outputHeat = false
    }
    inner class HeatRegulatorBuild : TvaHeatBlockBuild(){
        var minVent = 0f
        override fun config(): Float {
            return if(lastConfig != null) lastConfig as Float else minVent
        }
        override fun buildConfiguration(table: Table) {
            val label = Label(config().toString())
            table.table {
                table.slider(0f, heatCapacity, 10f, config(), false) { f ->
                    configure(f)
                    label.setText(config().toString())
                }
                table.row()
                table.add(label)
            }
        }
        override fun appectHeat(source: TvaHeatBlockBuild?): Boolean {
            return if(source != null){
                source.heatModule.heat > config()
            } else false
        }
    }
}