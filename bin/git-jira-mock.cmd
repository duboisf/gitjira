::#!
@echo off
call scala -savecompiled -classpath "~drp0\gitjira.jar;~drp0\scarg.jar" %0 %*
goto :eof
::!#
(new gitjira.GitJiraMain with gitjira.DummyJiraProvider) main args
