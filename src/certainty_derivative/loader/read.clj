(ns certainty-derivative.loader.read
  (:require [certainty-derivative.loader.transform :as xform]
            [clojure.java.io :as io]))

(defn read-file [filename]
  (let [acc (transient [])]
    (with-open [r (clojure.java.io/reader filename)]
      (doseq [line (line-seq r)]
        (try 
          (conj! acc (xform/parse-row line))
          (catch NumberFormatException e
            (throw "NumberFormatException while parsing line:"
                   {:line line}))
          )))
    (persistent! acc)))

(defn read-files [& filenames]
  (mapcat read-file filenames))
