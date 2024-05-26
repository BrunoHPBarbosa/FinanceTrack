
package com.hacksprint.financetrack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText

class CreateCategoryBottomSheet(
    private val onCreateClicked: (String) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.create_category_bottom_sheet, container, false)

        val btnCreateCategory = view.findViewById<Button>(R.id.btn_category_create)
        val etCategoryName = view.findViewById<TextInputEditText>(R.id.et_category_name)

        btnCreateCategory.setOnClickListener {

            if(etCategoryName.text?.isNotEmpty() == false) {
                showMessages("Category is required")

            } else {
                val categoryName = etCategoryName.text.toString().trim()
                onCreateClicked(categoryName)
                dismiss()
            }
        }

        return view
    }

    private fun showMessages(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}
