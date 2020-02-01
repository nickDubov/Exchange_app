package com.example.fullcurrpro

import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.persistence.DataQueryBuilder
import java.util.*

class Saved_exchange  {
    var result: String? = null
    var createdDate: String? = null
    val created: Date? = null
    val updated: Date? = null
    val objectId: String? = null
    val ownerId: String? = null
    var unit: String? = null

    fun save(): Saved_exchange {
        return Backendless.Data.of(Saved_exchange::class.java).save(this)
    }

    fun saveAsync(callback: AsyncCallback<Saved_exchange?>?) {
        Backendless.Data.of(Saved_exchange::class.java).save(this, callback)
    }

    fun remove(): Long {
        return Backendless.Data.of(Saved_exchange::class.java).remove(this)
    }

    fun removeAsync(callback: AsyncCallback<Long?>?) {
        Backendless.Data.of(Saved_exchange::class.java).remove(this, callback)
    }

    companion object {
        fun findById(id: String?): Saved_exchange {
            return Backendless.Data.of(Saved_exchange::class.java).findById(id)
        }

        fun findByIdAsync(
            id: String?,
            callback: AsyncCallback<Saved_exchange?>?
        ) {
            Backendless.Data.of(Saved_exchange::class.java).findById(id, callback)
        }

        fun findFirst(): Saved_exchange {
            return Backendless.Data.of(Saved_exchange::class.java).findFirst()
        }

        fun findFirstAsync(callback: AsyncCallback<Saved_exchange?>?) {
            Backendless.Data.of(Saved_exchange::class.java).findFirst(callback)
        }

        fun findLast(): Saved_exchange {
            return Backendless.Data.of(Saved_exchange::class.java).findLast()
        }

        fun findLastAsync(callback: AsyncCallback< Saved_exchange?>?) {
            Backendless.Data.of(Saved_exchange::class.java).findLast(callback)
        }

        fun find(queryBuilder: DataQueryBuilder?): List<Saved_exchange> {
            return Backendless.Data.of(Saved_exchange::class.java)
                .find(queryBuilder)
        }

        fun findAsync(
            queryBuilder: DataQueryBuilder?,
            callback: AsyncCallback<List<Saved_exchange?>?>?
        ) {
            Backendless.Data.of(Saved_exchange::class.java)
                .find(queryBuilder, callback)
        }
    }
}



