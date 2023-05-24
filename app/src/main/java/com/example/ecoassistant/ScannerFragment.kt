package com.example.ecoassistant

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.example.ecoassistant.databinding.FragmentScannerBinding

class ScannerFragment : Fragment() {

    private var binding1: FragmentScannerBinding? = null
    private val binding2 get() = binding1!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding1 = FragmentScannerBinding.inflate(inflater, container, false)
        return binding2.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding1 = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding2.scannerRules.text = HtmlCompat.fromHtml(getString(R.string.scanner_rules), HtmlCompat.FROM_HTML_MODE_LEGACY)

        binding1?.openScannerBtn?.setOnClickListener {
            val intent = Intent(activity, ScannerActivity::class.java)
            startActivity(intent)
        }
    }
}