package com.rmmcosta.betterreadskotlinbootstrap.book

import org.springframework.data.annotation.Id
import org.springframework.data.cassandra.core.cql.PrimaryKeyType
import org.springframework.data.cassandra.core.mapping.CassandraType
import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn
import org.springframework.data.cassandra.core.mapping.Table
import java.time.LocalDate

@Table(value = "book_by_id")
data class Book(
    @Id @PrimaryKeyColumn(name = "book_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    var id: String,

    @Column(value = "book_name")
    @CassandraType(type = CassandraType.Name.TEXT)
    var name: String,

    @Column(value = "book_description")
    @CassandraType(type = CassandraType.Name.TEXT)
    var description: String,

    @Column(value = "book_published_date")
    @CassandraType(type = CassandraType.Name.DATE)
    var publishedDate: LocalDate,

    @Column(value = "book_cover_ids")
    @CassandraType(type = CassandraType.Name.LIST, typeArguments = [CassandraType.Name.BIGINT])
    var covers: List<Long>,

    @Column(value = "book_author_names")
    @CassandraType(type = CassandraType.Name.LIST, typeArguments = [CassandraType.Name.TEXT])
    var authorNames: List<String>,

    @Column(value = "book_author_ids")
    @CassandraType(type = CassandraType.Name.LIST, typeArguments = [CassandraType.Name.TEXT])
    var authorIds: List<String>,

)
