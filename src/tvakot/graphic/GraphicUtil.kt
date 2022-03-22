package tvakot.graphic

import arc.Core
import arc.graphics.g2d.TextureRegion
import arc.math.Mathf


class GraphicUtil {

    companion object {
        /**@author Xeloboyo
         * code from pu, Xelo approves™
         */
        fun getRegions(region: TextureRegion, w: Int, h: Int, tilesize: Int): Array<TextureRegion?> {
            val size = w * h
            val regions = arrayOfNulls<TextureRegion>(size)
            val tileW = (region.u2 - region.u) / w
            val tileH = (region.v2 - region.v) / h
            for (i in 0 until size) {
                val tileX = (i % w).toFloat() / w
                val tileY = (i / w).toFloat() / h
                val reg = TextureRegion(region)

                //start coordinate
                reg.u = Mathf.map(tileX, 0f, 1f, reg.u, reg.u2) + tileW * 0.01f
                reg.v = Mathf.map(tileY, 0f, 1f, reg.v, reg.v2) + tileH * 0.01f
                //end coordinate
                reg.u2 = reg.u + tileW * 0.98f
                reg.v2 = reg.v + tileH * 0.98f
                reg.height = tilesize
                reg.width = reg.height
                regions[i] = reg
            }
            return regions
        }

        fun getRegions(region: TextureRegion, w: Int, h: Int): Array<TextureRegion?> {
            return getRegions(region, w, h, 32)
        }

        /**@author Sk7725
         * hehe, approved™
         */

        fun cameraXOffset(x: Float, height: Float): Float {
           return (x - Core.camera.position.x) * height
        }

        fun cameraYOffset(y: Float, height: Float): Float {
            return (y - Core.camera.position.y) * height
        }
    }
}