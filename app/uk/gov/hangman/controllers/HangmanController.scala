package uk.gov.hangman.controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc._
import uk.gov.hangman.model.Hangman
import uk.gov.hangman.services.{GameRepository, WordService}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HangmanController @Inject()(cc : ControllerComponents,
                                  repository : GameRepository,
                                  wordService : WordService) extends AbstractController(cc) {
  private implicit val executionContext: ExecutionContext = cc.executionContext

  def get(): Action[AnyContent] = Action.async {
    for {
      word <- wordService.getWord()
      id <- repository.insert(Hangman(word))
    } yield Redirect(routes.HangmanController.getById(id))
  }

  def getById(gameId : Long): Action[AnyContent] = Action.async {
    repository.get(gameId).map {
      case Some(game) => Ok(views.html.hangman(game, gameId))
      case None => NotFound
    }
  }

  def guess(gameId : Long, guessedLetter: Char) : Action[AnyContent] = Action.async {
    repository.get(gameId).flatMap {
      case Some(game) =>
        repository.update(gameId, game.guess(guessedLetter))
          .map[Result](_ => Redirect(routes.HangmanController.getById(gameId)))
      case None =>
        Future.successful(NotFound)
    }
  }
}
