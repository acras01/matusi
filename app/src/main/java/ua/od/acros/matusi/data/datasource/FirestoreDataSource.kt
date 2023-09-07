package ua.od.acros.matusi.data.datasource

interface FirestoreDataSource {
    suspend fun getDocumentFromCollectionById(collection: String, id: String): Map<String, Any>?
    suspend fun removeDocumentFromDatabaseById(collection: String, id: String)
    suspend fun insertDocumentToDatabaseById(collection: String, id: String, item: HashMap<String, Any?>)
}