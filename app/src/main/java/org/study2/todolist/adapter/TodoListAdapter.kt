package org.study2.todolist.adapter

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.study2.todolist.data.Todo
import org.study2.todolist.databinding.ItemTodoBinding

class TodoDiffutilCallback : DiffUtil.ItemCallback<Todo>() {

    // 7. 아이템이 같음을 판단하는 규칙 id가 다르면 변경된 것으로 판단하여 해당 아이템은 교체됩니다.
    override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem.id == newItem.id
    }

    //  8. 내용을 비교하는 규칙 DB는 id가 유니크하기 때문에 여기서는 동일한 규칙을 적용했습니다.
    override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem.id == newItem.id
    }
}

class TodoListAdapter(
    private val onClick: (Todo) -> Unit,  // 9.생성자로 아이템이 클릭되었을때 처리할 함수를 인자로 받습니다 이함수는 6 뷰홀더로 전달됩니다.
) : ListAdapter<Todo, TodoListAdapter.TodoViewHolder>(TodoDiffutilCallback()) {
//    10. ListAdapter<Todo, TodoListAdapter.TodoViewHolder>를 상속할때 아이템클래스와 뷰홀더클래스타입을 제네릭으로 지정합니다.그리고 인자에 TodoDiffutilCallback을 구현한 객체를 전달합니다.

    private lateinit var binding: ItemTodoBinding // 11. 바인딩 객체를 지정할 변수를 선언합니다.

    //    12.onCreateViewHolder() 메서드는 뷰 홀더를 생성하는 로직을 작성합니다. 우리는 바인딩 객체를 얻고 뷰 홀더를 생성합니다.
//    13.xml 레이아웃 파일을 바인딩 객체로 변경할 때는 액티비티 이외의 클래스에서는 LayoutInflater클래스와 컨텍스트가 있으면 가능합니다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//    14.뷰홀더 객체를 생성합니다. 전달 인자는 바인딩객체와 , 클릭시 수행할 함수입니다.
        return TodoViewHolder(binding, onClick)
    }

    //  15. onBindViewHolder() 메서드는 화면에 각 아이템이 보여질 때 마다 호출됩니다. 여기에서 실제로 보여질 내용을 설정합니다.
//    아이템이 계속 바뀌므로 바뀔 때 마다 클릭 이벤트 설정도 다시 해줍니다.
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.setOnClickListener(getItem(position))
    }

    class TodoViewHolder(
        private val binding: ItemTodoBinding, // 2.바인딩객체와
        private val onClick: (Todo) -> Unit,  //3. 클릭되었을때 처리를 할 함수를 전달합니다.
    ) : RecyclerView.ViewHolder(binding.root) { //1 어뎁터는 RecyclerView.ViewHolder 클래스를 상속하고
        //아이템 레이아웃의 뷰 인스턴스를 인자로 전달합니다. 뷰바인딩 객체의 root프로퍼티로 얻을수있습니다.

        //        5.bind() 메서드는 할 일 객체를 인자로 잔달 받아 실제로 화면에 표시합니다.
        fun bind(todo: Todo) {
            binding.text1.text = todo.title
//    6.Long 형태의 시간을 DateFormat.format() 함수로 년월일 형태로 변환하여 표시합니다.
            binding.text2.text = DateFormat.format("yyyy/MM/dd", todo.date)
        }

        //        4. 전달된 함수는 바인딩 객체가 클릭되면 수행되도록 하는 setOnClickListener()메서드를 작성합니다.
        fun setOnClickListener(todo: Todo) {
            binding.root.setOnClickListener {
                onClick(todo)
            }
        }
    }
}