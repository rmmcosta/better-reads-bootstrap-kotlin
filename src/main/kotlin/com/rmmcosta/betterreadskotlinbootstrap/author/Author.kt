package com.rmmcosta.betterreadskotlinbootstrap.author

import org.springframework.data.annotation.Id
import org.springframework.data.cassandra.core.cql.PrimaryKeyType
import org.springframework.data.cassandra.core.mapping.CassandraType
import org.springframework.data.cassandra.core.mapping.CassandraType.Name
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn
import org.springframework.data.cassandra.core.mapping.Table

@Table(value = "author_by_id")
class Author(
    @Id @PrimaryKeyColumn(name = "author_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    var id: String,

    @CassandraType(type = Name.TEXT)
    var name: String?,

    @CassandraType(type = Name.TEXT)
    var personalName: String?,
)
