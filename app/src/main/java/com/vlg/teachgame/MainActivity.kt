package com.vlg.teachgame

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.vlg.teachgame.state.LearnFragment

class MainActivity : AppCompatActivity(), Navigator {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startFragment(LearnFragment())
    }

    override fun startFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment).commit()
    }

    override fun showAd() {
        TODO("Not yet implemented")
    }

    override fun loadInterstitialAd() {
        TODO("Not yet implemented")
    }

    override fun destroyInterstitialAd() {
        TODO("Not yet implemented")
    }
}