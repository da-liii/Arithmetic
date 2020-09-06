package meetup

import arithmetic.LazyTree
import arithmetic.antlr.ArithmeticParser.{AtomContext, ExpressionContext, ScientificContext, VariableContext}
import org.antlr.v4.runtime.tree.TerminalNode
import arithmetic.ParseTreeOps._

object Meetup {
  def validate(text: String): Boolean = {
    LazyTree(text).expression().forall {
      case ctx: ExpressionContext =>
        true
      case ctx: AtomContext =>
        true
      case ctx: ScientificContext =>
        true
      case ctx: VariableContext =>
        true
      case node: TerminalNode =>
        node.getText.length == 1 && {
          val ch = node.getText.head
          Character.isLowerCase(ch) ||
            Character.isDigit(ch) ||
            Seq('(', ')', '+').contains(ch)
        }
      case x =>
        false
    }
  }

  def parenthesize(text: String): String = {
    val lazyTree = LazyTree(text)

    // Step 1: Find the node to replace
    val theNodeToReplace = lazyTree.expression().find {
      case node: ExpressionContext =>
        !(node.isTerminalNode) &&                 // Condition 1: Not a terminalNode
          !(node.isParenthesized) &&              // Condition 2: Not ()-ed
          (node.getParent == null || !(node.getParent.isParenthesized)) &&
                                                  // Condition 3: Has parent and parent is not ()-ed
          node.containPlusOp                      // Condition 4: Contains +

      case _ =>
        false
    }

    // Step 2: Replace it
    theNodeToReplace.map { node =>
      val targetText = lazyTree.extractViaTree(node)
      lazyTree.rewriteTree(node, s"(${targetText})")
    }.getOrElse(text)
  }

  def evaluate(text: String): String = {
    val lazyTree = LazyTree(text)

    // Step 1: Find the node which can be evaluated
    val theNodeToReplace = lazyTree.expression().find {
      case node: ExpressionContext =>
        node.containPlusOp &&                                                     // Condition 1: Not a terminalNode
          node.isParenthesized &&                                                 // Condition 2: ()-ed
          node.containNoVariable &&                                               // Condition 3: No variable
          node.count(_.isTerminalNode) == 5                                       // Condition 4: 5 number node
      case _ =>
        false
    }

    // Step 2: Evaluate and get the result
    // Step 3: Replace the target node with the result
    theNodeToReplace.map { node =>
      val result = node.filter(_.isScientific).map(_.getText)
        .map(Integer.parseInt)
        .sum
      lazyTree.rewriteTree(node, result.toString)
    }.getOrElse(text)
  }

  def commutate(text: String): String = {
    ???
  }

  def associate(text: String): String = {
    ???
  }

  def calc_until_unchanged(text : String, f: String => String): String = {
    var notChanged = true
    var previous: String = text
    while (notChanged) {
      val result = f(previous)
      if (result == previous) {
        notChanged = false
      } else {
        println(s"$previous\n-> $result\n")
        previous = result
      }
    }

    previous
  }

  def calculator(text: String): Unit = {
    println("Step 1: parenthesize")
    val step_1_result = calc_until_unchanged(text, parenthesize)

    println("Step 2: evaluate")
    calc_until_unchanged(step_1_result, evaluate)
  }
}