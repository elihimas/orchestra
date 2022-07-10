package com.elihimas.orchestra.activities.adapers

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elihimas.orchestra.activities.adapers.SplashAdapter.SplashHolder

class SplashAdapter : RecyclerView.Adapter<SplashHolder>() {

    class SplashHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SplashHolder {
        return SplashHolder(parent)
    }

    override fun onBindViewHolder(holder: SplashHolder, position: Int) {
    }

    override fun getItemCount(): Int = 0

}
