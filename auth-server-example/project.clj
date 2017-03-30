(defproject auth-server-example "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins      [[lein-swank "1.4.5"]]
  :main auth-server-example.core
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.eclipse.jetty/jetty-server "9.4.3.v20170317"]
                 [ring "1.6.0-RC1"]
                 [ring/ring-json "0.4.0"]
                 [compojure "1.5.2"]
                 [com.taoensso/carmine "2.16.0"]
                 [cheshire "5.7.0"]
                 [buddy "1.3.0"]])
