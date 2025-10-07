package com.example.ref01.ui.legacy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ref01.databinding.FragmentComentariosBinding
import kotlinx.coroutines.launch

/**
 * Fragment legacy con ViewBinding + RecyclerView.
 * Usa ComentariosViewModel para manejar estado y lista.
 */
class ComentariosFragment : Fragment() {

    private var _binding: FragmentComentariosBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ComentariosViewModel by viewModels()
    private val adapter = CommentsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bookId = arguments?.getInt(ARG_BOOK_ID, 0) ?: 0
        viewModel.setBook(bookId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentComentariosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvComments.layoutManager = LinearLayoutManager(requireContext())
        binding.rvComments.adapter = adapter

        binding.btnAddComment.setOnClickListener {
            viewModel.addComment(binding.etComment.text?.toString().orEmpty())
            binding.etComment.text?.clear()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.comments.collect { list ->
                        adapter.submit(list)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val ARG_BOOK_ID = "book_id"

        fun newInstance(bookId: Int) = ComentariosFragment().apply {
            arguments = Bundle().apply { putInt(ARG_BOOK_ID, bookId) }
        }
    }
}
