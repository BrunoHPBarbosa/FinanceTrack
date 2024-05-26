package com.hacksprint.financetrack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class InfoBottomSheet(
    private val title: String,
    private val message: String,
    private val action: String,
    private val onActionClicked: () -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.info_bottom_sheet, container, false)

        val tvInfoTitle = view.findViewById<TextView>(R.id.tv_info_title)
        val tvMessage = view.findViewById<TextView>(R.id.tv_info_message)
        val btnAction = view.findViewById<Button>(R.id.btn_info_delete)

        tvInfoTitle.text = title
        tvMessage.text = message
        btnAction.text = action

        btnAction.setOnClickListener {
            onActionClicked.invoke()
            dismiss()
        }
        return view
    }
}