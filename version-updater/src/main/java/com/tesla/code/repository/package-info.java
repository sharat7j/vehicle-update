/**
 * The repository package is responsible for providing the DB access layer. Currently implemented as an In-memory DB
 * using hibernate by reusing and extending existing CrudRepository implementations to be able to easily query the different
 * models.
 *
 * In future this can easily be swapped out with an persistence store or a DB that supports relational queries easily
 * without any code changes.
 */
package com.tesla.code.repository;