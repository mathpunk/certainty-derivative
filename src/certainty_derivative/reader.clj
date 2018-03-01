(ns certainty-derivative.reader
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.spec.alpha :as s]))

(defn detect-delimiter
  "Using the assumption that delimiter characters do not appear in the data
  values themselves."
  [input-row]
  (cond (string/includes? input-row "|") :pipe
        (string/includes? input-row ",") :comma
        :else :space))

(defmulti tokenize-row detect-delimiter)

(defmethod tokenize-row :space
  [input-row]
  (string/split input-row #" "))

(defmethod tokenize-row :comma
  [input-row]
  (string/split input-row #", "))

(defmethod tokenize-row :pipe
  [input-row]
  (string/split input-row #" \| "))

;; NB: It seems like there's probably a way to compress these methods into one
;; method with a regex with an 'or' expression. The first solution I tried in
;; that manner didn't work like I expected, hence the above. It seems clear
;; enough if a little redundant.

(defn parse-row [input-row]
  (let [fields (tokenize-row input-row)
        column-names [:last-name :first-name :gender
                      :favorite-color :date-of-birth]]
    (zipmap column-names fields)))

(s/def :certainty-derivative.input/row
  (s/keys :req-un [::last-name ::first-name ::gender
                   ::favorite-color ::date-of-birth]))

(defn read-file [filename]
  (let [acc (transient [])]
    (with-open [r (clojure.java.io/reader filename)]
      (doseq [line (line-seq r)]
        (conj! acc (parse-row line))))
    (persistent! acc)))

(defn read-files [& filenames]
  (mapcat read-file filenames))
