(ns certainty-derivative.loader.read
  (:require [certainty-derivative.loader.transform :as xform]
            [clojure.java.io :as io]))


(defn shove-line [accumulator input-row]
  (try
    (conj! accumulator (xform/parse-row input-row))
    (catch NumberFormatException e
      (throw (ex-info "NumberFormatException in line:"
                      {:line input-row})))))

(defn read-file [filename]
  (let [acc (transient [])]
    (with-open [r (clojure.java.io/reader filename)]
      (doseq [line (line-seq r)]
        (shove-line acc line)))
    (persistent! acc)))

(defn read-files [& filenames]
  (mapcat read-file filenames))
