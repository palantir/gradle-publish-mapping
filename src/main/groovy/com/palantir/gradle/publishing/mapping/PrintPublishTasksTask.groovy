package com.palantir.gradle.publishing.mapping;

import com.google.common.base.Strings;
import org.gradle.api.publish.plugins.PublishingPlugin;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.DefaultTask;

public class PrintPublishTasksTask extends DefaultTask {

  public static final String NAME = 'printPublishTasks'

  public PrintPublishTasksTask() {
    setGroup(PublishingPlugin.PUBLISH_TASK_GROUP)
    setDescription("Prints out all included and skipped publish tasks.")
  }

  @TaskAction
  public void printPublishTasks() {
    def blacklist = []
    def whitelist = []
    project.tasks.publish.taskDependencies.getDependencies().each { t ->
      if (t.enabled) {
        whitelist.add(t)
      } else {
        blacklist.add(t)
      }
    }
    def whitelistTitle = "Publish tasks run by 'publish':"
    project.logger.quiet(whitelistTitle)
    project.logger.quiet(Strings.repeat('-', whitelistTitle.length()))
    whitelist.each { t ->
      project.logger.quiet(t.name)
    }
    project.logger.quiet("")
    def blacklistTitle = "Publish tasks skipped by 'publish':"
    project.logger.quiet(blacklistTitle)
    project.logger.quiet(Strings.repeat('-', blacklistTitle.length()))
    blacklist.each { t ->
      project.logger.quiet(t.name)
    }
  }

}
