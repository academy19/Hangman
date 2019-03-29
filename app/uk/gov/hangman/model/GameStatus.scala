package uk.gov.hangman.model

sealed trait GameStatus

case object Won extends GameStatus
case object Lost extends GameStatus
case object Unfinished extends GameStatus
