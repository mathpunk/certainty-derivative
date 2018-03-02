(ns certainty-derivative.reader
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.spec.alpha :as s]
            [certainty-derivative.transformer :as xform]
            [java-time :as time]))

(s/def :certainty-derivative.input/row
  (s/keys :req-un [::last-name ::first-name ::gender
                   ::favorite-color ::date-of-birth]))

(defn read-file [filename]
  (let [acc (transient [])]
    (with-open [r (clojure.java.io/reader filename)]
      (doseq [line (line-seq r)]
        (conj! acc (xform/parse-row line))))
    (persistent! acc)))

(defn read-files [& filenames]
  (mapcat read-file filenames))
