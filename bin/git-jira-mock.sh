#!/bin/sh
dir=$(dirname `readlink $0`)
exec scala -savecompiled -classpath "$dir/scarg.jar:$dir/gitjira.jar" $0 $@
!#
(new gitjira.GitJiraMain with gitjira.DummyJiraProvider) main args
