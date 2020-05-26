#!/usr/bin/env groovy
import org.jenkinsci.plugins.workflow.cps.RunWrapperBinder

def call(RunWrapperBinder buildData) {
    return "Launched manually by ${buildData.getBuildCauses('hudson.model.Cause$UserIdCause')[0].userName}"
}
