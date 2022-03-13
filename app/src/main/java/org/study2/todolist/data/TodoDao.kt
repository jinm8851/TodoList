package org.study2.todolist.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

//Dao 객체는 @Dao 주석을 추가한 interface로 생성합니다.
@Dao
interface TodoDao {

/*  데이터를 가져올 때는 @Query 주석에 SQL 쿼리를 작성합니다. 쿼리는 최근 날짜가 위로 오도록 date 의
*   내림차순으로 정렬하는 쿼리를 사용하였습니다.
*   추가시 키본키가 동일할 경우 덮어쓰는 옵션을 추가 하였습니다*/
    @Query("SELECT * FROM todo ORDER BY date DESC")

/*  반환 타입에 Flow 를 사용하였습니다. Flow 는 코틀린의 고급기능 중 하나로 데이터를 관찰할수 있도록합니다.
*   List<Todo> 타입의 데이터가 Flow 에 담기도록 Room 에서 지원해 주기 때문에 Flow 를 사용해보겠습니다.*/
    fun getAll(): Flow<List<Todo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: Todo)

    @Update
    suspend fun update(entity: Todo)

    @Delete
    suspend fun delete(entity: Todo)
    /* 데이터베이스에서 데이터를 얻는 동작 이외의 추가, 수정, 삭제는 모두 비동기로 오래 걸리는 처리에 속합니다
    * 따라서 메서드 앞에 suspend 키워드를 추가하였습니다.
    * suspend 키워드를 추가한 메서드는 오래 걸리는 코드임을 나타내고 코틀린의 비동기 처리 방법인 코루틴을
    * 활용하여 다루어야 합니다.*/
}