package com.vlg.teachgame

import androidx.fragment.app.Fragment

interface Navigator {
    fun startFragment(fragment: Fragment)
    fun showAd()
    fun loadInterstitialAd()
    fun destroyInterstitialAd()
}