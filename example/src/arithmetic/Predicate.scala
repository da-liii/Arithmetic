package arithmetic

import ParseTreeOps._
import org.antlr.v4.runtime.tree.{ ParseTree, TerminalNode }
import arithmetic.antlr.ArithmeticParser._

object Predicate {
  def isTerminalNode(tree: ParseTree): Boolean = {
    tree match {
      case _: TerminalNode =>
        true
      case _ =>
        false
    }
  }

  def isParenthesized(tree: ParseTree): Boolean = {
    tree.getChild(0).isInstanceOf[TerminalNode] &&
      tree.getChild(tree.getChildCount-1).isInstanceOf[TerminalNode] &&
      "(".equals(tree.getChild(0).getText) &&
      ")".equals(tree.getChild(tree.getChildCount-1).getText)
  }

  def isScientific(tree: ParseTree): Boolean = {
    tree.isInstanceOf[ScientificContext]
  }

  def isAtom(tree: ParseTree): Boolean = {
    tree.isInstanceOf[AtomContext]
  }

  def isVariable(tree: ParseTree): Boolean = {
    tree.isInstanceOf[VariableContext]
  }

  def containPlusOp(tree: ParseTree): Boolean = {
    tree.exists(node => "+".equals(node.getText))
  }

  def containNoVariable(tree: ParseTree): Boolean = {
    !tree.exists {
      case _ :VariableContext =>
        true
      case _ =>
        false
    }
  }
}
