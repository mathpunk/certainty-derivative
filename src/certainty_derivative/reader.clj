(ns certainty-derivative.reader
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.spec.alpha :as s]))

(defn detect-delimiter
  "Using the assumption that delimiter characters do not appear in the data values themselves."
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

(defn parse-row [input-row]
  (let [fields (tokenize-row input-row)
        column-names [:last-name :first-name :gender :favorite-color :date-of-birth]]
    (zipmap column-names fields)))

(s/def :certainty-derivative.input/row
  (s/keys :req-un [::last-name ::first-name ::gender ::favorite-color ::date-of-birth]))
