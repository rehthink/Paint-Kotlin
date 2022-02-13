package com.rehthinkdev.paint

import android.graphics.Path

class InkPath(

    var color: Int,
    var emboss: Boolean,
    var blur: Boolean,
    //var isDrawing: Boolean,
    var strokeWidth: Int,
    path: Path
)

    {

        var path: Path

    init {
        this.path = path
    }
}
