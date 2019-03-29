package uk.gov.hangman.model

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Hangman private (word: Seq[Letter], remainingGuesses: Int) {
  def status  : GameStatus = {
    if (remainingGuesses <= 0) Lost
    else if (wordCompleted) Won
    else Unfinished
  }

  private def wordCompleted = {
    word.forall {
      case VisibleLetter(_) => true;
      case _ => false
    }
  }

  def hasEnded: Boolean = status != Unfinished

  def guess(guessedLetter : Char) : Hangman = {
    val newWord  = word.map(l => if (l == guessedLetter) l.makeVisible() else l)
    val newRemainingGuesses = if (word == newWord) remainingGuesses - 1 else remainingGuesses
    new Hangman(newWord, newRemainingGuesses)
  }

  override def toString: String = word.mkString(" ") + s" $remainingGuesses  guesses remaining"
}

object Hangman {
  def apply(word : String) : Hangman = Hangman(word.map(l => HiddenLetter(l)).toList, 12)

  implicit def writes : Writes[Hangman] = {
    ((JsPath \ "word").write[Seq[Letter]]
    and (JsPath \ "remainingGuesses").write[Int]) (unlift(Hangman.unapply))
  }
}