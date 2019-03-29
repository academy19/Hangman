package uk.gov.hangman.services

import com.google.inject.ImplementedBy

import scala.concurrent.{ExecutionContext, Future}
import scala.io.{BufferedSource, Source}
import scala.util.{Random, Try}

@ImplementedBy(classOf[DictionaryWordService])
trait WordService {
  def getWord()(implicit ec : ExecutionContext) : Future[String]
}


class SubmarineWordService extends WordService {
  override def getWord()(implicit ec : ExecutionContext): Future[String] = Future.successful("SUBMARINE")
}

class DictionaryWordService extends WordService {

  private def usingDictionaryFile[T](operation : BufferedSource => T) : T = {
    val filename = "/usr/share/dict/words"
    val source = Source.fromFile(filename)
    val result = operation(source)
    source.close()
    result
  }

  override def getWord()(implicit ec : ExecutionContext): Future[String] = {
    val selectedIdx = usingDictionaryFile(source => Random.nextInt(source.getLines().size))

    Future {usingDictionaryFile(source => {
      val lines = source.getLines()
      for (_ <- 1 to selectedIdx) lines.next()
      lines.next().toUpperCase()
    })}
  }
}