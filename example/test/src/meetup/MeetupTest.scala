package meetup

import arithmetic.LazyTree
import org.antlr.v4.runtime.tree.{ParseTree, TerminalNode}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class MeetupTest extends AnyFunSuite with Matchers {
  def beautifyName(node: ParseTree): String = {
    node.getClass.getSimpleName
      .replace("Context", "")
      .replace("Impl", "")
  }

  test("Demo 1: bfs traverse of `1 + a + 2`") {
    val lazyTree = LazyTree("1 + a + 2")
    lazyTree.expression().foreach { node =>
      println(s"${beautifyName(node)} [${lazyTree.extractViaTree(_ => node)}]")
    }
  }

  test("Demo 2: validate") {
    Meetup.validate("1 + 1") should be (true)
    Meetup.validate("1 - a") should be (false)
  }

  def showParenthesizing(text: String): Unit = {
    println("<-- Original Text")
    println(text)
    println("--> Original Text\n")

    println("<-- Replaced")
    println(Meetup.parenthesize(text))
    println("--> Replaced\n\n")
  }

  test("Demo 3: parenthesize") {
    showParenthesizing("1 + a + 2")
    showParenthesizing("(1 + a + 2)")
    showParenthesizing("((1 + a) + 2)")
  }

  def showEvaluate(text: String): Unit = {
    println("<-- Original Text")
    println(text)
    println("--> Original Text\n")

    println("<-- Evaluated")
    println(Meetup.evaluate(text))
    println("--> Evaluated\n\n")
  }

  test("Demo 4: evaluate") {
    showEvaluate("(a + 1) + 2")
    showEvaluate("a + (1 + 2)")
    showEvaluate("(1+2)+(3+4)+(a + b)")
  }

  test("Demo 5: calculator") {
    Meetup.calculator("(1+2) + (3+4) + 5")
  }
}