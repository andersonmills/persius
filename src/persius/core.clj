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
  [codes]
  (let 
    [query-map (-> (helpers/insert-into :chord_progressions)
                   (helpers/columns :chord_progression)
                   (helpers/values (map list codes))
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
                              :from :chord_progressions)
                   sql/format)]
    (println query-map)
    (jdbc/query db query-map)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [a 1
        codes (take 16r99 (repeatedly #(format "%x" (+ (rand-int 16refff) 16r1000))))]
  (println "create the entries")
  (create-chord-progressions codes)
  (println "read the entries")
  (read-chord-progressions)
  (println "update the entries")
  (println "delete the entries")
  (println codes)))
