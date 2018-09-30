# PA1A-shell

## Notes

This project is written using Java 10 and must be compiled with a JDK
10-compatible compiler; this is both for the new local variable type inference
syntax (e.g. `var expect = List.of(10, 11);`) and for several factory and
collector methods (e.g. `Collectors.toUnmodifiableList()`). Dimos said that all
the imports are OK!

## Authors

Written by Rebecca Turner (rebeccaturner@brandeis.edu) and Lin-ye Kaye
(linyekaye@brandeis.edu).

A general who-did-what table, although architectural decisions were made — and
much code was written — in tandem:

    Component                | Author
    ---------                | ------
    Arguments                | Rebecca
    CommandNotFoundFilter    | Rebecca
    Commands                 | Rebecca
    ExitFilter               | Lin-ye
    HelpFilter               | Lin-ye
    GrepFilter               | Lin-ye
    UniqFilter               | Lin-ye
    WcFilter                 | Lin-ye
    CatFilter                | Rebecca
    CdFilter                 | Rebecca
    LsFilter                 | Rebecca
    PwdFilter                | Rebecca
    RedirectFilter           | Rebecca
    CollectionFilter         | Rebecca
    EmptyFilter              | Rebecca
    GoodSequentialFilter     | Rebecca
    OutputStreamFilter       | Rebecca
    SequentialCommandBuilder | Rebecca
    SequentialFilterChain    | Rebecca
    SequentialInputFilter    | Lin-ye
    SequentialOutputFilter   | Lin-ye
    SequentialREPL           | Lin-ye
    WorkingDirectory         | Rebecca

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
