package org.study2.todolist.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

//AndroidViewModel 은 액티비티와 수명을 같이한다 생성자로 application 객체를 받음
/* 뷰 모델 클래스는 액티비티와 수명을함께 하도록 만들어져 있습니다.
*  여기서 액티비티의 수명이란 화면 회전과 같이 액티비티가 파괴되고 재생성되는 것이 아닌
*  완전히 종료 되는 것을 말합니다.(onCreate 부터 onDestroy 까지 죽지안고 살아있습)
*  이러한 특징 덕분에 데이터를 뷰모델에서 관리하고 이를 액티비티나 프래그먼트가 관찰하도록
*  만드는 것이 좋습니다.*/
class MainViewModel(application: Application) : AndroidViewModel(application) {


    //    Room 데이터 베이스 databaseBuilder() 메서드에 전달하는 인자로는 Application 객체와
//    데이터 베이스클래스 ,데이터 베이스 이름이 필요합니다.
    private val db = Room.databaseBuilder(
        application,
        TodoDatabase::class.java, "todo"
    ).build()

    //    DB의 결과를 관찰할 수 있도록 하는 방법
    /* StateFlow 는 현재 상태와 새로운 상태 업데이트를 이를 관찰하는 곳에 보내는 데이터 흐름을 표현합니다.
    *  여기서 상태는 데이터를 말합니다. 상태를 UI에 노출시킬 때는 StateFlow를 사용합니다.
    *  value 프로퍼티를 통해서 현재 상태값을 읽을 수 있습니다. 상태를 업데이트하고 관찰하는 곳으로
    *  상태를 전달하려면 MutableStateFlow 클래스의 value 프로퍼티에 새값을 할당합니다.*/
    private val _items = MutableStateFlow<List<Todo>>(emptyList())
    val items: StateFlow<List<Todo>> = _items

    var selectedTodo: Todo? = null

    //    초기화시 모든 데이터를 읽어 옴 init 함수에서 뷰모델이 초기화될 때 모든 할일 데이터를 읽어서 StateFlow로 외부에 노출 하도록 합니다.
    init {
//        viewModelScope를 활용하여 suspend 메서드를 수행합니다.
        /* ViewModel 과 AndroidViewModel 클래스는 viewModelScope 코루틴 스코프를 제공
        *  launch 함수 내에서 suspend 메서드를 실행할 수 있고 이는 비동기로 동작함 (메인쓰레드 밖에서 동작)*/
        viewModelScope.launch {
//            Flew 객체는 collect로 현재 값을 가져올 수 있음
//            getAll() 메서드는 Flow<List<Todo> 타입을 반환하는데 이를 collect 함수로 현재 상태를 수집(관찰) 할수 있습니다.
            db.todoDao().getAll().collect { todos ->
                _items.value = todos
            }
        }
    }


    fun addTodo(text: String, date: Long) {
        viewModelScope.launch {
            db.todoDao().insert(Todo(text,date))
        }
    }

    fun updateTodo(text: String, date: Long) {
//        _items.value
//            .find { todo -> todo.id == id } // id로 수정할 객체를 찾습니다.
        selectedTodo?.let { todo ->  // 만약 찾았다면 title에 수정할 내용 date에 수정한 시간을 지정하여
            todo.apply {
                this.title = text
                this.date = date
            }
//                업데이트를 수행합니다.
            viewModelScope.launch {
                db.todoDao().update(todo)
            }
            selectedTodo = null
        }
    }

    fun deleteTodo(id: Long) {
        _items.value
            .find { todo -> todo.id == id }
            ?.let { todo ->
                viewModelScope.launch {
                    db.todoDao().delete(todo)
                }
                selectedTodo = null
            }
    }
}

/* 액티비티나 프래그먼트와 같은 뷰에서 데이터베이스처럼 복잡한 처리를 직접하는 것 보다 별도의 클래스에 복잡한
* 코드를 작성하고 뷰에서는 그것을 활용하는 것이 좋은 방법입니다. 안드로이드에는 그러한 용도에 적합한
* ViewModel 과 AndroidViewModel 클래스를 제공합니다. 두클래스의 차이는 Application 객체를 사용할수 없느냐
* 있느냐 입니다. Room ,데이터베이스를 사용하려면 앱의 Application 객체가 필요하므로 우리는 AndroidViwModel
* 클래스를 활용하기로 합니다.*/