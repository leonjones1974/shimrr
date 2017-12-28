package uk.camsw.shimrr

import scala.language.higherKinds


trait MigrationOps {

  implicit class MigrationFOps[F[_] : cats.Functor, A](fa: F[A]) {
    import cats.syntax.functor._
    def migrateTo[B](implicit m: Migration[A, B]): F[B] = fa map m.migrate
  }

  implicit class MigrationOps[A](a: A) {

    def migrateTo[B](implicit
                     m: Migration[A, B]
                    ): B = m.migrate(a)
  }

  implicit class MigrationIterableOps[A](ia: Iterable[A]) {
    def migrateTo[B](implicit m: Migration[A, B]): Iterable[B] = ia map m.migrate

  }

}

object syntax extends MigrationOps