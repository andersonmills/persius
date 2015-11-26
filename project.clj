(defproject persius "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [honeysql "0.6.2"]
                 [org.clojure/java.jdbc "0.4.1"]
                 [migratus "0.8.7"]
                 [org.slf4j/slf4j-log4j12 "1.7.9"]
                 [org.postgresql/postgresql "9.4-1201-jdbc41"]]
  :plugins [[migratus-lein "0.2.0"]]
  :migratus {:store :database
             :migration-dir "migrations"
             :db {:classname "com.postgresql.jdbc.Driver"
                  :subprotocol "postgresql"
                  :subname "//localhost/persius"
                  :user "persius"
                  :password "persius"}}
  :main ^:skip-aot persius.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
