package uk.camsw.shimrr

import cats.syntax.all._
import shapeless.{Generic, HList}

trait MigrationOps {

  implicit class MigrationFOps[F[_] : cats.Functor, A <: Versioned](fa: F[A]) {
    def migrateTo[B <: Versioned](implicit m: Migration[A, B]): F[B] = fa.map(m.migrate)
  }

  implicit class MigrationOps[A <: Versioned](a: A) {

    def migrateTo[FIELD_DEFAULTS <: HList, B <: Versioned](implicit
                                                           ctx: MC[FIELD_DEFAULTS],
                                                           genA: Generic[A],
                                                           m: Migration[A, B]
                                                          ): B = m.migrate(a)

  }

}

object syntax extends MigrationOps