package org.jetbrains

import sbt.jetbrains.ideaPlugin.apiAdapter

import java.nio.file.Path
import scala.language.implicitConversions

package object sbtidea {
  implicit class Any2Option[T <: Any](any: T) {
    def lift2Option: Option[T] = Option(any)
  }

  //noinspection MutatorLikeMethodIsParameterless
  implicit class StringUtils(str: String) {
    def removeSpaces: String = str.replaceAll("\\s+", "")
    def escapeSpaces: String = str.replaceAll("\\s", "\\ ")
    def xmlQuote: String   = s"&quot;$str&quot;"
    def isValidFileName: Boolean =
      str.matches("\\A(?!(?:COM[0-9]|CON|LPT[0-9]|NUL|PRN|AUX|com[0-9]|con|lpt[0-9]|nul|prn|aux)|[\\s\\.])[^\\\\\\/:*\"?<>|]{1,254}\\z")
  }

  implicit def pathToPathExt(path: Path): apiAdapter.PathExt = new apiAdapter.PathExt(path)


  implicit class IteratorExt[A](private val delegate: Iterator[A]) extends AnyVal {
    def headOption: Option[A] = {
      if (delegate.hasNext) Some(delegate.next())
      else None
    }
  }
}
