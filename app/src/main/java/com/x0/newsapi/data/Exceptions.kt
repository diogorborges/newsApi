package com.x0.newsapi.data

open class DataSourceException(message: String? = null) : Exception(message)

class RemoteDataNotFoundException : DataSourceException("Data not found in remote data source")

class LocalDataNotFoundException : DataSourceException("Data not found in local data source")

open class RepositoryException(message: String? = null) : Exception(message)

class NetworkException : RepositoryException("No network connection")

class FailureException : RepositoryException("Api failure to retrieve the data")