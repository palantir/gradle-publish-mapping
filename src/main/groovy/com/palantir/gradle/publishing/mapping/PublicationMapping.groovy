package com.palantir.gradle.publishing.mapping;

public class PublicationMapping {

  Map<String, List> map = [:]

  public void put(String publicationName, String repositoryName) {
    this.put(publicationName, [repositoryName])
  }

  public void put(String publicationName, List<String> repositoryNames) {
    map[publicationName] = repositoryNames
  }

  public void add(String publicationName, String repositoryName) {
    this.add(publicationName, [repositoryName])
  }

  public void add(String publicationName, List<String> repositoryNames) {
    if (map.containsKey(publicationName)) {
      map[publicationName] += repositoryNames
    } else {
      map[publicationName] = repositoryNames
    }
  }

  public boolean isWhitelisted(String publicationName, String repositoryName) {
    return !map.containsKey(publicationName) || map[publicationName].toSet().contains(repositoryName)
  }

  public boolean isBlacklisted(String publicationName, String repositoryName) {
    return map.containsKey(publicationName) && !map[publicationName].toSet().contains(repositoryName)
  }

}
