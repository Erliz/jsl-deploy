#!/usr/bin/env groovy
import org.jenkinsci.plugins.workflow.cps.RunWrapperBinder

def getNotificationMessage(RunWrapperBinder buildData) {
    def message = []
    message.add("[${buildData.fullProjectName} #${buildData.number}](${buildData.absoluteUrl})")
    message.add(currentBuild.result)
    def changeLog = getChangelog(currentBuild)
    message.add(changeLog.length() > 0 ? changeLog : getCause(buildData))

    return message.join("\n")
}
