package xsbt.boot

class LaunchMain extends xsbti.AppMain {

  def run(configuration: xsbti.AppConfiguration) = {
    val oldClassLoader = Thread.currentThread.getContextClassLoader
    Thread.currentThread.setContextClassLoader(getClass.getClassLoader)

    val args = configuration.arguments
    if (args.length == 0) {
      throw new RuntimeException("Missing main class parameter!")
    }
    val mainClazzName = args(0)
    println("Loading main class '%s'" format mainClazzName)

    try {
      val mainClazz = Class.forName(mainClazzName)
      val mainArgs = args.tail

      println("Invoking main with remaining args:")
      println(mainArgs.mkString("\n"))

      val argTypes = classOf[Array[String]]
      val mainMethod = mainClazz.getDeclaredMethod("main", argTypes)

      mainMethod.invoke(null, mainArgs.asInstanceOf[AnyRef])
    }
    catch {
      case e => throw new RuntimeException(e)
    }
    Thread.currentThread.setContextClassLoader(oldClassLoader)

    Continue
  }

  object Continue extends xsbti.Continue

}
