package com.amp.bemobile.view.features.transactions.list

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.amp.bemobile.R
import com.amp.bemobile.databinding.ActivityTransactionListBinding
import com.amp.bemobile.view.core.utils.viewBinding
import com.amp.bemobile.view.features.transactions.info.TransactionInfoFragment
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransactionListActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityTransactionListBinding::inflate)

    private val viewModel: TransactionListViewModel by viewModel()

    private lateinit var transactionAdapter: TransactionListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()
        setupList()

        lifecycleScope.launchWhenStarted { viewModel.transactionState.collect(::render) }
    }

    private fun setupToolbar() {
        binding.toolbar.title = getString(R.string.transactions)
        setSupportActionBar(binding.toolbar)
    }

    private fun setupList() {
        transactionAdapter = TransactionListAdapter { item ->
            viewModel.onTriggerEvent(TransactionListEvent.ClickTransaction(item))
        }

        binding.transactionList
            .addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        binding.transactionList.layoutManager = LinearLayoutManager(this)
        binding.transactionList.adapter = transactionAdapter
    }

    private fun render(state: TransactionListState) {
        when (state) {
            is TransactionListState.Load -> {
                binding.progressBar.isVisible = false
                transactionAdapter.populate(state.transactions)
            }
            TransactionListState.ShowError -> showErrorDialog()
            TransactionListState.ShowLoading -> binding.progressBar.isVisible = true
            is TransactionListState.ShowCurrentTransaction -> {
                TransactionInfoFragment.newInstance(state.transactions, state.total)
                    .show(supportFragmentManager, "transaction")
            }
        }
    }

    private fun showErrorDialog() {
        AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle(R.string.error_title)
            .setMessage(R.string.error_message)
            .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
            .create().show()
    }
}