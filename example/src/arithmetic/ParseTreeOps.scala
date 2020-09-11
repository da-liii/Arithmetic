package arithmetic

import org.antlr.v4.runtime.tree.{ParseTree, TerminalNode}

import scala.reflect.ClassTag
import scala.collection.immutable.Queue
import ParseTreeOps._
import arithmetic.antlr.ArithmeticParser.{ScientificContext, VariableContext}

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


  // Predicates

  override def isTerminalNode: Boolean = {
    tree match {
      case _: TerminalNode =>
        true
      case _ =>
        false
    }
  }

  override def isParenthesized: Boolean = {
    tree.getChild(0).isInstanceOf[TerminalNode] &&
      tree.getChild(tree.getChildCount-1).isInstanceOf[TerminalNode] &&
      "(".equals(tree.getChild(0).getText) &&
      ")".equals(tree.getChild(tree.getChildCount-1).getText)
  }

  override def isScientific: Boolean = {
    tree match {
      case _: ScientificContext =>
        true
      case _ =>
        false
    }
  }

  override def containPlusOp: Boolean = {
    tree.exists(node => "+".equals(node.getText))
  }

  override def containNoVariable: Boolean = {
    !tree.exists {
      case _ :VariableContext =>
        true
      case _ =>
        false
    }
  }

  private def allNodes(): Stream[ParseTree] = {
    ???
  }
}

object ParseTreeOps {
  implicit def parseTree2parseTreeOps(parseTree: ParseTree): AbstractParseTreeOps = {
    new ParseTreeOps(parseTree)
  }
}
