(ns async-benchmark.ring
  (:require [clojure.core.async :refer [chan thread >!! <!! go <! >! go-loop close!]]))

(defn chan-str [ch]
  (str "chan-" (Integer/toString (.hashCode ch) 16)))

(defn thread-id []
  (.getName (Thread/currentThread)))

(defn ring-node [ch-return goal-value ch-in ch-out]
  ;(apply println "Creating with " (map chan-str [ch-in ch-out]))
  (thread
    (loop []
      (if-let [v (<!! ch-in)]
        (do #_(println (thread-id) v)
          (if (>= v goal-value)
            (do (close! ch-out)
                (>!! ch-return v))
            (>!! ch-out (inc v)))
          (recur))
        (do #_(println "Closing " (chan-str ch-out))
          (close! ch-out))))))

(defn ring-node-go [ch-return goal-value ch-in ch-out]
  ;(apply println "Creating with " (map chan-str [ch-in ch-out]))
  (go-loop []
    (if-let [v (<! ch-in)]
      (do #_(println (thread-id) v)
        (if (>= v goal-value)
          (do (close! ch-out)
              (>! ch-return v))
          (>! ch-out (inc v)))
        (recur))
      (do #_(println "Closing " (chan-str ch-out))
        (close! ch-out)))))

(defn broken-ring
  "Infinite chain of actors, each listening to the previous one"
  [ch-return goal-value first-ch-in]
  (iterate #(let [ch-out (chan)]
             (ring-node ch-return goal-value % ch-out)
             ch-out)
           first-ch-in))

(defn run-ring
  "Creates a ring of threads, each of them (ring-node)"
  [node-count goal-value]
  (let [first-ch-in (chan)]
    (try (let [ch-return    (chan)
               out-channels (broken-ring ch-return goal-value first-ch-in)
               last-ch-out  (nth out-channels node-count)
               ;_            (throw (OutOfMemoryError.))
               ;_            (println "Created a ring, last-ch-out: " (chan-str last-ch-out))
               _            (ring-node ch-return goal-value last-ch-out first-ch-in)
               _            (>!! first-ch-in 1)
               res          (<!! ch-return)]
           ;(println (thread-id) res)
           res)
         ;; To avoid restarting REPL
         (catch OutOfMemoryError e
           (close! first-ch-in)
           (println (str e))))))

(defn run-ring-go [node-count goal-value]
  (with-redefs [ring-node ring-node-go]
    (run-ring node-count goal-value)))

(comment

  ;; With threads
  (time (run-ring 1000 10000))
  ;; With go blocks
  (time (run-ring-go 10000 1000000))

  )
