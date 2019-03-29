package uk.gov.hangman.services

import com.google.inject.ImplementedBy
import uk.gov.hangman.model.Hangman

import scala.concurrent.Future

@ImplementedBy(classOf[InMemoryGameRepository])
trait GameRepository {
  def get(id : Long) : Future[Option[Hangman]]
  def insert(game : Hangman) : Future[Long]
  def update(id : Long, game : Hangman) : Future[Unit]
}

class InMemoryGameRepository extends GameRepository {
  private val games : scala.collection.mutable.Map[Long, Hangman] = scala.collection.mutable.Map()

  override def get(id: Long): Future[Option[Hangman]] = Future.successful(games.get(id))

  override def insert(game: Hangman): Future[Long] = {
    val keys = games.keys
    val newKey = (keys.toList :+ 0L).max + 1L
      games += (newKey -> game)
      Future successful newKey
  }

  override def update(id: Long, game: Hangman): Future[Unit] = {
    Future.successful(games.update(id, game))
  }
}