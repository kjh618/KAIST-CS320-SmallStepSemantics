package smallstepsemantics

import org.scalajs.dom
import dom.document
import scala.scalajs.js.annotation.JSExportTopLevel

object Main {
  private val programInput = document.getElementById("programInput").asInstanceOf[dom.html.TextArea]
  private val output = document.getElementById("output").asInstanceOf[dom.html.TextArea]

  @JSExportTopLevel("runProgram")
  def runProgram(): Unit = {
    output.value = try Interpreter.run(programInput.value) catch {
      case e: RuntimeException => e.getMessage
      case e: Throwable => s"unknown error: $e"
    }
  }

  def main(args: Array[String]): Unit = {
    val program =
      """{withcc done
        |  { {withcc esc
        |      {done {+ 1 {withcc k {esc k}}}}
        |    }
        |    3
        |  }
        |}
        |""".stripMargin
    println(Interpreter.run(program))
  }
}