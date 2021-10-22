package ru.erliz.jsl_deploy

class Notifications implements Serializable {
    def getBuildTriggerCause (buildData) {
        def userName = 'unknown'
        if (buildData.getBuildCauses('hudson.model.Cause$UserIdCause').size() > 0) {
            userName = buildData.getBuildCauses('hudson.model.Cause$UserIdCause')[0].userName
        }
        return "Launched manually by ${userName}"
    }
    
    def getChangelog (buildData) {
        def resultLines = []
        buildData.changeSets.each { k, sets ->
            sets.each { sk, entry ->
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
        message.add("<${buildData.fullProjectName} #${buildData.number}|${url}>")
        message.add("${statusIcon} ${buildData.result}")
        def changeLog = "Empty change log"
        try {
            changeLog = getChangelog(buildData)
        } catch(err) {
            changeLog = "Unable to get changelog: ${err}"
        }
        message.add(changeLog)
        message.add(getBuildTriggerCause(buildData))

        return message.join("\n")
    }
}
