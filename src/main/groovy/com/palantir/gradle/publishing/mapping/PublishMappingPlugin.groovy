package com.palantir.gradle.publishing.mapping;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.publish.ivy.plugins.IvyPublishPlugin;
import org.gradle.api.publish.ivy.tasks.PublishToIvyRepository;
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin;
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository;

public class PublishMappingPlugin implements Plugin<Project> {

  @Override
  public void apply(Project project) {
    project.plugins.apply('publishing')
    project.tasks.publish.extensions.mapping = new PublicationMapping()
    project.tasks.create(PrintPublishTasksTask.NAME, PrintPublishTasksTask)
    if (project.plugins.hasPlugin(IvyPublishPlugin)) {
      project.tasks.withType(PublishToIvyRepository).whenTaskAdded { t ->
        if (project.tasks.publish.mapping.isBlacklisted(t.publication.name, t.repository.name)) {
          t.enabled = false
        }
      }
    }
    if (project.plugins.hasPlugin(MavenPublishPlugin)) {
      project.tasks.withType(PublishToMavenRepository).whenTaskAdded { t ->
        if (project.tasks.publish.mapping.isBlacklisted(t.publication.name, t.repository.name)) {
          t.enabled = false
        }
      }
    }
  }
}
