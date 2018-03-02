(ns certainty-derivative.transformer
  (:require [java-time :as time]
            [clojure.string :as string]))

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

(defn string->date [s]
  (->> (string/split s #"-")
       (map #(Integer. %))
       (apply time/local-date)))

(defn parse-row [input-row]
  (let [fields (tokenize-row input-row)
        column-names [:last-name :first-name :gender
                      :favorite-color :date-of-birth]
        string-record (zipmap column-names fields)]
    (update-in string-record [:date-of-birth] string->date)))
