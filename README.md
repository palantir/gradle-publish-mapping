# Publish Mapping plugin for gradle [![Build Status](https://travis-ci.org/palantir/gradle-publish-mapping.svg?branch=master)](https://travis-ci.org/palantir/gradle-publish-mapping) [![Download](https://api.bintray.com/packages/palantir/releases/gradle-publish-mapping/images/download.svg) ](https://bintray.com/palantir/releases/gradle-publish-mapping/_latestVersion)

## Why?
We created Publish Mapping because Gradle, by default, publishes all publications to
all publishing repositories.  This may not always be wanted.

## How does it work?
This plugin works by disabling/removing the publish tasks that publish the publications to
the wrong publish repository.  It only understands tasks added by the ``ivy-publish`` and ``maven-publish`` plugins.

## Adding to Gradle

```gradle
buildscript {
	repositories {
		maven {
			url "http://dl.bintray.com/palantir/releases"
		}
		mavenCentral()
	}
	dependencies {
		classpath 'com.palantir:publish-mapping:2.0.0'
	}
}

apply plugin: 'ivy-publish' // or 'maven-publish'
apply plugin: 'publish-mapping'
```

## Usage

The ``publish.mapping`` extension allows the project to specify which repositories (from ``publishing.repositories``) each publication (from ``publishing.publications``)
should be published to.  It does this by disabling the publish tasks that don't match
the publish mapping.  By default if a publication does not have an entry in the mapping,
it will be published to all repositories by the ``publish`` task.

* ``put(String publicationName, List<String> repositoryNames)`` - Set a mapping entry
* ``add(String publicationName, List<String> repositoryNames)`` - Create a new or append to an existing mapping entry

```gradle
publish.mapping {
  put 'PublicationName', ['RepositoryName1', 'RepositoryName2',...] // set a mapping entry
  add 'PublicationName', ['RepositoryName3', ...] // create a new mapping entry or append to an existing one
}
```

The following example shows how to specify that the publication ‘aaa’ should only be published to repositories ‘xxx’ and ‘yyy’, the publication ‘bbb’ should not be published to any repository, and the publication ‘ccc’ should be published to all repositories.

```gradle
publishing {
  repositories {
    ivy {
      name 'xxx'
    }
    ivy {
      name 'yyy'
    }
    ivy {
      name 'zzz'
    }
  }
  publications {
    aaa(IvyPublication)
    bbb(IvyPublication)
    ccc(IvyPublication)
  }
}
publish.mapping {
  put 'aaa', ['xxx', 'yyy'] // 'aaa' is only published to repositories 'xxx' and 'yyy'
  put 'bbb', [] // 'bbb' is not published to any repository
  // by default 'ccc' is published to all repositories
}
```

Run the provided ``printPublishTasks`` task to see the expected behavior:

```gradle
$ ./gradlew printPublishTasks
:printPublishTasks
Publish tasks run by 'publish':
-------------------------------
publishCccPublicationToZzzRepository
publishCccPublicationToYyyRepository
publishAaaPublicationToXxxRepository
publishAaaPublicationToYyyRepository
publishCccPublicationToXxxRepository

Publish tasks skipped by 'publish':
-----------------------------------
publishBbbPublicationToXxxRepository
publishBbbPublicationToYyyRepository
publishAaaPublicationToZzzRepository
publishBbbPublicationToZzzRepository

BUILD SUCCESSFUL
```

# Version Compatibility

Sometimes Gradle changes the way tasks and publishing is implemented.  This table
maps ``publish-mapping`` versions to compatible Gradle versions.

| ``publish-mapping`` version | Gradle version | Notes |
| --------------------------- | -------------- | ----- |
| ``[1.1.0, 2.0.0)`` | ``2.2.1``     | &nbsp; |
| ``[2.0.0, )`` | ``2.4``, ``2.5``, ``2.6`` | &nbsp; |

# LICENSE

Gradle Publish Mapping is released by Palantir Technologies, Inc. under the Apache 2.0 License. see the included LICENSE file for details.
