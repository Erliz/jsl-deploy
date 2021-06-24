package ru.erliz.jsl_deploy

class Notifications implements Serializable {
    def getBuildTriggerCause (buildData) {
        return "Launched manually by ${buildData.getBuildCauses('hudson.model.Cause$UserIdCause')[0].userName}"
    }
    
    def getChangelog (buildData) {
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
    
    def getNotificationMessage (buildData, url) {
        def failureIcon = "\u274C"
        def successIcon = "\u2705"
        def statusIcon = buildData.result == 'SUCCESS' ? successIcon : failureIcon
        def message = []
        message.add("[${buildData.fullProjectName} #${buildData.number}](${url})")
        message.add("${statusIcon} ${buildData.result}")
        def changeLog
        try {
            changeLog = getChangelog(buildData)
        } catch(err) {
            changeLog = "Unable to get changelog: ${err}"
        }
        message.add(changeLog.length() > 0 ? changeLog : getBuildTriggerCause(buildData))

        return message.join("\n")
    }
}
