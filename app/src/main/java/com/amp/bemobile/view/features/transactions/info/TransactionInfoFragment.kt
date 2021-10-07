package com.amp.bemobile.view.features.transactions.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.amp.bemobile.R
import com.amp.bemobile.databinding.TransactionInfoFragmentBinding
import com.amp.bemobile.domain.features.transactions.models.TransactionView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TransactionInfoFragment : BottomSheetDialogFragment() {

    private var _binding: TransactionInfoFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TransactionInfoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val transactions = arguments?.getParcelableArrayList<TransactionView>(PARAM_TRANSACTIONS)

        binding.title.text = transactions?.first()?.sku ?: ""
        binding.total.text = getString(R.string.total, arguments?.getString(PARAM_TOTAL) ?: "")

        val transactionAdapter =
            TransactionInfoAdapter(transactions?.toMutableList() ?: mutableListOf())

        binding.transactionList.layoutManager = GridLayoutManager(activity, 5)
        binding.transactionList.adapter = transactionAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val PARAM_TRANSACTIONS = "PARAM_TRANSACTIONS"
        private const val PARAM_TOTAL = "PARAM_TOTAL"

        fun newInstance(
            transactions: List<TransactionView>,
            total: String
        ): TransactionInfoFragment =
            TransactionInfoFragment().apply {
                this.arguments = Bundle().apply {
                    putParcelableArrayList(
                        PARAM_TRANSACTIONS,
                        ArrayList<TransactionView>().apply { addAll(transactions) })
                    putString(PARAM_TOTAL, total)
                }
            }
    }
}