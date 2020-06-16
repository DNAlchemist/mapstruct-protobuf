# mapstruct-protobuf

Plugin for automatic exclusion of generated protobuf fields and methods.

## Installation

### Gradle

```annotationProcessor "one.chest:mapstruct-protobuf:0.0.5"```

### Maven

```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-compiler-plugin</artifactId>
      <version>3.6.1</version>
      <configuration>
        <annotationProcessorPaths>
          <path>
            <groupId>one.chest</groupId>
            <artifactId>mapstruct-protobuf</artifactId>
            <version>0.0.5</version>
          </path>
        </annotationProcessorPaths>
      </configuration>
    </plugin>
  </plugins>
</build>
```

