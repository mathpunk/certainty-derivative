(ns certainty-derivative.viewer
  (:require [java-time :as time]))

;; Sorting
;; ===============
(defn sort-by-last-name-descending [records]
  (reverse (sort-by :certainty-derivative.record/last-name records)))

(defn sort-by-gender-and-last-name-ascending [records]
  (let [score (fn [gender] (if (= gender "f") 1 0))]
    (sort-by (juxt (comp - score :certainty-derivative.record/gender) :certainty-derivative.record/last-name) records)))

(defn sort-by-date-of-birth-ascending [records]
  (sort-by :certainty-derivative.record/date-of-birth records))


;; String formatting
;; ======================
(defn friendly-name [record]
  (str (record :certainty-derivative.record/first-name)
       " "
       (record :certainty-derivative.record/last-name)))

(defn friendly-gender [record]
  (get {"m" " (male)"
        "f" " (female)"
        "Non-binary / third gender" " (NB/3rd)"}
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
