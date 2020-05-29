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
    
    def getNotificationMessage (buildData) {
        def message = []
        message.add("[${buildData.fullProjectName} #${buildData.number}](${buildData.absoluteUrl})")
        message.add("${buildData.result == 'SUCCESS' ? '✅' : '❌'} ${buildData.result}")
        def changeLog = getChangelog(buildData)
        message.add(changeLog.length() > 0 ? changeLog : getBuildTriggerCause(buildData))

        return message.join("\n")
    }
}
