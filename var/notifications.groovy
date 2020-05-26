import org.jenkinsci.plugins.workflow.cps.RunWrapperBinder

def getCause(RunWrapperBinder buildData) {
    "Launched manually by ${buildData.getBuildCauses('hudson.model.Cause$UserIdCause')[0].userName}"
}

def getChangelog(RunWrapperBinder buildData) {
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

def getNotificationMessage(RunWrapperBinder buildData) {
    def message = []
    message.add("[${buildData.fullProjectName} #${buildData.number}](${buildData.absoluteUrl})")
    message.add(currentBuild.result)
    def changeLog = getChangelog(currentBuild)
    message.add(changeLog.length() > 0 ? changeLog : getCause(buildData))

    return message.join("\n")
}
