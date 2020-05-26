#!/usr/bin/env groovy
import org.jenkinsci.plugins.workflow.cps.RunWrapperBinder

def getCause(RunWrapperBinder buildData) {
    "Launched manually by ${buildData.getBuildCauses('hudson.model.Cause$UserIdCause')[0].userName}"
}
