package meetup

import arithmetic.LazyTree
import arithmetic.antlr.ArithmeticParser.{AtomContext, ExpressionContext, ScientificContext, VariableContext}
import org.antlr.v4.runtime.tree.TerminalNode
import arithmetic.ParseTreeOps._
import arithmetic.Predicate._
import java.lang.IllegalStateException

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
    val theNodeToReplace = lazyTree.expression()
      .filterNot(isTerminalNode)    // Condition 1: Not a terminalNode
      .filterNot(isParenthesized)   // Condition 2: Not ()-ed
      .filter(containPlusOp)        // Condition 3: Contains +
      .filter(node => node.getParent == null
        || !isParenthesized(node.getParent)) // Condition 4: No parent or parent is not ()-ed
      .collectFirst {
        case context: ExpressionContext => context
      }

    // Step 2: Replace it
    theNodeToReplace.map { node =>
      println("Parenthesize:")
      val targetText = lazyTree.extractViaTree(node)
      lazyTree.rewriteTree(node, s"(${targetText})")
    }.getOrElse(text)
  }

  def evaluate(text: String): String = {
    val lazyTree = LazyTree(text)

    // Step 1: Find the node which can be evaluated
    val theNodeToReplace = lazyTree.expression()
      .filter(containPlusOp)              // Condition 1: Not a terminalNode
      .filter(isParenthesized)            // Condition 2: ()-ed
      .filter(containNoVariable)          // Condition 3: No variable
      .collectFirst {                       // Condition 4: 5 number node
        case node: ExpressionContext if node.count(isTerminalNode) == 5 =>
          node
      }
    
    // Step 2: Evaluate and get the result
    // Step 3: Replace the target node with the result
    theNodeToReplace.map { node =>
      println("Evaluate:")
      val result = node
        .filter(isScientific)
        .map(_.getText)
        .map(Integer.parseInt)
        .sum
      lazyTree.rewriteTree(node, result.toString)
    }.getOrElse(text)
  }

  // Pattern: c + expr => expr + c
  def commutate(text: String): String = {
    val lazyTree = LazyTree(text)

    // Step 1: Find the patterns
    val theNodeToCommutate = lazyTree.expression()
      .filterNot(isParenthesized)
      .filter(node => node.getChildCount() == 3)
      .filter { node =>
        val first = node.collectChildren().filter(_.isInstanceOf[ExpressionContext]).head
        val second = node.collectChildren().filter(_.isInstanceOf[ExpressionContext]).last

        first.count(isScientific) == 1 && second.count(isScientific) != 1
      }.headOption

    // Step 2: commutate the inner expr
    theNodeToCommutate.map { node =>
      println("Commutate:")
      val first = node.collectChildren().filter(_.isInstanceOf[ExpressionContext]).head
      val second = node.collectChildren().filter(_.isInstanceOf[ExpressionContext]).last
      val result = s"${lazyTree.extractViaTree(second)}+${lazyTree.extractViaTree(first)}"
      lazyTree.rewriteTree(node, result)
    }.getOrElse(text)
  }

  // Pattern: (expr1 + (expr2 + c))  => ((expr1 + expr2) + c)
  // Pattern: ((expr1 + c) + expr2) => (expr1 + (c+ expr2))
  def associate(text: String): String = {
    val lazyTree = LazyTree(text)

    val theNodeToAssociate = lazyTree.expression()
      .filterNot(isParenthesized)
      .filter(node => node.getChildCount() == 3)
      .filter { node =>
        val exprSeq = node.collectChildren().filter(_.isInstanceOf[ExpressionContext])
        val first = exprSeq.head
        val second = exprSeq.last
        if (isParenthesized(second)) {
          val innerFirst = second.getChild(1).getChild(0)
          val innerSecond = second.getChild(1).getChild(2)
          innerFirst.count(isScientific) != 1 && innerSecond.count(isScientific) == 1
        } else if (isParenthesized(first)) {
          val innerFirst = first.getChild(1).getChild(0)
          val innerSecond = first.getChild(1).getChild(2)
          innerFirst.count(isScientific) != 1 && innerSecond.count(isScientific) == 1
        } else {
          false
        }
      }.lastOption

    theNodeToAssociate.map { node =>
      println("Associate:")
      val first = node.getChild(0)
      val second = node.getChild(2)
      val result =
        if (!isParenthesized(first) && isParenthesized(second)) {
          val firstText = lazyTree.extractViaTree(first)
          val innerFirstText = lazyTree.extractViaTree(second.getChild(1).getChild(0))
          val innerSecondText = lazyTree.extractViaTree(second.getChild(1).getChild(2))
          s"($firstText + $innerFirstText) + $innerSecondText"
        } else {
          val secondText = lazyTree.extractViaTree(second)
          val innerFirstText = lazyTree.extractViaTree(first.getChild(1).getChild(0))
          val innerSecondText = lazyTree.extractViaTree(first.getChild(1).getChild(2))
          s"$innerFirstText + ($innerSecondText + $secondText)"
        }

      lazyTree.rewriteTree(node, result)
    }.getOrElse(text)
  }

  def calc_until_unchanged(text : String, f: String => String): String = {
    var changed = true
    var previous: String = text
    while (changed) {
      val result = f(previous)
      if (result == previous) {
        changed = false
      } else {
        println(s"$previous\n-> $result\n")
        previous = result
      }
    }

    previous
  }

  def calc_until_unchanged(text: String, fseq: Seq[String => String]): String = {
    var previous: String = text
    fseq.foreach { f =>
      val result = calc_until_unchanged(previous, f)
      if (result != previous) {
        println(s"$previous\n=> $result\n")
        previous = result
      }
    }
    previous
  }

  def iterate(text: String, total: Int, i: Int): String = {
    val result = calc_until_unchanged(text, Seq(commutate _, associate _, evaluate _))
    if (i < total && result != text) {
      println(s"[${i+1}/$total] $result")
      iterate(result, total, i+1)
    } else {
      result
    }
  }

  def calculator(text: String): Unit = {
    println("Step 1: parenthesize")
    val step_1_result = calc_until_unchanged(text, parenthesize _)

    println("Step 2: commutate/associate/evaluate")
    val result = iterate(step_1_result, 10, 0)
    println("Here is the result: " + result)
  }
}