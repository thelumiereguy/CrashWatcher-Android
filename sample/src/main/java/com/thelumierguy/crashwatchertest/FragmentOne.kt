package com.thelumierguy.crashwatchertest

import android.os.Bundle
import androidx.fragment.app.Fragment


private const val ARG_PARAM1 = "paramOne1"
private const val ARG_PARAM2 = "paramOne2"


class FragmentOne : Fragment(R.layout.fragment_one) {


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentOne().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}