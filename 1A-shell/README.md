# PA1A-shell

## Authors

Written by Rebecca Turner and Lin-ye Kaye.

## Compiling

Maven (3.5.4) is preferred but a lack of complex dependencies means that
`javac` should be fine. However, Maven provides easy Javadoc generation,
executable `.jar` building, source zipping, and so on.

In the simplest case, `mvn compile` and `mvn test` should be sufficient. Output
is written to the `target` directory.

Quick Maven reference:

    Task                                 | Maven phase (e.g. `mvn PHASE`)
    ----                                 | -----------
    Compile                              | `compile`
    Run JUnit 5 tests                    | `test` or `surefire:test`
    Build an executable `.jar`           | `package` or `jar:jar`
    Create a source `.zip` and `.tar.gz` | `package` or `assembly:single`
    Generate Javadocs                    | `package` or `javadoc:javadoc`

## Sources

    Directory tree                         | Files
    --------------                         | -----
    src                                    |
    ├───main/java/cs131/pa1                |
    │                   ├───command        | Implementations for commands like `cat`
    │                   └───filter         | Provided sources
    │                       └───sequential | Provided sources
    └───test                               |
        ├───java/cs131/pa1/test            | Provided tests
        │                  └───command     | Command tests
        └───resources                      | Test data
