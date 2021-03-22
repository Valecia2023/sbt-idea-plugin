package org.jetbrains.sbtidea.packaging

import java.nio.file.Path

trait ExcludeFilter extends Serializable {
  def apply(path: Path): Boolean
}

//noinspection ConvertExpressionToSAM : scala 2.10 compat
object ExcludeFilter {
  val AllPass: ExcludeFilter = new ExcludeFilter {
    override def apply(path: Path): Boolean = false
  }

  def merge(filters: Iterable[ExcludeFilter]): ExcludeFilter = new ExcludeFilter {
    override def apply(path: Path): Boolean = filters.exists(f => f(path))
  }
}