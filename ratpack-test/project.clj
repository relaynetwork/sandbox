(defproject ratpack-test "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :javac-options ["-target" "1.8" "-source" "1.8" "-Xlint:-options"]
  :dev-dependencies [[lein-swank "1.4.5"]]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [io.ratpack/ratpack-core "0.9.15"]
                ])
