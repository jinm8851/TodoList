package org.study2.todolist.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Todo::class], version = 1)
abstract class TodoDatabase: RoomDatabase() {
    abstract fun todoDao(): TodoDao

    /* 데이터베이스 클래스는 RoomDatabase 클래스를 상속받는 추상클래스입니다. 내부에는 TodoDao 객체를
    * 반환하는ㄴ 추상메서드를 제공하도록 합니다. 이클래스는 @Database 주석을 작성하고 관련된 내용을 옵션으로
    * 지정해야 합니다. 엔터티는 Todo이며(다수 지정 가능) 데이터 베이스 버전은 1로 지정했습니다.
    * 앱이 업데이트 되어 데이터 베이스 구조가 변경되거나 할때 이 버전을 올려 줘야 합니다.*/
}