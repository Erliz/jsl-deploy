package ru.erliz.jsl_deploy

import org.jenkinsci.plugins.workflow.cps.RunWrapperBinder
import java.lang.String

class Notifications implements Serializable {
    String getBuildTriggerCause (RunWrapperBinder buildData) {
        return "Launched manually by ${buildData.getBuildCauses('hudson.model.Cause$UserIdCause')[0].userName}"
    }
    
    String getChangelog (RunWrapperBinder buildData) {
        def resultLines = []
        def changeLogSets = buildData.changeSets
        for (int i = 0; i < changeLogSets.size(); i++) {
            def entries = changeLogSets[i].items
            for (int j = 0; j < entries.length; j++) {
                def entry = entries[j]
                resultLines.add("${entry.author}: ${entry.msg}")
            }
        }

        return resultLines.join("\n")
    }
    
    String getNotificationMessage (RunWrapperBinder buildData) {
        def message = []
        message.add("[${buildData.fullProjectName} #${buildData.number}](${buildData.absoluteUrl})")
        message.add(currentBuild.result)
        def changeLog = getChangelog(currentBuild)
        message.add(changeLog.length() > 0 ? changeLog : getCause(buildData))

        return message.join("\n")
    }
}
