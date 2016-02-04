(ns async-benchmark.misc
  (:require [clojure.core.async :refer [chan thread >!! <!! go <! >! go-loop close!]]))

(defn ping1 []
  (let [ch (chan)]
    (thread
      (>!! ch "ping"))
    (println (<!! ch))))

(defn actor [ch-in ch-out n msg]
  (thread
    (dotimes [_ n]
      (println (<!! ch-in))
      (>!! ch-out msg))))

(defn ping2 []
  (let [ch (chan)]
    (actor ch ch 5 "ping")
    (actor ch ch 5 "    pong")
    (>!! ch "serve")))

(defn chain-node [ch-in]
  (let [ch-out (chan)]
    (thread
      (let [v (<!! ch-in)]
        ;(println (thread-id) v)
        (>!! ch-out (inc v))))
    ch-out))

(defn chain-node-go [ch-in]
  (let [ch-out (chan)]
    (go
      (let [v (<! ch-in)]
        ;(println (thread-id) v)
        (>! ch-out (inc v))))
    ch-out))

(defn chain [n]
  (let [first-ch-in (chan)
        last-ch-out (last (take n (iterate chain-node first-ch-in)))
        _           (>!! first-ch-in 1)
        res         (<!! last-ch-out)]
    ;(println (thread-id) res)
    res))

(comment

  (ping1)
  (ping2)
  (time (chain 1000))
  (with-redefs [chain-node chain-node-go]
    (time (chain 100000)))

  )
