package arithmetic

import org.antlr.v4.runtime.tree.ParseTree
import scala.reflect.ClassTag

trait AbstractParseTreeOps {

  def foreach[U](f: ParseTree => U): Unit = ???

  def map[That](f: ParseTree => That): Stream[That] = ???

  def filter(f: ParseTree => Boolean): Stream[ParseTree] = ???

  def filterNot(f: ParseTree => Boolean): Stream[ParseTree] = ???

  def exists(predicate: ParseTree => Boolean): Boolean = ???

  def forall(predicate: ParseTree => Boolean): Boolean = ???

  def count(predicate: ParseTree => Boolean): Int = ???

  def find(predicate: ParseTree => Boolean): Option[ParseTree] = ???

  def collectFirst[T](partialFunction: PartialFunction[ParseTree, T]): Option[T] = ???

  def collectFirstAs[T](implicit classTag: ClassTag[T]): Option[T] = ???

  def collectChildren(): Seq[ParseTree] = ???

  // bfs is used as the default traverse order above
  def bfs(): Stream[ParseTree] = ???

  // dfs is used in some special cases
  def dfs(): Stream[ParseTree] = ???
}
