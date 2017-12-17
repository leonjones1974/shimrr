package uk.camsw.shimrr

import scala.collection.GenSeq

object syntax extends MigrationOps

trait MigrationOps {

  implicit class MigrationOps[A](a: A) {
    def migrateTo[B](implicit migration: Migration[A, B]): B = migration.migrate(a)
  }

  implicit class MigrationGenSeqOps[A](xs: GenSeq[A]) {
    def migrateTo[B](implicit migration: Migration[A, B]): GenSeq[B] = xs map migration.migrate
  }

}
