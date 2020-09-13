package arithmetic

import org.antlr.v4.runtime.tree.ParseTree

import scala.reflect.ClassTag
import scala.collection.immutable.Queue
import ParseTreeOps._

final class ParseTreeOps(tree: ParseTree) extends AbstractParseTreeOps {
  override def foreach[U](f: ParseTree => U): Unit = {
    ???
  }

  override def map[That](f: ParseTree => That): Stream[That] = {
    ???
  }

  override def filter(f: ParseTree => Boolean): Stream[ParseTree] = {
    ???
  }

  override def filterNot(f: ParseTree => Boolean): Stream[ParseTree] = {
    ???
  }

  override def exists(predicate: ParseTree => Boolean): Boolean = {
    ???
  }

  override def forall(predicate: ParseTree => Boolean): Boolean = {
    ???
  }

  override def count(predicate: ParseTree => Boolean): Int = {
    ???
  }

  override def find(predicate: ParseTree => Boolean): Option[ParseTree] = {
    ???
  }

  override def collectFirst[T](partialFunction: PartialFunction[ParseTree, T]): Option[T] = {
    ???
  }

  override def collectFirstAs[T](implicit classTag: ClassTag[T]): Option[T] = {
    ???
  }

  override def collectChildren(): Seq[ParseTree] = {
    ???
  }

  override def dfs(): Stream[ParseTree] = {
    ???
  }

  override def bfs(): Stream[ParseTree] = {
    ???
  }
}

object ParseTreeOps {
  implicit def parseTree2parseTreeOps(parseTree: ParseTree): AbstractParseTreeOps = {
    new ParseTreeOps(parseTree)
  }
}
