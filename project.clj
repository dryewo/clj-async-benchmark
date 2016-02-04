(defproject async-benchmark "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/core.async "0.2.374"]
                 [prismatic/schema "1.0.3"]
                 [midje "1.8.2"]]
  :main ^:skip-aot async-benchmark.core
  :target-path "target"
  :uberjar-name "async-benchmark.jar"
  :profiles {:uberjar {:aot :all}
             :dev     {:source-paths ["dev"]
                       :repl-options {:init-ns user}
                       :plugins      [[lein-midje "3.2"]]
                       :dependencies [[org.clojure/tools.namespace "0.2.11"]
                                      [org.clojure/java.classpath "0.2.2"]
                                      [criterium "0.4.3"]]}})
