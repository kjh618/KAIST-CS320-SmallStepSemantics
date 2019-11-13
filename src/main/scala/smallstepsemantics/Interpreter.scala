package smallstepsemantics

import scala.sys.error

object Interpreter {
  private def applyOperator(operator: (Int, Int) => Int, leftVal: Value, rightVal: Value): Value =
    (leftVal, rightVal) match {
      case (NumVal(leftNum), NumVal(rightNum)) => NumVal(operator(leftNum, rightNum))
      case _ => error(s"not both numbers: $leftVal, $rightVal")
    }

  private def interpret(contStack: List[Continuation], valStack: List[Value]): Value = {
    SmallStepSemantics.addStep(contStack, valStack)
    contStack match {
      case curCont :: restConts => curCont match {
        case EvalCont(expr, env) => expr match {
          case Num(num) =>
            interpret(restConts, NumVal(num) :: valStack)
          case Add(left, right) =>
            interpret(EvalCont(left, env) :: EvalCont(right, env) :: AddCont :: restConts, valStack)
          case Sub(left, right) =>
            interpret(EvalCont(left, env) :: EvalCont(right, env) :: SubCont :: restConts, valStack)
          case Id(name) =>
            interpret(restConts, env.e.getOrElse(name, error(s"free identifier: $name")) :: valStack)
          case Fun(param, body) =>
            interpret(restConts, CloVal(param, body, env) :: valStack)
          case App(fun, arg) =>
            interpret(EvalCont(fun, env) :: EvalCont(arg, env) :: AppCont :: restConts, valStack)
          case Withcc(name, body) =>
            interpret(EvalCont(body, Environment(env.e + (name -> ContVal(restConts, valStack)))) :: restConts, valStack)
        }
        case AddCont => valStack match {
          case rightVal :: leftVal :: restVals =>
            interpret(restConts, applyOperator(_ + _, leftVal, rightVal) :: restVals)
          case _ =>
            error("unreachable; not enough values to add")
        }
        case SubCont => valStack match {
          case rightVal :: leftVal :: restVals =>
            interpret(restConts, applyOperator(_ - _, leftVal, rightVal) :: restVals)
          case _ =>
            error("unreachable; not enough values to subtract")
        }
        case AppCont => valStack match {
          case argVal :: funVal :: restVals => funVal match {
            case CloVal(param, body, cloEnv) =>
              interpret(EvalCont(body, Environment(cloEnv.e + (param -> argVal))) :: restConts, restVals)
            case ContVal(newContStack, newValStack) =>
              interpret(newContStack, argVal :: newValStack)
            case _ =>
              error(s"not a closure or a continuation: $funVal")
          }
          case _ =>
            error("unreachable; not enough values to apply")
        }
      }
      case Nil =>
        if (valStack.length == 1)
          valStack.head
        else
          error("unreachable; too many result values")
    }
  }

  def run(program: String): String = {
    SmallStepSemantics.clear()
    val expr = ExpressionParser.parse(program)
    interpret(EvalCont(expr, Environment(Map())) :: Nil, Nil).toString
  }
}