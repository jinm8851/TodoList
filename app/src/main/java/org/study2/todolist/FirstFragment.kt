package org.study2.todolist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.study2.todolist.adapter.TodoListAdapter
import org.study2.todolist.data.MainViewModel
import org.study2.todolist.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    /* 프개그 먼트에서 activityViewModels() 확장함수를 사용하면 프래그먼트가 속한 액티비티의 생명주기를
    *  따르는 뷰 모델 클개스 인스턴스를 얻을 수 있습니다. 우리가 작성한 뷰 모델이 ActivityViewModel 클래스
    *  기반이기 때문이고 ViewModel 클래스 기반으로 만든 뷰 모델은 viewModel() 확장함수를 통해서 인스턴스를 얻습니다.*/
    private val viewModel by activityViewModels<MainViewModel>()

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /* layoutManager 설정으로 리사이크러뷰에 표시할 방법을 지정합니다. LinearLayoutManager 는
        * 일반 리스트 형태를 나타냅니다. 그 외에도 GridLayoutManager, StaggeredLayoutManager 가 있습니다.
        * 프래그먼트에서는 컨텍스트를 얻을때 requireContext() 메서드로 얻을 수 있습니다. 다음으로 어댑터를
        * 생성하고 리사이크러뷰에 연결해줍니다*/
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val todoListAdapter = TodoListAdapter { todo ->
//            클릭시처리
            viewModel.selectedTodo = todo
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)

        }
        binding.recyclerView.adapter = todoListAdapter



        /* 뷰 모델 객체안에 items 에 DB의 정보가 담길텐데 이르 collect() 함수로 수집을 합니다.
        *  collect() 함수는 suspend 함수로 지연 실행 됩니다. 따라서 코루틴스코프가 필요하며
        * 프레그먼트에서는 lifecycleScope 프로퍼티를 통해 쉽게 코루틴 스코프를 사용할 수 있습니다.*/
        lifecycleScope.launch {
            /* repeatOnLifecycle(Lifecycle.State.STARTED) 함수를 통해서 프래그먼트가 시작된 경우에만
            * Flow 의 수집을 시작하도록 할 수 있습니다. 원래는 생명주기에 맞게 Flow의 수집을 취소해야 하지만
            * 그러한 수고를 덜수 있습니다. 이 함수는 lifecycle-runtime-ktx:2.4.0-alpha01 이상에서 지원합니다.*/
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.items.collect {
                    Log.d("FirstFragment",it.toString())

                    /* 표시할 아이템 리스트는 아댑터의 submitList() 메서드에 전달합니다. 이메서드는 ListAdapter클래스가
                    * 제공하는 메서드 입니다 자동으로 변경점을 비교하고 번경이 일어난 아이템을 교체합니다.*/
                    todoListAdapter.submitList(it)
                }
            }
        }

        binding.addFab.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}