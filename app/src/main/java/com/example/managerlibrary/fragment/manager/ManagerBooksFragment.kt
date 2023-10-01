package com.example.managerlibrary.fragment.manager

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.managerlibrary.dao.BookDAO
import com.example.managerlibrary.dto.BookDTO
import com.example.managerlibrary.adapter.BooksAdapter
import com.example.managerlibrary.adapter.Top10Adapter
import com.example.managerlibrary.databinding.FragmentManagerBooksBinding
import com.example.managerlibrary.ui.manager.AddBookActivity
import com.example.managerlibrary.ui.manager.AddCategoryBooksActivity
import com.example.managerlibrary.viewmodel.SharedViewModel

class ManagerBooksFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var bookDAO: BookDAO
    lateinit var listBook: ArrayList<BookDTO>
    lateinit var adapter: BooksAdapter
    private lateinit var sharedViewModel: SharedViewModel

    private var _binding: FragmentManagerBooksBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentManagerBooksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.managerBooksRecyclerView.setHasFixedSize(true)
        binding.managerBooksRecyclerView.layoutManager = LinearLayoutManager(context)
        bookDAO = BookDAO(requireContext())
        listBook = bookDAO.getAllBook()

        if (!listBook.isEmpty()) {
            adapter = BooksAdapter(requireContext(), listBook)
            binding.managerBooksRecyclerView.adapter = adapter
            binding.managerBooksRecyclerView.visibility = View.VISIBLE
            adapter.notifyDataSetChanged()
        }
        val data = arguments?.getString("ok")
        if (data.equals("bookOK")) {
            refreshList()
        }

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        sharedViewModel.searchText.observe(viewLifecycleOwner, Observer { newText ->
            // Cập nhật giao diện hoặc thực hiện tìm kiếm với `newText`
            var filterList = ArrayList<BookDTO>()
            for (book in listBook) {
                if (book.name.contains(newText, ignoreCase = true)) {
                    filterList.add(book)
                }
            }

            //update to adapter
            binding.managerBooksRecyclerView.adapter = BooksAdapter(requireContext(), filterList)
            adapter.notifyDataSetChanged()

        })

        binding.fabAddBooks.setOnClickListener() {
            Intent(requireContext(), AddBookActivity::class.java).also {
                startActivity(it)
            }
        }

    }



    private fun refreshList() {
        listBook.clear()
        listBook = bookDAO.getAllBook()
        binding.managerBooksRecyclerView.adapter = BooksAdapter(requireContext(), listBook)
        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}