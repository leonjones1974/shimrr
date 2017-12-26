package uk.camsw.shimrr

import cats.syntax.all._

trait MigrationOps {

  implicit class MigrationFOps[F[_] : cats.Functor, A <: ReadRepair](fa: F[A]) {
    def migrateTo[B <: ReadRepair](implicit m: Migration[A, B]): F[B] = fa map m.migrate
  }

  implicit class MigrationOps[A <: ReadRepair](a: A) {

    def migrateTo[B <: ReadRepair](implicit
                                   m: Migration[A, B]
                                 ): B = m.migrate(a)

  }

}

object syntax extends MigrationOps