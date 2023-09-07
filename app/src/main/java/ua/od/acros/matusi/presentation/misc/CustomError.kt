package ua.od.acros.matusi.presentation.misc

data class CustomError (private val errorMessage: String): Throwable(errorMessage)