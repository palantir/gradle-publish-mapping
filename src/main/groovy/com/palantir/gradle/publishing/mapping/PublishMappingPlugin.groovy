// Copyright 2015 Palantir Technologies
//
// Licensed under the Apache License, Version 2.0 (the "License")
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.palantir.gradle.publishing.mapping;

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.ivy.plugins.IvyPublishPlugin
import org.gradle.api.publish.ivy.tasks.PublishToIvyRepository
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository

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
