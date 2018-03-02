(ns certainty-derivative.view)

(defn sort-by-last-name-descending [records]
  (reverse (sort-by :last-name records)))

(defn sort-by-gender-and-last-name-ascending [records]
  (let [score (fn [gender] (if (= gender "f") 1 0))]
    (sort-by (juxt (comp - score :gender) :last-name) records)))

(defn sort-by-date-of-birth-ascending [records]
  (sort-by :date-of-birth records))
