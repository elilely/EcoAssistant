package com.example.ecoassistant

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import com.example.ecoassistant.databinding.ActivitySignInBinding
import com.example.ecoassistant.databinding.FragmentGuideBinding

class GuideFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_guide, container, false)
    }

}