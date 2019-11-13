package smallstepsemantics

import scala.collection.mutable

object SmallStepSemantics {
  private case class Step(contStack: List[Continuation], valStack: List[Value]) {
    def contStackToString: String = contStack.foldLeft("")({ case (str, cont) => str + s"$cont :: "}) + "□"

    def valStackToString: String = valStack.foldLeft("")({ case (str, value) => str + s"$value :: "}) + "■"
  }

  private val steps: mutable.ArrayBuffer[Step] = mutable.ArrayBuffer.empty

  def clear(): Unit = steps.clear()

  def addStep(contStack: List[Continuation], valStack: List[Value]): Unit = {
    steps += Step(contStack, valStack)
  }

  override def toString: String = {
    val stepStrs = steps.map(step => (step.contStackToString, step.valStackToString))
    val contStackStrMaxLength = stepStrs.maxBy(_._1.length)._1.length
    stepStrs.map({ case (contStackStr, valStackStr) =>
      s"%-${contStackStrMaxLength}s ‖ %s".format(contStackStr, valStackStr)
    }).mkString("  ", "\n→ ", "")
  }
}