package tvakot.world.blocks.distribution

import arc.scene.ui.Label
import arc.scene.ui.layout.Table
import arc.util.io.Reads
import arc.util.io.Writes
import tvakot.world.blocks.TvaHeatBlock


open class HeatRegulator(name: String) : TvaHeatBlock(name) {
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
        override fun write(write: Writes) {
            super.write(write)
            write.f(config())
        }
        override fun read(read: Reads, revision: Byte) {
            super.read(read, revision)
            minVent = read.f()
        }
    }
}