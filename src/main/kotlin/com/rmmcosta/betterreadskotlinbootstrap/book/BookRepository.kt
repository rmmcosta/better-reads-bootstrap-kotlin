package com.rmmcosta.betterreadskotlinbootstrap.book

import org.springframework.data.cassandra.repository.CassandraRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : CassandraRepository<Book, String>