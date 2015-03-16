(defproject finagle "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :repositories [["twitter" "http://maven.twttr.com/"]]
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dev-dependencies [[lein-swank "1.4.3"]]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [com.twitter/finagle-core   "6.6.2"]
                 [com.twitter/finagle-thrift "6.6.2"
                   :exclusions [[org.apache.thrift/libthrift]]]
                 [org.apache.thrift/libthrift "0.9.2"]
                 ])
