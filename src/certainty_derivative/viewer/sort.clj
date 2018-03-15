(ns certainty-derivative.viewer.sort)

(defn by-last-name [records]
  (sort-by :certainty-derivative.record/last-name records))

(defn by-gender-and-last-name [records]
  (let [score (fn [gender] (if (= gender "f") 1 0))]
    (sort-by (juxt (comp - score :certainty-derivative.record/gender)
                   :certainty-derivative.record/last-name)
             records)))

(defn by-date-of-birth [records]
  (sort-by :certainty-derivative.record/date-of-birth records))
