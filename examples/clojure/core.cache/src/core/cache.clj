(ns core.cache
  (:require
   [clojure.core.cache :as cache :refer [defcache]]))

;; the goal with this example is to explore creating a cache with the
;; following properties:

;; * Least Recently Used are Evicted first
;; * Entries can exist in the cache for a maximum amount of time
;; * If the primary source is off-line, a secondary source can be used as a fall-back or the stale value should be returned
;; * is ideal if the caches can be flushed (eg: via an irmgard signal)

(defcache LRUWithTTLCache [initial-entries queue max-entries ttl])
