package com.rmmcosta.betterreadskotlinbootstrap.author

import org.springframework.data.cassandra.repository.CassandraRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthorRepository : CassandraRepository<Author, String>