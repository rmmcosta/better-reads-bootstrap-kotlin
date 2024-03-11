package com.rmmcosta.betterreadskotlinbootstrap.book

import org.springframework.data.annotation.Id
import org.springframework.data.cassandra.core.cql.PrimaryKeyType
import org.springframework.data.cassandra.core.mapping.CassandraType
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn
import org.springframework.data.cassandra.core.mapping.Table
import java.time.LocalDate

@Table(value = "book_by_id")
data class Book(
    @Id @PrimaryKeyColumn(name = "book_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    var id: String,

    @CassandraType(type = CassandraType.Name.TEXT)
    var name: String,

    @CassandraType(type = CassandraType.Name.TEXT)
    var description: String,

    @CassandraType(type = CassandraType.Name.DATE)
    var publishedDate: LocalDate,

    @CassandraType(type = CassandraType.Name.LIST, typeArguments = [CassandraType.Name.BIGINT])
    var covers: List<Long>,

    @CassandraType(type = CassandraType.Name.LIST, typeArguments = [CassandraType.Name.TEXT])
    var authorNames: List<String>,

    @CassandraType(type = CassandraType.Name.LIST, typeArguments = [CassandraType.Name.TEXT])
    var authorIds: List<String>,

)
