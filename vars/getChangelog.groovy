#!/usr/bin/env groovy
import org.jenkinsci.plugins.workflow.cps.RunWrapperBinder

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
