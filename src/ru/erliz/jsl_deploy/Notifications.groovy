package ru.erliz.jsl_deploy

class Notifications implements Serializable {
    def getBuildTriggerCause (buildData) {
        if (buildData.getBuildCauses('hudson.model.Cause$UserIdCause').size() > 0) {
            def name = buildData.getBuildCauses('hudson.model.Cause$UserIdCause')[0].userName
    
            return "Launched manually by ${name}"
        }

        return "Launched on push by ${getLastChangeAuthorName(buildData)}"
    }
    
    def getLastChangeAuthorName (buildData) {
        return buildData.changeSets.size() > 0 ? buildData.changeSets.first().first().committer : 'unknown'
    }
    
    def getChangelog (buildData) {
        def resultLines = []
        def passedBuilds = appendFailedBeforeBuilds([buildData], buildData.getPreviousBuild())
        passedBuilds.each { build ->
            build.changeSets.each { changeSet -> 
                def setLines = []
                changeSet.each { entry ->
                    // todo change some day to entry.getAuthor().getDisplayName()
                    setLines.add("${entry.committer}: ${entry.getMsg()}")
                }
                resultLines += setLines.reverse()
            }
        }

        return resultLines.join("\n")
    }
    
    def getNotificationMessage (buildData, url) {
        def failureIcon = "\u274C"
        def successIcon = "\u2705"
        def statusIcon = buildData.result == 'SUCCESS' ? successIcon : failureIcon
        def message = []
        message.add("<${url}|${buildData.fullProjectName} #${buildData.number}>")
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

    def appendFailedBeforeBuilds(passedBuilds, build) {
        if ((build != null) && (build.result != 'SUCCESS')) {
            passedBuilds.add(build)
            return appendFailedBeforeBuilds(passedBuilds, build.getPreviousBuild())
        }
        return passedBuilds
    }
}
