package com.zhao.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TestFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TestFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("${param1}","onCreate")
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.e("${param1}","onCreateView")
        return inflater.inflate(R.layout.fragment_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("${param1}","onViewCreated")
        val textView : TextView = view.findViewById(R.id.textView)
        Log.e("${param1}","$param1")
        textView.text = param1
        if (param1 == "testFragment1"){
            view.setBackgroundColor(ContextCompat.getColor(view.context,R.color.purple_200))
        }else{
            view.setBackgroundColor(ContextCompat.getColor(view.context,R.color.teal_200))
        }
        val button : Button = view.findViewById(R.id.button)
        button.setOnClickListener {
            Log.e("${param1}","$param1")
            if (param1 == "testFragment1"){
                (requireActivity() as MainActivity).cutFragment(1)
            }else{
                (requireActivity() as MainActivity).cutFragment(0)
            }
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.e("${param1}","onViewStateRestored")
    }

    /*override fun onStart() {
        super.onStart()
        Log.e("${param1}","onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.e("${param1}","onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.e("${param1}","onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.e("${param1}","onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("${param1}","onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("${param1}","onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.e("${param1}","onDetach")
    }*/

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TestFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TestFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.e(param1,hidden.toString())
    }

}