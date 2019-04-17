package com.x0.newsapi.data

open class RepositoryException(message: String? = null) : Exception(message)

class NetworkException : RepositoryException("No network connection")

class FailureException : RepositoryException("Api failure to retrieve the data")