(ns certainty-derivative.viewer.sort)

(defn sort-by-last-name-descending [records]
  (reverse (sort-by :certainty-derivative.record/last-name records)))

(defn sort-by-gender-and-last-name-ascending [records]
  (let [score (fn [gender] (if (= gender "f") 1 0))]
    (sort-by (juxt (comp - score :certainty-derivative.record/gender)
                   :certainty-derivative.record/last-name)
             records)))

(defn sort-by-date-of-birth-ascending [records]
  (sort-by :certainty-derivative.record/date-of-birth records))

(defn dispatch-sort [options records]
  (let [strategy (get-in options [:options :sort])]
    (case strategy
      "women" (sort-by-gender-and-last-name-ascending records)
      "lname" (sort-by-last-name-descending records)
      "dob" (sort-by-date-of-birth-ascending records))))
