# rebeccaturner-and-linyekaye-PA2A-sync

## Compiling

Sources are kept in `src/main/java`, tests are kept in `src/test/java`.

Maven (3.5.4) is preferred but a lack of complex dependencies means that
`javac` should be fine. However, Maven provides easy Javadoc generation,
executable `.jar` building, source zipping, and so on.

In the simplest case, `mvn compile` and `mvn test` should be sufficient.

Quick Maven reference:

    Task                                 | Maven phase (e.g. `mvn PHASE`)
    ----                                 | -----------
    Compile                              | `compile`
    Run JUnit 5 tests                    | `test` or `surefire:test`
    Build an executable `.jar`           | `package` or `jar:jar`
    Create a source `.zip` and `.tar.gz` | `package` or `assembly:single`
    Generate Javadocs                    | `package` or `javadoc:javadoc`

## Implementation

### Preemptive

To make a vehicle pull over:

1. lock its lock, so it can't immediately restart
2. Call its interrupt method, which delegates to `thread.interrupt()`
3. When you are ready to restart it, release its lock
