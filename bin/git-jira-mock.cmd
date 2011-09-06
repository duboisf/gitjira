::#!
@echo off
call scala -savecompiled -classpath "~drp0\gitjira.jar;~drp0\scarg.jar" %0 %*
goto :eof
::!#
Console.println("Hello, world!")
argv.toList foreach Console.println
