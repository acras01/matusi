package ua.od.acros.matusi.data.datasource.impl

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.od.acros.matusi.data.datasource.FirestoreDataSource
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.suspendCoroutine

class FirestoreDataSourceImpl (
    private val firestore: FirebaseFirestore
): FirestoreDataSource {

    private val coroutineContext: CoroutineContext = CoroutineName("firestore")

    override suspend fun getDocumentFromCollectionById(
        collection: String,
        id: String
    ): Map<String, Any>? {
        val fireCollection = firestore.collection(collection)
        val queryById: Query =
            fireCollection.whereEqualTo("id", id)
        var result: Map<String, Any>? = null
        suspendCoroutine<Map<String, Any>?> { cont ->
            queryById.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (!task.result.isEmpty) {
                        CoroutineScope(coroutineContext).launch {
                            result = getDocumentById(collection, id)
                            cont.resumeWith(Result.success((result)))
                        }
                    } else {
                        Log.d("Matusi", "no document with $id")
                        cont.resumeWith(Result.success(null))
                    }
                } else {
                    Log.d("Matusi", "error querying document with $id")
                    cont.resumeWith(Result.success(null))
                }
            }
        }
        return result
    }

    override suspend fun removeDocumentFromDatabaseById(collection: String, id: String) {
        val fireCollection = firestore.collection(collection)
        val queryById: Query =
            fireCollection.whereEqualTo("id", id)
        queryById.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (!task.result.isEmpty) {
                    CoroutineScope(coroutineContext).launch {
                        suspendCoroutine<Boolean> { cont ->
                            fireCollection.document(id).delete().addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d("Matusi", "successfully deleted document with $id")
                                    cont.resumeWith(Result.success(true))
                                } else {
                                    Log.d("Matusi", "error deleting document with $id")
                                    cont.resumeWith(Result.success(false))
                                }
                            }
                        }
                    }
                } else {
                    Log.d("Matusi", "no document with $id")
                }
            } else {
                Log.d("Matusi", "error querying document with $id")
            }
        }
    }

    override suspend fun insertDocumentToDatabaseById(
        collection: String,
        id: String,
        item: HashMap<String, Any?>) {
        val fireCollection = firestore.collection(collection)
        val queryById: Query =
            fireCollection.whereEqualTo("id", id)
        queryById.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (task.result.isEmpty)
                    Log.d("Matusi", "no document with $id")
                CoroutineScope(coroutineContext).launch {
                    suspendCoroutine<Boolean> { cont ->
                        fireCollection.document(id).set(item).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("Matusi", "successfully inserted document with $id")
                                cont.resumeWith(Result.success(true))
                            } else {
                                Log.d("Matusi", "error inserting document with $id")
                                cont.resumeWith(Result.success(false))
                            }
                        }
                    }
                }
            } else {
                Log.d("Matusi", "error querying document with $id")
            }
        }
    }

    private suspend fun getDocumentById(collection: String, id: String): Map<String, Any>? {
        var result: Map<String, Any>? = mapOf()
        val document = firestore.collection(collection).document(id)
        suspendCoroutine<Map<String, Any>?> { cont ->
            document.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    result = task.result.data
                    Log.d("Matusi", "successfully got document with $id")
                    cont.resumeWith(Result.success(result))
                } else {
                    Log.d("Matusi", "error querying document with $id")
                    cont.resumeWith(Result.success(null))
                }
            }
        }
        return result
    }
}