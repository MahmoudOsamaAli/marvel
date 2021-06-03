package com.example.marvel.ui.dialogs

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.marvel.R
import com.example.marvel.callBacks.OnCharacterClicked
import com.example.marvel.databinding.FragmentSearchDialogBinding
import com.example.marvel.model.characters.ResultsItem
import com.example.marvel.ui.charactersActivity.CharactersActivity
import com.example.marvel.ui.dialogs.searchAdapter.SearchAdapter
import com.example.marvel.utils.Extensions.setNoLimitsWindow
import com.example.marvel.viewModels.MainViewModel

class SearchDialog : DialogFragment() ,OnCharacterClicked{

    private lateinit var binding: FragmentSearchDialogBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter:SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_search_dialog, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setWindowParams()
    }

    private fun setWindowParams() {
        setNoLimitsWindow()
        dialog?.window?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black_fade
                )
            )
        )
        dialog?.window?.setLayout(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        init()

    }

    private fun init() {
        initAdapter()
        binding.cancelDialog.setOnClickListener {
            dismiss()
        }
        initEditTextWatcher()
    }

    private fun initEditTextWatcher() {
        binding.searchEditText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null)adapter.getFilter().filter(p0.toString())
            }

        })
    }

    private fun initAdapter() {
        viewModel.charactersListLiveData.observe(viewLifecycleOwner, { data ->
            if (this::adapter.isInitialized) adapter.updateData(data)
            else {
                adapter = SearchAdapter(data,this)
                binding.charactersList.adapter = adapter
            }
        })
    }

    override fun onItemClicked(item: ResultsItem, view: View) {
        viewModel.startCharactersDetailedActivity(item,view,requireActivity() as CharactersActivity)
        dismiss()
    }

    companion object {
        private const val TAG = "SearchDialog"
    }
}