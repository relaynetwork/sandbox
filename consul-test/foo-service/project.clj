(defproject foo-service "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-swank "1.4.5"]]
  :main foo-service.core
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [tolitius/envoy "0.1.2"]
                 [http-kit "2.2.0"]
                 [ring/ring-jetty-adapter "1.6.1"]
                 [ring/ring-json "0.4.0"]
                 [webster "0.1.0-SNAPSHOT"]
                 [services-client "0.1.1-SNAPSHOT"]])
