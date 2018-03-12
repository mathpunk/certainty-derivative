(ns certainty-derivative.viewer.render
  (:require [certainty-derivative.viewer.format :as format]))


(defn out [s]
  (println s))

(defn render [records]
  (doseq [record records]
    (-> record
        format/friendly-format
        out)))
