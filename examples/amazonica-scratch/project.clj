(defproject amazonica-scratch "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main ^:skip-aot amazonica-scratch.core
  :dependencies [
    [org.clojure/clojure       "1.6.0"]
    [cider/cider-nrepl         "0.7.0"]
    [amazonica                 "0.3.22"]
    [org.clojure/tools.nrepl   "0.2.3"]
    [org.clojure/tools.logging "0.3.0"]
    [org.clojure/data.json     "0.2.5"]
    [prismatic/schema          "0.3.1"]
  ])
