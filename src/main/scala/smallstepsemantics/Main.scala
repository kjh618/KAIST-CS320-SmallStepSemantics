package smallstepsemantics

import org.scalajs.dom
import dom.document
import scala.scalajs.js.annotation.JSExportTopLevel

object Main {
  private val programInput = document.getElementById("programInput").asInstanceOf[dom.html.TextArea]
  private val output = document.getElementById("output").asInstanceOf[dom.html.Paragraph]

  private def generateSmallStepSemantics(program: String): String = {
    Interpreter.run(program)
    SmallStepSemantics.toString
  }

  @JSExportTopLevel("runProgram")
  def runProgram(): Unit = {
    output.textContent = try generateSmallStepSemantics(programInput.value) catch {
      case e: RuntimeException => e.getMessage
      case e: Throwable => s"unknown error: $e"
    }
  }

  def main(args: Array[String]): Unit = {
  }
}