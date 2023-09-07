package ua.od.acros.matusi.presentation.misc

import androidx.annotation.DrawableRes

interface ActionBarManager {
    fun setActionBarLogo(@DrawableRes drawable: Int)
    fun removeActionBarLogo()
}