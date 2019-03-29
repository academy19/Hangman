package uk.gov.hangman.model

import play.api.libs.json._
import play.api.libs.functional.syntax._

sealed trait Letter {
  def inner: Char

  def ==(that : Char) : Boolean = inner == that

  def makeVisible() : Letter = VisibleLetter(inner)
  def hide() : Letter = HiddenLetter(inner)
}

case class VisibleLetter(inner : Char) extends Letter {
  override def toString = inner.toString
}

case class HiddenLetter(inner : Char) extends Letter {
  override def toString = "_"
}

object Letter {
  def deconstruct(letter : Letter) : (String, String) = {letter match {
    case VisibleLetter(x) => ("visible", x.toString)
    case HiddenLetter(x) => ("hidden", x.toString)
  }}

  implicit val writes : Writes[Letter] =
    ((JsPath \ "visibility").write[String] and
      (JsPath \ "letter").write[String])(l => deconstruct(l))
}