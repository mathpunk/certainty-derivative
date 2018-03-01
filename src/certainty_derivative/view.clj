(ns certainty-derivative.view)

(defn sort-by-last-name-descending [records]
  (reverse (sort-by :last-name records)))
