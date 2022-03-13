package org.study2.todolist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import org.study2.todolist.data.MainViewModel
import org.study2.todolist.databinding.FragmentSecondBinding
import java.util.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private val viewModel by activityViewModels<MainViewModel>()

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selectedTodo?.let {
            binding.todoEditText.setText(it.title)
            binding.calendarView.date = it.date
        }
//CalendarView 에서 선택한 날짜를 저장할 Calendar 객체를 선언합니다.
        val calendar = Calendar.getInstance()
//        CalendarView 에서 날짜가 변경되면 setOnDateChangeLisntener 를 통해 년월일값을 얻습니다.
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            calendar.apply {
                set(Calendar.YEAR,year)
                set(Calendar.MONTH,month)
                set(Calendar.DAY_OF_MONTH,dayOfMonth)
            }
        }

        binding.doneFab.setOnClickListener {
            if (binding.todoEditText.text.toString().isNotEmpty()) {
                if (viewModel.selectedTodo != null) {
                    viewModel.updateTodo(binding.todoEditText.text.toString(),calendar.timeInMillis)
                }else {
//추가 또는 수정시 calendar 객체에 저장된 시간 정보를 적용합니다.
                viewModel.addTodo(binding.todoEditText.text.toString(),calendar.timeInMillis)
                }
                /*findNavController() 메서드는 이 프래그먼트가 속한 액티비티가 가지고 있는
                NavHostFragment 의 컨트롤러 객체를 찾습니다. popBackStack() 메서드로 이전 화면으로 돌아갈수 있습니다.*/
                findNavController().popBackStack()
            }
        }

        binding.deleteFab.setOnClickListener {
            viewModel.deleteTodo(viewModel.selectedTodo!!.id)
            findNavController().popBackStack()
        }
//        선택된 할 일이 없을때는 지우기 버튼 감추기
        if (viewModel.selectedTodo == null) {
            binding.deleteFab.visibility = View.GONE
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}