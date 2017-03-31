(defproject auth-server-example "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins      [[lein-swank "1.4.5"]]
  :main auth-server-example.core
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [com.relaynetwork/service-base "0.1.0"]
                 [ring "1.6.0-RC1" :exclusions [org.clojure/tools.namespace commons-codec org.clojure/java.classpath]]
                 [compojure "1.5.2" :exclusions [commons-codec]]
                 ])
