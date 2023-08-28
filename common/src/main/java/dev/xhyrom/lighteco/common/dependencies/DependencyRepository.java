package dev.xhyrom.lighteco.common.dependencies;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

public enum DependencyRepository {
    MAVEN_CENTRAL("https://repo1.maven.org/maven2/");

    private final String url;

    DependencyRepository(String url) {
        this.url = url;
    }

    private URLConnection openConnection(Dependency dependency) throws IOException {
        URL dependencyUrl = new URL(this.url + dependency.getFullPath());
        return dependencyUrl.openConnection();
    }

    private byte[] download(Dependency dependency) throws IOException {
        URLConnection connection = this.openConnection(dependency);
        connection.connect();

        return connection.getInputStream().readAllBytes();
    }

    public void download(Dependency dependency, Path file) throws IOException {
        Files.write(file, this.download(dependency));
    }
}
