#!/bin/sh
dir=$(dirname `readlink $0`)
exec scala -savecompiled -classpath "$dir/scarg.jar:$dir/gitjira.jar" $0 $@
!#
import gitjira._
(new GitJiraMain with DummyJiraProvider) main args
