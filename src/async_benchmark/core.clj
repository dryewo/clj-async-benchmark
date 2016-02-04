(ns async-benchmark.core
  (:require [clojure.core.async :refer [chan thread >!! <!! go <! >! go-loop close!]]
            [async-benchmark.ring])
  (:gen-class))

(defn -main
  "Run ring benchmark with the provided parameters"
  [mode actors operations]
  (let [f (case mode
            "threads" async-benchmark.ring/run-ring
            "go-blocks" async-benchmark.ring/run-ring-go)]
    (if f
      (time (f (bigint actors) (bigint operations)))
      (println "Supported modes: threads, go-blocks"))))
