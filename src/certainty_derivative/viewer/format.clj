(ns certainty-derivative.viewer.format
  (:require certainty-derivative.record
            [clojure.set :as set]
            [java-time :as time]))

(defn friendly-name [record]
  (str (record :certainty-derivative.record/first-name)
       " "
       (record :certainty-derivative.record/last-name)))

(defn friendly-gender [record]
  (get {"m" " (male)"
        "f" " (female)"
        "nb/3rd" " (NB/3rd)"}
       (record :certainty-derivative.record/gender) ""))

(defn friendly-date-of-birth [record]
  (time/format "MM/dd/yyyy"
               (record :certainty-derivative.record/date-of-birth)))

(defn friendly-format
  "Transform a record into a human-readable statement."
  [record]
  (let [pronoun (get {"m" "His"
                      "f" "Her"}
                     (record :certainty-derivative.record/gender)
                     "Their")]
    (str (friendly-name record)
         (friendly-gender record)
         ", born "
         (friendly-date-of-birth record)
         ". "
         pronoun
         " favorite color is "
         (record :certainty-derivative.record/favorite-color)
         ".")))


(def fully-qualified-keys
  {:last-name :certainty-derivative.record/last-name
   :first-name :certainty-derivative.record/first-name
   :gender :certainty-derivative.record/gender
   :favorite-color :certainty-derivative.record/favorite-color
   :date-of-birth :certainty-derivative.record/date-of-birth})

(defn json-format [record]
  (-> record
      (set/rename-keys (set/map-invert fully-qualified-keys))
      (update :date-of-birth
              #(time/format "MM/dd/yyyy" %))))
