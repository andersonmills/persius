(ns persius.core
  (:require [clojure.java.jdbc :as jdbc]
            [honeysql.core :as sql]
            [honeysql.helpers :as helpers])
  (:gen-class))

(let  [db-host  "localhost"
       db-port 5432
       db-name  "persius"]

  (def db  {:classname  "org.postgresql.Driver" ; must be in classpath
            :subprotocol  "postgresql"
            :subname  (str  "//" db-host  ":" db-port  "/" db-name)
            ; Any additional keys are passed to the driver
            ; as driver-specific properties.
            :user "persius"
            :password "persius"}))

(defn create-chord-progressions
  [chords]
  (let
    [query-map (-> (helpers/insert-into :chord_progressions)
                   (helpers/columns :chord_progression)
                   (helpers/values (map list chords))
                   sql/format)]
    ;(println query-map)
    (try
      (jdbc/query db query-map)
      (catch Exception e
        (prn "caught" (.getMessage e))))))

(defn read-chord-progressions
  []
  (let
    [query-map (-> (sql/build :select :*
                              :from :chord_progressions ); :limit :33)
                   sql/format)
     read-chords (jdbc/query db query-map)]
    read-chords))

(defn print-chords
  [response-chords]
  (doseq
      [item response-chords]
      (println (:chord_id item) " " (:chord_progression item))))

(defn update-chords
  [chords]
  (let [a 1
        bad-chord-rows (filter #(re-matches #"^\d+$"  (:chord_progression %)) chords)
        bad-chords (reduce #(conj %1 (:chord_progression %2)) '() bad-chord-rows)
        query-map (-> (helpers/update :chord_progressions)
                      (helpers/sset {:chord_progression "ffff"})
                      (helpers/where [:in :chord_progression bad-chords])
                      (sql/format))
        _ (println query-map)]
    (try
      (jdbc/query db query-map)
      (catch Exception e
        (prn "caught" (.getMessage e))))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [a 1
        chords (take 16r99 (repeatedly #(format "%x" (+ (rand-int 16refff) 16r1000))))
        _ (println "create the entries")
        _ (create-chord-progressions chords)
        _ (println "read the entries")
        read-chords (read-chord-progressions)
        _ (print-chords read-chords)]
  (println "update the entries")
    (update-chords read-chords)
  (println "delete the entries")))
