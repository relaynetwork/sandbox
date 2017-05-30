(ns schema-coercion.core
  (:require
   [clojure.string :as str]
   [schema.core :as s]
   [schema.coerce :as coerce]))

(defn is-alpha-and-dashed? [x]
  (re-matches #"[a-zA-Z0-9-]+" (name x)))

(defn is-alpha-and-underscored? [x]
  (re-matches #"[a-zA-Z0-9_]+" (name x)))

(def ApiParam (s/both s/Keyword (s/pred is-alpha-and-dashed?)))
(def ApiRawParam (s/both s/Keyword (s/pred is-alpha-and-underscored?)))

(s/check ApiParam :foo_bar)
(s/check ApiParam :foo-bar)

(def MessageApiRawSchema {ApiRawParam s/Any})
(def MessageApiSchema {ApiParam s/Any})

(def ecate (atom []))

(defn thing [x]
  (reduce (fn [acc [k v]]
            (let [new-k (-> k name (str/replace "_" "-") keyword)]
              (swap! ecate conj {:old-k k
                                 :new-k new-k})
              (assoc acc
                new-k
                v)))
          {}
          x))

(def inconvertible-keys #{:no_change_map :no_change_vec})


(comment
  (def ReqSchema {:journey_id s/Str :recipients RecipientsMap})

  (def DoNotConvert {s/Any s/Any})

  (def ReqSchema {:journey_id s/Str :recipients DoNotConvert})
)

(defn- normalize-keys [target]
  (cond
    (map? target)
    (into {}
          (for [[k v] target]
            (if-not (inconvertible-keys inconvertible-keys k)
              [(-> k name (str/replace #"_" "-") keyword) (normalize-keys v)]
              [(-> k name (str/replace #"_" "-") keyword) v])))

    (vector? target)
    (vec (map normalize-keys target))

    :else
    target))

(comment
  (let [data {:some_key      "123"
              :another_key   "abc"
              :change_map    {:inner_key1 "val1"
                              :inner_key2 "val2"
                              :inner_key3 "val3"}
              :no_change_map {:inner_key4 "val4"
                              :inner_key5 "val5"
                              :inner_key6 "val6"}
              :change_vec    [{:inner_inner_key1 "val1"}
                              {:inner_inner_key2 "val2"}]
              :no_change_vec [{:inner_inner_key3 "val3"}
                              {:inner_inner_key4 "val4"}]
              :foo_bar       "banana"
              :bar_foo       "hammock"}
        matcher (coerce/coercer MessageApiSchema {MessageApiSchema normalize-keys})]
    (if (s/check MessageApiRawSchema data)
      {:status  400
       :message "fuck you"}
      (matcher data)))

  {:some-key      "123"
   :another-key   "abc"
   :change-map    {:inner-key1 "val1"
                   :inner-key2 "val2"
                   :inner-key3 "val3"}
   :no-change-map {:inner_key4 "val4"
                   :inner_key5 "val5"
                   :inner_key6 "val6"}
   :change-vec    [{:inner-inner-key1 "val1"}
                   {:inner-inner-key2 "val2"}]
   :no-change-vec [{:inner_inner_key3 "val3"}
                   {:inner_inner_key4 "val4"}]
   :foo-bar       "banana"
   :bar-foo       "hammock"}

  (s/check {ApiRawParam s/Str} {:cc_id "123"})
  )