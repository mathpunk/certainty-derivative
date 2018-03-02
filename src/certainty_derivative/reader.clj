(ns certainty-derivative.reader
  (:require [certainty-derivative.transformer :as xform]
            [clojure.java.io :as io]))

(defn read-file [filename]
  (let [acc (transient [])]
    (with-open [r (clojure.java.io/reader filename)]
      (doseq [line (line-seq r)]
        (conj! acc (xform/parse-row line))))
    (persistent! acc)))

(defn read-files [& filenames]
  (mapcat read-file filenames))
