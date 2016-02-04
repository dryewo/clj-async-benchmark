# clj-async-benchmark

Benchmarks of core.async performance.
When launched from the command line, runs a ring benchmark with specified parameters.

## Usage

1. Install [Leiningen](http://leiningen.org/#install)
2. Clone the repo

    $ lein uberjar
    $ java -jar target/async-benchmark.jar <MODE> <ACTORS> <OPERATIONS>

## Options

* <MODE> - either `threads` or `go-blocks`.
* <ACTORS> - number of actors in the ring. Should be 2 or greated.
* <OPERATIONS> - number of passes before returning.

## Examples

```
# Takes ~14s on 2015 Macbook Pro 13
$ java -jar target/async-benchmark.jar go-blocks 10000 10000000
```

## License

Copyright © 2016 Dmitrii Balakhonskii

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
