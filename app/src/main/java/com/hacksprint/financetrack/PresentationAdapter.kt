package com.hacksprint.financetrack

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

// adapter que infla os frames de apresentacao inicial
class PresentationAdapter (fragmentActivity: FragmentActivity):FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FrameLayoutFragment(R.layout.frame_img1)
            1 -> FrameLayoutFragment(R.layout.frame_img2)



            else -> throw IllegalArgumentException("invalido,$position")
        }
    }

}